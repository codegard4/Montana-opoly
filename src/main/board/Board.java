import javax.swing.*;

import java.awt.Container;
import java.io.*;



class Board extends JFrame {
    /*
     * Constructor for the Monopoly board.
     * Board is a hollow 11x11 square that will be represented by a 1x44 array.
     * 
     */
    private JFrame board;
    private ImageIcon gameBoard = new ImageIcon("src/dependencies/fullBoard.png");
    private Property[] boardArray;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    
    public Board() {
        board = new JFrame("Montana-opoly");
        board.setBounds(0,0,WIDTH,HEIGHT);
        boardArray = new Property[40];
        Container boardContent = board.getContentPane();
        boardContent.setLayout(new OverlayLayout(boardContent));
        JPanel boardPanel = new JPanel();
        JLabel boardLabel = new JLabel(gameBoard);
        boardPanel.add(boardLabel);
        boardLabel.setBounds(5,5,WIDTH-10,HEIGHT-10);
        boardContent.add(boardPanel);
    }

    public static void main(String[] args) {
        Board testBoard = new Board();
        testBoard.setVisible(true);
    }


}
    
