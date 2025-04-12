package src.main.player;

import src.main.board.Property;
import src.main.board.Space;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Player class represents a player in the game. Players have a token, a
 * name, and a wallet. The wallet contains the player's money and properties.
 */
public class Player {

    /** Is this player a CPU or a Human */
    public enum PlayerType {
        HUMAN,
        CPU
    }

    private String token;
    public Wallet wallet;
    public Space currentSpace;
    private static final int STARTING_MONEY = 1500;
    private final PlayerType playerType;

    /**
     * Default constructor for the Player. Players should be constructed fully at the start
     * of the game.
     */
    public Player(boolean isCPU) {
        token = null;
        this.wallet = new Wallet(STARTING_MONEY);
        this.currentSpace = null;
        if (isCPU) {
            System.out.println("CPU -- Player class");
            this.playerType = PlayerType.CPU;
        }
        else {
            System.out.println("Human -- Player class");
            this.playerType = PlayerType.HUMAN;
        }
    }

    /**
     * Constructor for the Player with a specified token and type.
     * @param token The player's token identifier.
     * @param type The type of player (HUMAN or CPU).
     */
    public Player(String token, String type) {
        this.token = token;
        this.wallet = new Wallet(STARTING_MONEY);
        this.currentSpace = null;
        playerType = PlayerType.valueOf(type);
    }

    /**
     * Gets the player's token.
     * @return The player's token.
     */
    public String getToken(){
        return token;
    }

    /**
     * Sets the player's token.
     * @param token The new token to be set.
     */
    public void setToken(String token){
        this.token = token;
    }

    /**
     * Gets the player's current money balance.
     * @return The amount of money in the player's wallet.
     */
    public int getMoney(){
        return wallet.getMoney();
    }

    /**
     * Gets the space the player is currently on.
     * @return The current space occupied by the player.
     */
    public Space getSpace(){
        return currentSpace;
    }

    /**
     * Moves the player to a new space.
     * @param newSpace The space to move to.
     */
    public void move(Space newSpace){
        currentSpace = newSpace;
    }

    /**
     * Buys a property if the player has enough money.
     *
     * @param property The property to be purchased.
     */
    public void buyProperty(Property property) {
        if (property.getPrice() > wallet.getMoney()) {
            return;
        }
        wallet.removeMoney(property.getPrice());
        wallet.addProperty(property);
        property.purchased(this);
    }

    /**
     * Awards the player $200 for passing Go.
     */
    public void passGo() {
        wallet.addMoney(200);
    }

    /**
     * Gets the player's wallet.
     * @return The player's wallet.
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Pays rent to another player.
     * @param p The player receiving the rent.
     * @param amount The amount to be paid.
     */
    public void payRent(Player p, int amount) {
        wallet.removeMoney(amount);
        p.getWallet().addMoney(amount);
    }

    /**
     * Sells a property to another player if the seller owns it.
     * @param property The property to sell.
     * @param buyer The player purchasing the property.
     * @return The property if successfully sold, null otherwise.
     */
    public Property sellProperty(Property property, Player buyer) {
        if (wallet.getProperties().contains(property)) {
            wallet.addMoney(property.getPrice());
            wallet.getProperties().remove(property);
            buyer.wallet.removeMoney(property.getPrice());
            buyer.wallet.addProperty(property);
            return property;
        } else {
            System.out.println("You do not own the property so you cannot sell it");
            return null;
        }
    }

    /**
     * Gets a list of all unmortgaged properties owned by the player.
     * @return A sorted list of unmortgaged properties.
     */
    public List<Property> getUnmortgagedProperties(){
        List<Property> unmortgagedProperties = new ArrayList<Property>();
        for (Property property : wallet.getProperties()) {
            if (!property.isMortgaged()){
                unmortgagedProperties.add(property);
            }
        }
        unmortgagedProperties.sort(Comparator.comparing(Property::getPropertyClass));
        return unmortgagedProperties;
    }

    /**
     * Mortgages a property, adding half of its price to the player's wallet.
     * @param property The property to mortgage.
     */
    public void mortgageProperty(Property property) {
        property.mortgage();
        wallet.addMoney(property.getPrice() / 2);
    }

    /**
     * Unmortgages a property, deducting half of its price from the player's wallet.
     * @param property The property to unmortgage.
     */
    public void unmortgageProperty(Property property) {
        property.unmortgage();
        wallet.removeMoney(property.getPrice() / 2);
        //TODO: implement this option in the GUI
    }

    /**
     * Calculates the total wealth of the player, including money and properties.
     * @return The player's total wealth.
     */
    public int getTotalWealth() {
        int totalWealth = wallet.getMoney();
        for (Property property : wallet.getProperties()) {
            totalWealth += property.getHouseValue();
            if (property.isMortgaged()) {
                totalWealth += property.getPrice() / 2;
            } else {
                totalWealth += property.getPrice();
            }
        }
        return totalWealth;
    }

    /**
     * Summarizes the player's money and total wealth.
     * @return A string summarizing the player's financial status.
     */
    public String summarizeMoney() {
        return getToken() + " - Total Wealth: $" + getTotalWealth();
    }

    /**
     * Calculates the rent for railroads (Mountain properties) owned by the player.
     * @return The amount to be paid as railroad rent.
     */
    public int calculateRailroadRent() {
        int count = 0;
        for (Property property : wallet.getProperties()) {
            if (property.getPropertyClass() == Property.PropertyClass.Mountain) {
                count++;
            }
        }
        return count * 25;
    }


    /**
     * 
     * @return Whether or not the player is a bot
     */
    public boolean isBot() {return playerType == PlayerType.CPU;}

    /**
     * Returns the string representation of a player
     * @return string with a player's token and bot status
     */
    @Override
    public String toString() {
        if (isBot()) {
            return getToken() + " (bot)";
        } else {
            return getToken();
        }
    }

}