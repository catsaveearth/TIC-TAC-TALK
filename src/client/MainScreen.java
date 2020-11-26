package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import client.Client;

public class MainScreen extends JFrame{
	JTable jTable;
	DefaultTableModel model;
	int flag = 0;
	private JMenuItem menuItemAdd;
	int searchingFlag = 0;

	public MainScreen() {
      JFrame frame = new JFrame();
      JPanel panel = new JPanel();
      
      // 버튼 있는 부분
      JPanel top = new JPanel();
      top.setPreferredSize(new Dimension(430, 40));
      top.setBorder(new TitledBorder(new LineBorder(Color.black,2)));
      // 버튼에 이미지 넣고 버튼 크기에 맞게 이미지 넣기
      ImageIcon icon = new ImageIcon("image\\searching.png");
      Image searchImage = icon.getImage();
      Image searchChangingImg = searchImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon searchChangeIcon = new ImageIcon(searchChangingImg);
      ImageIcon icon2 = new ImageIcon("image\\chatting.png");
      Image chatImage = icon2.getImage();
      Image chatChangingImage = chatImage.getScaledInstance(26, 26, Image.SCALE_SMOOTH);
      ImageIcon chatChangingIcon = new ImageIcon(chatChangingImage);
      ImageIcon icon3 = new ImageIcon("image\\setting.png");
      Image settingImage = icon3.getImage();
      Image settingChangeImg = settingImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon settingChangeIcon = new ImageIcon(settingChangeImg);

      JButton searching = new JButton();
      searching.setPreferredSize(new Dimension(30, 30));
      searching.setBounds(10, 10, 30, 30);
      searching.setIcon(searchChangeIcon);
      System.out.println(searching);
      top.add(searching);

      
      //searching 뻐튼 -> 끝! 건들지 마세요
      searching.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
               if (event.getButton() == MouseEvent.BUTTON1) {
                   JPopupMenu pm = new JPopupMenu();
                   JMenuItem pm_item1 = new JMenuItem("내 친구 찾기");
                   JMenuItem pm_item2 = new JMenuItem("새 친구 찾기");
                   
                   pm.add(pm_item1);
                   pm.add(pm_item2);
                   pm.show(event.getComponent(), event.getX(), event.getY());


                   pm_item1.addActionListener(new ActionListener() {
                	   @Override
                	   public void actionPerformed(ActionEvent e) {
                		   //내 친구 찾기
                		   FindMyFriend finder = new FindMyFriend();
                	   }
                	   
                   });
                   
                   pm_item2.addActionListener(new ActionListener() {
                	   @Override
                	   public void actionPerformed(ActionEvent e) {
                		   //새 친구 찾기
                		   FindAllFriend finder = new FindAllFriend();
                	   }
                   });                   
                }
           }
      });
      
      JButton chatting = new JButton();
      chatting.setBounds(350, 10, 30, 30);
      chatting.setIcon(chatChangingIcon);
      top.add(chatting);
      
      //멀티룸 초대 버튼 액션
      chatting.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            InviteFriend invite = new InviteFriend();
         }
      });
      
      JButton setting = new JButton();
      setting.setPreferredSize(new Dimension(30, 30));
      setting.setBounds(390, 10, 30, 30);
      setting.setIcon(settingChangeIcon);
      
      //내정보 수정 버튼 액션 -> 끝! 건들지 마세요
      setting.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            PWCheck check = new PWCheck();
         }
      });
      
      
      //기본 정보 가져오기!=========================================
      String[] binfo = Client.basicinfo();
      
      // 내 정보 부분
      JPanel myInfo = new JPanel();
      GridLayout layout = new GridLayout(2, 2);
      JPanel blank = new JPanel();
      JPanel blank2 = new JPanel();
      myInfo.setLayout(layout);
      JLabel myInfo2 = new JLabel("   내 정보");
      myInfo2.setFont(new Font("나눔바른펜", Font.PLAIN, 11));
      myInfo2.setPreferredSize(new Dimension(430, 13));

      ImageIcon addIcon = new ImageIcon("image\\add.png");
      Image addImage = addIcon.getImage();
	  Image addChangingImg = addImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH);
	  ImageIcon addChangeIcon = new ImageIcon(addChangingImg);

      
      myInfo.setPreferredSize(new Dimension(430, 60));
      myInfo.setBorder(new TitledBorder(new LineBorder(Color.red,2)));
      
	  JLabel myName = new JLabel("   " + binfo[0] + "(" + binfo[1] + ")");
      myName.setFont(new Font("나눔바른펜", Font.PLAIN, 13));
	  JLabel message = new JLabel(binfo[2]);
      message.setFont(new Font("나눔바른펜", Font.PLAIN, 13));
      JLabel text1 = new JLabel("친구목록");
      

      // 친구목록 부분
      JPanel friend = new JPanel();
      JLabel friend2 = new JLabel("   친구목록");
      friend2.setFont(new Font("나눔바른펜", Font.PLAIN, 11));
      friend2.setPreferredSize(new Dimension(430, 13));
      friend.setPreferredSize(new Dimension(430, 380));
      friend.setBorder(new TitledBorder(new LineBorder(Color.green,2)));
      
      ImageIcon onlineImgIcon = new ImageIcon("image\\online.png");
      Image online = onlineImgIcon.getImage();
      Image onlineImg = online.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
      ImageIcon onlineIcon = new ImageIcon(onlineImg);
      
      ImageIcon offlineImgIcon = new ImageIcon("image\\offline.png");
      Image offline = offlineImgIcon.getImage();
      Image offlineImg = offline.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
      ImageIcon offlineIcon = new ImageIcon(offlineImg);

      String statusStr = ""; // online인지 offline인지 정하는 부분
      Icon status = new ImageIcon();
      
      if (statusStr == "online") {
         status = new ImageIcon("image\\online.png");
      } else if (statusStr == "offline") {
         status = new ImageIcon("image\\offline.png");
      }
      
      
      
      //친구목록 불러오기!=========================================
      String[][] friendlist = Client.friendList();
		// String[][name, nickname, last_connection, 상메]


      String columnNames[] = { "닉네임(이름)", "한줄메시지", "status" };
      Object rowData[][] = { }; // 친구목록 들어가야 될 자리!

      model = new DefaultTableModel(rowData, columnNames) {
         public boolean isCellEditable(int i, int c) {
            return false;
         }
         
         public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
         }
      };

      if(friendlist != null) {
    	  int idx = 0;
    	  int ck = Integer.parseInt(friendlist[0][0]);

          for(String[] fl : friendlist) {
        	  if(idx == ck) break;
        	  if(idx == 0) {idx++; continue;}
    		  idx++;
    		  
        	  String line = fl[0] + "(" + fl[1] + ")";
        	  
        	  if(fl[2].compareTo("0") == 0) {
            	  Object inData[] = {line, fl[3], onlineIcon};
        		  model.addRow(inData);	
        	  }
        	  else {
            	  Object inData[] = {line, fl[3], offlineIcon};

        		  model.addRow(inData);
        	  }
        	  
          } 
      }
      //나중에 친구목록 수정도 model을 건드리는 등의 행동으로 하자...
      
      
      //왜 이거 선택이 안먹지?? 나중에물어보기
      jTable = new JTable(model);
      jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택
      //jTable.addMouseListener(this);
      jTable.getColumn("닉네임(이름)").setPreferredWidth(100);
      jTable.getColumn("한줄메시지").setPreferredWidth(250);
      jTable.getColumn("status").setPreferredWidth(50);
      JScrollPane jScollPane = new JScrollPane(jTable);
      jScollPane.setPreferredSize(new Dimension(425, 350));
      jTable.getTableHeader().setReorderingAllowed(false);
      jTable.getTableHeader().setResizingAllowed(false);
   
      
      
      jTable.setShowGrid(false);
      jTable.setRowHeight(30);
      
      // 공공데이터 부분
      JPanel publicData = new JPanel();
      publicData.setPreferredSize(new Dimension(430, 60));
      publicData.setBorder(new TitledBorder(new LineBorder(Color.yellow,2)));
      
      myInfo.add(myInfo2);
      myInfo.add(blank);
      myInfo.add(myName);
      myInfo.add(message);
      
      friend.add(friend2);
      friend.add(jScollPane, "Left"); //JScrooPane에 담은 JList를 나타내기 위해 배치한다.

      panel.add(top);
      panel.add(myInfo);
      panel.add(friend);
      panel.add(publicData);
      frame.getContentPane().add(searching);
      frame.getContentPane().add(chatting);
      frame.getContentPane().add(setting);
      frame.getContentPane().add(panel);
      
      frame.setVisible(true);
      frame.setSize(450, 600);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //모든 구축이 끝나면 socket풀어줌
      Client.freeSocket();
      
      
      String[] buttons = {"수락", "거절"};
      
//      while(true) {
//    	  
//    	  //친구요청이 있다는 신호
//    	  if(Client.fslInfoPlusREQTOCLIENT == true) {
//    		  String[][] FirendRequset = Client.getFriendPlusInfo();
//    		  
//    		  /*
//    	      int result = JOptionPane.showOptionDialog(null, "입력하고싶은 메시지", "알림창 이름", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, 배열넣는부분!!!!!!, "두번째값");
//    	      if (result == 0) {
//    	         //첫번째버튼 눌렀을 때
//    	      } else if (result == 1) {
//    	         //두번째버튼 눌렀을 때
//    	      }
//    		  */
//    		  
//    	  }
//    	  
//      }
   }

}