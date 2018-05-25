package current;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CyclicBarrierTest {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(8);
    public static void main(String[] args) {
        int threadNum = 4;
        CyclicBarrier barrier = new CyclicBarrier(threadNum);
        for(int i = 0; i < threadNum; i++){
            threadPool.submit(new WorkThread(barrier));
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(int i = 0; i < threadNum; i++){
            threadPool.submit(new WorkThread(barrier));
        }
    }

    public static class WorkThread implements Runnable{

        private CyclicBarrier barrier;
        public WorkThread(CyclicBarrier b){
            this.barrier = b;
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始执行阶段1");
                Thread.sleep(Math.abs(new Random().nextInt())%10000);
                System.out.println(Thread.currentThread().getName() + " 阶段1执行完毕");
                barrier.await(10000, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + " 开始执行阶段2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        
    }
}
