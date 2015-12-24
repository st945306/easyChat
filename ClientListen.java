import javax.swing.*;

public class ClientListen extends Thread {
	Client client;
	JTextArea msgToDisplay;
	String msg;

	public ClientListen(Client cl, JTextArea ms) {
		client = cl;
		msgToDisplay = ms;
	}

	@Override
	public void run() {
		System.out.println("Client listen starts");
		while(true) {
			msg = client.receive();
			if(msg.length() == 0)
				continue;
			msgToDisplay.append("Others: " + msg + "\n");
		}
	}
}
