package src.main.player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import src.main.board.Property;

/**
 * A Montana-opoly player's wallet contains all of their properties and money.
 */
public class Wallet{
    private int money;
    private List<Property> properties;
    private String[] colors = new String[] {"Brown","LightBlue","Magenta","Orange","Red","Yellow","Green","Blue"};
    private int[] numFull = new int[] {2,3,3,3,3,3,3,3};
    private Map<String, Integer> fullSetList;

    /**
     * Creates a wallet with a starting amount of money and no properties.
     * @param startingMoney The amount of money a player starts with.
     */
    public Wallet(int startingMoney){
        this.money = startingMoney;
        this.properties = new ArrayList<>();
        fullSetList = new HashMap<>();
        for(int i = 0; i < colors.length; i++){
            fullSetList.put(colors[i], numFull[i]);
        }
    }

    /**
     * Gets the current amount of money in the wallet.
     * @return The amount of money the player has.
     */
    public int getMoney(){
        return money;
    }

    /**
     * Adds money to the player's wallet.
     * @param amount The amount of money to add.
     */
    public void addMoney(int amount){
        money += amount;
    }

    /**
     * Removes money from the player's wallet.
     * @param amount The amount of money to deduct.
     */
    public void removeMoney(int amount){
        money -= amount;
    }

    /**
     * Gets the list of properties owned by the player.
     * @return A list of properties owned by the player.
     */
    public List<Property> getProperties(){
        return properties;
    }

    /**
     * Adds a property to the player's wallet. If the player collects all properties of the same color,
     * the full set status is updated.
     * @param property The property to be added.
     * @return true if the property was successfully added, false otherwise.
     */
    public boolean addProperty(Property property){
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
