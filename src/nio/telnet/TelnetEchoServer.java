package nio.telnet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lz
 * 2018/9/25 17:20
 */
public class TelnetEchoServer {

    public static final Integer PORT = 8089;

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open();) {
            serverSocketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(PORT);
            serverSocketChannel.socket().bind(address);
            System.out.println("启动~~~~>>:" + address);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (PORT > 0){
                int selectNum = selector.select();
                System.out.println("Select Number is:" + selectNum);

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    Run run = new Run(iterator, selector);
                    threadPool.submit(run);
                }
            }
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        new TelnetEchoServer().start();
    }
}
