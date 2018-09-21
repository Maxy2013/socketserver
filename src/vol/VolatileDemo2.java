package vol;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * volatile 不能保证程序执行的原子性
 *
     三种方法解决 this.number++ 不是原子性操作的方法
     1、synchronized
     2、locks
     3、
 * @author lz
 * 2018/9/21 15:13
 */
public class VolatileDemo2 {

    private Lock lock = new ReentrantLock();

    private int number = 0;

    public int getNUmber(){
        return this.number;
    }

    /**
     * 加上 synchronized 关键字保证代码执行的原子性
     */
    public void increase(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       /*1、 synchronized
       synchronized(this){
            //缩小锁的范围，使其成为原子性操作，加在方法上也是可以的
            this.number++;//这句代码不是原子性代码
        }*/


       //2、java.util.concurrent.locks.Lock
//        lock.lock();
//        this.number++;
//        lock.unlock();//释放锁
        //同样也可以改造成try{}finally{}代码块执行
        lock.lock();
        try {
            this.number++;
        }finally {
            lock.unlock();
        }
        /*
            可以被拆分成三个原子性代码
            1、读取number的值
            2、让number + 1
            3、写入number+1后的值
         */
        /*
            三种方法解决 this.number++ 不是原子性操作的方法
            1、synchronized
            2、java.util.concurrent.locks.Lock
            3、
         */
    }

    public static void main(String[] args) {
        VolatileDemo2 demo = new VolatileDemo2();
        for(int i = 0; i < 500; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    demo.increase();
                }
            }).start();
        }

        //如果子线程的运行个数大于1，主线程让出CPU资源，让子线程执行完，再执行主线程
        while (Thread.activeCount() > 1){
            Thread.yield();
        }
        /*
         * 结果有的时候是500，有时小于500，这说明volatile不能保证程序执行的原子性
         */
        System.out.println("number: " + demo.getNUmber());
    }

}
