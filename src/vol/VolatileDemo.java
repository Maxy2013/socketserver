package vol;

import syn.SynchronizedDemo;

/**
 * volatile 不能保证程序执行的原子性
 * @author lz
 * 2018/9/21 15:13
 */
public class VolatileDemo {

    private volatile int number = 0;

    public int getNUmber(){
        return this.number;
    }

    public void increase(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.number++;//这句代码不是原子性代码
        /*
            可以被拆分成三个原子性代码
            1、读取number的值
            2、让number + 1
            3、写入number+1后的值
         */
    }

    public static void main(String[] args) {
        VolatileDemo demo = new VolatileDemo();
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
