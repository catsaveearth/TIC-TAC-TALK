package client;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class InviteFriendInOriginRoom extends JFrame implements MouseListener {
	JTable jTable;
	DefaultTableModel model;
	HashSet<Integer> selectnum = new HashSet<Integer>();
    JLabel flist = new JLabel("친구를 선택하세요");
    JPanel listPanel = new JPanel();

	public void mouseClicked(MouseEvent me) {
		int row = jTable.getSelectedRow();

		if(selectnum.contains(row)) {//선택해제
			selectnum.remove(row);
		}
		else { //선택
			selectnum.add(row);
		}
		
		if(selectnum.isEmpty()) {
			flist.setText("친구를 선택하세요");	
		}
		else {
			flist.setText(selectnum.toString());
		}
	}
	
	public InviteFriendInOriginRoom(DefaultTableModel m, int rn) {
		model = m;
		JFrame frame = new JFrame();
		JPanel friend = new JPanel();
		friend.setPreferredSize(new Dimension(250, 100));
	    JButton makeroom = new JButton();
	    makeroom.setBackground(new Color(74, 210, 149));
	    makeroom.setPreferredSize(new Dimension(180, 25));
		makeroom.setFont(new Font("나눔바른펜", Font.BOLD, 10));
		makeroom.setText("초대하기");
		flist.setFont(new Font("나눔바른펜", Font.PLAIN, 18));
		flist.setForeground(Color.white);

		//친구 초대하기
		makeroom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!selectnum.isEmpty()) {
					//ID를 굴비엮기
					String IDs = "";
					int first = 0;
					for(int i : selectnum) {
						if (first++ == 0) IDs = (String) model.getValueAt(i, 0);
						else IDs = IDs + "^" + model.getValueAt(i, 0);
					}
					frame.dispose();
				   //초대 요청
					Client.InviteFriend(rn, IDs);
				}
			}
		});
		
		JPanel title = new JPanel();
		title.setPreferredSize(new Dimension(250, 30));
		title.setBackground(new Color(74, 210, 149));
	    JLabel friend2 = new JLabel("친구초대");
	    friend2.setFont(new Font("나눔바른펜", Font.PLAIN, 15));
	    friend.setPreferredSize(new Dimension(430, 380));
	    
	    jTable = new JTable(model);
	    jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택
	    jTable.addMouseListener(this);
	    jTable.getColumn("닉네임(이름)").setPreferredWidth(100);
	    jTable.getColumn("status").setPreferredWidth(50);
	    JScrollPane jScollPane = new JScrollPane(jTable);
	    jScollPane.setPreferredSize(new Dimension(180, 227));
	
	    
	    jTable.getColumn("한줄메시지").setWidth(0);
	    jTable.getColumn("한줄메시지").setMinWidth(0);
	    jTable.getColumn("한줄메시지").setMaxWidth(0);
	    
	    jTable.getTableHeader().setReorderingAllowed(false);
	    jTable.getTableHeader().setResizingAllowed(false);
	   
	    jTable.setRowSelectionAllowed(true);
	    jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    
	    jTable.setShowGrid(false);
	    jTable.setRowHeight(30);
	    
	    title.add(friend2);
	    jScollPane.setPreferredSize(new Dimension(230, 230));
	    JPanel table = new JPanel();
	    table.setPreferredSize(new Dimension(250, 280));
	    table.setBackground(new Color(0, 54, 78));
	    
	    table.add(jScollPane, "Left");
	    table.add(flist);  
	    frame.add(title, BorderLayout.NORTH);
	    frame.add(table);
	    frame.add(makeroom, BorderLayout.SOUTH);
	    
	    frame.setVisible(true);
        frame.setSize(250, 350);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}