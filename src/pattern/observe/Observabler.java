package pattern.observe;

import java.util.Observable;

public class Observabler extends Observable implements IObservable {

    @Override
    public void actionOne() {
        System.out.println("正在执行动作1**********************");
        this.setChanged();
        this.notifyObservers("*正在执行动作1*");
    }

    @Override
    public void actionTwo() {
        System.out.println("正在执行动作2$$$$$$$$$$$$$$$$$$$$$$");
        this.setChanged();
        this.notifyObservers("$正在执行动作2$");
    }
}
