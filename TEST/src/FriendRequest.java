import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FriendRequest extends JFrame implements MouseListener {
	JTable jTable;
	DefaultTableModel model;
	
	private void removeCurrentRow() {
        int selectedRow = jTable.getSelectedRow();
        model.removeRow(selectedRow);
    }
	
	public void mouseClicked(MouseEvent me) {
		String[] buttons = {"YES", "NO"};
		int result = JOptionPane.showOptionDialog(null, "수락하시겠습니까?", "친구신청", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, "두번째값");
		if (result == 0) {
			// 표에 친구 추가
			removeCurrentRow();
			dispose();
		} else if (result == 1) {
			removeCurrentRow();
			dispose();
		}
	}
	
	public FriendRequest() {
		JFrame frame = new JFrame();
		JPanel friend = new JPanel();
		
	    JLabel friend2 = new JLabel("친구신청");
	    friend2.setFont(new Font("나눔바른펜", Font.PLAIN, 15));
	    //friend2.setPreferredSize(new Dimension(430, 13));
	    friend.setPreferredSize(new Dimension(430, 380));
	    
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
	    
	    String columnNames[] = { "닉네임(이름)", "status" };
	    Object rowData[][] = // 친구목록 들어가야 될 자리!
	       {
	       { "닉네임1(이름)", onlineIcon},
	       { "닉네임2(이름)", offlineIcon},
	       { "닉네임3(이름)", status},
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
	    jTable.getColumn("status").setPreferredWidth(50);
	    JScrollPane jScollPane = new JScrollPane(jTable);
	    jScollPane.setPreferredSize(new Dimension(180, 227));
	    
	    jTable.setShowGrid(false);
	    jTable.setRowHeight(30);
	    
	    friend.add(friend2);
	    friend.add(jScollPane, "Left"); //JScrooPane에 담은 JList를 나타내기 위해 배치한다.
	    frame.add(friend);
	    
	    frame.setVisible(true);
        frame.setSize(200, 300);
        //frame.setResizable(false);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	public static void main(String[] args) {
		FriendRequest request = new FriendRequest();
	}
}


