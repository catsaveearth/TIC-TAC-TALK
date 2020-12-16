package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * 공사 끝
 * */


@SuppressWarnings("serial")
public class PWCheck extends JFrame {
	
	public PWCheck() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(5, 2));
		
		JPanel blank = new JPanel();
		blank.setBackground(new Color(74, 210, 149));
		blank.setBorder(null);
		JPanel blank1 = new JPanel();
		blank1.setBackground(new Color(74, 210, 149));
		blank1.setBorder(null);
		
		
		ImageIcon icon = new ImageIcon("image/passwardcheck.png");
	    Image titleImage = icon.getImage();
	    Image titleChangeImg = titleImage.getScaledInstance(300, 50, Image.SCALE_SMOOTH);
	    ImageIcon titleChangeIcon = new ImageIcon(titleChangeImg);
	    JButton label = new JButton();
	    label.setPreferredSize(new Dimension(100, 30));
	    label.setBounds(5, 5, 15, 15);
	    label.setIcon(titleChangeIcon);
	    label.setBorder(null);
	    label.addActionListener(new ActionListener() {
	     @Override
	     	public void actionPerformed(ActionEvent e) {
	        }
	    });
		
	    
	    JPanel pwPanel = new JPanel();
	    pwPanel.setBackground(new Color(74, 210, 149));
		JPasswordField passward = new JPasswordField(18);
	    
		JPanel btnPanel = new JPanel();
		btnPanel.setBackground(new Color(74, 210, 149));
		
		JButton checkBtn = new JButton("확인");
		checkBtn.setBounds(10, 10, 10, 10);
		checkBtn.setBackground(new Color(0, 54, 78));
		checkBtn.setForeground(Color.white);

		checkBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				if (Client.pwcheck(passward.getPassword())) {
					frame.dispose();
					Setting setting = new Setting();
				} else {
					JOptionPane.showMessageDialog(null,  "Wrong!!");
				}
			}
		});

		pwPanel.add(passward);
		btnPanel.add(checkBtn);
		frame.getContentPane().add(blank);
		frame.getContentPane().add(label);
		frame.getContentPane().add(pwPanel);
		frame.getContentPane().add(btnPanel);
		frame.getContentPane().add(blank1);
		frame.getContentPane().setBackground(new Color(74, 210, 149));
		
		frame.setTitle("Passward Check");
		frame.setVisible(true);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}
}