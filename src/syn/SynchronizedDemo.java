package syn;

/**
 * synchronized 共享变量的可见性
 * @author lz
 * 2018/9/21 14:30
 */
public class SynchronizedDemo {

    //定义共享变量
    private boolean ready = false;
    private int num = 1;
    private int result = 0;

    /**
     * 定义写操作
     * 添加 synchronized，防止程序在运行的时候代码重排序
     * 让共享变量及时更新到主内存中，使其他线程能及时看到更新后的值
     */
    public synchronized void write(){
        ready = true;
        num = 2;
    }

    /**
     * 定义读操作
     */
    public synchronized void read(){
        if(ready){
            result = num * 3;
        }
        System.out.println("result 的值为：" + result);
    }

    /**
     * 内部线程类
     */

    private class ReadWriteThread extends Thread{
        //根据构造方法传入flag参数
        private boolean flag;

        public ReadWriteThread(boolean flag){
            this.flag = flag;
        }

        @Override
        public void run() {
            if(flag){
                write();
            }else{
                read();
            }
        }
    }


    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();
        //启动线程的写操作
        demo.new ReadWriteThread(true).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动线程的读操作
        demo.new ReadWriteThread(false).start();
    }
}
