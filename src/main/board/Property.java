package src.main.board;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.main.player.Player;

public class Property extends Space{
    /*
     * Constructor for the Property card.
     * A property card has the property's name, price, rent, owner and number of houses.
     */
    private String name; // name of the property
    private int price; // price to buy the property
    private int[] rent; // rent for each of the house numbers for the property
    private int houses; 
    private Player owner;
    private boolean mortgaged;
    private int[][] playerPlaces;
    private JPanel labelHolder;
    private JLabel label;
    private final static int LABEL_WIDTH = 75;
    private final static int LABEL_HEIGHT = 30;
    private final int labelX = -1; // TODO: change
    private final int labelY = -1; // TODO: change

    public Property(String name, int price, int[] rent) {
        super(name, SpaceType.PROPERTY);  // Calls Space constructor
        this.price = price;
        this.rent = rent;
        this.houses = 0;
        this.owner = null;
        this.mortgaged = false;
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
        this.owner = newOwner;
    }

     @Override
    public String toString() {
        return super.toString() + " - Price: " + price + " - Rent: " + rent[houses] + " - Houses: " + houses;
    }

}
