package pattern.visitor;

public class FinanceDepartment extends Department {

    @Override
    public void Visit(FullTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        double weekWage = employee.getWeeklyWage();

        if (workTime > 40) {
            weekWage = weekWage + (workTime - 40) * 50;
        } else if (workTime < 40) {
            weekWage = weekWage - (40 - workTime) * 80;
            if (weekWage < 0) {
                weekWage = 0;
            }
        }

        System.out.println("正式员工 " + employee.getName() + " 实际工资为：" + weekWage + " 元");
    }

    @Override
    public void Visit(PartTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        double hourWage = employee.getHourWage();
        System.out.println("临时工 " + employee.getName() + " 实际工资为：" + workTime * hourWage + " 元");
    }

}
