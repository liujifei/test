package pattern.visitor;

public class HRDepartment extends Department {

    @Override
    public void Visit(FullTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        System.out.println("正式员工 " + employee.getName() + " 实际工作时间为：" + workTime + " 小时");

        if (workTime > 40) {
            System.out.println("正式员工 " + employee.getName() + " 加班时间为：" + (workTime - 40) + " 小时");
        } else if (workTime < 40) {
            System.out.println("正式员工 " + employee.getName() + " 请假时间为：" + (40 - workTime) + " 小时");
        }
    }

    @Override
    public void Visit(PartTimeEmployee employee) {
        int workTime = employee.getWorkTime();
        System.out.println("临时工 " + employee.getName() + " 实际工作时间为：" + workTime + " 小时");
    }

}
