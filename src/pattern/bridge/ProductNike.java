package pattern.bridge;


public class ProductNike extends Product {

    @Override
    public void beProducted() {
        System.out.println("生产nike");

    }

    @Override
    public void beSelled() {
        System.out.println("出售nike");
    }

}
