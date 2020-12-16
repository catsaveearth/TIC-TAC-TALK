package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * 완료
 * */

//findAllFriend

@SuppressWarnings("serial")
public class FindAllFriend extends JFrame {
	String searchFriend = "";
	
	public FindAllFriend() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(6, 1));
		JPanel blank = new JPanel();
		blank.setBackground(new Color(74, 210, 149));
		blank.setBorder(null);
		JPanel blank1 = new JPanel();
		blank1.setBackground(new Color(74, 210, 149));
		blank1.setBorder(null);
		JPanel blank2 = new JPanel();
		blank2.setBackground(new Color(74, 210, 149));
		blank2.setBorder(null);
		
		ImageIcon icon = new ImageIcon("image/findAllFriend.png");
	    Image titleImage = icon.getImage();
	    Image titleChangeImg = titleImage.getScaledInstance(350, 35, Image.SCALE_SMOOTH);
	    ImageIcon titleChangeIcon = new ImageIcon(titleChangeImg);
	    JButton name = new JButton();
	    name.setPreferredSize(new Dimension(100, 30));
	    name.setBounds(5, 5, 500, 15);
	    name.setIcon(titleChangeIcon);
	    //setting.setForeground(Color.red);
	    name.setBorder(null);
	    name.addActionListener(new ActionListener() {
	     @Override
	     	public void actionPerformed(ActionEvent e) {
	          // 만약 비밀번호가 맞으면 Setting setting = new Setting();
	          // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
	        }
	    });

		JTextField find = new JTextField(15);
		JPanel findPanel = new JPanel();
		findPanel.setBackground(new Color(74, 210, 149));
		
		JButton search = new JButton("SEARCH");
		search.setBackground(new Color(0, 54, 78));
		search.setForeground(Color.white);
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(new Color(74, 210, 149));
		search.setPreferredSize(new Dimension(90, 20));
		
		search.addActionListener(new ActionListener() {
	         @SuppressWarnings("unused")
			@Override
		     public void actionPerformed(ActionEvent e) {
	        	 searchFriend = find.getText(); // 찾을 친구의 이름을 string으로 저장
	        	 frame.dispose();

	        	 AllFriendList list = new AllFriendList(searchFriend);
	         }
	    });
		
		findPanel.add(find);
		btnPanel.add(search);
		
		frame.getContentPane().add(blank);
		frame.getContentPane().add(name);
		frame.getContentPane().add(findPanel);
		frame.getContentPane().add(btnPanel);
		frame.getContentPane().add(blank1);
		frame.getContentPane().add(blank2);

		frame.getContentPane().setBackground(new Color(74, 210, 149));
		
		frame.setTitle("Searching All Friend");
		frame.setVisible(true);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
}