package client;
import java.awt.*;
import javax.swing.*;


/**
 * 완료
 * */


public class FriendInfo extends JFrame{

	//type : 0 - NF, 1 - F
	public FriendInfo(String ID, int type) {
		JFrame frame = new JFrame();
		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(500, 60));
		namePanel.setBackground(new Color(0, 54, 78));
		JLabel nameLabel = new JLabel("이름들어갑니다");
		nameLabel.setFont(new Font("나눔바른펜", Font.BOLD, 40));
		nameLabel.setForeground(Color.white);
		
		namePanel.add(nameLabel);

		JPanel infoPanel = new JPanel();
		JPanel messagePanel = new JPanel();
		messagePanel.setPreferredSize(new Dimension(500, 50));
		messagePanel.setBackground(Color.white);
		messagePanel.setBorder(null);
		JButton messageBtn = new JButton();
		messageBtn.setBounds(0, 70, 150, 50);
		messageBtn.setBorder(null);
		ImageIcon icon1 = new ImageIcon("image/message.png");
	    Image messageImage = icon1.getImage();
	    Image messageChangeImg = messageImage.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
	    ImageIcon messageChangeIcon = new ImageIcon(messageChangeImg);
	    messageBtn.setIcon(messageChangeIcon);
		JPanel messageLabelPanel = new JPanel();
		messageLabelPanel.setPreferredSize(new Dimension(330, 50));
		messageLabelPanel.setBackground(Color.white);
		JLabel messageLabel = new JLabel("한줄메시지들어갑니다");
		messageLabel.setFont(new Font("나눔바른펜", Font.BOLD, 20));
		messageLabel.setForeground(Color.black);
		JPanel messageIcon = new JPanel();
		messageIcon.setPreferredSize(new Dimension(150, 50));
		messageIcon.setBackground(new Color(74, 210, 149));
		
		messageLabelPanel.add(messageLabel);
		messagePanel.add(messageIcon);
		messagePanel.add(messageLabelPanel);
		
		JPanel emailPanel = new JPanel();
		emailPanel.setPreferredSize(new Dimension(500, 50));
		emailPanel.setBackground(Color.white);
		emailPanel.setBorder(null);
		JButton emailBtn = new JButton();
		emailBtn.setBounds(0, 125, 150, 50);
		emailBtn.setBorder(null);
		ImageIcon icon2 = new ImageIcon("image/email.png");
	    Image emailImage = icon2.getImage();
	    Image emailChangeImg = emailImage.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
	    ImageIcon emailChangeIcon = new ImageIcon(emailChangeImg);
	    emailBtn.setIcon(emailChangeIcon);
		JPanel emailLabelPanel = new JPanel();
		emailLabelPanel.setPreferredSize(new Dimension(330, 50));
		emailLabelPanel.setBackground(Color.white);
		JLabel emailLabel = new JLabel("이메일들어갑니다");
		emailLabel.setFont(new Font("나눔바른펜", Font.BOLD, 20));
		emailLabel.setForeground(Color.black);
		JPanel emailIcon = new JPanel();
		emailIcon.setPreferredSize(new Dimension(150, 50));
		emailIcon.setBackground(new Color(74, 210, 149));
		
		emailLabelPanel.add(emailLabel);
		emailPanel.add(emailIcon);
		emailPanel.add(emailLabelPanel);
		
		
		
		
		
		JPanel phonePanel = new JPanel();
		phonePanel.setPreferredSize(new Dimension(500, 50));
		phonePanel.setBackground(Color.white);
		phonePanel.setBorder(null);
		JButton phoneBtn = new JButton();
		phoneBtn.setBounds(0, 180, 150, 50);
		phoneBtn.setBorder(null);
		ImageIcon icon3 = new ImageIcon("image/phonenumber.png");
	    Image phoneImage = icon3.getImage();
	    Image phoneChangeImg = phoneImage.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
	    ImageIcon phoneChangeIcon = new ImageIcon(phoneChangeImg);
	    phoneBtn.setIcon(phoneChangeIcon);
		JPanel phoneLabelPanel = new JPanel();
		phoneLabelPanel.setPreferredSize(new Dimension(330, 50));
		phoneLabelPanel.setBackground(Color.white);
		JLabel phoneLabel = new JLabel("전화번호들어갑니다");
		phoneLabel.setFont(new Font("나눔바른펜", Font.BOLD, 20));
		phoneLabel.setForeground(Color.black);
		JPanel phoneIcon = new JPanel();
		phoneIcon.setPreferredSize(new Dimension(150, 50));
		phoneIcon.setBackground(new Color(74, 210, 149));
		
		phoneLabelPanel.add(phoneLabel);
		phonePanel.add(phoneIcon);
		phonePanel.add(phoneLabelPanel);
		
		
		
		
		
		JPanel birthPanel = new JPanel();
		birthPanel.setPreferredSize(new Dimension(500, 50));
		birthPanel.setBackground(Color.white);
		birthPanel.setBorder(null);
		JButton birthBtn = new JButton();
		birthBtn.setBounds(0, 235, 150, 50);
		birthBtn.setBorder(null);
		ImageIcon icon4 = new ImageIcon("image/birth.png");
	    Image birthImage = icon4.getImage();
	    Image birthChangeImg = birthImage.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
	    ImageIcon birthChangeIcon = new ImageIcon(birthChangeImg);
	    birthBtn.setIcon(birthChangeIcon);
		JPanel birthLabelPanel = new JPanel();
		birthLabelPanel.setPreferredSize(new Dimension(330, 50));
		birthLabelPanel.setBackground(Color.white);
		JLabel birthLabel = new JLabel("생년월일들어갑니다");
		birthLabel.setFont(new Font("나눔바른펜", Font.BOLD, 20));
		birthLabel.setForeground(Color.black);
		JPanel birthIcon = new JPanel();
		birthIcon.setPreferredSize(new Dimension(150, 50));
		birthIcon.setBackground(new Color(74, 210, 149));
		
		birthLabelPanel.add(birthLabel);
		birthPanel.add(birthIcon);
		birthPanel.add(birthLabelPanel);
		
		
		

		
		JPanel githubPanel = new JPanel();
		githubPanel.setPreferredSize(new Dimension(500, 50));
		githubPanel.setBackground(Color.white);
		birthPanel.setBorder(null);
		JButton githubBtn = new JButton();
		githubBtn.setBounds(0, 290, 150, 50);
		githubBtn.setBorder(null);
		ImageIcon icon5 = new ImageIcon("image/github.png");
	    Image githubImage = icon5.getImage();
	    Image githubChangeImg = githubImage.getScaledInstance(150, 50, Image.SCALE_SMOOTH);
	    ImageIcon githubChangeIcon = new ImageIcon(githubChangeImg);
	    githubBtn.setIcon(githubChangeIcon);
		JPanel githubLabelPanel = new JPanel();
		githubLabelPanel.setPreferredSize(new Dimension(330, 50));
		githubLabelPanel.setBackground(Color.white);
		JLabel githubLabel = new JLabel("github들어갑니다");
		githubLabel.setFont(new Font("나눔바른펜", Font.BOLD, 20));
		githubLabel.setForeground(Color.black);
		JPanel githubIcon = new JPanel();
		githubIcon.setPreferredSize(new Dimension(150, 50));
		githubIcon.setBackground(new Color(74, 210, 149));
		
		githubLabelPanel.add(githubLabel);
		githubPanel.add(githubIcon);
		githubPanel.add(githubLabelPanel);

		
		
		
		
		//정보 받아오기
		String info[] = Client.getFriendInfo(ID);
		// [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
		
		for(String q : info) {
			System.out.println(q);
		}
		
		
		String line1 = info[0] + "(" + info[1] + ")";
		nameLabel.setText(line1);
		messageLabel.setText(info[2]);
		emailLabel.setText(info[3]);
		phoneLabel.setText(info[4]);
		birthLabel.setText(info[5]);
		githubLabel.setText(info[6]);

		
		//친구라면 정보 좀 더 보여줌
		if(type == 0) {
			phoneLabel.setText("비공개");
			birthLabel.setText("비공개");
			githubLabel.setText("비공개");
			
			/*add(phonePanel);
			add(birthPanel);
			add(githubPanel);*/
		}
		

		infoPanel.setBackground(new Color(153, 255, 153));
		infoPanel.add(namePanel);
		infoPanel.add(messagePanel);
		infoPanel.add(emailPanel);
		infoPanel.add(phonePanel);
		infoPanel.add(birthPanel);
		infoPanel.add(githubPanel);
		
		frame.getContentPane().add(messageBtn);
		frame.getContentPane().add(emailBtn);
		frame.getContentPane().add(phoneBtn);
		frame.getContentPane().add(birthBtn);
		frame.getContentPane().add(githubBtn);
		frame.getContentPane().add(infoPanel);

		
		frame.setVisible(true);
		frame.setSize(450, 374);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
}