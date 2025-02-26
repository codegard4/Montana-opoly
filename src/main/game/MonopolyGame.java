import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.random.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class MonopolyGame extends JFrame {
    /**
     * Creates instance of game
     */
    private int width = 1280;
    private int height = 750;
    private int centerHrzntl = (int) (width / 2);
    private int centerVert = (int) (height / 2);
    private JFrame startScreen;
    private Image background;
    private JPanel buttons;
    private JPanel image;
    private JButton startGame;
    private JButton rules;
    private JButton about;
    private JButton exit;

    private void startGame() {
        JFrame startScreen = new JFrame("Montana-opoly");
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setBounds(0, 0, width, height);
    
        Container contentPane = startScreen.getContentPane();
        image = new JPanel();
        try {
            background = ImageIO.read(new File("src\\dependencies\\TitleScreen.png")); 
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * Can't get the f---ing machine to set the background, javax.swing can eat my nuts
         */
        // paintComponents(image.getGraphics(), background);
        buttons = new JPanel();
        buttons.setLayout(null);
        startGame = new JButton("Start Game");
        startGame.setBounds(centerHrzntl-415,650,200,30);
        rules = new JButton("Rules & How To Play");
        rules.setBounds(centerHrzntl - 205, 650, 200, 30);
        about = new JButton("Credits & About the Team");
        about.setBounds(centerHrzntl + 5, 650, 200, 30);
        exit = new JButton("Exit Game");
        exit.setBounds(centerHrzntl + 215, 650, 200, 30);
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                exit();
            }
        });
        buttons.add(startGame);
        buttons.add(rules);
        buttons.add(about);
        buttons.add(exit);
        contentPane.add(buttons);
        buttons.setBounds(centerHrzntl - 50, 550, 100, 200);
        
        startScreen.setVisible(true);

    }

    private void exit() {
        System.exit(0);
    }
    
    public MonopolyGame(){
        startGame();
    }

    public static void main(String[] args){
        MonopolyGame monopoly = new MonopolyGame();
    }

    public void paintComponents(Graphics g, Image i) {
        super.paintComponents(g);
        g.drawImage(i, 0, 0, null);
    }
}
