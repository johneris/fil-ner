package fil.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Token {
	
	private String lexeme;
	private TokenType type;
	
	public Token(String lexeme, TokenType type)	{
		this.lexeme = lexeme;
		this.type = type;
	}
	
	public String getLexeme()	{
		return this.lexeme;
	}
	
	public TokenType getTokenType()	{
		return this.type;
	}
	
	public static List<Token> tokenizer(String input)	{
		List<Token> tokens = new ArrayList<Token>();
		StringTokenizer st = new StringTokenizer(input, " ");
		while(st.hasMoreTokens())	{
			String s = st.nextToken();
			while(	s.length() > 0 && 
					!Character.isLetter(s.charAt(0)) && 
					!Character.isDigit(s.charAt(0))	)	{
				tokens.add(new Token(s.substring(0, 1), TokenType.DELIMETER) );
				s = s.substring(1);
			}
			if(s.length() > 0)	{
				int ctr = 0;
				while(	!(Character.isLetter(s.charAt(s.length()-(ctr+1))) || 
						Character.isDigit(s.charAt(s.length()-(ctr+1))))	)
					ctr++;
				tokens.add(new Token(s.substring(0, s.length()-ctr), TokenType.WORD) );
				s = s.substring(s.length()-ctr);
			}
			if(s.length() > 0)	{
				for(Character ch : s.toCharArray())	{
					tokens.add(new Token(ch+"", TokenType.DELIMETER) );
				}
			}
		}
		tokens = trimSlash(tokens);
		return tokens;
	}
	
	private static List<Token> trimSlash(List<Token> t)	{
		List<Token> tokens = new ArrayList<Token>();
		for(int i = 0; i < t.size(); i++)	{
			if(t.get(i).getLexeme().contains("/"))	{
				String [] split = t.get(i).getLexeme().split("/");
				for(int j = 0; j < split.length; j++)	{
					tokens.add( new Token(split[j], TokenType.WORD) );
					if(!(j == split.length-1))
						tokens.add( new Token("/", TokenType.DELIMETER) );
				}
			}
			else
				tokens.add(t.get(i));
		}
		return tokens;
	}
	
}
