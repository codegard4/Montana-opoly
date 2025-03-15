import javax.swing.*;

import java.awt.Container;
import java.io.*;
import java.util.Scanner;

class Board extends JFrame {
    /*
     * Constructor for the Monopoly board.
     * Board is a hollow 11x11 square that will be represented by a 1x44 array.
     * 
     */
    private JFrame board;
    private ImageIcon gameBoard = new ImageIcon("Montana-opoly\\src\\dependencies\\fullBoard.png");
    private Space[] boardArray;
    private static final int WIDTH = 775;
    private static final int HEIGHT = 775;
    
    public Board() {
        board = new JFrame("Montana-opoly");
        board.setBounds(0,0,WIDTH,HEIGHT);
        boardArray = new Space[40];
        loadSpaces();
        Container boardContent = board.getContentPane();
        boardContent.setLayout(new OverlayLayout(boardContent));
        JPanel boardPanel = new JPanel();
        JLabel boardLabel = new JLabel(gameBoard);
        boardPanel.add(boardLabel);
        boardLabel.setBounds(5,5,WIDTH-10,HEIGHT-10);
        boardContent.add(boardPanel);
        board.paintComponents(getGraphics());
        board.setVisible(true);
    }

    public static void main(String[] args) {
        Board testBoard = new Board();
    }

    private void loadSpaces(){
        try(final Scanner spaceReader = new Scanner(new File("src\\dependencies\\spaceList.txt"))){
            spaceReader.nextLine();
            int i = 0;
            while(spaceReader.hasNext()){
                String[] newSpace = spaceReader.next().split(",");
                boardArray[i] = new Space(newSpace[1], newSpace[0]);
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
    
