package serialization;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {

    public Server() {
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ObjectEncoder(),
                                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                            new FileHandler()
                                    );
                        }
                    });
            // ObjectEncoder, ObjectDecoder
            ChannelFuture future = bootstrap.bind(8189).sync();
            System.out.println("Server started");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            auth.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server();
        FileMessage message = FileMessage.builder()
                .name("Ivan.txt")
                .data("asfasf".getBytes())
                .build();

    }
}
