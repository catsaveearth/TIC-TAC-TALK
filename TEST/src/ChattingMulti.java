import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChattingMulti {
    static int port;
	String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JPanel panel = new JPanel();
    JTextField textField = new JTextField(25);
    JTextArea messageArea = new JTextArea(16, 50);
    JButton button = new JButton("SEND");
    JPanel leftLine = new JPanel();
    JPanel rightLine = new JPanel();
    JPanel top = new JPanel();
    JLabel label = new JLabel("CHATTING ROOM");

    public ChattingMulti() {
        label.setFont(new Font("고딕", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        
    	ImageIcon icon = new ImageIcon("image/add.png");
    	Image addImage = icon.getImage();
	    Image addChangingImg = addImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH);
	    ImageIcon addChangeIcon = new ImageIcon(addChangingImg);
	     
	    ImageIcon icon3 = new ImageIcon("image/user.png");
	    Image userImage = icon3.getImage();
	    Image userChangeImg = userImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH);
	    ImageIcon userChangeIcon = new ImageIcon(userChangeImg);

	    JButton add = new JButton();
	    add.setBounds(10, 10, 30, 30);
	    add.setIcon(addChangeIcon);

	    add.addActionListener(new ActionListener() {
	       @Override
	       public void actionPerformed(ActionEvent e) {
	      	 System.out.println("add");
	      	 InviteFriend invite = new InviteFriend();
	          // 만약 비밀번호가 맞으면 Setting setting = new Setting();
	          // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
	       }
	    });
	      
	    JButton user = new JButton();
	    user.setBounds(354, 10, 30, 30);
	    user.setIcon(userChangeIcon);
	      
	    user.addActionListener(new ActionListener() {
	       @Override
	       public void actionPerformed(ActionEvent e) {
	    	   ChattingOnlinePeople people = new ChattingOnlinePeople();
	       }
	    });
	      

        frame.getContentPane().add(add);
        frame.getContentPane().add(user);
        top.add(label);
        leftLine.setBackground(new Color(0, 78, 150));
        rightLine.setBackground(new Color(0, 78, 150));
        textField.setEditable(true);
        
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
        
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //out.println(textField.getText());
        		String getTxt = textField.getText();
        		messageArea.append(getTxt + "\n");
        		textField.setText("");
                //textField.setHorizontalAlignment(RIGHT);
            }
        });
        
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String getTxt = textField.getText();        		
        		messageArea.append(getTxt + "\n");
        		textField.setText("");
	        }
        });


        frame.setVisible(true);
        frame.setSize(400, 600);
        frame.setResizable(false);
    }
	

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a your nickname",
            "Nickname selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    public static void main(String[] args) throws Exception {
    	ChattingMulti chatting = new ChattingMulti();
    }
}