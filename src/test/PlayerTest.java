package src.test;

import src.main.board.Property;
import src.main.player.Player;

public class PlayerTest {

    public static void main(String[] args) {
        testBuyProperty();
        testCannotBuyExpensiveProperty();
        testSellPropertyToBank();
        testSellPropertyToAnotherPlayer();
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
        Player testPlayer = new Player("testToken", "testName", 1500);
        Property testProperty = new Property("TestProperty", 100, new int[]{10, 20, 40, 80, 120});

        boolean success = testPlayer.buyProperty(testProperty);

        checkTrue(success, "testBuyProperty - Should successfully buy property");
        checkTrue(testPlayer.wallet.getMoney() == 1400, "testBuyProperty - Money should decrease correctly");
        checkTrue(testPlayer.wallet.getProperties().contains(testProperty), "testBuyProperty - Property should be in player's wallet");
        checkTrue(testProperty.getOwner() == testPlayer, "testBuyProperty - Player should be the owner");
    }

    public static void testCannotBuyExpensiveProperty() {
        Player testPlayer = new Player("testToken", "testName", 1500);
        Property expensiveProperty = new Property("ExpensiveProperty", 2000, new int[]{10, 20, 40, 80, 120});

        boolean success = testPlayer.buyProperty(expensiveProperty);

        checkFalse(success, "testCannotBuyExpensiveProperty - Should NOT be able to buy expensive property");
        checkTrue(testPlayer.wallet.getMoney() == 1500, "testCannotBuyExpensiveProperty - Money should remain the same");
        checkFalse(testPlayer.wallet.getProperties().contains(expensiveProperty), "testCannotBuyExpensiveProperty - Property should NOT be in wallet");
        checkTrue(expensiveProperty.getOwner() == null, "testCannotBuyExpensiveProperty - Property should have no owner");
    }

    public static void testSellPropertyToBank() {
        Player testPlayer = new Player("testToken", "testName", 1500);
        Property testProperty = new Property("TestProperty", 100, new int[]{10, 20, 40, 80, 120});
        testPlayer.buyProperty(testProperty);

        testPlayer.sellProperty(testProperty);

        checkTrue(testPlayer.wallet.getMoney() == 1450, "testSellPropertyToBank - Player should receive half value of property");
        checkFalse(testPlayer.wallet.getProperties().contains(testProperty), "testSellPropertyToBank - Property should be removed from wallet");
        checkTrue(testProperty.getOwner() == null, "testSellPropertyToBank - Property should have no owner");
    }

    public static void testSellPropertyToAnotherPlayer() {
        Player seller = new Player("dog", "Alice", 1500);
        Player buyer = new Player("car", "Bob", 1500);
        Property property = new Property("TestProperty", 200, new int[]{10, 20, 40, 80, 120});

        seller.buyProperty(property);
        boolean sold = seller.sellProperty(property, buyer);

        checkTrue(sold, "testSellPropertyToAnotherPlayer - Property should be sold successfully");
        checkTrue(seller.wallet.getMoney() == 1700, "testSellPropertyToAnotherPlayer - Seller should receive money");
        checkTrue(buyer.wallet.getMoney() == 1300, "testSellPropertyToAnotherPlayer - Buyer should lose money");
        checkFalse(seller.wallet.getProperties().contains(property), "testSellPropertyToAnotherPlayer - Seller should no longer own property");
        checkTrue(buyer.wallet.getProperties().contains(property), "testSellPropertyToAnotherPlayer - Buyer should now own property");
        checkTrue(property.getOwner() == buyer, "testSellPropertyToAnotherPlayer - Buyer should be the new owner");
    }

    public static void testCannotSellPropertyNotOwned() {
        Player seller = new Player("dog", "Alice", 1500);
        Player buyer = new Player("car", "Bob", 1500);
        Property property = new Property("UnownedProperty", 200, new int[]{10, 20, 40, 80, 120});

        boolean sold = seller.sellProperty(property, buyer);

        checkFalse(sold, "testCannotSellPropertyNotOwned - Seller should NOT be able to sell unowned property");
        checkTrue(seller.wallet.getMoney() == 1500, "testCannotSellPropertyNotOwned - Seller's money should not change");
        checkTrue(buyer.wallet.getMoney() == 1500, "testCannotSellPropertyNotOwned - Buyer's money should not change");
        checkTrue(property.getOwner() == null, "testCannotSellPropertyNotOwned - Property should remain unowned");
    }
}
