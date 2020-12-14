package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
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
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(5, 2));
		
		JPanel blank = new JPanel();
		blank.setBackground(new Color(74, 210, 149));
		blank.setBorder(null);
		JPanel blank1 = new JPanel();
		blank1.setBackground(new Color(74, 210, 149));
		blank1.setBorder(null);
		
		
		JPanel panel = new JPanel();
		

		ImageIcon icon = new ImageIcon("image/passwardcheck.png");
	    Image titleImage = icon.getImage();
	    Image titleChangeImg = titleImage.getScaledInstance(300, 50, Image.SCALE_SMOOTH);
	    ImageIcon titleChangeIcon = new ImageIcon(titleChangeImg);
	    JButton label = new JButton();
	    label.setPreferredSize(new Dimension(100, 30));
	    label.setBounds(5, 5, 15, 15);
	    label.setIcon(titleChangeIcon);
	    //setting.setForeground(Color.red);
	    label.setBorder(null);
	    label.addActionListener(new ActionListener() {
	     @Override
	     	public void actionPerformed(ActionEvent e) {
	          // 만약 비밀번호가 맞으면 Setting setting = new Setting();
	          // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
	        }
	    });
		
	    
	    JPanel pwPanel = new JPanel();
	    pwPanel.setBackground(new Color(74, 210, 149));
		JPasswordField passward = new JPasswordField(18);
	    
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(new Color(74, 210, 149));
		
		JButton checkBtn = new JButton("확인");
		checkBtn.setBounds(10, 10, 10, 10);
		checkBtn.setBackground(new Color(0, 54, 78));
		checkBtn.setForeground(Color.white);

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
		
		pwPanel.add(passward);
		btnPanel.add(checkBtn);
		frame.add(blank);
		frame.add(label);
		frame.add(pwPanel);
		frame.add(btnPanel);
		frame.add(blank1);
		
		frame.setVisible(true);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
}
