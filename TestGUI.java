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

public class TestGUI {
	Client client;
	JLabel systemMsg;
	int apple = 0;

	public static void main(String[] args) {
		TestGUI testGUI = new TestGUI();
		testGUI.go();
	}

	public void go() {
		client = new Client();
        client.createSocket();

		JFrame frame = new JFrame("easyChat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

		JLabel header = new JLabel("Login");
		JLabel username = new JLabel("Username");
		JLabel password = new JLabel("Password");
		JTextField usernameTextField = new JTextField(16);
		JPasswordField passwordField = new JPasswordField(16);
		JButton btnLogIn = new JButton("Log In");
		JLabel registration = new JLabel("Click me to sign up");
		systemMsg = new JLabel();

		username.setFont(new Font("Arial", Font.PLAIN, 16));
		password.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		btnLogIn.setFont(new Font("Arial", Font.PLAIN, 16));
		registration.setFont(new Font("Arial", Font.PLAIN, 16));
		systemMsg.setFont(new Font("Arial", Font.PLAIN, 16));

		btnLogIn.addActionListener(new logInListener());

        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGap(20)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
						.addComponent(username)
						.addComponent(password)
						.addComponent(systemMsg)
					)
					.addGap(15)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(usernameTextField)
						.addComponent(passwordField)
						.addComponent(btnLogIn)
					)
				)
				.addComponent(registration)
			)
			.addGap(20)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGap(20)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(username)
				.addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(10)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(password)
				.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
			)
			.addGap(10)
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(systemMsg)
				.addComponent(btnLogIn)
			)
			.addGap(10)
			.addComponent(registration)
			.addGap(15)
		);

		frame.pack();
		frame.setVisible(true);
	}

	class logInListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(client.login("Ryan", "QAQ")) {
				systemMsg.setForeground(Color.green);
				systemMsg.setText("Login success!");
			}
			else {
				systemMsg.setForeground(Color.red);
				systemMsg.setText("Wrong username or password!");
			}
		}
	}
}
