public class User{
	public static final int MAXUSERNUM = 10;
	public static final int MAXFILESIZE = 30000000;	//30mb
	private String[] mailbox = new String[MAXUSERNUM];
	private String[] chatRoomMailbox = new String[ChatRoom.MAXCHATROOMNUM];
	private boolean[] hasNewMessage = new boolean[MAXUSERNUM];
	private boolean[] chatRoomHasNewMessage = new boolean[ChatRoom.MAXCHATROOMNUM];
	private byte[] filebox = new byte[MAXFILESIZE];
	public boolean hasNewFile;
	static int userNum;
	private int id;
	private String name;
	private String fileName;
	private int fileSize;
	private String password;
	private boolean online;

	public User(int id, String name, String password, boolean online){
		this.id = id;
		this.name = name;
		this.password = password;
		this.online = online;
		for (int i = 0; i < MAXUSERNUM; i++)
			hasNewMessage[i] = false;
	}

	public void printUserInfo(){
		System.out.format("%d name: %s, password: %s, online: %s%n", this.id, this.name, this.password, this.online);
	}

	public String getName(){
		return this.name;
	}

	public String getPassword(){
		return this.password;
	}

	public int getId(){
		return this.id;
	}

	public void setOnline(){
		this.online = true;
	}

	public void setOffline(){
		this.online = false;
	}

	public boolean isOnline(){
		return this.online;
	}

	public void putMessage(boolean inChatRoom, int fromID, String userName, String message){
		message = userName + ": " + message;
		if (!inChatRoom) {
			if (hasNewMessage[fromID]){
				mailbox[fromID] += "\n";
				mailbox[fromID] += message;
			}
			else {
				mailbox[fromID] = message;
				hasNewMessage[fromID] = true;
			}
		}
		else {
			if (chatRoomHasNewMessage[fromID]){
				chatRoomMailbox[fromID] += "\n";
				chatRoomMailbox[fromID] += message;
			}
			else {
				chatRoomMailbox[fromID] = message;
				chatRoomHasNewMessage[fromID] = true;
			}
		}
	}

	public String getMessage(boolean inChatRoom, int fromID){
		if (!inChatRoom){
			if (hasNewMessage[fromID]){
				hasNewMessage[fromID] = false;
				return mailbox[fromID];
			}
		}
		else {
			if (chatRoomHasNewMessage[fromID]){
				chatRoomHasNewMessage[fromID] = false;
				return chatRoomMailbox[fromID];
			}
		}
		return "";
	}

	public void putFile(String fileName, int fileSize, byte[] file){
		this.fileName = "new.jpg";
		this.fileSize = fileSize;
		this.filebox = file;
		this.hasNewFile = true;
	}

	public int getFileSize(){
		return fileSize;
	}

	public String getFileName(){
		return fileName;
	}

	public byte[] getFile(){
		this.hasNewFile = false;
		return filebox;
	}
}
