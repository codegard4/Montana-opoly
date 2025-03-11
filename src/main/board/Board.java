package src.main.board;


public class Board {
    /*
     * Constructor for the Monopoly board.
     * Board is a hollow 11x11 square that will be represented by a 1x44 array.
     *
     */
    private Space[] board;

    public Board() {
        board = new Space[44];
        board[0] = new Space("Go", 0);
        
    }


}

