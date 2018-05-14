package CountDownLatch;

import java.util.concurrent.CountDownLatch;


public class Driver {
    void main() throws InterruptedException { 
        int N = 10;
        CountDownLatch startSignal = new CountDownLatch(1);  
        CountDownLatch doneSignal = new CountDownLatch(N);  
 
        for (int i = 0; i < N; ++i) // create and start threads  
            new Thread(new Worker(startSignal, doneSignal, i)).start();
 
        doSomethingElse(1);            // don't let run yet  
        startSignal.countDown();      // let all threads proceed  
        doSomethingElse(2);  
        doneSignal.await();           // wait for all to finish
        System.out.println("finished");
    }
    void doSomethingElse(int i){
        System.out.println("doSomethingElse" + i);
    }
    public static void main(String[] args) {
        Driver driver = new Driver();
        try {
            driver.main();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}  
 
class Worker implements Runnable {  
    private final CountDownLatch startSignal;  
    private final CountDownLatch doneSignal;
    int i;
    Worker(CountDownLatch startSignal, CountDownLatch doneSignal, int i) {  
        this.startSignal = startSignal;  
        this.doneSignal = doneSignal;  
        this.i = i;
    }  
    public void run() {  
        try {  
            startSignal.await();  
            doWork(i);  
            doneSignal.countDown();
            doneSignal.await();
            doMyfinished(i);
        } catch (InterruptedException ex) {} // return;  
    }  
 
    void doWork(int i) {System.out.println("doWork"+i);}  
    void doMyfinished(int i) {System.out.println("doMyfinished"+i);}  
}
