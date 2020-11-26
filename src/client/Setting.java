package client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Setting extends JFrame {
	public Setting() {
		super.setLayout(new GridLayout(16, 2));
		JPanel panel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel IDPanel = new JPanel();
		JPanel NickNamePanel = new JPanel();
		JPanel PWPanel = new JPanel();
		JPanel NamePanel = new JPanel();
		JPanel PNPanel = new JPanel();
		JPanel EmailPanel = new JPanel();
		JPanel BirthPanel = new JPanel();
		JPanel GithubPanel = new JPanel();
		JPanel messagePanel = new JPanel();
		JLabel label = new JLabel("Please modify your information!");
		JLabel label1 = new JLabel("비밀번호를 변경하지 않는다면 비밀번호 칸을 비워두세요");

		JLabel labelID = new JLabel("ID                           : ");
		JLabel labelNickName = new JLabel("NickName           : ");
		JLabel labelPW = new JLabel("Password           : ");
		JLabel labelName = new JLabel("Name                   : ");
		JLabel labelPN = new JLabel("Phone Number  : ");
		JLabel labelEmail = new JLabel("Email                    : ");
		JLabel labelBirth = new JLabel("Birth                     : ");
		JLabel labelGit = new JLabel("GitHub                  : ");
		JLabel labelMessage = new JLabel("Message             : ");
		
		JTextField ID = new JTextField(15);
		ID.setEditable(false);
		JTextField NickName = new JTextField(15);
		JPasswordField txtPass = new JPasswordField(15);
		JTextField name = new JTextField(15);
		JTextField phoneNumber = new JTextField(15);
		JTextField email = new JTextField(15);
		JTextField github = new JTextField(15);
		JTextField smessage = new JTextField(15);
		
		
		String[] year = new String[71];
		String yearSelect = "";
		int yearStart = 1950;
		for (int i = 0; i <= 70; i++) {
			year[i] = "  " + Integer.toString(yearStart);
			yearStart++;
		}
		
		JComboBox yearCombo = new JComboBox(year); 
		add(yearCombo);

		String[] month = { "  1", "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", "  10", "  11", "  12" };
		JComboBox monthCombo = new JComboBox(month); 
		add(monthCombo);
		
		String[] day = new String[31];
		int dayStart = 1;
		for (int i = 0; i < 31; i++) {
			day[i] = "  " + Integer.toString(dayStart);
			dayStart++;
		}
		
		JComboBox dayCombo = new JComboBox(day); 
		add(dayCombo);	
		
		JPanel panelBtn = new JPanel();
		JButton SettingBtn = new JButton("SAVE");
		
		panel.add(label);
		panel1.add(label1);
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
		messagePanel.add(labelMessage);
		messagePanel.add(smessage);
		panelBtn.add(SettingBtn, BorderLayout.SOUTH);
		
		
		//정보 불러오기!![ID NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]
		String[] infoo = Client.settinginfo();
		for(String k : infoo) {
			System.out.println(k);
		}
		
		
		ID.setText(infoo[0]);
		NickName.setText(infoo[1]);
		name.setText(infoo[2]);
		phoneNumber.setText(infoo[3]);
		email.setText(infoo[4]);
		
		String Y = infoo[5].substring(0, 2);
		int M = Integer.parseInt(infoo[5].substring(2, 4));
		int D = Integer.parseInt(infoo[5].substring(4, 6));

		
		if(Integer.parseInt(Y) >= 50) Y = "19" + Y;
		else Y = "20" + Y;
		
		int Yy = Integer.parseInt(Y) - 1950;

		yearCombo.setSelectedIndex(Yy);
		monthCombo.setSelectedIndex(M - 1);;
		dayCombo.setSelectedIndex(D - 1);
		
		
		if(infoo[6].compareTo("null") != 0) 
			github.setText(infoo[6]);
		if(infoo[7].compareTo("null") != 0) 
			smessage.setText(infoo[7]);

		
		
		
		//setting save 버튼 액션!
		SettingBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String git = "0";
				
				if(NickName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "NickName을 입력하세요");
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
				String message = NickName.getText() + "`|" + name.getText() 
				                 + "`|" + phoneNumber.getText() + "`|"
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

				String Git = null;
				String state_m = null;
				//생일, 깃헙, 상메 더해주기
				if (github.getText().equals("")) {
					Git = "";
				}
				else Git = github.getText();
				
				if (smessage.getText().equals("")) {
					state_m = "";
				}
				else state_m = smessage.getText();
				
				message = message + year + m + d + "`|" + Git + "`|" + smessage.getText();


				//pw입력됬는지 확인
				//+ " " + String.valueOf(txtPass.getPassword())
				if(String.valueOf(txtPass.getPassword()).equals("")) {
					System.out.println("pw가 현재 null입니다.");
					message = "0`|" + message;
				}
				else {
					System.out.println("pw가 현재 값이 있습니다.");
					message = "1`|" + String.valueOf(txtPass.getPassword()) + "`|" + message;
				}
				
				//이제 client에서 update!!
				int tf = Client.modifyInfo(message);
				

				if(tf == 0) {
					JOptionPane.showMessageDialog(null, "SUCCESS!!");
				}
				else if(tf == 1) {
					//뭐 아이디가 중복된다, 그런거 알려줘야 하나???
					JOptionPane.showMessageDialog(null, "아이디 중복!!");
				}			
			}
		});
		
		JButton back = new JButton("BACK");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}			
		});
			
		
		add(panel);
		add(panel1);
		add(IDPanel);
		add(NickNamePanel);
		add(PWPanel);
		add(NamePanel);
		add(PNPanel);
		add(EmailPanel);
		add(BirthPanel);
		add(GithubPanel);
		add(messagePanel);
		add(panelBtn);
		
		setVisible(true);
		setSize(450, 600);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
}
