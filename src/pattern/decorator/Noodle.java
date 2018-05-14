package pattern.decorator;

public abstract class Noodle {

    protected static Integer level = 0;

    public abstract void cook();

    public abstract void teast();

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
    }

    public void levelDown() {
        level--;
    }
}
