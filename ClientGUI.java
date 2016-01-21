/*
	future work
	- cancel registration
	- cancel send file
	- the path of sending file
*/

import java.awt.*;			//Color, Cursor, Font
import java.awt.event.*;	//Listener
import javax.swing.*;		//JFrame
import javax.swing.border.Border;	//for JTextArea
import javax.swing.text.*;		//Auto-scrolling

public class ClientGUI {
	private static ClientGUI clientGUI;
	private Client client;
	private ClientListen clientListen;
	private String targetUser;	//the user who you chat with now

	JFrame startFrame, registrationFrame, 
		selectUserFrame, sendAndListenFrame, 
		createChatroomFrame, chatroomFrame;

	//startFrame
	JTextField usernameTextField;
	JPasswordField passwordField;

	//registrationFrame
	JTextField r_usernameTextField;
	JPasswordField r_passwordField;

	//selectUserFrame
	JTextField selectUserTextField;

	//sendAndListenFrame
	JTextArea msgToDisplay;
	JTextField msgToSend;
	JLabel knockKnockResult;
	
	//createChatroomFrame
	JTextField createChatroomTextField;

	//chatroomFrame
	JTextField msgToSend_chatroom;
	JLabel chatroomUserList;
	JTextArea msgToDisplay_chatroom;

	public static Boolean isListening = false;
	public static Boolean isListenLocked = false;
	public static Boolean isLogin = false;


	public static void main(String[] args) {
		clientGUI = new ClientGUI();
		clientGUI.go();
	}

	public void go() {
		//0: Connection
		client = new Client();
		client.createSocket();


		//1: Frame initialization
		startFrame = new JFrame("EasyChat");
		registrationFrame = new JFrame("Registration");
		selectUserFrame = new JFrame("Select User");
		createChatroomFrame = new JFrame("Create Chatroom");
		sendAndListenFrame = new JFrame();
		chatroomFrame = new JFrame();


		//2: startFrame
		//2.0: Declare variables (startFrame)
		JLabel username = new JLabel("Username");
		JLabel password = new JLabel("Password");
		usernameTextField = new JTextField(16);
		passwordField = new JPasswordField(16);
		JButton btnLogIn = new JButton("Log In");
		JLabel registration = new JLabel("Sign Up");

		//2.1: Setting style (startFrame)
		username.setFont(new Font("Arial", Font.PLAIN, 16));
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnLogIn.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setCursor(new Cursor(Cursor.HAND_CURSOR));
		registration.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setForeground(Color.blue);

		//2.2: Adding handlers/listeners (startFrame)
		startFrame.addWindowListener(new closeHandler());
		usernameTextField.addActionListener(new logInListener());
		passwordField.addActionListener(new logInListener());
		btnLogIn.addActionListener(new logInListener());
		registration.addMouseListener(new gotoRegistrationListener());

		//2.3: Layout setting (startFrame)
		GroupLayout loginLayout = new GroupLayout(startFrame.getContentPane());
		startFrame.getContentPane().setLayout(loginLayout);

		loginLayout.setHorizontalGroup(loginLayout.createSequentialGroup()
			.addGap(50)
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

		//2.4: Other setting of components and the frame (startFrame)
		usernameTextField.requestFocus();
		startFrame.pack();
		startFrame.setLocationRelativeTo(null);
		startFrame.setResizable(false);
		startFrame.setVisible(true);


		//3: registrationFrame
		//3.0: Declare variables (registrationFrame)
		JLabel r_username = new JLabel("Username");
		JLabel r_password = new JLabel("Password");
		r_usernameTextField = new JTextField(16);
		r_passwordField = new JPasswordField(16);
		JButton btnRegistration = new JButton("Sign Up");

		//3.1: Setting style (registrationFrame)
		r_username.setFont(new Font("Arial", Font.PLAIN, 16));
		r_password.setFont(new Font("Arial", Font.PLAIN, 16));
		r_usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		r_passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnRegistration.setFont(new Font("Arial", Font.PLAIN, 16));

		//3.2: Adding handlers/listeners (registrationFrame)
		registrationFrame.addWindowListener(new closeHandler());
		r_usernameTextField.addActionListener(new registrationListener());
		r_passwordField.addActionListener(new registrationListener());
		btnRegistration.addActionListener(new registrationListener());

		//3.3: Layout setting (registrationFrame)
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

		//3.4: Other setting of components and the frame (registrationFrame)
		registrationFrame.pack();
		registrationFrame.setLocationRelativeTo(null);
		registrationFrame.setResizable(false);
		registrationFrame.setVisible(false);


		//4: selectUserFrame
		//4.0: Declare variables (selectUserFrame)
		JLabel selectUser = new JLabel("Please enter the target user");
		selectUserTextField = new JTextField(16);
		JButton btnSelectUser = new JButton("Chat");
		JLabel gotoCreateChatroom = new JLabel("or Create a chatroom!");

		//4.1: Setting style (selectUserFrame)
		selectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		selectUserTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoCreateChatroom.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gotoCreateChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoCreateChatroom.setForeground(Color.blue);

		//4.2: Adding handlers/listeners (selectUserFrame)
		selectUserFrame.addWindowListener(new closeHandler());
		selectUserTextField.addActionListener(new selectUserListener());
		btnSelectUser.addActionListener(new selectUserListener());
		gotoCreateChatroom.addMouseListener(new gotoCreateChatroomListener());

		//4.3: Layout setting (selectUserFrame)
		GroupLayout selectUserLayout = new GroupLayout(selectUserFrame.getContentPane());
		selectUserFrame.getContentPane().setLayout(selectUserLayout);

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

		//4.4: Other setting of components and the frame (selectUserFrame)
		selectUserFrame.pack();
		selectUserFrame.setLocationRelativeTo(null);
		selectUserFrame.setResizable(false);
		selectUserFrame.setVisible(false);


		//5: createChatroomFrame
		//5.0: Declare variables (createChatroomFrame)
		JLabel createChatroom = new JLabel("Please enter the chatroom name");
		createChatroomTextField = new JTextField(16);
		JButton btnCreateChatroom = new JButton("Create");
		JLabel gotoSelectUser = new JLabel("or Select a user to chat with!");

		//5.1: Setting style (createChatroomFrame)
		createChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		createChatroomTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnCreateChatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoSelectUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gotoSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		gotoSelectUser.setForeground(Color.blue);

		//5.2: Adding handlers/listeners (createChatroomFrame)
		createChatroomFrame.addWindowListener(new closeHandler());
		createChatroomTextField.addActionListener(new createChatroomListener());
		btnCreateChatroom.addActionListener(new createChatroomListener());
		gotoSelectUser.addMouseListener(new gotoSelectUserListener());

		//5.3: Layout setting (createChatroomFrame)
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
				.addComponent(gotoSelectUser)
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
			.addComponent(gotoSelectUser)
			.addGap(20)
		);

		//5.4: Other setting of components and the frame (createChatroomFrame)
		createChatroomFrame.pack();
		createChatroomFrame.setLocationRelativeTo(null);
		createChatroomFrame.setResizable(false);
		createChatroomFrame.setVisible(false);


		//6: sendAndListenFrame
		//6.0: Declare variables (sendAndListenFrame)
		msgToDisplay = new JTextArea(15, 40);
			DefaultCaret caret = (DefaultCaret)msgToDisplay.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgToSend = new JTextField();
		JButton btnSendMsg = new JButton("Send");
		JButton btnSendFile = new JButton("Send File");
		JButton btnKnockKnock = new JButton("Knock knock");
		JButton btnReSelectUser = new JButton("Go Back");
		knockKnockResult = new JLabel();
		JScrollPane scrollPanel = new JScrollPane(msgToDisplay, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//6.1: Setting style (sendAndListenFrame)
		Border textAreaBorder = BorderFactory.createLineBorder(Color.GRAY);
		msgToDisplay.setBorder(BorderFactory.createCompoundBorder(textAreaBorder, 
		BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		msgToDisplay.setEditable(false);
		msgToDisplay.setFont(new Font("Arial", Font.PLAIN, 20));
		msgToDisplay.setLineWrap(true);
		msgToDisplay.setWrapStyleWord(true);
		msgToSend.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSendMsg.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSendFile.setFont(new Font("Arial", Font.PLAIN, 16));
		btnKnockKnock.setFont(new Font("Arial", Font.PLAIN, 16));
		btnReSelectUser.setFont(new Font("Arial", Font.PLAIN, 16));
		knockKnockResult.setFont(new Font("Arial", Font.PLAIN, 16));

		//6.2: Adding handlers/listeners (sendAndListenFrame)
		sendAndListenFrame.addWindowListener(new closeHandler());
		msgToSend.addActionListener(new sendMsgListener());
		btnSendMsg.addActionListener(new sendMsgListener());
		btnSendFile.addActionListener(new sendFileListener());
		btnKnockKnock.addActionListener(new knockKnockListener());
		btnReSelectUser.addActionListener(new reSelectUserListener());

		//6.3: Layout setting (sendAndListenFrame)
		GroupLayout sendAndListenLayout = new GroupLayout(sendAndListenFrame.getContentPane());
		sendAndListenFrame.getContentPane().setLayout(sendAndListenLayout);

		sendAndListenLayout.setHorizontalGroup(sendAndListenLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(sendAndListenLayout.createParallelGroup()
				.addComponent(scrollPanel)
				.addGap(10)
				.addGroup(sendAndListenLayout.createSequentialGroup()
					.addGroup(sendAndListenLayout.createParallelGroup()
						.addComponent(msgToSend)
						.addGap(10)
						.addComponent(knockKnockResult)
					)
					.addGap(10)
					.addGroup(sendAndListenLayout.createParallelGroup()
						.addComponent(btnSendMsg)
						.addGap(10)
						.addComponent(btnKnockKnock)
					)
					.addGap(5)
					.addGroup(sendAndListenLayout.createParallelGroup()
						.addComponent(btnSendFile)
						.addGap(10)
						.addComponent(btnReSelectUser)
					)
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
				.addComponent(btnSendFile)
			)
			.addGap(10)
			.addGroup(sendAndListenLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(knockKnockResult)
				.addGap(10)
				.addComponent(btnKnockKnock)
				.addGap(5)
				.addComponent(btnReSelectUser)
			)
			.addGap(20)
		);

		//6.4: Other setting of components and the frame (startFrame)
		sendAndListenFrame.pack();
		sendAndListenFrame.setLocationRelativeTo(null);
		sendAndListenFrame.setResizable(true);
		sendAndListenFrame.setVisible(false);


		//7: chatroomFrame
		//7.0: Declare variables (chatroomFrame)
		msgToDisplay_chatroom = new JTextArea(15, 40);
			DefaultCaret caret_chatroom = (DefaultCaret)msgToDisplay.getCaret();
			caret_chatroom.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatroomUserList = new JLabel("<html><b>Online User</b></html>");
		msgToSend_chatroom = new JTextField();
		JButton btnSendMsg_chatroom = new JButton("Send");
		JButton btnSendFile_chatroom = new JButton("Send File");
		JButton btnKnockKnock_chatroom = new JButton("Knock knock");
		JButton btnReSelectUser_chatroom = new JButton("Go Back");
		JScrollPane scrollPanel_chatroom = new JScrollPane(msgToDisplay_chatroom, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//7.1: Setting style (chatroomFrame)
		Border textAreaBorder_chatroom = BorderFactory.createLineBorder(Color.GRAY);
		chatroomUserList.setFont(new Font("Arial", Font.PLAIN, 16));
		msgToDisplay_chatroom.setBorder(BorderFactory.createCompoundBorder(textAreaBorder_chatroom, 
		BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		msgToDisplay_chatroom.setEditable(false);
		msgToDisplay_chatroom.setFont(new Font("Arial", Font.PLAIN, 20));
		msgToDisplay_chatroom.setLineWrap(true);
		msgToDisplay_chatroom.setWrapStyleWord(true);
		msgToSend_chatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSendMsg_chatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSendFile_chatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		btnKnockKnock_chatroom.setFont(new Font("Arial", Font.PLAIN, 16));
		btnReSelectUser_chatroom.setFont(new Font("Arial", Font.PLAIN, 16));

		//7.2: Adding handlers/listeners (chatroomFrame)
		chatroomFrame.addWindowListener(new closeHandler());
		msgToSend_chatroom.addActionListener(new sendMsg_chatroom_Listener());
		btnSendMsg_chatroom.addActionListener(new sendMsg_chatroom_Listener());
		btnSendFile_chatroom.addActionListener(new sendFile_chatroom_Listener());
		btnKnockKnock_chatroom.addActionListener(new knockKnock_chatroom_Listener());
		btnReSelectUser_chatroom.addActionListener(new reSelectUser_chatroom_Listener());

		//7.3: Layout setting (chatroomFrame)
		GroupLayout chatroomLayout = new GroupLayout(chatroomFrame.getContentPane());
		chatroomFrame.getContentPane().setLayout(chatroomLayout);

		chatroomLayout.setHorizontalGroup(chatroomLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(chatroomLayout.createParallelGroup()
				.addGroup(chatroomLayout.createSequentialGroup()
					.addComponent(scrollPanel_chatroom)
					.addGap(10)
					.addComponent(chatroomUserList)
				)
				.addGap(10)
				.addGroup(chatroomLayout.createSequentialGroup()
					.addComponent(msgToSend_chatroom)
					.addGap(10)
					.addComponent(btnSendMsg_chatroom)
					.addGap(5)
					.addComponent(btnSendFile_chatroom)
					.addGap(5)
					.addComponent(btnKnockKnock_chatroom)
					.addGap(5)
					.addComponent(btnReSelectUser_chatroom)
				)
			)
			.addGap(20)
		);

		chatroomLayout.setVerticalGroup(chatroomLayout.createSequentialGroup()
			.addGap(20)
			.addGroup(chatroomLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(scrollPanel_chatroom)
				.addGap(10)
				.addComponent(chatroomUserList)
			)
			.addGap(10)
			.addGroup(chatroomLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(msgToSend_chatroom, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
				.addGap(10)
				.addComponent(btnSendMsg_chatroom)
				.addGap(5)
				.addComponent(btnSendFile_chatroom)
				.addGap(5)
				.addComponent(btnKnockKnock_chatroom)
				.addGap(5)
				.addComponent(btnReSelectUser_chatroom)
			)
			.addGap(20)
		);

		//7.4: Other setting of components and the frame (chatroomFrame)
		chatroomFrame.pack();
		chatroomFrame.setLocationRelativeTo(null);
		chatroomFrame.setResizable(true);
		chatroomFrame.setVisible(false);
	}


	//Listener Part
	class closeHandler extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			System.out.println("User closed the GUI window");
			if(sendAndListenFrame.isVisible())
				client.store(msgToDisplay.getText());
			if(chatroomFrame.isVisible())
				client.store(msgToDisplay_chatroom.getText());
			
			isListening = false;
			if(isLogin) {
				client.logout();
				System.out.println("Client logout due to window closed");
			}
			System.exit(0);
		}
	}

	class logInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.login(usernameTextField.getText(), String.valueOf(passwordField.getPassword()))) {
				isLogin = true;
				client.createFileSocket();
				selectUserTextField.setText("");
				selectUserTextField.requestFocus();
				selectUserFrame.setVisible(true);
				startFrame.setVisible(false);
			}
			else {//login fail
				JOptionPane.showMessageDialog(null, "Wrong username or password!", "Error", JOptionPane.ERROR_MESSAGE);
			}
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
				selectUserTextField.setText("");
				selectUserTextField.requestFocus();
				selectUserFrame.setVisible(true);
				registrationFrame.setVisible(false);
			}
			else {
				JOptionPane.showMessageDialog(null, "The username has been used!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class selectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.selectTarget(selectUserTextField.getText())) {
					targetUser = selectUserTextField.getText();

					msgToDisplay.setText(client.restore());
					isListening = true;
					clientListen = new ClientListen(client, clientGUI, msgToDisplay);
					clientListen.start();

					msgToSend.setText("");
					msgToSend.requestFocus();
					sendAndListenFrame.setTitle(targetUser);
					sendAndListenFrame.setVisible(true);
					selectUserFrame.setVisible(false);
			}
			else {//target does not exist
				JOptionPane.showMessageDialog(null, "User " + selectUserTextField.getText() + " is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class gotoCreateChatroomListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			createChatroomTextField.setText("");
			createChatroomTextField.requestFocus();
			createChatroomFrame.setVisible(true);
			selectUserFrame.setVisible(false);
		}
	}

	class createChatroomListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			boolean temp1 = client.createChatRoom(createChatroomTextField.getText());
			boolean temp2 = client.enterChatRoom(createChatroomTextField.getText());

			msgToDisplay_chatroom.setText(client.restore());
			isListening = true;
			clientListen = new ClientListen(client, clientGUI, msgToDisplay_chatroom);
			clientListen.start();

			msgToSend_chatroom.setText("");
			msgToSend_chatroom.requestFocus();
			chatroomFrame.setTitle(createChatroomTextField.getText());
			chatroomFrame.setVisible(true);
			createChatroomFrame.setVisible(false);
		}	
	}

	class gotoSelectUserListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			selectUserTextField.setText("");
			selectUserTextField.requestFocus();
			selectUserFrame.setVisible(true);
			createChatroomFrame.setVisible(false);
		}
	}

	class sendMsgListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = msgToSend.getText();
			if(msg != "") {
				msgToDisplay.append("Me: " + msg + "\n");
				isListenLocked = true;
				client.send(msg);
				isListenLocked = false;
				msgToSend.setText("");
				msgToSend.requestFocus();
			}
		}
	}

	class sendFileListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			FileDialog fd = new FileDialog(sendAndListenFrame, "Choose a file", FileDialog.LOAD);
			fd.setVisible(true);
			if(fd != null) {
				isListenLocked = true;
				client.sendFile(fd.getFile());
				isListenLocked = false;
				msgToDisplay.append("[System Message] You have sent a file!\n");
			}
		}
	}

	class knockKnockListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListenLocked = true;
			System.out.println("DEBUG <" + targetUser + "> ");
			if(client.checkOnline(targetUser) == 1) {
				knockKnockResult.setForeground(Color.red);
				knockKnockResult.setText(targetUser + " is online");;
			}
			else {
				knockKnockResult.setForeground(Color.gray);
				knockKnockResult.setText(targetUser + " is offline");
			}
			isListenLocked = false;
		}
	}

	class reSelectUserListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListening = false;
			client.store(msgToDisplay.getText());
			selectUserTextField.setText("");
			selectUserTextField.requestFocus();
			selectUserFrame.setVisible(true);
			sendAndListenFrame.setVisible(false);
		}
	}

	class sendMsg_chatroom_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String msg = msgToSend_chatroom.getText();
			if(msg != "") {
				msgToDisplay_chatroom.append("Me: " + msg + "\n");
				isListenLocked = true;
				client.send(msg);
				isListenLocked = false;
				msgToSend_chatroom.setText("");
				msgToSend_chatroom.requestFocus();
			}
		}
	}

	class sendFile_chatroom_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			FileDialog fd = new FileDialog(chatroomFrame, "Choose a file", FileDialog.LOAD);
			fd.setVisible(true);
			if(fd != null) {
				isListenLocked = true;
				client.sendFile(fd.getFile());
				isListenLocked = false;
				msgToDisplay_chatroom.append("[System Message] You have sent a file!\n");
			}
		}
	}

	class knockKnock_chatroom_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListenLocked = true;
			String[] member = client.getChatRoomMember();
			chatroomUserList.setText("<html><b>Online User</b><br>" + String.join("<br>", member) + "</html>");
			isListenLocked = false;
		}
	}

	class reSelectUser_chatroom_Listener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			isListening = false;
			client.store(msgToDisplay_chatroom.getText());
			createChatroomTextField.setText("");
			createChatroomTextField.requestFocus();
			createChatroomFrame.setVisible(true);
			chatroomFrame.setVisible(false);
		}
	}
}