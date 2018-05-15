package pattern.memento;

import java.util.HashMap;

import pattern.memento.StateObject.Memento;


public class MementoManager {

    private static final int MAX_SIZE = 10;
    private HashMap<String, Memento> map = new HashMap<String, Memento>(MAX_SIZE);
    public Memento getMemento(String index){
        return this.map.get(index);
    }
    public void setMemento(String index, Memento memento) throws OutSizeException{
        if(map.size() < MAX_SIZE){
            this.map.put(index, memento);
        } else {
            throw new OutSizeException("备忘录已满，保存状态失败");
        }
    }
}
