import javax.swing.*;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.Rectangle;


class Board extends JFrame {
    /*
     * Constructor for the Monopoly board.
     * Board is a hollow 11x11 square that will be represented by a 1x44 array.
     * 
     */
    private JFrame board;
    private ImageIcon gameBoard = new ImageIcon("Montana-opoly\\src\\dependencies\\fullBoard.jpg");
    private Space[] boardArray;
    private Map<Rectangle, Space> viewMap;
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
        board.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Click event occurred at (" + e.getX() + "," + e.getY() +")");
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
                if(newSpace[1].equals("Property")){
                    if(!newSpace[6].equals("")){
                        int[] propRent = new int[7];
                        for(int k=5;k<12;k++){
                            propRent[k-5] = Integer.parseInt(newSpace[k]);
                        }
                        boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), propRent);
                        i++;
                    }
                    else {
                        if(!newSpace[5].equals("")){
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]), Integer.parseInt(newSpace[5]));
                            i++;
                        }
                        else{
                            boardArray[i] = new Property(newSpace[0], newSpace[2], newSpace[3], Integer.parseInt(newSpace[4]));
                            i++;
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
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
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Not Found!", "Game Initialization Error", JOptionPane.ERROR_MESSAGE);
        }

        viewMap = new HashMap<>();
        for (Space c : boardArray){
            viewMap.put(c.getClickPane(), c);
        }
    }

    public void clickProperty(MouseEvent e){
        Point clickPoint = new Point(e.getX(), e.getY());
        for(Rectangle r : viewMap.keySet()){
            if(r.contains(clickPoint)){
                viewMap.get(r).viewProperty();
            }
        }
    }


}
    
