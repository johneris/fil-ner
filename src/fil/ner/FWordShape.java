package fil.ner;

public class FWordShape extends Feature	{
	
	private String ws;
	
	public FWordShape(String ws, int[] values) {
		super(values);
		this.ws = ws;
	}
	
	public String getWordShape()	{
		return this.ws;
	}
	
	public static String extractWordShape(String s)	{
		String ws = "";
		int lowercase = 0;
		int uppercase = 0;
		int digit = 0;
		for(int i = 0; i < s.length(); i++)	{
			if( Character.isDigit(s.charAt(i)) )	{
				if(!(digit >= 4))	ws += "d";
				if(digit == 4)		ws += "+";
				lowercase = 0;
				uppercase = 0;
				digit++;
			} else if( Character.isUpperCase(s.charAt(i)) )	{
				if(!(uppercase >= 3))	ws += "X";
				if(uppercase == 3)		ws += "+";
				lowercase = 0;
				uppercase++;
				digit = 0;
			} else if( Character.isLowerCase(s.charAt(i)) )	{
				if(!(lowercase >= 3))	ws += "x";
				if(lowercase == 3)		ws += "+";
				lowercase++;
				uppercase = 0;
				digit = 0;
			} else	{
				ws += s.charAt(i);
				lowercase = 0;
				uppercase = 0;
				digit = 0;
			}
		}
		return ws;
	}
	
}
