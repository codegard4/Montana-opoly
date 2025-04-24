package src.main.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.board.Property;

/**
 * A Montana-opoly player's wallet contains all of their properties and money.
 */
public class Wallet {
    private int money; // A player's money
    private final List<Property> properties; // A player's properties
    private final Map<String, Integer> fullSetList; // A list of a player's full sets

    /**
     * Creates a wallet with a starting amount of money and no properties.
     *
     * @param startingMoney The amount of money a player starts with.
     */
    public Wallet(int startingMoney) {
        this.money = startingMoney;
        this.properties = new ArrayList<>();
        fullSetList = new HashMap<>();
        String[] colors = new String[]{"Brown", "LightBlue", "Magenta", "Orange", "Red", "Yellow", "Green", "Blue"};
        for (int i = 0; i < colors.length; i++) {
            int[] numFull = new int[]{2, 3, 3, 3, 3, 3, 3, 3};
            fullSetList.put(colors[i], numFull[i]);
        }
    }

    /**
     * Gets the current amount of money in the wallet.
     *
     * @return The amount of money the player has.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Adds money to the player's wallet.
     *
     * @param amount The amount of money to add.
     */
    public void addMoney(int amount) {
        money += amount;
    }

    /**
     * Removes money from the player's wallet.
     *
     * @param amount The amount of money to deduct.
     */
    public void removeMoney(int amount) {
        money -= amount;
    }

    /**
     * Gets the list of properties owned by the player.
     *
     * @return A list of properties owned by the player.
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Adds a property to the player's wallet. If the player collects all properties of the same color,
     * the full set status is updated.
     *
     * @param property The property to be added.
     */
    public void addProperty(Property property) {
        String col = property.getColor();
        List<Property> checkForSet = new ArrayList<>();
        checkForSet.add(property);
        if (!col.equals("Utility") && !col.equals("Mountain")) {
            for (Property p : properties) {
                if (p.getColor().equals(property.getColor())) {
                    checkForSet.add(p);
                }
            }
            if (fullSetList.get(property.getColor()).equals(checkForSet.size())) {
                for (Property p : checkForSet) {
                    p.fullSet(true);
                }
            }
        }
        properties.add(property);
    }

    /**
     * Removes a property from a player's wallet
     *
     * @param property the property to be removed
     */
    public void removeProperty(Property property) {
        boolean removed = properties.remove(property);
        if (!removed) return;

        String col = property.getColor();

        // Only check full set status for color groups (not Utility or Mountain)
        if (!col.equals("Utility") && !col.equals("Mountain")) {

            // Count how many properties of this color remain
            int sameColorCount = 0;
            for (Property p : properties) {
                if (p.getColor().equals(col)) {
                    sameColorCount++;
                }
            }

            // If the player no longer has a full set, remove full set status
            int requiredForSet = fullSetList.getOrDefault(col, Integer.MAX_VALUE);
            if (sameColorCount < requiredForSet) {
                for (Property p : properties) {
                    if (p.getColor().equals(col)) {
                        p.fullSet(false);
                    }
                }
            }
        }
    }
}
