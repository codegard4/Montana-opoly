package src.main.board;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import src.main.player.Player;
import src.main.board.Card;

// TODO: add javadocs

/**
 *
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
    private int currentPlayerIndex = 0; // start with the first player
    private Random dice = new Random();
    private Random randomCard = new Random();

    /**
     *
     * @param numPlayers
     */
    public Board(int numPlayers, int numTurns) {
        turns = numTurns;
        populateTokens();
        board = new JFrame("Montana-opoly");
        board.setBounds(0,0,WIDTH-100,HEIGHT); //TODO: add player panel in the last 100 pixel width of the board
        boardArray = new Space[40];
        // load the board spaces
        loadSpaces();
        loadCards();
       for(Space s: boardArray) {
           System.out.println(s); // check if the spaces are loaded properly
       }
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
        Container boardContent = board.getContentPane();
        boardContent.setLayout(new OverlayLayout(boardContent));
        JPanel boardPanel = new JPanel();
        JLabel boardLabel = new JLabel(gameBoard);
        boardPanel.add(boardLabel);
        boardLabel.setBounds(5,5,WIDTH-10,HEIGHT-10);
        boardContent.add(boardPanel);
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
        board.paintComponents(getGraphics());
        board.setVisible(true);
        playGame();
    }

    public static void main(String[] args) {
        Board testBoard = new Board(0);
    }

    private void loadCards() {
        loadCardFile("src\\dependencies\\cards.txt");
    }

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
     *
     */
    private void populateTokens() {
        // TODO: change to montana themed tokens
        String[] tokensToAdd = {"Car", "Dog", "Hat", "Boat", "Thimble", "Iron"};
        tokens.addAll(Arrays.asList(tokensToAdd));
    }

    /**
     *
     *
     */
    private void setupPlayerPanel() {
        playerPanel = new JPanel();
        //TODO: move the player panel below the board
        playerPanel.setBounds(WIDTH, 0, 200, HEIGHT);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));

        updatePlayerPanel(); // Populate it with player info
    }

    /**
     *
     */
    private void updatePlayerPanel() {
        playerPanel.removeAll();
        //TODO: update properties too
        for (Player p : players) {
            JLabel playerLabel = new JLabel(p.getToken() + " - $" + p.getMoney() + " is on: " + p.getSpace());
            playerPanel.add(playerLabel);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    /**
     *
     * @param playerIndex
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
     *
     */
    private void loadSpaces(){
        try(final Scanner spaceReader = new Scanner(new File("src\\dependencies\\spaceList.txt"))){
            spaceReader.nextLine();
            int i = 0;
            while(spaceReader.hasNext()){
                String[] newSpace = spaceReader.next().split(",");
                if(newSpace[1].equals("Property")){
                    if(newSpace.length > 6){
                        int[] propRent = new int[7];
                        for(int k=5;k<12;k++){
                            propRent[k-5] = Integer.parseInt(newSpace[k]);
                        }
                        boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), propRent, i);
                        i++;
                    }
                    else {
                        if(newSpace.length > 5){
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), Integer.parseInt(newSpace[5]), i);
                            i++;
                        }
                        else{
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), i);
                            i++;
                        }
                    }
                }
                else{
                    boardArray[i] = new Space(newSpace[1], newSpace[0], newSpace[2], i);
                    i++;
                }
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Space File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        try(final Scanner coordReader = new Scanner(new File("src\\dependencies\\spaceCoords.txt"))){
            coordReader.nextLine();
            int i = 0;
            while(coordReader.hasNext()){
                String[] newCoord = coordReader.next().split(",");
                int[] coords = new int[8];
                for(int k=1;k<9;k++){
                    coords[k-1] = Integer.parseInt(newCoord[k]);
                }
                boardArray[i].setClickPane(coords);
                i++;
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Coordinates File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        viewMap = new HashMap<>();
        for (Space c : boardArray){
            viewMap.put(c.getClickPane(), c);
        }
    }

    /**
     *
     * @param e
     */
    public void clickProperty(MouseEvent e){
        Point clickPoint = new Point(e.getX(), e.getY());
        for(Rectangle r : viewMap.keySet()){
            if(r.contains(clickPoint)){
                viewMap.get(r).viewProperty();
            }
        }
    }

    /**
     *
     */
    private void playGame() {
        int turnsTaken = 0;
        while(turnsTaken < turns * players.length){
            nextTurn();
        }
    }

    public Space getSpaceAt(int index){
        return boardArray[index];
    }

    public int getBoardSize() {
        return boardArray.length;
    }

    //TODO: trading properties
    //TODO: bankruptcy logic
    //TODO
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + "'s turn!", "Turn Notification", JOptionPane.INFORMATION_MESSAGE);

        int roll = rollDice();
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " rolled a " + roll, "Dice Roll", JOptionPane.INFORMATION_MESSAGE);

        int currentIndex = currentPlayer.getSpace().getIndex();
        int newIndex = (currentIndex + roll) % boardArray.length;

        // Check if player passes GO
        if (newIndex < currentIndex) { // Looping around the board means they passed GO
            currentPlayer.passGo();
            JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " passed GO and collected $200!",
                    "Pass GO", JOptionPane.INFORMATION_MESSAGE);
        }
        Space landedSpace = boardArray[newIndex];
        currentPlayer.move(landedSpace);
        updatePlayerPanel();
        handleSpecialSpace(currentPlayer, landedSpace);
        // Draw Chance or Community Chest card if applicable
        if (landedSpace.getType() == SpaceType.CHANCE) {
            drawChanceCard(currentPlayer);
        } else if (landedSpace.getType() == SpaceType.COMMUNITY_CHEST) {
            drawCommunityChestCard(currentPlayer);
        }

        nextTurn();
    }


    public void drawChanceCard(Player player) {
        if (chanceCards.isEmpty()) return;
        Card drawnCard = chanceCards.get(randomCard.nextInt(chanceCards.size()));
        drawnCard.applyEffect(player, this);
    }

    public void drawCommunityChestCard(Player player) {
        if (communityChestCards.isEmpty()) return;
        Card drawnCard = communityChestCards.get(randomCard.nextInt(communityChestCards.size()));
        drawnCard.applyEffect(player, this);
    }


    private void handleSpecialSpace(Player player, Space space) {
        if (space instanceof Property) {
            Property property = (Property) space;
            handlePropertyLanding(player, property);
        } else if (space.getType().equals("Railroad")) { //TODO: implement getType
            handleRailroad(player, space);
        } else if (space.getType().equals("Rest Area")) {
            JOptionPane.showMessageDialog(null, player.getToken() + " landed on Rest Area.", "Rest Area", JOptionPane.INFORMATION_MESSAGE);
        } else if (space.getType().equals("Go to Butte")) {
            JOptionPane.showMessageDialog(null, player.getToken() + " must go to Butte! Oh the horror!!", "Go to Butte", JOptionPane.INFORMATION_MESSAGE);
            player.move(boardArray[9]); // move them to Butte
        } else if (space.getType().equals("Butte")) {
            JOptionPane.showMessageDialog(null, player.getToken() + " is just visiting Butte.", "Just Visiting", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (space.getType().equals("Lose A Turn")) {
            JOptionPane.showMessageDialog(null, player.getToken() + "Lost their turn", "Lose a Turn", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            // it is not a railroad, rest area, go to butte or butte -- do nothing
        }
    }

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


    private int rollDice() {
        return dice.nextInt(6) + 1 + dice.nextInt(6) + 1;
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        takeTurn();
    }
}
    
