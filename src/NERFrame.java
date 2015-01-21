import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JSeparator;

import fil.ner.NERToken;
import fil.ner.NERTag;
import fil.tools.TokenType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

public class NERFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	NamedEntityRecognition ner = null;
	
	private final Highlighter.HighlightPainter[] highlighter = new Highlighter.HighlightPainter[NERTag.values().length];;
	
	private final FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt", "text");;
	private final javax.swing.JFileChooser SaveFileChooser = new javax.swing.JFileChooser();
	private final javax.swing.JFileChooser OpenFileChooser = new javax.swing.JFileChooser();
	
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnAbout;
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnRun;
	private JButton btnClear;
	private JToolBar toolBar;
	private JPanel panelCenter;
	private JLabel lblEnterFilipinoText;
	private JTextArea txtrInput;
	private JButton btnPerson;
	private JButton btnTitle;
	private JButton btnPlace;
	private JButton btnOrg;
	private JButton btnDate;
	private JButton btnTime;
	private JButton btnEvent;
	private JButton btnMoney;
	private JButton btnPercent;
	private JButton btnOther;
	private JMenuItem mntmSaveFile;
	private JMenuItem mntmOpen;
	private JSeparator separatorFile;
	private JMenuItem mntmExit;
	private JMenuItem mntmAboutUs;
	private JMenu mnRun;
	private JMenuItem mntmTagNamedEntities;
	private JScrollPane scrollPane;
	private JSeparator separatorEdit;
	private JMenu mnEdit;
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmSelectAll;
	private JMenuItem mntmAboutSystem;
	
	public NERFrame() {
		ner = new NamedEntityRecognition();
        System.out.println("Loading UI...");
        initUI();
	}
	
	private void initUI()	{
        highlighter[NERTag.TITLE.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        highlighter[NERTag.PERSON.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
        highlighter[NERTag.PLACE.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        highlighter[NERTag.ORGANIZATION.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        highlighter[NERTag.DATE.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
        highlighter[NERTag.TIME.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(new Color(135,206,235));
        highlighter[NERTag.EVENT.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(new Color(147,112,219));
        highlighter[NERTag.MONEY.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(new Color(189,183,107));
        highlighter[NERTag.PERCENT.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(new Color(152,251,152));
        highlighter[NERTag.OTHER.ordinal()] = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
		
		SaveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        SaveFileChooser.setApproveButtonText("Save");
        SaveFileChooser.setOpaque(true);

        OpenFileChooser.setApproveButtonText("Open File");
        OpenFileChooser.setDialogTitle("Open File");
        OpenFileChooser.setForeground(java.awt.Color.red);
		
		setTitle("Named Entity Recognition for Filipino Text");
		getContentPane().setBackground(SystemColor.desktop);
		getContentPane().setSize(new Dimension(600, 600));
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ner.recordMEMM();
                System.exit(0);
            }
        });
		
		toolBar = new JToolBar();
		toolBar.setBackground(new Color(196, 203, 222));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnOpen = new JButton("");
		btnOpen.setContentAreaFilled(false);
		btnOpen.setToolTipText("Open File");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						openFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
		btnOpen.setIcon(new ImageIcon(NERFrame.class.getResource("/img/open.png")));
		toolBar.add(btnOpen);
		
		btnSave = new JButton("");
		btnSave.setContentAreaFilled(false);
		btnSave.setToolTipText("Save File");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});
		btnSave.setIcon(new ImageIcon(NERFrame.class.getResource("/img/save.png")));
		toolBar.add(btnSave);
		
		btnRun = new JButton("");
		btnRun.setContentAreaFilled(false);
		btnRun.setToolTipText("Tag Named Entities");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tag(arg0);
			}
		});
		btnRun.setIcon(new ImageIcon(NERFrame.class.getResource("/img/run.png")));
		toolBar.add(btnRun);
		
		btnClear = new JButton("");
		btnClear.setContentAreaFilled(false);
		btnClear.setToolTipText("Clear Text");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrInput.setText("");
			}
		});
		btnClear.setIcon(new ImageIcon(NERFrame.class.getResource("/img/clear.png")));
		toolBar.add(btnClear);
		
		panelCenter = new JPanel();
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);
		
		lblEnterFilipinoText = new JLabel("Enter Filipino Text:");
		lblEnterFilipinoText.setFont(new Font("Candara", Font.PLAIN, 20));
		lblEnterFilipinoText.setBounds(20, 11, 170, 30);
		panelCenter.add(lblEnterFilipinoText);
		
		txtrInput = new JTextArea();
		txtrInput.setWrapStyleWord(true);
		txtrInput.setAutoscrolls(true);
		txtrInput.setLineWrap(true);
		txtrInput.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
		
		btnPerson = new JButton("Person");
		btnPerson.setHorizontalAlignment(SwingConstants.LEFT);
		btnPerson.setIcon(new ImageIcon(NERFrame.class.getResource("/img/person.PNG")));
		btnPerson.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnPerson.setBackground(Color.WHITE);
		btnPerson.setBounds(445, 83, 126, 23);
		panelCenter.add(btnPerson);
		
		btnTitle = new JButton("Title");
		btnTitle.setIcon(new ImageIcon(NERFrame.class.getResource("/img/title.PNG")));
		btnTitle.setHorizontalAlignment(SwingConstants.LEFT);
		btnTitle.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnTitle.setBackground(Color.WHITE);
		btnTitle.setBounds(445, 50, 126, 23);
		panelCenter.add(btnTitle);
		
		btnPlace = new JButton("Place");
		btnPlace.setIcon(new ImageIcon(NERFrame.class.getResource("/img/place.PNG")));
		btnPlace.setHorizontalAlignment(SwingConstants.LEFT);
		btnPlace.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnPlace.setBackground(Color.WHITE);
		btnPlace.setBounds(445, 117, 126, 23);
		panelCenter.add(btnPlace);
		
		btnOrg = new JButton("Org");
		btnOrg.setIcon(new ImageIcon(NERFrame.class.getResource("/img/org.PNG")));
		btnOrg.setHorizontalAlignment(SwingConstants.LEFT);
		btnOrg.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnOrg.setBackground(Color.WHITE);
		btnOrg.setBounds(445, 151, 126, 23);
		panelCenter.add(btnOrg);
		
		btnDate = new JButton("Date");
		btnDate.setIcon(new ImageIcon(NERFrame.class.getResource("/img/date.PNG")));
		btnDate.setHorizontalAlignment(SwingConstants.LEFT);
		btnDate.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnDate.setBackground(Color.WHITE);
		btnDate.setBounds(445, 185, 126, 23);
		panelCenter.add(btnDate);
		
		btnTime = new JButton("Time");
		btnTime.setIcon(new ImageIcon(NERFrame.class.getResource("/img/time.PNG")));
		btnTime.setHorizontalAlignment(SwingConstants.LEFT);
		btnTime.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnTime.setBackground(Color.WHITE);
		btnTime.setBounds(445, 219, 126, 23);
		panelCenter.add(btnTime);
		
		btnEvent = new JButton("Event");
		btnEvent.setIcon(new ImageIcon(NERFrame.class.getResource("/img/event.PNG")));
		btnEvent.setHorizontalAlignment(SwingConstants.LEFT);
		btnEvent.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnEvent.setBackground(Color.WHITE);
		btnEvent.setBounds(445, 253, 126, 23);
		panelCenter.add(btnEvent);
		
		btnMoney = new JButton("Money");
		btnMoney.setIcon(new ImageIcon(NERFrame.class.getResource("/img/money.PNG")));
		btnMoney.setHorizontalAlignment(SwingConstants.LEFT);
		btnMoney.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnMoney.setBackground(Color.WHITE);
		btnMoney.setBounds(445, 287, 126, 23);
		panelCenter.add(btnMoney);
		
		btnPercent = new JButton("Percent");
		btnPercent.setIcon(new ImageIcon(NERFrame.class.getResource("/img/percent.PNG")));
		btnPercent.setHorizontalAlignment(SwingConstants.LEFT);
		btnPercent.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnPercent.setBackground(Color.WHITE);
		btnPercent.setBounds(445, 321, 126, 23);
		panelCenter.add(btnPercent);
		
		btnOther = new JButton("Other");
		btnOther.setIcon(new ImageIcon(NERFrame.class.getResource("/img/other.PNG")));
		btnOther.setHorizontalAlignment(SwingConstants.LEFT);
		btnOther.setFont(new Font("Corbel", Font.PLAIN, 18));
		btnOther.setBackground(Color.WHITE);
		btnOther.setBounds(445, 355, 126, 23);
		panelCenter.add(btnOther);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 48, 415, 377);
		scrollPane.setViewportView(txtrInput);
		panelCenter.add(scrollPane);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open File");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					openFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mntmOpen.setMnemonic('O');
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		mntmSaveFile = new JMenuItem("Save File");
		mntmSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});
		mntmSaveFile.setMnemonic('S');
		mntmSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveFile);
		
		separatorFile = new JSeparator();
		mnFile.add(separatorFile);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exit(arg0);
			}
		});
		mntmExit.setMnemonic('E');
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);
		
		mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);
		
		mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrInput.cut();
			}
		});
		mntmCut.setMnemonic('T');
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnEdit.add(mntmCut);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrInput.copy();
			}
		});
		mntmCopy.setMnemonic('C');
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnEdit.add(mntmCopy);
		
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrInput.paste();
			}
		});
		mntmPaste.setMnemonic('P');
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnEdit.add(mntmPaste);
		
		separatorEdit = new JSeparator();
		mnEdit.add(separatorEdit);
		
		mntmSelectAll = new JMenuItem("Select All");
		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtrInput.selectAll();
			}
		});
		mntmSelectAll.setMnemonic('A');
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnEdit.add(mntmSelectAll);
		
		mnRun = new JMenu("Run");
		mnRun.setMnemonic('R');
		menuBar.add(mnRun);
		
		mntmTagNamedEntities = new JMenuItem("Tag Named Entities");
		mntmTagNamedEntities.setMnemonic('T');
		mntmTagNamedEntities.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tag(arg0);
			}
		});
		mntmTagNamedEntities.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		mntmTagNamedEntities.setName("");
		mnRun.add(mntmTagNamedEntities);
		
		mnAbout = new JMenu("About");
		mnAbout.setMnemonic('A');
		menuBar.add(mnAbout);
		
		mntmAboutUs = new JMenuItem("About Us");
		mntmAboutUs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AboutUs();
			}
		});
		
		mntmAboutSystem = new JMenuItem("About System");
		mntmAboutSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AboutSystem();
			}
		});
		mntmAboutSystem.setMnemonic('S');
		mnAbout.add(mntmAboutSystem);
		mntmAboutUs.setMnemonic('U');
		mnAbout.add(mntmAboutUs);
		
		setSize(new Dimension(640, 550));
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@SuppressWarnings("unused")
	private void saveFile()	{

        SaveFileChooser.setFileFilter(filter);
        SaveFileChooser.setCurrentDirectory(new File("./"));
        int actionDialog = SaveFileChooser.showSaveDialog(this);
        if (actionDialog == JFileChooser.APPROVE_OPTION) {

            try {
                File fileName = new File(SaveFileChooser.getSelectedFile() + "");
                if (fileName == null)	{
                	return;
                }
                if (fileName.exists()) {
                    actionDialog = JOptionPane.showConfirmDialog(this,
                            "Replace existing file?");
                    if (actionDialog == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                try (BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName))) {
                    outFile.write(txtrInput.getText());
                    outFile.flush();
                }

            } catch (IOException ex) {
                Logger.getLogger(NERFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void openFile() throws IOException {
        OpenFileChooser.setFileFilter(filter);
        OpenFileChooser.setCurrentDirectory(new File("./"));
        if (OpenFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        	this.txtrInput.setText("");
            BufferedReader br = null;
            try {
                File f = OpenFileChooser.getSelectedFile();
                br = new BufferedReader(new FileReader(f));
                String st;
                while ((st = br.readLine()) != null) {
                    txtrInput.append(st);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NERFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(NERFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void exit(java.awt.event.ActionEvent evt) {
        ner.recordMEMM();
        this.dispose();
    }
    
    private void tag(java.awt.event.ActionEvent evt)	{
    	Highlighter h = this.txtrInput.getHighlighter();
        String input = this.txtrInput.getText();

        System.out.println("Tagging named entities...");
        List<NERToken> tokens = ner.memm.tag(input);
        System.out.println("Tagging named entities successful...");
        
        System.out.println("Writing output file...");
        this.writeOutputFile(tokens);
        System.out.println("see output file in output.txt...");
        
        int i = 0;
        for (NERToken token : tokens) {
            while (!(token.getLexeme().equals(input.substring(i, i + token.getLexeme().length())))) {
                i++;
            }
            try {
                int start = i;
                int end = i + token.getLexeme().length();
                i = end;
                if(token.getNERTag() == null)	continue;
                h.addHighlight(start, end, highlighter[token.getNERTag().ordinal()]);
            } catch (BadLocationException e) {
                Logger.getLogger(NERFrame.class.getName()).log(Level.SEVERE, null, highlighter[token.getNERTag().ordinal()]);
            }
        }
    }
    
	void writeOutputFile(List<NERToken> t)	{
		List<NERToken> tokens = new ArrayList<NERToken>(t);
		try {
			PrintWriter pf = new PrintWriter(new File("output.txt"));
			for(int i = 0; i < tokens.size(); i++)	{
				if(tokens.get(i).getNERTag() == NERTag.TITLE)	{
					int j = i;
					NERTag tag = tokens.get(j).getNERTag();
					while(j < tokens.size() && tokens.get(j).getNERTag() == tag)	{
						j++;
						if(!(j < tokens.size()))	break;
					}
					NERTag next_tag = tokens.get(j).getNERTag();
					if(next_tag == NERTag.PERSON)	{
						pf.append(" <PERSON><TITLE>");
						pf.append(tokens.get(i).getLexeme());
						i++;
						while(i < tokens.size() && tokens.get(i).getNERTag() == NERTag.TITLE)	{
							if(tokens.get(i).getTokenType() == TokenType.WORD)	pf.append(" ");
							pf.append(tokens.get(i).getLexeme());
							i++;
							if(!(i < tokens.size()))	break;
						}
						i--;
						pf.append("</TITLE>");
						i++;
						pf.append(tokens.get(i).getLexeme());
						i++;
						while(i < tokens.size() && tokens.get(i).getNERTag() == NERTag.PERSON)	{
							if(tokens.get(i).getTokenType() == TokenType.WORD)	pf.append(" ");
							pf.append(tokens.get(i).getLexeme());
							i++;
							if(!(i < tokens.size()))	break;
						}
						i--;
						pf.append("</PERSON>");
						continue;
					}
				}
				if(tokens.get(i).getNERTag() != null)	{
					NERTag tag = tokens.get(i).getNERTag();
					pf.append(" <" + tag.toString() + ">");
					pf.append(tokens.get(i).getLexeme());
					i++;
					while(i < tokens.size() && tokens.get(i).getNERTag() == tag)	{
						if(tokens.get(i).getTokenType() == TokenType.WORD)	pf.append(" ");
						pf.append(tokens.get(i).getLexeme());
						i++;
						if(!(i < tokens.size()))	break;
					}
					i--;
					pf.append("</" + tag.toString() + ">");
				}
				else	{
					if(i > 0 && tokens.get(i).getTokenType() == TokenType.WORD)	pf.append(" ");
					pf.append(tokens.get(i).getLexeme());
				}
			}
			pf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}