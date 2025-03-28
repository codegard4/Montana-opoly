package src.main.board;
import src.main.player.Player;

public class Property extends Space {
    /*
     * Constructor for the Property card.
     * A property card has the property's name, price, rent, owner and number of houses.
     */
    private enum PropertyClass {
        Brown,
        LightBlue,
        Magenta,
        Orange,
        Red,
        Yellow,
        Green,
        Blue,
        Mountain,
        Utility
    }

    private PropertyClass color;
    
    private String name; // name of the property
    private int price; // price to buy the property
    private int[] rent; // rent for each of the house numbers for the property
    private int rrRent;
    private int houses; 
    private Player owner;
    private boolean mortgaged;
    private int[][] playerPlaces;
    // private JPanel labelHolder;
    // private JLabel label;
    // private final static int LABEL_WIDTH = 75;
    // private final static int LABEL_HEIGHT = 30;
    // private final int labelX;
    // private final int labelY;

    /*
     * Costructor for standard properties
     */
    public Property(String name, String imgfile, String propertyClass, int price, int[] rent, int index){
        super("Property", name, imgfile, index); // define a Space instance
        this.name = name;
        this.price = price; // set the property price
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0; // initially all properties have 0 houses
        this.rent = rent; 
        this.owner = null;
        this.mortgaged = false; // initially all properties are not mortgaged
    }

    /*
     * Constructor for Railroads
    */
    public Property(String name, String imgfile, String propertyClass, int price, int rent, int index){
        super("Property", name, imgfile, index); // define a Space instance
        this.name = name;
        this.price = price; // set the property price
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0; // initially all properties have 0 houses
        this.rrRent = rent; 
        this.owner = null;
        this.mortgaged = false; // initially all properties are not mortgaged
    }

    /*
     * Constructor for Utilties
     */
    public Property(String name, String imgfile, String propertyClass, int price, int index){
        super("Property", name, imgfile, index); // define a Space instance
        this.name = name;
        this.price = price; // set the property price
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0; // initially all properties have 0 houses 
        this.owner = null;
        this.mortgaged = false; // initially all properties are not mortgaged
    }

    public String getName(){
        /*
         * Returns the name of the property.
         */
        return name;
    }
    public int getPrice(){
        /*
         * Returns the price of the property.
         */
        return price;
    }
    public int getRent(){
        /*
         * Returns the rent of the property.
         */
        return rent[houses];
    }
    public int getHouses(){
        /*
         * Returns the number of houses on the property.
         */
        return houses;
    }
    public void addHouse(){
        /*
         * Adds a house to the property.
         */
        houses += 1;
    }
    private void sellHouse(){
        /*
         * Removes a house from the property.
         */
        houses -= 1;
    }
    public Player getOwner(){
        /*
         * Returns the owner of the property.
         */
        return owner;
    }
    public void purchased(Player newOwner){
        /*
         * Sets the owner of the property to the player.
         */
        owner = newOwner;
    }

    @Override
    public String toString(){
        if (!this.color.equals(PropertyClass.Mountain) && !this.color.equals(PropertyClass.Utility)){
            return name + " - Price: " + price + " - Rent: " + rent[houses] + " - Houses: " + houses;
        }
        else {
            if (this.color.equals(PropertyClass.Mountain)) {
                return name + " - Price: " + price + " - Rent: Dependent upon how many Mountain ranges are owned"  + " - Houses cannot be placed on this property";
            }
        }
        return name + " - Price: " + price + " - Rent: Dependent upon how many utilities are owned and the roll of the dice"  + " - Houses cannot be placed on this property";
    }

}
