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
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(15, 2));
		frame.setBackground(new Color(74, 210, 149));
		
		JPanel blank = new JPanel();
		blank.setBackground(new Color(74, 210, 149));
		JPanel blank1 = new JPanel();
		blank1.setBackground(new Color(74, 210, 149));
		JPanel blank2 = new JPanel();
		blank2.setBackground(new Color(74, 210, 149));
		JPanel blank3 = new JPanel();
		blank3.setBackground(new Color(74, 210, 149));
		JPanel blank4 = new JPanel();
		blank4.setBackground(new Color(74, 210, 149));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(new Color(74, 210, 149));
		JPanel IDPanel = new JPanel();
		IDPanel.setBackground(new Color(74, 210, 149));
		JPanel NickNamePanel = new JPanel();
		NickNamePanel.setBackground(new Color(74, 210, 149));
		JPanel PWPanel = new JPanel();
		PWPanel.setBackground(new Color(74, 210, 149));
		JPanel NamePanel = new JPanel();
		NamePanel.setBackground(new Color(74, 210, 149));
		JPanel PNPanel = new JPanel();
		PNPanel.setBackground(new Color(74, 210, 149));
		JPanel EmailPanel = new JPanel();
		EmailPanel.setBackground(new Color(74, 210, 149));
		JPanel BirthPanel = new JPanel();
		BirthPanel.setBackground(new Color(74, 210, 149));
		JPanel GithubPanel = new JPanel();
		GithubPanel.setBackground(new Color(74, 210, 149));
		
		ImageIcon icon = new ImageIcon("image/signupTitle.png");
	    Image titleImage = icon.getImage();
	    Image titleChangeImg = titleImage.getScaledInstance(600, 40, Image.SCALE_SMOOTH);
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
		panelBtn.setBackground(new Color(74, 210, 149));
		JButton backBtn = new JButton("BACK");
		backBtn.setBackground(new Color(0, 54, 78));
		backBtn.setForeground(Color.white);
		JButton signUpBtn = new JButton("SIGN UP");
		signUpBtn.setForeground(Color.white);
		signUpBtn.setBackground(new Color(0, 54, 78));
		
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
				frame.dispose();
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
					frame.dispose();
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
		
		frame.add(blank);
		frame.add(label);
		frame.add(IDPanel);
		frame.add(NickNamePanel);
		frame.add(PWPanel);
		frame.add(NamePanel);
		frame.add(PNPanel);
		frame.add(EmailPanel);
		frame.add(BirthPanel);
		frame.add(GithubPanel);
		frame.add(panelBtn);
		frame.add(blank1);
		frame.add(blank2);
		frame.add(blank3);
		frame.add(blank4);
		
		frame.setVisible(true);
		frame.setSize(450, 550);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

}