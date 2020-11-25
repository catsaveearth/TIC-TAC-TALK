package client;

/**
 * 공사 끝
 * */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SignUp extends JFrame {
	public SignUp() {
		super.setLayout(new GridLayout(15, 2));
		JPanel panel = new JPanel();
		JPanel infoPanel = new JPanel();
		JPanel IDPanel = new JPanel();
		JPanel NickNamePanel = new JPanel();
		JPanel PWPanel = new JPanel();
		JPanel NamePanel = new JPanel();
		JPanel PNPanel = new JPanel();
		JPanel EmailPanel = new JPanel();
		JPanel BirthPanel = new JPanel();
		JPanel GithubPanel = new JPanel();
		JLabel label = new JLabel("Please write your information!");
		JLabel labelID = new JLabel("ID                           : ");
		JLabel labelNickName = new JLabel("NickName           : ");
		JLabel labelPW = new JLabel("Password           : ");
		JLabel labelName = new JLabel("Name                   : ");
		JLabel labelPN = new JLabel("Phone Number  : ");
		JLabel labelEmail = new JLabel("Email                    : ");
		JLabel labelBirth = new JLabel("Birth                     : ");
		
		String[] year = new String[71];
		String yearSelect = "";
		int yearStart = 1950;
		for (int i = 0; i <= 70; i++) {
			year[i] = "  " + Integer.toString(yearStart);
			yearStart++;
		}
		
		setLayout(new FlowLayout());
		JComboBox yearCombo = new JComboBox(year); 
		add(yearCombo);

		String[] month = { "  1", "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", "  10", "  11", "  12" };
		String monthSelect = "";
		setLayout(new FlowLayout());
		JComboBox monthCombo = new JComboBox(month); 
		add(monthCombo);
		
		String[] day = new String[31];
		String daySelect = "";
		int dayStart = 1;
		for (int i = 0; i < 31; i++) {
			day[i] = "  " + Integer.toString(dayStart);
			dayStart++;
		}
		
		setLayout(new FlowLayout());
		JComboBox dayCombo = new JComboBox(day); 
		add(dayCombo);
		

		
		
		JLabel labelGit = new JLabel("GitHub                  : ");
		JTextField ID = new JTextField(15);
		JTextField NickName = new JTextField(15);
		JPasswordField txtPass = new JPasswordField(15);
		JTextField name = new JTextField(15);
		JTextField phoneNumber = new JTextField(15);
		JTextField email = new JTextField(15);
		JTextField github = new JTextField(15);
		JPanel panelBtn = new JPanel();
		JButton backBtn = new JButton("BACK");
		JButton signUpBtn = new JButton("SIGN UP");
		
		panel.add(label, BorderLayout.NORTH);
		IDPanel.add(labelID);
		IDPanel.add(ID);
		NickNamePanel.add(labelNickName);
		NickNamePanel.add(NickName);
		PWPanel.add(labelPW);
		PWPanel.add(txtPass);
		NamePanel.add(labelName);
		NamePanel.add(name);
		PNPanel.add(labelPN);
		PNPanel.add(phoneNumber);
		EmailPanel.add(labelEmail);
		EmailPanel.add(email);
		BirthPanel.add(labelBirth);
		BirthPanel.add(yearCombo);
		BirthPanel.add(monthCombo);
		BirthPanel.add(dayCombo);
		GithubPanel.add(labelGit);
		GithubPanel.add(github);
		panelBtn.add(backBtn);
		panelBtn.add(signUpBtn);
		
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				LogIn check = new LogIn();
				// 중복 체크 해주기!!
			}
		});
		
		signUpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String git = "0";
				
				if(ID.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "ID를 입력하세요");
					return;
				}
				
				if(NickName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "NickName을 입력하세요");
					return;
				}
				
				if(String.valueOf(txtPass.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요");
					return;
				}
				
				if(name.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "이름을 입력하세요");
					return;
				}
				
				if(phoneNumber.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "전화번호를 입력하세요");
					return;
				}
				
				if(email.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "email을 입력하세요");
					return;
				}

			
				//모든 조건을 통과하면 그제야 준다
				String message = ID.getText() + "`|" + NickName.getText()
								+ "`|" + String.valueOf(txtPass.getPassword()) + "`|"
								+ name.getText() + "`|" + phoneNumber.getText() + "`|"
								+ email.getText() + "`|";
				
				String year = yearCombo.getSelectedItem().toString().substring(4);
				int month = Integer.parseInt(monthCombo.getSelectedItem().toString().trim());
				int day = Integer.parseInt(dayCombo.getSelectedItem().toString().trim());
				String m = null;
				String d = null;
				
				if(month < 10) m = "0" + month;	
				else m = Integer.toString(month);
				if(day < 10) d = "0" + day;
				else d = Integer.toString(day);

				message = message + year + m + d;
				
				if(github.getText().compareTo("") != 0) {
					git = "1";
					message = message + "`|" + github.getText();
				}

				//이제 client에서 함수 불러서 Server와 소통해서 Check
				int tf = Client.register(git, message);
				

				if(tf == 0) {
					JOptionPane.showMessageDialog(null, "SUCCESS!!");
					dispose();
					LogIn check = new LogIn();
				}
				else if(tf == 1) {
					//뭐 아이디가 중복된다, 그런거 알려줘야 하나???
					JOptionPane.showMessageDialog(null, "아이디 중복!!");
			
				}
				else if(tf == 2) {
					JOptionPane.showMessageDialog(null, "닉네임 중복!!");

				}
				else if(tf == 3) {
					JOptionPane.showMessageDialog(null, "Fail!!");
				}
			}
		});
		

		add(panel);
		add(IDPanel);
		add(NickNamePanel);
		add(PWPanel);
		add(NamePanel);
		add(PNPanel);
		add(EmailPanel);
		add(BirthPanel);
		add(GithubPanel);
		add(panelBtn);
		
		setVisible(true);
		setSize(450, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

}
