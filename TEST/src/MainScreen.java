import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class MainScreen extends JFrame implements MouseListener, ActionListener {
   JTable jTable;
   DefaultTableModel model;
   int flag = 0;
   private JMenuItem menuItemAdd;
   int searchingFlag = 0;
   
   private void removeCurrentRow() {
        int selectedRow = jTable.getSelectedRow();
        model.removeRow(selectedRow);
    }

   public void actionPerformed(ActionEvent event) {
        JMenuItem menu = (JMenuItem) event.getSource();
        System.out.println(menu);
        if (menu.getText().equals("정보")) {
           FriendInfo friendInfo = new FriendInfo();
        } else if (menu.getText().equals("1:1 채팅")) {
           // 온라인일때는 연결되고 온라인아닐때는 1:1채팅 신청 추가하기!!
           ChattingOne chatting = new ChattingOne();
        } else if (menu.getText().equals("친구삭제")) {
           removeCurrentRow();
        }
    }
   
   public void mouseClicked(MouseEvent me) {
      System.out.println(me);
        if (me.getButton() == MouseEvent.BUTTON1) {
           flag++;
           System.out.println(flag);
        }
        if (me.getButton() == MouseEvent.BUTTON3) {
           flag += 2;
           System.out.println(flag);
           if (flag == 3) {
               JPopupMenu pm = new JPopupMenu();
                JMenuItem pm_item1 = new JMenuItem("정보");
                pm_item1.addActionListener(this);
                System.out.println(this);
                JMenuItem pm_item2 = new JMenuItem("1:1 채팅");
                pm_item2.addActionListener(this);
                JMenuItem pm_item3 = new JMenuItem("친구삭제");
                pm_item3.addActionListener(this);
                pm.add(pm_item1);
                pm.add(pm_item2);
                pm.add(pm_item3);
                pm.show(me.getComponent(), me.getX(), me.getY());
           }
           flag = 0;
        }
   }
   
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mousePressed(MouseEvent e) {}
   public void mouseReleased(MouseEvent e) {}
      
   public MainScreen() {
      JFrame frame = new JFrame();
      JPanel panel = new JPanel();
      
      // 버튼 있는 부분
      JPanel top = new JPanel();
      top.setPreferredSize(new Dimension(430, 40));
      top.setBorder(new TitledBorder(new LineBorder(Color.black,2)));
      // 버튼에 이미지 넣고 버튼 크기에 맞게 이미지 넣기
      ImageIcon icon = new ImageIcon("image/searching.png");
      Image searchImage = icon.getImage();
      Image searchChangingImg = searchImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon searchChangeIcon = new ImageIcon(searchChangingImg);
      ImageIcon icon2 = new ImageIcon("C:\\Users\\jimin\\OneDrive\\바탕 화면\\수업\\2학년 2학기\\컴퓨터네트워크 및 실습\\팀플\\아이콘\\chatting.png");
      Image chatImage = icon2.getImage();
      Image chatChangingImage = chatImage.getScaledInstance(26, 26, Image.SCALE_SMOOTH);
      ImageIcon chatChangingIcon = new ImageIcon(chatChangingImage);
      ImageIcon icon3 = new ImageIcon("C:\\Users\\jimin\\OneDrive\\바탕 화면\\수업\\2학년 2학기\\컴퓨터네트워크 및 실습\\팀플\\아이콘\\setting.png");
      Image settingImage = icon3.getImage();
      Image settingChangeImg = settingImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon settingChangeIcon = new ImageIcon(settingChangeImg);

      JButton searching = new JButton();
      searching.setPreferredSize(new Dimension(30, 30));
      searching.setBounds(10, 10, 30, 30);
      searching.setIcon(searchChangeIcon);
      System.out.println(searching);
      top.add(searching);

      searching.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent me) {
              System.out.println(me);
           }
       });
      
      searching.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent event) {
               if (event.getButton() == MouseEvent.BUTTON1) {
                   JPopupMenu pm = new JPopupMenu();
                   JMenuItem pm_item1 = new JMenuItem("내 친구 찾기");
                   JMenuItem pm_item2 = new JMenuItem("새 친구 찾기");
                   pm.add(pm_item1);
                   pm_item1.addMouseListener(this);
                   pm.add(pm_item2);
                   pm_item2.addMouseListener(this);
                   pm.show(event.getComponent(), event.getX(), event.getY());
                }
           }

          public void mouseEntered(MouseEvent e) {}
          public void mouseExited(MouseEvent e) {}
          public void mousePressed(MouseEvent e) {}
          public void mouseReleased(MouseEvent e) {}
      		
    	  public void mousePerformed(MouseEvent event) { // 왜 안될까!!
    	        JMenuItem menu = (JMenuItem) event.getSource();
    	        System.out.println("menu = " + menu);
    	        if (menu.getText().equals("내 친구 찾기")) {
                    FindMyFriend friend = new FindMyFriend();
                 } else if (menu.getText().equals("새 친구 찾기")) {
                    // 온라인일때는 연결되고 온라인아닐때는 1:1채팅 신청 추가하기!!
              	   FindAllFriend friend = new FindAllFriend();
                 }
    	    }
      });
      
      JButton chatting = new JButton();
      chatting.setBounds(350, 10, 30, 30);
      chatting.setIcon(chatChangingIcon);
      top.add(chatting);
      
      chatting.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            InviteFriend invite = new InviteFriend();
            
            
            
            
            ChattingMulti multi = new ChattingMulti();
         }
      });
      
      JButton setting = new JButton();
      setting.setPreferredSize(new Dimension(30, 30));
      setting.setBounds(390, 10, 30, 30);
      setting.setIcon(settingChangeIcon);
      
      setting.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            PWCheck check = new PWCheck();
            // 만약 비밀번호가 맞으면 Setting setting = new Setting();
            // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
         }
      });
      
      // 내 정보 부분
      JPanel myInfo = new JPanel();
      GridLayout layout = new GridLayout(2, 2);
      JPanel blank = new JPanel();
      JPanel blank2 = new JPanel();
      myInfo.setLayout(layout);
      JLabel myInfo2 = new JLabel("   내 정보");
      myInfo2.setFont(new Font("나눔바른펜", Font.PLAIN, 11));
      myInfo2.setPreferredSize(new Dimension(430, 13));
      JButton add = new JButton();
      add.setBounds(370, 60, 45, 40);
      ImageIcon addIcon = new ImageIcon("image/add.png");
      Image addImage = addIcon.getImage();
	  Image addChangingImg = addImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH);
	  ImageIcon addChangeIcon = new ImageIcon(addChangingImg);
	  add.setIcon(addChangeIcon);

	  add.addActionListener(new ActionListener() {
	     @Override
	     public void actionPerformed(ActionEvent e) {
           FriendRequest request = new FriendRequest();
	        // 만약 비밀번호가 맞으면 Setting setting = new Setting();
	        // 틀리면 JOptionPane.showMessageDialog(null,  "Wrong!!");
	     }
	  });
      
      myInfo.setPreferredSize(new Dimension(430, 60));
      myInfo.setBorder(new TitledBorder(new LineBorder(Color.red,2)));
      
      JLabel myName = new JLabel("   닉네임(이름)");
      myName.setFont(new Font("나눔바른펜", Font.PLAIN, 13));
      JLabel message = new JLabel("한줄메시지");
      message.setFont(new Font("나눔바른펜", Font.PLAIN, 13));
      JLabel text1 = new JLabel("친구목록");
      
      // 친구목록 부분
      JPanel friend = new JPanel();
      JLabel friend2 = new JLabel("   친구목록");
      friend2.setFont(new Font("나눔바른펜", Font.PLAIN, 11));
      friend2.setPreferredSize(new Dimension(430, 13));
      friend.setPreferredSize(new Dimension(430, 380));
      friend.setBorder(new TitledBorder(new LineBorder(Color.green,2)));
      
      ImageIcon onlineImgIcon = new ImageIcon("image/online.png");
      Image online = onlineImgIcon.getImage();
      Image onlineImg = online.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
      ImageIcon onlineIcon = new ImageIcon(onlineImg);
      
      ImageIcon offlineImgIcon = new ImageIcon("image/offline.png");
      Image offline = offlineImgIcon.getImage();
      Image offlineImg = offline.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
      ImageIcon offlineIcon = new ImageIcon(offlineImg);

      String statusStr = ""; // online인지 offline인지 정하는 부분
      Icon status = new ImageIcon();
      
      if (statusStr == "online") {
         status = new ImageIcon("image/online.png");
      } else if (statusStr == "offline") {
         status = new ImageIcon("image/offline.png");
      }
      
      String columnNames[] = { "닉네임(이름)", "한줄메시지", "status" };
      Object rowData[][] = // 친구목록 들어가야 될 자리!
         {
         { "닉네임1(이름)", "한줄메시지", onlineIcon},
         { "닉네임2(이름)", "한줄메시지", offlineIcon},
         { "닉네임3(이름)", "한줄메시지", status},
         };
      
      model = new DefaultTableModel(rowData, columnNames) {
         public boolean isCellEditable(int i, int c) {
            return false;
         }
         
         public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
         }
      };

      jTable = new JTable(model);
      jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택
      jTable.addMouseListener(this);
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
      frame.getContentPane().add(add);
      frame.getContentPane().add(panel);
      
      frame.setVisible(true);
      frame.setSize(450, 600);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.addWindowListener(new WindowListener() { /////////////////////////////////////////////////
          public void windowOpened(WindowEvent e) {}
          public void windowIconified(WindowEvent e) {}
          public void windowDeiconified(WindowEvent e) {}
          public void windowDeactivated(WindowEvent e) {}
          public void windowClosing(WindowEvent e) {
        	  System.out.println(e);
          	String[] buttons = {"YES", "NO"};
    		int result = JOptionPane.showOptionDialog(null, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, "두번째값");
    		if (result == 0) {
    			System.exit(0);
    		} else if (result == 1) {
    		      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    		}
          }
          public void windowClosed(WindowEvent e) {
        	  System.out.println("A");
          }
          public void windowActivated(WindowEvent e) {}
      }); ////////////////////////////////////////////////////////////////////////////////////////////
   }
   
   public static void main(String[] args) {
      MainScreen main = new MainScreen();
   }

}