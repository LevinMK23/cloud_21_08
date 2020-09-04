import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void setStageSize(Stage stage, double stageWidth, double stageHeight) {
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.setMinWidth(stageWidth);
        stage.setMinHeight(stageHeight);
    }

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Файловый сервер: вход");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            Controller.stop();
            Platform.exit();
        });
        runStage(primaryStage);
    }

    public static void runStage(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
