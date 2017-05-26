package menu;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

public class prova extends JFrame {

	private JPanel contentPane;
	private JTextField txtProva;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					prova frame = new prova();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public prova() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtProva = new JTextField();
		txtProva.setText("prova");
		txtProva.setBounds(132, 68, 192, 63);
		contentPane.add(txtProva);
		txtProva.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(24, 23, 82, 73);
		contentPane.add(panel);
	}
}
