import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class Controller implements Initializable {

    public ListView<String> listView;
    public TextField text;
    public Button send;
    private Socket socket;
    private static DataInputStream is;
    private static DataOutputStream os;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private String clientPath = "client/ClientStorage";

    public static void stop() {
        try {
            os.writeUTF("quit");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(ActionEvent actionEvent) {
        String[] message = text.getText().split(": ");
        String[] fileFull = message[1].split(",");
        String fileName = fileFull[0];
        File file = new File(clientPath + "/" + fileName);

        try {
            is = new DataInputStream(new FileInputStream(file));
            os = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            byte [] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                os.flush();
            }
            System.out.println("File output stream succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.clear();
    }

    public void initialize(URL location, ResourceBundle resources) {

        text.setOnAction(this::sendFile);
        File dir = new File(clientPath);
        for (File file : dir.listFiles()) {
            listView.getItems().add(file.getName() + ", " + file.length() + " bytes");


            // Навесил обработчик клика по имени файла в списке для его передачи в поле ввода
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        text.setText("Upload file: " + selectedItem);
    });
        }
        try {
            socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
//            new Task<String>() {
//                @Override
//                protected String call() throws Exception {
//                    return is.readUTF();
//                }
//
//                @Override
//                protected void succeeded() {
//                    try {
//                        listView.getItems().add(get());
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
            new Thread(() -> {
                while (true) {
                    try {
                        String message = is.readUTF();
                        if (message.equals("quit")) {
                            break;
                        }
                        Platform.runLater(() -> listView.getItems().add(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
