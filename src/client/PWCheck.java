package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * 공사 끝
 * */


public class PWCheck extends JFrame {
	
	public PWCheck() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("                   Write your password!!");
		label.setFont(new Font("맑은 고딕(본문)", Font.BOLD, 15));
		label.setPreferredSize(new Dimension(300, 50));
		JPasswordField passward = new JPasswordField(15);
		JPanel btnPanel = new JPanel();
		JButton checkBtn = new JButton("확인");
			
		panel.add(label);
		panel.add(passward);
		btnPanel.add(checkBtn);
		panel.add(btnPanel, BorderLayout.SOUTH);

		checkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Client.pwcheck(passward.getPassword())) {
					dispose();
					Setting setting = new Setting();
				} else {
					JOptionPane.showMessageDialog(null,  "Wrong!!");
				}
			}
		});
		
		add(panel);
		
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
