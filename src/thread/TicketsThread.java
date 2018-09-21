package thread;

/**
 * @author lz
 * 2018/9/21 8:54
 */
public class TicketsThread {

    public static void main(String[] args) {
        //创建三个线程，模拟三个卖票窗口
        SaleThread sale1 = new SaleThread("窗口1");
        SaleThread sale2 = new SaleThread("窗口2");
        SaleThread sale3 = new SaleThread("窗口3");

        //启动线程
        sale1.start();
        sale2.start();
        sale3.start();
    }
}

class SaleThread extends Thread{
    /**
     * 剩余的票数
     */
    private int ticketsCount = 5;
    /**
     * 买票窗口名称
     */
    private String windowName;

    /**
     * 创建构造函数，初始化窗口名称
     * @param windowName 窗口名称
     */
    public SaleThread(String windowName){
        this.windowName = windowName;
    }

    @Override
    public synchronized void run() {
        //如果还有票，窗口就会卖票
        while (ticketsCount > 0){
            ticketsCount--;
            System.out.println(windowName + "卖了1张票，还剩下" + ticketsCount + "张票。");
        }
    }
}
