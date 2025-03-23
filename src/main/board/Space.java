import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import src.main.player.Player;

public class Space {
    private String spaceName;
    private List<Player> playersOnSpace;
    private ImageIcon spaceImg;

    private Rectangle clickPane;
    
    private enum SpaceType {
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

    public Space(String spaceType, String spaceName, String imgFile) {
        this.spaceType = SpaceType.valueOf(spaceType);
        this.spaceName = spaceName;
        this.spaceImg = new ImageIcon("src\\dependencies\\" + imgFile);
        this.playersOnSpace = new LinkedList<>();
    }

    public void setClickPane(int[] coords) {
        this.clickPane = new Rectangle(new Point(coords[0], coords[1]), new Dimension(coords[2]-coords[0], coords[6]-coords[7]));
    }

    public Rectangle getClickPane() {
        return clickPane;
    }

    public void viewProperty() {
        JFrame view = new JFrame();
        view.setBounds(300,300,250,400);
        view.setLayout(null);
        Container viewPane = view.getContentPane();
        JLabel viewLabel = new JLabel(spaceImg);
        viewLabel.setBounds(325,325,200, 350);
        viewPane.add(viewLabel);
        view.paintComponents(view.getGraphics());
        view.setVisible(true);
    }
}
