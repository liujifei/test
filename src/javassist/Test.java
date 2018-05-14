package javassist;


public class Test {
    public static void main(String[] args) {
        method();
    }
    public static void method(){
        int s = 0;
        for(int i = 0; i < 10; i++){
            s += i;
            System.out.println(i+1);
        }
        System.out.println("s:" + s);
    }
}
