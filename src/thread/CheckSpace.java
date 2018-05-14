package thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CheckSpace implements Runnable{
    private static Log log = LogFactory.getLog(CheckSpace.class);

    /** 检查锁 */
    public static volatile Boolean needCheck = true;
    public static Lock checkLock= new ReentrantLock();
    public static Condition checkCondition = checkLock.newCondition();

    @Override
    public void run() {
        log.info("2启动检查线程");
        checkLock.lock();
        try {
            if(needCheck){
                log.info("2唤醒check");
                checkCondition.signalAll();
            } else {
                log.info("2阻塞check");
                checkCondition.await();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            checkLock.unlock();
        }
        log.info("2检查");
        
        Integer[] input = new Integer[]{0,0};
        log.info("=====================");
        readParam(input);
        Integer i  = input[0];
        Integer j  = input[1];
        Consumer.jobLock.lock();
        try {
            if(i <= j){
                //剩余空间小于最小需要空间，需要执行检测，任务消费线程阻塞
                needCheck = true;
                log.info("2判断完毕阻塞job");
            } else {
                needCheck = false;
                log.info("2判断完毕唤醒job");
                Consumer.jobCondition.signalAll();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            Consumer.jobLock.unlock();
        }
        log.info(i);
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName, Integer[] input) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                input[line] = Integer.valueOf(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    @SuppressWarnings("resource")
    public static void readParam(Integer[] input) {
        for(int i = 0; i < 2; i++){
            Scanner in=new Scanner(System.in);  
            input[i] = Integer.valueOf(in.nextLine());//读一行 
        }
    }
}
