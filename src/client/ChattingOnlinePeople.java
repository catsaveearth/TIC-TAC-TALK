package client;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class ChattingOnlinePeople extends JFrame {
	JTable jTable;
	DefaultTableModel model;
	
	public ChattingOnlinePeople(String[] ulist) {
		JFrame frame = new JFrame();
		JPanel friend = new JPanel();
		
	    JLabel friend2 = new JLabel("참여자목록");
	    friend2.setFont(new Font("나눔바른펜", Font.PLAIN, 15));
	    //friend2.setPreferredSize(new Dimension(430, 13));
	    friend.setPreferredSize(new Dimension(200, 30));
	    friend.setBackground(new Color(74, 210, 149));

	    String columnNames[] = { "닉네임(이름)" };
	    Object rowData[][] = { };    
	    model = new DefaultTableModel(rowData, columnNames) {
	       public boolean isCellEditable(int i, int c) {
	          return false;
	       }
	    };
	
	    for(String ul : ulist) {
			Object inData[] = {ul};
			model.addRow(inData);
	    }
	    
	    jTable = new JTable(model);
	    jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택
	    jTable.getColumn("닉네임(이름)").setPreferredWidth(100);
	    JScrollPane jScollPane = new JScrollPane(jTable);
	    jScollPane.setPreferredSize(new Dimension(180, 227));
	    JPanel table = new JPanel();
	    table.setBackground(new Color(0, 54, 78));
	    table.setPreferredSize(new Dimension(200, 200));
	    
	    jTable.setShowGrid(false);
	    jTable.setRowHeight(30);
	    
	    friend.add(friend2);
	    table.add(jScollPane);
	    //friend.add(jScollPane, "Left"); //JScrooPane에 담은 JList를 나타내기 위해 배치한다.
	    //frame.add(friend2);
	    frame.add(friend, BorderLayout.NORTH);
	    frame.add(table);
	    
	    frame.setVisible(true);
        frame.setSize(200, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
	}
}