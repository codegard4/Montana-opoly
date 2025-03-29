package src.main.game;
import src.main.board.Board;
import src.main.player.Wallet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import src.main.board.Property;
import src.main.player.Player;

public class MonopolyGame extends JFrame {
    /**
     * Creates instance of game
     */
    private int width = 1200;
    private int height = 720;
    private int centerHrzntl = (int) (width / 2);
    private int centerVert = (int) (height / 2);
    private JFrame startScreen;
    private JPanel buttons;
    private JPanel image;
    private JButton startGame;
    private JButton rules;
    private JButton about;
    private JButton exit;
    private Board gameBoard;
    
    private static final Color HUNTER_GREEN = new Color(35,133,51);

    private void openGame() {
        startScreen = new JFrame("Montana-opoly");
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setBounds(0, 0, width, height);
    
        Container contentPane = startScreen.getContentPane();
        contentPane.setLayout(new OverlayLayout(contentPane));
        JPanel background = new JPanel();
        background.setLayout(null);
        ImageIcon picture = new ImageIcon("src\\dependencies\\Montana-opoly_Title_Screen.jpg");
        JLabel picLabel = new JLabel(picture);
        picLabel.setBounds(0,0,width,height);
        startGame = new JButton("Start Game");
        startGame.setBounds(2*centerHrzntl - 235, 20,200,30);
        startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        rules = new JButton("Rules & How To Play");
        rules.setBounds(2*centerHrzntl - 235, 60, 200, 30);
        about = new JButton("Credits & About the Team");
        about.setBounds(2*centerHrzntl - 235, 100, 200, 30);
        about.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                teamBio();
            }
        });
        exit = new JButton("Exit Game");
        exit.setBounds(2*centerHrzntl - 235, 140, 200, 30);
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                exit();
            }
        });
        startGame.setOpaque(true);
        rules.setOpaque(true);
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
        background.setBounds(0,0,width-200,height-200);
        contentPane.add(background);
        startScreen.setVisible(true);
    }
    private void startGame() {
        startScreen.dispose(); // Close the start screen
        int numPlayers = getPlayerCount(); // Ask for player count
        gameBoard = new Board(numPlayers); // Pass player count to board
    }

    private int getPlayerCount() {
        Integer[] options = {1, 2, 3, 4};
        return (int) JOptionPane.showInputDialog(
                null,
                "How many players will be playing?",
                "Player Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    private void exit() {
        System.exit(0);
    }
    
    public MonopolyGame(){
        openGame();
    }

    public static void main(String[] args){
        MonopolyGame monopoly = new MonopolyGame();
    }

    private void teamBio(){
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

        try(Scanner bioReader = new Scanner(new File("src\\dependencies\\teamBio.txt"))){
            while(bioReader.hasNext()){
                text.setText(text.getText() + bioReader.nextLine()+"\n");
            }
        }
        catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "File Not Found!", ":(", JOptionPane.ERROR_MESSAGE);
        }
        team.add(close);
        text.setBounds(20, 20, 350, 170);
        close.setBounds(20, 200, 350, 30);
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                bio.dispose();
            }
        });
        team.add(text);
        team.add(close);
        content.add(team);
        team.setBounds(10, 10, 370, 370);
        try {
            Thread.sleep(200);
            bio.setState(java.awt.Frame.ICONIFIED);
            Thread.sleep(200);
            bio.setState(java.awt.Frame.NORMAL);
        }
        catch (InterruptedException e) {

        }
        bio.addWindowListener(new WindowListener(){
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
        bio.setBounds(0, 0, 400, 400);
        bio.setVisible(true);
        
    }

    public int calculateRent(Player p, Property prop) {
        Wallet owner = p.getWallet();
        if (prop.getColor().equals("Mountain")){
            int count = 0;
            for (Property k: owner.getProperties()){
                if (k.getColor().equals("Mountain")){
                    count += 1;
                }
            }
            return (int) (25*Math.pow(2.0, (count-1)));
        }
        else {
            if (prop.getColor().equals("Utility")){
                int count = 0;
                for (Property k: owner.getProperties()){
                    if (k.getColor().equals("Utility")){
                        count += 1;
                    }
                }
                return (int) (4 + 6*(count-1))*gameBoard.rollDice();
            }
            else {
                return prop.getRent();
            }
        }
    }

    public void trade(Player p) {
        JFrame tradeMachine = new JFrame();
        Container tradePane = tradeMachine.getContentPane();
        tradePane.setLayout(null);
        tradeMachine.setBounds(5,5,600,600);
        JPanel p1box = new JPanel();
        JPanel p2box = new JPanel();
        JPanel p1acquire = new JPanel();
        JPanel p2acquire = new JPanel();
        p1box.setBounds(10, 10, 150, 500);
        p2box.setBounds(400, 10, 150, 500);
        p1acquire.setBounds(200, 10, 150, 225);
        p2acquire.setBounds(200, 250, 150, 225);
        


    }
}
