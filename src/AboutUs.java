import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;


public class AboutUs extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lblDevelopers;
	
	public AboutUs()	{
		super("About Us");
		setSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(224, 255, 255));
		
		lblDevelopers = new JLabel("Developers:");
		lblDevelopers.setBorder(new EmptyBorder(10, 20, 0, 0));
		lblDevelopers.setFont(new Font("Bradley Hand ITC", Font.BOLD | Font.ITALIC, 22));
		getContentPane().add(lblDevelopers, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 20, 20, 10));
		panel.setBackground(new Color(224, 255, 255));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblBolicoKenneth = new JLabel("Bolico, Kenneth");
		lblBolicoKenneth.setBounds(20, 0, 188, 53);
		lblBolicoKenneth.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblBolicoKenneth);
		
		JLabel lblPupStaMesa = new JLabel("PUP Sta. Mesa, Manila");
		lblPupStaMesa.setBounds(195, 0, 143, 53);
		lblPupStaMesa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(lblPupStaMesa);
		
		JLabel lblKennethemail = new JLabel("kenneth.bolico@gmail.com");
		lblKennethemail.setBounds(348, 0, 236, 53);
		lblKennethemail.setFont(new Font("Courier New", Font.PLAIN, 14));
		panel.add(lblKennethemail);
		
		JLabel lblNagaJohnMark = new JLabel("Naga, John Mark");
		lblNagaJohnMark.setBounds(20, 53, 188, 53);
		lblNagaJohnMark.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblNagaJohnMark);
		
		JLabel label = new JLabel("PUP Sta. Mesa, Manila");
		label.setBounds(195, 53, 143, 53);
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(label);
		
		JLabel lblJmemail = new JLabel("johnmarknaga@yahoo.com");
		lblJmemail.setBounds(348, 53, 236, 53);
		lblJmemail.setFont(new Font("Courier New", Font.PLAIN, 14));
		panel.add(lblJmemail);
		
		JLabel lblVillanuevaJohnEris = new JLabel("Villanueva, John Eris");
		lblVillanuevaJohnEris.setBounds(20, 106, 188, 53);
		lblVillanuevaJohnEris.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblVillanuevaJohnEris);
		
		JLabel label_1 = new JLabel("PUP Sta. Mesa, Manila");
		label_1.setBounds(195, 106, 143, 53);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(label_1);
		
		JLabel labelErisemail = new JLabel("johnerisvillanueva@gmail.com");
		labelErisemail.setBounds(348, 106, 236, 53);
		labelErisemail.setFont(new Font("Courier New", Font.PLAIN, 14));
		panel.add(labelErisemail);
		
		JLabel lblVillanuevaJohnRobert = new JLabel("Villanueva, John Robert");
		lblVillanuevaJohnRobert.setBounds(20, 159, 188, 53);
		lblVillanuevaJohnRobert.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(lblVillanuevaJohnRobert);
		
		JLabel label_2 = new JLabel("PUP Sta. Mesa, Manila");
		label_2.setBounds(195, 159, 143, 53);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(label_2);
		
		JLabel labelBertemail = new JLabel("treborisoka07@gmail.com");
		labelBertemail.setBounds(348, 159, 236, 53);
		labelBertemail.setFont(new Font("Courier New", Font.PLAIN, 14));
		panel.add(labelBertemail);
		
		
		setBackground(new Color(176, 224, 230));
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

}
