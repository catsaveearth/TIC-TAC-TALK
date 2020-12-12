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
		
		// 콤보박스에 Action 리스너 등록. 선택된 아이템의 이미지 출력
		yearCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(yearCombo.getSelectedItem().toString().substring(2));
			}
		});
		
		
		String[] month = { "  1", "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", "  10", "  11", "  12" };
		String monthSelect = "";
		setLayout(new FlowLayout());
		JComboBox monthCombo = new JComboBox(month); 
		add(monthCombo);
		
		// 콤보박스에 Action 리스너 등록. 선택된 아이템의 이미지 출력
		monthCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(monthCombo.getSelectedItem().toString().substring(2));
			}
		});
		
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
		
		// 콤보박스에 Action 리스너 등록. 선택된 아이템의 이미지 출력
		dayCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(dayCombo.getSelectedItem().toString().substring(2));
			}
		});
		
		
		
		JLabel labelGit = new JLabel("GitHub                  : ");
		JTextField ID = new JTextField(15);
		JTextField NickName = new JTextField(15);
		JPasswordField txtPass = new JPasswordField(15);
		JTextField name = new JTextField(15);
		JTextField phoneNumber = new JTextField(15);
		JTextField email = new JTextField(15);
		JTextField birth = new JTextField(15); // 특정 형태 정해주기
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
				JOptionPane.showMessageDialog(null,  "SUCCESS!!");
				dispose();
				LogIn check = new LogIn();
				// 중복 체크 해주기!!
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
