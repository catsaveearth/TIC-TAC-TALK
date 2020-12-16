/**
MainServer.java

modifier: Kim Su hyeon.
E-mail Address: tpfbdpf@naver.com
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import Variable.Message;
import Variable.TMessage;
import java.text.SimpleDateFormat;


public class MainServer {
	public static HashMap<String, PrintWriter> client = new HashMap<>(); //접속중인 client관리
	public static HashMap<Integer, Queue<Message>> messageSet = new HashMap<>(); //chat thread와의 공유메모리
	public static HashMap<Integer, Queue<TMessage>> TTTSet = new HashMap<>(); //TTT thread와의 공유메모리

	//공유메모리를 사용할 때 원자성 보장 (Ensure atomic when using shared memory)
	private static AtomicInteger messageCK = new AtomicInteger(1); 
	private static AtomicInteger TTTCK = new AtomicInteger(1);

	//thread들을 관리하는 thread pool (The thread pool that manages the threads)
	private static ExecutorService messagepool = Executors.newFixedThreadPool(500);
	private static ExecutorService filepool = Executors.newFixedThreadPool(50);
	private static ExecutorService TTTpool = Executors.newFixedThreadPool(50);

	final private static int portnum = 6789;
	private static int forroomnumber = 1;
	private static int TTTnumber = 1;
	private static int fileportnum = 33333;

	// 현재 시간을 가져옴 (get current time)
	public static String getCurrentTime() {
		Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다

		// HHmmss
		SimpleDateFormat date_format = new SimpleDateFormat("yyMMddHHmmssSS");
		return date_format.format(date_now).toString();
	}

	public static void main(String[] args) throws Exception {
		// client와 소통하는 thread를 관리할 pool생성 (최대 500명까지 가능)
		ExecutorService pool = Executors.newFixedThreadPool(500);

		System.out.println("start server!");
		try (ServerSocket listener = new ServerSocket(portnum)) { // listener socket생성
			while (true) {
				pool.execute(new Handler(listener.accept())); // socket연결요청이 오면 accept하고 thread를 생성하며 socket을 넘겨줌
			}
		}
	}

	/**
	 * client thread 코드. - 이제 client와 관련된 일은 모두 여기서 처리한다. client가 연결 될 때 마다 Handler
	 * thread가 하나 씩 생겨나서 client에서의 입력을 전담 마크한다. Handler thread는 client hashmap에서
	 * 관리된다. Client thread to here. are all code - now the work associated with
	 * client Every time be linked client handler client, caused by a single thread
	 * for input from the mark. Handler thread the client hashmap in.
	 */
	public static class Handler implements Runnable {
		// 연결 socket과 stream
		private Socket socket;
		private Scanner in;
		private PrintWriter out;
		Thread thread;

		// 사용자 정보 저장
		private String ID = null;

		// constructor -> stream 연결작업
		public Handler(Socket socket) throws IOException {
			this.socket = socket; // 생성할 때 socket 받음
		}

		@Override
		public void run() {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream(), true);

				// 메인 화면 뜰  까지 돌아가는 것
				while (true) { // 처음에 로그인 과정 + 회원가입 => 로그인을 해야지 이 while을 넘어간다
					String line = in.nextLine();
					System.out.println(line);

					// 로그인 : LOGIN ID PASSWORD
					if (line.startsWith("REQSALT")) {
						String info[] = line.split("\\`\\|");
						System.out.println(info[1]);
						out.println(query.bringSALT(info[1]));
					}
					else if (line.startsWith("LOGIN")) {
						String info[] = line.split("\\`\\|");
						// 아이디 비번이 맞는지 데이터베이스와 대초하며 체크
						int rt = query.selectLOGIN(info[1], info[2]);

						if (rt == 1) { // 만약 맞다면, ID값을 저장
							this.ID = info[1];
							client.put(ID, out); // clinet hashmap에 사용자등록
							out.println("LOGIN`|SUCCESS");
							break;
						} else { // 아니면 다시 로그인하라고 해줌
							out.println("LOGIN`|FAIL");
						}
					}

					// 회원가입
					else if (line.startsWith("REGISTER")) {
						String info[] = line.split("\\`\\|");

						for (String t : info) {
							System.out.println(t);
						}

						HashMap<String, String> temp = new HashMap<String, String>();
						temp.put("SALT", info[2]);
						temp.put("ID", info[3]);
						temp.put("NICKNAME", info[4]);
						temp.put("PASSWORD", info[5]);
						temp.put("NAME", info[6]);
						temp.put("PHONE", info[7]);
						temp.put("EMAIL", info[8]);
						temp.put("BIRTH", info[9]);

						if (info[1].compareTo("1") == 0) {
							temp.put("GITHUB", info[10]);
						}

						// 일단 id, nickname중복 체크를 한다.
						// 만약 중복체크를 하는데 겹친다면, 어디가 안되는지 리턴
						if (query.selectID(info[3]) == -1) {
							out.println("REGISTER`|ID");
						} else if (query.selectNICKNAME(info[4]) == -1) {
							out.println("REGISTER`|NN");
						} else {
							query.insertUSER(temp);
							out.println("REGISTER`|OK");
						}
						query.updateLAST_CONNECTION(info[3], getCurrentTime()); // 지금 시간 넣어주기
					}
				}
				//로그인 성공시 여기서 시작
							
				// 첫 시작시 기본 정보들을 client에게 전부 보내준다 JDBC를 이용해야함
			    HashMap<String,String> binfo = new HashMap<String,String>();
			    binfo = query.selectNAME_NICKNAME_STATE(ID);
				
				out.println("BASICINFO`|name`|" + binfo.get("NAME"));
				out.println("BASICINFO`|nickname`|" + binfo.get("NICKNAME"));
				out.println("BASICINFO`|state_message`|" + binfo.get("STATE_MESSAGE"));
				
				//접속상태 업데이트!!! -> 지금 접속중이라는 뜻
				query.updateLAST_CONNECTION(ID, "0");
								
				//친구목록(친구가 접속중인지 아닌지도)
				String[][] f_list = query.selectFRIEND(ID);

				out.println("BASICINFO`|FRIENDLIST"); //이제부터 친구 리스트가 넘어간다고 신호를 준다
				int fnum = Integer.parseInt(f_list[0][1]) - 1;
				
				for(int i=1;i<=fnum;i++) {
					String[] l = f_list[i];
					String flist = "";
					int ck = 1;

					for(String t : l) {
						if(ck==1) flist = t;
						else flist = flist + "`|" + t;
						ck++;
					}
					out.println(flist);
				}
				out.println("BFEND"); //이제 넘기는거 종료라는 뜻!
				
				//다른 사람들에게 내가 들어왔다고 알린다! (접속상태 변경)
				for (PrintWriter output : client.values()) {
					if(output.equals(out)) continue;
					//모두에게 보내면 client에서 알아서 걸러서 들을것임
					//형식 ; UPDATE F_state F_ID 상태(1이면 오프라인)
					output.println("UPDATE`|F_state`|" + ID + "`|" + 0);
				}
				//여기까지가 (로그인 & 회원가입 & 기본 정보 보내주기) <<==================================

				//이제 친구신청을 감지하는 Thread와 아래의 while이 동시에 돌아가게 됩니다
				RealTimeUpdater runnable = new RealTimeUpdater(ID, out);
				thread = new Thread(runnable);

				thread.start();	//친구신청 확인 thread 작동 시작
				System.out.println("돌기시작합니다!");

				//프로그램이 돌아가는 동안 소통이 이루어지는 부분 (클라이언트로부터 입력을 받는부분! / client -> server)
				while (true) {
					System.out.println("빙글뱅글!");
					
					String line = in.nextLine();
					System.out.println(line);
					
/**친구 관련 처리========================================*/
					if (line.startsWith("FRIEND")) {
						String info[] = line.split("\\`\\|");
						
						//친구 신청 (apply) => FRIEND APP [FID]
						if(info[1].compareTo("APP") == 0) {
							//ID, FID가 친구 신청 대기 table에 들어가게 된다.
							query.insertFRIEND_PLUS(ID, info[2]);
						}
						
						//친구 수락 => FRIEND OK [FID]
						else if(info[1].compareTo("OK") == 0) {
							//친구신청 테이블에서 삭제해주고, 친구 테이블에 추가해준다.
							query.deleteFRIEND_PLUS(ID, info[2]);
							query.insertFRIEND(ID, info[2]);
							
							//친구를 수락해서 Friend가 되었다면, client에도 추가를 해줘야겠죠?
							//일단 내꺼 업데이트하기 => 친구 테이블을 재구성하라고 알려주는거임
							//ID, name, nickname, last_connection, 상메
							HashMap<String, String> mapmy = query.bringINFO(info[2]);	
							out.println("FRIEND`|APND`|" + mapmy.get("ID") + "`|" + mapmy.get("NAME") + "`|" + mapmy.get("NICKNAME") + "`|"
									+ mapmy.get("LAST_CONNECTION") + "`|"+ mapmy.get("STATE_MESSAGE"));
							
							//만약 친구도 접속중이라면, 친구 list를 업데이트 시켜주라고 직접 말해주기!
							if(client.containsKey(info[2])) {
								HashMap<String, String> mapp = query.bringINFO(ID);	
								client.get(info[2]).println("FRIEND`|APND`|" + mapp.get("ID") + "`|" + mapp.get("NAME") + "`|" + mapp.get("NICKNAME") + "`|"
										+ mapp.get("LAST_CONNECTION") + "`|"+ mapp.get("STATE_MESSAGE"));
							}
						}
						
						//친구 거절 => FRIEND NO [FID]
						else if(info[1].compareTo("NO") == 0) {
							//친구 신청 테이블에서 삭제해주고, 끝.
							query.deleteFRIEND_PLUS(ID, info[2]);
						}
						
						//친구 상세정보 확인 => FRIEND INFO [NICKNAME NAME STATE_MESSAGE EMAIL PHONE BIRTH GITHUB]
						else if(info[1].compareTo("INFO") == 0) {
							HashMap<String, String> map = query.bringINFO(info[2]);
							out.println("FRIEND`|INFO`|" + map.get("NICKNAME") + "`|" + map.get("NAME") + "`|" + map.get("STATE_MESSAGE")  + "`|" 
									+ map.get("EMAIL")  + "`|" + map.get("PHONE") + "`|" + map.get("BIRTH")  + "`|" + map.get("GITHUB")  + "`|");
						}
						
						//친구 신청 테이블에 있는지 확인 => FRIEND PCK [FID]
						//없으면 false를 리턴한다.
						else if(info[1].compareTo("PCK") == 0) {
							String ck = query.checkFRIEND_PLUS(ID, info[2]);
							System.out.println("ww" + ck);

							if(ck.compareTo("true") == 0) out.println("FRIEND`|PCK`|T");
							else out.println("FRIEND`|PCK`|F");
						}
						
						//친구 테이블에 있는지 확인 => FRIEND FCK [FID]
						else if(info[1].compareTo("FCK") == 0) {

							String ck = query.checkFRIEND(ID, info[2]);
							System.out.println(ck);

							if(ck.compareTo("true") == 0) out.println("FRIEND`|FCK`|T");
							else out.println("FRIEND`|FCK`|F");	
						}
					}
					
/**검색 관련 처리========================================*/
					else if (line.startsWith("SEARCH")) {
						String info[] = line.split("\\`\\|");
						
						//내 친구 검색 (my friend) => SEARCH MF [검색어]
						if(info[1].compareTo("MF") == 0) {
							String[][] idlist = query.searchMYFRIEND(ID, info[2]);
							int num = 0;
							String list = null;
							
							
							if(idlist == null) {
								out.println("SEARCH`|REQ`|MF`|" + 0);
								continue;
							}
							
							for(String[] s : idlist) {
								if(s[0] == null) break;
								num++;
								int ck = 1;
								
								for(String k : s) {
									if(num == 1 && ck == 1) list =  k;
									else if(ck == 1) list = list + "`|" + k;
									else list = list + "^" + k;
									ck++;
								}
								System.out.println(list);
							}
							//값을 보내줘야함
							out.println("SEARCH`|REQ`|MF`|" + num + "`|" + list);
						}
						
						//외부 친구 검색 (other friend) => SEARCH OF [검색어]
						else if(info[1].compareTo("OF") == 0) {
							String[][] idlist = query.searchOTHERFRIEND(ID, info[2]);
							int num = 0;
							String list = null;
							
							if(idlist == null) {
								out.println("SEARCH`|REQ`|OF`|" + 0);
								continue;
							}

							for(String[] s : idlist) {
								if(s[0] == null) break;
								num++;
								int ck = 1;
								
								for(String k : s) {
									if(num == 1 && ck == 1) list = "`|" + k;
									else if(ck == 1) list = list + "`|" + k;
									else list = list + "^" + k;
									ck++;
								}
								System.out.println(list);
							}

							//값을 보내줘야함
							out.println("SEARCH`|REQ`|OF`|" + num + list);
							
							System.out.println(num);
							System.out.println("SEARCH`|REQ`|OF`|" + num + list);
						}
					}
								
/**설정 관련 처리========================================*/
					else if (line.startsWith("SETTING")) {
						String info[] = line.split("\\`\\|");

						
						//설정에 입장하기 위한 비번 check => SETTING PWCK [암호화된 PW]
						if(info[1].compareTo("PWCK") == 0) {
							int ck = query.checkPASSWORD(ID, info[2]);

							//결과 리턴
							if(ck == 1) out.println("SETTING`|PWCK`|OK");
							else out.println("SETTING`|PWCK`|NO");
						}
						
						//설정을 저장 => SETTING SAVE [0 NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]
						//           SETTING SAVE [1 PW SALT NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE] => 어차피 아이디는 못바꿈
						else if(info[1].compareTo("SAVE") == 0) {
							int ck = Integer.parseInt(info[2]);
							if(ck == 1) ck++;

							//바뀐것만 update해주자!
							HashMap<String, String> map = query.bringINFO(ID); 

							if(map.get("NICKNAME").compareTo(info[3 + ck]) != 0) {
								//닉네임 중복체크!!!
								if(query.selectNICKNAME(info[3 + ck]) == 1) {
									query.updateNICKNAME(ID, info[3 + ck]);
									out.println("SETTING`|NN`|OK");				
									}
								else {
									out.println("SETTING`|NN`|NO");	
									continue;
								}
							}
							else out.println("SETTING`|NN`|OK");	
							
							if (map.get("NAME").compareTo(info[4 + ck]) != 0) {
								query.updateNAME(ID, info[4 + ck]);
							}

							if (map.get("PHONE").compareTo(info[5 + ck]) != 0) {
								query.updatePHONE(ID, info[5 + ck]);
							}

							if (map.get("EMAIL").compareTo(info[6 + ck]) != 0) {
								query.updateEMAIL(ID, info[6 + ck]);
							}

							if (map.get("BIRTH").compareTo(info[7 + ck]) != 0) {
								query.updateBIRTH(ID, info[7 + ck]);
							}
							
							//이거 두개는 걍 업데이트 하자 => GITHUB STATE_MESSAGE
							try {
								if(info[8 + ck].compareTo("") == 0) query.updateGITHUB(ID, null);
								else query.updateGITHUB(ID, info[8 + ck]);
							}
							catch(Exception e) {
								query.updateGITHUB(ID, null);
								info[8 + ck] = null;
							}
							
							try {
								if(info[9 + ck].compareTo("") == 0)
									query.updateSTATE_MESSAGE(ID, null);
								else
									query.updateSTATE_MESSAGE(ID, info[9 + ck]);
							}
							catch(Exception e) {
								query.updateSTATE_MESSAGE(ID, null);
								info[9 + ck] = null;
							}

							//비밀번호 업데이트
							if(ck == 2) query.updatePASSWORD(ID, info[3], info[3]);
							
							//모든 입력이 무사히 다 끝나면 그제서야 업데이트!
							out.println("UPDATE`|MYINFO`|" + info[4 + ck] + "`|" + info[3 + ck] + "`|" + info[9 + ck]);
							
							//친구들에게도 바뀐 내 정보를 자랑해야지
							//일단 내 친구 정보 받아오고
							String[][] f_list2 = query.selectFRIEND(ID);
							
							//친구들의 ID가 접속중이라면 보내주세요~
							int plag = 1;
							for(String[] l : f_list2) {
								if(plag == 1) {
									plag++;
									continue;
								}
								if(client.containsKey(l[4])) {
									client.get(l[4]).println("UPDATE`|FINFO`|" + ID + "`|" + info[4 + ck] + "`|" + info[3 + ck] + "`|" + info[9 + ck]);
								}
							}
						}
						
						//내 정보 요청 => SETTING REQ (GUI에 채워넣을 내 정보를 요청하는 것)
						else if(info[1].compareTo("REQ") == 0) {
							//나의 상세 정보를 보내준다.
							HashMap<String, String> map = query.bringINFO(ID);
							
							//정보를 보내준다
							out.println("SETTING`|INFO`|" + map.get("ID") + "`|" + map.get("NICKNAME") + "`|" + map.get("NAME") + "`|" 
										+ map.get("PHONE") + "`|" + map.get("EMAIL")  + "`|" + map.get("BIRTH")  + "`|" + map.get("GITHUB")  
										+ "`|" + map.get("STATE_MESSAGE"));
							//보내는 형식 : FRIEND INFO [ID NICKNAME NAME PHONE EMAIL BIRTH GITHUB STATE_MESSAGE]
							//github과 상메는 없다면 null일 것.			
						}
	
						//회원탈퇴=> SETTING BYE
						else if(info[1].compareTo("BYE") == 0) {
							
							//친구들의 ID가 접속중이라면 탈퇴햇다고 알려주기
							String[][] f_list2 = query.selectFRIEND(ID);
							int plag = 1;
							for(String[] l : f_list2) {
								if(plag == 1) {
									plag++;
									continue;
								}
								if(client.containsKey(l[4])) {
									client.get(l[4]).println("UPDATE`|FBYE`|" + ID );
								}
							}
							query.deleteEVERYWHERE(ID);	
						}
					}
					
					
/**채팅방 관련 (1:1) personal chat ========================================*/
					else if (line.startsWith("PCHAT")) {
						String info[] = line.split("\\`\\|");
						
						//PCHAT`|REQCHAT`|" + FID : 얘랑 채팅하고 싶다고 신호주기
						if(info[1].compareTo("REQCHAT") == 0) {
							//상대방에게 채팅방에 참여할건지 물어봐야함
							HashMap<String, String> map = query.bringINFO(ID);
							//여기서 id는 A. (b가 A의 정보를 받는 상황) (지금 여기는 A고, B에게 보내야 합니다!)
							
							//PCHAT`|QUSCHAT`|" + 채팅요청자ID + 별명 + 이름 : 상대방 알려주면서 채팅할거냐고 물어보기   =>받는쪽 : 이때 별명(이름), ID 저장하기
							client.get(info[2]).println("PCHAT`|QUSCHAT`|" + ID+ "`|" + map.get("NICKNAME")+ "`|" + map.get("NAME"));
						}
						
						//PCHAT`|PESPONCHAT`|" + 채팅요청자ID + Y/N : 채팅할거냐고 물어f을때 채팅 할건지 말건지 답변
						else if(info[1].compareTo("PESPONCHAT") == 0) {
							//PCHAT`|ANSCHAT`|" + 채팅요청자ID + 별명(이름) : 상대방이 채팅 수락했다고 알려주기 + 채팅 잠금 풀림 //보낸쪽 : 이때 별명(이름), ID 저장하기
							HashMap<String, String> map = query.bringINFO(ID);
							//여기서 id는 B (A가 B의 수락 여부와 정보를 받는 상황) => a에게 정보를 전달해야 하는 상황 (지금 여기는 B다)

							if(info[3].equals("Y")) { //채팅을 수락한다면 수락한다고 알려줌
								client.get(info[2]).println("PCHAT`|ANSCHAT`|" + ID +"`|" + map.get("NICKNAME")+ "`|" + map.get("NAME") + "`|" + "Y");
							}
							else { //거절한다면 거절한다고 알림
								client.get(info[2]).println("PCHAT`|ANSCHAT`|" + ID + "`|" + map.get("NICKNAME")+ "`|" + map.get("NAME") + "`|" + "N");
							}
						}
						
						//A가 쓴 채팅을 B에게 보내주는 상황 (지금 여기는 A이고, 나는 b으 ㅣ클라이언트에 바로 채팅 보내기! (서버가 아님))
						//PCHAT`|sendCHAT`|" + 채팅받는자ID + Content : 채팅내용 전송 (내가쓴거임)
						else if(info[1].compareTo("sendCHAT") == 0) {
							//PCHAT`|receivedCHAT`|" + 채팅보낸자ID + Content : 채팅내용 전송 (내가 받은거)
							client.get(info[2]).println("PCHAT`|receivedCHAT`|" + ID +"`|" + info[3]);
						}
						
						//A가 B에게 본인이 나간다고 알려주는 부분
						//PCHAT`|outCHAT`|" + 채팅받는자ID
						else if(info[1].compareTo("outCHAT") == 0) {
							//PCHAT`|outCHAT`|" + 채팅보낸자ID
							client.get(info[2]).println("PCHAT`|OUTCHAT`|" + ID);
						}
					}
				
					
/**채팅방 관련 (멀티) chat multi ========================================*/
					else if (line.startsWith("MCHAT")) {
						while (messageCK.get() == 0) {};
						messageCK.set(0);
						
						String info[] = line.split("\\`\\|");
						
						//"MCHAT`|REQROOM`|" + 방이름 + 내용 보임 여부 +  방만들기 요청자 ID + flist      
						//방만들기 요청
						if(info[1].compareTo("REQROOM") == 0) {
							//방 숫자를 부여받는다
							int rn = forroomnumber;
							forroomnumber++;
							
							//flist를 나눠서 찐으로 리스트 만들기?
							String requset_flist[] = info[5].split("\\^");
														
							messagepool.execute(new Chat(rn, info[2], info[3], info[4], requset_flist)); // socket연결요청이 오면 accept하고 thread를 생성하며 socket을 넘겨줌

							Queue<Message> m = new LinkedList<Message>();
							messageSet.put(rn, m);
							
							//"MCHAT`|RoomNumber`|" + 방번호    //방 번호 보내주기 - 이건 연속된 스텝으로 가야할듯??? 즉, 방이름 저기서 기다려야 하는 부분임.
							out.println("MCHAT`|RoomNumber`|" + rn);
						}

						// "MCHAT`|RESPONCHAT`|" + 방번호+ 내 ID?? + Y // 채팅할거냐고 물어f을때 채팅 할건지 말건지 답변
						else if (info[1].compareTo("RESPONCHAT") == 0) {
							// 이거 받으면 바로 채팅에 참여하겠다는 의미와 같습니다.
							Message m = new Message(Integer.parseInt(info[2]), 0, ID, "0", "0");
							messageSet.get(Integer.parseInt(info[2])).add(m);
						}

						// out.println("MCHAT`|sendCHAT`|"+ Integer.toString(rn) + "`|" + ID + "`|" +
						// getCurrentTime() + "`|" + chat);
						// 메세지받음
						else if (info[1].compareTo("sendCHAT") == 0) {
							Message m = new Message(Integer.parseInt(info[2]), 1, ID, info[4], info[5]);
							messageSet.get(Integer.parseInt(info[2])).add(m);
						}

						//"MCHAT`|OUTCHAT`|" + 방번호 + 나가는ID //채팅에서 나갑니다
						//나간다고 말하기
						else if(info[1].compareTo("OUTCHAT") == 0) {
							//나간다 - rn, 3, 나가는자 ID, 0, 0
							Message m = new Message(Integer.parseInt(info[2]), 3, ID, "0", "0");
							messageSet.get(Integer.parseInt(info[2])).add(m);
						}
						
						//"MCHAT`|REQuLIST`|" + 방번호  //채팅에서 나갑니다
						//나간다고 말하기
						else if(info[1].compareTo("REQuLIST") == 0) {
							Message m = new Message(Integer.parseInt(info[2]), 4, ID, "0", "0");
							messageSet.get(Integer.parseInt(info[2])).add(m);
						}
						
						//"MCHAT`|InviteFriend`|" + 방번호 + 친구 아이디(들)
						else if(info[1].compareTo("InviteFriend") == 0) {
							Message m = new Message(Integer.parseInt(info[2]), 5, ID, info[3], "0");
							messageSet.get(Integer.parseInt(info[2])).add(m);
						}
						messageCK.set(1);
					}
					
/**file 전송 관련 (A - sender, B - receiver) ========================================*/
					else if (line.startsWith("FILES")) {
						String info[] = line.split("\\`\\|");
						// >> 여기서는 파일을 주고받을지 결정하는 연락들이 오고가고, 파일을 주고 받는건 새로운 thread에서 새로 socket을 열어서 진행
						
						//A가 B에게 파일을 보내고 싶다고 연락이 왔어요 => FILES ASK 상대ID
						if(info[1].compareTo("ASK") == 0) {
							//상대를 찾아서 보내는 API에 맞춰서 보내줌 (FILES ASK A아이디 이름(별명)
							HashMap<String, String> map = query.bringINFO(ID);
							String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";
							client.get(info[2]).println("FILES`|ASK`|" + ID + "`|" + senderInfo);
						}
												
						//B에게 A가 보내는 파일을 받을지 말지 여부를 결정하는 연락이 왔어요 => FILES ANS 상대ID, Y/N
						else if(info[1].compareTo("ANS") == 0) {
							
							//파일 전송을 받는다고 한다면?
							if(info[3].equals("Y")) {
								//둘 사이를 이어줄 thread를 만들어 줍니다.
								filepool.execute(new filemanage(fileportnum)); // socket연결요청이 오면 accept하고 thread를 생성하며 socket을 넘겨줌

								//보내는 사람이 받는 사람보다 1 더 큰 portnum을 가진다.
								out.println("FILES`|PNUM`|" + fileportnum );
								fileportnum+=1;
								client.get(info[2]).println("FILES`|ANS`|" + ID + "`|" + "Y" + "`|" + fileportnum);
								fileportnum+=1;
							}
							else { //안받는다고 하면? => A에게 안줘도 된다고 알리기
								client.get(info[2]).println("FILES`|ANS`|" + ID + "`|" + "N" );
							}
						}						
					}
					
/**TTT 관련  ========================================*/
					else if (line.startsWith("TTT")) {
						String info[] = line.split("\\`\\|");
						
						while (TTTCK.get() == 0) {};
						TTTCK.set(0);
						
						//A가 B에게 대결을 신청 (TTT ASK 상대ID)
						if(info[1].compareTo("ASK") == 0) {
							// 상대를 찾아서 보내는 API에 맞춰서 보내줌 (TTT ASK A아이디 이름(별명)
							HashMap<String, String> map = query.bringINFO(ID);
							String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";
							client.get(info[2]).println("TTT`|ASK`|" + ID + "`|" + senderInfo);
						}
												
						//B에게 A가 보내는 파일을 받을지 말지 여부를 결정하는 연락이 왔어요 => FILES ANS 상대ID, Y/N
						else if(info[1].compareTo("ANS") == 0) {
							//게임을 한다고 하면?
							if(info[3].equals("Y")) {
								int rn = TTTnumber++;
								
								//선 정하기
								double dValue = Math.random();
								int order = ((int) (dValue * 10))%2;
								int Aorder = 0;
								int Border = 0;
								
								//0이면 A선, 1이면 B가 선.
								if(order == 0) Aorder = 1;
								else Border = 1;
								
								//TTT게임을 수행할 thread를 만들어 줍니다.
								TTTpool.execute(new TTT(rn, info[2] ,ID)); //게임을 건 상대가 A
								Queue<TMessage> m = new LinkedList<TMessage>();
								TTTSet.put(rn, m);
								
								HashMap<String, String> map = query.bringINFO(info[2]);
								String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";
								
								HashMap<String, String> map2 = query.bringINFO(ID);
								String senderInfo2 = map2.get("NICKNAME") + "(" + map2.get("NAME") + ")";

								//받는 쪽으로 다시 정보를 보내준다 (TTT INFO MNN FNN ROOMNUMBER ORDER) => 이거 받고 GUI구축
								out.println("TTT`|INFO`|" + senderInfo2 + "`|" + senderInfo + "`|" + rn + "`|" + Border);

								//게임 건 사람에게도 정보를 보내줌 (TTT INFO MNN FNN ROOMNUMBER ORDER) => 이거 받고 GUI 구축
								client.get(info[2]).println("TTT`|INFO`|" + senderInfo + "`|" + senderInfo2 + "`|" + rn + "`|" + Aorder);
							}
							else { //안받는다고 하면? => A에게 거절했다고 알리기
								client.get(info[2]).println("TTT`|ANS`|" + ID + "`|" + "N" );
							}
						}		
						
						//게임 도중 주고받는 정보들 (TTT ING RoonNumber X Y)
						else if(info[1].compareTo("ING") == 0) {
							// Tmessage queue에 넣어준다. => 그럼 그 thread에서 상대에게 내 정보를 알려주던가 할 것.
							TMessage m = new TMessage(Integer.parseInt(info[3]), Integer.parseInt(info[4]), ID);
							TTTSet.get(Integer.parseInt(info[2])).add(m);
						}
						TTTCK.set(1);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// Client가 종료하면, 흔적들을 다 정리해준다.

				if(ID != null) {
					// 로그인 된 상태라면
					//마지막 접속시간도 업데이트되어야함
					query.updateLAST_CONNECTION(ID, getCurrentTime());
					
					//친구들에게도 나 종료한다고 동네방네 소문내기
					for (PrintWriter output : client.values()) {
						if(output.equals(out)) continue;
						//모두에게 보내면 client에서 알아서 걸러서 들을것임
						//형식 ; UPDATE F_state F_ID 상태(1이면 오프라인)
						output.println("UPDATE`|F_state`|" + ID + "`|" + 1);
					}
					// 채팅방에서도 다 나가져야한다!!! => client에서 처리
					//client에서 빠짐
					client.remove(ID);
				}
				//비로그인 상태에는 남는게 없어서 걍 ㄹㅇ이소켓만 끝내면 됨
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	/** file 전송 thread 코드=====================================================
	 * : file을 전송할 때, 두 client와 새로운 socket을 열어서 기존 Socket과는 파일을 독립적으로 전송한다.
	 * */
	public static class filemanage implements Runnable{
		private ServerSocket soc;
		private ServerSocket soc1;
		static Socket sender = new Socket(); 
		static Socket receiver = new Socket(); 

		public filemanage (int pnum) throws IOException {
	    	soc = new ServerSocket(pnum);  //받는 사람 소켓
	    	soc1 = new ServerSocket(pnum + 1);  //보내는 사람 소켓.
		}
		
		@SuppressWarnings("resource")
		@Override
		public void run() {

			try {
				sender = soc1.accept();
				receiver = soc.accept();

				// 보내는 사람으로부터 파일을 받기!
				InputStream in = null; // A로 부터 읽어오기위함
				FileOutputStream out = null; // 서버에서의 파일생성을 위해 생성
				in = sender.getInputStream(); // 클라이언트로 부터 바이트 단위로 입력을 받는 InputStream을 얻어와 개통합니다.
				DataInputStream din = new DataInputStream(in); // InputStream을 이용해 데이터 단위로 입력을 받는 DataInputStream 개통.
			
				
				/* sender -> receiver*/
				int data = din.readInt(); // (Int형 데이터)받을 파일의 byte 읽어오기
				String filename = din.readUTF(); // String형 데이터를 전송받아 filename(파일의 이름으로 쓰일)에 저장합니다.
				String[] flist = filename.split("\\\\");
				filename = flist[flist.length-1];

				File file = new File(filename); // 입력받은 File의 이름으로 복사하여 생성합니다.

				out = new FileOutputStream(file); // 생성한 파일을 클라이언트로부터 전송받아 완성시키는 FileOutputStream을 개통합니다.
				byte[] buffer = new byte[1024]; // 바이트단위로 임시저장하는 버퍼를 생성합니다.

				int len; // 전송할 데이터의 길이를 측정하는 변수입니다.
				for (; data > 0; data--) { // 전송받은 data의 횟수만큼 전송받아서 FileOutputStream을 이용하여 File을 완성시킵니다.
					len = in.read(buffer);
					out.write(buffer, 0, len);
				}
				System.out.println("파일 받기 완료");

				/* server -> receiver */
				FileInputStream fin = new FileInputStream(new File(filename)); // FileInputStream - 파일에서 입력받는 스트림
				OutputStream outt = receiver.getOutputStream(); // 클라이언트에게 보내기 위함
				DataOutputStream dout = new DataOutputStream(outt); // OutputStream을 이용해 데이터 단위로 보내는 스트림을 개통합니다
				buffer = new byte[1024]; //임시저장 버퍼
				len = 0; //길이
				data = 0; // 전송횟수
				
				// FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
				while ((len = fin.read(buffer)) > 0) {
					data++;
				}
				fin.close();

				fin = new FileInputStream(filename); // FileInputStream이 만료되었으니 새롭게 개통합니다.

				dout.writeInt(data); // 데이터 전송횟수를 서버에 전송하고,
				dout.writeUTF(filename); // 파일의 이름을 서버에 전송합니다.

				len = 0;
				for (; data > 0; data--) { // 데이터를 읽어올 횟수만큼 FileInputStream에서 파일의 내용을 읽어옵니다.
					len = fin.read(buffer); // FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
					outt.write(buffer, 0, len); // 서버에게 파일의 정보(1kbyte만큼보내고, 그 길이를 보냅니다.
				}
				System.out.println("파일 보내기 완료");

				out.close(); // client에게 보낸 후 파일을 지우기 위해서 필수!!
				fin.close(); // client에게 보낸 후 파일을 지우기 위해서 필수!!

				if (file.exists()) { // 보낸 파일 삭제
					if (file.delete()) {
						System.out.println("파일삭제 성공");
					} else {
						System.out.println("파일삭제 실패");
					}
				} else {
					System.out.println("파일이 존재하지 않습니다.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} //다 끝나면 종료
		}
	}
	
	/** multi-Chat thread 코드=====================================================
	 * 여러 client끼리 단체 채팅을 할 때, 그 채팅의 모든것을 담당한다.
	 * */
	public static class Chat implements Runnable {
		int room_num;
		String room_name;
		private String type; //이전내용 보여줄지 말지 => N면 안보여주고 Y면 보여줌
		private int participants_num = 0; //방장 포함
		private HashSet<String> participants_list = new HashSet<String>(); //list를 모아둔 것
		
		public Chat(int room_num, String room_name, String type, String requester_ID, String[] flist) {
			this.room_num = room_num;
			this.room_name = room_name;
			this.type = type; // 1이 들어오면 전에꺼 다보여줘야함.

			HashMap<String, String> map = query.bringINFO(requester_ID);
			String makerInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";

			// flist돌면서 사람들에게 채팅 초대 메세지 보내기
			for (String id : flist) {
				// "MCHAT`|INVCHAT`|" + 방번호 + 방이름 + 초대자 이름 //상대방에게 님 초대당햇다고 알려주기
				if (client.containsKey(id))
					client.get(id).println("MCHAT`|INVCHAT`|" + room_num + "`|" + room_name + "`|" + makerInfo);
			}
			participants_list.add(requester_ID);
			participants_num++;
		}

		@Override
		public void run() {
			boolean flag = true;
			
			while (flag) {			
				if (!messageSet.get(room_num).isEmpty()){
					Message m = messageSet.get(room_num).poll();
					
					if (m.equals(null))
						continue;

					int k = m.getType();

					if (k == 0) {
						// 참여한다 - rn, 0, 참여자 ID, 0, 0
						// 다른 참여자들에게 이사람이 들어왔다고 알려주는 부분 + 들어온 사람에게는 Type체크해서 
						// "MCHAT`|ANSCHAT`|" + 방번호 + 상대ID
						HashMap<String, String> map = query.bringINFO(m.getSender_id());
						String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";

						for (String id : participants_list) {
							client.get(id).println("MCHAT`|ANSCHAT`|" + room_num + "`|" + senderInfo);
						}

						if(type.equals("1")) { //1이라고 하면 들어오기 전의 메세지를 다 보내준다
							// return array[][time, sender, content]
							String[][] chatlist = query.bringCHATTING(Integer.toString(room_num));
							
							int cnum = Integer.parseInt(chatlist[0][0]);
							String messagelist = "";
							
							for(int i=1;i<=cnum;i++) {
								HashMap<String, String>  map2 = query.bringINFO(chatlist[i][1]);
								String senderInfo2 = map2.get("NICKNAME") + "(" + map2.get("NAME") + ")";
								client.get(m.getSender_id()).println("=====>" + chatlist[i][2]);

								messagelist =  messagelist + "`|" + senderInfo2 + "^" + chatlist[i][2];
							}
							//이전 챗내용 보내주기
							client.get(m.getSender_id()).println("MCHAT`|PRECHAT`|" + room_num +  "`|" + cnum + messagelist);
						}
						participants_list.add(m.getSender_id());
						participants_num++;
					}

					else if (k == 1) {// 메세지보내기 - rn, 1, sender ID, time, message
						// "MCHAT`|receivedCHAT`|" + 방번호 + 채팅보낸자ID + 별명(name) + content //채팅내용 전송 (뿌리기)
						HashMap<String, String> map = query.bringINFO(m.getSender_id());
						String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";

						for (String id : participants_list) {
							client.get(id).println("MCHAT`|receivedCHAT`|" + room_num + "`|" + m.getSender_id() + "`|"
									+ senderInfo + "`|" + m.getMessage());
						}
						query.insertCHATTING(Integer.toString(room_num), m.getTime(), m.getSender_id(), m.getMessage());
					}

					else if (k == 2) { // 초대한다- rn, 2, 초대자 ID, 초대받는자 ID, 0
						client.get(m.getTime())
								.println("MCHAT`|INVCHAT`|" + room_num + "`|" + room_name + "`|" + m.getSender_id());
					}

					else if (k == 3) { // 나간다 - rn, 3, 나가는자 ID, 0, 0
						participants_list.remove(m.getSender_id());
						participants_num--;
						// "MCHAT`|outCHAT`|" + 방번호 + 나가는ID + 별명(name) //채팅에서 나갑니다 (뿌리기)
						HashMap<String, String> map = query.bringINFO(m.getSender_id());
						String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";

						for (String id : participants_list) {
							client.get(id).println(
									"MCHAT`|outCHAT`|" + room_num + "`|" + m.getSender_id() + "`|" + senderInfo);
						}
					}

					else if (k == 4) {
						// "MCHAT`|ulist`|" + 방번호 + 리스트 채울 수 있는 정보 //접속중인 유저리스트를 요청자에게 전송
						String userlist = null; // 이거 채워주기!!!!
						int a = 0;

						for (String id : participants_list) {
							HashMap<String, String> map = query.bringINFO(id);
							String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";

							if (a == 0) {
								a++;
								userlist = senderInfo;
							} else {
								userlist = userlist + "^" + senderInfo;
							}
						}
						client.get(m.getSender_id()).println("MCHAT`|ulist`|" + room_num + "`|" + userlist);
					}
					
					else if (k == 5) { //친구에게 초대하기
						String ulist[] = m.getTime().split("\\^");
						
						HashMap<String, String> map = query.bringINFO(m.getSender_id());
						String senderInfo = map.get("NICKNAME") + "(" + map.get("NAME") + ")";
						
						//flist돌면서 사람들에게 채팅 초대 메세지 보내기
						for(String id : ulist) {
							//"MCHAT`|INVCHAT`|" + 방번호 + 방이름 + 초대자 이름    //상대방에게 님 초대당햇다고 알려주기
							if(client.containsKey(id) && !participants_list.contains(id))
								client.get(id).println("MCHAT`|INVCHAT`|" + room_num + "`|" + room_name + "`|" + senderInfo);
						}
					}
					messageCK.set(1);
				} else {
					System.out.println("+");
				}

				// 인원수가 0명이 되면 챗이 종료됨 => 이것도 사라짐
				if (participants_num < 1) {
					flag = false;
					messageSet.remove(room_num);
				} else {
					System.out.println(".");
				}
			}
			//스레드 종료하기전에 chat내용 전체 삭제, room에서 본인 삭제 해야함	
			query.deleteCHATTING(Integer.toString(room_num));
		}
	}

	/** 틱택톡 thread 코드=====================================================
	 * 두 client끼리 틱택톡 게임을 할 때, 게임의 모든것을 담당!
	 * */
	public static class TTT implements Runnable{
		private int room_num;
		private String AID;
		private String BID;
		private int gameboard[][]= {{0,0,0},{0,0,0},{0,0,0}};
		private int count=0;
		
		public TTT(int room_num, String A, String B) {
			//order가 1이면 A가 선, 0이면 B가 선.
			this.room_num = room_num;
			this.AID = A; //O역할
			this.BID = B; //X역할
		}

		//승패가 났는지 확인하는 코드. (누가 승자인지 확인하는 코드 (1이면 A, 2이면 B)), -1이면 무승부, 0이면 더 할수 있다는 뜻 
		public int checkIfWinner() {
			for(int i=0;i<gameboard.length;i++) {
				if (((gameboard[i][0]==1)||(gameboard[i][0]==2)) && (gameboard[i][0] == gameboard[i][1]) && (gameboard[i][0] == gameboard[i][2])) {
					if(gameboard[i][0]==1) return 1;
					else return 2;
				}
				else if (((gameboard[0][i]==1)||(gameboard[0][i]==2)) && (gameboard[0][i] == gameboard[1][i]) && (gameboard[0][i] == gameboard[2][i])) {
					if(gameboard[0][i]==1) return 1;
					else return 2;
				}			
			}
			
			if (((gameboard[0][0]==1)||(gameboard[0][0]==2)) && (gameboard[0][0] == gameboard[1][1]) && (gameboard[0][0] == gameboard[2][2])) {
				if(gameboard[0][0]==1) return 1;
				else return 2;
			}	
			
			else if (((gameboard[0][2]==1)||(gameboard[0][2]==2)) && (gameboard[0][2] == gameboard[1][1]) && (gameboard[0][2] == gameboard[2][0])) {
				if(gameboard[0][2]==1) return 1;
				else return 2;
			}	
			
			if(count==9) {//무승부1
				return -1;
			}
			return 0;
		}
		
		@Override
		public void run() {
			boolean flag = true;

			while (flag) {
				if (!TTTSet.get(room_num).isEmpty()){
					TMessage m = TTTSet.get(room_num).poll();
					
					if (m.equals(null))
						continue;
					
					int ck;
					if(m.getSender_id().equals(AID)) ck = 1;
					else ck = 2;
					
					gameboard[m.getx()][m.gety()] = ck;
					client.get(BID).println(gameboard[0][0]+ "|" + gameboard[0][1]+ "|" + gameboard[0][2]+ "\n" + gameboard[1][0] + "|" +  gameboard[1][1]+ "|" +  gameboard[1][2]
							+ "\n" + gameboard[2][0]+ "|" + gameboard[2][1] + "|" + gameboard[2][2]);
					client.get(BID).println(m.getx()+ "|" +m.gety());
					
					//상대가 어디에 수를 뒀는지 알리기
					if(m.getSender_id().equals(AID)) 
						client.get(BID).println("TTT`|NOTI`|" + room_num + "`|" + m.getx() + "`|" + m.gety());
					else
						client.get(AID).println("TTT`|NOTI`|" + room_num + "`|" + m.getx() + "`|" + m.gety());
					
					int ckwinner = checkIfWinner();
					
					if(ckwinner == 1) { //승자가 A라면~
						client.get(AID).println("TTT`|RESULT`|" + room_num + "`|" + "WIN");
						client.get(BID).println("TTT`|RESULT`|" + room_num + "`|" + "LOSE");
						flag = false;
						
					} else if (ckwinner == 2) { // 승자가 B라면~~
						client.get(AID).println("TTT`|RESULT`|" + room_num + "`|" + "LOSE");
						client.get(BID).println("TTT`|RESULT`|" + room_num + "`|" + "WIN");
						flag = false;

					} else if (checkIfWinner() == -1) {// 무승부!
						client.get(AID).println("TTT`|RESULT`|" + room_num + "`|" + "SAME");
						client.get(BID).println("TTT`|RESULT`|" + room_num + "`|" + "SAME");

						flag = false;
					}
					
				} else {
					System.out.println("+");
				}
			}
			TTTSet.remove(room_num);
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++) {
					System.out.print(gameboard[i][j]);
				}
				System.out.println("");
			}
		}
	}
	
	/** 친구신청 감시 thread 코드=====================================================
	 * 하나의 handler thread가 실행될 , 같이 붙어서 실행되며 실시간으로 친구신청을 감지한다.
	 * */
	public static class RealTimeUpdater implements Runnable {
		// 사용자 정보 저장
		private String ID = null;
		private PrintWriter out;

		public RealTimeUpdater(String id, PrintWriter out) throws IOException {
			ID = id;
			this.out = out;
		}

		@Override
		public void run() {
			System.out.println("realtime!");

			while (client.containsKey(ID)) {
				if (query.checkPLUS(ID) == 1) { // 만약 친구신청 리스트에 내가 있다면?

					String[][] FriendPlusList = query.bringFRIEND_PLUS(ID);

					//return String[][name, nickname, last_connection, 상메 ,id]
					for (String[] list : FriendPlusList) {
						try {
							if (list[0].compareTo("null") == 0)
								continue;
						} catch (Exception e) {
							break;
						}

						out.println("UPDATE`|FRIREQ`|" + list[1] + "`|" + list[0] + "`|" + list[4]);
						// client에서 응답해서 무언가 바뀔때까지 기다림
						while (query.checkFRIEND_PLUS(list[4], ID).compareTo("false") != 0) {
							;
						}
						System.out.println("change!");

						// 바뀌어서 DB에 적용되면 그제서야 다음으로 넘어갑니다
					}
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}