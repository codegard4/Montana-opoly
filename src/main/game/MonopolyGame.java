package src.main.game;

import src.main.board.Board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.*;

/**
 * The MonopolyGame class represents the main game window for Montana-opoly.
 * It manages the game setup, player interactions, and the overall game flow.
 */
public class MonopolyGame {

    @SuppressWarnings("FieldCanBeLocal")
    private final int width = 1200;
    @SuppressWarnings("FieldCanBeLocal")
    private final int height = 720;
    private JFrame startScreen;
    private int[] gameParams = null;
    private static final Color HUNTER_GREEN = new Color(35, 133, 51);

    /**
     * Main method for monopoly game which calls monopoly game and then board with the arguments
     * that are passed from monopoly game
     *
     * @param args arguments from the command line that are not used
     */
    public static void main(String[] args) {
        MonopolyGame game = new MonopolyGame();
        while (game.getGameParams() == null) {
            try {
                Thread.sleep(100); // Check every 100ms
            } catch (InterruptedException ignore) {
            }
        }
        if (game.getGameParams() != null) {
            int players = game.getGameParams()[0];
            int turns = game.getGameParams()[1];
            int bots = game.getGameParams()[2];

            Board board = new Board(players, turns, bots);
            board.playGame();
        }
    }

    public int[] getGameParams() {
        return gameParams;
    }


    /**
     * Constructor initializes the game and opens the start screen.
     */
    public MonopolyGame() {
        openGame();
    }

    /**
     * Opens the start screen of the game, allowing the player to start or view game details.
     */
    private void openGame() {
        startScreen = new JFrame("Montana-opoly");
        startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startScreen.setBounds(0, 0, width, height);

        Container contentPane = startScreen.getContentPane();
        contentPane.setLayout(new OverlayLayout(contentPane));
        JPanel background = new JPanel();
        background.setLayout(null);
        ImageIcon picture = new ImageIcon(Paths.get("Montana-opoly", "src", "dependencies", "Montana-opoly_Title_Screen.jpg").toString());
        JLabel picLabel = new JLabel(picture);
        picLabel.setBounds(0, 0, width, height);

        JButton startGame = new JButton("Start Game");
        int centerHrzntl = (int) width / 2;
        startGame.setBounds(2 * centerHrzntl - 235, 20, 200, 30);
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        JButton rules = new JButton("Rules & How To Play");
        rules.setBounds(2 * centerHrzntl - 235, 100, 200, 30);

        rules.setOpaque(true);
        rules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rules();
            }
        });
        JButton about = new JButton("Credits & About the Team");
        about.setBounds(2 * centerHrzntl - 235, 60, 200, 30);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamBio();
            }
        });

        JButton exit = new JButton("Exit Game");

        exit.setBounds(2 * centerHrzntl - 235, 140, 200, 30);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        startGame.setOpaque(true);
        about.setOpaque(true);
        exit.setOpaque(true);

        startGame.setBackground(HUNTER_GREEN);
        rules.setBackground(HUNTER_GREEN);
        about.setBackground(HUNTER_GREEN);
        exit.setBackground(HUNTER_GREEN);
        startGame.setForeground(Color.WHITE);
        rules.setForeground(Color.WHITE);
        about.setForeground(Color.WHITE);
        exit.setForeground(Color.WHITE);

        background.add(startGame);
        background.add(rules);
        background.add(about);
        background.add(exit);
        background.add(picLabel);
        background.setBounds(0, 0, width - 200, height - 200);
        contentPane.add(background);
        startScreen.setVisible(true);
    }

    /**
     * Starts the game by closing the start screen and initializing the board.
     */
    private void startGame() {
        startScreen.dispose();
        int numPlayers = getPlayerCount();
        int numTurns = getNumberTurns();
        int numBots = getBotCount(5 - numPlayers);

        if ((numPlayers + numBots) < 2) {
            numBots++;
            JOptionPane.showMessageDialog(null, "Must have at least 2 players -- adding a bot", "Too Few Players", JOptionPane.WARNING_MESSAGE);
        }
        if (numPlayers + numBots > 4) {
            numBots = 4 - numPlayers;
            JOptionPane.showMessageDialog(null, "Number of players must be <= 4 -- removing bot(s)", "Too Many Players", JOptionPane.WARNING_MESSAGE);
        }
        gameParams = new int[]{numPlayers, numTurns, numBots};
    }

    /**
     * Get the number of bots to play with
     *
     * @return the number of bots that is selected
     */
    private int getBotCount(int maxBots) {
        Integer[] options = new Integer[maxBots];
        for (int i = 0; i < maxBots; i++) {
            options[i] = i;
        }
        return (int) JOptionPane.showInputDialog(null, "How many bots will play?", "Bot Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    /**
     * Prompts the user to select the number of turns for the game.
     *
     * @return the number of turns selected
     */
    private int getNumberTurns() {
        Integer[] options = {5, 10, 20, 30, 50};
        return (int) JOptionPane.showInputDialog(null, "How many turns will the game last?", "Turn Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    /**
     * Prompts the user to select the number of players for the game.
     *
     * @return the number of players selected
     */
    private int getPlayerCount() {
        Integer[] options = {1, 2, 3, 4};
        return (int) JOptionPane.showInputDialog(null, "How many players will be playing? (Max 4 real & CPU)", "Player Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    /**
     * Exits the game application.
     */
    private void exit() {
        System.exit(0);
    }


    /**
     * Loads and displays the team biography screen.
     */
    private void teamBio() {
        startScreen.dispose();
        JFrame bio = new JFrame("Team Bio");
        Container content = bio.getContentPane();

        content.setLayout(null);
        JPanel team = new JPanel();
        JTextArea text = new JTextArea();
        JButton close = new JButton("Close");
        text.setText("");
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        try (Scanner bioReader = new Scanner(new File(Paths.get("Montana-opoly", "src", "dependencies", "teamBio.txt").toString()))) {
            while (bioReader.hasNext()) {
                text.setText(text.getText() + bioReader.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", ":(", JOptionPane.ERROR_MESSAGE);
        }
        boundarySet(bio, content, team, text, close);

    }

    public void rules() {
        startScreen.dispose();
        JFrame rules = new JFrame("Rules");
        Container content = rules.getContentPane();
        content.setLayout(null);
        JPanel team = new JPanel();
        JTextArea text = new JTextArea();
        JButton close = new JButton("Close");
        text.setText("");
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        
        try (Scanner bioReader = new Scanner(new File(Paths.get("Montana-opoly", "src", "dependencies", "rules.txt").toString()))) {
            while (bioReader.hasNext()) {
                text.setText(text.getText() + bioReader.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", ":(", JOptionPane.ERROR_MESSAGE);
        }
        boundarySet(rules, content, team, text, close);
    }

    private void boundarySet(JFrame rules, Container content, JPanel team, JTextArea text, JButton close) {
        team.setLayout(null);
        team.add(close);
        close.setBounds(20, 420, 480, 30);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rules.dispose();
            }
        });
        JScrollPane textPane = new JScrollPane(text);
        textPane.setBounds(20, 20, 480, 380);
        team.add(textPane);
        team.add(close);
        content.add(team);
        team.setBounds(10, 10, 500, 500);
        try {
            Thread.sleep(200);
            rules.setState(Frame.ICONIFIED);
            Thread.sleep(200);
            rules.setState(Frame.NORMAL);
        } catch (InterruptedException ignored) {
        }
        rules.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                openGame();
            }
        });
        rules.setBounds(0, 0, 550, 550);
        rules.setVisible(true);
    }
}
