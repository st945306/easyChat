import java.io.*;
import java.net.*;

public class Client{

	private BufferedReader fromServer, fromUser;
	private PrintWriter toServer;
	private final String serverIp = "127.0.0.1";
	private final int serverPort = 12345;
	private int userID;
	private InputStream is;
	private OutputStream os;
	private String targetUserName, chatRoomName;
	private boolean inChatRoom;

	public static void main(String argv[]){
		Client client = new Client();
		client.run();
	}

	public void createSocket(){
		Socket socket = new Socket();
		try{
			socket = new Socket(serverIp, serverPort);
		}
		catch(Exception e){
			System.out.println("create socket error");
		}
		try{
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(socket.getOutputStream(), true);
			fromUser = new BufferedReader(new InputStreamReader(System.in));
			System.out.println(fromServer.readLine());
		}
		catch(Exception e){
			System.out.println("get I/O error");
		}
	}

	public boolean login(String name, String password){
		String result = new String();
		try{
			toServer.println("login");
			toServer.println(name);
			toServer.println(password);
			userID = Integer.parseInt(fromServer.readLine());
		}
		catch(Exception e){
			System.out.println("client send msg error");
		}
		return (userID >= 0);
	}

	public boolean register(String name, String password){
		try{
			toServer.println("register");
			toServer.println(name);
			toServer.println(password);
			userID = Integer.parseInt(fromServer.readLine());
		}
		catch(Exception e){
			System.out.println("client send msg error");
		}
		return (userID >= 0);
	}

	public void createFileSocket(){
		try{
			fromServer.readLine();	//wait until server open the socket
			Socket fileSocket = new Socket(serverIp, 10000 + userID);
			is = fileSocket.getInputStream();
			os = fileSocket.getOutputStream();
		}
		catch(Exception e){
			System.out.println("create file socket error");
		}
	}

	public int checkOnline(String targetName){
		String reply = new String();
		try{
			toServer.println("checkOnline");
			toServer.println(targetName);
			reply = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("checkOnline error");
		}
		return Integer.parseInt(reply);
	}

	public boolean selectTarget(String targetName){
		String reply = new String();
		targetUserName = targetName;
		try{
			toServer.println("selectTarget");
			toServer.println(targetName);
			reply = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("selectTarget error");
		}

		if (reply.equals("success")){
			System.out.println("target user exist");
			inChatRoom = false;
			return true;
		}
		else{
			System.out.println("target user does not exist");
			return false;
		}
	}

	public boolean createChatRoom(String chatRoomName){
		String result = new String();
		try {
			toServer.println("createChatRoom");
			toServer.println(chatRoomName);
			result = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("create chat room error");
		}
		if (result.equals("success")){
			System.out.println("chat room created");
			return true;
		}
		else{
			System.out.println("chat room name exist");
			return false;
		}
	}

	public boolean enterChatRoom(String chatRoomName){
		String reply = new String();
		this.chatRoomName = chatRoomName;
		try{
			toServer.println("enterChatRoom");
			toServer.println(chatRoomName);
			reply = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("enter chat room error");
		}
		if (reply.equals("success")){
			System.out.println("entered the chat room " + chatRoomName);
			inChatRoom = true;
			return true;
		}
		else{
			System.out.println("chat room does not exist");
			return false;
		}

	}

	public String[] getChatRoomMember(){
		try {
			toServer.println("getChatRoomMember");
			int memberNum = Integer.parseInt(fromServer.readLine());
			String[] members = new String[memberNum];
			for (int i = 0; i < memberNum; i++)
				members[i] = fromServer.readLine();
			return members;
		}
		catch(Exception e){
			System.out.println("get chat room member error");
			System.exit(0);
			return new String[1];
		}
	}

	public void send(String message){
		try{
			toServer.println("send");
			toServer.println(message);
		}
		catch(Exception e){
			System.out.println("send message error");
		}
	}

	public String receive(){
		String message = new String();
		try{
			toServer.println("receive");
			message = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("receive message error");
		}
		return message;
	}

	public void sendFile(String fileName){
		try{
			System.out.println("client sending file " + fileName + "...");
			toServer.println("sendFile");

			File file = new File(fileName);
			int fileSize = (int)file.length();
			toServer.println(fileName);
			toServer.println(fileSize);

			byte[] buffer = new byte[fileSize];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(buffer, 0, fileSize);

			os.write(buffer, 0, fileSize);
			os.flush();

			bin.close();
			fin.close();
			System.out.println("file " + fileName + " sent");
		}
		catch (Exception e){
			System.out.println("send file error");
		}
	}

	public boolean receiveFile(){
		try {
			toServer.println("receiveFile");
			String status = fromServer.readLine();
			if (status.equals("noFile"))
				return false;
			String fileName = fromServer.readLine();
			int fileSize = Integer.parseInt(fromServer.readLine());

			byte[] buffer = new byte[fileSize];
			FileOutputStream fout = new FileOutputStream(fileName);
			BufferedOutputStream bout = new BufferedOutputStream(fout);

			int byteRead = 0;
			for (int i = 0; i < fileSize;){
				byteRead = is.read(buffer, 0, fileSize);
				bout.write(buffer, 0, byteRead);
				i += byteRead;
				System.out.format("%.1f%% complete%n", i * 1.0 / fileSize * 100);
			}
			bout.flush();
			bout.close();
			fout.close();
			System.out.format("file %s: %d bytes received%n", fileName, fileSize);
			return true;
		}
		catch (Exception e){
			System.out.println("receive file error");
			return false;
		}
	}

	public void logout(){
		toServer.println("logout");
	}

	private String getHisFileName(){
		return (inChatRoom) ? "r" + chatRoomName + ".his" : "u" + targetUserName + ".his";
	}

	public void store(String his){
		try{
			FileWriter fr = new FileWriter(getHisFileName(), false);
			PrintWriter pw = new PrintWriter(fr);
			pw.print(his);
			pw.flush();
			fr.close();
		}
		catch (Exception e){
			System.out.println("store error");
			return;
		}
	}

	public String restore(){
		try{
			FileReader fr = new FileReader(getHisFileName());
			BufferedReader br = new BufferedReader(fr);
			String his = "";
			while (br.ready())
				his += (br.readLine() + "\n");
			return his;
		}
		catch(IOException e){
			System.out.println("restore error");
			System.exit(0);
			return new String();
		}
	}

	public void run(){
		createSocket();
		String command, password;
		String name = "";
		boolean isLoginOrRegister = false;
		try {
			// login or register
			while(!isLoginOrRegister){
				System.out.print("login or register: ");
				command = fromUser.readLine();
				System.out.print("name: ");
				name = fromUser.readLine();
				System.out.print("password: ");

				password = fromUser.readLine();
				if (command.equals("login"))
					if (login(name, password)){	
						System.out.println("successfully logged in");
						isLoginOrRegister = true;
					}
					else
						System.out.println("login failed");
				else if (command.equals("register"))
					if (register(name, password)){
						System.out.println("successfully registered");
						isLoginOrRegister = true;
					}
					else
						System.out.println("registered failed");
			}
			// create fileSocket
			createFileSocket();

			// check online
//			System.out.println(checkOnline("Ryan"));
//			System.out.println(checkOnline("Nicky"));

/*
			createChatRoom("Yo man");
			enterChatRoom("Yo man");
			
			String[] members = getChatRoomMember();
			for (int i = 0; i < members.length; i++)
				System.out.println(members[i]);
*/
/*
			while (true){
				System.out.print("Who do you want to chat with? ");
				String targetName = fromUser.readLine();
				if (selectTarget(targetName)){
					System.out.format("Start chatting with %s!%n", targetName);
					break;
				}
			}
*/
	//		selectTarget("Nicky");
	/*		
			if (name.equals("Nicky"))
				sendFile("old.jpg");
			else
				if (!receiveFile())
	
*/
		//	store("line1" + "\n" + "line2" + "\n" + "line3" + "\n");
		//	System.out.print(restore());


			String message, fileName, tName, cName;
			String[] members;
			while (true){
				if (fromUser.ready()){
					message = fromUser.readLine();
					switch (message){
						case "checkOnline":
							System.out.print("target name: ");
							tName = fromUser.readLine();
							switch (checkOnline(tName)){
								case 1:
									System.out.println("user is online");
									break;
								case 2:
									System.out.println("user is offline");
									break;
								case 3:
									System.out.println("user does not exist");
									break;
							}
							break;

						case "selectTarget":
							System.out.print("target name: ");
							tName = fromUser.readLine();
							selectTarget(tName);
							break;

						case "createChatRoom":
							System.out.print("chat room name: ");
							cName = fromUser.readLine();
							createChatRoom(cName);
							break;

						case "enterChatRoom":
							System.out.print("chat room name: ");
							cName = fromUser.readLine();
							enterChatRoom(cName);
							break;

						case "sendFile":
							System.out.print("file name: ");
							fileName = fromUser.readLine();
							sendFile(fileName);
							break;
						
						case "receiveFile":
							if (!receiveFile())
								System.out.println("no new file!");
							break;

						case "get":
							members = getChatRoomMember();
							for (int i = 0; i < members.length; i++)
								System.out.println(members[i]);
							break;

						case "logout":
							logout();
							System.exit(0);

						default:
							send(message);
					}
				}
				message = receive();
				if (message.length() != 0)
					System.out.println(message);
			}
			//send("You suck!");
			//System.out.println(receive());
		}
		catch (Exception e){
			System.out.println("general error");
		}
	}
}
