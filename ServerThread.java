// change the function name of loginOrRegister?
// cause it has a STOP function now
import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	private static final int MAXUSERNUM = 30;
	private static final int MAXCHATROOMNUM = 30;
	private static User[] users = new User[MAXUSERNUM];
	private static ChatRoom[] chatRooms = new ChatRoom[MAXCHATROOMNUM];
	private static String[][] mailbox = new String[MAXUSERNUM][MAXUSERNUM];
	private static boolean[][] hasNewMessage = new boolean[MAXUSERNUM][MAXUSERNUM];
	private Socket socket, fileSocket;
	private ServerSocket fileServerSocket;
	private PrintWriter toClient;
	private BufferedReader fromClient;
	private int userID, targetUserID, chatRoomID;
	private InputStream is;
	private OutputStream os;
	private boolean inChatRoom = false;


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
			}
			if (command.equals("login")){
				System.out.println("login");
				for (int i = 0; i < User.userNum; i++){
					if (users[i].getName().equals(name) && 
						users[i].getPassword().equals(password)){
						toClient.println(i);
						users[i].setOnline();
						userID = i;
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
				}
				System.out.format("%d name: %s, password: %s is registered%n", id, name, password);
				toClient.println(id);
				userID = id;
				return;
			}
		}
	}

	private void startChat(){
		String command = "nothing";
		String targetName = new String();
		String onlineResult = new String();
		String message = new String();

		System.out.println(userID);
		System.out.println(hasNewMessage[0][userID]);



		while(true){
			try{
				if (fromClient.ready())
					command = fromClient.readLine();
				if (command.equals("check")){
					targetName = fromClient.readLine();
					System.out.format("checking if %s is online...%n", targetName);
					int i;
					for (i = 0; i < User.userNum; i++)
						if (users[i].getName().equals(targetName)){
							if (users[i].isOnline())
								onlineResult = "1";
							else
								onlineResult = "2";
							break;
						}
					if (i == User.userNum)
						onlineResult = "3";
					toClient.println(onlineResult);
					command = "nothing";
				}
				else if (command.equals("change")){
					//change targetUserID
					targetName = fromClient.readLine();
					System.out.println("selecting " + targetName + "...");
					int i;
					for (i = 0; i < User.userNum; i++)
						if (users[i].getName().equals(targetName)){
							toClient.println("success");
							targetUserID = i;
							inChatRoom = false;
							break;
						}
					if (i == User.userNum)
						toClient.println("failed");
					command = "nothing";
				}
				else if (command.equals("createChatRoom")){
					String chatRoomName = fromClient.readLine();
					int i;
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
					command = "nothing";
				}
				else if (command.equals("enterChatRoom")){
					//change chatRoomID
					String chatRoomName = fromClient.readLine();
					System.out.println("entering " + chatRoomName + "...");
					int i;
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
					command = "nothing";
				}
				else if(command.equals("send")){
					//write to mailbox[userID][targetUserID]
					message = fromClient.readLine();
					if (!inChatRoom){
						System.out.format("from %d to %d: %s%n", userID, targetUserID, message);
						if (hasNewMessage[userID][targetUserID]){
							mailbox[userID][targetUserID] += "\n";
							mailbox[userID][targetUserID] += message;
						}
						else {
							mailbox[userID][targetUserID] = message;
							hasNewMessage[userID][targetUserID] = true;
						}
					}
					else {
						int[] memberIDs = chatRooms[chatRoomID].memberIDs;
						for (int i = 0; i < chatRooms[chatRoomID].memberNum; i++){
							System.out.format("from %d to %d: %s%n", userID, memberIDs[i], message);
							if (hasNewMessage[userID][memberIDs[i]]){
								mailbox[userID][memberIDs[i]] += "\n";
								mailbox[userID][memberIDs[i]] += message;
							}
							else {
								mailbox[userID][memberIDs[i]] = message;
								hasNewMessage[userID][memberIDs[i]] = true;
							}
						}

					}
					command = "nothing";
				}
				else if(command.equals("receive")){
					//read from mailbox[targetUserID][userID]
					if (!inChatRoom){
						if (hasNewMessage[targetUserID][userID]){
							message = mailbox[targetUserID][userID];
							hasNewMessage[targetUserID][userID] = false;
							toClient.println(message);
						}
						else
							toClient.println("");
					}
					else {
						int[] memberIDs = chatRooms[chatRoomID].memberIDs;
						message = "";
						for (int i = 0; i < chatRooms[chatRoomID].memberNum; i++)
							if (hasNewMessage[memberIDs[i]][userID]){
								message += mailbox[memberIDs[i]][userID];
								hasNewMessage[memberIDs[i]][userID] = false;
							}
						toClient.println(message);
					}
					command = "nothing";
				}
				else if(command.equals("sendFile")){
					String filename = fromClient.readLine();
					int filesize = Integer.parseInt(fromClient.readLine());

					byte[] buffer = new byte[filesize];
					FileOutputStream fout = new FileOutputStream("file2");
					BufferedOutputStream bout = new BufferedOutputStream(fout);

					int byteRead = 0;
					for (int i = 0; i < filesize;){
						byteRead = is.read(buffer, 0, filesize);
						bout.write(buffer, 0, byteRead);
						i += byteRead;
						System.out.format("%.1f%% complete%n", i * 1.0 / filesize * 100);
					}
					bout.flush();
					bout.close();
					fout.close();
					System.out.format("file %s: %d bytes received%n", filename, filesize);

					command = "nothing";
				}
				else if(command.equals("receiveFile")){


					command = "nothing";
				}
				else if (command.equals("logout")){
					//close all sockets, setOffline
					System.out.println("User " + users[userID].getName() + " logged out...");
					is.close();
					os.close();
					fileSocket.close();
					fileServerSocket.close();
					socket.close();
					users[userID].setOffline();
					return;
				}

			}
			catch(Exception e){
				System.out.println("chat error");
			}
		}
	
	}

	public void run(){
		try{
			toClient = new PrintWriter(socket.getOutputStream(), true);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			fromClient = new BufferedReader(isr);

			toClient.println("Welcome to easyChat!");
			loginOrRegister();	//will only return when user successfully login or register

			// create fileServerSocket
			InetAddress ip = InetAddress.getByName(Server.serverIP);
			fileServerSocket = new ServerSocket(10000 + userID, 50, ip);
			toClient.println("done");
			fileSocket = fileServerSocket.accept();
			is = fileSocket.getInputStream();
			os = fileSocket.getOutputStream();
	/*		
			System.out.println(fromClient.readLine());
			System.out.println(fromClient.readLine());

			toClient.println("test1");
*/

			startChat();
		}
		catch(Exception e){
			System.out.println("server send msg error");
		}
	}
}