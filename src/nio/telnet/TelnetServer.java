package nio.telnet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author lz
 * 2018/9/19 10:14
 */
public class TelnetServer {

    private static final Integer PORT = 8090;
    private void telnetStart(){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(PORT);
            serverSocketChannel.socket().bind(address);
            System.out.println("start at " + address);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (PORT > 0){
                int selectedNum = selector.select();
                System.out.println("The selector number is " + selectedNum);
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()){
                    SelectionKey selectionKey = iter.next();
                    if((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        socketChannel.write(ByteBuffer.wrap("Welcome to.......\r\n".getBytes()));
                        iter.remove();
                    }else if((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
                        System.out.println("Receive read event");



                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        socketChannel.read(buffer);

                        buffer = (ByteBuffer) selectionKey.attachment();
                        if (null == buffer || !buffer.hasRemaining()){
                            int sendLength = socketChannel.socket().getSendBufferSize() * 50;
                            buffer = ByteBuffer.allocate(sendLength);
                            for(int i = 0; i < buffer.capacity()-2; i++){
                                buffer.put((byte)('a' + i/25));
                            }
                            buffer.flip();
                            System.out.println("send another huge block data " + sendLength);
                        }
                        int written = socketChannel.write(buffer);
                        System.out.println("written ---->>" + written);
                        if (buffer.hasRemaining()){
                            System.out.println("bind to session, Remaining() --->>" + buffer.remaining());
                            buffer = buffer.compact();
                            selectionKey.attach(buffer);
                            selectionKey.interestOps(selectionKey.interestOps()|SelectionKey.OP_WRITE);
                        }
                        iter.remove();
                    }else if((selectionKey.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE){
                        System.out.println("Receive write event");
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        if(null != buffer){
                            int written = socketChannel.write(buffer);
                            System.out.println("written ---->>" + written);
                            if(buffer.hasRemaining()){
                                System.out.println("write not finished, but bind to session, remains " + buffer.remaining());
                                buffer.compact();
                                selectionKey.attach(buffer);
                                selectionKey.interestOps(selectionKey.interestOps()|SelectionKey.OP_WRITE);
                            }else {
                                //数据写过了之后取消写事件
                                System.out.println("block write finished");
                                selectionKey.attach(null);
                                selectionKey.interestOps(selectionKey.interestOps() &~ SelectionKey.OP_WRITE);
                            }
                        }
                        iter.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TelnetServer().telnetStart();
    }
}
