package src.main.player;

import src.main.board.Property;

/**
 * The player class represents a player in the game. Players have a token, a
 * name, and a wallet. The wallet contains the player's money and properties.
 */
public class Player {
    private String token;
    private String name;
    public Wallet wallet;
    public Property currentProperty;

    /**
     * Constructor for the Player. Players should be constructed fully at the start
     * of the game
     * @param token
     * @param name
     */
    public Player(String token, String name, int startingMoney) {
        this.token = token;
        this.name = name;
        this.wallet = new Wallet(startingMoney);
        this.currentProperty = null;

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
<<<<<<< HEAD
    public Property sellProperty(Property property){
        /*
         * Removes a property from the player's list of properties.
         */
        return null;
=======
>>>>>>> 4ee229b7842e327074b5814db2977ecff55751b1

    /**
     * Sells a property if the player owns it
     * 
     * @param property
     * @return
     */
    public void passGo() {
        wallet.addMoney(200);
    }

<<<<<<< HEAD
    public void passGo(){
        wallet.addMoney(200);
    }

=======
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
>>>>>>> 4ee229b7842e327074b5814db2977ecff55751b1

}