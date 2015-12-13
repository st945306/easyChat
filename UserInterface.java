import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UserInterface extends Application {

    static Text testText = new Text("listen test");
    Client client;
    TextField userNameTextField;
    PasswordField passwordField;
    Text sysMsg;

    @Override
    public void start(Stage primaryStage) {
        client = new Client();
        client.createSocket();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login");
        sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        grid.add(sceneTitle, 0, 0, 3, 1);

        Label userName = new Label("User Name");
        userName.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        grid.add(userName, 0, 1);

        userNameTextField = new TextField();
        grid.add(userNameTextField, 1, 1, 2, 1);

        Label password = new Label("Password");
        password.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        grid.add(password, 0, 2);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2, 2, 1);

        Button btnLogin = new Button("Login");
        Button btnNewAccount = new Button("Login");
        HBox hbBtnLogin = new HBox();
        hbBtnLogin.setAlignment(Pos.TOP_RIGHT);
        hbBtnLogin.getChildren().add(btnLogin);
        grid.add(hbBtnLogin, 2, 3);

        sysMsg = new Text();
        sysMsg.setFill(Color.FIREBRICK);
        HBox hbSysMsg = new HBox();
        hbSysMsg.setAlignment(Pos.CENTER);
        hbSysMsg.getChildren().add(sysMsg);
        grid.add(hbSysMsg, 1, 3);

        grid.add(testText, 0, 4, 3, 1);

        btnLogin.setOnAction(new EventHandler() {
            @Override
            public void handle(Event e) {
                checkLogin();
            }
        });

        userNameTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    checkLogin();
                }
            }
        });

        Scene scene = new Scene(grid, 300, 240);
        primaryStage.setScene(scene);
        primaryStage.show();

        Listen listen = new Listen(null);
        listen.init(testText);
    }

    void checkLogin() {
        String userNameValue = userNameTextField.getText();
        String passwordValue = passwordField.getText();

        String result = "Fail";
        if (client.login(userNameValue, passwordValue)) {
            result = "Success";
        }

        sysMsg.setText(result);
    }
}
