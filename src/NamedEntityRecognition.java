import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import fil.ner.FArticle;
import fil.ner.FRegex;
import fil.ner.FSavedNE;
import fil.ner.FSubstring;
import fil.ner.FWordShape;
import fil.ner.MEMM;
import fil.ner.NERTag;

public class NamedEntityRecognition	{

	private static final String article_file = "learning/article";
	private static final String savedne_file = "learning/savedne";
	private static final String wordshape_file = "learning/wordshape";
	private static final String substring_file = "learning/substring";
	private static final String regex_file = "learning/regex";
	
	public MEMM memm = null;
	
	public NamedEntityRecognition()	{
		memm = new MEMM(initArticle(), initSavedNE(),
				initWordShape(), initSubstring(), initRegex());
	}
	
	List<FArticle> initArticle()	{
		System.out.println("Initializing Feature - Article...");
		List<FArticle> articles = new ArrayList<FArticle>();
		try {
			Scanner in = new Scanner(new File(article_file));
			while(in.hasNext())	{
				FArticle article = null;
				String a = in.nextLine();
				article = new FArticle(a);
				int size = Integer.parseInt(in.nextLine());
				for(int i = 0; i < size; i++)	{
					String [] t = in.nextLine().split(" ");
					NERTag [] tags = new NERTag[t.length];
					for(int j = 0; j < t.length; j++)	{
						NERTag[] ts = NERTag.values();
						tags[j] = ts[Integer.parseInt(t[j])];
					}
					article.addNextTags(tags);
				}
				articles.add(article);
			}
			in.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"article file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			System.out.println("File Not Found - " + "article file missing!");
			System.out.println("Feature - Article : Creating untrained article file...");
			new File(article_file);
		}
		return articles;
	}
	
	List<FSavedNE> initSavedNE()	{
		System.out.println("Initializing learned Named Entities...");
		List<FSavedNE> savedne = new ArrayList<FSavedNE>();
		try {
			Scanner in = new Scanner(new File(savedne_file));
			while(in.hasNext())	{
				int [] values = new int[NERTag.values().length];
				String s = in.nextLine();
				for(int i = 0; i < NERTag.values().length; i++)
					values[i] = in.nextInt();
				in.nextLine();
				savedne.add(new FSavedNE(s, values));
			}
			in.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"savedne file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			System.out.println("File Not Found - " + "savedne file missing!");
			System.out.println("Creating savedne file...");
			new File(savedne_file);
		}
		return savedne;
	}
	
	List<FWordShape> initWordShape()	{
		System.out.println("Initializing Feature - Word Shape...");
		List<FWordShape> wordshape = new ArrayList<FWordShape>();
		try {
			Scanner in = new Scanner(new File(wordshape_file));
			while(in.hasNext())	{
				int [] values = new int[NERTag.values().length];
				String s = in.nextLine();
				for(int i = 0; i < NERTag.values().length; i++)	
					values[i] = in.nextInt();
				in.nextLine();
				wordshape.add(new FWordShape(s, values));
			}
			in.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"wordshape file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			System.out.println("File Not Found - " + "wordshape file missing!");
			System.out.println("Feature - Word Shape : Creating untrained wordshape file...");
			new File(wordshape_file);
		}
		return wordshape;
	}
	
	List<FSubstring> initSubstring()	{
		System.out.println("Initializing Feature - Substring...");
		List<FSubstring> substring = new ArrayList<FSubstring>();
		try {
			Scanner in = new Scanner(new File(substring_file));
			while(in.hasNext())	{
				int [] values = new int[NERTag.values().length];
				String s = in.nextLine();
				for(int i = 0; i < NERTag.values().length; i++)	
					values[i] = in.nextInt();
				in.nextLine();
				substring.add(new FSubstring(s, values));
			}
			in.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"substring file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			System.out.println("File Not Found - " + "substring file missing!");
			System.out.println("Feature - Substring : Creating untrained substring file...");
			new File(substring_file);
		}
		return substring;
	}
	
	List<FRegex> initRegex()	{
		System.out.println("Initializing Feature - Regex...");
		List<FRegex> regex = new ArrayList<FRegex>();
		try {
			Scanner in = new Scanner(new File(regex_file));
			while(in.hasNext())	{
				int [] values = new int[NERTag.values().length];
				String s = in.nextLine();
				for(int i = 0; i < NERTag.values().length; i++)	
					values[i] = in.nextInt();
				in.nextLine();
				regex.add(new FRegex(s, values));
			}
			in.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"regex file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			System.out.println("File Not Found - " + "regex file missing!");
			System.out.println("Feature - Regex : Creating untrained regex file...");
			new File(regex_file);
		}
		return regex;
	}
	
	void recordMEMM()	{
		recordArticle();
		recordSavedNE();
		recordWordShape();
		recordSubstring();
		recordRegex();
	}
	
	void recordArticle()	{
		System.out.println("Saving Feature - Article...");
		try {
			PrintWriter pf = new PrintWriter(new File(article_file));
			for(FArticle s : memm.farticle)	{
				pf.append(s.getArticle() + "\n");
				pf.append(s.getNextTags().size() + "\n");
				for(List<NERTag> set : s.getNextTags())	{
					for(NERTag tag : set)	{
						pf.append(tag.ordinal() + " ");
					}
					pf.append("\n");
				}
			}
			pf.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"article file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	void recordSavedNE()	{
		System.out.println("Saving Named Entities...");
		try {
			PrintWriter pf = new PrintWriter(new File(savedne_file));
			for(FSavedNE s : memm.fsavedne)	{
				pf.append(s.getNE() + "\n");
				for(NERTag tag : NERTag.values())
					pf.append(s.getOccurencePerTag(tag) + " ");
				pf.append("\n");
			}
			pf.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"savedne file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	void recordWordShape()	{
		System.out.println("Saving Feature - Word Shape...");
		try {
			PrintWriter pf = new PrintWriter(new File(wordshape_file));
			for(FWordShape s : memm.fwordshape)	{
				pf.append(s.getWordShape() + "\n");
				for(NERTag tag : NERTag.values())
					pf.append(s.getOccurencePerTag(tag) + " ");
				pf.append("\n");
			}
			pf.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"wordshape file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	void recordSubstring()	{
		System.out.println("Saving Feature - Substring...");
		try {
			PrintWriter pf = new PrintWriter(new File(substring_file));
			for(FSubstring s : memm.fsubstring)	{
				pf.append(s.getSubstring() + "\n");
				for(NERTag tag : NERTag.values())
					pf.append(s.getOccurencePerTag(tag) + " ");
				pf.append("\n");
			}
			pf.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"substring file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	void recordRegex()	{
		System.out.println("Saving Feature - Regex...");
		try {
			PrintWriter pf = new PrintWriter(new File(regex_file));
			for(FRegex s : memm.fregex)	{
				pf.append(s.getRegex() + "\n");
				for(NERTag tag : NERTag.values())
					pf.append(s.getOccurencePerTag(tag) + " ");
				pf.append("\n");
			}
			pf.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				"regex file missing!",
				"File Not Found!",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
}
