package nio.telnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lz
 * 2018/9/25 17:55
 */
public class Run implements Runnable{

    private Iterator<SelectionKey> iterator;

    private Selector selector;

    public Run (Iterator<SelectionKey> iterator, Selector selector){
        this.iterator = iterator;
        this.selector = selector;
    }

    @Override
    public void run(){
        try {
            SelectionKey selectionKey = iterator.next();
            if((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                socketChannel.write(ByteBuffer.wrap("Learn ByteBuffer\r\n".getBytes()));
                iterator.remove();
            }else if ((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1000);
                buffer.put("\r\nContent is: ".getBytes());
                socketChannel.read(buffer);
                buffer.put("\r\n---".getBytes());
                buffer.flip();
                socketChannel.write(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
