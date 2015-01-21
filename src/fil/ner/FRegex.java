package fil.ner;

public class FRegex extends Feature	{
	
	private String regex;
	
	public FRegex(String regex, int[] values) {
		super(values);
		this.regex = regex;
	}
	
	public String getRegex()	{
		return this.regex;
	}

}
