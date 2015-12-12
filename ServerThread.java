import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

	private static final int MAXUSER = 30;
	private static String[][] messages = new String[MAXUSER][MAXUSER];
	private Socket socket;
	private User[] users = new User[MAXUSER];
	private PrintWriter toClient;
	private BufferedReader fromClient;

	private void readUserData(){
		User.userNum = 0;
		try{
			FileReader fr = new FileReader("./user.dat");
			BufferedReader br = new BufferedReader(fr);
			int id;
			String name, password;
			while (br.ready()){
				id = Integer.parseInt(br.readLine());
				name = br.readLine();
				password = br.readLine();
				users[id] = new User(id, name, password);
				users[id].printUserInfo();
				User.userNum++;
			}
		}
		catch(IOException e){
			System.out.println("read user data error");
		}
	}

	public ServerThread(Socket socket){
		this.socket = socket;
		readUserData();
	}
	
	private void loginOrRegister(){
		String command = new String();
		String name = new String();
		String password = new String();
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
					toClient.println("success");
					return;
				}
				toClient.println("failed");
			}
		}
		else if (command.equals("register")){
			System.out.println("register");

		}
	}

	public void run(){
		try{
			OutputStream os = socket.getOutputStream();
			toClient = new PrintWriter(os, true);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			fromClient = new BufferedReader(isr);

			toClient.println("Welcome to easyChat!");
			loginOrRegister();


			/*
			while (true){
				if (!fromClient.ready())
					continue;

			}
			String str = fromClient.readLine();
			System.out.println(str);
			*/
		}
		catch(Exception e){
			System.out.println("server send msg error");
		}
	}


}