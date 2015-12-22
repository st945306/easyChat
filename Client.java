import java.io.*;
import java.net.*;

public class Client{

	private BufferedReader fromServer, fromUser;
	private PrintWriter toServer;
	private final String serverIp = "127.0.0.1";
	private final int serverPort = 12345;
	private OutputStream os;
	private InputStream is;

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
			is = socket.getInputStream();
			os = socket.getOutputStream();
			fromServer = new BufferedReader(new InputStreamReader(is));
			toServer = new PrintWriter(os, true);
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

			os.flush();

			os.write(buffer, 0, filesize);
			os.flush();
		}
		catch (Exception e){
			System.out.println("send file error");
		}
	}
	public void run(){
		createSocket();

		boolean isLoginOrRegister = false;
		String command, name, password;
		try {
			
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
			System.out.println(checkOnline("Ryan"));
			System.out.println(checkOnline("Nicky"));


			while (true){
				System.out.print("Who do you want to chat with? ");
				String targetName = fromUser.readLine();
				if (selectTarget(targetName)){
					System.out.format("Start chatting with %s!%n", targetName);
					break;
				}
			}
			

		//	sendFile("old.jpg");
			String message;
			while (true){
				if (fromUser.ready()){
					message = fromUser.readLine();
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
