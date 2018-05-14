package pattern.decorator;


public class Guest {

    public static void main(String[] args) {
        Noodle noodle;
        noodle = new DeliciousNoodle();
        noodle = new AddSalt(noodle);
        noodle = new AddPapper(noodle);
        noodle.cook();
        noodle.teast();
    }
}
