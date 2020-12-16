package client;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ChattingOne{
	private String FID;
    private String nm;
    private String sender;
    
    JFrame frame = new JFrame("Chatter");
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(23);
    JTextArea messageArea = new JTextArea(16, 50);
    JButton button = new JButton("SEND");
    JPanel topLine = new JPanel();
    JPanel leftLine = new JPanel();
    JPanel rightLine = new JPanel();
    JPanel bottomLine = new JPanel();
    JPanel top = new JPanel();
    JPanel bottom = new JPanel();
    JPanel name = new JPanel();
    JLabel label = new JLabel("CHATTING ROOM");


    //상대방의 반응에 따른 결정
    public void checkAnswer(String YN, String nn, String name) {
    	if(YN.equals("N")) {
			JOptionPane.showMessageDialog(null, "상대방이 채팅을 거절하셨습니다.");
			Client.delPCHAT(FID);
	        frame.dispose();
    	}
    	else {
    		setoppenInfo(nn, name);
    		frame.setTitle("1:1 chat with " + sender);
    		messageArea.append("상대방이 입장하셨습니다. \n");

    		//채팅 활성화
            textField.setEditable(true);
    	}
    }
    
    public void setoppenInfo(String nn, String name) {
		this.nm = name;
		sender = nn + "(" + nm + ")";
    }
    
    public void setTextFree() {
        textField.setEditable(true);
    }

    public void endchat() {
		messageArea.append("상대방이 나가셨습니다. \n");
        textField.setEditable(false);
		button.setEnabled(false);
    }
    
	//메세지 추가
    public void receiveChat(String content) {
    	messageArea.append(sender + ": "+ content + "\n");
    }
    
    //메세지 보내기
    public void sendChat() {
		String getTxt = textField.getText();
		if(getTxt.equals("")) return;
		Client.sendPCHAT(FID, getTxt); //이게 핵심!
		
		messageArea.append("나 : " + getTxt + "\n");
		textField.setText("");
    }
     
    public void exitChat() { //채팅 종료하기
    	//종료할거냐고 한 번 더 물어보기
		int reply = JOptionPane.showConfirmDialog(null, "채팅을 종료하시겠습니까?", "채팅알림", JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.YES_OPTION) {
			Client.delPCHAT(FID);
	        frame.dispose();
		}	
    }
    
    public void FexitChat() { //채팅 종료하기
    	Client.delPCHAT(FID);
        frame.dispose();
    }
    
    public ChattingOne(String FID) {
    	this.FID = FID;
    	
    	frame.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
            	exitChat();
            }
            public void windowClosed(WindowEvent e) {
            	
            }
            public void windowActivated(WindowEvent e) {}
        }); 
    	
    	name.setPreferredSize(new Dimension(300, 30));
    	name.add(label);
    	label.setHorizontalAlignment(JLabel.CENTER);
    	label.setPreferredSize(new Dimension(220, 30));
        label.setFont(new Font("고딕", Font.BOLD, 23));
        label.setForeground(Color.black);
        
        
      
        topLine.setPreferredSize(new Dimension(600, 10));
        topLine.setBackground(new Color(255, 229, 110));
        leftLine.setBackground(new Color(255, 229, 110));
        rightLine.setBackground(new Color(255, 229, 110));
        bottomLine.setPreferredSize(new Dimension(600, 1));
        bottomLine.setBackground(new Color(255, 229, 110));
        textField.setEditable(true);
        Font font = new Font("고딕", Font.PLAIN, 14);
        messageArea.setFont(font);
        messageArea.setLineWrap(true);
        messageArea.setBorder(null);
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(0, 54, 78));
        button.setPreferredSize(new Dimension(58, 22));
        button.setFont(new Font("고딕", Font.BOLD, 13));
        button.setBackground(new Color(255, 229, 110));
        button.setBorder(null);
        top.setBackground(new Color(74, 210, 149));
        top.setPreferredSize(new Dimension(400, 50));
        top.add(label);
        top.add(topLine);
        bottom.setPreferredSize(new Dimension(400, 60));
        bottom.setBackground(new Color(74, 210, 149));
        bottom.add(textField);
        bottom.add(button);
        panel.setBackground(new Color(74, 210, 149));
        panel.setPreferredSize(new Dimension(400, 50));
        panel.add(bottomLine);
        panel.add(bottom);
        panel.setBackground(new Color(255, 229, 110));
        panel.setPreferredSize(new Dimension(350, 45));
        frame.getContentPane().add(leftLine, BorderLayout.EAST);
        frame.getContentPane().add(rightLine, BorderLayout.WEST);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        
        messageArea.setForeground(Color.white);
   
        //버튼 눌러도, 엔터를 쳐도 같은 동작!
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendChat();
            }
        });
        
        
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(textField.isEnabled())
        		sendChat();
	        }
        });


        frame.setVisible(true);
        frame.setSize(350, 550);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );

    }
}