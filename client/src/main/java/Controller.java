import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private String clientPath = "client/ClientStorage";

    public static void stop() {
        try {
            os.writeUTF("quit");
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        String message = text.getText();
        try {
            if (message.startsWith("/upload")) {
                os.writeUTF(message);
                String[] arr = message.split("[|]");
                File src = new File("client/ClientStorage/"+arr[1].trim());
                InputStream isFile = new FileInputStream(src);
                byte[] buffer = new byte[8192]; // 8Kb
                int count = 0;
                while ((count = isFile.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
                isFile.close();
            }else {
                os.writeUTF(message);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.clear();
    }

public void initialize(URL location, ResourceBundle resources) {

        text.setOnAction(this::sendMessage);
        File dir = new File(clientPath);
        for (File file : dir.listFiles()) {
            listView.getItems().add(file.getName() + "        |       " + file.length() + " bytes");
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

    public void onMousePresset(MouseEvent mouseEvent) {

        String selectedItem = listView.getSelectionModel().getSelectedItem();
        String[] arr = selectedItem.split("[|]");
        text.setText("/upload | "+ arr[0]);
    }
}
