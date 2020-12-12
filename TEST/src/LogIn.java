import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LogIn extends JFrame {

	public LogIn() {
		super.setLayout(new GridLayout(5, 2));
		JPanel blank = new JPanel();
		JPanel panel = new JPanel();
		
		JPanel IDPanel = new JPanel();
		JLabel label = new JLabel("ID                 : ");
		JTextField txtID = new JTextField(10);
		
		JPanel PWPanel = new JPanel();
		JLabel pswrd = new JLabel("Password : ");
		JPasswordField txtPass = new JPasswordField(10);
		
		JPanel BtnPanel = new JPanel();
		JButton logBtn = new JButton("Log In");
		JButton signUpBtn = new JButton("SIGN UP");
		
		IDPanel.add(label);
		IDPanel.add(txtID);
		PWPanel.add(pswrd);
		PWPanel.add(txtPass);
		BtnPanel.add(logBtn);
		BtnPanel.add(signUpBtn);
		
		logBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ID = "jimin";
				String pass = "0000";
				
				if (ID.equals(txtID.getText()) && pass.equals(txtPass.getText())) {
					//JOptionPane.showMessageDialog(null,  "You have logged in successfully!");
					// 초기화면 나오도록 하기!!
					dispose();
					MainScreen frame = new MainScreen();
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
		
		add(blank);
		add(IDPanel);
		add(PWPanel);
		add(BtnPanel);
		
		setVisible(true);
		setSize(300, 220);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		new LogIn();
	}
}
