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
			toServer.println("check");
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
		try{
			toServer.println("change");
			toServer.println(targetName);
			reply = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("selectTarget error");
		}

		if (reply.equals("success")){
			System.out.println("target user exist");
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
			return true;
		}
		else{
			System.out.println("chat room does not exist");
			return false;
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

	public void sendFile(String filename){
		try{
			System.out.println("sending file " + filename + "...");
			toServer.println("sendFile");

			File file = new File(filename);
			int filesize = (int)file.length();
			toServer.println(filename);
			toServer.println(filesize);

			byte[] buffer = new byte[filesize];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(buffer, 0, buffer.length);

			os.write(buffer, 0, filesize);
			os.flush();

			bin.close();
			fin.close();
		}
		catch (Exception e){
			System.out.println("send file error");
		}
	}

	public void logout(){
		toServer.println("logout");
	}

	public void run(){
		createSocket();

		boolean isLoginOrRegister = false;
		String command, name, password;
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
			System.out.println(checkOnline("Ryan"));
			System.out.println(checkOnline("Nicky"));

			createChatRoom("Yo man");
			enterChatRoom("Yo man");

/*
			while (true){
				System.out.print("Who do you want to chat with? ");
				String targetName = fromUser.readLine();
				if (selectTarget(targetName)){
					System.out.format("Start chatting with %s!%n", targetName);
					break;
				}
			}


		
	//		selectTarget("Nicky");

/*
			toServer.println("111");
			toServer.println("222");
			System.out.println(fromServer.readLine());
*/
//			sendFile("file");

			String message;
			while (true){
				if (fromUser.ready()){
					message = fromUser.readLine();
					if (message.equals("logout")){
						logout();
						break;
					}
					send(message);
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
