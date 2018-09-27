package runnable.casecondition;

/**
 * @author lz
 * 2018/9/26 9:39
 */
public class EnergySystem {

    /**
     * 能量的总和
     */
    private double[] totalEnergy;

    private static final Object lockObj = new Object();

    /**
     * 初始化总能量
     *
     * @param initEnergy 初始的能量值
     * @param n          能量的个数
     */
    public EnergySystem(double initEnergy, int n) {
        totalEnergy = new double[n];
        for (int i = 0; i < totalEnergy.length; i++) {
            totalEnergy[i] = initEnergy;
        }
    }

    /**
     * 计算总能量
     *
     * @return 返回总能量值
     */
    private double getTotalEnergy() {
        double sum = 0;
        for (double e : totalEnergy) {
            sum += e;
        }
        return sum;
    }

    /**
     * @return 返回能量盒子数
     */
    public int getTotalBox() {
        return totalEnergy.length;
    }

    /**
     * 能量的转义，从一个能量盒子转移到另一个能量盒子
     *
     * @param from   转移的开始盒子
     * @param to     接收能量的盒子
     * @param amount 转移的能量的数量
     */
    public void transfer(int from, int to, double amount) {


        //给对象加锁
        synchronized (lockObj){
            //如果from 中的能量小于需要转移的能量，直接返回结束
            //if (totalEnergy[from] < amount) return;//直接返回后，该线程还会继续申请CPU资源，增加程序的性能负担
            //应该使用while
            //while保证 在不满足条件的情况下，任务都会被阻拦，而不是继续竞争CPU的资源
            //这些阻拦的线程将在 Wait set中等待
            while (totalEnergy[from] < amount){
                try {
                    lockObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print(Thread.currentThread().getName());
            totalEnergy[from] -= amount;//从from中转移出amount的能量值
            System.out.printf("从盒子[%d]转移%10.2f单位的能量到盒子[%d]", from, amount, to);
            totalEnergy[to] += amount;//往to 中转移 amount的能量
            System.out.printf("能量的总和是：%10.2f%n", getTotalEnergy());

            //唤醒所有lockObj对象上等待的线程，让他们继续执行
            lockObj.notifyAll();
        }



    }
}
