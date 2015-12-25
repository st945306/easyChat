class ChatRoom{
	static public int chatRoomNum = 0;
	public int memberNum;
	public int[] memberIDs = new int[100];
	private String chatRoomName;
	private int chatRoomID;

	public ChatRoom(String name){
		this.chatRoomID = chatRoomNum;
		this.chatRoomName = name;
		this.memberNum = 0;
		chatRoomNum++;
	}

	public void addMember(int id){
		this.memberIDs[this.memberNum] = id;
		this.memberNum++;
	}

	public void printInfo(){
		System.out.format("Chat room %s with %d members: %n", this.chatRoomName, this.memberNum);
		for (int i = 0; i < this.memberNum; i++)
			System.out.println(memberIDs[i]);
	}

	public String getName(){
		return this.chatRoomName;
	}





}