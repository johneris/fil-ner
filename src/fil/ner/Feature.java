package fil.ner;

public abstract class Feature {
	
	private int occurence;
	private int [] tag;
	
	public Feature(int [] values)	{
		tag = new int[NERTag.values().length];
		occurence = 0;
		for(NERTag t : NERTag.values())	{
			try {
				tag[t.ordinal()] = values[t.ordinal()];
				occurence += tag[t.ordinal()];
			} catch (Exception e) {
				tag[t.ordinal()] = 0;
				continue;
			}
		}
	}
	
	public double computeProbability(NERTag tag)	{
		return (double)this.tag[tag.ordinal()] / (double)occurence;
	}
	
	public void addProbability(NERTag tag)	{
		this.tag[tag.ordinal()]++;
		occurence++;
	}
	
	public int getOccurence()	{
		return this.occurence;
	}
	
	public int getOccurencePerTag(NERTag tag)	{
		return this.tag[tag.ordinal()];
	}
	
}
