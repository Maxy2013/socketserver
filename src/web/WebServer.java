package web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    public static void main(String[] args) {
        new WebServer().start();
    }

    private ServerSocket serverSocket = null;
    private ExecutorService executorService;
    private static final Integer PORT = 8080;

    public WebServer(){
        try {
            serverSocket = new ServerSocket(PORT);

            executorService = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start(){
        try {
            while (PORT > 1){
                System.out.println("waiting。。。。。");
                Socket socket = serverSocket.accept();
                System.out.println("one connection established!....");

                //启动一个线程，处理该客户端请求
                WebClient handler =new WebClient(socket);
                //将任务交由线程池处理
                executorService.execute(handler);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
