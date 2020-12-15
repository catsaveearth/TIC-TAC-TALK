package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
	private static Socket clientSocket;
	private static PrintWriter out;
	private static Scanner in;
	private static String salt = null;
	private static String ID = null;
	private static AtomicInteger readSocket = new AtomicInteger(1);
	private static AtomicInteger writeSocket = new AtomicInteger(1);
	// socket에 값을 넣고 빼는걸 제어할 친구! 초기값은 1 : 1일때는 사용가능, 0일때는 사용 불가능!
	private static HashMap<String, ChattingOne> PCHAT = new HashMap<String, ChattingOne>(); //누구랑 일댈중인지 저장하는 친구. 친구의 ID가 저장됨.
	private static HashMap<Integer, ChattingMulti> MCHAT = new HashMap<Integer, ChattingMulti>(); //누구랑 일댈중인지 저장하는 친구. 친구의 ID가 저장됨.

	public static String getCurrentTime() {
		Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
		
		//HHmmss
		SimpleDateFormat date_format = new SimpleDateFormat("yyMMddHHmmssSS");

		return date_format.format(date_now).toString();
	}
	
	
	// thread들과 소통하기 위한 변수 부분!!!
	static boolean PWck[] = {false, false}; //초기상태! {값 업데이트 확인, 실제 값}
	static boolean NNck[] = {false, false}; //초기상태! {값 업데이트 확인, 실제 값}
	static boolean settingInfock = false; //settingInfo의 값 업데이트 확인
	static String[] settingInfo = new String[8]; // [ID NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]
	static boolean fsl[] = {false, false}; //{친구내검색 업데이트, 외부친구검색 업데이트)
	static String[][] fslInfo = new String[21][4]; //친구검색한 결과리스트 (ID, name, nickname, last_connection)
	static boolean friendInfock = false; 
	static String[] friendInfo = new String[7]; // [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
	static boolean friend_dbck[] = {false, false}; //서버에서 정보왔는지 확인하는 얘 (PCK, FCK)
	static boolean friend_result[] = {true, true}; //값이 있는지 없는지 알려줌(PCK, FCK)
	static String lefsfe;
	static int roomNum = 0;
	static boolean ckroomNum = false; 
	//boolean변수들을 True로 해놓으면 MainScreen에서 정보를 빼가고 false로 돌려놓을 것.

	
	
	// IP주소와 port number를 통해서 서버와 연결을 시작하는 method.
	public static void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
	}

	// 소금만들기
	public static String makeSalt() {
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			random.nextBytes(bytes);
			String salt = new String(Base64.getEncoder().encode(bytes));

			return salt;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 암호화 함수 짜기
	protected static String encryptionPW(String pw, String salt) {
		String raw = pw;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			md.update(raw.getBytes());
			String hex = String.format("%064x", new BigInteger(1, md.digest()));
			return hex;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// login체크 함수
	protected static boolean logincheck(String id, char[] pw) {
		out.println("REQSALT" + "`|" + id); // 소금요청
		salt = in.nextLine();

		String spw = String.valueOf(pw);
		spw = encryptionPW(spw, salt);
		// 나중에 비밀번호도 암호화해서 넘겨주기

		out.println("LOGIN" + "`|" + id + "`|" + spw);
		String line = in.nextLine();

		if (line.startsWith("LOGIN")) {
			String info[] = line.split("\\`\\|");

			if (info[1].compareTo("SUCCESS") == 0) { // 로그인 성공 메세지를 받았다면
				ID = id;
				return true;
			}
		}
		return false;
	}

	// register 함수
	protected static int register(String git, String temp) {
		// 0 - 회원가입 성공
		// 1 - 아이디 중복
		// 2 - 닉네임 중복
		// 3 - 그냥 실패

		String tp[] = temp.split("\\`\\|");

		// 비밀번호 암호화
		String salt = makeSalt();
		tp[2] = encryptionPW(tp[2], salt);

		temp = "`|" + salt;

		for (String k : tp) {
			temp = temp + "`|" + k;
		}

		// 서버로 정보 보내기
		out.println("REGISTER`|" + git + temp);

		String line = in.nextLine();

		if (line.startsWith("REGISTER")) {
			String info[] = line.split("\\`\\|");

			if (info[1].compareTo("OK") == 0) { // 로그인 성공 메세지를 받았다면
				return 0;
			} else if (info[1].compareTo("ID") == 0) { // 아이디 중복
				return 1;
			} else if (info[1].compareTo("NN") == 0) { // 닉네임
				return 2;
			}
		}
		// 그냥 실패하면 3을 리턴
		return 3;
	}

	// MainScreen에서 사용한다 - 두 함수를 불러와서 메인 정보를 꾸미게 됩니다.
	protected static String[] basicinfo() {
		String[] binfo = new String[3];
		
		for (int i = 0; i < 3; i++) {
			String line = in.nextLine();
			
			if (line.startsWith("BASICINFO")) {
				String info[] = line.split("\\`\\|");

				if (info[1].compareTo("name") == 0) {
					binfo[0] = line.substring(line.indexOf(info[2]));

				} else if (info[1].compareTo("nickname") == 0) {
					binfo[1] = line.substring(line.indexOf(info[2]));

				} else if (info[1].compareTo("state_message") == 0) {
					if (info[2].compareTo("null") == 0) {
						binfo[2] = null;
					} else
						binfo[2] = info[2];
				}
			}
		}
		return binfo;
	}

	public static String[][] friendList() {
		String[][] info = new String[20][5];
		// String[][ID, name, nickname, last_connection, 상메]

		String line = in.nextLine();

		if (line.compareTo("BASICINFO`|FRIENDLIST") != 0) {
			return null;
		}

		line = in.nextLine();
		int idx = 1;

		while (line.compareTo("BFEND") != 0) {
			String i[] = line.trim().split("\\`\\|");

			for (int k = 0; k < 5; k++) { // 아이디, 이름, 닉네임, 접속여부, 상메는?
				info[idx][k] = i[k];
			}
			idx++;
			line = in.nextLine().trim();
		}
		info[0][0] = Integer.toString(idx);

		return info; // 정보를 보여주는 측에서는 처음 []가 null이면 거기서 멈추게 해야할듯. 가능하겠지?
	}

	public static void freeSocket() {
		readSocket.set(1);
		writeSocket.set(1);
	}

	// ========================================이제 여기 위는 건들지마라

	
	// 정보수정할때 pw맞는지 확인하는 함수
	protected static boolean pwcheck(char[] pw) {

		String spw = String.valueOf(pw);
		spw = encryptionPW(spw, salt); // 이미 소금이 있다

		// 비밀번호 체크해달라고 보내고,
		out.println("SETTING`|PWCK`|" + spw);


		// 비밀번호의 체크 여부를 여기서 수령하게 됩니다. -> 새로 체크될 떄 까지 기다리기
		while(PWck[0] != true){
			System.out.println(lefsfe);
		}
		
		PWck[0] = false; //재활용 가능하게 바꿔준다
		System.out.println(PWck[0] + " " + PWck[1]);

		
		if (PWck[1] == true) { //비밀번호가 맞다면 true, 아니라면 false를 리턴
			PWck[1] = false;
			return true;
		}
		return false;
	}

	//내정보 수정 함수
	protected static int modifyInfo(String temp) {
		// 0 - 수정 성공
		// 2 - 닉네임 중복

		// 서버로 정보 보내기
		out.println("SETTING`|SAVE`|" + temp);

		//닉네임 중복여부 확인
		while(NNck[0] != true){
			System.out.println("waiting-modityinfo");
		}
		
		NNck[0] = false; //재활용 가능하게 바꿔준다
		System.out.println(NNck[0] + " " + NNck[1]);

		
		if (NNck[1] == true) { //비밀번호가 맞다면 true, 아니라면 false를 리턴
			return 0;
		}
		return 1;
	}
	
	//내정보 보내주는 함수
	protected static String[] settinginfo() {	
		//일단 서버에 정보를 요청합니다!
		out.println("SETTING`|REQ");
		
		//정보가 오길 기다림
		while(settingInfock != true){
			System.out.println("waiting-settinginfo");
		}
		settingInfock = false;
		
		return settingInfo;
	}

	//외부 친구 검색 리스트를 보내주는 함수
	protected static String[][] NotfriendSearchList(String kw) {
		// String[][name, nickname, last_connection]

		//일단 서버에 정보를 요청합니다! (with kw)
		out.println("SEARCH`|OF`|" + kw);
		
		//정보가 오길 기다림
		while(fsl[1] != true){
			System.out.println("waiting-NFSL");
		}
		fsl[1] = false;
		return fslInfo;
	}
	
	//친구 내 검색 리스트를 보내주는 함수
	protected static String[][] FriendSearchList(String kw) {
		// String[][name, nickname, last_connection]

		//일단 서버에 정보를 요청합니다! (with kw)
		out.println("SEARCH`|MF`|" + kw);
		
		//정보가 오길 기다림
		while(fsl[0] != true){
			System.out.println("waiting-FSL");
		}
		fsl[0] = false;
		
		return fslInfo;
	}
	
	//친구 정보를 받아오는 함수
	protected static String[] getFriendInfo(String FID) {
		
		//일단 서버에 정보를 요청합니다!
		out.println("FRIEND`|INFO`|" + FID);
		
		//정보가 오길 기다림
		while(friendInfock != true){
			System.out.println("waiting-FINFO");
		}
		friendInfock = false;

		return friendInfo;
	}
		
	//친구신청하는 함수
	protected static int requsetFriend(String fid) {
		//1 : 친구신청 완료, 0 : 친구신청 실패 (이미 되어있는거임)
		
		//일단, 친구신청 테이블에 존재하는지 확인하기
		out.println("FRIEND`|PCK`|" + fid);
		//그리고 친구 테이블에도 존재하는지 확인하기
		out.println("FRIEND`|FCK`|" + fid);

		//연락기다리기 (둘 중 하나라도 아직 false면 넘기면 안됨)
		while(friend_dbck[0] == false || friend_dbck[1] == false) {
			System.out.println("waiting-RF");
		}
		friend_dbck[0] = false;
		friend_dbck[1] = false;


		//둘다 false여야 친구도 아니고 친구신청 테이블에도 없는 것이 된다 => 그럼 신청해도 된다는 뜻!
		if(friend_result[0] == false && friend_result[1] == false) {
			//통과한다면 친구신청 테이블에 넣어주라고 요청!
			out.println("FRIEND`|APP`|" + fid);
			return 1;
		}
		return 0;
	}
	
	
	
	//==================채팅 기능 관련 함수
	
	// <일대일 채팅>
	//상대방이랑 일대일 채팅중인지 확인
	protected static boolean ckINPCHAT(String FID) {
		if(PCHAT.containsKey(FID)) return true;
		else return false;
	}
	
	//일대일 채팅중인 사람들 모아두는 hashmap에 넣기
	protected static void addPCHAT(String FID, ChattingOne chat) {
		PCHAT.put(FID, chat);
	}
	
	//일대일 채팅중인 사람들 모아두는 hashmap에서 삭제하고 상대방에게 나간다고 말함
	protected static void delPCHAT(String FID) {
		PCHAT.remove(FID);
		out.println("PCHAT`|outCHAT`|" + FID);
	}
	
	//상대방에게 채팅 하고 싶다고 요청
	protected static void ckANSWER(String FID) {
		//서버에 나 얘랑 채팅하고 싶다고 요청하기!
		out.println("PCHAT`|REQCHAT`|" + FID);
	}
	
	//상대방에게 채팅을 수락한다고 Y/N 보내기 (일댈버전)
	protected static void CHATANSWER(String FID, boolean ans) {
		//PCHAT`|PESPONCHAT`|" + 채팅요청자ID + Y/N : 채팅할거냐고 물어봣을때 채팅 할건지 말건지 답변
		System.out.println("2 =>" + ans);
		
		//그래 나 너랑 채팅할게!
		if(ans) out.println("PCHAT`|PESPONCHAT`|" + FID + "`|Y");
		else out.println("PCHAT`|PESPONCHAT`|" + FID+ "`|N");
	}

	
	//사용자가 보내는 채팅을 받아서 서버로 전송하는 역할. (받는사람과 보내는 내용)
	//PCHAT`|sendCHAT`|" + 채팅받는자ID + Content : 채팅내용 전송 (내가쓴거임)
	protected static void sendPCHAT(String FID, String chat) {
		out.println("PCHAT`|sendCHAT`|" + FID + "`|" + chat);
	}
	
	
	// <멀티챗>==========================================
	//서버에게 룸만든다고 요청
	protected static void makeMultiRoom(String roomname, String showpre, String flist) {
		//"MCHAT`|REQROOM`|" + 방이름 + 내용 보임 여부 +  방만들기 요청자 ID + flist      //방만들기 요청 
		out.println("MCHAT`|REQROOM`|" + roomname + "`|" + showpre + "`|" + ID + "`|" + flist);
		
		// room number를 받기를 기다림
		while(ckroomNum != true){
			System.out.println("wait roomNum");
		}
		ckroomNum = false; //재활용 가능하게 바꿔준다
		
		int rn = roomNum;
		//멀티 챗을 위한 채팅창을 띄워줍니다
		ChattingMulti nchat = new ChattingMulti(rn, roomname);
		
		//채팅창을 관리 hashMap에 넣어줍니다
		MCHAT.put(rn, nchat);
	}

	//상대방에게 채팅을 수락한다고 Y/N 보내기 (멀티버전)
	protected static void MCHATANSWER(int roomid, String roomname, boolean ans) {
		//그래 나 너랑 채팅할게!
		if(ans) {
			out.println("MCHAT`|RESPONCHAT`|" + roomid + "`|" + ID + "`|Y");
			ChattingMulti nchat = new ChattingMulti(roomid, roomname);
			//채팅창을 관리 hashMap에 넣어줍니다
			MCHAT.put(roomid, nchat);
			System.out.println(roomid + "한다구");
		}
		//아니면 아예 무시! 
	}
	
	//사용자가 보내는 채팅을 받아서 서버로 전송하는 역할. (받는사람과 보내는 내용)
	//"MCHAT`|sendCHAT`|" + 방번호 + 채팅 보낸자ID + 시간 + content // 채팅 전송
	protected static void sendMCHAT(int rn, String chat) {
		out.println("MCHAT`|sendCHAT`|"+ Integer.toString(rn) + "`|" + ID + "`|" + getCurrentTime() + "`|" + chat);
	}
	
	//나 나가용
	//"MCHAT`|OUTCHAT`|" + 방번호 + 나가는ID //채팅에서 나갑니다
	protected static void delMCHAT(int rn) {
		MCHAT.remove(rn);
		out.println("MCHAT`|OUTCHAT`|" + rn + "`|" + ID);
	}
	
	//들어온 사람 리스트좀 주세요
	//"MCHAT`|REQuLIST`|" + 방번호 //채팅에서 나갑니다
	protected static void reqULIST(int rn) {
		out.println("MCHAT`|REQuLIST`|" + rn);
	}
		
	//친구 초대할거에요!
	//"MCHAT`|InviteFriend`|" + 친구 아이디(들)
	protected static void InviteFriend(int rn, String list) {
		out.println("MCHAT`|InviteFriend`|" + rn + "`|" + list);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		String file = "C:\\Users\\jimin\\OneDrive\\바탕 화면\\수업\\2학년 2학기\\컴퓨터네트워크 및 실습\\팀플\\EXERCISE\\src\\serverinfo.dat";
		String ip = null;
		int port = 0;

		// server에 대한 환경 설정을 위한 파일 읽어오기
		try {
			BufferedReader fileIn = new BufferedReader(new FileReader(file));
			ip = fileIn.readLine();
			String portString = fileIn.readLine();
			port = Integer.parseInt(portString);
			fileIn.close();
			startConnection(ip, port);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// stream이 연결되었습니다!

		// main screen을 구축하는 동안, 다른곳에서는 socket사용 못하게 막아놓기!
		// mainsScreen구축까지 돌아가는 login안의 로직은 atomic이 보장됨 (순서대로 가기 때문)
		readSocket.set(0);
		writeSocket.set(0);

		// 그럼 로그인 or 회원가입 화면이 뜬다
		new LogIn();

		// main구축이 끝나면 MainScreen에서 함수를 불러서 socket의 제한을 풀어줄 것.
		// => 이때부터 thread에서 입출력가능
		// 이제부터는 동적 이벤트 뿐! 즉, server에서 연락이 오던지, client가 먼저 작동하던지 둘 중 하나다.

		// input이랑 Basic받을 얘
		ExecutorService b_pool = Executors.newFixedThreadPool(2);

		b_pool.execute(new input());

		//이제부터 서버에서 오는 모든 입력은 input thread를 통해서 처리됨
	}
	
	// 오로지 입력만 받는 쓰레드 (입력 들어오면 정리해서 필요한 곳에 넣어준다!!)
	public static class input implements Runnable {
		// 즉, 입력의 경우의 수가 전부 존재해야함!

		@Override
		public void run() {
			while (readSocket.get() == 0) {}; // 1되면 풀려남
			// 연락 받는건 무조건 이 thread에서 처리!

			try {
			while (true) {
				System.out.println("뱅뱅");
				String line = in.nextLine();
				System.out.println(line);

				
				//새로운 정보가 업데이트되는 부분
				if(line.startsWith("UPDATE")) {
					String info[] = line.split("\\`\\|");
					
					//친구요청이 들어왔어요 => UPDATE FRIREQ NN name FID
					if(info[1].compareTo("FRIREQ") == 0) {
						int result = MainScreen.showFriendPlus(info[2], info[3]);
						
						if(result == 1) { //친구를 수락햇다면
							out.println("FRIEND`|OK`|" + info[4]);	
						}
						else if(result == 0) {//친구를 거절했다면
							out.println("FRIEND`|NO`|" + info[4]);	
						}
					}

					//내 정보를 업데이트 하라네? 형식 ; UPDATE MYINFO name nn state_m
					else if(info[1].compareTo("MYINFO") == 0) {
						MainScreen.changeMyInfo(info[2], info[3], info[4]);
					}
					
					//친구 정보를 업데이트 해주자! 형식 ; UPDATE FINFO ID name nn state_m
					else if(info[1].compareTo("FINFO") == 0) {
						String[] finfo = {info[2], info[3], info[4], info[5]};
						MainScreen.changeFriendInfo(finfo);
					}
					
					
					//친구가 들어왔다/나갔다? 형식 ; UPDATE F_state F_ID 상태(0이면 온라인)
					else if(info[1].compareTo("F_state") == 0) {
						MainScreen.changeFriendstate(info[2], info[3]);
					}
					
				}
				
				// 친구 관련
				else if(line.startsWith("FRIEND")) {
					String info[] = line.split("\\`\\|");

					
					//친구 정보를 받았다 	=> FRIEND INFO [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
					if(info[1].compareTo("INFO") == 0) {
						//정보를 저장해준다 [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
						friendInfo[0] = info[2];
						friendInfo[1] = info[3];
						friendInfo[2] = info[4];
						friendInfo[3] = info[5];
						friendInfo[4] = info[6];
						friendInfo[5] = info[7];
						friendInfo[6] = info[8];
						
						friendInfock = true;
					}
					
					// 친구 추가 테이블에 대해 확인한 정보 => FRIEND`|PCK`|" + T/F
					else if(info[1].compareTo("PCK") == 0) {
						if(info[2].compareTo("T") == 0) {
							friend_result[0] = true;
						}
						else friend_result[0] = false;

						friend_dbck[0] = true;
					}
					
					// 친구 테이블에 대해 확인한 정보 => FRIEND`|FCK`|" + T/F
					else if(info[1].compareTo("FCK") == 0) {
						if(info[2].compareTo("T") == 0) {
							friend_result[1] = true;
						}
						else friend_result[1] = false;

						friend_dbck[1] = true;
					}
					
					// 새 친구를 list에 업데이트 해주세요~~ => FRIEND`|APND`|" + ID, name, nickname, last_connection, 상메
					else if(info[1].compareTo("APND") == 0) {
						String[] finfo = {info[2], info[3], info[4], info[5], info[6]};
						MainScreen.changeFriend(finfo);
					}
				}

				// 검색
				else if (line.startsWith("SEARCH")) {
					String info[] = line.split("\\`\\|");

					// 검색 결과가 돌아왔다 => SEARCH REQ MF/OF [검색된 친구 수] [친구 리스트...  어떻게 넘겨줄것인가.... 이름, 별명, 음... 다른정보들? 일단 지민이가 GUI만든거보고 생각하자]
					if(info[1].compareTo("REQ") == 0) {

						int num = Integer.parseInt(info[3]);
						
						for(int i = 1 ; i<=num ; i++) {
							String ln[] = info[i + 3].split("\\^");
							
							for(int j = 0;j<4;j++) {
								fslInfo[i][j] = ln[j];
							}
						}
						fslInfo[0][0] = Integer.toString(num);
											
						if(info[2].equals("MF")) fsl[0] = true;
						else fsl[1] = true;
					}
				}
				
				//설정
				else if (line.startsWith("SETTING")) {
					String info[] = line.split("\\`\\|");

					// 내 정보를 받는다 => SETTING INFO [ID NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]			
					if(info[1].compareTo("INFO") == 0) {
						int idx = 2;

						for(int i=0;i<8;i++, idx++) {
							settingInfo[i] = info[idx];
						}
						settingInfock = true;
					}

					// 비밀번호 체크 요청한거 응답=> SETTING PWCK OK/NO
					else if(info[1].compareTo("PWCK") == 0) {
						if(info[2].compareTo("OK") == 0) PWck[1] = true;
						else PWck[1] = false;
						
						PWck[0] = true;
					}
					 
					// 셋팅 저장에서 닉네임 겹치는 경우 체크 => SETTING NN
					else if(info[1].compareTo("NN") == 0) {
						if(info[2].compareTo("OK") == 0) NNck[1] = true;
						else NNck[1] = false;
						
						NNck[0] = true;
						System.out.println("=>" + NNck[0] + " " + NNck[1]);
					}
				}

				//채팅방 관련 (1댇)
				else if (line.startsWith("PCHAT")) {
					String info[] = line.split("\\`\\|");

					//PCHAT`|QUSCHAT`|" + 채팅요청자ID + 별명 + 이름 : 
					//상대방 알려주면서 채팅할거냐고 물어보기   =>받는쪽 : 이때 별명(이름), ID 저장하기
					if(info[1].compareTo("QUSCHAT") == 0) {
						//채팅할거냐고 물어보기 -> 메인 페이지에서 팝업 띄워서 물어보자!
						MainScreen.showPCHAT(info[2], info[3], info[4]);		
					}

					//상대방이 채팅을 수락햇는지 거절햇는지 체크
					//"PCHAT`|ANSCHAT`|" + ID +"`|" + map.get("NICKNAME")+ "`|" + map.get("NAME")
					else if(info[1].compareTo("ANSCHAT") == 0) {
						//상대방의 수락여부를 체크 info[5]& 이름과 닉네임 저장
						PCHAT.get(info[2]).checkAnswer(info[5], info[3], info[4]);
					}
					
					//PCHAT`|receivedCHAT`|" + 채팅보낸자ID + Content : 채팅내용 전송 (내가 받은거)
					else if(info[1].compareTo("receivedCHAT") == 0) {
						//받은 채팅 내용 보내주기!
						PCHAT.get(info[2]).receiveChat(info[3]);
					}
					
					//PCHAT`|OUTCHAT`|" + 채팅보낸자ID
					else if(info[1].compareTo("OUTCHAT") == 0) {
						//받은 채팅 내용 보내주기!
						if(PCHAT.containsKey(info[2]))
							PCHAT.get(info[2]).endchat();
					}
				}

			
				
				//채팅방 관련 (멀티)
				else if (line.startsWith("MCHAT")) {
					String info[] = line.split("\\`\\|");

					//상대방 알려주면서 채팅할거냐고 물어보기  
					//client.get(id).println("MCHAT`|INVCHAT`|" + room_num + "`|" + room_name + "`|" + makerInfo);
					if(info[1].compareTo("INVCHAT") == 0) {
						//채팅할거냐고 물어보기 -> 메인 페이지에서 팝업 띄워서 물어보자!
						MainScreen.showMCHAT(Integer.parseInt(info[2]), info[3], info[4]);		
					}
					
					//밤 번호 알려주는 부분
					//"MCHAT`|RoomNumber`|" + 방번호
					else if(info[1].compareTo("RoomNumber") == 0) {
						roomNum = Integer.parseInt(info[2]);
						ckroomNum = true;
					}
					
					//client.get(id).println("MCHAT`|ANSCHAT`|" + room_num + "`|" + m.getSender_id());
					//누구 들어왔다고 알리는 부분
					else if(info[1].compareTo("ANSCHAT") == 0) {
						System.out.println(MCHAT.keySet());
						MCHAT.get(Integer.parseInt(info[2])).notifyCome(info[3]);
					}
					
					
					//client.get(id).println("MCHAT`|receivedCHAT`|" + room_num 
					//		+ "`|" + m.getSender_id() + "`|" + senderInfo + "`|" + m.getMessage());
					//메세지를 받았습니다
					else if(info[1].compareTo("receivedCHAT") == 0) {
						if(info[3].equals(ID)) 	MCHAT.get(Integer.parseInt(info[2])).receiveChat("나", info[5]);
						else MCHAT.get(Integer.parseInt(info[2])).receiveChat(info[4], info[5]);

					}
					
					//client.get(id).println("MCHAT`|outCHAT`|" + room_num + "`|" + m.getSender_id() + "`|" + senderInfo);
					//누구 나간다고 알리는 부분
					else if(info[1].compareTo("outCHAT") == 0) {
						MCHAT.get(Integer.parseInt(info[2])).notifyOut(info[4]);
					}
					
					//"MCHAT`|ulist`|" + 방번호 + 리스트 채울 수 있는 정보 
					//방에 있는 사람들 리스트
					else if(info[1].compareTo("ulist") == 0) {
						String userList[] = info[3].split("\\^");
						ChattingOnlinePeople people = new ChattingOnlinePeople(userList);
					}
					
					//"MCHAT`|PRECHAT`|" + room_num +  "`|" + cnum + "`|" + messagelist
					//입장전 채팅 보여주는 얘!
					else if(info[1].compareTo("PRECHAT") == 0) {
						String ulist[][] = new String[100][2];
						
						for(int i = 4 ; i < 3 + Integer.parseInt(info[3]) ; i++) {
							String userList[] = info[i].split("\\^");

							ulist[i-4][0] = userList[0];
							ulist[i-4][1] = userList[1];
							System.out.println("=> " + userList[0] + userList[1]);
						}

						MCHAT.get(Integer.parseInt(info[2])).PrereceiveChat(Integer.parseInt(info[3]), ulist); ;

					}
					
				}
			}
		
		}finally {
			//개인채팅들 다 나가기
			for(ChattingOne a : PCHAT.values()) {
				a.FexitChat();
			}
			PCHAT.clear();
			
			//멀티채팅들도 다 나가기
			for(ChattingMulti a : MCHAT.values()) {
				int rnum = a.roomnumber;
				MCHAT.remove(rnum);
				out.println("MCHAT`|OUTCHAT`|" + rnum + "`|" + ID);
			}
			MCHAT.clear();
		}
	}
}
}
