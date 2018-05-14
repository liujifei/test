package pattern.decorator;


public class AddSalt extends Chef {

    public AddSalt(Noodle noodle) {
        super(noodle);
    }

    private void addSalt(){
        System.out.println("有咸味了");
        food.levelUp();
    }

    @Override
    public void cook(){
        super.cook();
        this.addSalt();
    }
}
