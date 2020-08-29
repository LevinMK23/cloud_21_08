import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        URL foo = getClass().getResource("sample.fxml");
          Parent root = FXMLLoader.load(foo);
          primaryStage.setTitle("Chat");
          primaryStage.setScene(new Scene(root));
          primaryStage.show();
          primaryStage.setOnCloseRequest(event -> {
            Controller.stop();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
