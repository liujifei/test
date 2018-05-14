package pattern.observe;

import java.util.Observer;


public class Client {

    public static void main(String[] args) {

        Observabler executer = new Observabler();
        Observer observor1 = new Observor1();
        Observer observor2 = new Observor2();
        executer.addObserver(observor1);
        executer.addObserver(observor2);
        executer.actionOne();
        executer.actionTwo();
    }

}
