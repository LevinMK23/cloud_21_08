package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

public class ProtocolHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        while (byteBuf.isReadable()) {
            byte cur = byteBuf.readByte();
            System.out.print((char) cur);
        }
        //byteBuf.release();
        byteBuf = byteBuf.resetReaderIndex();
        System.out.println();
        byteBuf.retain();
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection handled");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
