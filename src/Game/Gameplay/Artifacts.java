package Game.Gameplay;

import Game.DrawableObject;
import Game.GamePanel;

import java.util.Random;

public class Artifacts extends DrawableObject {
    public String name,condDesc;
    public boolean active = false,flatOrScale = false; // false = flat ; true =  % ;
    double hpBonus,atBonus,dfBonus,mpBonus;
    int id;
    public static GamePanel gp;

    Artifacts(){}
    Artifacts(String name,double health,double attack,double defence,double mana, boolean fOs ,int id){
        this.name = name;
        this.hpBonus = health;
        this.atBonus = attack;
        this.dfBonus = defence;
        this.mpBonus = mana;
        this.flatOrScale = fOs;
        this.id = id;
        condDesc = getDescription();
        image = loadImage(name);
        image = image==null? loadImage("Skeleton") : image;
        width = 100;
        height = 100;
        visible = true;
    }


    public static Artifacts getArtifact(int num){
        return switch(num){
            case 0 -> new Artifacts("Test Shield",        0,0,1,0,  false,num);
            case 1 -> new Artifacts("Test Sword" ,        0,5,0,0,  false,num);
            case 2 -> new Artifacts("Training Arrow",     1,1,0,0,  false,num);
            case 3 -> new Artifacts("Novice Scroll",      0,1,0,1,  false,num);
            case 4 -> new Artifacts("Wooden Shield",      0,1,1,0,  false,num);
            case 5 -> new Artifacts("Sharpening Stone",   0,1,0,0,  false,num);
            case 6 -> new Artifacts("Veteran Helmet",   0,0,0,0,  false,num); // must heal not increase health
            case 7 -> new Artifacts("Iron Shield",       -5,0,1,0, false,num);
            case 8 -> new Artifacts("Steel Heart",        2,0,0,0,  false,num);
            case 9 -> new Artifacts("Copper Bracelet",    0,0,1,0,  false,num);
            case 10 -> new Artifacts("Lucky Coin",        0,1.5,0,0,true, num); // on Game.Attack
            case 11 -> new Artifacts("Lucky Dice",        0,2,0,0,  true, num); // on Game.Attack
            case 12 -> new Artifacts("Thorn",            -1,0,0,0, false,num); // on Damaged
            case 13 -> new Artifacts("Berserk Heart",     0,2,0,0,  false,num);
            case 14 -> new Artifacts("Skull of Suffering",0,0,0,0,  false,num); // on Damaged
            case 15 -> new Artifacts("Fire Scroll",       0,0,0,0,  false,num); // enemy on fire
            case 16 -> new Artifacts("Icicle",            0,2,0,0,  false,num); // freeze enemy
            case 17 -> new Artifacts("Magic Crystal",     0,0,0,2,  false,num);
            case 18 -> new Artifacts("Magic Book",        0,0,0,0,  false,num); // -1 mana use
            case 19 -> new Artifacts("Magic Eye",         0,2,0,0,  false,num); // if mp is 1 can use any attack with +1 dm
            case 20 -> new Artifacts("Silver Sword",      0,0,0,0,  false,num); // ignore 1 armor
            case 21 -> new Artifacts("Silver Necklace",   0,0,0,0,  false,num); // healing +1

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
            case "Veteran Helmet"     -> getArtifact(6);
            case "Iron Shield"        -> getArtifact(7);
            case "Steel Heart"        -> getArtifact(8);
            case "Copper Bracelet"    -> getArtifact(9);
            case "Lucky Coin"         -> getArtifact(10);
            case "Lucky Dice"         -> getArtifact(11);
            case "Thorn"              -> getArtifact(12);
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
          // o -> 4,9
          case 1 -> "if Player have more then 1 defence gives 5 attack";
          case 2,5 -> "Gives 1 attack";
          case 3 -> "Gives 1 mana";
          case 0,4,9-> "Gives 1 defence";
          // 5 -> 2
          case 6 -> "Heal After killing an Enemy";
          case 7 -> "Gives 1 defence in exchange for 5 health";
          case 8,13 -> "Gives 2 health";
          // 9 -> 4
          case 10 -> "Each Attack has 10% chance to have 150% damage";
          case 11 -> "Each Attack has 10% chance to have 200% damage";
          case 12 -> "Deal 1 damage to Enemy when Attacked";
          // 13 -> 8
          case 14 -> "When damaged gives 1 attack for single encounter";
          case 15 -> "Puts Enemies on Fire";
          case 16 -> "Freezes Enemies";
          case 17 -> "Gives 2 mana";
          case 18 -> "Attacks use less mana -1";
          case 19 -> "If mp is at 1 give +1 and can use any Attack";
          case 20 -> "Ignore 1 enemy armor";
          case 21 -> "Increase healing by 1";

          default -> " ";
      };
    }

    // type activation
    // just adds                                        --> on Start
    // in battle on attack       + chance of activation --> on Game.Attack
    // in battle on getting hurt + chance of activation --> on Damaged
    // on turn                   + chance of activation --> on Turn
    // on kill                   + chance of activation --> on Kill  x
    // at the end of battle      + chance of activation --> at End

    public void activate(){
        switch(id){

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
                gp.getClothestEnemy().getHurt(1, Attack.AttackType.MAGIC); break;
            case 14 :
                gp.player.atBonus++; break;
            case 15 :
                gp.getClothestEnemy().effects.add(Effects.getEffect("Fire",1)); break;
            case 16 :
                gp.getClothestEnemy().effects.add(Effects.getEffect("Freeze",1)); break;
            case 18 :
                gp.player.mana = Math.min(gp.player.mana++,gp.player.maxMana);
                break;
            case 19 :
                active = true;
                gp.player.atBonus++;
                break;
            case 20 :
                active = true;
                gp.getClothestEnemy().defence--;
                break;
            case 21 :
                gp.player.health++;
                break;
            default:
                active = true;
                gp.player.atBonus = (int) ( gp.player.atBonus + (!flatOrScale ? atBonus :  gp.player.attack    * atBonus!=0? (atBonus-1) : 0));
                gp.player.hpBonus = (int) ( gp.player.hpBonus + (!flatOrScale ? hpBonus :  gp.player.maxHealth * hpBonus!=0? (hpBonus-1) : 0));
                gp.player.dfBonus = (int) ( gp.player.dfBonus + (!flatOrScale ? dfBonus :  gp.player.defence   * dfBonus!=0? (dfBonus-1) : 0));
                gp.player.mpBonus = (int) ( gp.player.mpBonus + (!flatOrScale ? mpBonus :  gp.player.maxMana   * mpBonus!=0? (mpBonus-1) : 0));
                break;
        }
    }

    public void checkCondition(int num){
         switch (num){
             case 0 : if(checkOnStart()   && !active){activate();} break; // 0 for static
             case 1 : if(checkBeforeAt()  && !active){activate();} break; // 1 for before attack
             case 2 : if(checkAfterAt()   && !active){activate();} break; // 2 for after attack
             case 3 : if(checkOnDamaged() && !active){activate();} break; // 3 for on damaged
             case 4 : if(checkOnTurn()    && !active){activate();} break; // 4 for on turn

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
            case 10,11 -> getRandom(10); // 1/10 == 10%
            case 15,16 -> getRandom(4);  // 1/2  == 50%
            case 19 -> gp.player.mana==1;

            default -> false;
        };
    }

    public boolean checkAfterAt(){
        return switch (id){
            case 6  -> gp.getClothestEnemy().health <=0;
            case 15,16 -> true;


            default -> false;
        };
    }

    public boolean checkOnStart(){
        return switch (id){
            case 0,2,3,4,5,7,8,9,13,17 -> true;
            case 1 -> gp.player.getDf() > 0;

            default -> false; // 6, 10, 11, 12
        };
    }

    public boolean getRandom(int chance){
        return new Random().nextInt(chance) == 1;
    }
}
