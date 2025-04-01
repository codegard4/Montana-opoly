package src.main.player;

import src.main.board.Property;
import src.main.board.Space;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The player class represents a player in the game. Players have a token, a
 * name, and a wallet. The wallet contains the player's money and properties.
 */
public class Player {

    private enum PlayerType {
        HUMAN,
        CPU
    }

    private String token;
    public Wallet wallet;
    public Space currentSpace;
    private static final int STARTING_MONEY = 1500;
    private static PlayerType playerType;

    /**
     * Constructor for the Player. Players should be constructed fully at the start
     * of the game
     */
    public Player() {
        token = null;
        this.wallet = new Wallet(STARTING_MONEY);
        this.currentSpace = null;
    }
    /**
     * Constructor for the Player. Players should be constructed fully at the start
     * of the game
     * @param token
     */
    public Player(String token, String type) {
        this.token = token;
        this.wallet = new Wallet(STARTING_MONEY);
        this.currentSpace = null;
        playerType = PlayerType.valueOf(type);
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

    public void passGo() {
        wallet.addMoney(200);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void payRent(Player p, int amount) {
        wallet.removeMoney(amount);
        p.getWallet().addMoney(amount);
    }

    /**
     * Sells a property if the player owns it
     * Gives the buyer the property and transfers the cost of the property
     * 
     * @param property
     * @return
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

    public void mortgageProperty(Property property) {
        // mortgage the property by setting it to mortgaged and give the player the mortgage amount
        property.morgtage();
        wallet.addMoney(property.getPrice() / 2);
    }
    public void unmortgageProperty(Property property) {
        property.unmorgtage();
        wallet.removeMoney(property.getPrice() / 2);
        // unmortgage a property by setting it to unmortgaged and charge the player the amount to unmortgage
    }

    public int calculateRailroadRent(){
        // calculate the amount someone will pay in rent based on the number of railroads owned
//        (25$ for every) railroad that is owned by this player
        return -1;
    }

}