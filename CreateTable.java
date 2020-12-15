package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class CreateTable {
     	//�����ͺ��̽� ���� ���� ����
		private static Connection conn;
		private static PreparedStatement pstmt;
		private static ResultSet rs;
		private ResultSet rs2;
		private static DataSource ds;
		//�����ͺ��̽� ����

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost/network";
		String id = "root";		
		String password = "12345";

		Connection con = null; 
		  try {

			  Class.forName("com.mysql.jdbc.Driver");
			  con=DriverManager.getConnection(url,id,password);  
			  Statement stmt = con.createStatement();
			  stmt.executeUpdate("create table USER ("
	                     +"id varchar(20) primary key,"
         		         +"password varchar(20),"
         		         +"name varchar(20), "
         		         +"nickname varchar(20) unique, "
         		         +"phone varchar(20), "         		         
         		         +"email varchar(20), "
         		         +"birth varchar(20), "
         		         +"github varchar(20), "
         		         +"state_message varchar(20), "
         		         +"last_connection varchar(20) "
         		         + ");");
			  System.out.println("\"USER\" Table�� �����Ǿ����ϴ�.");

			  
			  stmt.executeUpdate("create table FRIEND ("
	                     +"my_id varchar(20),"
      		             +"friend_id varchar(20) "
      		             + ");");
			  System.out.println("\"FRIEND\" Table�� �����Ǿ����ϴ�.");


			  stmt.executeUpdate("create table FRIEND_PLUS ("
	                     +"send_id varchar(20),"
   		                 +"receive_id varchar(20)"
   		                 + ");");
			  System.out.println("\"FRIEND_PLUS\" Table�� �����Ǿ����ϴ�.");

			  
			  stmt.executeUpdate("create table CHAT ("
	                     +"chat_id varchar(20), "
	                     +"maker_id varchar(20) "
   		                 + ");");
			  System.out.println("\"CHAT\" Table�� �����Ǿ����ϴ�.");

			  
			  stmt.executeUpdate("create table CHATTING ("
	                     +"chat_id varchar(20),"
		                 +"time varchar(20), "
		                 +"sender varchar(20), "
		                 +"content varchar(20) "
		                 + ");");
			  System.out.println("\"CHATTING\" Table�� �����Ǿ����ϴ�.");
			  
			  con.close();
			  
		  }
		  catch(Exception e) {
			  System.out.println(e.getMessage());
			  e.printStackTrace();	
		  }
	}
}
