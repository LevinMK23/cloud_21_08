import java.io.*;
import java.util.UUID;

public class FileReadWrite {
    // File
    // InputStream -> потоки данных
    // OutputStream -> потоки данных
    // Reader, Writer
    // буферизация
    static final int CHUNK_SIZE = 8192;

    public static String genToken(){
        return UUID.randomUUID().toString();
    }


    public static void sendRemote(String sourceFilePath, DataOutputStream os) throws  IOException{
        File src  = new File(sourceFilePath);
        InputStream is = new FileInputStream(src);
        String token = genToken();
        os.writeUTF("/c file start " + token);
        byte [] buffer = new byte[CHUNK_SIZE]; // 8Kb
        int count;
        while ((count = is.read(buffer)) != -1) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
    }

    public static void readRemote(String message, DataInputStream is) throws IOException {
        String[]  s = message.split(" ");
        File to = new File("common/src/main/java/"+s[3]+".txt");
        if (!to.exists()) to.createNewFile();
        OutputStream os = new FileOutputStream(to);
        byte [] buffer = new byte[CHUNK_SIZE]; // 8Kb
        int count;
        while ((count = is.read(buffer)) != -1) {
            os.write(buffer, 0, count);
            os.flush();
        }
        os.close();
    }
}
