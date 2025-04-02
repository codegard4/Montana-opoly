package src.main.board;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import src.main.player.Player;
import src.main.board.Card;

// TODO: add javadocs

/**
 * Board is called by MonopolyGame and creates a gameboard to play monopoly on
 */
public class Board extends JFrame {

    private JFrame board;
    private JPanel playerPanel;
    private ImageIcon gameBoard = new ImageIcon("src\\dependencies\\fullBoard.jpg");
    private Space[] boardArray;
    private List<Card> chanceCards = new ArrayList<>();
    private List<Card> communityChestCards = new ArrayList<>();
    private Map<Rectangle, Space> viewMap;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    private Player[] players;
    // TODO: change to Montana themed tokens
    private final List<String> tokens = new ArrayList<>();
    private final int turns;
    private int turnsTaken = 0;
    private boolean newTurn = true;
    private int currentPlayerIndex = 0; // start with the first player
    private Random dice = new Random();
    private Random randomCard = new Random();

    public JButton newTurnButton = new JButton("New Turn");
    public JButton endGameButton = new JButton("End Game");
    public JButton tradeButton = new JButton("Trade");
    public JButton addHousesButton = new JButton("Add Houses");

    /**
     * Board constructor
     * @param numPlayers number of players
     * @param numTurns number of game turns each player gets
     */
    public Board(int numPlayers, int numTurns) {
        turns = numTurns;
        populateTokens();
        board = new JFrame("Montana-opoly");
        board.setBounds(0, 0, WIDTH, HEIGHT);
        boardArray = new Space[40];
        // load the board spaces and cards
        loadSpaces();
        loadCards();

        // Initialize players
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
            players[i].move(boardArray[0]); // all players start on GO
        }

        // Show Token Selection Dialog for each player
        for (int i = 0; i < numPlayers; i++) {
            showTokenSelectionPopup(i);
        }

        // Setup player panel
        setupPlayerPanel();
        board.add(playerPanel);
        // Setup board
        Container boardContent = board.getContentPane();
        boardContent.setLayout(new BorderLayout());
        JPanel boardPanel = new JPanel();
        JLabel boardLabel = new JLabel(gameBoard);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        newTurnButton.setEnabled(false);

        // Add all the buttons
        buttonPanel.add(newTurnButton);
        buttonPanel.add(endGameButton);
        buttonPanel.add(tradeButton);
        buttonPanel.add(addHousesButton);

        boardPanel.add(boardLabel);
        boardContent.add(buttonPanel, BorderLayout.NORTH);
        boardContent.add(boardPanel, BorderLayout.CENTER);
        boardContent.add(playerPanel, BorderLayout.SOUTH);
        board.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickProperty(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // Listen for each of the buttons to be clicked
        newTurnButton.addActionListener(e -> {newTurnButton.setEnabled(false); startNewTurn();});
        endGameButton.addActionListener(e -> endGame());
        tradeButton.addActionListener(e -> initiateTrade());
        addHousesButton.addActionListener(e -> addHouses());

        board.paintComponents(getGraphics());
        board.setVisible(true);
        playGame();
    }

    public static void main(String[] args) {
        Board testBoard = new Board(2, 5);
    }

    /**
     * Load all the chance and community chest cards
     */
    private void loadCards() {
        loadCardFile("src\\dependencies\\cards.txt");
    }

    /**
     * Loads each card from a properly formatted .txt file
     * @param filePath the path to the chance and community chest cards
     */
    private void loadCardFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String description = data[0];
                int moneyEffect = Integer.parseInt(data[1]);
                int moveSpaces = Integer.parseInt(data[2]);
                boolean goToLocation = Boolean.parseBoolean(data[3]);
                boolean isChance = Boolean.parseBoolean(data[4]);

                Card card = new Card(description, moneyEffect, moveSpaces, goToLocation);
                if (isChance) {
                    chanceCards.add(card);
                } else {
                    communityChestCards.add(card);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading cards from file: " + filePath, "File Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * End the game and show who won
     */
    private void endGame() {
        // Sort players by total money in descending order
        Arrays.sort(players, (p1, p2) -> Integer.compare(p2.getMoney(), p1.getMoney()));

        // Build summary string
        StringBuilder summary = new StringBuilder("Game Over! Final Rankings:\n");
        int rank = 1;
        for (Player p : players) {
            summary.append(rank).append(". ").append(p.summarizeMoney()).append("\n");
            rank++;
        }

        // Display final rankings
        JOptionPane.showMessageDialog(null, summary.toString(), "Final Rankings", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Allow two players to make a property trade
     */
    private void initiateTrade() {
        JOptionPane.showMessageDialog(board, "Trade feature not yet implemented.", "Trade", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Allow a player to add houses to their properties
     */
    private void addHouses() {
        JOptionPane.showMessageDialog(board, "House purchasing not yet implemented.", "Add Houses", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     */
    private void populateTokens() {
        // TODO: change to montana themed tokens
//        String[] tokensToAdd = {"Car", "Dog", "Hat", "Boat", "Thimble", "Iron"};
        String[] tokensToAdd = {"F-150", "Hay Bale", "Bird Dog", "Fishing Boat", "Pickaxe", "Cowboy Boot"};
        tokens.addAll(Arrays.asList(tokensToAdd));
    }

    /**
     * Gets the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * Sets up the panel that will display player information
     */
    private void setupPlayerPanel() {
        playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        updatePlayerPanel();
    }

    /**
     * Updates the player panel to display players' money and owned properties.
     */
    private void updatePlayerPanel() {
        playerPanel.removeAll();

        // Set GridLayout: 1 row, n columns (one for each player)
        playerPanel.setLayout(new GridLayout(1, players.length, 10, 10));

        for (Player p : players) {

            // Create individual player column
            JPanel singlePlayerPanel = new JPanel();
            singlePlayerPanel.setLayout(new BoxLayout(singlePlayerPanel, BoxLayout.Y_AXIS));
            singlePlayerPanel.setBorder(BorderFactory.createTitledBorder(p.getToken()));

            // Player, Token, Money and current space
            JLabel playerLabel = new JLabel("Money: $" + p.getMoney());
            singlePlayerPanel.add(playerLabel);
            JLabel spaceLabel = new JLabel("Current Space: " + p.getSpace());
            singlePlayerPanel.add(spaceLabel);

            // Display owned properties
            List<Property> ownedProperties = p.getUnmortgagedProperties();
            if (!ownedProperties.isEmpty()) {
                singlePlayerPanel.add(new JLabel("Properties:"));
                for (Property prop : ownedProperties) {
                    JLabel propertyLabel = new JLabel("• " + prop.getName() + " ($" + prop.getPrice() + ")");
                    singlePlayerPanel.add(propertyLabel);
                }
            }
            // Add this player's column to the player panel
            playerPanel.add(singlePlayerPanel);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }


    /**
     * Shows the tokens that players can select at the start of the game
     * @param playerIndex which player is selecting their token
     */
    private void showTokenSelectionPopup(int playerIndex) {
        // Display a dropdown for token selection
        String[] tokenChoices = tokens.toArray(new String[0]); // we have to pass the JOptionPane a []
        String token = (String) JOptionPane.showInputDialog(board,
                "Player " + (playerIndex + 1) + ", pick your token:",
                "Token Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tokenChoices,
                tokens.get(0)); // Set default to the first token

        if (token != null) {
            players[playerIndex].setToken(token);
            tokens.remove(token);
        }
    }

    /**
     * Load all the gameboard spaces from a .txt file
     */
    private void loadSpaces() {
        try (final Scanner spaceReader = new Scanner(new File("src\\dependencies\\spaceList.txt"))) {
            spaceReader.nextLine();
            int i = 0;
            while (spaceReader.hasNext()) {
                String[] newSpace = spaceReader.next().split(",");
                if (newSpace[1].equals("Property")) {
                    if (newSpace.length > 6) {
                        int[] propRent = new int[7];
                        for (int k = 5; k < 12; k++) {
                            propRent[k - 5] = Integer.parseInt(newSpace[k]);
                        }
                        boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), propRent, i);
                        i++;
                    } else {
                        if (newSpace.length > 5) {
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), Integer.parseInt(newSpace[5]), i);
                            i++;
                        } else {
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), i);
                            i++;
                        }
                    }
                } else {
                    boardArray[i] = new Space(newSpace[1], newSpace[0], newSpace[2], i);
                    i++;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Space File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        try (final Scanner coordReader = new Scanner(new File("src\\dependencies\\spaceCoords.txt"))) {
            coordReader.nextLine();
            int i = 0;
            while (coordReader.hasNext()) {
                String[] newCoord = coordReader.next().split(",");
                int[] coords = new int[8];
                for (int k = 1; k < 9; k++) {
                    coords[k - 1] = Integer.parseInt(newCoord[k]);
                }
                boardArray[i].setClickPane(coords);
                i++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Coordinates File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        viewMap = new HashMap<>();
        for (Space c : boardArray) {
            viewMap.put(c.getClickPane(), c);
        }
    }

    /**
     * Detects when a property is clicked
     * @param e mouse click event
     */
    public void clickProperty(MouseEvent e) {
        Point clickPoint = new Point(e.getX(), e.getY());
        for (Rectangle r : viewMap.keySet()) {
            if (r.contains(clickPoint)) {
                viewMap.get(r).viewProperty();
            }
        }
    }

    /**
     * Play the game until the number of turns is reached
     */
    private void playGame() {
        while (turnsTaken <= (turns * players.length)) {
            if (newTurn) {
                JOptionPane.showMessageDialog(null, "Turn " + turnsTaken, "Turns Info", JOptionPane.INFORMATION_MESSAGE);
                nextTurn();
            }
        }

        endGame();

    }

    /**
     * Get the space at the index provided
     * @param index the board index
     * @return the space at that index
     */
    public Space getSpaceAt(int index) {
        return boardArray[index];
    }

    /**
     * Get the size of the game board
     * @return length of the baord array
     */
    public int getBoardSize() {
        return boardArray.length;
    }

    //TODO: trading properties
    //TODO: bankruptcy logic

    /**
     * Current player takes their turn
     */
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + "'s turn!", "Turn Notification", JOptionPane.INFORMATION_MESSAGE);

        int roll = rollDice();
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " rolled a " + roll, "Dice Roll", JOptionPane.INFORMATION_MESSAGE);

        int currentIndex = currentPlayer.getSpace().getIndex();
        int newIndex = (currentIndex + roll) % boardArray.length;

        // Check if player passes GO
        if (newIndex < currentIndex) {
            currentPlayer.passGo();
            JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " passed GO and collected $200!",
                    "Pass GO", JOptionPane.INFORMATION_MESSAGE);
        }

        Space landedSpace = boardArray[newIndex];
        currentPlayer.move(landedSpace);
        updatePlayerPanel();
        handleSpecialSpace(currentPlayer, landedSpace);

        // Draw Chance or Community Chest card if applicable
        if (landedSpace.getType() == Space.SpaceType.Chance) {
            drawChanceCard(currentPlayer);
        } else if (landedSpace.getType() == Space.SpaceType.CommunityChest) {
            drawCommunityChestCard(currentPlayer);
        }
    }

    /**
     * Draw a chance card and apply the changes to the player
     * @param player the player drawing the card
     */
    public void drawChanceCard(Player player) {
        if (chanceCards.isEmpty()) return;
        Card drawnCard = chanceCards.get(randomCard.nextInt(chanceCards.size()));
        drawnCard.applyEffect(player, this);
    }

    /**
     * Draw a community chest card and apply the changes to the player
     * @param player the player drawing the card
     */
    public void drawCommunityChestCard(Player player) {
        if (communityChestCards.isEmpty()) return;
        Card drawnCard = communityChestCards.get(randomCard.nextInt(communityChestCards.size()));
        drawnCard.applyEffect(player, this);
    }


    /**
     * Handle a player landing on a space
     * @param player the player landing on the space
     * @param space the space landed on
     */
    private void handleSpecialSpace(Player player, Space space) {
        if (space instanceof Property) {
            Property property = (Property) space;
            handlePropertyLanding(player, property);
        } else if (space.getType().equals(Space.SpaceType.Property)) { //TODO: implement getType
            handleRailroad(player, space);
        } else if (space.getType().equals(Space.SpaceType.GoToRestArea)) {
            JOptionPane.showMessageDialog(null, player.getToken() + " landed on Rest Area.", "Rest Area", JOptionPane.INFORMATION_MESSAGE);
        } else if (space.getType().equals(Space.SpaceType.GoToButte)) {
            JOptionPane.showMessageDialog(null, player.getToken() + " must go to Butte! Oh the horror!!", "Go to Butte", JOptionPane.INFORMATION_MESSAGE);
            player.move(boardArray[9]); // move them to Butte
        } else if (space.getType().equals(Space.SpaceType.Butte)) {
            JOptionPane.showMessageDialog(null, player.getToken() + " is just visiting Butte.", "Just Visiting", JOptionPane.INFORMATION_MESSAGE);
        } else if (space.getType().equals(Space.SpaceType.LoseATurn)) {
            JOptionPane.showMessageDialog(null, player.getToken() + "Lost their turn", "Lose a Turn", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // it is not a railroad, rest area, go to butte or butte -- do nothing
        }
    }

    /**
     * Options for a player to take when they land on a property
     * @param player the player landing on the property
     * @param property the property landed on
     */
    private void handlePropertyLanding(Player player, Property property) {
        if (property.getOwner() == null) {
            int buyProperty = JOptionPane.showConfirmDialog(null,
                    "Would you like to buy " + property.getName() + " for $" + property.getPrice() + "?",
                    "Buy Property",
                    JOptionPane.YES_NO_OPTION);

            if (buyProperty == JOptionPane.YES_OPTION) {
                if (player.getMoney() >= property.getPrice()) {
                    player.buyProperty(property);
                } else {
                    // Mortgage properties if player can't afford
                    mortgageToAfford(player, property.getPrice());

                    // After mortgaging, check if the player has enough money now
                    if (player.getMoney() >= property.getPrice()) {
                        player.buyProperty(property);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You still don't have enough money to buy this property.",
                                "Insufficient Funds",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } else if (!property.getOwner().equals(player)) {
            player.payRent(property.getOwner(), property.getRent());
        }
    }

    /**
     * Popup to have players mortgage properties until they can afford a required amount of money
     * @param player the player to choose properties from
     * @param requiredAmount the amount the player needs
     */
    private void mortgageToAfford(Player player, int requiredAmount) {
        while (player.getMoney() < requiredAmount) {
            List<Property> unmortgagedProperties = player.getUnmortgagedProperties();

            if (unmortgagedProperties.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "You have no properties left to mortgage.",
                        "No Available Properties",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Convert properties to String list for selection
            String[] propertyChoices = new String[unmortgagedProperties.size()];
            for (int i = 0; i < unmortgagedProperties.size(); i++) {
                propertyChoices[i] = unmortgagedProperties.get(i).getName() + " (Mortgage Value: $" + unmortgagedProperties.get(i).getPrice() / 2 + ")";
            }
            String chosenProperty = (String) JOptionPane.showInputDialog(null,
                    "You need more money! Choose a property to mortgage:",
                    "Mortgage Property",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    propertyChoices,
                    propertyChoices[0]);

            if (chosenProperty == null) {
                break; // welp, guess they want to lose...
            }
            // Find the selected property and mortgage it
            for (Property p : unmortgagedProperties) {
                if (chosenProperty.startsWith(p.getName())) {
                    player.mortgageProperty(p);
                    break;
                }
            }
        }
    }

    /**
     * Handle landing on a railroad
     * @param player the player landing on the railroad
     * @param space the railroad
     */
    private void handleRailroad(Player player, Space space) {
        Property railroad = (Property) space;

        if (railroad.getOwner() == null) {
            // Offer to buy the railroad
            int buyRailroad = JOptionPane.showConfirmDialog(null,
                    "Would you like to buy " + railroad.getName() + " for $" + railroad.getPrice() + "?",
                    "Buy Railroad",
                    JOptionPane.YES_NO_OPTION);

            if (buyRailroad == JOptionPane.YES_OPTION && player.getMoney() >= railroad.getPrice()) {
                player.buyProperty(railroad);
                JOptionPane.showMessageDialog(null,
                        player.getToken() + " purchased " + railroad.getName() + " for $" + railroad.getPrice(),
                        "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
            } else if (buyRailroad == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null,
                        "You don't have enough money to buy this railroad!",
                        "Not Enough Funds", JOptionPane.WARNING_MESSAGE);
            }
        } else if (!railroad.getOwner().equals(player)) {
            // Pay rent based on the number of railroads the owner has
            int rent = railroad.getOwner().calculateRailroadRent();
            player.payRent(railroad.getOwner(), rent);

            JOptionPane.showMessageDialog(null,
                    player.getToken() + " paid $" + rent + " in railroad fees to " + railroad.getOwner().getToken(),
                    "Rent Paid", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * Roll the dice randomly
     * @return the dice roll
     */
    public int rollDice() {
        return dice.nextInt(6) + 1 + dice.nextInt(6) + 1;
    }

    /**
     * Start a new turn with the button click
     */
    private void startNewTurn() {
        newTurn = true;
        nextTurn();
    }

    /**
     * Allow every player to roll dice in a "turn"
     */
    private void nextTurn() {
        // Loop through each player and allow them to take a turn
        for (currentPlayerIndex = 0; currentPlayerIndex < players.length; currentPlayerIndex++) {
            System.out.println("Player " + (currentPlayerIndex + 1) + "'s turn.");
            takeTurn();
        }

        // Round complete, disable turns until "New Turn" button is clicked
        newTurn = false;

        // Show message to indicate that all players have taken their turn
        JOptionPane.showMessageDialog(board,
                "All players have taken their turns. You can trade, buy houses, or end the game before continuing.",
                "Round Complete", JOptionPane.INFORMATION_MESSAGE);
        updatePlayerPanel();

        // Enable the "New Turn" button for the user to start the next round
        newTurnButton.setEnabled(true);

        // Increment turnsTaken to track game progress
        turnsTaken++;
    }

}
    
