package thread;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Consumer implements Runnable{
    private static Log log = LogFactory.getLog(Consumer.class);

    /** 队列锁 */
    public static Lock jobLock = new ReentrantLock();
    public static Condition jobCondition = jobLock.newCondition();


    /**
     * @ClassName ExamController
     * @Description TODO(这里用一句话描述这个类的作用)
     * @author Jeffy
     * @Date 2017年8月9日 上午9:59:31
     * @email: liujifei@inspur.com
     * @version 1.0.0
     */
    @Override
    public void run() {
        for (int i = 0; i >= 0; i++) {
            log.info("1判断");
            //判断资产库剩余空间
            CheckSpace.checkLock.lock();
            try {
                log.info("1唤醒check");
                CheckSpace.needCheck = true;
                CheckSpace.checkCondition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CheckSpace.checkLock.unlock();
            }
            jobLock.lock();
            try {
                log.info("1阻塞消费");
                CheckSpace.needCheck = true;
                jobCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                jobLock.unlock();
            }
            log.info("1执行");
            CheckSpace.needCheck = false;
            Scanner in=new Scanner(System.in);  
            String str = in.nextLine();//读一行 
            log.info("开始消费");
        }
    }

    public static void main(String[] args) {
        log.info("hello");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Consumer());
        /**schedule线程池*/
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleWithFixedDelay(new CheckSpace(), 0, 2, TimeUnit.SECONDS);
//        pool.schedule(new CheckSpace(), 2, TimeUnit.SECONDS);
    }

}
