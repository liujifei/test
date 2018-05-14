package pattern.decorator;


public class AddPapper extends Chef{

    public AddPapper(Noodle noodle) {
        super(noodle);
    }

    private void addPapper(){
        System.out.println("有辣味了");
        food.levelUp();
    }

    @Override
    public void cook(){
        super.cook();
        this.addPapper();
    }
}
