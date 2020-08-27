import javafx.application.Platform;
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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class Controller implements Initializable {

    public ListView<String> listView;
    public TextField text;
    public Button send;
    private Socket socket;
    private static DataInputStream is;
    private static DataOutputStream os;

    private String clientPath = "client/ClientStorage/";

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
        if (message.equals("/quit")) {
            try {
                os.writeUTF("/quit");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] commandMsg = message.split(" ");
        String command = commandMsg[0];
        String fileName = commandMsg[1];
        if (command.equals("/sendFile")) {
            File file = new File(clientPath + fileName);
            try (FileInputStream fis = new FileInputStream(file)) {
                os.writeUTF(command);
                os.writeUTF(fileName);
                os.writeLong(file.length());
                byte[] buf = new byte[256];
                int read = 0;
                while ((read = fis.read(buf)) != -1) {
                    os.write(buf, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("/downloadFile")) {
            try {
                os.writeUTF(command);
                os.writeUTF(fileName);
                String answer = is.readUTF();
                if (answer.equals("OK")) {
                    long fileLength = is.readLong();
                    File file = new File(clientPath + fileName);
                    file.createNewFile();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buf = new byte[256];
                        if (fileLength < 256) {
                            fos.write(is.read());
                        } else {
                            int read = 0;
                            for (int i = 0; i < fileLength / 256; i++) {
                                read = is.read(buf);
                                fos.write(buf, 0, read);
                            }
                        }
                    }

                } else text.setText("File not found");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            os.writeUTF(message);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.clear();
    }

    public void initialize(URL location, ResourceBundle resources) {
        text.setOnAction(actionEvent -> sendMessage(actionEvent));
        File dir = new File(clientPath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
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
//            new Thread(() -> {
//                while (true) {
//                    try {
//                        String message = is.readUTF();
//                        if (message.equals("quit")) {
//                            break;
//                        }
//                        Platform.runLater(() -> listView.getItems().add(message));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
