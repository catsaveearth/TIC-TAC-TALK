package Variable;

import java.util.ArrayList;

public class RequestRoom {
	private String requester_ID;
	private String threadckID; //초반부에 client의 thread를 구분할 ID
	private int type; //0이면 개인, 1이면 단체
	private int participants_num; //방장 포함 총 인원 수
	private ArrayList<String> participants_list = new ArrayList<String>();
	
	
	
	public RequestRoom(String rID, int type, int ptNum, String threadID, String[] name) {
		this.requester_ID = rID;
		this.participants_num = ptNum;
		this.type = type;
		threadckID = threadID;

		// name에는 3부터 ID가 담겨있음

		if (type == 0) {
			participants_list.add(name[3]);
		} else {
			int ck = 0;
			for (String i : name) {
				if (ck < 3) {
					ck++;
					continue;
				}
				participants_list.add(name[ck]);
			}
		}
	}
	
	
	public String getRequester_ID() {
		return requester_ID;
	}
	
	public int getType() {
		return type;
	}
	
	public int getParticipants_num() {
		return participants_num;
	}
	
	public ArrayList<String> getParticipants_list() {
		return participants_list;
	}
}
