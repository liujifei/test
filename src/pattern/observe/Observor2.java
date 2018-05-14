package pattern.observe;

import java.util.Observable;
import java.util.Observer;


public class Observor2 implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Observor2接收到：" + arg.toString());
    }
}
