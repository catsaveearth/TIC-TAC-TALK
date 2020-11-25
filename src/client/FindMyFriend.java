package client;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FindMyFriend extends JFrame {
	String searchFriend = "";
	
	public FindMyFriend() {
		super.setLayout(new GridLayout(6, 1));
		//JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JPanel blank = new JPanel();
		JLabel name = new JLabel("내 친구 찾기");
		JPanel namePanel = new JPanel();
		name.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JTextField find = new JTextField(15);
		JPanel findPanel = new JPanel();
		JButton search = new JButton("SEARCH");
		JPanel btnPanel = new JPanel();
		search.setPreferredSize(new Dimension(90, 20));
		
		search.addActionListener(new ActionListener() {
	         @Override
		     public void actionPerformed(ActionEvent e) {
	        	 searchFriend = find.getText(); // 찾을 친구의 이름을 string으로 저장
	        	 MyFriendList list = new MyFriendList();
	        	 
	        	 /*int result = 0;
	        	 // 친구 있을때
	        	 result = JOptionPane.showConfirmDialog(null, "1:1 채팅을 진행하시겠습니까?");
	        	 // 1:1채팅으로 바로 갈지
	        	 // 친구 정보 보는걸로 할지
	        	 System.out.println(result);
	        	 if (result == 0) {
	        		 dispose();
	        		 MyFriendSelect select = new MyFriendSelect();
	        	 } else if (result == 1) {
	        		 dispose();
	        	 }*/
	        	 // 일부 글자만 써도 나오게 할건지 전체 풀 네임쳐야 나오게 할건지 의논
	        	 // 만약 있다면 그 사람의 정보 띄우기? 혹은 친구 신청 창 띄우기?
	        	 // 없다면 다시 검색
	        	 // if (없다면) {
	        	 // 	JOptionPane.showMessageDialog(null,  "Not Exist!!");
	        	 // 다시 검색하게 할건지 창 종료할건지
	        	 // }
		     }
	    });
		
		namePanel.add(name);
		findPanel.add(find);
		btnPanel.add(search);
		
		add(blank);
		add(namePanel);
		add(findPanel);
		add(btnPanel);
		
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindMyFriend frame = new FindMyFriend();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
