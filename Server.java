import java.io.*;
import java.net.*;

public class Server{

	private ServerSocket serverSocket;
	public final static String serverIp = "140.112.30.34";
	public final static int serverPort = 12345;

	public static void main(String argv[]){
		Server server = new Server();
		server.run();
	}

	private void createSocket(){
		try{
			InetAddress ip = InetAddress.getByName(serverIp);
			serverSocket = new ServerSocket(serverPort, 50, ip);
		}
		catch(Exception e){
			System.out.println("open server socket error");
		}
	}

	private void listen(){
		Socket clientSocket = new Socket();
		while (true){
			try{
				clientSocket = serverSocket.accept();
			}
			catch(Exception e){
				System.out.println("accept client socket error");
			}
			ServerThread serverThread = new ServerThread(clientSocket);
			serverThread.start();
		}
	}

	public void run(){
		createSocket();
		listen();
	}
}
