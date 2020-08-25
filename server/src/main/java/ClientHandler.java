import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final DataInputStream is;
    private final DataOutputStream os;
    private final IOServer server;
    private final Socket socket;
    private static int counter = 0;
    private final String name;
    private String clientPath = "server/ClientStorage";

    public ClientHandler(Socket socket, IOServer ioServer) throws IOException {
        server = ioServer;
        this.socket = socket;
        counter++;
        name = "user_" + counter;
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

                if (message.startsWith("/upload")) {

                    String directoryName = "server/"+name;
                    File directory = new File(directoryName);
                    if(!directory.exists()){
                        directory.mkdir();
                    }

                    String[] arr = message.split("[|]");
                    File to = new File( directoryName + "/"+arr[1].trim());
                    OutputStream osFile = new FileOutputStream(to);
                    byte [] buffer = new byte[8192]; // 8Kb
                    int count = 0;
                    while (is.available() !=0 && (count = is.read(buffer)) != -1) {
                        osFile.write(buffer, 0, count);
                    }
                    osFile.close();

                }else{

                    System.out.println("message from " + name + ": " + message);

                    server.broadCastMessage(message);
                    if (message.equals("quit")) {
                        server.kick(this);
                        os.close();
                        is.close();
                        socket.close();
                        System.out.println("client " + name + " disconnected");
                        break;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
