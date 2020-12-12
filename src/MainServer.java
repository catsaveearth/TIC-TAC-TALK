/**
MainServer.java

채팅 프로그램의 main server.
room manage를 돌리면서 채팅방을 관리한다. => 클라이언트 -> 서버로 채팅 연락이 올 시, 방에 있는 사람들의 정보를 넘겨주어야 한다.

client로부터 accept연결 요청을 받으면 client를 다루는 thread를 생성해서 socket을 넘겨준다. (그 thread는 EchoServer에 존재)


modifier: Kim Su hyeon.
E-mail Address: tpfbdpf@naver.com
Last Changed: Nov 13, 2020.
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import Variable.RequestRoom;
import client.Client.input;
import Variable.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;


public class MainServer {

	// 접속중인 client의 정보를 관리한다. - 로그인에 성공해야 여기에 들어올 수 있음
	public static HashMap<String, PrintWriter> client = new HashMap<>();

	// client thread와 room manage thread를 이어줄 친구들
	public static Queue<RequestRoom> createRoomQueue = new LinkedList();
	public static Queue<Message> messageSet = new LinkedList(); //메세지의 경우 여러 쓰레드들이 동시에 접근이 가능해야함 -> hashMap! (room number가 밖에서 체크 가능하도록)
	
	final private static int portnum = 6789;
		
	public static String getCurrentTime() {
		Date date_now = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
		
		//HHmmss
		SimpleDateFormat date_format = new SimpleDateFormat("yyMMddHHmmssSS");

		return date_format.format(date_now).toString();
	}

	public static void main(String[] args) throws Exception {
		// client와 소통하는 thread를 관리할 pool생성 (최대 500명까지 가능)
		ExecutorService pool = Executors.newFixedThreadPool(500);
		ExecutorService chatpool = Executors.newFixedThreadPool(500);

		
		// chatroom을 관리하는 roomManage 실행
		pool.execute(new RoomManage());
		
		System.out.println("start server!");
		try (ServerSocket listener = new ServerSocket(portnum)) { // listener socket생성
			while (true) {
				pool.execute(new Handler(listener.accept())); // socket연결요청이 오면 accept하고 thread를 생성하며 socket을 넘겨줌
			}
		}
	}

	
	
	/** client thread 코드. - 이제 client와 관련된 일은 모두 여기서 처리한다. */
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

//메인 화면 뜰 떄 까지 돌아가는 것				
				while (true) { // 처음에 로그인 과정 + 회원가입 => 로그인을 해야지 while을 넘어간다
					String line = in.nextLine();
					System.out.println(line);

					// 로그인 : LOGIN ID PASSWORD
					if(line.startsWith("REQSALT")) {
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

					// 회원가입 : 
					else if (line.startsWith("REGISTER")) {
						String info[] = line.split("\\`\\|");
						
						for(String t : info) {
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

						if(info[1].compareTo("1") == 0) {
							temp.put("GITHUB", info[10]);
						}
						
						
						//일단 id, nickname중복 체크를 한다.
						//만약 중복체크를 하는데 겹친다면, 어디가 안되는지 리턴
						if(query.selectID(info[3]) == -1) {
							out.println("REGISTER`|ID");
						}
						else if(query.selectNICKNAME(info[4]) == -1) {
							out.println("REGISTER`|NN");
						}
						else {
							query.insertUSER(temp);
							out.println("REGISTER`|OK");
						}
						query.updateLAST_CONNECTION(info[3], getCurrentTime()); //지금 시간 넣어주기
					}
				}

							
			    HashMap<String,String> binfo = new HashMap<String,String>();
			    binfo = query.selectNAME_NICKNAME_STATE(ID);
				
				// 기본 정보들을 client에게 전부 보내준다 JDBC를 이용해야함
				out.println("BASICINFO`|name`|" + binfo.get("NAME"));
				out.println("BASICINFO`|nickname`|" + binfo.get("NICKNAME"));
				out.println("BASICINFO`|state_message`|" + binfo.get("STATE_MESSAGE"));
				
				//접속상태 업데이트!!! -> 지금 접속중이라는 뜻
				query.updateLAST_CONNECTION(ID, "0");
								
				//친구목록(친구가 접속중인지 아닌지도)
				//친구목록 받아오기
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

				
				//다른 사람들에게 내가 들어왔다고 알린다!
				for (PrintWriter output : client.values()) {
					if(output.equals(out)) continue;
					//모두에게 보내면 client에서 알아서 걸러서 들을것임
					//형식 ; UPDATE F_state F_ID 상태(1이면 오프라인)
					output.println("UPDATE`|F_state`|" + ID + "`|" + 0);
				}
				
				
				//===============================>> 여기 위까지 구현 완료 (로그인 & 회원가입 & 기본 정보 보내주기) <<==================================

				//이제 친구신청을 감지하는 Thread와 아래의 while이 동시에 돌아가게 됩니다
				RealTimeUpdater runnable = new RealTimeUpdater(ID, out);
				thread = new Thread(runnable);

				thread.start();
				//친구신청 확인 thread돌아갑니다~
				
				System.out.println("돌기시작합니다!");

//프로그램이 돌아가는 동안 소통이 이루어지는 부분 (클라이언트로부터 입력을 받는다! / client -> server)
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

							if(ck.compareTo("true") == 0) {
								out.println("FRIEND`|PCK`|T");
							}
							else out.println("FRIEND`|PCK`|F");
						}
						
						//친구 테이블에 있는지 확인 => FRIEND FCK [FID]
						else if(info[1].compareTo("FCK") == 0) {

							String ck = query.checkFRIEND(ID, info[2]);
							System.out.println(ck);

							if(ck.compareTo("true") == 0) {
								out.println("FRIEND`|FCK`|T");
							}
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
							//	// return String[][id, nickname, name, last_connection]
							
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
							if(ck == 1) {
								out.println("SETTING`|PWCK`|OK");
							}
							else {
								out.println("SETTING`|PWCK`|NO");
							}

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
							else {
								out.println("SETTING`|NN`|OK");									
							}
							
							
							if(map.get("NAME").compareTo(info[4 + ck]) != 0) {
								query.updateNAME(ID, info[4 + ck]);
							}
							
							if(map.get("PHONE").compareTo(info[5 + ck]) != 0) {
								query.updatePHONE(ID, info[5 + ck]);
							}
							
							if(map.get("EMAIL").compareTo(info[6 + ck]) != 0) {
								query.updateEMAIL(ID, info[6 + ck]);
							}
							
							if(map.get("BIRTH").compareTo(info[7 + ck]) != 0) {
								query.updateBIRTH(ID, info[7 + ck]);
							}
							
							//이거 두개는 걍 업데이트 하자 => GITHUB STATE_MESSAGE
							try {
								if(info[8 + ck].compareTo("") == 0)
									query.updateGITHUB(ID, null);
								else
									query.updateGITHUB(ID, info[8 + ck]);
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
						
						
						//PCHAT`|PESPONCHAT`|" + 채팅요청자ID + Y/N : 채팅할거냐고 물어봣을때 채팅 할건지 말건지 답변
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
						String info[] = line.split("\\`\\|");
						
						//1:1 채팅 신청 => CHATM APP [thread 식별번호] [초대인원 수] [상대ID]
						if(info[1].compareTo("APP") == 0) {
							//방을 요청했다! <--- 여기서는 이거까지

							//새 방을 요청하는 형식을 짜서
							RequestRoom r = new RequestRoom(ID, 1, Integer.parseInt(info[1]), info[2], info);

							//방만드는 대기 queue에 넣어준다
							createRoomQueue.add(r);
						}
					}
					
/**채팅 수락 여부 ========================================*/
					else if (line.startsWith("CHAT")) { 
						String info[] = line.split("\\`\\|");
						
						//1:1 채팅 수락 => CHAT OK [roomID]
						if(info[1].compareTo("OK") == 0) {
							//나에게 들어온 요청에 대해 채팅방에 들어가는 것을 수락햇다.
							//이런거 의사를 표현하는 queue를 하나 더 둘까????????????????????????????????????????
						}
						
						
						//1:1 채팅 거절 => CHAT NO [roomID]
						else if(info[1].compareTo("NO") == 0) {
							//나에게 들어온 요청에 대해 채팅방에 들어가는 것을 거절했다.
							//이런거 의사를 표현하는 queue를 하나 더 둘까????????????????????????????????????????
						}
					}
					
/**채팅 ========================================*/
					else if (line.startsWith("CHAT")) { //CHAT room_id sender_id time message 순으로 => message가 맨 뒤로 가야함!!
						String info[] = line.split("\\`\\|");
						
						int room_id = Integer.parseInt(info[1]);
						Message m = new Message(room_id, info[2], info[3], line.substring(line.indexOf(info[4])));
						//룸 아이디, 보낸이, 시간, 내용
						
						messageSet.add(m);
						//이렇게 추가하면 이제 chat thread에서 처리할 것임
					}
									
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
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
					
					
					// 채팅방에서도 다 나가져야한다!!!

					//친신받는것도 꺼줌
					
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

	
	// roomManage thread 코드
	public static class RoomManage implements Runnable {

		ExecutorService chat_pool = Executors.newFixedThreadPool(500);

		@Override
		public void run() {
			
			Random random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
	        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음
			
			while(true) {
				
				//방 만들어달라는 요청이 잇다면
				if(!createRoomQueue.isEmpty()) {
					//요청 꺼내오기
					RequestRoom temp = createRoomQueue.poll();
					
					//방번호 랜덤으로 생성하기
					
					
					
					
					
					int num = random.nextInt(10000);
					//데이터베이스 room_num에 혹시 이미 있는수인지 체크, 만약 있다면 다시 난수 생성
					
					
					
					//새로운 채팅에 대한 스레드 작동!
					//이 chat pool은 client에 직접 메세지를 보내주게 되는데, 위의 client hashmap을 이용하게 됨.
					chat_pool.execute(new Chat(num, temp.getRequester_ID(), temp.getType(), temp.getParticipants_num(), temp.getParticipants_list()));
				}
					
				
				
				
			}
		}
	}
	
	
	// 채팅방 thread 코드
	public static class Chat implements Runnable {
		int room_num;
		private String requester_ID;
		private int type; //0이면 개인, 1이면 단체
		private int participants_num; //방장 포함
		private ArrayList<String> participants_list = new ArrayList<String>();

		
		public Chat(int room_num, String requester_ID, int type, int participants_num, ArrayList<String> participants_list) {
			this.room_num = room_num;
			this.requester_ID = requester_ID;
			this.type = type;
			this.participants_num = participants_num;
			this.participants_list = participants_list;
			
			//그리고 DB에 추가하는 연산! => chatTable에 추가
		}

		//먼저 방이 만들어진다!
		/**이 thread가 해야할 행동들을 적어보자
		 * 
		 * 먼저 만들어진다면 방에 초대하는 행동을 취해야 한다.
		 * (예외처리는 나중에 생각해 제발 일단 기능부터 만들어!!!!!)
		 * 
		 * - 일댈인경우
		 * 
		 * 1. 먼저 사용자에게 방이 만들어졌다고 알림 => 그럼 일단 client측에서는 채팅방을 하나 띄우고, 채팅 입력은 못하는 상태로 만들거임.
		 * 2. 다른 상대에게도 채팅 요청이 왔다고 알림 => 상대에게 보내면 그 client측에서는 팝업을 띄우고 수락하시겠습니까? 가 뜸
		 * 3. 일단 그 다른 상대에게서 응답이 올 것임 (Y/N)
		 * 
		 * 4-1. 상대가 거절한다? -> 방만든 사람의 client로 거절의 메세지를 보냄 : 그럼 방만든 사람의 client는 상대방이 채팅을 거절했습니다! 메세지 뜨고 채팅방 닫기
		 *  				=> 그리고 DB에서 이 채팅방을 삭제하고, thread도 종료된다.
		 * 
		 * 4-2. 상대가 수락한다? -> 방만든 사람의 client로 수락의 메세지를 보냄 : 그럼 방만든 사람의 client의 채팅방은 활성화된다
		 * 					 -> 수락한 상대쪽에서도 수락한 순간 채팅방이 뜨면서 활성화 되어야 함. (이건 그쪽 클라이언트에서 할 일)
		 * 
		 * (상대가 수락한다는 가정 하에)
		 * 5. while을 돌면서 client로부터 입력이 있는지 확인함. => peek을 통해 queue맨 위를 보면서 내 메세진지 아닌지 확인 => queue니까 시간순서대로 들어올수밖에 없다!
		 * 만약 우리 방의 메세지다?
		 * 
		 * 메세지를 DB에 저장하고, 방에 있는 모든 사람들에게 message를 뿌린다. (그래봣자 1댈은 두명밖에 없지만...)
		 * 
		 * 
		 * 지금 해둔 가정이 단순히 창을 닫은건 나가진게 아니고, 채팅방 내에서 나가기 버튼을 누르거나 로그아웃을 해야 완전히 꺼진것으로 간주한다!!!
		 * 이걸 어덯게 판단하지? => 아 이거 너무 어려워ㅠㅠㅠ => 건의해보자...어케할건지...ㅠㅠㅠㅠ
		 * 
		 *  
		 * 6. 이를 반복하다가 누구 한명이 종료한다면? -> ~~님이 나가셨습니다. 채팅이 종료되었습니다. 
		 * 									=> 그리고 남은 상대방 측의 text area의 활성화가 채팅을 못치게 됨. 
		 * 
		 *
		 *
		 * - 멀티인경우
		 * 
		 * 1. 먼저 사용자에게 방이 만들어졌다고 알림 => 그럼 일단 client측에서는 채팅방을 하나 띄움
		 * 2. 다른 상대에게도 채팅 요청이 왔다고 알림 => 상대에게 보내면 그 client측에서는 팝업을 띄우고 수락하시겠습니까? 가 뜸
		 * 3. 일단 그 다른 상대에게서 응답이 올 것임 (Y/N)
		 * 
		 * 4-1. 상대가 거절한다? -> 그냥 그사람은 안들어오게 되는 것. 목록에서 삭제한다.(DB에서나...) 아 이거솓 꼬인다...
		 * 
		 * 4-2. 상대가 수락한다? -> 수락한 상대 client에 채팅방이 뜸 + 다른 상대들에게 누구님이 들어왔습니다~ 뜨게하기.
		 * 
		 * (상대가 수락한다는 가정 하에)
		 * 5. while을 돌면서 client로부터 입력이 있는지 확인함. => peek을 통해 queue맨 위를 보면서 내 메세진지 아닌지 확인 => queue니까 시간순서대로 들어올수밖에 없다!
		 * 만약 우리 방의 메세지다?
		 * 메세지를 DB에 저장하고, 방에 있는 모든 사람들에게 message를 뿌린다. (그래봣자 1댈은 두명밖에 없지만...)
		 * 
		 * 
		 * 지금 해둔 가정이 단순히 창을 닫은건 나가진게 아니고, 채팅방 내에서 나가기 버튼을 누르거나 로그아웃을 해야 완전히 꺼진것으로 간주한다!!!
		 * 이걸 어덯게 판단하지? => 아 이거 너무 어려워ㅠㅠㅠ => 건의해보자...어케할건지...ㅠㅠㅠㅠ
		 * 
		 * 
		 * 
		 * 6. 이를 반복하다가 누군가에게서 나간다는 신호가 왔다면? => 상대가 나갓다고 다른 사용자들에게 알린다.
		 * 
		 * 
		 * 
		 * 
		 * 
		 * */
		
		
		@Override
		public void run() {
			
			while(true) {				
				//메세지셋에 메세지가 들어있는데
				if(!messageSet.isEmpty()) {
					if(messageSet.peek().getRoom_id() == room_num) {
						//그 메세지가 우리거넹?
						
						
					}
				}
			}

			
			
			
			//인원수가 1명이 되면 챗이 종료됨
			//스레드 종료하기전에 chat내용 전체 삭제, room에서 본인 삭제 해야함
			
			
		}

	}

	
	
	// 실시간으로 친구 신청 감시하는 얘
	public static class RealTimeUpdater implements Runnable {
		// 사용자 정보 저장
		private String ID = null;
		private PrintWriter out;

		// constructor -> stream 연결작업
		public RealTimeUpdater(String id, PrintWriter out) throws IOException {
			ID = id;
			this.out = out;
		}


		@Override
		public void run() {
			System.out.println("realtime!");

			while (client.containsKey(ID)) {
				System.out.println("rego");

				if (query.checkPLUS(ID) == 1) { // 만약 친구신청 리스트에 내가 있다면?

					String[][] FriendPlusList = query.bringFRIEND_PLUS(ID);
					int i = 0;

					// 걍 있는거 다 보내~~ // return String[][name, nickname, last_connection, 상메 ,id]
					for (String[] list : FriendPlusList) {
						try {
							if (list[0].compareTo("null") == 0)
								continue;
						} catch (Exception e) {
							break;
						}

						out.println("UPDATE`|FRIREQ`|" + list[1] + "`|" + list[0] + "`|" + list[4]);

						System.out.println("!");

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("안죽엇닥");
			}
		}
	}
}
