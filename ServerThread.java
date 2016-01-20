import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	private static final int MAXUSERNUM = User.MAXUSERNUM;
	private static final int MAXCHATROOMNUM = ChatRoom.MAXCHATROOMNUM;
	private static User[] users = new User[MAXUSERNUM];
	private static ChatRoom[] chatRooms = new ChatRoom[MAXCHATROOMNUM];
//	private static String[][] mailbox = new String[MAXUSERNUM][MAXUSERNUM];
//	private static boolean[][] hasNewMessage = new boolean[MAXUSERNUM][MAXUSERNUM];
	private Socket socket, fileSocket;
	private ServerSocket fileServerSocket;
	private PrintWriter toClient;
	private BufferedReader fromClient;
	private int userID, targetUserID, chatRoomID;
	private InputStream is;
	private OutputStream os;
	private boolean inChatRoom = false;
	private String userName;

	public ServerThread(Socket socket, User[] users, ChatRoom[] chatRooms){
		this.socket = socket;
		this.users = users;
		this.chatRooms = chatRooms;
	}
	
	private void loginOrRegister(){
		String command = new String();
		String name = new String();
		String password = new String();
		while (true){
			try{
				command = fromClient.readLine();
				name = fromClient.readLine();
				password = fromClient.readLine();
			}
			catch (Exception e){
				System.out.println("server login or register error");
				return;
			}
			if (command.equals("login")){
				System.out.println("login");
				for (int i = 0; i < User.userNum; i++){
					if (users[i].getName().equals(name) && 
						users[i].getPassword().equals(password)){
						toClient.println(i);
						users[i].setOnline();
						userID = i;
						userName = name;
						return;
					}
				}
				System.out.println("wrong name or password");
				toClient.println(-1);
			}
			else if (command.equals("register")){
				System.out.println("register");
				int i;
				for (i = 0; i < User.userNum; i++)
					if (users[i].getName().equals(name)){
						System.out.println("name has already been registered");
						toClient.println(-1);
						break;
					}
				if (i != User.userNum)
					continue;
				//name is legal to use
				int id = User.userNum;
				users[id] = new User(id, name, password, true);
				users[id].printUserInfo();
				User.userNum++;

				try{
					FileWriter fr = new FileWriter("./user.dat", true);
					PrintWriter pw = new PrintWriter(fr);
					pw.format("%d%n%s%n%s%n", id, name, password);
					pw.flush();
					fr.close();
				}
				catch (Exception e){
					System.out.println("server write user.dat error");
					return;
				}
				System.out.format("%d name: %s, password: %s is registered%n", id, name, password);
				toClient.println(id);
				userID = id;
				userName = name;
				return;
			}
		}
	}

	private void checkOnline(){
		int i, result = -1;
		try {
			String targetName = fromClient.readLine();
			System.out.format("checking if %s is online...%n", targetName);
			for (i = 0; i < User.userNum; i++)
				if (users[i].getName().equals(targetName)){
					if (users[i].isOnline())
						result = 1;//is online
					else
						result = 2;//is not online
					break;
				}
			if (i == User.userNum)
				result = 3;//user not exist
			toClient.println(result);
		}
		catch(Exception e){
			System.out.println("check online error");
		}
	}

	private void selectTarget(){
		int i;
		try {
			String targetName = fromClient.readLine();
			System.out.println("selecting " + targetName + "...");
			for (i = 0; i < User.userNum; i++)
				if (users[i].getName().equals(targetName)){
					toClient.println("success");
					targetUserID = i;
					inChatRoom = false;
					break;
				}
			if (i == User.userNum)
				toClient.println("failed");
		}
		catch (Exception e){
			System.out.println("selectTarget error");
		}
	}
	
	private void createChatRoom(){
		int i;
		try {
			String chatRoomName = fromClient.readLine();
			for (i = 0; i < ChatRoom.chatRoomNum; i++)
				if (chatRoomName.equals(chatRooms[i].getName())){
					System.out.println("chat room exists");
					toClient.println("failed");
					break;
				}
			if (i == ChatRoom.chatRoomNum){
				chatRooms[ChatRoom.chatRoomNum] = new ChatRoom(chatRoomName);
				System.out.println("chat room " + chatRoomName + " is created");
				toClient.println("success");
			}
		}
		catch(Exception e){
			System.out.println("create chat room error");
		}
	}

	private void enterChatRoom(){
		int i;
		try {
			String chatRoomName = fromClient.readLine();
			System.out.println("entering " + chatRoomName + "...");
			for (i = 0; i < ChatRoom.chatRoomNum; i++)
				if (chatRooms[i].getName().equals(chatRoomName)){
					toClient.println("success");
					chatRoomID = i;
					chatRooms[i].addMember(userID);
					chatRooms[i].printInfo();
					inChatRoom = true;
					break;
				}
			if (i == ChatRoom.chatRoomNum)
				toClient.println("failed");
		}
		catch(Exception e){
			System.out.println("enter chat room error");
		}
	}

	private void getChatRoomMember(){
		try {
			toClient.println(chatRooms[chatRoomID].memberNum);
			for (int i = 0; i < chatRooms[chatRoomID].memberNum; i++)
				toClient.println(users[chatRooms[chatRoomID].memberIDs[i]].getName());	
		}
		catch(Exception e){
			System.out.println("get chat room member error");
		}
	}

	private void send(){
		try {
			String message = fromClient.readLine();
			if (!inChatRoom){
				System.out.format("from %d to %d: %s%n", userID, targetUserID, message);
				users[targetUserID].putMessage(false, userID, userName, message);
			}
			else {
				int[] memberIDs = chatRooms[chatRoomID].memberIDs;
				for (int i = 0; i < chatRooms[chatRoomID].memberNum; i++){
					if (userID == memberIDs[i])
						continue;
					System.out.format("from %d to %d: %s%n", userID, memberIDs[i], message);
					users[memberIDs[i]].putMessage(true, chatRoomID, userName, message);
				}
			}
		}
		catch(Exception e){
			System.out.println("send error");
		}
	}

	private void receive(){
		try {
			String message;
			if (!inChatRoom)
				toClient.println(users[userID].getMessage(false, targetUserID));
			else
				toClient.println(users[userID].getMessage(true, chatRoomID));
		}
		catch(Exception e){
			System.out.println("receive error");
		}
	}

	//this is for server receive file and put in fileBox
	private void sendFile(){
		try {
			String fileName = fromClient.readLine();
			int fileSize = Integer.parseInt(fromClient.readLine());
			byte[] buffer = new byte[fileSize];

			int byteRead = 0;
			for (int i = 0; i < fileSize;){
				byteRead = is.read(buffer, i, fileSize - i);
				i += byteRead;
				System.out.format("%.1f%% complete%n", i * 1.0 / fileSize * 100);
			}
			if (!inChatRoom){
				users[targetUserID].putFile(fileName, fileSize, buffer);
				System.out.format("file %s: %d bytes received by %s%n", fileName, 
									fileSize, users[targetUserID].getName());
			}
			else {
				int[] memberIDs = chatRooms[chatRoomID].memberIDs;
				for (int i = 0; i < chatRooms[chatRoomID].memberNum; i++){
					if (userID == memberIDs[i])
						continue;
					users[memberIDs[i]].putFile(fileName, fileSize, buffer);
					System.out.format("file %s: %d bytes received by %s%n", fileName,
									fileSize, users[memberIDs[i]].getName());
				}
			}
		}
		catch (Exception e){
			System.out.println("put file in box error");
		}
	}

	//this is for server send file
	private void receiveFile(){
		try {
			if (!users[userID].hasNewFile){
				toClient.println("noFile");
				return;
			}
			toClient.println("hasFile");

			String fileName = users[userID].getFileName();
			int fileSize = users[userID].getFileSize();
			byte[] file = users[userID].getFile();

			System.out.println("server sending file " + fileName + "...");

			toClient.println(fileName);
			toClient.println(fileSize);

			os.write(file, 0, fileSize);
			os.flush();
		}
		catch (Exception e){
			System.out.println("server send file error");
		}

	}

	private void logout(){
		System.out.println("User " + users[userID].getName() + " logged out...");
		try {
			is.close();
			os.close();
			fileSocket.close();
			fileServerSocket.close();
			socket.close();
			users[userID].setOffline();
		}
		catch (Exception e){
			System.out.println("log out error");
		}
	}

	private void startChat(){
		String command = "nothing";
		while(true){
			try{
				command = fromClient.readLine();
				if (command.equals("checkOnline"))
					checkOnline();
				else if (command.equals("selectTarget"))
					selectTarget();
				else if (command.equals("createChatRoom"))
					createChatRoom();
				else if (command.equals("enterChatRoom"))
					enterChatRoom();
				else if (command.equals("getChatRoomMember"))
					getChatRoomMember();
				else if(command.equals("send"))
					send();
				else if(command.equals("receive"))
					receive();
				else if(command.equals("sendFile"))
					sendFile();			
				else if(command.equals("receiveFile"))
					receiveFile();
				else if (command.equals("logout")){
					logout();
					return;
				}
			}
			catch(Exception e){
				System.out.println("chat error");
				return;
			}
		}
	}

	public void run(){
		try{
			toClient = new PrintWriter(socket.getOutputStream(), true);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			fromClient = new BufferedReader(isr);
			toClient.println("Welcome to easyChat!");
		}
		catch(Exception e){
			System.out.println("server welcome error");
		}

		loginOrRegister();	//will only return when user successfully login or register
		
		try {
			// create fileServerSocket
			InetAddress ip = InetAddress.getByName(Server.serverIP);
			fileServerSocket = new ServerSocket(10000 + userID, 50, ip);
			toClient.println("done");
			fileSocket = fileServerSocket.accept();
			is = fileSocket.getInputStream();
			os = fileSocket.getOutputStream();
		}
		catch(Exception e){
			System.out.println("create file socket error");
		}

		startChat();
	}
}
