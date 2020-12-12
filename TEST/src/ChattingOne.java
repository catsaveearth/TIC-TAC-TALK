import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChattingOne {
    static int port;
	String serverAddress;
    Scanner in;
    PrintWriter out;
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

    public ChattingOne () {
    	name.setPreferredSize(new Dimension(300, 30));
    	name.add(label);
    	label.setHorizontalAlignment(JLabel.CENTER);
    	label.setPreferredSize(new Dimension(220, 30));
        label.setFont(new Font("고딕", Font.BOLD, 23));
        label.setForeground(Color.black);
        
    	ImageIcon icon = new ImageIcon("image/add.png");
    	Image addImage = icon.getImage();
	    Image addChangingImg = addImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	    ImageIcon addChangeIcon = new ImageIcon(addChangingImg);

	    ImageIcon icon3 = new ImageIcon("image/user.png");
	    Image userImage = icon3.getImage();
	    Image userChangeImg = userImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	    ImageIcon userChangeIcon = new ImageIcon(userChangeImg);

	    JButton add = new JButton();
	    add.setBounds(10, 6, 28, 28);
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
	    user.setBounds(304, 6, 28, 28);
	    user.setIcon(userChangeIcon);
	    
	    user.addActionListener(new ActionListener() {
		       @Override
		       public void actionPerformed(ActionEvent e) {
		    	   ChattingOnlinePeople people = new ChattingOnlinePeople();
		       }
		});
	    
        frame.getContentPane().add(add);
        frame.getContentPane().add(user);
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
        
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
                //textField.setHorizontalAlignment(RIGHT);
            }
        });
        
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String getTxt = textField.getText();
        		messageArea.setForeground(Color.white);
        		messageArea.append(getTxt + "\n");
        		textField.setText("");
	        }
        });


        frame.setVisible(true);
        frame.setSize(350, 550);
        //frame.setResizable(false);
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
    	ChattingOne chatting = new ChattingOne();
    }
}





















