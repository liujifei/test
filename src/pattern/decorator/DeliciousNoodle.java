package pattern.decorator;


public class DeliciousNoodle extends Noodle {

    @Override
    public void cook() {
        System.out.println("烧水");
        System.out.println("下面");
        System.out.println("熟了");
    }

    @Override
    public void teast() {
        System.out.println("觉得很好吃");
        System.out.println("推荐程度" + this.getLevel() + "颗星");
    }

}
