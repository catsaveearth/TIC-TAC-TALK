import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class CreateTable {
      //데이터베이스 연결 변수 선언 (Declaring database connection variables)
      //데이터베이스 연결 (database connection)

   public static void main(String[] args) {
      String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
      String id = "root";      
      String password = "12345";
		Connection con = null; 
		  try {
			  Class.forName("com.mysql.jdbc.Driver");
			  con=DriverManager.getConnection(url,id,password);  
			  Statement stmt = con.createStatement();
			  stmt.executeUpdate("create table USER ("
		                 +"id varchar(20) primary key,"
	       		         +"password varchar(65),"
	       		         +"name varchar(20), "
	       		         +"nickname varchar(20) unique, "
	       		         +"phone varchar(20), "         		         
	       		         +"email varchar(20), "
	       		         +"birth varchar(20), "
	       		         +"github varchar(20), "
	       		         +"state_message varchar(20), "
	       		         +"last_connection varchar(20),"
	       		         + "SALT varchar(30)"
	       		         + ");");
			  System.out.println("\"USER\" Table이 생성되었습니다.");

			  
			  stmt.executeUpdate("create table FRIEND ("
	                     +"my_id varchar(20),"
    		             +"friend_id varchar(20) "
    		             + ");");
			  System.out.println("\"FRIEND\" Table이 생성되었습니다.");


			  stmt.executeUpdate("create table FRIEND_PLUS ("
	                     +"send_id varchar(20),"
 		                 +"receive_id varchar(20)"
 		                 + ");");
			  System.out.println("\"FRIEND_PLUS\" Table이 생성되었습니다.");

			  
			  stmt.executeUpdate("create table CHATTING ("
	                     +"chat_id varchar(20),"
		                 +"time varchar(20), "
		                 +"sender varchar(20), "
		                 +"content varchar(20) "
		                 + ");");
			  System.out.println("\"CHATTING\" Table이 생성되었습니다.");
			  
			  con.close();
		  }
		  catch(Exception e) {
			  System.out.println(e.getMessage());
			  e.printStackTrace();	
		  }
	}
}