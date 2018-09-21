package runnable.daemonthread;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

/**
 * 守护线程
 */
class Daemon implements Runnable{

    @Override
    public void run() {
        System.out.println("进入守护线程" + Thread.currentThread().getName());

        //向文件中写入内容
        try {
            writeContentTofile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果守护线程正常退出，将会执行输出操作
        System.out.println("退出守护线程" + Thread.currentThread().getName());
    }

    private void writeContentTofile() throws Exception{
        File file = new File("resource" + File.separator + "word.txt");
        FileOutputStream fos = new FileOutputStream(file, true);
        int count = 0;
        while (count < 999){
            fos.write(("\r\nword" + count).getBytes());
            System.out.println("守护线程向文件中写入了" + ("word" + count++));
            Thread.sleep(1000);
        }
    }
}

/**
 * @author lz
 * 2018/9/21 10:01
 */
public class DaemonThread {

    public static void main(String[] args) {
        System.out.println("----->>进入了主线程");

        //创建线程
        Daemon daemon = new Daemon();
        Thread thread = new Thread(daemon, "daemon");
        //将线程设置成守护线程，一定要在程序启动之前设置
        thread.setDaemon(true);
        thread.start();

        //当主线程监控到控制台输入的内容，主线程结束
        Scanner scanner = new Scanner(System.in);
        //线程阻塞，接收到键盘的输入则会结束主线程，主线程结束，守护线程也将消亡
        scanner.next();

        //监控到输入，主线程结束
        System.out.println("----->>主线程结束");
    }
}
