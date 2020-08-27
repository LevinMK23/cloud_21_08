import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final DataInputStream is;
    private final DataOutputStream os;
    private final IOServer server;
    private final Socket socket;
    private static int counter = 0;
    private final String name;
    private String serverPath = "server/ServerStorage/";

    public ClientHandler(Socket socket, IOServer ioServer) throws IOException {
        server = ioServer;
        this.socket = socket;
        counter++;
        name = "user#" + counter;
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client handled: ip = " + socket.getInetAddress());
        System.out.println("Nick:" + name);
    }

    public void sendMessage(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }

    public void run() {
        while (true) {
            try {
                String message = is.readUTF();
                if (message.equals("quit")) {
                    server.kick(this);
                    os.close();
                    is.close();
                    socket.close();
                    System.out.println("client " + name + " disconnected");
                    break;
                }
                if (message.equals("/sendFile")) {
                    String fileName = is.readUTF();
                    long fileLength = is.readLong();
                    File file = new File(serverPath + fileName);
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
                } else {
                    String fileName = is.readUTF();
                    File file = new File(serverPath + fileName);
                    if (file.exists()) {
                        os.writeUTF("Ok");
                    }
                    try (FileInputStream fis = new FileInputStream(file)) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
