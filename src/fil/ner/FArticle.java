package fil.ner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FArticle	{
	
	private String article;
	private Set<List<NERTag>> next_tags;
	
	public FArticle(String article) {
		this.next_tags = new HashSet<List<NERTag>>();
		this.article = article;
	}
	
	public String getArticle()	{
		return this.article;
	}
	
	public Set<List<NERTag>> getNextTags()	{
		return this.next_tags;
	}

	public void addNextTags(NERTag [] tags)	{
		List<NERTag> l = new ArrayList<NERTag>();
		for(NERTag t : tags)	l.add(t);
		this.next_tags.add(l);
	}
	
	public void addNextTags(List<NERTag> tags)	{
		List<NERTag> l = new ArrayList<NERTag>();
		for(NERTag t : tags)	l.add(t);
		this.next_tags.add(l);
	}
	
}