package pattern.memento;

import java.util.HashMap;


public class MementoManager {

    private static final int MAX_SIZE = 10;
    private HashMap<String, IMemento> map = new HashMap<String, IMemento>(MAX_SIZE);
    public IMemento getMemento(String index){
        return this.map.get(index);
    }
    public void setMemento(String index, IMemento memento) throws OutSizeException{
        if(map.size() < MAX_SIZE){
            this.map.put(index, memento);
        } else {
            throw new OutSizeException("备忘录已满，保存状态失败");
        }
    }
}
