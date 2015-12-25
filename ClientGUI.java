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
	public static Boolean isSending = false;
	Boolean isLogin = false;
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

		startFrame.addWindowListener(new closeHandler());
		usernameTextField.addActionListener(new enterlogInListener());
		passwordField.addActionListener(new enterlogInListener());
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
        startFrame.setResizable(false);
		startFrame.setLocationRelativeTo(null);
		usernameTextField.requestFocus();
		startFrame.setVisible(true);

		//selectTargetFrame
		JLabel selectUser = new JLabel("Target User");
		selectUserTextField = new JTextField(16);
		JButton btnSelectUser = new JButton("Confirm");

		selectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		selectUserTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));

		selectTargetFrame.addWindowListener(new closeHandler());
		btnSelectUser.addActionListener(new selectUserListener());
		selectUserTextField.addActionListener(new enterSelectUserListener());

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

		sendAndListenFrame.addWindowListener(new closeHandler());
		btnSendMsg.addActionListener(new sendMsgListener());
		msgToSend.addActionListener(new enterSendMsgListener());
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
        sendAndListenFrame.setResizable(false);
		sendAndListenFrame.setLocationRelativeTo(null);
		sendAndListenFrame.setVisible(false);
	}

	void doLogin() {
		if(client.login(usernameTextField.getText(), String.valueOf(passwordField.getPassword()))) {
			isLogin = true;
			client.createFileSocket();
			selectUserTextField.requestFocus();
			selectTargetFrame.setVisible(true);
			startFrame.setVisible(false);
		}
		else {//login fail
			JOptionPane.showMessageDialog(null, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void doSelectUser() {
		if(client.selectTarget(selectUserTextField.getText())) {
			isListening = true;
			clientlisten = new ClientListen(client, clientGUI, msgToDisplay, selectUserTextField.getText());
			clientlisten.start();
			sendAndListenFrame.setTitle(selectUserTextField.getText());
			selectUserTextField.setText("");
			msgToSend.requestFocus();
			sendAndListenFrame.setVisible(true);
			selectTargetFrame.setVisible(false);
		}
		else { //target does not exist
			JOptionPane.showMessageDialog(null, "User " + selectUserTextField.getText() + " is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void doSend() {
		String msg = msgToSend.getText();
		msgToDisplay.append("Me: " + msg + "\n");
		isSending = true;
		client.send(msg);
		isSending = false;
		msgToSend.requestFocus();
		msgToSend.setText("");
	}

	class closeHandler extends WindowAdapter{
		public void windowClosing(WindowEvent event) {
			if(isLogin) client.logout();
			System.out.println("client logout");
			System.exit(0);
		}
	}

	class enterlogInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doLogin();
		}
	}

	class logInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doLogin();
		}
	}

	class enterSelectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doSelectUser();
		}
	}

	class selectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doSelectUser();
		}
	}

	class enterSendMsgListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doSend();
		}
	}

	class sendMsgListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			doSend();
		}
	}

	class reSelectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListening = false;
			selectUserTextField.requestFocus();
			selectTargetFrame.setVisible(true);
			sendAndListenFrame.setVisible(false);
		}
	}
}
