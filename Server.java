import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server{

	private ServerSocket serverSocket;
private String serverConfig;
	public static String serverIP;
	public static int serverPort; //assume the port is always correct

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

	private void listen() {
		Socket clientSocket = new Socket();
		System.out.println("server starts listening");
		while(true) {
			try {
				clientSocket = serverSocket.accept();
			}
			catch(Exception e) {
				System.out.println("accept client socket error");
			}
			ServerThread serverThread = new ServerThread(clientSocket);
			serverThread.start();
		}
	}

	public void run() {
		createSocket();
		listen();
	}
}
