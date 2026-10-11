import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Entity {
    int attack, maxHealth, defence, maxMana;
    int health, mana;
    int size = 100;
    int hpBonus, mpBonus,atBonus,dfBonus;
    int level = 1, expDrop = 0, expCount = 0, expMax= 2;
    int maxX = 50;
    int x,y,pos,barWidth,barHeight;
    double powBonus = 1;
    String name ,curAnim , curHealth,lastDam;
    Image sprite,healthBar;
    boolean side,playAnim,visible = false,floatingDam;
    GamePanel gp;
    Waiter waiter = new Waiter();
    Integer[] attacks = new Integer[4];
    List<Effects> effects = new ArrayList<>();
    Attack.AttackType[] weakness;
    // exp need = lev^2 - previous exp need

    Entity(GamePanel gp){
        this.gp = gp;
        barWidth = (int) (gp.pixelMult*15);
        barHeight= (int) (gp.pixelMult*5);
    }

    public Image LoadImage(String name){
        try{
            healthBar = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/healthBar.png")));
           return switch (name){
               case "Warrior"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/warrior.png")));
               case "Mage"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/mage.png")));
               case "Archer"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/archer.png")));
               case "Zombie"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/zombie.png")));
               case "Skeleton",
                    "3Skeleton"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/skeleton.png")));
               case "Vampire"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/vamp.png")));
               case "EyeMonster",
                    "EyeMonsters"-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/eyeEnemy.png")));
               case "FireSkull"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/fireSkull.png")));
               case "WaterHand"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/waterEnemy.png")));
               case "Knight"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/knight.png")));
               //case "EyeEnemy"  : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/eyeEnemy.png")));break;
               case "Necromancer"-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/necromancer.png")));
               default -> throw new IllegalStateException("Unexpected value: " + name);
           };

           //System.out.println(sprite);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getHp(){return this.maxHealth+hpBonus;}
    public int getMp(){return this.maxMana+mpBonus;}
    public int getAt(){return this.attack+atBonus;}
    public int getDf(){return this.defence+dfBonus;}

    public void update(){
        if(expCount>=expMax){
            expCount-=expMax;
            level++;
            attack++;maxHealth++;maxMana++;
            expMax = (int) (Math.pow(level,2)-Math.pow(level-1,2));
        }
        if(playAnim && visible){
            switch(curAnim){
                case "Attack" :
                    if(side? x<maxX+getPosX(pos): x>getPosX(pos)-maxX ){
                        x += side ? 5 : -5;
                    } else {
                        playAnim = false;
                        x = getPosX(pos);
                        this.ChangePos(pos);
                    }
                    break;
                case "GetDamage" :

                    break;
            }
        }else {
            playAnim = false;
        }
        if(this.CheckDead()){this.visible = false;}

    }

    public void draw(Graphics2D g2){
        if (visible) {
            if (sprite != null) {
                g2.drawImage(healthBar,x,y-barHeight,barWidth,barHeight,null);
                g2.setColor(Color.RED);
                g2.fillRect(x+31, y-19, (int)((double)health/maxHealth*56), 7);

                if(floatingDam){
                    g2.setFont(new Font("SansSerif",Font.BOLD,20));
                    FontMetrics fm = g2.getFontMetrics();

                    int xText = (size - fm.stringWidth(lastDam)) / 2;
                    int yText =  y - 40;
                    g2.drawString(lastDam,x+xText,yText);
                    if(waiter.wait(2)){
                        floatingDam = false;
                        lastDam = null;
                    }
                }

                g2.drawImage(sprite, side ? x : x + size, y, side ? size : -size, size, null);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif",Font.BOLD,15));
                // (width - fm.stringWidth(text)) / 2;
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(curHealth, x+((31-fm.stringWidth(curHealth))/2), y-10);

                g2.setColor(Color.BLUE);
                g2.drawString(String.valueOf(mana),x+size,y-30);
            }
        }
    }

    public void setStats(){
        switch (name){
            case "Warrior"     : attack = 3 ; maxHealth = 10; defence = 0; maxMana = 5   ; break;
            case "Mage"        : attack = 2 ; maxHealth = 8 ; defence = 0; maxMana = 20  ; break;
            case "Archer"      : attack = 2 ; maxHealth = 10; defence = 0; maxMana = 5   ; break;
            case "Zombie"      : attack = 1 ; maxHealth = 5 ; defence = 0; maxMana = 2   ; expDrop = 2 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY}; break;
            case "Skeleton"    : attack = 1 ; maxHealth = 4 ; defence = 0; maxMana = 2   ; expDrop = 1 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;

            case "Vampire"     : attack = 3 ; maxHealth = 8 ; defence = 0; maxMana = 5   ; expDrop = 4 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;
            case "EyeMonster"  : attack = 1 ; maxHealth = 6 ; defence = 0; maxMana = 5   ; expDrop = 5 ; weakness= new Attack.AttackType[]{Attack.AttackType.POISON};  break;

            case "FireSkull"   : attack = 6 ; maxHealth = 12; defence = 0; maxMana = 5   ; expDrop = 6 ; weakness= new Attack.AttackType[]{Attack.AttackType.WATER, Attack.AttackType.HOLY};   break;
            case "WaterHand"   : attack = 5 ; maxHealth = 15 ; defence = 0; maxMana = 2  ; expDrop = 8 ; weakness= new Attack.AttackType[]{Attack.AttackType.GROUND, Attack.AttackType.HOLY}; break;

            case "Knight"      : attack = 10 ; maxHealth = 25 ; defence = 0; maxMana = 2 ; expDrop = 10 ;weakness= new Attack.AttackType[]{Attack.AttackType.DARK, Attack.AttackType.MAGIC};  break;

            case "Necromancer" : attack = 15 ; maxHealth = 50; defence = 1; maxMana = 10 ; expDrop = 15; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;

        }
    }

    public void setAttacks(){
        attacks[0] = 0;
        attacks[1] = 1;
        attacks[2] = 2;
        attacks[3] = 3;
    }

    public void addAttack(int num){
        for (int i=0; i < attacks.length;i++){
            if (attacks[i]==null){
                //System.out.println("added "+num);
                attacks[i]= num;

                break;
            }
        }
    }

    public void removeAttack(int num){
        attacks[num] = null;
    }

    public void activateEffect(){
        if(!effects.isEmpty()){
            for(Effects effect : effects){
                effect.doEffect(this);
            }
        }
    }

    public void getHurt(double damage, Attack.AttackType type){

        if(weakness!=null){for(Attack.AttackType weak : weakness){if(weak==type){ damage*=1.5; break;}}}

        health = (int) (health - Math.max(damage, 0));
        lastDam = damage>0? "-" + (int)damage : "0";
        floatingDam = true;
    }

    // for Enemy AI
    public void attack(Entity target){
        // new Random().nextInt

        if(attacks[0]!=null && !this.CheckDead()) {
            int rn = new Random().nextInt(4);
            Attack at = Attack.getAttacks(this.name,attacks[rn]);
            //System.out.println(this.name + " tried using attack with number: "+rn);
            while(at==null || at.manaUse>mana) {
                //System.out.println(this.name + " AGAIN "+ rn);
                rn = new Random().nextInt(4);
                at = Attack.getAttacks(this.name,attacks[rn]);

            }
            playAnimation("Attack");
            at.doAttack(this, target);

        }
    }

    public void playAnimation(String name){
        this.playAnim = true;
        this.curAnim = name;
    }

    public boolean CheckDead(){
        if(health<=0){
            this.visible=false;
        }
        return health<=0;
    }

    public void setClass(String name){
        this.name = name;
        sprite = LoadImage(name);
        setStats();
        if(sprite!=null) {
            size = (int) (sprite.getWidth(null) * gp.pixelMult);
        }
        side = true;
        this.health = maxHealth;
        this.mana = maxMana;
        curHealth = String.valueOf(health);
        this.setAttacks();

    }

    public int getPosX(int pos){
        return switch (pos){
            case 0 -> (int) (39*gp.pixelMult); // 39
            case 1,2 -> (int) (22*gp.pixelMult); // 22 137  // 22 137
            case 3 -> (int) (203*gp.pixelMult); // 203 1268
            case 4,5 -> (int) (220*gp.pixelMult); // 220 1375 // 220 1268
            default -> 0;
        };
    }

    public int getPosY(int pos){
        return switch (pos){
            case 0,3 ->  (int) (67*gp.pixelMult); // 67
            case 1,4 -> (int) (22*gp.pixelMult); // 22 137  // 22 137
            case 2,5 -> (int) (203*gp.pixelMult); // 203 1268
            default -> 0;
        };
    }

    public void ChangePos(int pos){
        this.pos = pos;
        switch (pos){
            case 0 :
                x = (int) (39*gp.pixelMult); // 39
                y = (int) (67*gp.pixelMult); // 67
                side = true;
                break;
            case 1:
                x = (int) (22*gp.pixelMult); // 22 137
                y = (int) (52*gp.pixelMult); // 52 325
                side = true;
                break;
            case 2:
                x = (int) (22*gp.pixelMult); // 22 137
                y = (int) (82*gp.pixelMult); // 82 512
                side = true;
                break;
            case 3:
                x = (int) (203*gp.pixelMult); // 203 1268
                y = (int) (67*gp.pixelMult); // 67 512
                side = false;
                break;
            case 4:
                x = (int) (220*gp.pixelMult); // 220 1375
                y = (int) (52*gp.pixelMult); // 52 512
                side = false;
                break;
            case 5:
                x = (int) (220*gp.pixelMult); // 220 1268
                y = (int) (82*gp.pixelMult); // 82 512
                side = false;
                break;


        }
    }

    public static String getEnemy(int tier){
        return switch (tier){
            case 0 -> switch (new Random().nextInt(2)){
                case 0 -> "Zombie";
                case 1 -> "Skeleton";
                //case 2 -> "Vampire";
                default -> "Warrior";};
            case 1 -> switch (new Random().nextInt(2)) {
                case 0 -> "EyeMonster";
                case 1 -> "Vampire";
                default -> "Warrior";};
            case 2 -> switch (new Random().nextInt(3)) {
                    case 0 -> "FireSkull";
                    case 1 -> "WaterHand";
                    //case 2 -> "Golem"; // there are no golem yet
                    default -> "Warrior";};
            case 3 -> switch (new Random().nextInt(3)) {
                case 0 -> "Knight";
                //case 1 -> "Demon";    // there are no this guy yet
                //case 2 -> "EvilTree"; // there are no this guy yet
                default -> "Warrior";};
            case 4 -> switch (new Random().nextInt(2)) { // boss
                case 0 -> "Necromancer";
                //case 1 -> "Demon";    // there are no this guy yet
                //case 2 -> "EvilTree"; // there are no this guy yet
                default -> "Necromancer";};
            default -> throw new IllegalStateException("There are nos such tier as: " + tier);
        };

    }

}
