package pattern.visitor;


public abstract class Department {
    public abstract void Visit(FullTimeEmployee employee);
    public abstract void Visit(PartTimeEmployee employee);
}
