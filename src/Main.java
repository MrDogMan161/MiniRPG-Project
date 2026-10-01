import javax.swing.*;
import java.awt.*;


public class Main {


    public static void main(String[] args) {

        GamePanel gp = new GamePanel();

        JFrame frame = new JFrame("MiniRPG");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(gp);
        frame.setVisible(true);
        frame.pack();
        gp.startGameThread();

        //System.out.println(frame.getInsets().top);
    }

}