package src.main.board;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The Space class represents a space on the game board. Each space has a name,
 * an image, a type, and an index indicating its position on the board.
 */
public class Space {
    private String spaceName;
    private ImageIcon spaceImg;
    private Rectangle clickPane;

    /**
     * Enum representing the different types of spaces on the board.
     */
    public enum SpaceType {
        Property,
        RestArea,
        GoToRestArea,
        GoToButte,
        Butte,
        GO,
        Chance,
        CommunityChest,
        LoseATurn
    }

    private SpaceType spaceType;
    private int index;

    /**
     * Constructs a new Space object.
     *
     * @param spaceType The type of the space.
     * @param spaceName The name of the space.
     * @param imgFile The filename of the image representing the space.
     * @param index The index of the space on the board.
     */
    public Space(String spaceType, String spaceName, String imgFile, int index) {
        this.spaceType = SpaceType.valueOf(spaceType);
        this.spaceName = spaceName;
        this.index = index; // Track the numerical position of the space for movement logic
        this.spaceImg = new ImageIcon(Paths.get("src", "dependencies", "propertyImages", imgFile).toString());
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
     * Sets the clickable area of the space.
     *
     * @param coords An array defining the rectangular click area coordinates.
     */
    public void setClickPane(int[] coords) {
        this.clickPane = new Rectangle(new Point(coords[0], coords[1]), new Dimension(coords[2]-coords[0], coords[7]-coords[3]));
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
        view.setBounds(200,200,310,525);
        view.setLayout(null);
        Container viewPane = view.getContentPane();
        JLabel viewLabel = new JLabel(spaceImg);
        viewLabel.setBounds(5,5,300,515);
        viewPane.add(viewLabel);
        view.paintComponents(view.getGraphics());
        view.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = new Point(e.getX(), e.getY());
                if(!view.contains(clickPoint)){
                    view.dispose();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
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
