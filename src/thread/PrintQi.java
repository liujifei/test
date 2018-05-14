package thread;


public class PrintQi implements Runnable{
    Num num ;
    public PrintQi(Num num)
    {
        this.num = num;
    }
    public void run()
    {
        while(num.i<= 100)
        {   
            synchronized (num) {
                if(num.flag)
                {
                    try {
                        num.wait();
                    } catch (Exception e) {
                    }
                }
                else {
                    System.out.println("奇数"+num.i);
                    num.i++;
                    num.flag = true;
                    num.notify();
                }
            }
        }
    }
}
