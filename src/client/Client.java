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
import java.util.Base64;
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
		String[][] info = new String[20][4];
		// String[][name, nickname, last_connection, 상메]

		String line = in.nextLine();

		if (line.compareTo("BASICINFO`|FRIENDLIST") != 0) {
			return null;
		}

		line = in.nextLine();
		int idx = 1;

		while (line.compareTo("BFEND") != 0) {
			String i[] = line.trim().split("\\`\\|");

			for (int k = 0; k < 4; k++) { // 이름, 닉네임, 접속여부
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

	// thread들과 소통하기 위한 변수 부분!!!

	
	static boolean PWck[] = {false, false}; //초기상태! {값 업데이트 확인, 실제 값}
	static boolean NNck[] = {false, false}; //초기상태! {값 업데이트 확인, 실제 값}
	static boolean settingInfock = false; //settingInfo의 값 업데이트 확인
	static String[] settingInfo = new String[8]; // [ID NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]


	// 정보수정할때 pw맞는지 확인하는 함수
	protected static boolean pwcheck(char[] pw) {

		String spw = String.valueOf(pw);
		spw = encryptionPW(spw, salt); // 이미 소금이 있다

		// 비밀번호 체크해달라고 보내고,
		out.println("SETTING`|PWCK`|" + spw);


		// 비밀번호의 체크 여부를 여기서 수령하게 됩니다. -> 새로 체크될 떄 까지 기다리기
		while(PWck[0] != true){
			System.out.println("waiting-pwcheck");
		}
		
		PWck[0] = false; //재활용 가능하게 바꿔준다
		System.out.println(PWck[0] + " " + PWck[1]);

		
		if (PWck[1] == true) { //비밀번호가 맞다면 true, 아니라면 false를 리턴
			return true;
		}
		return false;
	}

	// 내정보 수정 함수
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
		String[] binfo = new String[9];
		
		//일단 서버에 정보를 요청합니다!
		out.println("SETTING`|REQ");
		
		//정보가 오길 기다림
		while(settingInfock != true){
			System.out.println("waiting-settinginfo");
		}
		settingInfock = false;
		
		return settingInfo;
	}

	
	
	
	public static void main(String[] args) {
		String file = "serverinfo.dat";
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
		b_pool.execute(new basic());
//		
//		//채팅방 받을 얘
//		ExecutorService chat_pool = Executors.newFixedThreadPool(100);
//
//

//		//이제부터 서버에서 오는 모든 입력은 input thread를 통해서 처리됨

	}

	// 오로지 입력만 받는 쓰레드 (입력 들어오면 정리해서 필요한 곳에 넣어준다!!)
	public static class input implements Runnable {
		// chat이면 챗,
		// 뭐 친구요청이면 친구요청 등등 처리하게
		// 친구 신청 요청은 여기서 받게 하자
		// 즉, 입력의 경우의 수가 전부 존재해야함!!!!!!!!!

		@Override
		public void run() {
			while (readSocket.get() == 0) {}; // 1되면 풀려남
			// 연락 받는건 무조건 이 thread에서 처리!

			while (true) {
				String line = in.nextLine();

				// 친구 관련
				if(line.startsWith("FRIEND")) {
					String info[] = line.split("\\`\\|");

					// 친구 요청이 들어왔다 => FRIEND REQ [친구 이름] [친구 별명]
					if(info[1].compareTo("REQ") == 0) {


					}
				}

				// 검색
				else if (line.startsWith("SEARCH")) {
					String info[] = line.split("\\`\\|");

					// 검색 결과가 돌아왔다 => SEARCH REQ [검색된 친구 수] [친구 리스트...  어떻게 넘겨줄것인가.... 이름, 별명, 음... 다른정보들? 일단 지민이가 GUI만든거보고 생각하자]
					if(info[1].compareTo("REQ") == 0) {


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
						System.out.println("=>" + PWck[0] + " " + PWck[1]);

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
				else if (line.startsWith("NMCHAT")) {
					String info[] = line.split("\\`\\|");


					if(info[1].compareTo("INFO") == 0) {

						
						

					}

					// 비밀번호 체크 요청한거 응답=> SETTING PWCK 1/0 (1이 true, 0이 false)
					else if(info[1].compareTo("PWCK") == 0) {


					}
				}

				//채팅방 관련 (멀티)
				else if (line.startsWith("MCHAT")) {
					String info[] = line.split("\\`\\|");


					if(info[1].compareTo("INFO") == 0) {

						
						

					}

					// 비밀번호 체크 요청한거 응답=> SETTING PWCK 1/0 (1이 true, 0이 false)
					else if(info[1].compareTo("PWCK") == 0) {


					}
				}
				
				
			}
		}
	}

	// 메인화면을 제어할 친구!
	public static class basic implements Runnable {

		@Override
		public void run() {

		}

	}

	// 채팅방 마다 생기는 스레드
	public static class chat implements Runnable {

		private int room_num;

		@Override
		public void run() {

		}

	}

}
