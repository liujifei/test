package pattern.bridge;


public class Client {

    public static void main(String[] args) {
//        Corp corp = new Corp(new ProductAddidas());
        Corp corp = new Corp(new ProductNike());
        corp.run();
    }

}
