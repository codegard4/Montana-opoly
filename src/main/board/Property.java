package src.main.board;

import src.main.player.Player;

/**
 * Represents a property in the game.
 * A property has attributes such as name, price, rent, owner, and number of houses.
 */
public class Property extends Space {
    /**
     * Enum representing different property classes.
     */
    public enum PropertyClass {
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
    private int houses;
    private Player owner;
    private boolean mortgaged;
    private boolean fullSetMember = false;
    private final boolean canAddHouses;

    /**
     * Constructor for standard properties.
     *
     * @param name          Name of the property.
     * @param imgfile       Image file associated with the property.
     * @param propertyClass Property class/type.
     * @param price         Purchase price of the property.
     * @param rent          Rent values based on the number of houses.
     * @param index         Index of the property on the board.
     */
    public Property(String name, String imgfile, String propertyClass, int price, int[] rent, int index) {
        super("Property", name, imgfile, index);
        this.name = name;
        this.price = price;
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0;
        this.rent = rent;
        this.owner = null;
        this.mortgaged = false;
        this.canAddHouses = true;
    }

    /**
     * Constructor for railroads. -- different because rent is based on the number of railroads owned
     *
     * @param name          Name of the railroad.
     * @param imgfile       Image file associated with the railroad.
     * @param propertyClass Property class/type.
     * @param price         Purchase price of the railroad.
     * @param rent          Rent values for railroads.
     * @param index         Index of the railroad on the board.
     */
    public Property(String name, String imgfile, String propertyClass, int price, int rent, int index) {
        super("Property", name, imgfile, index);
        this.name = name;
        this.price = price;
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0;
        this.rent = new int[]{25, 50, 75, 100}; // Railroad rent values
        this.owner = null;
        this.mortgaged = false;
        this.canAddHouses = false;
    }

    /**
     * Constructor for utilities. -- different because rent is calculated based on the dice roll
     *
     * @param name          Name of the utility.
     * @param imgfile       Image file associated with the utility.
     * @param propertyClass Property class/type.
     * @param price         Purchase price of the utility.
     * @param index         Index of the utility on the board.
     */
    public Property(String name, String imgfile, String propertyClass, int price, int index) {
        super("Property", name, imgfile, index);
        this.name = name;
        this.rent = new int[]{4, 7}; // Utility rent values
        this.price = price;
        this.color = PropertyClass.valueOf(propertyClass);
        this.houses = 0;
        this.owner = null;
        this.mortgaged = false;
        this.canAddHouses = false;
    }

    /**
     * Gets the name of the property.
     *
     * @return Property name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the purchase price of the property.
     *
     * @return Property price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the number of houses / 2
     */
    public int getHouseValue() {
        return houses * (getPrice() / 2);
    }

    /**
     * Can houses be added to this property?
     *
     * @return is it a property or utility/railroad/etc.
     */
    public boolean canAddHouses() {
        return canAddHouses;
    }

    /**
     * Checks if the property is mortgaged.
     *
     * @return True if the property is mortgaged, false otherwise.
     */
    public boolean isMortgaged() {
        return mortgaged;
    }

    /**
     * Mortgages the property.
     */
    public void mortgage() {
        mortgaged = true;
    }

    /**
     * Removes the mortgage from the property.
     */
    public void unmortgage() {
        mortgaged = false;
    }

    /**
     * Gets the current rent value of the property.
     *
     * @return Rent amount based on the number of houses or full set ownership.
     */
    public int getRent() {
        if (houses > 0) {
            return rent[houses + 1];
        } else {
            if (fullSetMember) {
                return rent[1];
            }
        }
        return rent[0];
    }

    /**
     * Gets the property class.
     *
     * @return Property class.
     */
    public PropertyClass getPropertyClass() {
        return color;
    }

    /**
     * Gets the number of houses on the property.
     *
     * @return Number of houses.
     */
    public int getHouses() {
        return houses;
    }

    /**
     * Gets the color category of the property.
     *
     * @return Property color as a string.
     */
    public String getColor() {
        return String.valueOf(color);
    }

    /**
     * Adds a house to the property.
     */
    public void addHouse() {
        houses += 1;
    }

    public int getNumHouses() {
        return houses;
    }

    /**
     * Sets whether the property belongs to a full set.
     *
     * @param b True if the property is part of a full set, false otherwise.
     */
    public void fullSet(boolean b) {
        fullSetMember = b;
    }

    /**
     * Sells a house from the property.
     */
    private void sellHouse() {
        houses -= 1;
    }

    /**
     * Gets the owner of the property.
     *
     * @return Property owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Assigns a new owner to the property.
     *
     * @param newOwner The player who purchased the property.
     */
    public void purchased(Player newOwner) {
        owner = newOwner;
    }

    /**
     * Returns a string representation of the property, including price, rent, and number of houses.
     *
     * @return Property details as a string.
     */
    @Override
    public String toString() {
        if (!this.color.equals(PropertyClass.Mountain) && !this.color.equals(PropertyClass.Utility)) {
            return name + " - Price: " + price + " - Rent: " + rent[houses] + " - Houses: " + houses;
        } else if (this.color.equals(PropertyClass.Mountain)) {
            return name + " - Price: " + price + " - Rent: Dependent on Mountain ranges owned - No houses allowed";
        }
        return name + " - Price: " + price + " - Rent: Dependent on utilities owned and dice roll - No houses allowed";
    }

    /**
     * Provides a short summary of the property.
     *
     * @return Short listing of the property with name and price.
     */
    public String shortListing() {
        return name + " - $" + price;
    }
}