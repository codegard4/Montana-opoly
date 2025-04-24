package src.main.board;

import javax.swing.JOptionPane;

import src.main.player.Player;

/**
 * The card class stores chance and community chest cards and applies their rewards / penalties to the player
 */
public class Card {
    private final String description;
    private final int moneyEffect; // Positive for gain, negative for loss
    private final int moveSpaces;  // Exact board index if goToLocation is true, -1 means no movement
    private final boolean goToLocation; // If true, moveSpaces is an exact index on the board

    /**
     * Constructor for Chance and Community Chest cards
     *
     * @param description  the card description
     * @param moneyEffect  how the player's money will change
     * @param moveSpaces   how the player's position will change
     * @param goToLocation whether the player is moving locations
     */
    public Card(String description, int moneyEffect, int moveSpaces, boolean goToLocation) {
        this.description = description;
        this.moneyEffect = moneyEffect;
        this.moveSpaces = moveSpaces;
        this.goToLocation = goToLocation;
    }

    /**
     * Applies effect of Chance/Community Chest card drawn to player and displays message to board.
     *
     * @param player player who drew the card
     * @param board  board the player is playing on
     */
    public void applyEffect(Player player, Board board) {
        JOptionPane.showMessageDialog(null, description, "Card Drawn", JOptionPane.INFORMATION_MESSAGE);

        // Apply money effect if applicable
        if (moneyEffect != 0) {
            player.wallet.addMoney(moneyEffect);
        }

        // Handle movement only if moveSpaces is not -1
        if (moveSpaces != -1) {
            if (goToLocation) {
                // Move to an exact board location
                if (moveSpaces >= 0 && moveSpaces < board.getBoardSize()) {
                    player.move(board.getSpaceAt(moveSpaces));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid move location on card: " + description, "Card Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Move relative to the current position
                int currentIndex = player.getSpace().getIndex();
                int newIndex = (currentIndex + moveSpaces) % board.getBoardSize();
                if (newIndex < currentIndex) { // Looping around the board means they passed GO
                    player.passGo();
                    JOptionPane.showMessageDialog(null, player.getToken() + " passed GO and collected $200!", "Pass GO", JOptionPane.INFORMATION_MESSAGE);
                }
                player.move(board.getSpaceAt(newIndex));
            }
        }
        // move spaces is -1 which indicates we are not moving spaces
    }
}
