package nio.homework;

/**
 * @author lz
 * 2018/9/25 14:29
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ChatRoom implements Runnable {
    private Selector selector;
    private String chatRoomName;
    private List<SocketChannel> members = new ArrayList<>();

    public ChatRoom(Selector selector, String chatRoomName) {
        this.selector = selector;
        this.chatRoomName = chatRoomName;
    }

    @Override

    public void run() {
        try {
            while (true) {
                int select = selector.select();
                System.out.println("num:" + select);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        System.out.println(socketChannel.getRemoteAddress() + "的read event");
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        if (buffer == null) {
                            int sendBufferSize = socketChannel.socket().getSendBufferSize() * 5;
                            buffer = ByteBuffer.allocate(sendBufferSize);
                        }
                        buffer.put(("\r\n" + socketChannel.getRemoteAddress() + "say:").getBytes());
                        socketChannel.read(buffer);
                        buffer.flip(); //读完毕
                        for (SocketChannel socketChannel1 : members) {
                            if (socketChannel1.equals(this)) {
                                continue;
                            }
                            socketChannel1.write(buffer);
                            buffer.compact();
                            if (buffer.hasRemaining()) {
                                System.out.println("还有" + buffer.remaining() + "没有写完");
                                selectionKey.attach(buffer);
                                selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                            }
                        }
                    } else if (selectionKey.isWritable()) {
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        int write = socketChannel.write(buffer);
                        System.out.println("writed:" + write);
                        if (buffer.hasRemaining()) {
                            System.out.println("还有" + buffer.remaining() + "没有写完");
                            buffer.compact();
                            selectionKey.attach(buffer);
                        } else {
                            System.out.println("数据全部写完");
                            buffer.compact();
                            selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
                        }
                    } iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public List<SocketChannel> getMembers() {
        return members;
    }

    public void setMembers(List<SocketChannel> members) {
        this.members = members;
    }
}
