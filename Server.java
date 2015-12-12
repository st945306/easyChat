import java.io.*;
import java.net.*;

public class Server{

	private ServerSocket serverSocket;

	private void createSocket(){
		try{
			int port = 12345;
			InetAddress ip = InetAddress.getByName("127.0.0.1");
			serverSocket = new ServerSocket(port, 50, ip);
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
