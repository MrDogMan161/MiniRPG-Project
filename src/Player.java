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

    public void activateArtifacts(){
        for(Artifacts art : ownedArt){
            System.out.println(art.name +" " + art.checkOnStart() + " " + this.getDf());
            if(art.checkOnStart() && !art.active){

                art.active = true;
                this.atBonus = (int) (this.atBonus + (!art.flatOrScale ? art.atBonus : this.attack    * art.atBonus!=0? (art.atBonus-1) : 0));
                this.hpBonus = (int) (this.hpBonus + (!art.flatOrScale ? art.hpBonus : this.maxHealth * art.hpBonus!=0? (art.hpBonus-1) : 0));
                this.dfBonus = (int) (this.dfBonus + (!art.flatOrScale ? art.dfBonus : this.defence   * art.dfBonus!=0? (art.dfBonus-1) : 0));
                this.mpBonus = (int) (this.mpBonus + (!art.flatOrScale ? art.mpBonus : this.maxMana   * art.mpBonus!=0? (art.mpBonus-1) : 0));

            }
        }
    }

}
