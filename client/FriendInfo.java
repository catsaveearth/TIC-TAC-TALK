package client;
import java.awt.*;
import javax.swing.*;


/**
 * 완료
 * */


public class FriendInfo extends JFrame{

	//type : 0 - NF, 1 - F
	public FriendInfo(String ID, int type) {
        super.setLayout(new GridLayout(8, 1));
		//JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel name = new JLabel("닉네임(이름)");
		name.setFont(new Font("맑은고딕(본문)", Font.BOLD, 30));
		JLabel message = new JLabel("한줄메시지");
		message.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JLabel email = new JLabel("이메일");
		email.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		

		JLabel phone = new JLabel("전화번호");
		phone.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JLabel birth = new JLabel("생일");
		birth.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		JLabel github = new JLabel("깃헙");
		github.setFont(new Font("맑은고딕(본문)", Font.BOLD, 15));
		
		
		JPanel namePanel = new JPanel();
		namePanel.add(name);
		JPanel messagePanel = new JPanel();
		messagePanel.add(message);
		JPanel emailPanel = new JPanel();
		emailPanel.add(email);
		
		JPanel phonePanel = new JPanel();
		phonePanel.add(phone);
		JPanel birthPanel = new JPanel();
		birthPanel.add(birth);
		JPanel githubPanel = new JPanel();
		githubPanel.add(github);
		
		add(namePanel);
		add(messagePanel);
		add(emailPanel);
		
		//정보 받아오기
		String info[] = Client.getFriendInfo(ID);
		// [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
		
		for(String q : info) {
			System.out.println(q);
		}
		
		
		String line1 = info[0] + "(" + info[1] + ")";
		name.setText(line1);
		message.setText(info[2]);
		email.setText(info[3]);
		phone.setText(info[4]);
		birth.setText(info[5]);
		github.setText(info[6]);

		
		//친구라면 정보 좀 더 보여줌
		if(type == 1) {
			add(phonePanel);
			add(birthPanel);
			add(githubPanel);
		}

		setVisible(true);
		setSize(500, 400);
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
