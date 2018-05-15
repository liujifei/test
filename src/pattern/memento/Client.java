package pattern.memento;


public class Client {

    public static void main(String[] args) {
        StateObject state = new StateObject();
        MementoManager manage = new MementoManager();
        state.setState1("1");
        state.setState2("2");
        state.setState3("3");
        System.out.println("初始状态进备忘录：" + state);
        try {
            manage.setMemento("01", state.createMemento());
        } catch (OutSizeException e) {
            System.out.println(e.getMessage());
        }

        state.setState1("4");
        state.setState2("5");
        state.setState3("6");
        try {
            manage.setMemento("02", state.createMemento());
        } catch (OutSizeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("新状态进备忘录\t状态修改为：" + state);

        state.restoreMemento(manage.getMemento("01"));
        System.out.println("状态恢复为01：" + state);
        state.restoreMemento(manage.getMemento("02"));
        System.out.println("状态恢复为02：" + state);
    }
}
