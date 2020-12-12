package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import client.Client;

public class MainScreen extends JFrame {
	static JTable jTable;
	static String columnNames[] = { "ID", "닉네임(이름)", "한줄메시지", "status" };
	static Object rowData[][] = {}; // 친구목록 들어가야 될 자리!
	static DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
		public boolean isCellEditable(int i, int c) {
			return false;
		}

		public Class getColumnClass(int column) {
			try {
				return getValueAt(0, column).getClass();
			}
			catch (Exception e) {
				return null;
			}
		}
	};
	
	int flag = 0;
	private JMenuItem menuItemAdd;
	int searchingFlag = 0;
	
	//외부에서 접근해서 수정이 가능해야 할 element들
	static JLabel myName;
	static JLabel message;
	static int friendnum;
	static ImageIcon onlineIcon;
	static ImageIcon offlineIcon;
	
	
	//내정보 수정!!
	public static void changeMyInfo(String name, String nn, String state_M) { // 밖에서 부르면 내 정보를 수정
		if (state_M.compareTo("null") == 0)
			state_M = null;

		// 이름이랑 상메를 바꾸면 된다
		myName.setText("   " + nn + "(" + name + ")");
		message.setText(state_M);
	}
	
	//친구 정보 수정 - ID name nn state_m
	public static void changeFriendInfo(String[] info) { //밖에서 부르면 친구 정보 수정
		int row = -1;

		//먼저 친구를 찾습니다
  	  	for(int i=0;i<friendnum;i++) {
  			Object line = model.getValueAt(i, 0);
  			if(line.toString().compareTo(info[0]) == 0) row = i;
  	  	}

  	  	if(row == -1) return;
  	  	
		if (info[3].compareTo("null") == 0) info[3]  = " ";;
  	  	
	  	model.setValueAt(info[2] + "(" + info[1] + ")", row, 1);
	  	model.setValueAt(info[3], row, 2);

	}
	
	//접속상태 수정
	public static void changeFriendstate(String ID, String state) { //밖에서 부르면 친구 접속상태 수정
		int row = -1;
		String columnNames[] = { "ID", "닉네임(이름)", "한줄메시지", "status" };

  	  	for(int i=0;i<friendnum;i++) {
  			Object line = model.getValueAt(i, 0);
  			  			
  			if(line.toString().compareTo(ID) == 0) row = i;
  	  	}

  	  	if(row == -1) return;
  	  	
  	  	if(Integer.parseInt(state) == 0)
  	  		model.setValueAt(onlineIcon, row, 3);
  	  	else
  	  		model.setValueAt(offlineIcon, row, 3);
	}
	
	//친구 추가시 리스트 업데이트
	public static void changeFriend(String[] flist) { // 친구가 추가될 시 list update
		// ID, name, nickname, last_connection, 상메
		String nn_name = flist[2] + "(" + flist[1] + ")";
		
		if (flist[4].compareTo("null") == 0) flist[4]  = " ";

		if (flist[3].compareTo("0") == 0) {
			Object inData[] = { flist[0], nn_name, flist[4], onlineIcon };
			model.addRow(inData);
		} else {
			Object inData[] = { flist[0], nn_name, flist[4], offlineIcon };
			model.addRow(inData);
		}
		friendnum++;
	}
	
	//친구신청 팝업 보여주기
	public static int showFriendPlus(String nn, String name) {
		String[] buttons = {"Yes", "No"};
		String windowName = "친구 신청";
		String showMessage = nn + "(" + name + ") 님의 친구신청을 수락하시겠습니까?";
		
		
	      int result = JOptionPane.showOptionDialog(null, showMessage, windowName, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, "두번째값");
	      if (result == 0) {
	    	  return 1; //친구수락
	      } else if (result == 1) {
	    	  return 0; //친구거절
	      }
    	  return 0; //친구거절
	}

	//일댈채팅신청 팝업 보여주기
	public static void showPCHAT(String fid, String nn, String name) {
		String windowName = "채팅 신청";
		String showMessage = nn + "(" + name + ") 님의 1대 1 채팅을 수락하시겠습니까?";
			
		int reply = JOptionPane.showConfirmDialog(null, showMessage, windowName, JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.YES_OPTION) {
			System.out.println("1 => Yes");
			Client.CHATANSWER(fid, true);
			ChattingOne newchat = new ChattingOne(fid);
			newchat.setoppenInfo(nn, name);
			newchat.setTextFree();
			Client.addPCHAT(fid, newchat);
		} else {
			Client.CHATANSWER(fid, false);
		}
	}
	
	
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

      
      //searching 버튼 -> 끝! 건들지 마세요
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
      
	  myName = new JLabel("   " + binfo[0] + "(" + binfo[1] + ")");
      myName.setFont(new Font("나눔바른펜", Font.PLAIN, 13));
      message = new JLabel(binfo[2]);
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
      onlineIcon = new ImageIcon(onlineImg);
      
      ImageIcon offlineImgIcon = new ImageIcon("image\\offline.png");
      Image offline = offlineImgIcon.getImage();
      Image offlineImg = offline.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
      offlineIcon = new ImageIcon(offlineImg);

      String statusStr = ""; // online인지 offline인지 정하는 부분
      Icon status = new ImageIcon();
      
      if (statusStr == "online") {
         status = new ImageIcon("image\\online.png");
      } else if (statusStr == "offline") {
         status = new ImageIcon("image\\offline.png");
      }

      
      
      
      
      //친구목록 불러오기!=========================================
      String[][] friendlist = Client.friendList();
      // String[][ID, name, nickname, last_connection, 상메]

      if(friendlist != null) {
    	  int idx = 0;
    	  int ck = Integer.parseInt(friendlist[0][0]);
    	  friendnum = ck - 1;

			for (String[] fl : friendlist) {
				if (idx == ck) break;
				if (idx == 0) {
					idx++;
					continue;
				}
				idx++;
				
				String line = fl[0] + "(" + fl[1] + ")";

				// 상메가 null일 경우 처리해주기
				if (fl[3].compareTo("null") == 0) fl[3] = " ";

				if (fl[2].compareTo("0") == 0) {
					Object inData[] = { fl[4], line, fl[3], onlineIcon };
					model.addRow(inData);
				} else {
					Object inData[] = { fl[4], line, fl[3], offlineIcon };
					model.addRow(inData);
				}
			}
		}

      
      
      
      
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
   }
}