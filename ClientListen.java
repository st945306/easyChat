import javax.swing.*;

public class ClientListen extends Thread {
	Client client;
	ClientGUI clientGUI;
	String targetUser;

	JTextArea msgToDisplay;
	String msg;

	public ClientListen(Client cli, ClientGUI cliGUI, JTextArea msg, String user) {
		client = cli;
		clientGUI = cliGUI;
		msgToDisplay = msg;
		targetUser = user;
	}

	@Override
	public void run() {
		System.out.println("Client listen starts");
		while(clientGUI.isListening) {
			//don't do anything while processing sending/checking online
			if(clientGUI.isSending || clientGUI.isCheckingOnline) continue;

			//get messages
			msg = client.receive();
			if(msg.length() == 0)
				continue;
			msgToDisplay.append(msg + "\n");
		}
		System.out.println("Client listen terminates");
	}
}
