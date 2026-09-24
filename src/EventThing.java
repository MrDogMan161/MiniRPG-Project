import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class EventThing {
    int x,y,width,height;
    GamePanel gp;
    Image image,image2;
    boolean visible = false;
    String type;
    Waiter waiter = new Waiter();

    EventThing(GamePanel gp){
        this.gp = gp;
    }

    public void loadImage(String name){
        try {
            switch (name){
                case "Chest" :
                    image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestClosed.png")));
                    image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestOpened.png")));
                    break;
                case "Heal" :
                    image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/healFountain.png")));
                    break;
                case "Skill" :
                    image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/skillStone.png")));
                    break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(){
        if(image!=null){
            width = (int) (image.getWidth(null) * gp.globMult);
            height = (int) (image.getHeight(null) * gp.globMult);
        }
    }

    public void draw(Graphics2D g2){
        if(visible){
            g2.drawImage(image,x,y,width,height,null);
        }
    }

    public void spawnThing(String name){
        loadImage(name);
        this.type = name;
        visible = true;
        this.ChangePos(3);
    }

    public void doStaff(Player player){
        if(waiter.wait(5)){
            switch (type){
                case "Chest" : image = image2; player.attack++; break;
                case "Heal"  : player.maxHealth++; break;
                case "Skill" : player.addAttack(new Random().nextInt(16)); break;
            }
            gp.curEvent = null;
            this.visible = false;
        }

    }

    public int getPosX(int pos){
        return switch (pos){
            case 0 -> (int) (gp.getWidth()/6.66); // 39
            case 1,2 -> (int) (gp.getWidth()/11.68); // 22 137  // 22 137
            case 3 -> (int) (gp.getWidth()/1.26); // 203 1268
            case 4,5 -> (int) (gp.getWidth()/1.16); // 220 1375 // 220 1268
            default -> 0;
        };
    }

    public int getPosY(int pos){
        return switch (pos){
            case 0,3 ->  (int) (gp.getHeight()/2.15); // 39
            case 1,4 -> (int) (gp.getHeight()/2.77); // 22 137  // 22 137
            case 2,5 -> (int) (gp.getHeight()/1.76); // 203 1268
            default -> 0;
        };
    }

    public void ChangePos(int pos){
        switch (pos){
            case 0 :
                x = (int) (gp.getWidth()/6.66); // 39
                y = (int) (gp.getHeight()/2.15); // 67
                break;
            case 1:
                x = (int) (gp.getWidth()/11.68); // 22 137
                y = (int) (gp.getHeight()/2.77); // 52 325
                break;
            case 2:
                x = (int) (gp.getWidth()/11.68); // 22 137
                y = (int) (gp.getHeight()/1.76); // 82 512
                break;
            case 3:
                x = (int) (gp.getWidth()/1.26); // 203 1268
                y = (int) (gp.getHeight()/2.15); // 67 512
                break;
            case 4:
                x = (int) (gp.getWidth()/1.16); // 220 1375
                y = (int) (gp.getHeight()/2.77); // 52 512
                break;
            case 5:
                x = (int) (gp.getWidth()/1.16); // 220 1268
                y = (int) (gp.getHeight()/1.76); // 82 512
                break;


        }
    }

}
