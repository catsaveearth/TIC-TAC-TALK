package client;

/**
 * 공사 끝
 * */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LogIn extends JFrame {

	public LogIn() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(6, 2));
		frame.setBackground(new Color(74, 210, 149));
		
		JPanel blank = new JPanel();
		blank.setBackground(new Color(74, 210, 149));
		JPanel blank1 = new JPanel();
		blank1.setBackground(new Color(74, 210, 149));
		blank1.setBorder(null);
		JPanel panel = new JPanel();
		panel.setBackground(new Color(74, 210, 149));
		
		
		ImageIcon icon = new ImageIcon("image/LogInTitle.png");
		    Image titleImage = icon.getImage();
		    Image titleChangeImg = titleImage.getScaledInstance(300, 34, Image.SCALE_SMOOTH);
		    ImageIcon titleChangeIcon = new ImageIcon(titleChangeImg);
		    JButton titleBtn = new JButton();
		    titleBtn.setPreferredSize(new Dimension(100, 30));
		    titleBtn.setBounds(5, 5, 15, 15);
		    titleBtn.setIcon(titleChangeIcon);
		    //setting.setForeground(Color.red);
		    titleBtn.setBorder(null);
		    titleBtn.addActionListener(new ActionListener() {
		     @Override
		     	public void actionPerformed(ActionEvent e) {
		          // 만약 비밀번호가 맞으면 Setting setting = new Setting();
		          // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
		        }
		});
		
		JPanel IDPanel = new JPanel();
		IDPanel.setBackground(new Color(74, 210, 149));
		JLabel label = new JLabel("ID                 : ");
		label.setFont(new Font("고딕", Font.BOLD, 12));
		label.setForeground(Color.black);
		JTextField txtID = new JTextField(10);
		
		JPanel PWPanel = new JPanel();
		PWPanel.setBackground(new Color(74, 210, 149));
		JLabel pswrd = new JLabel("Password : ");
		pswrd.setFont(new Font("고딕", Font.BOLD, 12));
		pswrd.setForeground(Color.black);
		JPasswordField txtPass = new JPasswordField(10);
		
		JPanel BtnPanel = new JPanel();
		BtnPanel.setBackground(new Color(74, 210, 149));
		JButton logBtn = new JButton("LOGIN");
		logBtn.setForeground(Color.white);
		JButton signUpBtn = new JButton("SIGN-UP");
		signUpBtn.setForeground(Color.white);
		
		logBtn.setBackground(new Color(0, 54, 78));
		logBtn.setPreferredSize(new Dimension(80, 25));
		logBtn.setBorder(null);
		signUpBtn.setBackground(new Color(0, 54, 78));
		signUpBtn.setPreferredSize(new Dimension(80, 25));
		signUpBtn.setBorder(null);

		logBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean log = Client.logincheck(txtID.getText(), txtPass.getPassword());
						
				if (log) {//로그인 성공하면
					dispose();
					MainScreen frame = new MainScreen(); //main으로 고고씽~
					
				} else {
					JOptionPane.showMessageDialog(null,  "You failed to log in");
				}
			}
			
		});
		
		signUpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				SignUp frame = new SignUp();
			}
			
		});
		
		IDPanel.add(label);
		IDPanel.add(txtID);
		PWPanel.add(pswrd);
		PWPanel.add(txtPass);
		BtnPanel.add(logBtn);
		BtnPanel.add(signUpBtn);
		
		frame.add(blank);
		frame.add(titleBtn);
		frame.add(IDPanel);
		frame.add(PWPanel);
		frame.add(BtnPanel);
		frame.add(blank1);
		
		frame.setVisible(true);
		frame.setSize(300, 220);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
