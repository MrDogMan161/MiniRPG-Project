package Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Entity extends DrawableObject {
    int attack, maxHealth, defence, maxMana;
    int health, mana;
    int size = 100;
    int hpBonus, mpBonus,atBonus,dfBonus;
    int level = 1, expDrop = 0, expCount = 0, expMax= 2;
    int maxX = 50,timer;
    double deg;
    int pos,barWidth,barHeight,shadowWidth,shadowHeight;
    double powBonus = 1;
    String name ,curAnim , curHealth,lastDam;
    Image healthBar,shadow,effectIm;
    boolean mirrored,playAnim,floatingDam;
    Waiter waiter = new Waiter();
    Integer[] attacks = new Integer[4];
    List<Effects> effects = new CopyOnWriteArrayList<>();
    Attack.AttackType[] weakness;
    AffineTransform at = new AffineTransform();
    // exp need = lev^2 - previous exp need

    Entity(GamePanel gp){
        this.gp = gp;
        healthBar = loadImage("HealthBar");
        shadow = loadImage("EntityShadow");

        barWidth     = (int) (healthBar.getWidth(null) * gp.pixelMult);
        barHeight    = (int) (healthBar.getHeight(null) * gp.pixelMult);
        shadowWidth  = (int) (shadow.getWidth(null) * gp.pixelMult);
        shadowHeight = (int) (shadow.getHeight(null) * gp.pixelMult);

    }

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
                    if(timer<10){ // mirrored ? x>getPosX(pos)-maxX : x<maxX+getPosX(pos)
                        timer++;
                        deg+=4.5;
                    } else {
                        playAnim = false;
                        timer=0;
                        deg=0;
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
            if (image != null) {
                g2.drawImage(shadow, (int) (x+gp.pixelMult), (int) (y+size-((double) shadowHeight /2)-gp.pixelMult),shadowWidth,shadowHeight,null);

                g2.drawImage(healthBar,x,y-barHeight-gp.getPixel(1),barWidth,barHeight,null);
                g2.setColor(Color.RED);
                g2.fillRect(x+gp.getPixel(5), y-gp.getPixel(5), (int)((double)health/maxHealth*gp.getPixel(9)), gp.getPixel(1));
                g2.setColor(Color.BLUE);
                g2.fillRect(x+gp.getPixel(5), y-gp.getPixel(3), (int)((double)mana/maxMana*gp.getPixel(9)), gp.getPixel(1));

                if(floatingDam){
                    g2.setFont(new Font("SansSerif",Font.BOLD,20));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.setColor(Color.RED);
                    int xText = (size - fm.stringWidth(lastDam)) / 2;
                    int yText =  y - 40;
                    g2.drawString(lastDam,x+xText,yText);
                    if(waiter.wait(2)){
                        floatingDam = false;
                        lastDam = null;
                    }
                }

                at.setToIdentity();
                at.translate(mirrored? x+size:x,y);
                at.scale(mirrored? -gp.pixelMult : gp.pixelMult,gp.pixelMult);
                at.rotate( Math.toRadians(deg),13,16);

                g2.drawImage(image,at,null);
                //g2.drawImage(image, mirrored ? x+size : x, y, mirrored ? -size : size, size, null);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif",Font.BOLD,15));
                // (width - fm.stringWidth(text)) / 2;
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(curHealth, x+((gp.getPixel(5)-fm.stringWidth(curHealth))/2), y-10-gp.getPixel(1));

                if(!effects.isEmpty()){
                    for (int i = 0; i < effects.size(); i++) {

                       g2.drawImage(effects.get(i).image,x + (i*gp.getPixel(9)),y-gp.getPixel(16),gp.getPixel(8),gp.getPixel(8),null);
                       g2.drawString(String.valueOf(effects.get(i).duration),x +gp.getPixel(8)+ (i*gp.getPixel(9)),y-gp.getPixel(8));

                    }

                }
                //g2.drawString(String.valueOf(mana),x+size,y-30);
                if(effectIm!=null){
                    g2.drawImage(effectIm,x,y,size,size,null);
                }

            }
        }
    }

    public void setStats(){
        switch (name){
            case "Warrior"     : attack = 3  ; maxHealth = 10; maxMana = 5   ; break;
            case "Mage"        : attack = 2  ; maxHealth = 8 ; maxMana = 12  ; break;
            case "Archer"      : attack = 2  ; maxHealth = 9 ; maxMana = 8   ; break;

            case "Zombie"      : attack = 1  ; maxHealth = 5 ; maxMana = 2   ; expDrop = 2 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY}; break;
            case "Skeleton"    : attack = 1  ; maxHealth = 40; maxMana = 2   ; expDrop = 1 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;

            case "Vampire"     : attack = 3  ; maxHealth = 8 ; maxMana = 5   ; expDrop = 4 ; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;
            case "EyeMonster"  : attack = 1  ; maxHealth = 6 ; maxMana = 5   ; expDrop = 5 ; weakness= new Attack.AttackType[]{Attack.AttackType.POISON};  break;

            case "FireSkull"   : attack = 6  ; maxHealth = 12; maxMana = 5   ; expDrop = 6 ; weakness= new Attack.AttackType[]{Attack.AttackType.WATER, Attack.AttackType.HOLY};   break;
            case "WaterHand"   : attack = 5  ; maxHealth = 15; maxMana = 2  ; expDrop = 8 ; weakness= new Attack.AttackType[]{Attack.AttackType.GROUND, Attack.AttackType.HOLY}; break;

            case "Knight"      : attack = 10 ; maxHealth = 25; maxMana = 2 ; expDrop = 10 ;weakness= new Attack.AttackType[]{Attack.AttackType.DARK, Attack.AttackType.MAGIC};  break;

            case "Necromancer" : attack = 15 ; maxHealth = 50; maxMana = 10 ; expDrop = 15; weakness= new Attack.AttackType[]{Attack.AttackType.FIRE, Attack.AttackType.HOLY};  break;

        }
    }

    public void loadEffect(String effectName){
        effectIm = switch (effectName){
            case "Fire"  -> loadImage("FireEffect");
            case "Freeze"-> loadImage("FreezeEffect");
            default -> null;
        };
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
        if(health<=0){this.visible=false;}
        return health<=0;
    }

    public void setClass(String name){
        this.name = name;
        image = loadImage(name);

        setStats();
        if(image!=null) {size = (int) (image.getWidth(null) * gp.pixelMult);}

        mirrored = false;
        this.health = maxHealth;
        this.mana = maxMana;
        curHealth = String.valueOf(health);
        this.setAttacks();

    }

    public int getPosX(int pos){
        return switch (pos){
            case 0 ->   (int) ((47*gp.pixelMult) - (double) size /2); // 39
            case 1,2 -> (int) ((39*gp.pixelMult) - (double) size /2); // 22 137  // 22 137
            case 3 ->   (int) ((211*gp.pixelMult)- (double) size /2); // 203 1268
            case 4,5 -> (int) ((228*gp.pixelMult)- (double) size /2); // 220 1375 // 220 1268
            default -> 0;
        };
    }

    public int getPosY(int pos){
        return switch (pos){
            case 0,3 ->  (int) ((83*gp.pixelMult)-size); // 67
            case 1,4 ->  (int) ((70*gp.pixelMult)-size); // 22 137  // 22 137
            case 2,5 ->  (int) ((96*gp.pixelMult)-size); // 203 1268
            default -> 0;
        };
    }

    public void ChangePos(int pos){
        this.pos = pos;
        x = getPosX(pos);
        y = getPosY(pos);
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

    public int getHp(){return this.maxHealth+hpBonus;}
    public int getMp(){return this.maxMana+mpBonus;}
    public int getAt(){return this.attack+atBonus;}
    public int getDf(){return this.defence+dfBonus;}

}
