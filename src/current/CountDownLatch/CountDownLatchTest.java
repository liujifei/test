package current.CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CountDownLatchTest {
    private static final CountDownLatch countDownLatch = new CountDownLatch(2);
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.submit(new Runnable(){
            @Override
            public void run() {
                System.out.println("执行线程1");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println("线程1执行完毕");
            }
            
        });
        threadPool.submit(new Runnable(){
            @Override
            public void run() {
                System.out.println("执行线程2");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println("线程2执行完毕");
            }
            
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("全部执行完毕，main线程继续");
    }
}
