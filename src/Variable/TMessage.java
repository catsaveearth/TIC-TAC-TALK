package Variable;

public class TMessage {
	private int x;
	private int y;
	private String sender_id;

	
	public TMessage(int x, int y, String sender_id) {
		this.x = x;
		this.y = y;
		this.sender_id = sender_id;	
	}
	
	public int getx() {
		return x;
	}
	
	public int gety() {
		return y;
	}
	
	public String getSender_id() {
		return sender_id;
	}
	
}
