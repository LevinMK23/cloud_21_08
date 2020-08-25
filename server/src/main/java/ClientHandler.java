import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private DataInputStream is;
//    private final DataOutputStream os;
    private static FileOutputStream fos;
    private final IOServer server;
    private final Socket socket;
    private static int counter = 0;
    private final String name;
    File test;

    public ClientHandler(Socket socket, IOServer ioServer) throws IOException {
        server = ioServer;
        this.socket = socket;
        counter++;
        name = "user#" + counter;
        is = new DataInputStream(socket.getInputStream());
//        os = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client handled: ip = " + socket.getInetAddress());
        System.out.println("Nick:" + name);
    }

//    public void sendMessage(String message) throws IOException {
//        os.writeUTF(message);
//        os.flush();
//    }

    public void run() {
        test = new File("server/ServerStorage/test.txt");
        try {
            if (!test.exists()) test.createNewFile();
            is = new DataInputStream(socket.getInputStream());
            fos = new FileOutputStream(test);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                byte [] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                System.out.println("File write succeed");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {

//                String message = is.readUTF();
//                System.out.println("message from " + name + ": " + message);
//                server.broadCastMessage(message);
//                if (message.equals("quit")) {
//                    server.kick(this);
//                    os.close();
//                    is.close();
//                    socket.close();
//                    System.out.println("client " + name + " disconnected");
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }
}
