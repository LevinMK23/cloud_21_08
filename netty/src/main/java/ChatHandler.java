import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.nio.file.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatHandler extends SimpleChannelInboundHandler<String> {

    private static final ConcurrentLinkedDeque<SocketChannel> channels
            = new ConcurrentLinkedDeque<>();
    private static int cnt = 0;
    private String userName = "user#";
    private Callback callback;

    public ChatHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // client accepted!
        cnt++;
        userName += cnt;
        System.out.printf("Client %s connected\n", userName);
        channels.add((SocketChannel) ctx.channel());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
        // impl
        System.out.printf("Received message from %s: %s\n", userName, s);
        callback.call(s);
        String [] data = s.split(" ");
        Path path = Paths.get("netty/" + data[0]);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(Paths.get("netty/" + data[0]), data[1].getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("File with name: " + data[0] + " created!");
        channels
                //.filter(channel -> !channel.equals(ctx.channel()))
                .forEach(channel -> channel.writeAndFlush(String.format("[%s]: %s", userName, s)));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("Client %s disconnected\n", userName);
        channels.remove((SocketChannel) ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
