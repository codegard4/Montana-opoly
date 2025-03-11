package src.main.player;
import java.util.ArrayList;
import java.util.List;

import src.main.board.Property;

public class Wallet{
    /*
     * A player's wallet contains their money and properties.
     */
    private int money;
    private List<Property> properties;


    public Wallet(int startingMoney){
        /*
         * Constructor for the Wallet.
         * A wallet is initialized with 1500 money and an empty list of properties.
         */
        this.money = startingMoney;
        this.properties = new ArrayList<Property>();
    }

    public int getMoney(){
        /*
         * Returns the amount of money in the wallet.
         */
        return money;
    }
    public void addMoney(int amount){
        /*
         * Adds money to the wallet.
         */
        money += amount;
    }
    public void removeMoney(int amount){
        /*
         * Removes money from the wallet.
         */
        money -= amount;
    }
    public List<Property> getProperties(){
        /*
         * Returns the list of properties in the wallet.
         */
        return properties;
    }

    /**
     * gets the total value of all properties in the wallet
     * @return
     */
    public int getPropertyValue(){
        /*
         * Returns the total value of all properties in the wallet.
         */
        int totalValue = 0;
        for (Property property : properties){
            totalValue += property.getPrice(); // TODO: Implement getPrice() in Property.java
        }
        return totalValue;
    }
    public boolean addProperty(Property property){
        /*
         * Adds a property to the wallet.
         */
        return properties.add(property);
    }


}