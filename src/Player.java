import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{
     List<Artifacts> ownedArt = new ArrayList<>();

    Player(GamePanel gp) {
        super(gp);
    }

    public void drawArtifacts(Graphics2D g2){
        for(Artifacts art : ownedArt){
            art.draw(g2);
        }
    }

    public void activateArtifacts(int num){
        for(Artifacts art : ownedArt){art.checkCondition(num);}
    }

}
