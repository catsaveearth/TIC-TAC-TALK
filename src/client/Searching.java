package client;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Searching extends JFrame {

	public Searching() {
		JFrame frame = new JFrame();
		JLabel label = new JLabel("Searching");
		
		JButton back = new JButton("BACK");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainScreen main = new MainScreen();
			}
			
		});
		
		frame.getContentPane().add(label);
		frame.getContentPane().add(back);
		
		frame.setVisible(true);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Searching Searching = new Searching();
	}

}
