package src.main.game;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;

import src.main.board.Property;
import src.main.player.Player;
import src.main.player.Wallet;

public class TradeMachine {
    private JFrame machine;
    private Container content;
    private JPanel pWallet;
    private JPanel partnerWallet;
    private JPanel tradeCenter;
    private ListModel<Property> pList;
    private ListModel<Property> partnerList;
    private JList<String> pPropList;
    private JList<String> partnerPropList;
    private JList<String> pAcquireList;
    private JList<String> partnerAcquireList;
    private JScrollPane pProp;
    private JScrollPane partnerProp;
    private JTextArea pMoney;
    private JTextArea partnerMoney;
    private JScrollPane pAcquire;
    private JScrollPane partnerAcquire;
    private JComboBox<String> partnerComboBox;
    private Player p;
    private List<Player> partners;

    public TradeMachine(Player play, Player[] playPartners) {
        p = play;

        machine = new JFrame();
        machine.setBounds(5,5,700,700);
        machine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        content = machine.getContentPane();
        content.setLayout(new GridLayout(1,3));
        pWallet = new JPanel();
        partnerWallet = new JPanel();
        tradeCenter = new JPanel();
        pWallet.setLayout(null);
        partnerWallet.setLayout(null);
        tradeCenter.setLayout(null);
        
        Wallet pWall = p.getWallet();
        String[] propArray = new String[pWall.getProperties().size()];
        for(int i = 0;i<propArray.length;i++){
            propArray[i] = pWall.getProperties().get(i).shortListing();
        }
        pPropList = new JList<>(propArray);
        pProp = new JScrollPane(pPropList);
        pProp.setBounds(10,10,200,400);
        pWallet.add(pProp);

        String[] tokens = new String[playPartners.length];
        for(int i = 0;i<playPartners.length;i++){
            tokens[i] = playPartners[i].getToken();
        }
        partnerComboBox = new JComboBox<>(tokens);

        partnerComboBox.setSelectedIndex(0);
        partnerComboBox.setBounds(410, 20, 200, 35);
        Player partner = getTradePartner(playPartners, String.valueOf(partnerComboBox.getSelectedItem()));
        Wallet partnerWall = partner.getWallet();
        String[] partnerPropArray = new String[partnerWall.getProperties().size()];
        for(int i = 0;i<propArray.length;i++){
            partnerPropArray[i] = partnerWall.getProperties().get(i).shortListing();
        }
        partnerPropList = new JList<>(partnerPropArray);
        partnerProp = new JScrollPane(partnerPropList);
        partnerProp.setBounds(410,60,200,400);
        partnerWallet.add(partnerComboBox);
        partnerWallet.add(partnerProp);
        machine.add(pWallet);
        machine.add(tradeCenter);
        machine.add(partnerWallet);
        machine.setVisible(true);
    }

    private Player getTradePartner(Player[] p, String token) {
        for(Player k : p){
            if (k.getToken().equals(token)){
                return k;
            }
        }
        return null;
    }
}
