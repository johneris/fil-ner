package fil.ner;

public class FSubstring extends Feature	{
	
	private String substring;
	
	public FSubstring(String substring, int[] values) {
		super(values);
		this.substring = substring;
	}
	
	public String getSubstring()	{
		return this.substring;
	}

}
