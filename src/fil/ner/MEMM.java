package fil.ner;

import java.util.ArrayList;
import java.util.List;

import fil.tools.Token;
import fil.tools.TokenType;

public class MEMM {
	
	static final NERTag [] TAGS = NERTag.values();
	static final EFeature [] FEATURES = EFeature.values();
	
	public List<FArticle> farticle;
	public List<FSavedNE> fsavedne;
	public List<FWordShape> fwordshape;	
	public List<FSubstring> fsubstring;
	public List<FRegex> fregex;
	
	List<String> possible_org_abbv;
	List<String> possible_place_abbv;
	
	public MEMM(List<FArticle> article, List<FSavedNE> savedne,
			List<FWordShape> wordshape, List<FSubstring> substring,
			List<FRegex> regex)	{
		this.farticle = article;
		this.fsavedne = savedne;
		this.fwordshape = wordshape;
		this.fsubstring = substring;
		this.fregex = regex;
		
		this.possible_org_abbv = new ArrayList<String>();
		this.possible_place_abbv = new ArrayList<String>();
	}
	
	private FSavedNE searchSavedNE(String s)	{
		for(FSavedNE saved : this.fsavedne)	
			if(saved.getNE().equals(s))
				return saved;
		return null;
	}
	
	private FArticle searchArticle(String s)	{
		for(FArticle a : this.farticle)	
			if(a.getArticle().equalsIgnoreCase(s))
				return a;
		return null;
	}
	
	public List<NERToken> tag(String input)	{
		List<NERToken> tokens = NERToken.nerTokenizer(input);
		
		/* Probability Table */
		double [][][] prob = 
			// token - feature - tag
			new double[tokens.size()][FEATURES.length][TAGS.length];
		// initialize probability
		for(int i = 0; i < tokens.size(); i++)
			for(int f = 0; f < FEATURES.length; f++)
				for(int t = 0; t < TAGS.length; t++)	
					prob[i][f][t] = 0;
		
		// tag date, time, money, and percent
		// using regular expressions
		tokens = this.polishTaggingRegex(tokens);
		
		List<Integer> iarticle_person = new ArrayList<Integer>();
		/* Article based tagging */
		for(int i = 0; i < tokens.size(); i++)	{
			FArticle curr_article = null;
			// find an article
			while(this.searchArticle(tokens.get(i).getLexeme()) == null)	{
				i++;
				if(i >= tokens.size())	break;
			}
			// index out of bounds
			if(i >= tokens.size())	break;
			else	curr_article = this.searchArticle(tokens.get(i).getLexeme());
			
			// i is now the index of the article
			// let ai be the index of the article
			int ai = i;
			
			// proceed to next token
			i++;
			// find the next capitalized token
			while(	i < tokens.size() && 
					!isCapitalized(tokens.get(i)) &&
					!Character.isDigit(tokens.get(i).getLexeme().charAt(0)) &&
					tokens.get(i).getTokenType() == TokenType.WORD &&
					searchArticle(tokens.get(i).getLexeme()) == null )	{
				i++;
			}
			
			// index out of bounds
			if(i >= tokens.size())	break;
			// an article is found
			if(searchArticle(tokens.get(i).getLexeme()) != null)	{
				i = ai;
				continue;
			}
			
			if(curr_article.getArticle().matches("[Ss]i") || 
					curr_article.getArticle().matches("[Nn]i") || 
					curr_article.getArticle().matches("[Kk]ay") ||
					curr_article.getArticle().matches("[Ss]ila") ||
					curr_article.getArticle().matches("[Ss]ina") ||
					curr_article.getArticle().matches("[Kk]ina") ||
					curr_article.getArticle().matches("[Nn]ina") ||
					curr_article.getArticle().matches("[Aa]ni") ||
					curr_article.getArticle().matches("[Kk]ila") )
				iarticle_person.add(ai);
			
			// i is now the index of the capitalized token 
			// and ai is the index of the article
			// ci is now the index of the capitalized token
			int ci = i;
			
			// add prob to the next token according to prev article
			for(List<NERTag> ntags : curr_article.getNextTags())	{	
				prob[i][EFeature.ARTICLE.ordinal()][ntags.get(0).ordinal()] = 1;
			}
			prob[i][EFeature.ARTICLE.ordinal()][NERTag.OTHER.ordinal()] = 1;
			
			/* Maximizing Entropy - Saved Named Entities */
			// for all the saved named entities
			for(FSavedNE saved_ne : this.fsavedne)	{
				String [] ne = saved_ne.getNE().split(" ");
				if( ne.length > tokens.size()-ci )	continue;
				int matches = 0;
				// count the number of matches
				for(int j = 0; j < ne.length; j++)	{
					if(ne[j].equals(tokens.get(ci+j).getLexeme()))	{
						matches++;
					}
				}
				// match
				if( 1 <= ((double)matches / (double)ne.length)  )	{
					for(int j = 0; j < ne.length; j++)
						for(NERTag tag : TAGS)
							if(saved_ne.computeProbability(tag) > 0)
								// add probability
								prob[ci+j][EFeature.SAVED_NE.ordinal()][tag.ordinal()] = 2;
				}
				else if( 0.75 <= ((double)matches / (double)ne.length) )	{
					for(int j = 0; j < ne.length; j++)
						for(NERTag tag : TAGS)
							if(saved_ne.computeProbability(tag) > 0)
								// add probability if it prob is not <= 2
								if(!(prob[ci+j][EFeature.SAVED_NE.ordinal()][tag.ordinal()] >= 2))
									prob[ci+j][EFeature.SAVED_NE.ordinal()][tag.ordinal()] = 1;
				}
			}
			/* end Maximizing Entropy - Saved Named Entities */
			
			// ci is now the index of the capitalized token
			ci = i;
			/* Maximizing Entropy - Word Shape */
			// for all the word shape
			for(FWordShape word_shape : this.fwordshape)	{
				String [] ws = word_shape.getWordShape().split(" ");
				if( ws.length > tokens.size()-ci )	continue;
				int matches = 0;
				// count the number of matches
				for(int j = 0; j < ws.length; j++)	{
					if(ws[j].equals(FWordShape.extractWordShape(tokens.get(ci+j).getLexeme())))	{
						matches++;
					}
				}
				// if match is 100%
				if( 1 <= ((double)matches / (double)ws.length) )	{
					for(int j = 0; j < ws.length; j++)
						for(NERTag tag : TAGS)
							if(word_shape.computeProbability(tag) > 0)
								// add probability
								prob[ci+j][EFeature.WORD_SHAPE.ordinal()][tag.ordinal()] = 1;
				}
			}
			/* end Maximizing Entropy - Word Shape */
		}
		/* end Article based tagging */
		
		/* Compute the probabilities */
		// compute the summation of tag's probability / token
		double [][] prob_sum = new double[tokens.size()][TAGS.length];
		for(int i = 0; i < tokens.size(); i++)
			for(int f = 0; f < FEATURES.length; f++)
				for(int t = 0; t < TAGS.length; t++)
					prob_sum[i][t] += prob[i][f][t];

		// the highest probability gets the tag
		for(int i = 0; i < tokens.size(); i++)	{
			int max_index = getMax(prob_sum[i]);
			if( !(max_index == -1 || prob_sum[i][max_index] <= 1))
				tokens.get(i).setNERTag(TAGS[max_index]);
		}
		
		tokens = this.polishTaggingPrevTag(tokens);
		tokens = this.polishTaggingOther(tokens);
		tokens = this.polishTaggingSubstring(tokens);
		tokens = this.rememberOrgPlaceAbbv(tokens);
		
		// tag person
		for(Integer i : iarticle_person)	{
			int ai = i;
			i++;
			while(	i < tokens.size() && 
					!isCapitalized(tokens.get(i)) &&
					!Character.isDigit(tokens.get(i).getLexeme().charAt(0)) &&
					tokens.get(i).getTokenType() == TokenType.WORD &&
					searchArticle(tokens.get(i).getLexeme()) == null )	{
				i++;
			}
			if(i >= tokens.size())	break;
			if(searchArticle(tokens.get(i).getLexeme()) != null)	{
				i = ai;
				continue;
			}
			int ci = i;
			while( isCapitalized(tokens.get(ci)) || 
					(tokens.get(ci).getLexeme().equals(".") && ci-1 >= 0 && tokens.get(ci-1).getLexeme().matches("[A-Z](.[A-Z])*") ) )	{
				tokens.get(ci).setNERTag(NERTag.PERSON);
				ci++;
				if(ci >= tokens.size())	break;
			}
		}
		
		tokens = this.tagTitlePersonRelationship(tokens);
		tokens = this.polishTaggingPrevTag(tokens);
		
		return tokens;
	}
	
	public boolean isCapitalized(Token token)	{
		if(Character.isDigit(token.getLexeme().charAt(0)))	return false;
		return Character.isUpperCase(token.getLexeme().charAt(0));
	}
	
	public int getMax(double [] values)	{
		int index = 0;
		double max = 0;
		for(int i = 0; i < values.length; i++)	{
			if(values[i] > max)	{
				index = i;
				max = values[i];
			}
		}
		return max == 0 ? -1 : index;
	}
	
	private int regexNoOFWords(String regex)	{
		return regex.split("[ ]").length;
	}
	
	String getAbbv(String s)	{
		String abbv = "";
		String [] split = s.split(" ");
		for(int i = 0; i < split.length; i++)	{
			if( isCapitalized(new Token(split[i], TokenType.WORD)) )
				abbv += split[i].charAt(0);
			if( split[i].equals("of") 
				&& i-1 >= 0 && split[i-1].equals("Department"))
				abbv += "O";
			if( split[i].equals("on") 
				&& i-1 >= 0 && split[i-1].equals("Commission"))
				abbv += "O";
			if( split[i].equals("de") )
					abbv += "D";
		}
		return abbv;
	}
	
	public List<NERToken> polishTaggingRegex(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		for(int i = 0; i < tokens.size(); i++)	{
			for(FRegex regex : this.fregex)	{
				int ct = this.regexNoOFWords(regex.getRegex())-1;
				String s = "";
				if( i + ct < tokens.size())	{
					s = tokens.get(i).getLexeme();
					for(int j = 1; j <= ct; j++)	s += " " + tokens.get(i+j).getLexeme();
					if( s.matches(regex.getRegex()) )	{
						NERTag tag = null;
						for(NERTag tags : TAGS)
							if(regex.getOccurencePerTag(tags) > 0)	tag = tags;
						for(int j = 0; j <= ct; j++)	tokens.get(i+j).setNERTag(tag);
					}
				}
			}
		}
		for(int i = 0; i < tokens.size(); i++)	{
			String s = tokens.get(i).getLexeme();
			if( s.matches("[a-z]{4}[a-z]*"))
				if( tokens.get(i).getNERTag() != NERTag.TIME &&
						tokens.get(i).getNERTag() != NERTag.PERCENT)
					tokens.get(i).setNERTag(null);
			if( s.matches("(taong|noong|nitong|ngayong|ang|sa|ay|ng)") &&
					tokens.get(i).getNERTag() == NERTag.DATE)	{
				if( s.matches("ng") && i-1 >= 0 && isCapitalized(tokens.get(i-1)) )	continue;
				tokens.get(i).setNERTag(null);
			}
		}
		return tokens;
	}
	
	public List<NERToken> tagTitlePersonRelationship(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		for(int i = 0; i < tokens.size(); i++)	{
			for(FSavedNE saved_ne : this.fsavedne)	{
				String [] ne = saved_ne.getNE().split(" ");
				if( ne.length > tokens.size()-i )	continue;
				if( saved_ne.getOccurencePerTag(NERTag.TITLE) <= 0 )	continue;
				int matches = 0;
				// count the number of matches
				for(int j = 0; j < ne.length; j++)	{
					if(ne[j].equals(tokens.get(i+j).getLexeme()))	{
						matches++;
					}
				}
				// match
				if( 1 <= ((double)matches / (double)ne.length)  )	{
					for(int j = 0; j < ne.length; j++)
						tokens.get(i+j).setNERTag(NERTag.TITLE);
				}
			}
			if(tokens.get(i).getNERTag() == NERTag.TITLE)	{
				while(tokens.get(i).getNERTag() == NERTag.TITLE)	{
					i++;
				}
				// i is now the index of the token after title
				while(	i < tokens.size() &&
						isCapitalized(tokens.get(i)) || 
						(tokens.get(i).getLexeme().equals(".") && 
								i-1 >= 0 && tokens.get(i-1).getLexeme().matches("[A-Z](.[A-Z])*") )
						)	{
					// if another title is found
					if( this.searchSavedNE(tokens.get(i).getLexeme()) != null)		{
						while(tokens.get(i).getNERTag() != NERTag.TITLE)	{
							tokens.get(i).setNERTag(NERTag.TITLE);
							i--;
						}
						while(i < tokens.size() && tokens.get(i).getNERTag() == NERTag.TITLE)	{
							i++;
						}
					}
					if(i < tokens.size())
						tokens.get(i++).setNERTag(NERTag.PERSON);
					if(i < tokens.size() && tokens.get(i).getLexeme().equals(".")
						&& tokens.get(i-1).getLexeme().matches("[A-Z]") )	{
						tokens.get(i++).setNERTag(NERTag.PERSON);
					}
				}
			}
		}
		for(int i = 0; i < tokens.size(); i++)	{
			if(tokens.get(i).getNERTag() == NERTag.TITLE)	{
				int it = i;
				while(it-1 >= 0 && ( tokens.get(it-1).getNERTag() != null || 
						// World Bank (WB) Secretary Asdfg
						( tokens.get(it-1).getLexeme().equals(")") &&
						  it-2 >= 0 && tokens.get(it-2).getNERTag() == NERTag.ORGANIZATION &&
						  it-3 >= 0 && tokens.get(it-3).getLexeme().equals("(") )
						) )	{
					if( tokens.get(it-1).getLexeme().equals(")") &&
							  it-2 >= 0 && tokens.get(it-2).getNERTag() == NERTag.ORGANIZATION &&
							  it-3 >= 0 && tokens.get(it-3).getLexeme().equals("(") )  it-=2;
					it--;
				}
				while(it < tokens.size() && it < i)	{
					tokens.get(it).setNERTag(NERTag.TITLE);
					it++;
				}
			}
		}
		return tokens;
	}
		
	public List<NERToken> polishTaggingSubstring(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		for(int i = 0; i < tokens.size(); i++)	{
			if(tokens.get(i).getNERTag() != null)	{
				String ne = "";
				int len = 0;
				// get the current NERTag
				NERTag tag = tokens.get(i).getNERTag();
				// get the named entity
				while(tokens.get(i).getNERTag() == tag)	{
					ne += (" " + tokens.get(i).getLexeme() );
					len++; i++;
					if(i >= tokens.size())	break;
				}
				ne = ne.substring(1);
				for(FSubstring subword : this.fsubstring)	{
					if(ne.contains(subword.getSubstring()))
						for(NERTag tags : TAGS)	
							if(subword.getOccurencePerTag(tags) > 0)
								for(int x = i-len; x < i; x++)	
									tokens.get(x).setNERTag(tags);
				}
			}
		}
		return tokens;
	}

	public List<NERToken> polishTaggingPrevTag(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		
		for(int i = 1; i < tokens.size(); i++)	{
			// if the previous tag is in [date, time, money, percent, title] continue 
			if( tokens.get(i-1).getNERTag() == NERTag.DATE ||
					tokens.get(i-1).getNERTag() == NERTag.TIME ||
					tokens.get(i-1).getNERTag() == NERTag.MONEY ||
					tokens.get(i-1).getNERTag() == NERTag.PERCENT ||
					tokens.get(i-1).getNERTag() == NERTag.TITLE ||
					tokens.get(i-1).getNERTag() == null )
				continue;
			
			NERTag prev_tag = tokens.get(i-1).getNERTag();
			
			String curr_lex = tokens.get(i).getLexeme();
			String prev_lex = tokens.get(i-1).getLexeme();
			
			// the current word starts in uppercase
			if( isCapitalized(tokens.get(i)) || 
					( prev_tag == NERTag.PERSON &&
					(curr_lex.equals(".") && i-1 >= 0 && prev_lex.matches("[A-Z](.[A-Z])*") ))
					)
				tokens.get(i).setNERTag(prev_tag);
			
			// current lexeme is in[, at] and next lexeme starts uppercase
			if( curr_lex.matches(",|at") && 
					i+1 < tokens.size() && isCapitalized(tokens.get(i+1)) )
				tokens.get(i+1).setNERTag(prev_tag);
			
			// current lexeme is in[' and for &] and next lexeme starts uppercase
			if( curr_lex.matches("'|and|for|&") && 
					i+1 < tokens.size() && isCapitalized(tokens.get(i+1)) )	{
				tokens.get(i).setNERTag(prev_tag);
				tokens.get(i+1).setNERTag(prev_tag);
			}
			
			// current lexeme is , && next lexeme is at && lexeme + 2 starts uppercase
			if( curr_lex.equals(",") &&
					i+1 < tokens.size() && tokens.get(i+1).getLexeme().equals("at") &&
					i+2 < tokens.size() && isCapitalized(tokens.get(i+2)) )	{
				tokens.get(i+2).setNERTag(prev_tag);
			}
			
			// current lexeme is in[of|ng|de|del]
			if( curr_lex.matches("(of|ng|de|del)") && 
					i+1 < tokens.size() && isCapitalized(tokens.get(i+1)) &&
					isCapitalized(tokens.get(i-1)) )
				tokens.get(i).setNERTag(prev_tag);
			
			// ng mga
			if(curr_lex.matches("ng") && 
					i+1 < tokens.size() && tokens.get(i+1).getLexeme().matches("mga") &&
					i+2 < tokens.size() && isCapitalized(tokens.get(i+2)) &&
					isCapitalized(tokens.get(i-1)) )	{
				tokens.get(i).setNERTag(prev_tag);
				tokens.get(i+1).setNERTag(prev_tag);
			}
			
			// of the
			if(curr_lex.matches("of") && 
					i+1 < tokens.size() && tokens.get(i+1).getLexeme().matches("the") &&
					i+2 < tokens.size() && isCapitalized(tokens.get(i+2)) &&
					isCapitalized(tokens.get(i-1))  )	{
				tokens.get(i).setNERTag(prev_tag);
				tokens.get(i+1).setNERTag(prev_tag);
			}
			
			if(curr_lex.matches("[0-9].*"))
				tokens.get(i).setNERTag(prev_tag);
			
			// if the previous tag is place
			// if prev word is in [Sta|Sto|Brgy|St|Ave|Blk]
			// current lexeme is .
			if( prev_tag == NERTag.PLACE &&
					prev_lex.matches("(Sta|Sto|Brgy|St|Ave|Blk)") &&
					curr_lex.equals(".") )	{
				tokens.get(i).setNERTag(NERTag.PLACE);
			}
			
			// if the previous tag is person
			// if prev word is in [Jr|Sr]
			// current lexeme is .
			if( prev_tag == NERTag.PERSON &&
					prev_lex.matches("(Jr|Sr)") &&
					curr_lex.equals(".") )	{
				tokens.get(i).setNERTag(NERTag.PERSON);
			}
			
		}
		return tokens;
	}

	public List<NERToken> polishTaggingOther(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		for(int i = 0; i < tokens.size(); i++)	{
			if(tokens.get(i).getNERTag() == null)	{
				if( isCapitalized(tokens.get(i)) 
						&& i-1 >= 0 && !tokens.get(i-1).getLexeme().matches("[?.!\"]"))	{
					tokens.get(i).setNERTag(NERTag.OTHER);
				}
			}
		}
		return tokens;
	}
	
	public List<NERToken> rememberOrgPlaceAbbv(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		List<String> orgs = new ArrayList<String>();
		List<String> places = new ArrayList<String>();
		for(int i = 0; i < tokens.size(); i++)	{
			if( tokens.get(i).getNERTag() == NERTag.ORGANIZATION ||
					tokens.get(i).getNERTag() == NERTag.PLACE)	{
				String ne = "";
				// get the current NERTag
				NERTag tag = tokens.get(i).getNERTag();
				// get the named entity
				while(tokens.get(i).getNERTag() == tag)	{
					ne += (" " + tokens.get(i).getLexeme() );
					i++;
					if(i >= tokens.size())	break;
				}
				i--;
				ne = ne.substring(1);
				if(tag == NERTag.ORGANIZATION)	orgs.add(ne);
				if(tag == NERTag.PLACE) places.add(ne);
			}
		}
		for(String s : orgs)	this.possible_org_abbv.add( getAbbv(s) );
		for(String s : places)	this.possible_place_abbv.add( getAbbv(s) );
		
		for(int i = 0; i < tokens.size(); i++)	{
			if(tokens.get(i).getNERTag() != null)	{
				String ne = "";
				int len = 0;
				// get the current NERTag
				NERTag tag = tokens.get(i).getNERTag();
				// get the named entity
				while(tokens.get(i).getNERTag() == tag)	{
					ne += (" " + tokens.get(i).getLexeme() );
					len++; i++;
					if(i >= tokens.size())	break;
				}
				ne = ne.substring(1);
				if(this.possible_org_abbv.contains(ne))
					for(int x = i-len; x < i; x++)	
						tokens.get(x).setNERTag(NERTag.ORGANIZATION);
				if(this.possible_place_abbv.contains(ne))
					for(int x = i-len; x < i; x++)	
						tokens.get(x).setNERTag(NERTag.PLACE);
			}
		}
		return tokens;
	}

}
