/*
	reference:
	font size of a component: http://docs.oracle.com/javase/tutorial/uiswing/components/html.html

	question:
	padding of JButton

	to-do list:
	add a link at the bottom
	cannot connect to the server
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class ClientGUI {
	static ClientGUI clientGUI;
	Client client;
	ClientListen clientlisten;
	public static Boolean isListening = false;
	JFrame startFrame, selectTargetFrame, sendAndListenFrame;
	JTextField usernameTextField, selectUserTextField, msgToSend;
	JPasswordField passwordField;
	JTextArea msgToDisplay;

	public static void main(String[] args) {
		clientGUI = new ClientGUI();
		clientGUI.go();
	}

	public void go() {
		client = new Client();
		client.createSocket();

		startFrame = new JFrame("start");
		selectTargetFrame = new JFrame("select target");
		sendAndListenFrame = new JFrame("");

		//startFrame
		JLabel username = new JLabel("Username");
		JLabel password = new JLabel("Password");
		usernameTextField = new JTextField(16);
		passwordField = new JPasswordField(16);
		JButton btnLogIn = new JButton("Log In");
		JLabel registration = new JLabel("Click me to sign up");

		username.setFont(new Font("Arial", Font.PLAIN, 16));
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnLogIn.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setFont(new Font("Arial", Font.PLAIN, 16));

		btnLogIn.addActionListener(new logInListener());

        GroupLayout loginLayout = new GroupLayout(startFrame.getContentPane());
        startFrame.getContentPane().setLayout(loginLayout);

		loginLayout.setHorizontalGroup(loginLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(loginLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(loginLayout.createSequentialGroup()
					.addGroup(loginLayout.createParallelGroup()
						.addComponent(username)
						.addGap(14)
						.addComponent(password)
					)
					.addGap(12)
					.addGroup(loginLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(usernameTextField)
						.addGap(14)
						.addComponent(passwordField)
						.addGap(14)
						.addComponent(btnLogIn)
					)
				)
				.addGap(14)
				.addComponent(registration)
			)
			.addGap(20)
		);

		loginLayout.setVerticalGroup(loginLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(loginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(username)
				.addGap(12)
				.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(14)
			.addGroup(loginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(password)
				.addGap(12)
				.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(14)
			.addComponent(btnLogIn)
			.addGap(14)
			.addComponent(registration)
		);

		startFrame.pack();
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setResizable(false);
		startFrame.setLocationRelativeTo(null);
		startFrame.setVisible(true);

		//selectTargetFrame
		JLabel selectUser = new JLabel("Target User");
		selectUserTextField = new JTextField(16);
		JButton btnSelectUser = new JButton("Confirm");

		selectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		selectUserTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));

		btnSelectUser.addActionListener(new selectUserListener());

		GroupLayout selectUserLayout = new GroupLayout(selectTargetFrame.getContentPane());
        selectTargetFrame.getContentPane().setLayout(selectUserLayout);

		selectUserLayout.setHorizontalGroup(selectUserLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(selectUserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addGroup(selectUserLayout.createSequentialGroup()
					.addComponent(selectUser)
					.addGap(10)
					.addComponent(selectUserTextField)
				)
				.addGap(10)
				.addComponent(btnSelectUser)
			)
			.addGap(20)
		);

		selectUserLayout.setVerticalGroup(selectUserLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(selectUserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(selectUser)
				.addGap(10)
				.addComponent(selectUserTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(10)
			.addComponent(btnSelectUser)
		);

		selectTargetFrame.pack();
		selectTargetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectTargetFrame.setResizable(false);
		selectTargetFrame.setLocationRelativeTo(null);
		selectTargetFrame.setVisible(false);

		//sendAndListenFrame
		msgToSend = new JTextField();
		msgToDisplay = new JTextArea(30, 40);

		JButton btnSendMsg = new JButton("Send");
		JButton btnReSelectUser = new JButton("Go Back");
		JScrollPane scrollPanel = new JScrollPane(msgToDisplay, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		msgToSend.setFont(new Font("Arial", Font.PLAIN, 16));
		Border textAreaBorder = BorderFactory.createLineBorder(Color.GRAY);
		msgToDisplay.setBorder(BorderFactory.createCompoundBorder(textAreaBorder, 
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		msgToDisplay.setFont(new Font("Arial", Font.PLAIN, 22));
		msgToDisplay.setLineWrap(true);
		msgToDisplay.setWrapStyleWord(true);
		btnSendMsg.setFont(new Font("Arial", Font.PLAIN, 16));
		btnReSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));

		btnSendMsg.addActionListener(new sendMsgListener());
		btnReSelectUser.addActionListener(new reSelectUserListener());

		GroupLayout sendAndListenLayout = new GroupLayout(sendAndListenFrame.getContentPane());
        sendAndListenFrame.getContentPane().setLayout(sendAndListenLayout);

		sendAndListenLayout.setHorizontalGroup(sendAndListenLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(sendAndListenLayout.createParallelGroup()
				.addComponent(scrollPanel)
				.addGap(10)
				.addGroup(sendAndListenLayout.createSequentialGroup()
					.addComponent(msgToSend)
					.addGap(10)
					.addComponent(btnSendMsg)
					.addGap(5)
					.addComponent(btnReSelectUser)
				)
			)
			.addGap(20)
		);

		sendAndListenLayout.setVerticalGroup(sendAndListenLayout.createSequentialGroup()
			.addGap(20)
			.addComponent(scrollPanel)
			.addGap(10)
			.addGroup(sendAndListenLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(msgToSend, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addGap(10)
				.addComponent(btnSendMsg)
				.addGap(5)
				.addComponent(btnReSelectUser)
			)
			.addGap(20)
		);

		sendAndListenFrame.pack();
		sendAndListenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sendAndListenFrame.setResizable(false);
		sendAndListenFrame.setLocationRelativeTo(null);
		sendAndListenFrame.setVisible(false);
	}

	class logInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.login(usernameTextField.getText(), String.valueOf(passwordField.getPassword()))) {
				client.createFileSocket();
				selectTargetFrame.setVisible(true);
				startFrame.setVisible(false);
			}
			else {//login fail
				JOptionPane.showMessageDialog(null, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class selectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.selectTarget(selectUserTextField.getText())) {
				isListening = true;
				clientlisten = new ClientListen(client, clientGUI, msgToDisplay);
				clientlisten.start();
				sendAndListenFrame.setTitle(selectUserTextField.getText());
				selectUserTextField.setText("");
				sendAndListenFrame.setVisible(true);
				selectTargetFrame.setVisible(false);
			}
			else { //target does not exist
				JOptionPane.showMessageDialog(null, "User " + selectUserTextField.getText() + " is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class sendMsgListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = msgToSend.getText();
			msgToDisplay.append("Me: " + msg + "\n");
			client.send(msg);
			msgToSend.setText("");
		}
	}

	class reSelectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListening = false;
			selectTargetFrame.setVisible(true);
			sendAndListenFrame.setVisible(false);
		}
	}
}
