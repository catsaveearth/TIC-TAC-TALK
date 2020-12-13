package Variable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Message {
	private int room_id;
	private int type;
	private String sender_id;
	private String message;
	private String time;
	
	public Message(int room_id, int type, String sender_id, String time, String message) {
		this.room_id = room_id;
		this.type = type;
		this.sender_id = sender_id;
		this.message = message;
		this.time = time;
		
	}
	
	public int getRoom_id() {
		return room_id;
	}
	
	public int getType() {
		return type;
	}
	
	public String getSender_id() {
		return sender_id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTime() {
		return time;
	}
	
}
