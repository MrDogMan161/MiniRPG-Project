package Game.Gameplay;

import Game.DrawableObject;
import Game.GamePanel;
import Game.Waiter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class EventStructure extends DrawableObject {
    Image image2;
    String name;
    Waiter waiter = new Waiter();
    Waiter waiter2 = new Waiter();

    public EventStructure(GamePanel gp){
        this.gp = gp;
    }

    public void loadImages(String name){
        try {
            image =  loadImage(name);
            image2 = name.equals("Chest")? ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestOpened.png"))) : null;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void spawnStructure(int id){

        switch (id){
            case 2 : name = "Chest" ;break;
            case 3 : name = "Shop"; break;
            case 4 : name = "Heal"; break;
        }
        loadImages(name);
        setSize();

        visible = true;
        velX = 0; velY = 0;
        ChangePos(3);
    }

//    public void doStaff(Player player){
//        if(waiter2.wait(1)){
//            if(image2!=null){image = image2;}
//        }
//        if(waiter.wait(2)){
//            switch (name){
//                case "Chest" :
//                    Chooser.addNewChooser(Artifacts.getArtifact(new Random().nextInt(22)));
//                    Chooser.addNewChooser(Artifacts.getArtifact(new Random().nextInt(22)));
//                break;
//                case "Heal"  :
//                    player.maxHealth++;                   // add 1 health
//                    player.health = player.maxHealth;     // heal to full
//                    player.curHealth =  String.valueOf(player.health);
//                    break;
//                case "Skill" :
//                    player.attack++;
//                    break;
//            }
//            this.visible = false;
//        }
//
//    }

    public int getPosX(int pos){
        return switch (pos){
            case 0 ->   (int) ((47*gp.pixelMult) - (double) width /2); // 39
            case 1,2 -> (int) ((39*gp.pixelMult) - (double) width /2); // 22 137  // 22 137
            case 3 ->   (int) ((211*gp.pixelMult)- (double) width /2); // 203 1268
            case 4,5 -> (int) ((228*gp.pixelMult)- (double) width /2); // 220 1375 // 220 1268
            default -> 0;
        };
    }

    public int getPosY(int pos){
        return switch (pos){
            case 0,3 ->  (int) ((83*gp.pixelMult)-width); // 67
            case 1,4 ->  (int) ((70*gp.pixelMult)-width); // 22 137  // 22 137
            case 2,5 ->  (int) ((96*gp.pixelMult)-width); // 203 1268
            default -> 0;
        };
    }

    public void ChangePos(int pos){
        x = getPosX(pos);
        y = getPosY(pos);
    }
    public static String getEvent(){
        return  switch (new Random().nextInt(2)){
                case 0 -> "Heal";
                case 1 -> "Skill";
                //case 1 -> "Skill";
                default -> "Warrior";
        };


    }
}
