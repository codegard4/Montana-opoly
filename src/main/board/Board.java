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

// TODO: add javadocs

/**
 *
 */
public class Board extends JFrame {

    private JFrame board;
    private JPanel playerPanel;
    private ImageIcon gameBoard = new ImageIcon("src\\dependencies\\fullBoard.jpg");
    private Space[] boardArray;
    private Map<Rectangle, Space> viewMap;
    private static final int WIDTH = 775;
    private static final int HEIGHT = 775;
    private Player[] players;
    // TODO: change to Montana themed tokens
    private final List<String> tokens = new ArrayList<>();
    private int turns =  10;
    private int currentPlayerIndex = 0; // start with the first player
    private Random dice = new Random();

    /**
     *
     * @param numPlayers
     */
    public Board(int numPlayers) {
        populateTokens();
        board = new JFrame("Montana-opoly");
        board.setBounds(0,0,WIDTH,HEIGHT);
        boardArray = new Space[40];
        // load the board spaces
        loadSpaces();
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
                // TODO Auto-generated method stub
                clickProperty(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }
        });
        board.paintComponents(getGraphics());
        board.setVisible(true);
        playGame();
    }

    public static void main(String[] args) {
        Board testBoard = new Board(0);
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

    /**
     *
     */
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + "'s turn!", "Turn Notification", JOptionPane.INFORMATION_MESSAGE);

        int roll = rollDice();
        JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " rolled a " + roll, "Dice Roll", JOptionPane.INFORMATION_MESSAGE);

        int currentIndex = currentPlayer.getSpace().getIndex();
        currentIndex = (currentIndex + roll) % boardArray.length;
        Space landedSpace = boardArray[currentIndex];
        currentPlayer.move(landedSpace);

        updatePlayerPanel();
        // we landed on a property
        if (landedSpace instanceof Property) {
            Property landedProperty = (Property) landedSpace;
            handlePropertyLanding(currentPlayer, landedProperty);
        } else {
            JOptionPane.showMessageDialog(null, currentPlayer.getToken() + " landed on " + landedSpace.getName(), "Player Move", JOptionPane.INFORMATION_MESSAGE);
        }

        nextTurn();
    }

    private void handlePropertyLanding(Player player, Property property) {
        if (property.getOwner() == null) {
            int buyProperty = JOptionPane.showConfirmDialog(null,
                    "Would you like to buy " + property.getName() + " for $" + property.getPrice() + "?",
                    "Buy Property",
                    JOptionPane.YES_NO_OPTION);

            if (buyProperty == JOptionPane.YES_OPTION && player.getMoney() >= property.getPrice()) {
                player.buyProperty(property);
                JOptionPane.showMessageDialog(null, player.getToken() + " bought " + property.getName() + "!", "Purchase Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Purchase declined!", "Purchase", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (!property.getOwner().equals(player)) {
            int rent = property.getRent();
            player.payRent(property.getOwner(), rent); //TODO: implement
            JOptionPane.showMessageDialog(null, player.getToken() + " paid $" + rent + " in rent to " + property.getOwner().getToken(), "Rent Payment", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     *
     * @return
     */
    private int rollDice() {
        return dice.nextInt(6) + 1 + dice.nextInt(6) + 1;
    }

    /**
     *
     */
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        takeTurn();
    }
}
    
