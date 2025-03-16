package src.main.board;

public class Space {
    /*
     * Represents any space on the board, including properties and special spaces.
     */
    protected String name;
    protected SpaceType type;

    public Space(String name, SpaceType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public SpaceType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
