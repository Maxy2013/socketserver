package runnable;

/**
 * 1、Runnable 方式可以避免Thread方式由于java的单继承特性带来的缺陷
 * 2、Runnable 的代码可以被对个线程（Thread实例）共享，适合于多个线程处理统一资源的情况
 */


class SaleThread implements Runnable{
    /**
     * 剩余的票数
     */
    private int ticketsCount = 9;

    /**
     * 加上同步关键字[synchronized]则按顺序输出，在任何时刻都能保证只有一个线程执行锁里面的代码
     */
    @Override
    public synchronized void run() {
        //如果还有票，窗口就会卖票
        while (ticketsCount > 0){
            ticketsCount--;
            System.out.println(Thread.currentThread().getName() + "卖了1张票，还剩下" + ticketsCount + "张票。");
        }
    }
}



/**
 * @author lz
 * 2018/9/21 9:24
 */
public class TicketsRunnable {

    public static void main(String[] args) {
        SaleThread sale = new SaleThread();

        //创建线程
        Thread ht1 = new Thread(sale, "窗口1");
        Thread ht2 = new Thread(sale, "窗口2");
        Thread ht3 = new Thread(sale, "窗口3");

        ht1.start();
        ht2.start();
        ht3.start();
    }
}
