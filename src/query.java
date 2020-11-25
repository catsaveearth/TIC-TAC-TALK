
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class query {

	/* [로그인] */

	// 1. id, password를 받고 맞는건지 ckeck
	// 매개변수: String id, String password
	// 맞으면 return 1 : 틀리면 return -1(or 0)
	public static int selectLOGIN(String Qid, String Qpassword) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id, password" + " FROM user" + " WHERE id='" + Qid + "'";

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString(1);
				String password = rs.getString(2);

				System.out.println("id: " + id);
				System.out.println("password: " + password);
				System.out.println("password: " + Qpassword);

				System.out.println();
				if (Qpassword.equals(password)) {
					return 1;
				}
			}
			return -1;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static String bringSALT(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String salt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT salt" + " FROM USER" + " WHERE id = '" + Qid + "';";
			rs = stmt.executeQuery(sql);

			int i = 0;
			while (rs.next()) {
				salt = rs.getString(1);
				i++;
			}
			return salt;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return salt;
	}

	/* [회원가입] */

	// 2. id에 대한 중복체크
	// 매개분수: String id
	// 사용 가능하면 return 1 : 중복이면 return -1
	public static int selectID(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id" + " FROM user;";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString(1);
				System.out.println("id: " + id);
				System.out.println();
				if (id.equals(Qid)) {
					return -1;
				}
			}
			return 1;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	// 3. nickname에 대한 중복체크
	// 매개변수: String nickname
	// 사용 가능하면 return 1 : 틀리면 return -1
	public static int selectNICKNAME(String Qnickname) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT nickname" + " FROM user;";

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String nickname = rs.getString(1);
				System.out.println("nickname: " + nickname);
				System.out.println();
				if (nickname.equals(Qnickname)) {
					return -1;
				}
			}
			return 1;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	// 4. 새로운 user에 대한 record정보 기록 (필수정보들)
	// 매개변수 HashMap<String,String>map=(id, password, name, nickname, phone, email,
	// birth (,github), SALT)
	public static void insertUSER(HashMap<String, String> map) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "INSERT INTO USER VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, map.get("ID"));
			pstmt.setString(2, map.get("PASSWORD"));
			pstmt.setString(3, map.get("NAME"));
			pstmt.setString(4, map.get("NICKNAME"));
			pstmt.setString(5, map.get("PHONE"));
			pstmt.setString(6, map.get("EMAIL"));
			pstmt.setString(7, map.get("BIRTH"));
			if (map.containsKey("GITHUB")) {
				pstmt.setString(8, map.get("GITHUB"));
			} else {
				pstmt.setString(8, null);
			}
			pstmt.setString(9, null);
			pstmt.setString(10, null);
			pstmt.setString(11, map.get("SALT"));

			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/* [메인페이지 로딩] */

	// 5. 본인의 이름, nickname, 한줄메세지 받아오기
	// 매개변수: String id
	// return HashMap<String,String>map=(name, nickname, state_message)
	public static HashMap selectNAME_NICKNAME_STATE(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id, name, nickname, state_message" + " FROM user" + " WHERE id='" + Qid + "';";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString(1);
				String name = rs.getString(2);
				String nickname = rs.getString(3);
				String state_message = rs.getString(4);
				System.out.println("id: " + id);
				System.out.println("name: " + name);
				System.out.println("nickname: " + nickname);
				System.out.println("state_message: " + state_message);
				System.out.println();

				map.put("NAME", name);
				map.put("NICKNAME", nickname);
				map.put("STATE_MESSAGE", state_message);
			}
			return map;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	// 6. 친구목록 받아오기 => 이름, 닉네임, 접속여부, 상메
	// 매개변수: String id
	// return String[][name, nickname, last_connection, 상메]
	public static String[][] selectFRIEND(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet sr = null;

		String[][] info = new String[20][4];
		info[0][0] = "test";
		int i = 1;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			String sql = "SELECT friend_id" + " FROM FRIEND" + " WHERE my_id='" + Qid + "';";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String friend_id = rs.getString(1);
				System.out.println("friend_id: " + friend_id);
				System.out.println();

				String sql2 = "SELECT id, name, nickname, last_connection, state_message" + " FROM USER" + " WHERE id='" + friend_id
						+ "';";
				sr = stmt2.executeQuery(sql2);
				while (sr.next()) {
					String id = sr.getString(1);
					String name = sr.getString(2);
					String nickname = sr.getString(3);
					String last_connection = sr.getString(4);
					String state_message = sr.getString(5);



					info[i][0] = name;
					info[i][1] = nickname;
					info[i][2] = last_connection;
					info[i][3] = state_message;

					i++;
				}

			}
			info[0][1] = Integer.toString(i);
			return info;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		info[0][1] = Integer.toString(i);
		return info;
	}

	// 7. 친구요청 목록 받아오기 (자신에게 친구 요청을 건 client의 id를 받아옴)
	// 매개변수: String id
	// return String[send_id1,. . ., send_id20]
	public static String[] bringFRIEND_PLUS(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String[] array = new String[20];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT send_id, receive_id" + " FROM friend_plus" + " WHERE receive_id = '" + Qid + "';";
			rs = stmt.executeQuery(sql);

			int i = 0;
			while (rs.next()) {
				String send_id = rs.getString(1);
				String receive_id = rs.getString(2);

				System.out.println("send_id: " + send_id);
				System.out.println("receive_id: " + receive_id);
				System.out.println();

				array[i] = send_id;
				i++;
			}
			return array;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	/* [메인페이지 조작] */

	// 8. 내정보 받아오기 - 이메일, 전화번호 등등등....
	// 매개변수: String id
	// return HashMap<String,String>map=(phone, email, birth, github, state_message,
	// last_connection)
	public static HashMap bringINFO(String Qid) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id, password, name, nickname, phone, email, birth, github, state_message, last_connection"
					+ " FROM user" + " WHERE id='" + Qid + "';";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString(1);
				String password = rs.getString(2);
				String name = rs.getString(3);
				String nickname = rs.getString(4);
				String phone = rs.getString(5);
				String email = rs.getString(6);
				String birth = rs.getString(7);
				String github = rs.getString(8);
				String state_message = rs.getString(9);
				String last_connection = rs.getString(10);

				System.out.println("id: " + id);
				System.out.println("password: " + password);
				System.out.println("name: " + name);
				System.out.println("nickname: " + nickname);
				System.out.println("phone: " + phone);
				System.out.println("email: " + email);
				System.out.println("birth: " + birth);
				System.out.println("github: " + github);
				System.out.println("state_message: " + state_message);
				System.out.println("last_connection: " + last_connection);
				System.out.println();

				map.put("ID", id);
				map.put("PASSWORD", password);
				map.put("NAME", name);
				map.put("NICKNAME", nickname);
				map.put("PHONE", phone);
				map.put("EMAIL", email);
				map.put("BIRTH", birth);
				map.put("GITHUB", github);
				map.put("STATE_MESSAGE", state_message);
				map.put("LAST_CONNECTION", last_connection);
			}
			return map;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	// 9. 내정보 수정 - 닉네임, github, 한줄 메세지, 최근 접속 시간
	// 매개변수할떄 서버에서 8번 query 사용해서 변경하지 않은건 그대로 보내주세요
	// void 반환x

	// password 수정
	public static void updatePASSWORD(String Qid, String Qpassword, String Salt) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " password = '" + Qpassword + "'" 
					+ " SALT = '" + Salt + "'"+ " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 이름 수정
	public static void updateNAME(String Qid, String Qname) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " name = '" + Qname + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 별명 수정
	public static void updateNICKNAME(String Qid, String Qnickname) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " nickname = '" + Qnickname + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 폰번호 수정
	public static void updatePHONE(String Qid, String Qphone) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " phone = '" + Qphone + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 이메일 수정
	public static void updateEMAIL(String Qid, String Qemail) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " email = '" + Qemail + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 출생 수정
	public static void updateBIRTH(String Qid, String Qbirth) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " birth = '" + Qbirth + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 깃허브 수정
	public static void updateGITHUB(String Qid, String Qgithub) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " github = '" + Qgithub + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 한줄 메세지 수정
	public static void updateSTATE_MESSAGE(String Qid, String Qstate_message) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " state_message = '" + Qstate_message + "'" + " WHERE id='" + Qid + "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 최근 접속 시간 수정
	public static void updateLAST_CONNECTION(String Qid, String Qlast_connection) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "update user set" + " last_connection = '" + Qlast_connection + "'" + " WHERE id='" + Qid
					+ "';";

			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 10. password가 맞는지 확인하기
	// 매개변수: String id, String password
	// 맞으면 return 1 : 틀리면 return -1(or 0)
	public static int checkPASSWORD(String Qid, String Qpassword) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id, password" + " FROM user" + " WHERE id='" + Qid + "'";

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String id = rs.getString(1);
				String password = rs.getString(2);

				System.out.println("id: " + id);
				System.out.println("password: " + password);
				System.out.println();
				if (Qpassword.equals(password)) {
					return 1;
				}
			}
			return -1;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	// 12. 친구신청하기 => 친구신청 테이블에 업데이트
	// 매개변수: String send_id, String Receive_id
	// void(반환x)
	public static void insertFRIEND_PLUS(String Sid, String Rid) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "INSERT INTO FRIEND_PLUS VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Sid);
			pstmt.setString(2, Rid);

			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 13-1. 친구 수락 시, 친구 테이블에 넣어주기
	public static void insertFRIEND(String Sid, String Rid) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "INSERT INTO FRIEND VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Sid);
			pstmt.setString(2, Rid);

			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Rid);
			pstmt.setString(2, Sid);

			count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 13-2. 친구 수락 시, 친구 요청 테이블에서 없애기
	// 매개변수: String send_id, String receive_id - 순서 상관없음.
	// void - 반환 x
	public static void deleteFRIEND_PLUS(String Sid, String Rid) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "DELETE FROM FRIEND_PLUS" + " where (send_id='" + Sid + "' and receive_id='" + Rid
					+ "') or (send_id='" + Rid + "' and receive_id='" + Sid + "'); ;";

			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			System.out.println("제대로 삭제되었습니다.");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/* [채팅관련] */

	// 14. 채팅방 등록
	// 매개변수: String chat_id, String maker_id
	// void 반환x
	public static void insertCHAT(String Qchat_id, String Qmaker_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "INSERT INTO CHAT VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Qchat_id);
			pstmt.setString(2, Qmaker_id);

			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 15. 한 채팅방에 대한 모든 채팅 내용 삭제 (in CHATTING table)
	// 매개변수: String chat_id
	// void - 반환 x
	public static void deleteCHATTING(String Qchat_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "DELETE FROM CHATTING" + " where (chat_id='" + Qchat_id + "');";

			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			System.out.println("제대로 삭제되었습니다.");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 15-2. 한 채팅방 완전 삭제 (in CHAT table)
	// 매개변수: String chat_id
	// void - 반환 x
	public static void deleteCHAT(String Qchat_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "DELETE FROM CHAT" + " where (chat_id='" + Qchat_id + "');";

			pstmt = conn.prepareStatement(sql);
			pstmt.execute();

			System.out.println("제대로 삭제되었습니다.");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 16. 한 채팅방에 대한 모든 채팅 내용 불러오기
	// 매개변수: String chat_id
	// return array[][time, sender, content]
	public static String[][] bringCHATTING(String Qchat_id) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String[][] array = new String[100][3];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT time, sender, content" + " FROM CHATTING" + " WHERE chat_id = '" + Qchat_id + "';";
			rs = stmt.executeQuery(sql);

			int i = 0;
			while (rs.next()) {
				String time = rs.getString(1);
				String sender = rs.getString(2);
				String content = rs.getString(3);

				System.out.println("time: " + time);
				System.out.println("sender: " + sender);
				System.out.println("content: " + content);
				System.out.println();

				array[i][0] = time;
				array[i][1] = sender;
				array[i][2] = content;
				i++;
			}
			return array;
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	// 17. 채팅 내용 기록 등록
	// 매개변수: String chat_id, String time, String sender_id, String content
	// void 반환x
	public static void insertCHATTING(String Qchat_id, String Qtime, String Qsender, String Qcontent) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");

			String sql = "INSERT INTO CHATTING VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Qchat_id);
			pstmt.setString(2, Qtime);
			pstmt.setString(3, Qsender);
			pstmt.setString(4, Qcontent);

			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 0-1. 문자열을 포함하는 아이디찾기 (내친구o)
	// 매개변수: String id, String search
	// return String[id]
	public static String[] searchMYFRIEND(String Qid, String search) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String[] array = new String[20];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id" + " FROM USER" + " WHERE id != '" + Qid + "' AND" + " (id LIKE '%" + search
					+ "%' OR" + " nickname LIKE '%" + search + "%') AND " + " id IN(select friend_id" + " from FRIEND"
					+ " where my_id = '" + Qid + "');";

			rs = stmt.executeQuery(sql);

			int i = 0;
			while (rs.next()) {
				String id = rs.getString(1);

				// System.out.println("id: "+id);

				array[i] = id;
				i++;
			}
			System.out.println();
			return array;
		} catch (ClassNotFoundException e) {
			System.out.println("    ̹   ε      ");
		} catch (SQLException e) {
			System.out.println("     " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	// 0-2. 문자열을 포함하는 아이디찾기 (내친구x)
	// 매개변수: String id, String search
	// return String[id]
	public static String[] searchOTHERFRIEND(String Qid, String search) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String[] array = new String[20];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?characterEncoding=UTF-8&serverTimezone=UTC";
			conn = DriverManager.getConnection(url, "root", "12345");
			stmt = conn.createStatement();

			String sql = "SELECT id" + " FROM USER" + " WHERE id != '" + Qid + "' AND" + " (id LIKE '%" + search
					+ "%' OR" + " nickname LIKE '%" + search + "%') AND " + " id NOT IN(select friend_id"
					+ " from FRIEND" + " where my_id = '" + Qid + "');";

			rs = stmt.executeQuery(sql);

			int i = 0;
			while (rs.next()) {
				String id = rs.getString(1);

				// System.out.println("id: "+id);

				array[i] = id;
				i++;
			}
			return array;
		} catch (ClassNotFoundException e) {
			System.out.println("    ̹   ε      ");
		} catch (SQLException e) {
			System.out.println("     " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

}
