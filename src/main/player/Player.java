package src.main.player;
import src.main.board.Property;

public class Player{
    /*
     * Constructor for the Player.
     * Each player has a name, wallet, properties, and a symbol.
     */
    private String token;
    private String name;
    public Wallet wallet;

    /**
     * Constructor for the Player. Players should be constructed fully at the start of the game
     * @param token
     * @param name
     */
    public Player(String token, String name){
        this.token = token;
        this.name = name;
        this.wallet = new Wallet();

    }
    public boolean buyProperty(Property property){
        /*
         * Adds a property to the player's list of properties.
         */

        if (property.getPrice() > wallet.getMoney()){
            return false;
        }
        wallet.removeMoney(property.getPrice());
        wallet.addProperty(property);
        property.purchased(this);
        return true;

    }
    private Property sellProperty(Property property){
        /*
         * Removes a property from the player's list of properties.
         */
        return null;

    }


}