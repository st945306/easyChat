/*
	reference:
	font size of a component: http://docs.oracle.com/javase/tutorial/uiswing/components/html.html

	question:
	padding of JButton

	to-do list:
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
	JFrame startFrame, registrationFrame, selectTargetFrame, createChatroomFrame, sendAndListenFrame;
	JTextField usernameTextField, r_usernameTextField, selectUserTextField, createChatroomTextField, msgToSend;
	JPasswordField passwordField, r_passwordField;
	JLabel isTargetUserOnline;
	JTextArea msgToDisplay;
	String targetUser;

	public static void main(String[] args) {
		clientGUI = new ClientGUI();
		clientGUI.go();
	}

	public void go() {
		client = new Client();
		client.createSocket();

		startFrame = new JFrame("EasyChat");
		registrationFrame = new JFrame("Registration");
		selectTargetFrame = new JFrame("Select Target");
		createChatroomFrame = new JFrame("Create Chatroom");
		sendAndListenFrame = new JFrame("");

		//startFrame
		JLabel username = new JLabel("Username");
		JLabel password = new JLabel("Password");
		usernameTextField = new JTextField(16);
		passwordField = new JPasswordField(16);
		JButton btnLogIn = new JButton("Log In");
		JLabel registration = new JLabel("Sign Up");

		username.setFont(new Font("Arial", Font.PLAIN, 16));
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnLogIn.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setCursor(new Cursor(Cursor.HAND_CURSOR));
		registration.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setForeground(Color.blue);

		startFrame.addWindowListener(new closeHandler());
		usernameTextField.addActionListener(new logInListener());
		passwordField.addActionListener(new logInListener());
		btnLogIn.addActionListener(new logInListener());
		registration.addMouseListener(new gotoRegistrationListener());

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
			.addGap(20)
		);

		startFrame.pack();
        startFrame.setResizable(false);
		startFrame.setLocationRelativeTo(null);
		usernameTextField.requestFocus();
		startFrame.setVisible(true);

		//registrationFrame
		JLabel r_username = new JLabel("Username");
		JLabel r_password = new JLabel("Password");
		r_usernameTextField = new JTextField(16);
		r_passwordField = new JPasswordField(16);
		JButton btnRegistration = new JButton("Sign Up");

		r_username.setFont(new Font("Arial", Font.PLAIN, 16));
		r_password.setFont(new Font("Arial", Font.PLAIN, 16));
		r_usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		r_passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnRegistration.setFont(new Font("Arial", Font.PLAIN, 16));

		registrationFrame.addWindowListener(new closeHandler());
		r_usernameTextField.addActionListener(new registrationListener());
		r_passwordField.addActionListener(new registrationListener());
		btnRegistration.addActionListener(new registrationListener());

        GroupLayout registrationLayout = new GroupLayout(registrationFrame.getContentPane());
        registrationFrame.getContentPane().setLayout(registrationLayout);

		registrationLayout.setHorizontalGroup(registrationLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(registrationLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(registrationLayout.createSequentialGroup()
					.addGroup(registrationLayout.createParallelGroup()
						.addComponent(r_username)
						.addGap(14)
						.addComponent(r_password)
					)
					.addGap(12)
					.addGroup(registrationLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(r_usernameTextField)
						.addGap(14)
						.addComponent(r_passwordField)
						.addGap(14)
						.addComponent(btnRegistration)
					)
				)
			)
			.addGap(20)
		);

		registrationLayout.setVerticalGroup(registrationLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(registrationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(r_username)
				.addGap(12)
				.addComponent(r_usernameTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(14)
			.addGroup(registrationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(r_password)
				.addGap(12)
				.addComponent(r_passwordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(14)
			.addComponent(btnRegistration)
			.addGap(20)
		);

		registrationFrame.pack();
        registrationFrame.setResizable(false);
		registrationFrame.setLocationRelativeTo(null);

		//selectTargetFrame
		JLabel selectUser = new JLabel("Please enter the target user");
		selectUserTextField = new JTextField(16);
		JButton btnSelectUser = new JButton("Confirm");
		JLabel gotoCreateChatroom = new JLabel("or Create a chatroom!");

		selectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		selectUserTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoCreateChatroom.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gotoCreateChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoCreateChatroom.setForeground(Color.blue);

		selectTargetFrame.addWindowListener(new closeHandler());
		btnSelectUser.addActionListener(new selectUserListener());
		selectUserTextField.addActionListener(new selectUserListener());
		gotoCreateChatroom.addMouseListener(new gotoCreateChatroomListener());

		GroupLayout selectUserLayout = new GroupLayout(selectTargetFrame.getContentPane());
        selectTargetFrame.getContentPane().setLayout(selectUserLayout);

		selectUserLayout.setHorizontalGroup(selectUserLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(selectUserLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(selectUserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addGroup(selectUserLayout.createSequentialGroup()
						.addComponent(selectUser)
						.addGap(10)
						.addComponent(selectUserTextField)
					)
					.addGap(10)
					.addComponent(btnSelectUser)
				)
				.addGap(15)
				.addComponent(gotoCreateChatroom)
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
			.addGap(15)
			.addComponent(gotoCreateChatroom)
			.addGap(20)
		);

		selectTargetFrame.pack();
        selectTargetFrame.setResizable(false);
		selectTargetFrame.setLocationRelativeTo(null);
		selectTargetFrame.setVisible(false);

		//createChatroomFrame
		JLabel createChatroom = new JLabel("Please enter the chatroom name");
		createChatroomTextField = new JTextField(16);
		JButton btnCreateChatroom = new JButton("Confirm");
		JLabel gotoSelectTarget = new JLabel("or Select a target to chat with!");

		createChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		createChatroomTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnCreateChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoSelectTarget.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gotoSelectTarget.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoSelectTarget.setForeground(Color.blue);

		createChatroomFrame.addWindowListener(new closeHandler());
		btnCreateChatroom.addActionListener(new createChatroomListener());
		createChatroomTextField.addActionListener(new createChatroomListener());
		gotoSelectTarget.addMouseListener(new gotoSelectTargetListener());

		GroupLayout createChatroomLayout = new GroupLayout(createChatroomFrame.getContentPane());
        createChatroomFrame.getContentPane().setLayout(createChatroomLayout);

		createChatroomLayout.setHorizontalGroup(createChatroomLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(createChatroomLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(createChatroomLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
					.addGroup(createChatroomLayout.createSequentialGroup()
						.addComponent(createChatroom)
						.addGap(10)
						.addComponent(createChatroomTextField)
					)
					.addGap(10)
					.addComponent(btnCreateChatroom)
				)
				.addGap(15)
				.addComponent(gotoSelectTarget)
			)
			.addGap(20)
		);

		createChatroomLayout.setVerticalGroup(createChatroomLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(createChatroomLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(createChatroom)
				.addGap(10)
				.addComponent(createChatroomTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(10)
			.addComponent(btnCreateChatroom)
			.addGap(15)
			.addComponent(gotoSelectTarget)
			.addGap(20)
		);

		createChatroomFrame.pack();
        createChatroomFrame.setResizable(false);
		createChatroomFrame.setLocationRelativeTo(null);
		createChatroomFrame.setVisible(false);

		//sendAndListenFrame
		isTargetUserOnline = new JLabel();
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
		msgToDisplay.setEditable(false);
		msgToDisplay.setFont(new Font("Arial", Font.PLAIN, 22));
		msgToDisplay.setLineWrap(true);
		msgToDisplay.setWrapStyleWord(true);
		btnSendMsg.setFont(new Font("Arial", Font.PLAIN, 16));
		btnReSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));

		sendAndListenFrame.addWindowListener(new closeHandler());
		btnSendMsg.addActionListener(new sendMsgListener());
		msgToSend.addActionListener(new sendMsgListener());
		btnReSelectUser.addActionListener(new reSelectUserListener());

		GroupLayout sendAndListenLayout = new GroupLayout(sendAndListenFrame.getContentPane());
        sendAndListenFrame.getContentPane().setLayout(sendAndListenLayout);

		sendAndListenLayout.setHorizontalGroup(sendAndListenLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(sendAndListenLayout.createParallelGroup()
				.addComponent(isTargetUserOnline)
				.addGap(10)
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
			.addComponent(isTargetUserOnline)
			.addGap(10)
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

	class closeHandler extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			if(isListening)
				isListening = false;
			if(isLogin) {
				client.logout();
				System.out.println("client logout");
			}
			System.exit(0);
		}
	}

	class gotoRegistrationListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			r_usernameTextField.requestFocus();
			registrationFrame.setVisible(true);
			startFrame.setVisible(false);
		}
	}

	class registrationListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.register(r_usernameTextField.getText(), String.valueOf(r_passwordField.getPassword()))) {
				JOptionPane.showMessageDialog(null, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
				selectTargetFrame.setVisible(true);
				registrationFrame.setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(null, "Username has been used", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class logInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
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
	}

	class selectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.selectTarget(selectUserTextField.getText())) {
				targetUser = selectUserTextField.getText();
				isListening = true;
				clientlisten = new ClientListen(client, clientGUI, msgToDisplay, targetUser);
				clientlisten.start();
				sendAndListenFrame.setTitle(targetUser);
				selectUserTextField.setText("");
				msgToSend.requestFocus();
				sendAndListenFrame.setVisible(true);
				selectTargetFrame.setVisible(false);
			}
			else { //target does not exist
				JOptionPane.showMessageDialog(null, "User " + selectUserTextField.getText() + " is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class createChatroomListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.createChatRoom(createChatroomTextField.getText()))
				System.out.println("CREATE CHAT ROOM " + createChatroomTextField.getText());
		}	
	}

	class gotoCreateChatroomListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			createChatroomFrame.setVisible(true);
			selectTargetFrame.setVisible(false);
		}
	}

	class gotoSelectTargetListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			selectTargetFrame.setVisible(true);
			createChatroomFrame.setVisible(false);
		}
	}

	class sendMsgListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = msgToSend.getText();
			if(msg != "")
				msgToDisplay.append("Me: " + msg + "\n");
			isSending = true;
			client.send(msg);
			isSending = false;
			msgToSend.requestFocus();
			msgToSend.setText("");

			//additional function: check if target is online
			//not truly implemented yet
			/*if(client.checkOnline(targetUser) == 1)
				isTargetUserOnline.setText(targetUser + " is online");
			else
				isTargetUserOnline.setText(targetUser + " is offline");*/
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
