package protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyProtocolClientNetwork implements Runnable{

    public static SocketChannel channel;

    @Override
    public void run() {
        EventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    channel = socketChannel;
                                    socketChannel.pipeline().addLast(
                                            new SimpleChannelInboundHandler<ByteBuf>() {

                                                @Override
                                                public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                                    ctx.writeAndFlush(
                                                            ByteBufAllocator
                                                                    .DEFAULT
                                                                    .directBuffer(256)
                                                                    .writeBytes("Hello world".getBytes())
                                                    );

                                                }

                                                @Override
                                                protected void messageReceived(
                                                        ChannelHandlerContext channelHandlerContext,
                                                        ByteBuf byteBuf) throws Exception {
                                                    byte[] data = new byte[byteBuf.readableBytes()];
                                                    System.out.println(byteBuf);
                                                    byteBuf.readBytes(data);
                                                    for (byte datum : data) {
                                                        System.out.print((char) datum);
                                                    }
                                                    System.out.println();
                                                }
                                            }
                                    );
                                }
                            }
                    );

            ChannelFuture f = bootstrap
                    .connect("localhost", 8189)
                    .sync();

            f.channel().closeFuture().sync(); // block
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
