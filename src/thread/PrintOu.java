package thread;


public class PrintOu implements Runnable{
    Num num;
    public PrintOu(Num num) {
        this.num = num;
    }
    public void run()
    {
        while(num.i<=100)
        {
            synchronized (num)/* 必须要用一把锁对象，这个对象是num*/ {
                if(!num.flag)
                {
                    try 
                    {
                        num.wait();  //操作wait()函数的必须和锁是同一个
                    } catch (Exception e) 
                    {}
                }   
                else {
                    System.out.println("oushu-----"+num.i);
                    num.i++;
                    num.flag = false;
                    num.notify();
                }
            }
        }
    }
}
