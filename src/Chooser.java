import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chooser {
    int x,y,width = 181,height = 262 , displayWidth = 100,displayHeight = 100;
    boolean visible = false;
    static List<Chooser> choserList = new ArrayList<>();
    Image image , displayImage;
    String title,name,description;
    Artifacts atf;
    Entity enm;
    EventThing evn;

    Chooser(String event){
        loadImage(event);
        switch (event){
            case "Skill" :
                this.title = "Event";
                this.name = "Skill Stone";
                this.description = "Stone that adds 5 Attack to your character";
                break;
            case "Chest" :
                this.title = "Event";
                this.name = "Mysterious Chest";
                this.description = "Chest that might give you an artifact";
                break;
            case "Heal"   :
                this.title = "Event";
                this.name = "Health Fountain";
                this.description = "Fountain that adds 1 Max Health to your character";
                break;
            case "Zombie" :
                this.title = "Fight";
                this.name = event;
                this.description = "Begin fight with one Zombie";
                break;
            case "Skeleton" :
                this.title = "Fight";
                this.name = "Skeleton";
                this.description = "Begin fight with one Skeleton";
                break;
            case "Necromancer" :
                this.title = "Fight";
                this.name = event;
                this.description = "Fight with boss";
                break;
            case "Skeleton3" :
                this.title = "Fight";
                this.name = "Skeleton Triplet";
                this.description = "Fight three skeletons at once";
                break;
            default:
                System.out.println("THERE ARE NO SUCH THING AS " + event);

        }
        updatePos();
        visible = true;
    }
    public void loadImage(String name){
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/tornPaper")));
            switch (name){
                case "Chest"       : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestClosed.png")));  break;
                case "Heal"        : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/healFountain.png"))); break;
                case "Skill"       : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/skillStone.png")));   break;
                case "Zombie"      : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/zombie.png")));       break;
                case "Skeleton",
                     "Skeleton3"   : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/skeleton.png")));     break;
                case "Necromancer" : displayImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/necromancer.png")));  break;

            }

        } catch (IOException e) {

        }
    }

    public static void updatePos(){
        switch (choserList.size()){
            case 1 :
                Chooser chooser = choserList.getFirst();
                chooser.x = 1600/2 - chooser.width/2;
                chooser.y = 900/2  - chooser.height/2;
                break;
            case 2 :

        }
        for (int i = 0; i < choserList.size(); i++) {

        }
    }

    public void draw(Graphics2D g2){
        if(visible){
            g2.drawImage(image,x,y,width,height,null);
        }
    }
    public static void drawAll(Graphics2D g2){
        for(Chooser chooser : choserList){
            chooser.draw(g2);
        }
    }
    public static void addNewChooser(String event){
        choserList.add(new Chooser(event));
    }

}
