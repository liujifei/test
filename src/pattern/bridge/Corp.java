package pattern.bridge;

public class Corp {

    private Product product;

    public Corp(Product product) {
        this.product = product;
    }

    public void run() {
        product.beProducted();
        product.beSelled();
    }
}
