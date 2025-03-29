package src.main.player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import src.main.board.Property;

public class Wallet{
    /*
     * A player's wallet contains their money and properties.
     */
    private int money;
    private List<Property> properties;
    private String[] colors = new String[] {"Brown","LightBlue","Magenta","Orange","Red","Yellow","Green","Blue"};
    private int[] numFull = new int[] {2,3,3,3,3,3,3,3};
    private Map<String, Integer> fullSetList; 
 

    public Wallet(int startingMoney){
        /*
         * Constructor for the Wallet.
         * A wallet is initialized with 1500 money and an empty list of properties.
         */
        this.money = startingMoney;
        this.properties = new ArrayList<>();
        fullSetList = new HashMap<>();
        for(int i = 0;i<colors.length;i++){
            fullSetList.put(colors[i], numFull[i]);
        }
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
        String col = property.getColor();
        List<Property> checkForSet = new ArrayList<>();
        checkForSet.add(property);
        if(!col.equals("Utility") && !col.equals("Mountain")){
            for(Property p: properties){
                if(p.getColor().equals(property.getColor())){
                    checkForSet.add(p);
                }
            }
            if(fullSetList.get(property.getColor()).equals(checkForSet.size())){
                for(Property p: checkForSet){
                    p.fullSet(true);
                }
            }
        }
        return properties.add(property);
    }


}