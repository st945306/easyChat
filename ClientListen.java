import javax.swing.*;

public class ClientListen extends Thread {
	Client client;
	ClientGUI clientGUI;
	JTextArea textarea;
	String msg;

	public ClientListen(Client client, ClientGUI clientGUI, JTextArea textarea) {
		this.client = client;
		this.clientGUI = clientGUI;
		this.textarea = textarea;
	}

	@Override
	public void run() {
		System.out.println("Client listen starts");
		while(clientGUI.isListening) {
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
				System.out.println("error: ClientListen sleep");
				System.out.println(e);
			}

			if(clientGUI.isListenLocked)
				continue;

			//get file
			if(client.receiveFile())
				textarea.append("[System Message] You have received a file!\n");

			//get messages
			msg = client.receive();
			if(msg.length() == 0) continue;
			textarea.append(msg + "\n");
		}
		System.out.println("Client listen terminates");
	}
}