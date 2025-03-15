import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;

import src.main.player.Player;

public class Space {
    private String spaceName;
    private List<Player> playersOnSpace;
    private ImageIcon spaceImg;
    
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
}
