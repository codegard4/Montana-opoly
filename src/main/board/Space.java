package src.main.board;

public class Space {
    // a space could be a property, a chance card, a community chest card, a tax space
    // a "Butte", a go space, or a free parking space
    private String name;
    private int position;
    private Property property;
    private boolean isProperty;

    public Space(String name, int position){
        this(name, position, null);
    }

    public Space(String name, int position, Property property){
        this.name = name;
        this.position = position;
        this.property = property;
        this.isProperty = true;
    }

    public String getName(){
        return name;
    }

}
