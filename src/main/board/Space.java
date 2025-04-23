package src.main.board;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The Space class represents a space on the game board. Each space has a name,
 * an image, a type, and an index indicating its position on the board.
 */
public class Space {
    private final String spaceName;
    private final ImageIcon spaceImg;
    private Rectangle clickPane;

    /**
     * Enum representing the different types of spaces on the board.
     */
    public enum SpaceType {
        Property, RestArea, GoToRestArea, GoToButte, Butte, GO, Chance, CommunityChest, LoseATurn
    }

    private final SpaceType spaceType;
    private final int index;

    /**
     * Constructs a new Space object.
     *
     * @param spaceType The type of the space.
     * @param spaceName The name of the space.
     * @param imgFile   The filename of the image representing the space.
     * @param index     The index of the space on the board.
     */
    public Space(String spaceType, String spaceName, String imgFile, int index) {

        this.spaceType = SpaceType.valueOf(spaceType);
        this.spaceName = spaceName;
        this.index = index; // Track the numerical position of the space for movement logic
        if (imgFile == null) {
            this.spaceImg = null;
        } else {
            this.spaceImg = new ImageIcon(Paths.get("Montana-opoly", "src", "dependencies", "propertyImages", imgFile).toString());
        }
    }

    /**
     * Gets the index of this space on the board.
     *
     * @return The index of the space.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the type of this space.
     *
     * @return The space type.
     */
    public SpaceType getType() {
        return spaceType;
    }

    /**
     * Sets the clickable area of the space. Used for initializing the board space.
     *
     * @param coords An array defining the rectangular click area coordinates.
     */
    public void setClickPane(int[] coords) {
        this.clickPane = new Rectangle(new Point(coords[0]+65, coords[1]+20), new Dimension(coords[2] - coords[0], coords[7] - coords[3]));
    }

    /**
     * Sets the clickable area of the space. Used for dynamic resizing during game.
     *
     * @param coords An array defining the rectangular click area coordinates.
     */
    public void setClickPane(Point p, int i) {
        if(i > 0 && i <= 9){
            this.clickPane = new Rectangle(new Point(p.x + (9-i)*clickPane.width, p.y + 8*clickPane.width+clickPane.height), new Dimension(clickPane.width, clickPane.height));
        }
        if(i >  10 && i <= 19) {
            this.clickPane = new Rectangle(new Point(p.x, p.y + (19-i)*clickPane.width+clickPane.height), new Dimension(clickPane.width, clickPane.height));
        }
        if(i > 20 && i <= 29) {

        }
        if(i > 30 && i <= 39) {

        }
        if(i == 0){
            this.clickPane = new Rectangle(new Point(p.x, p.y), new Dimension(clickPane.width, clickPane.height));
        }
        if(i == 10){

        }
        if(i == 20){

        }
        if(i == 30){

        }
    }

    /**
     * Gets the clickable area of this space.
     *
     * @return The clickable area as a Rectangle object.
     */
    public Rectangle getClickPane() {
        return clickPane;
    }

    /**
     * Opens a JFrame to display the property details associated with this space.
     */
    public void viewProperty() {
        JFrame view = new JFrame();
        view.setBounds(200, 200, 325, 525);
        view.setLayout(null);
        Container viewPane = view.getContentPane();
        JLabel viewLabel = new JLabel(spaceImg);
        viewLabel.setBounds(5, 5, 300, 515);
        viewPane.add(viewLabel);
        view.paintComponents(view.getGraphics());
        view.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = new Point(e.getX(), e.getY());
                if (!view.contains(clickPoint)) {
                    view.dispose();
                }
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
        view.setVisible(true);
    }

    /**
     * Returns the name of the space as a string.
     *
     * @return The name of the space.
     */
    @Override
    public String toString() {
        return spaceName;
    }
}
