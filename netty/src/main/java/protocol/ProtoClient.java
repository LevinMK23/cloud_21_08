package protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ProtoClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8189)) {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            os.write("Hello world".getBytes());
            byte [] buffer = new byte[256];
            int cnt = is.read(buffer);
            for (int i = 0; i < cnt; i++) {
                System.out.print((char)buffer[i]);
            }
            System.out.println();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
