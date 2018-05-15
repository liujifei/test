package pattern.visitor;


public interface IEmployee {
    public void accept(Department handler);
}
