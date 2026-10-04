import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Artifacts {
    String name, setName;
    boolean active = false,flatOrScale = false; // false = flat ; true =  % ;
    double hpBonus,atBonus,dfBonus,mpBonus;
    int x,y,width = 100,height = 100 , id;
    Image image;
    static GamePanel gp;

    Artifacts(){
    }
    Artifacts(String name,String setName,double hNum,double aNum,double dNum,double mNum, boolean fOs ,int id){
        this.name = name;
        this.setName = setName;
        this.hpBonus = hNum;
        this.atBonus = aNum;
        this.dfBonus = dNum;
        this.mpBonus = mNum;
        this.flatOrScale = fOs;
        this.id = id;
        image = loadImages(id);
    }

    public Image loadImages(int num){
        try{
            return switch (num){
                case 0 -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightShield.png")));
                case 1 -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightSword.png")));
                default-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/skeleton.png")));
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,width,height,null);
    }

    public static Artifacts getArtifact(int num){
        return switch(num){
            case 0 -> new Artifacts("Test Shield",    "Knight",0,0,5,0,false,num);
            case 1 -> new Artifacts("Test Sword" ,    "Knight",0,5,0,0,false,num);
            case 2 -> new Artifacts("Training Arrow","Trainee",1,1,0,0,false,num);
            case 3 -> new Artifacts("Novice Scroll", "Trainee",0,1,0,1,false,num);
            case 4 -> new Artifacts("Wooden Shield", "Trainee",0,1,1,0,false,num);
            case 5 -> new Artifacts("Sharpening Stone", "None",0,1,0,0,false,num);
            case 6 -> new Artifacts("Veteran's Helmet", "None",2,0,0,0,false,num); // must heal not increase health
            case 7 -> new Artifacts("Iron Shield",    "Metal",-5,0,5,0,false,num);
            case 8 -> new Artifacts("Steal Heart",      "Metal",2,0,0,0,false,num);
            case 9 -> new Artifacts("Copper Bracelet", "Metal",0,0,1,0,false,num);
            case 10 -> new Artifacts("Lucky Coin",      "Lucky",0,1.5,0,0,true,num); // on Attack
            case 11 -> new Artifacts("Lucky Dice",      "Lucky",0,2,0,0,true,num); // on Attack

            default -> new Artifacts();
        };
    }
    /* Training Arrow disable range debuff for other classes or add +1 At and Hp for Archer
    // Novice Scroll  disable magic debuff for other classes or add +1 At and Mp for Mage
    // Wooden Shield  disable melee debuff for other classes or add +1 At and Df for Warrior

    // Sharpening Stone add +1 dam
    // Veteran's Helmet +2 hp on kill

    // Iron Shield adds 5 df but removes 5 hp
    // Steal Heart adds 2 hp
    // Copper Bracelet adds + 1 def

    // Lucky Coin every attack has chance to 1.5 its attack
    // Lucky Dice every attack has chance to 2 its attack

    // Berserk Heart +2 at when hp<50%
    // Skull of Suffering when attack gain +1 at stack

    // Fire Scroll attack might put enemy on fire
    // Icicle attacks might freeze enemy causing them to skip turn

    // Magic Crystal +2 Mp
    // Magic Book -1 mp needed for ability
    // Magic Eye if Mp at 1 then all magic attacks cost one mana to use and have +1 dam

    // Silver Sword ignore 1 enemy armor
    // Silver NeckLace healing more effective +1

    // Hunters Hood if don't get hit on turn then on next have +5 dam
    // Beast Fang + dam when low hp

    // Blood Stone vampirizm +1 hp

    // Kings Crown +1 to all
    // Kings Gown +5 Def
    // Kings Sword +5 attack

    // Hero`s Shield + 5 def
    // Fools Hat every battle adds or remove cur characteristic
    // Couple of Dice attack might hit twice
    // Soul Lanter 10% change to revive with half hp
    // Winning Wreath +1 to all after 5 wins while still alive
    // Ancient Blood +10hp
    // Torn Wreath upon daath revive with 1hp
    */

    public static Artifacts getArtifact(String name){
        return switch(name){
            case "Test Shield"     -> getArtifact(0);
            case "Test Sword"      -> getArtifact(1);
            case "Training Arrow"  -> getArtifact(2);
            case "Novice Scroll"   -> getArtifact(3);
            case "Wooden Shield"   -> getArtifact(4);
            case "Sharpening Stone"-> getArtifact(5);
            case "Veteran's Helmet"-> getArtifact(6);
            case "Iron Shield"     -> getArtifact(7);
            case "Steal Heart"     -> getArtifact(8);
            case "Copper Bracelet" -> getArtifact(9);
            case "Lucky Coin"      -> getArtifact(10);
            case "Lucky Dice"      -> getArtifact(11);

            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    // type activation
    // just adds                                        --> on Start
    // in battle on attack       + chance of activation --> on Attack
    // in battle on getting hurt + chance of activation --> on Damaged
    // on turn                   + chance of activation --> on Turn
    // at the end of battle      + chance of activation --> at End

    public boolean checkOnTurn(){
        return switch (id){
            case 1 -> gp.player.getDf() > 0;

            default -> true;
        };
    }
    public boolean checkOnDamaged(){
        return switch (id){
            case 1 -> gp.player.getDf() > 0;

            default -> true;
        };
    }
    public boolean checkOnAttack(){
        return switch (id){
            case 1 -> gp.player.getDf() > 0;

            default -> true;
        };
    }

    public boolean checkOnStart(){
        return switch (id){
            case 1 -> gp.player.getDf() > 0;
            case 2 -> gp.player.name.equals("Archer");
            case 3 -> gp.player.name.equals("Mage");
            case 4 -> gp.player.name.equals("Warrior");

            default -> true;
        };
    }
    public boolean checkAtEnd(){
        return switch (id){
            case 1 -> gp.player.getDf() > 0;

            default -> true;
        };
    }
}
