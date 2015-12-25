import javax.swing.*;

public class ClientListen extends Thread {
	Client client;
	ClientGUI clientGUI;
	JTextArea msgToDisplay;
	String msg;

	public ClientListen(Client cl, ClientGUI clg, JTextArea ms) {
		client = cl;
		clientGUI = clg;
		msgToDisplay = ms;
	}

	@Override
	public void run() {
		System.out.println("Client listen starts");
		while(clientGUI.isListening) {
			if(clientGUI.isSending)
				continue;
			msg = client.receive();
			if(msg.length() == 0)
				continue;
			msgToDisplay.append("Others: " + msg + "\n");
		}
		System.out.println("Client listen terminates");
	}
}
