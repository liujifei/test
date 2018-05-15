package pattern.visitor;

import java.util.ArrayList;

public class EmployeeList {

    private ArrayList<IEmployee> empList = new ArrayList<IEmployee>();

    public void AddEmployee(IEmployee emp) {
        this.empList.add(emp);
    }

    public void accept(Department handler) {
        for (IEmployee employee : empList) {
            employee.accept(handler);
        }
    }
}
