//this is a prototype of client GUI
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class TestSendAndListen {

	Client client;
	ClientListen clientlisten;
	JTextField msgToSend;
	JTextArea msgToDisplay;
	//JLabel systemMsg;

	public static void main(String[] args) {
		TestSendAndListen testSendAndListen = new TestSendAndListen();
		testSendAndListen.go();
	}

	public void go() {
		client = new Client();

		JFrame frame = new JFrame("easyChat Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		JLabel msgToSendLabel = new JLabel("Message to send");
		JLabel msgToDisplayLabel = new JLabel("Message to display");
		msgToSend = new JTextField();
		msgToDisplay = new JTextArea(30, 40);
		JButton btnSetNicky = new JButton("Set me Nicky");
		JButton btnSetRyan = new JButton("Set me Ryan");
		JButton btnSend = new JButton("Send");
		//systemMsg = new JLabel();

		clientlisten = new ClientListen(client, msgToDisplay);

		btnSetNicky.addActionListener(new setNickyListener());
		btnSetRyan.addActionListener(new setRyanListener());
		btnSend.addActionListener(new sendListener());

		GroupLayout layout = new GroupLayout(frame.getContentPane());
		frame.getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGap(20)
			.addGroup(layout.createParallelGroup()
				.addComponent(btnSetNicky)
				.addComponent(btnSetRyan)
				.addComponent(msgToSendLabel)
				.addComponent(msgToSend)
				.addComponent(msgToDisplayLabel)
				.addComponent(msgToDisplay)
				.addComponent(btnSend)
			)
			.addGap(20)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGap(20)
			.addComponent(btnSetNicky)
			.addGap(10)
			.addComponent(btnSetRyan)
			.addComponent(msgToSendLabel)
			.addGap(10)
			.addComponent(msgToSend)
			.addGap(10)
			.addComponent(msgToDisplayLabel)
			.addGap(10)
			.addComponent(msgToDisplay)
			.addGap(10)
			.addComponent(btnSend)
			.addGap(20)
		);

		frame.pack();
		frame.setVisible(true);
	}

	class setNickyListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			client.createSocket();
			client.login("Nicky", "1234");
			client.createFileSocket();
			client.selectTarget("Ryan");
			clientlisten.start();
		}
	}

	class setRyanListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			client.createSocket();
			client.login("Ryan", "QAQ");
			client.createFileSocket();
			client.selectTarget("Nicky");
			clientlisten.start();
		}
	}

	class sendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = msgToSend.getText();
			msgToDisplay.append("Me: " + msg + "\n");
			client.send(msg);
			msgToSend.setText("");
		}
	}
}
