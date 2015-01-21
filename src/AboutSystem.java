import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;


public class AboutSystem extends JFrame {

	private static final long serialVersionUID = 1L;

	public AboutSystem()	{
		super("About System");
		setSize(new Dimension(500, 300));
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		getContentPane().setBackground(new Color(224, 255, 255));
		getContentPane().setLayout(null);
		
		JLabel lblNermemm = new JLabel("(NER-MEMM)");
		lblNermemm.setFont(new Font("Consolas", Font.PLAIN, 18));
		lblNermemm.setBounds(189, 103, 122, 27);
		getContentPane().add(lblNermemm);
		
		JLabel lblNamedEntityRecognition = new JLabel("Named Entity Recognizer");
		lblNamedEntityRecognition.setFont(new Font("Consolas", Font.PLAIN, 18));
		lblNamedEntityRecognition.setBounds(127, 24, 261, 27);
		getContentPane().add(lblNamedEntityRecognition);
		
		JLabel lblForFilipinoText = new JLabel("for Filipino Text");
		lblForFilipinoText.setFont(new Font("Consolas", Font.PLAIN, 18));
		lblForFilipinoText.setBounds(153, 50, 193, 27);
		getContentPane().add(lblForFilipinoText);
		
		JLabel lblUsingMaximumEntropy = new JLabel("using Maximum Entropy Markov Model");
		lblUsingMaximumEntropy.setFont(new Font("Consolas", Font.PLAIN, 18));
		lblUsingMaximumEntropy.setBounds(81, 76, 367, 27);
		getContentPane().add(lblUsingMaximumEntropy);
		
		JLabel lblNewLabel = new JLabel("This system is a project of PUP students taking BS Computer Science ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(60, 153, 408, 25);
		getContentPane().add(lblNewLabel);
		
		JLabel lblInTheSubject = new JLabel("in the subject Natural Language Processing and Special Project under the");
		lblInTheSubject.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblInTheSubject.setBounds(31, 178, 426, 25);
		getContentPane().add(lblInTheSubject);
		
		JLabel lblSupervisionOfProf = new JLabel("supervision of Prof. Ria Sagum and Prof. Ma. Esperanza Reyes.");
		lblSupervisionOfProf.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSupervisionOfProf.setBounds(31, 203, 426, 25);
		getContentPane().add(lblSupervisionOfProf);
		
		setVisible(true);
	}
}
