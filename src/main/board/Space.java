package src.main.board;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import src.main.player.Player;

//TODO: javadocs

public class Space {
    private String spaceName;
//    private List<Player> playersOnSpace;
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
    private int index;

    public Space(String spaceType, String spaceName, String imgFile, int index) {
        this.spaceType = SpaceType.valueOf(spaceType);
        this.spaceName = spaceName;
        this.index = index; // keep track of where the space is numerically so that we can move players around spaces easier
        this.spaceImg = new ImageIcon("src\\dependencies\\propertyImages\\" + imgFile);
//        this.playersOnSpace = new LinkedList<>();
    }
    public int getIndex() {
        return index;
    }

    public void setClickPane(int[] coords) {
        this.clickPane = new Rectangle(new Point(coords[0], coords[1]), new Dimension(coords[2]-coords[0], coords[7]-coords[3]));
    }

    public Rectangle getClickPane() {
        return clickPane;
    }

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
                // TODO Auto-generated method stub
                Point clickPoint = new Point(e.getX(), e.getY());
                if(!view.contains(clickPoint)){
                    view.dispose();
                }
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
        view.setVisible(true);
    }
    @Override
    public String toString(){
        return spaceName;
    }
}
