import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server{

	private ServerSocket serverSocket;
	private String serverConfig;
	public static String serverIP;
	public static int serverPort; //assume the port is always correct

	private final int MAXUSERNUM = 30;
	private final int MAXCHATROOMNUM = 30;
	private User[] users = new User[MAXUSERNUM];
	private ChatRoom[] chatRooms = new ChatRoom[MAXCHATROOMNUM];

	Server() {
		try {
			FileReader fr = new FileReader("./server_config.dat");
			BufferedReader br = new BufferedReader(fr);
			serverIP = br.readLine();
			serverPort = Integer.valueOf(br.readLine());
		}
		catch(IOException e) {
			System.out.println("read server config error");
			System.out.println("server halts");
			System.exit(1);
			//I don't know what number should be used here
		}
	}

	public static void main(String args[]) {
		Server server = new Server();
		server.run();
	}

	public void changeServerIP() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("> ");
		serverIP = scanner.nextLine();
		try {
			FileWriter fw = new FileWriter("./server_config.dat");
			fw.write(serverIP + "\n" + serverPort);
			fw.flush();
			fw.close();
		}
		catch(Exception e) {
			System.out.println("error occurs while writing server config");
		}
		System.out.println("IP has changed to " + serverIP);
		System.out.println("try to open the server socket");
		createSocket();
	}

	private void createSocket() {
		try {
			InetAddress ip = InetAddress.getByName(serverIP);
			serverSocket = new ServerSocket(serverPort, 50, ip);
		}
		catch(Exception e) {
			System.out.println("open server socket error");
			System.out.println("please assign a valid IP address");
			changeServerIP();
		}
	}

	private void readUserData(){
		User.userNum = 0;
		ChatRoom.chatRoomNum = 0;
		try{
			FileReader fr = new FileReader("./user.dat");
			BufferedReader br = new BufferedReader(fr);
			int id;
			String name, password;
			while (br.ready()){
				id = Integer.parseInt(br.readLine());
				name = br.readLine();
				password = br.readLine();
				users[id] = new User(id, name, password, false);
				users[id].printUserInfo();
				User.userNum++;
			}
		}
		catch(IOException e){
			System.out.println("read user data error");
		}
	}

	private void listen() {
		readUserData();
		Socket clientSocket = new Socket();
		System.out.println("server starts listening");
		while(true) {
			try {
				clientSocket = serverSocket.accept();
			}
			catch(Exception e) {
				System.out.println("accept client socket error");
			}
			ServerThread serverThread = new ServerThread(clientSocket, users, chatRooms);
			serverThread.start();
		}
	}

	public void run() {
		createSocket();
		listen();
	}
}
