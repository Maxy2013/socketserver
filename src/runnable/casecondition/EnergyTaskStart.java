package runnable.casecondition;

/**
 * @author lz
 * 2018/9/26 10:22
 */
public class EnergyTaskStart {

    /**
     * ，每个盒子的初始能量值
     */
    private static final double INIT_AMOUNT = 1000;
    /**
     * 整个能量系统中所有的能量盒子数
     */
    private static final int TOTAL_BOX = 100;


    public void start(){
        //创建一个共享的能量系统
        EnergySystem energySystem = new EnergySystem(INIT_AMOUNT, TOTAL_BOX);
        for(int i = 0; i < TOTAL_BOX; i++){
            EnergyTransferTask task = new EnergyTransferTask(energySystem, i, INIT_AMOUNT);
            Thread thread = new Thread(task,"EnergyTransfer_" + i);
            thread.start();
        }

    }

    public static void main(String[] args) {
        new EnergyTaskStart().start();
    }
}
