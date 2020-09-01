package serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, AbstractMessage abstractMessage) throws Exception {
//        ByteBuf buf = ctx.alloc().directBuffer(100500);
        if (abstractMessage instanceof FileMessage) {
            FileMessage message = (FileMessage) abstractMessage;
            if (!Files.exists(Paths.get("netty/" + message.getName()))) {
                Files.createFile(Paths.get("netty/" + message.getName()));
                Files.write(
                        Paths.get("netty/" + message.getName()),
                        message.getData(),
                        StandardOpenOption.APPEND);
            }
        }
    }
}
