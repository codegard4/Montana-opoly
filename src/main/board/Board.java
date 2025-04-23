package src.main.board;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import javax.swing.*;
import src.main.player.Player;

// ...


/**
 * Board is the container for all of our monopoly game logic
 * Board is called by MonopolyGame and creates a gameboard to play monopoly on
 */
public class Board extends JFrame {

    private final JFrame board;
    private JPanel playerPanel;
    private final ImageIcon gameBoard = new ImageIcon(Paths.get("Montana-opoly", "src", "dependencies", "fullBoard.jpg").toString());
    private final Space[] boardArray;
    private final List<Card> chanceCards = new ArrayList<>();
    private final List<Card> communityChestCards = new ArrayList<>();
    private Map<Rectangle, Space> viewMap;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    private final Player[] players;
    private final int numRealPlayers;
    private final List<String> tokens = new ArrayList<>();
    private final int turns;
    private int turnsTaken = 0;
    private boolean newTurn = true;
    private int currentPlayerIndex = 0; // start with the first player
    private final Random dice = new Random();
    private final Random randomCard = new Random();

    public JButton newTurnButton = new JButton("New Turn");
    public JButton endGameButton = new JButton("End Game");
    public JButton tradeButton = new JButton("Trade");
    public JButton addHousesButton = new JButton("Add Houses");

    /**
     * This is the main method
     *
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        Board testBoard = new Board(2, 5, 1);
        testBoard.playGame();
    }

    /**
     * Board constructor
     *
     * @param numPlayers number of players
     * @param numTurns   number of game turns each player gets
     * @param numBots    number of bots
     */
    public Board(int numPlayers, int numTurns, int numBots) {
        numRealPlayers = numPlayers;
        turns = numTurns;
        populateTokens();
        board = new JFrame("Montana-opoly");
        board.setBounds(0, 0, WIDTH, HEIGHT);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardArray = new Space[40];
        // load the board spaces and cards
        loadSpaces();
        loadCards();

        // Initialize players
        players = new Player[numPlayers + numBots]; // store bots as players with a CPU controller
        for (int i = 0; i < players.length; i++) {
//            System.out.println(i);
            if (i < numPlayers) {
//                System.out.println("Adding real player");
                players[i] = new Player(false);
            } else {
                players[i] = new Player(true);
//                System.out.println("Adding BOT");
            }
            players[i].move(boardArray[0]); // all players start on GO
        }


        // Show Token Selection Dialog for each player
        for (int i = 0; i < numPlayers; i++) {
            showTokenSelectionPopup(i);
        }

        // Give the bots tokens
        assignTokensToBots(numPlayers);
        // Setup player panel
        setupPlayerPanel();
        setupBoard();

        // Listen for each of the buttons to be clicked
        newTurnButton.addActionListener(e -> {
            newTurnButton.setEnabled(false);
            startNewTurn();
        });
        endGameButton.addActionListener(e -> endGame());
        tradeButton.addActionListener(e -> initiateTrade());
        addHousesButton.addActionListener(e -> addHouses());

        board.paintComponents(getGraphics());
        board.setVisible(true);

    }

    /**
     * Gets the current player
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    /**
     * Detects when a property is clicked
     *
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
    public void playGame() {
        while (turnsTaken <= (turns * players.length)) {
            if (newTurn) {
                JOptionPane.showMessageDialog(null, "Turn " + turnsTaken, "Turns Info", JOptionPane.INFORMATION_MESSAGE);
                nextTurn();
            }
        }
        endGame();
    }


    /**
     * Draw a chance card and apply the changes to the player
     *
     * @param player the player drawing the card
     */
    public void drawChanceCard(Player player) {
        if (chanceCards.isEmpty()) return;
        Card drawnCard = chanceCards.get(randomCard.nextInt(chanceCards.size()));
        drawnCard.applyEffect(player, this);
    }

    /**
     * Draw a community chest card and apply the changes to the player
     *
     * @param player the player drawing the card
     */
    public void drawCommunityChestCard(Player player) {
        if (communityChestCards.isEmpty()) return;
        Card drawnCard = communityChestCards.get(randomCard.nextInt(communityChestCards.size()));
        drawnCard.applyEffect(player, this);
    }

    /**
     * Get the space at the index provided
     *
     * @param index the board index
     * @return the space at that index
     */
    public Space getSpaceAt(int index) {
        return boardArray[index];
    }

    /**
     * Get the size of the game board
     *
     * @return length of the baord array
     */
    public int getBoardSize() {
        return boardArray.length;
    }

    /**
     * Roll the dice randomly
     *
     * @return the dice roll
     */
    public int rollDice() {
        return dice.nextInt(6) + 1 + dice.nextInt(6) + 1;
    }

    /**
     * Load all the chance and community chest cards
     */
    private void loadCards() {
        loadCardFile(Paths.get("Montana-opoly", "src", "dependencies", "cards.txt").toString());

    }

    /**
     * Setup the board graphics, buttons, labels, etc.
     */
    private void setupBoard() {
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
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        board.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Point g = boardPanel.getLocation();
                adjustBoard(g);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        adjustBoard(boardPanel.getLocation());
    }

    private void adjustBoard(Point p) {
        for(int i = 0;i < boardArray.length;i++){
            boardArray[i].setClickPane(p, i);
        }
    }

    /**
     * Loads each card from a properly formatted .txt file
     *
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
        Arrays.sort(players, (p1, p2) -> Integer.compare(p2.getTotalWealth(), p1.getTotalWealth()));

        // Build summary string
        StringBuilder summary = new StringBuilder("Game Over! Final Rankings:\n");
        int rank = 1;
        for (Player p : players) {
            summary.append(rank).append(". ").append(p.summarizeMoney()).append("\n");
            rank++;
        }
        // Display final rankings
        JOptionPane.showMessageDialog(null, summary.toString(), "Final Rankings", JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }

    /**
     * Allow two players to make a property trade in between turns -- display the value of each property during the trade
     */
    private void initiateTrade() {
        if (players.length < 2) {
            JOptionPane.showMessageDialog(board, "At least two players are needed to trade.", "Trade Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create dropdowns for selecting players
        Player player1 = (Player) JOptionPane.showInputDialog(board, "Select the first player:", "Player 1", JOptionPane.PLAIN_MESSAGE, null, players, players[0]);
        if (player1 == null) return;

        // Create a filtered list to avoid selecting the same player
        List<Player> otherPlayers = Arrays.stream(players).filter(p -> !p.equals(player1)).toList();
        Player player2 = (Player) JOptionPane.showInputDialog(board, "Select the second player:", "Player 2", JOptionPane.PLAIN_MESSAGE, null, otherPlayers.toArray(), otherPlayers.get(0));
        if (player2 == null) return;

        // Two bots cannot trade so return here
        if (player1.isBot() && player2.isBot()) {
            JOptionPane.showMessageDialog(board, "Trade cancelled — two bot players may not trade with each other.", "Trade Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get properties to trade
        List<Property> p1Props = player1.getUnmortgagedProperties();
        List<Property> p2Props = player2.getUnmortgagedProperties();
        if (p1Props.isEmpty() || p2Props.isEmpty()) {
            JOptionPane.showMessageDialog(board, "One or both players have no properties to trade.", "Trade Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Property p1Prop = (Property) JOptionPane.showInputDialog(board, player1 + ", select a property to trade:", "Player 1's Property", JOptionPane.PLAIN_MESSAGE, null, p1Props.toArray(), p1Props.get(0));

        Property p2Prop = (Property) JOptionPane.showInputDialog(board, player2 + ", select a property to trade:", "Player 2's Property", JOptionPane.PLAIN_MESSAGE, null, p2Props.toArray(), p2Props.get(0));

        // Check if both properties were selected
        if (p1Prop == null || p2Prop == null) {
            JOptionPane.showMessageDialog(board, "Trade cancelled — one player did not select a property to trade.", "Trade Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm trade
        int confirm = JOptionPane.showConfirmDialog(board, player1 + " will trade " + p1Prop.getName() + " ($" + p1Prop.getPrice() + ") with " + player2 + "'s " + p2Prop.getName() + " ($" + p2Prop.getPrice() + ")\n\nProceed?", "Confirm Trade", JOptionPane.YES_NO_OPTION);

        // Bot behavior (bot player will not accept trade if the property they are requested to trade is greater than 1.5x the value of the property they receive)
        boolean tradeAccepted = true;
        if (!player1.isBot() && player2.isBot()) {
            if (((double) p1Prop.getPrice() < (double) p2Prop.getPrice())) {
                tradeAccepted = false;
            }
        }

        // The trade was confirmed, move properties between players
        if (confirm == JOptionPane.YES_OPTION && tradeAccepted) {
            player1.wallet.removeProperty(p1Prop);
            player2.wallet.removeProperty(p2Prop);
            player1.wallet.addProperty(p2Prop);
            player2.wallet.addProperty(p1Prop);
            JOptionPane.showMessageDialog(board, "Trade successful!", "Trade Complete", JOptionPane.INFORMATION_MESSAGE);
            updatePlayerPanel(); // Refresh UI if needed
        } else {
            if (!tradeAccepted) {
                JOptionPane.showMessageDialog(board, player2.getToken() + " is not interested in this trade.", "Trade declined.", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(board, "Trade cancelled.", "Trade Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Allow any player to add houses to their properties between turns.
     */
    private void addHouses() {
        // Select a player
        String[] playerOptions = new String[numRealPlayers];
        for (int i = 0; i < players.length; i++) {
            if (players[i].isBot()) continue;
            playerOptions[i] = players[i].getToken(); // Use token as identifier
        }
        String selectedPlayerToken = (String) JOptionPane.showInputDialog(board, "Select a player to add houses:", "Choose Player", JOptionPane.PLAIN_MESSAGE, null, playerOptions, playerOptions[0]);
        if (selectedPlayerToken == null) return; // Cancelled

        // Find the selected player
        Player selectedPlayer = null;
        for (Player p : players) {
            if (p.getToken().equals(selectedPlayerToken)) {
                selectedPlayer = p;
                break;
            }
        }
        if (selectedPlayer == null) return; // Shouldn't happen, but safety check
        if (selectedPlayer.isBot()) return; // Bots cannot add houses
        List<Property> ownedProperties = selectedPlayer.getUnmortgagedProperties();
        if (ownedProperties.isEmpty()) {
            JOptionPane.showMessageDialog(board, selectedPlayerToken + " doesn't own any properties to build houses on.", "No Properties", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Select a property from the chosen player's owned properties
        String[] propertyOptions = new String[ownedProperties.size()];
        for (int i = 0; i < ownedProperties.size(); i++) {
            Property prop = ownedProperties.get(i);

            // check to see if it is a railroad or utility
            if (prop.canAddHouses()) {
                propertyOptions[i] = prop.getName() + " (Houses: " + prop.getNumHouses() + ")";
            }
        }
        String selectedPropertyName = (String) JOptionPane.showInputDialog(board, "Select a property to add a house for " + selectedPlayerToken + ":", "Choose Property", JOptionPane.PLAIN_MESSAGE, null, propertyOptions, propertyOptions[0]);
        if (selectedPropertyName == null) return; // Cancelled

        // Find the selected property
        for (Property prop : ownedProperties) {
            if (selectedPropertyName.startsWith(prop.getName())) {
                int houseCost = prop.getPrice() / 2;

                if (selectedPlayer.getMoney() >= houseCost) {
                    prop.addHouse(); // Adds the house
                    selectedPlayer.wallet.removeMoney(houseCost); // Subtract the cost
                    JOptionPane.showMessageDialog(board, "Added 1 house to " + prop.getName() + " for $" + houseCost + ".", "House Added", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(board, selectedPlayerToken + " doesn't have enough money to build a house.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
                }
                break;
            }
        }
        updatePlayerPanel();
    }


    /**
     * Add all the tokens that can be selected to the tokens list
     */
    private void populateTokens() {
        String[] tokensToAdd = {"Pickup", "Hay Bale", "Bird Dog", "Fishing Boat", "Pickaxe", "Cowboy Boot"};
        tokens.addAll(Arrays.asList(tokensToAdd));
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
            singlePlayerPanel.setBorder(BorderFactory.createTitledBorder(p.toString()));

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
     *
     * @param playerIndex which player is selecting their token
     */
    private void showTokenSelectionPopup(int playerIndex) {
        // Display a dropdown for token selection
        String[] tokenChoices = tokens.toArray(new String[0]); // we have to pass the JOptionPane a []
        String token = (String) JOptionPane.showInputDialog(board, "Player " + (playerIndex + 1) + ", pick your token:", "Token Selection", JOptionPane.QUESTION_MESSAGE, null, tokenChoices, tokens.getFirst()); // Set default to the first token

        if (token != null) {
            players[playerIndex].setToken(token);
            tokens.remove(token);
        }
    }

    /**
     * Randomly assigns the remaining tokens to the bot players.
     * Assumes human players have already selected their tokens and they have been removed from the `tokens` list.
     */
    private void assignTokensToBots(int numHumanPlayers) {
        List<String> remainingTokens = new ArrayList<>(tokens); // copy to avoid concurrent modification
        Collections.shuffle(remainingTokens); // randomize order

        for (int i = numHumanPlayers; i < players.length; i++) {
            if (!remainingTokens.isEmpty()) {
                String token = remainingTokens.removeFirst(); // get the first available token
                players[i].setToken(token);
                tokens.remove(token); // ensure it's removed from the master token list too
            } else {
                players[i].setToken("BOT" + (i - numHumanPlayers + 1)); // fallback in case tokens run out
            }
        }
    }


    /**
     * Load all the gameboard spaces from a .txt file
     */
    private void loadSpaces() {
        try (final Scanner spaceReader = new Scanner(new File(Paths.get("Montana-opoly", "src", "dependencies", "spaceList.txt").toString()))) {
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
        try (final Scanner coordReader = new Scanner(new File(Paths.get("Montana-opoly", "src", "dependencies", "spaceCoords.txt").toString()))) {
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
     * Current player takes their turn
     */
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        JOptionPane.showMessageDialog(null, currentPlayer.toString() + "'s turn!", "Turn Notification", JOptionPane.INFORMATION_MESSAGE);
        int roll = rollDice();
        JOptionPane.showMessageDialog(null, currentPlayer.toString() + " rolled a " + roll, "Dice Roll", JOptionPane.INFORMATION_MESSAGE);
        int currentIndex = currentPlayer.getSpace().getIndex();
        int newIndex = (currentIndex + roll) % boardArray.length;

        // Check if player passes GO
        if (newIndex < currentIndex) {
            currentPlayer.passGo();
            JOptionPane.showMessageDialog(null, currentPlayer.toString() + " passed GO and collected $200!", "Pass GO", JOptionPane.INFORMATION_MESSAGE);
        }

        // Move the player then handle the new space they moved to
        Space landedSpace = boardArray[newIndex];
        currentPlayer.move(landedSpace);
        updatePlayerPanel();
        handleSpecialSpace(currentPlayer, landedSpace);

        // Draw Chance or Community Chest card if applicable
        if (landedSpace.getType() == Space.SpaceType.Chance) {
            JOptionPane.showMessageDialog(null, currentPlayer.toString() + " Landed on Chance", "Chance", JOptionPane.INFORMATION_MESSAGE);
            drawChanceCard(currentPlayer);
        } else if (landedSpace.getType() == Space.SpaceType.CommunityChest) {
            JOptionPane.showMessageDialog(null, currentPlayer.toString() + " Landed on Community Chest", "Community Chest", JOptionPane.INFORMATION_MESSAGE);
            drawCommunityChestCard(currentPlayer);
        }
    }

    /**
     * Handle a player landing on a space
     *
     * @param player the player landing on the space
     * @param space  the space landed on
     */
    private void handleSpecialSpace(Player player, Space space) {
        if (space instanceof Property property) {
            handlePropertyLanding(player, property);
        } else if (space.getType().equals(Space.SpaceType.Property)) {
            handleRailroad(player, space);
        } else if (space.getType().equals(Space.SpaceType.GoToRestArea)) {
            JOptionPane.showMessageDialog(null, player.toString() + " landed on Rest Area.", "Rest Area", JOptionPane.INFORMATION_MESSAGE);
        } else if (space.getType().equals(Space.SpaceType.GoToButte)) {
            JOptionPane.showMessageDialog(null, player.toString() + " must go to Butte! Oh the horror!!", "Go to Butte", JOptionPane.INFORMATION_MESSAGE);
            player.move(boardArray[9]); // move them to Butte
        } else if (space.getType().equals(Space.SpaceType.Butte)) {
            JOptionPane.showMessageDialog(null, player.toString() + " is just visiting Butte.", "Just Visiting", JOptionPane.INFORMATION_MESSAGE);
        } else if (space.getType().equals(Space.SpaceType.LoseATurn)) {
            JOptionPane.showMessageDialog(null, player.toString() + " Lost their current turn", "Lose a Turn", JOptionPane.INFORMATION_MESSAGE);
        }
        updatePlayerPanel();
    }

    /**
     * Options for a player to take when they land on a property
     *
     * @param player   the player landing on the property
     * @param property the property landed on
     */
    private void handlePropertyLanding(Player player, Property property) {
        if (property.getOwner() == null) {

            // Bot behavior (bot player does not buy property if it costs more than their current cash on hand - $125)
            if (player.isBot()) {
                if (property.getPrice() < player.getMoney() - 125) {
                    player.buyProperty(property);
                } else {
                    JOptionPane.showMessageDialog(board, player.toString() + " has elected not to buy " + property.getName() + ".", "Property not purchased", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                int buyProperty = JOptionPane.showConfirmDialog(null, "Would you like to buy " + property.getName() + " for $" + property.getPrice() + "?", "Buy Property", JOptionPane.YES_NO_OPTION);

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
                            JOptionPane.showMessageDialog(null, "You still don't have enough money to buy this property.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        } else if (!property.getOwner().equals(player)) {
            // Pay rent!
            player.payRent(property.getOwner(), property.getRent());
            JOptionPane.showMessageDialog(null, "This property is already owned -- pay rent of $" + property.getRent() + ".", "Pay Rent", JOptionPane.WARNING_MESSAGE);
        }
        updatePlayerPanel();
    }

    /**
     * Popup to have players mortgage properties until they can afford a required amount of money
     *
     * @param player         the player to choose properties from
     * @param requiredAmount the amount the player needs
     */
    private void mortgageToAfford(Player player, int requiredAmount) {
        while (player.getMoney() < requiredAmount) {
            List<Property> unmortgagedProperties = player.getUnmortgagedProperties();

            if (unmortgagedProperties.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no properties left to mortgage.", "No Available Properties", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convert properties to String list for selection
            String[] propertyChoices = new String[unmortgagedProperties.size()];
            for (int i = 0; i < unmortgagedProperties.size(); i++) {
                propertyChoices[i] = unmortgagedProperties.get(i).getName() + " (Mortgage Value: $" + unmortgagedProperties.get(i).getPrice() / 2 + ")";
            }
            String chosenProperty = (String) JOptionPane.showInputDialog(null, "You need more money! Choose a property to mortgage:", "Mortgage Property", JOptionPane.QUESTION_MESSAGE, null, propertyChoices, propertyChoices[0]);

            if (chosenProperty == null) {
                break;
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
     *
     * @param player the player landing on the railroad
     * @param space  the railroad
     */
    private void handleRailroad(Player player, Space space) {
        Property railroad = (Property) space;

        if (railroad.getOwner() == null) {

            // Offer to buy the railroad
            int buyRailroad = JOptionPane.showConfirmDialog(null, "Would you like to buy " + railroad.getName() + " for $" + railroad.getPrice() + "?", "Buy Railroad", JOptionPane.YES_NO_OPTION);

            if (buyRailroad == JOptionPane.YES_OPTION && player.getMoney() >= railroad.getPrice()) {
                player.buyProperty(railroad);
                JOptionPane.showMessageDialog(null, player.toString() + " purchased " + railroad.getName() + " for $" + railroad.getPrice(), "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
            } else if (buyRailroad == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "You don't have enough money to buy this railroad!", "Not Enough Funds", JOptionPane.WARNING_MESSAGE);
            }
        } else if (!railroad.getOwner().equals(player)) {

            // Pay rent based on the number of railroads the owner has
            int rent = railroad.getOwner().calculateRailroadRent();
            player.payRent(railroad.getOwner(), rent);

            JOptionPane.showMessageDialog(null, player.toString() + " paid $" + rent + " in railroad fees to " + railroad.getOwner().getToken(), "Rent Paid", JOptionPane.INFORMATION_MESSAGE);
        }
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
            takeTurn();
            updatePlayerPanel();
        }

        // Round complete, disable turns until "New Turn" button is clicked
        newTurn = false;

        // Show message to indicate that all players have taken their turn
        JOptionPane.showMessageDialog(board, "All players have taken their turns. You can trade, buy houses, or end the game before continuing.", "Round Complete", JOptionPane.INFORMATION_MESSAGE);
        updatePlayerPanel();

        // Enable the "New Turn" button for the user to start the next round
        newTurnButton.setEnabled(true);

        // Increment turnsTaken to track game progress
        turnsTaken++;
    }
}
    
