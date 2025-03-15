package src.test;

import src.main.board.Property;
import src.main.player.Player;

public class PlayerTest{
    public static void main(String[] args) {
        Player testPlayer = new Player("testToken", "testName");
        Property testProperty = new Property("TestProperty", 100, new int[]{10,20,40,80,120});
        testPlayer.buyProperty(testProperty);
        if (testPlayer.wallet.getMoney() == 1400){
            System.out.println("PlayerTest: buyProperty() passed");
            System.out.println("Properties: " + testPlayer.wallet.getProperties());
            System.out.println("Player's Money: " + testPlayer.wallet.getMoney());
        } else {
            System.out.println("PlayerTest: buyProperty() failed");
        }
        Property testExpensiveProperty = new Property("TestExpensiveProperty", 2000, new int[]{10,20,40,80,120});
        if (!testPlayer.buyProperty(testExpensiveProperty)){
            System.out.println("PlayerTest: buyProperty() passed");
            System.out.println("Properties: " + testPlayer.wallet.getProperties());
            System.out.println("Player's Money: " + testPlayer.wallet.getMoney());
        } else {
            System.out.println("PlayerTest: buyProperty() failed because it bought the expensive property");
        }
    }
}
