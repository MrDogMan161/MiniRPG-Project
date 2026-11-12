package Game.Gameplay;

import Game.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{
     public List<Artifacts> ownedArt = new ArrayList<>();
     int coins;
     String coinCount = "Coins: "+coins;

    public Player(GamePanel gp) {
        super(gp);
    }

    public void drawArtifacts(Graphics2D g2){
        for(Artifacts art : ownedArt){
            art.draw(g2,gp.alpha);
        }
    }

    public void activateArtifacts(int num){
        for(Artifacts art : ownedArt){art.checkCondition(num);}
    }

}
