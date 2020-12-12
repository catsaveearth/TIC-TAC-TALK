import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyFriendSelect extends JFrame {
	public MyFriendSelect() {
		JPanel panel = new JPanel();
		JPanel blank = new JPanel();
		JLabel label = new JLabel("                   Choose the Option!");
		label.setFont(new Font("맑은 고딕(본문)", Font.BOLD, 15));
		label.setPreferredSize(new Dimension(300, 30));
		JPanel btnPanel = new JPanel();
		JButton chatting = new JButton("1:1 채팅");
		JButton info = new JButton("정보보기");
			
		panel.add(blank);
		panel.add(label);
		btnPanel.add(chatting);
		btnPanel.add(info);
		panel.add(btnPanel, BorderLayout.SOUTH);

		chatting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ChattingOne chatting = new ChattingOne();
			}
		});
		
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				FriendInfo friend = new FriendInfo();
			}
		});
		
		add(panel);
		
		setVisible(true);
		setSize(300, 150);
		setLocationRelativeTo(null);
		setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyFriendSelect();
	}
}
