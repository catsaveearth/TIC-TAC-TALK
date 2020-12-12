package client;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChattingOne{
	private String FID;
    private String NN;
    private String name;
    private String sender;
    
    JFrame frame = new JFrame(FID);
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(25);
    JTextArea messageArea = new JTextArea(16, 50);
    JButton button = new JButton("SEND");
    JPanel leftLine = new JPanel();
    JPanel rightLine = new JPanel();
    JPanel top = new JPanel();
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
    		messageArea.append("상대방이 입장하셨습니다. \n");

    		//채팅 활성화
            textField.setEditable(true);
    	}
    }
    
    public void setoppenInfo(String nn, String name) {
		NN = nn;
		this.name = name;
		sender = nn + "(" + name + ")";
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
    	
        label.setFont(new Font("고딕", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
	  
        top.add(label);
        leftLine.setBackground(new Color(0, 78, 150));
        rightLine.setBackground(new Color(0, 78, 150));
        textField.setEditable(false);
        Font font = new Font("고딕", Font.PLAIN, 14);
        messageArea.setFont(font);
        messageArea.setEditable(false); // 기존에 입력한 문자를 수정할 수 없도록 한다.
        messageArea.setBackground(new Color(143, 226, 255));
        button.setPreferredSize(new Dimension(58, 22));
        button.setFont(new Font("고딕", Font.BOLD, 13));
        button.setBackground(Color.yellow);
        button.setBorder(null);
        top.setBackground(new Color(0, 78, 150));
        top.setPreferredSize(new Dimension(400, 50));
        panel.add(textField, BorderLayout.SOUTH);
        panel.add(button, BorderLayout.EAST);
        panel.setBackground(Color.WHITE);
        panel.setBackground(new Color(0, 78, 150));
        frame.add(leftLine, BorderLayout.EAST);
        frame.add(rightLine, BorderLayout.WEST);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(top, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        
        
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
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE );

    }
 
}