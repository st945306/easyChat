import java.io.*;
import java.net.*;

public class Client{

	private BufferedReader fromServer, fromUser;
	private PrintWriter toServer;
	private InputStream str;

	public void createSocket(){
		String ip = "127.0.0.1";
		int port = 12345;
		Socket socket = new Socket();
		try{
			socket = new Socket(ip, port);
		}
		catch(Exception e){
			System.out.println("create socket error");
		}
		try{
			str = socket.getInputStream();
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
			result = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("client send msg error");
		}
		return (result.equals("success")) ? true : false;
	}

	public boolean register(String name, String password){
		String result = new String();
		try{
			toServer.println("register");
			toServer.println(name);
			toServer.println(password);
			result = fromServer.readLine();
		}
		catch(Exception e){
			System.out.println("client send msg error");
		}
		return (result.equals("success")) ? true : false;
	}

	public void run(){
		createSocket();
		try {
			while (true){
			String command = fromUser.readLine();
			String name = fromUser.readLine();
			String password = fromUser.readLine();
			if (command.equals("login")){
				if (login(name, password))
					System.out.println("successfully login");
				else
					System.out.println("login failed");
			}
		}
			/*
			else if (command.equals("register")){
				if (register(name, password))
					System.out.println("successfully registered");
				else
					System.out.println("registered failed");
			}*/
		}
		catch (Exception e){
			System.out.println("general error");
		}
	}
}
