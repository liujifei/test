package pattern.decorator;


public abstract class Chef extends Noodle {

    protected Noodle food;
    public Chef(Noodle noodle){
        this.food = noodle;
    }

    public void cook(){
        this.food.cook();
    }
    
    public void teast(){
        this.food.teast();
    }
}
