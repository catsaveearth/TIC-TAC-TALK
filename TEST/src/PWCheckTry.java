import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PWCheckTry extends JFrame {
	
	public PWCheckTry() {
        super.setLayout(new GridLayout(3, 1));
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Try Again?");
		JPanel btnPanel = new JPanel();
		JButton retry = new JButton("RETRY");
		JButton back = new JButton("BACK");
			
		panel.add(label);
		btnPanel.add(retry);
		btnPanel.add(back);
		// 버튼이 밑으로 나오게 수정

		retry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				PWCheck check = new PWCheck();
			}
		});
			
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainScreen back = new MainScreen();
			}
		});
		
		add(panel);
		add(btnPanel);
		
		setVisible(true);
		setSize(300, 150);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PWCheckTry frame = new PWCheckTry();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
