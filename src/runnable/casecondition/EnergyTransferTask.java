package runnable.casecondition;

/**
 * @author lz
 * 2018/9/26 10:09
 */
public class EnergyTransferTask implements Runnable{

    /**
     * 共享的能量世界
     */
    private EnergySystem energySystem;

    /**
     * 能量转移的源能量盒子的下标
     */
    private int fromBox;
    /**
     * 单次转移能量的最大值
     */
    private double maxAmount;

    /**
     * 最大的休眠时间
     */
    private int delay = 10;

        public EnergyTransferTask(EnergySystem energySystem, int fromBox, double maxAmount) {
        this.energySystem = energySystem;
        this.fromBox = fromBox;
        this.maxAmount = maxAmount;
    }

    @Override
    public void run() {
        //能量转移任务
        try {
            /**
             * 循环次数
             */
            int times = 20;
            while (times > 0){
                //获得随机的能量接收的盒子下标
                int toBox = (int) (energySystem.getTotalBox() * Math.random());
                //获得转移的总量
                double amount = maxAmount * Math.random();
                //执行转移
                energySystem.transfer(fromBox, toBox, amount);
                //让线程休眠一下，降低能量转移中产生的热量
                Thread.sleep((long) (delay * Math.random()));
                times--;
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
