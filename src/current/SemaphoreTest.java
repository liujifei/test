package current;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SemaphoreTest {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(8);
    public static void main(String[] args) {
        int threadNum = 9;
        int resourceNum = 5;
        Semaphore semaphore = new Semaphore(resourceNum);
        for(int i = 0; i < threadNum; i++){
            threadPool.submit(new WorkThread(semaphore));
        }
    }

    public static class WorkThread implements Runnable{

        private Semaphore semaphore;
        public WorkThread(Semaphore s){
            this.semaphore = s;
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始占用，资源剩余量" + semaphore.availablePermits());
                semaphore.acquire();
                Thread.sleep(Math.abs(new Random().nextInt())%10000);
                semaphore.release();
                System.out.println(Thread.currentThread().getName() + " 资源释放完毕，剩余量" + semaphore.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
