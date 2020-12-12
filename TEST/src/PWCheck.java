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

public class PWCheck extends JFrame {
	
	public PWCheck() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("                   Write your password!!");
		label.setFont(new Font("맑은 고딕(본문)", Font.BOLD, 15));
		label.setPreferredSize(new Dimension(300, 50));
		JPasswordField passward = new JPasswordField(10);
		JPanel PWPanel = new JPanel();
		
		JPanel btnPanel = new JPanel();
		JButton checkBtn = new JButton("확인");
			
		panel.add(label);
		panel.add(passward);
		btnPanel.add(checkBtn);
		panel.add(btnPanel, BorderLayout.SOUTH);

		checkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass = "0000";
				
				if (pass.equals(passward.getText())) {
					//JOptionPane.showMessageDialog(null,  "You have logged in successfully!");
					// 초기화면 나오도록 하기!!
					dispose();
					Setting setting = new Setting();
				} else {
					JOptionPane.showMessageDialog(null,  "Wrong!!");
					dispose();
					String[] buttons = {"RETRY", "BACK"};
					int result = JOptionPane.showOptionDialog(null, "Try Again?", "TRY", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, "두번째값");
					if (result == 0) {
						dispose();
						PWCheck check = new PWCheck();
					} else if (result == 1) {
						dispose();
						MainScreen back = new MainScreen();
					}
				}
			}
		});
		
		add(panel);
		
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PWCheck frame = new PWCheck();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
