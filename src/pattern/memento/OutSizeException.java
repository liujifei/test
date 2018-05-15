package pattern.memento;


public class OutSizeException extends Exception {

    public OutSizeException() {
        super("容器已满");
    }
    public OutSizeException(String message) {
        super(message);
    }

    /**
     * @Field @serialVersionUID : TODO
     */
    private static final long serialVersionUID = 455143118984578028L;

}
