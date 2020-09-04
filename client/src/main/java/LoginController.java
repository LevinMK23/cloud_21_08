import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import jdk.tools.jlink.internal.Platform;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class LoginController {
    @FXML
    private Label infoLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button passwordButton;

    @FXML
    public void login() {
        if(checkLoginPassword(loginTextField.getText(),passwordField.getText())){
            validLogin();
        } else {
            invalidLogin();
        }
    }

    private boolean checkLoginPassword(String login, String password){
        if(login.equals("admin") && password.equals("admin")) {
            return true;
        }
        return false;
    }

    private void validLogin() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.setResizable(true);
        Main.setStageSize(Main.primaryStage, 600, 400);
        Main.primaryStage.show();
    }

    private void invalidLogin() {
        infoLabel.setText("Некорректный логин или пароль");
        infoLabel.setStyle("-fx-text-fill: red");
        passwordField.clear();
    }
}
