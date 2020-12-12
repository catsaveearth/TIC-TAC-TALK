import java.awt.*;
import javax.swing.*;

public class FriendInfo extends JFrame{

	public FriendInfo() {
        super.setLayout(new GridLayout(8, 1));
		//JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel name = new JLabel("닉네임(이름)");
		name.setFont(new Font("맑은고딕(본문)", Font.BOLD, 30));
		JLabel message = new JLabel("한줄메시지");
		message.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JPanel namePanel = new JPanel();
		namePanel.add(name);
		JPanel messagePanel = new JPanel();
		messagePanel.add(message);
		
		JLabel phoneNumber = new JLabel("전화번호");
		phoneNumber.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JLabel email = new JLabel("한줄메시지");
		email.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JPanel phoneNumberPanel = new JPanel();
		phoneNumberPanel.add(phoneNumber);
		JPanel emailPanel = new JPanel();
		emailPanel.add(email);
		
		add(namePanel);
		add(messagePanel);
		add(phoneNumberPanel);
		add(emailPanel);
		
		setVisible(true);
		setSize(500, 400);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FriendInfo info = new FriendInfo();
	}

}
