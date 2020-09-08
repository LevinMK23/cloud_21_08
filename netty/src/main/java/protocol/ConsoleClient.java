package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.FileRegion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleClient {
    public static void main(String[] args) throws IOException {
        new Thread(new NettyProtocolClientNetwork()).start();
        Scanner in = new Scanner(System.in);
        String name = in.next();
        File file = new File("netty/" + name);
        int nameLen = name.length();
        long fileLen = file.length();
        FileInputStream is = new FileInputStream(new File(name));
        // req byte 35 name_len, name, file_len, file_bytes
        // 1b + 4b + 20b + 8b -> 40b
        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(1 + 4 + nameLen + 8);
        buffer.writeByte(35);
        buffer.writeInt(nameLen);
        buffer.writeBytes(name.getBytes());
        buffer.writeLong(fileLen);
        while (is.available() > 0) {


        }
    }
}
