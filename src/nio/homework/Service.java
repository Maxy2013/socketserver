package nio.homework;

/**
 * @author lz
 * 2018/9/25 14:28
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 */

public class Service {
    private static int ACCEPT = SelectionKey.OP_ACCEPT;
    private static int READ = SelectionKey.OP_READ;
    private static int WRITE = SelectionKey.OP_WRITE;
    private static int CONNECT = SelectionKey.OP_CONNECT;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static int chatRoomSize = 3;
    private static int chatRoomCount = 0;
    private static Selector messageSelector;

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); Selector selector = Selector.open();) {
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9000);
            serverSocketChannel.socket().bind(inetSocketAddress);
            serverSocketChannel.register(selector, ACCEPT);
            int count = 0;
            String chatRoomName = "";
            ChatRoom chatRoom = null;
            while (chatRoomSize > 0) {
                if (selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = serverChannel.accept();
                            System.out.println("新的连接!" + socketChannel);
                            socketChannel.configureBlocking(false);
                            if (count % chatRoomSize == 0) {
                                chatRoomCount++;
                                messageSelector = Selector.open();
                                chatRoomName = "chatRoom" + chatRoomCount;
                                socketChannel.write(ByteBuffer.wrap((socketChannel.getRemoteAddress() + "welcome to " + chatRoomName).getBytes()));
                                socketChannel.register(messageSelector, READ);
                                System.out.println("新的聊天室！" + chatRoomName);
                                chatRoom = new ChatRoom(messageSelector, chatRoomName);
                                threadPool.submit(chatRoom);
                            } else {
                                socketChannel.write(ByteBuffer.wrap((socketChannel.getRemoteAddress() + "welcome to " + chatRoomName).getBytes()));
                                socketChannel.register(messageSelector, READ);
                            }
                            chatRoom.getMembers().add(socketChannel);
                            count++;
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Hello World!");
    }
}
