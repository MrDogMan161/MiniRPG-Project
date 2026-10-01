import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{
    int moneyCount=0,expCount=0,expMax=100;
    List<Artifacts> ownedArt = new ArrayList<>();

    Player(GamePanel gp) {
        super(gp);
    }

    public void drawArtifacts(Graphics2D g2){
        for(Artifacts art : ownedArt){
            art.draw(g2);
        }
    }

}
