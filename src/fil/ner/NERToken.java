package fil.ner;

import java.util.ArrayList;
import java.util.List;

import fil.tools.Token;
import fil.tools.TokenType;

public class NERToken extends Token	{
	
	private NERTag tag;
	
	public NERToken(String lexeme, TokenType type) {
		super(lexeme, type);
		this.tag = null;
	}
	
	public NERToken(Token token)	{
		super(token.getLexeme(), token.getTokenType());
		this.tag = null;
	}
	
	public NERTag getNERTag()	{
		return this.tag;
	}
	
	public void setNERTag(NERTag tag)	{
		this.tag = tag;
	}
	
	public static List<NERToken> nerTokenizer(String input)	{
		List<NERToken> nertokens = new ArrayList<NERToken>();
		List<Token> tokens = Token.tokenizer(input);
		for(Token t : tokens)	nertokens.add( new NERToken(t) );
		return nertokens;
	}
	
}
