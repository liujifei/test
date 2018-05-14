package thread;


public class Main {

    public static void main(String[] args) {
        Num num = new Num(); //声明一个资源

        PrintQi pQi = new PrintQi(num);
        PrintOu pOu = new PrintOu(num);

        Thread aThread = new Thread(pQi);
        Thread bThread = new Thread(pOu);

        aThread.start();
        bThread.start();

    }

}
