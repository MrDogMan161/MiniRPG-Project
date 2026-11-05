package Game;

import javax.swing.*;


public class Main {


    public static void main(String[] args) {

        GamePanel gp = new GamePanel();

        JFrame frame = new JFrame("MiniRPG");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(gp);
        frame.setIconImage(gp.startButton.loadImage("Skeleton"));
        frame.setVisible(true);
        frame.pack();
        gp.startGameThread();

        //System.out.println(frame.getInsets().top);
    }

}