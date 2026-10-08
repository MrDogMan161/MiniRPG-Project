import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Artifacts {
    String name, setName,condDesc;
    boolean active = false,flatOrScale = false; // false = flat ; true =  % ;
    double hpBonus,atBonus,dfBonus,mpBonus;
    int x,y,width = 100,height = 100 , id;
    Image image;
    static GamePanel gp;

    Artifacts(){
    }
    Artifacts(String name,String setName,double health,double attack,double defence,double mana, boolean fOs ,int id){
        this.name = name;
        this.setName = setName;
        this.hpBonus = health;
        this.atBonus = attack;
        this.dfBonus = defence;
        this.mpBonus = mana;
        this.flatOrScale = fOs;
        this.id = id;
        condDesc = getDescription();
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
            case 0 -> new Artifacts("Test Shield",        "Knight", 0,0,5,0,  false,num);
            case 1 -> new Artifacts("Test Sword" ,        "Knight", 0,5,0,0,  false,num);
            case 2 -> new Artifacts("Training Arrow",     "Trainee",1,1,0,0,  false,num);
            case 3 -> new Artifacts("Novice Scroll",      "Trainee",0,1,0,1,  false,num);
            case 4 -> new Artifacts("Wooden Shield",      "Trainee",0,1,1,0,  false,num);
            case 5 -> new Artifacts("Sharpening Stone",   "None",   0,1,0,0,  false,num);
            case 6 -> new Artifacts("Veteran's Helmet",   "None",   0,0,0,0,  false,num); // must heal not increase health
            case 7 -> new Artifacts("Iron Shield",        "Metal",  -5,0,5,0, false,num);
            case 8 -> new Artifacts("Steal Heart",        "Metal",  2,0,0,0,  false,num);
            case 9 -> new Artifacts("Copper Bracelet",    "Metal",  0,0,1,0,  false,num);
            case 10 -> new Artifacts("Lucky Coin",        "Lucky",  0,1.5,0,0,true, num); // on Attack
            case 11 -> new Artifacts("Lucky Dice",        "Lucky",  0,2,0,0,  true, num); // on Attack
            case 12 -> new Artifacts("Spiked Shield",     "Spike",  -1,0,0,0, false,num); // on Damaged
            case 13 -> new Artifacts("Berserk Heart",     "Berserk",0,2,0,0,  false,num);
            case 14 -> new Artifacts("Skull of Suffering","Berserk",0,0,0,0,  false,num); // on Damaged
            case 15 -> new Artifacts("Fire Scroll",       "Fire",   0,0,0,0,  false,num); // enemy on fire
            case 16 -> new Artifacts("Icicle",            "Ice",    0,2,0,0,  false,num); // freeze enemy
            case 17 -> new Artifacts("Magic Crystal",     "Magic",  0,0,0,2,  false,num);
            case 18 -> new Artifacts("Magic Book",        "Magic",  0,0,0,0,  false,num); // -1 mana use
            case 19 -> new Artifacts("Magic Eye",         "Magic",  0,2,0,0,  false,num); // if mp is 1 can use any attack with +1 dm
            case 20 -> new Artifacts("Silver Sword",      "Silver", 0,0,0,0,  false,num); // ignore 1 armor
            case 21 -> new Artifacts("Silver Necklace",   "Silver", 0,0,0,0,  false,num); // healing +1

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
            case "Test Shield"        -> getArtifact(0);
            case "Test Sword"         -> getArtifact(1);
            case "Training Arrow"     -> getArtifact(2);
            case "Novice Scroll"      -> getArtifact(3);
            case "Wooden Shield"      -> getArtifact(4);
            case "Sharpening Stone"   -> getArtifact(5);
            case "Veteran's Helmet"   -> getArtifact(6);
            case "Iron Shield"        -> getArtifact(7);
            case "Steal Heart"        -> getArtifact(8);
            case "Copper Bracelet"    -> getArtifact(9);
            case "Lucky Coin"         -> getArtifact(10);
            case "Lucky Dice"         -> getArtifact(11);
            case "Spiked Shield"      -> getArtifact(12);
            case "Berserk Heart"      -> getArtifact(13);
            case "Skull of Suffering" -> getArtifact(14);
            case "Fire Scroll"        -> getArtifact(15);
            case "Icicle"             -> getArtifact(16);
            case "Magic Crystal"      -> getArtifact(17);
            case "Magic Book"         -> getArtifact(18);
            case "Magic Eye"          -> getArtifact(19);
            case "Silver Sword"       -> getArtifact(20);
            case "Silver Necklace"    -> getArtifact(21);

            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public String getDescription(){
      return switch(id){
          case 1 -> "if Player have more then 1 df";
          case 2 -> "When Players Class: Archer";
          case 3 -> "When Players Class: Mage";
          case 4 -> "When Players Class: Warrior";
          case 6 -> "Heal After Enemy kill";
          case 10,11 -> "On Attack has 10% chance to activate";
          case 12 -> "Deal damage to Enemy when Attacked";
          case 14 -> "When damaged stack 1 At";
          case 15 -> "Puts Enemies on Fire(WIP)";
          case 16 -> "Freezes Enemies(WIP)";
          case 18 -> "Attacks use less mana -1";
          case 19 -> "If mp is at 1 give +1 and can use any Attack";
          case 20 -> "Ignore 1 enemy armor";
          case 21 -> "Increase healing by 1";

          default -> " ";
      };
    }

    // type activation
    // just adds                                        --> on Start
    // in battle on attack       + chance of activation --> on Attack
    // in battle on getting hurt + chance of activation --> on Damaged
    // on turn                   + chance of activation --> on Turn
    // on kill                   + chance of activation --> on Kill  x
    // at the end of battle      + chance of activation --> at End

    public void activate(){
        switch(id){
            case 0,1,2,3,4,5,7,8,9 :
                active = true;
                gp.player.atBonus = (int) ( gp.player.atBonus + (!flatOrScale ? atBonus :  gp.player.attack    * atBonus!=0? (atBonus-1) : 0));
                gp.player.hpBonus = (int) ( gp.player.hpBonus + (!flatOrScale ? hpBonus :  gp.player.maxHealth * hpBonus!=0? (hpBonus-1) : 0));
                gp.player.dfBonus = (int) ( gp.player.dfBonus + (!flatOrScale ? dfBonus :  gp.player.defence   * dfBonus!=0? (dfBonus-1) : 0));
                gp.player.mpBonus = (int) ( gp.player.mpBonus + (!flatOrScale ? mpBonus :  gp.player.maxMana   * mpBonus!=0? (mpBonus-1) : 0));
                break;
            case 6 :
                //gp.player.health += 2;// helmet
                gp.player.health = Math.min(gp.player.health + 2, gp.player.maxHealth);
                gp.player.curHealth =  String.valueOf(gp.player.health);
                break;
            case 10,11:
                System.out.println("Lucky");
                gp.player.lastDam = "Lucky";
                gp.player.floatingDam = true;
                gp.player.powBonus += (atBonus-1);
                break;
            case 12 :
                gp.getClothestEnemy().getHurt(1); break;
            case 14 :
                gp.player.atBonus++; break;
            case 15 :
                gp.getClothestEnemy().getHurt(2); break;
            case 16 :
                gp.turn++;
                break;
            case 18 :
                gp.player.mana = Math.min(gp.player.mana++,gp.player.maxMana);
                break;
            case 19 :
                active = true;
                gp.player.atBonus++;
                gp.player.mana = gp.player.maxMana;
                break;
            case 20 :
                active = true;
                gp.getClothestEnemy().defence--;
                break;
            case 21 :
                gp.player.health++;
                break;
        }
    }

    public void checkCondition(int num){
         switch (num){
             case 0 : if(checkOnStart() && !active){activate();} break; // 0 for static
             case 1 : if(checkBeforeAt()){activate();}           break; // 1 for before attack
             case 2 : if(checkAfterAt()){activate();}            break; // 2 for after attack
             case 3 : if(checkOnDamaged()){activate();}          break; // 3 for on damaged
             case 4 : if(checkOnTurn()){activate();}             break; // 4 for on turn

             default : break;
        }
    }


    public boolean checkOnTurn(){
        /*return switch (id){
            case 1 -> gp.player.getDf() > 0;

            default -> false;
        };*/
        return false;
    }
    public boolean checkOnDamaged(){
        return switch (id){
            case 12 -> true;

            default -> false;
        };
    }
    public boolean checkBeforeAt(){
        return switch (id){
            case 6 -> gp.getClothestEnemy().health <=0;
            case 10,11 -> getRandom(5); // 1/10 == 10%

            default -> false;
        };
    }

    public boolean checkAfterAt(){
        return switch (id){
            case 6 -> gp.getClothestEnemy().health <=0;

            default -> false;
        };
    }

    public boolean checkOnStart(){
        return switch (id){
            case 0,5,7,8,9 -> true;
            case 1 -> gp.player.getDf() > 0;
            case 2 -> gp.player.name.equals("Archer");
            case 3 -> gp.player.name.equals("Mage");
            case 4 -> gp.player.name.equals("Warrior");

            default -> false; // 6, 10, 11, 12
        };
    }

    public boolean getRandom(int chance){
        return new Random().nextInt(chance) == 1;
    }
}
