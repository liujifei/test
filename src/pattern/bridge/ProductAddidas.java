package pattern.bridge;


public class ProductAddidas extends Product {

    @Override
    public void beProducted() {
        System.out.println("生产addidas");

    }

    @Override
    public void beSelled() {
        System.out.println("出售addidas");
    }

}
