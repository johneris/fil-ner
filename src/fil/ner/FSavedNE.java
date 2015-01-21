package fil.ner;

public class FSavedNE extends Feature	{
	
	private String ne;
	
	public FSavedNE(String ne, int[] values) {
		super(values);
		this.ne = ne;
	}
	
	public String getNE()	{
		return this.ne;
	}

}
