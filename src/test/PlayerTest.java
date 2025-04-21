package src.test;

import src.main.board.Property;
import src.main.player.Player;

import java.util.List;

public class PlayerTest {

    public static void main(String[] args) {
        testBuyProperty();
        testCannotBuyExpensiveProperty();
        testCannotSellPropertyNotOwned();
        System.out.println("All tests finished.");
    }

    public static void checkTrue(boolean condition, String testName) {
        if (condition) {
            System.out.println(testName + ": PASSED");
        } else {
            System.out.println(testName + ": FAILED");
        }
    }

    public static void checkFalse(boolean condition, String testName) {
        if (!condition) {
            System.out.println(testName + ": PASSED");
        } else {
            System.out.println(testName + ": FAILED");
        }
    }

    public static void testBuyProperty() {
        Player testPlayer = new Player(false);
        Property testProperty = new Property("TestProperty", null, "Brown", 100, new int[]{10, 20, 40, 80, 120}, 0);

        testPlayer.buyProperty(testProperty);
        List<Property> properties = testPlayer.getUnmortgagedProperties();
        boolean success = properties.contains(testProperty);

        checkTrue(success, "testBuyProperty - Should successfully buy property");
        checkTrue(testPlayer.wallet.getMoney() == 1400, "testBuyProperty - Money should decrease correctly");
        checkTrue(testPlayer.wallet.getProperties().contains(testProperty), "testBuyProperty - Property should be in player's wallet");
        checkTrue(testProperty.getOwner() == testPlayer, "testBuyProperty - Player should be the owner");
    }

    public static void testCannotBuyExpensiveProperty() {
        Player testPlayer = new Player(false);
        Property testProperty = new Property("TestProperty", null, "Brown", 2000, new int[]{10, 20, 40, 80, 120}, 0);

        testPlayer.buyProperty(testProperty);
        List<Property> properties = testPlayer.getUnmortgagedProperties();
        boolean success = !properties.contains(testProperty);

        checkTrue(success, "testCannotBuyExpensiveProperty - Should NOT be able to buy expensive property");
        checkTrue(testPlayer.wallet.getMoney() == 1500, "testCannotBuyExpensiveProperty - Money should remain the same");
        checkFalse(testPlayer.wallet.getProperties().contains(testProperty), "testCannotBuyExpensiveProperty - Property should NOT be in wallet");
        checkTrue(testProperty.getOwner() == null, "testCannotBuyExpensiveProperty - Property should have no owner");
    }

    public static void testCannotSellPropertyNotOwned() {
        Player seller = new Player(false);
        Player buyer = new Player(false);

        Property property = new Property("TestProperty", null, "Brown", 2000, new int[]{10, 20, 40, 80, 120}, 0);

        Property p = seller.sellProperty(property, buyer);
        boolean sold = p == null;

        checkTrue(sold, "testCannotSellPropertyNotOwned - Seller should NOT be able to sell unowned property");
        checkTrue(seller.wallet.getMoney() == 1500, "testCannotSellPropertyNotOwned - Seller's money should not change");
        checkTrue(buyer.wallet.getMoney() == 1500, "testCannotSellPropertyNotOwned - Buyer's money should not change");
        checkTrue(property.getOwner() == null, "testCannotSellPropertyNotOwned - Property should remain unowned");
    }
}
