package src.main.player;

import src.main.board.Property;
import src.main.board.Space;

/**
 * The player class represents a player in the game. Players have a token, a
 * name, and a wallet. The wallet contains the player's money and properties.
 */
public class Player {
    private String token;
    public Wallet wallet;
    public Space currentSpace;
    private static final int STARTING_MONEY = 1500;

    /**
     * Constructor for the Player. Players should be constructed fully at the start
     * of the game
     */
    public Player() {
        this(null);
    }
    /**
     * Constructor for the Player. Players should be constructed fully at the start
     * of the game
     * @param token
     */
    public Player(String token) {
        this.token = token;
        this.wallet = new Wallet(STARTING_MONEY);
        this.currentSpace = null;

    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }
    public int getMoney(){
        return wallet.getMoney();
    }
    public Space getSpace(){
        return currentSpace;
    }
    public void move(Space newSpace){
        currentSpace = newSpace;
    }

    /**
     * Buys a property if the player has enough money
     * 
     * @param property
     * @return
     */
    public boolean buyProperty(Property property) {
        if (property.getPrice() > wallet.getMoney()) {
            return false;
        }
        wallet.removeMoney(property.getPrice());
        wallet.addProperty(property);
        property.purchased(this);
        return true;

    }
    public Property sellProperty(Property property){
        /*
         * Removes a property from the player's list of properties.
         */
        return null;
    }

    /**
     * Sells a property if the player owns it
     * 
     * @param property
     * @return
     */
    public void passGo() {
        wallet.addMoney(200);
    }

    /**
     * Sells a property if the player owns it
     * Gives the buyer the property and transfers the cost of the property
     * 
     * @param property
     * @return
     */
    private Property sellProperty(Property property, Player buyer) {
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

}