


public class Property {
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

    public Property(String name, int price, int[] rent){
        this.name = name; // set the property name
        this.price = price; // set the property price
        this.houses = 0; // initially all properties have 0 houses
        this.rent = rent; 
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
        return name + " - Price: " + price + " - Rent: " + rent[houses] + " - Houses: " + houses;
    }

}
