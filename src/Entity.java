import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Entity {
    int maxHealth,health, attack, defence, mana,maxMana, size = 100, offset = 30;
    int maxX = 50;
    int x,y,pos,lastDam = 0;
    String name ,curAnim;
    Image sprite;
    boolean side,playAnim,visible = false,floatingDam;
    GamePanel gp;
    Waiter waiter = new Waiter();
    Integer[] attacks = new Integer[4];


    Entity(GamePanel gp){
        this.gp = gp;
    }

    public void LoadImage(){
        try{
           switch (name){
               case "Warrior"  : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/warrior.png"))); break;
               case "Mage"     : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/mage.png")));    break;
               case "Archer"   : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/archer.png")));  break;
               case "Zombie"   : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/zombie.png")));  break;
               case "Necromancer": sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/necromancer.png")));  break;
               case "Skeleton" : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/skeleton.png")));break;
           }
           //System.out.println(sprite);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStats(){
        switch (name){
            case "Warrior"     : attack = 3 ; maxHealth = 15; defence = 0; maxMana = 5 ; break;
            case "Mage"        : attack = 2 ; maxHealth = 8 ; defence = 0; maxMana = 20; break;
            case "Archer"      : attack = 2 ; maxHealth = 10; defence = 0; maxMana = 5 ; break;
            case "Zombie"      : attack = 1 ; maxHealth = 10; defence = 0; maxMana = 2 ; break;
            case "Necromancer" : attack = 3 ; maxHealth = 15; defence = 0; maxMana = 10; break;
            case "Skeleton"    : attack = 1 ; maxHealth = 8 ; defence = 0; maxMana = 2 ; break;

        }
    }

    public void setAttacks(String name){
        switch (name){
            case "Warrior"     : attacks[0] = -1  ; break;
            case "Mage"        : attacks[0] = 5  ; break;
            case "Archer"      : attacks[0] = 8  ; break;
            case "Zombie"      : attacks[0] = 2  ; break;
            case "Necromancer" : attacks[0] = 7  ; break;
            case "Skeleton"    : attacks[0] = 2  ; break;
        }
    }

    public void addAttack(int num){
        for (int i=0; i < attacks.length;i++){
            if (attacks[i]==null){
                System.out.println("added "+num);
                attacks[i]= num;

                break;
            }
        }
    }

    public void removeAttack(int num){
        attacks[num] = null;
    }

    public void attack(Entity target){
        // new Random().nextInt

        if(attacks[0]!=null) {
            Attack at = Attack.getAttacks(attacks[0]);
            playAnimation("Attack");
            at.doAttack(this, target);
            System.out.println(at.name);
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
        LoadImage();
        setStats();
        side = true;
        this.health = maxHealth;
        this.mana = maxMana;
        this.setAttacks(name);

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
        this.pos = pos;
        switch (pos){
            case 0 :
                x = (int) (gp.getWidth()/6.66); // 39
                y = (int) (gp.getHeight()/2.15); // 67
                side = true;
                break;
            case 1:
                x = (int) (gp.getWidth()/11.68); // 22 137
                y = (int) (gp.getHeight()/2.77); // 52 325
                side = true;
                break;
            case 2:
                x = (int) (gp.getWidth()/11.68); // 22 137
                y = (int) (gp.getHeight()/1.76); // 82 512
                side = true;
                break;
            case 3:
                x = (int) (gp.getWidth()/1.26); // 203 1268
                y = (int) (gp.getHeight()/2.15); // 67 512
                side = false;
                break;
            case 4:
                x = (int) (gp.getWidth()/1.16); // 220 1375
                y = (int) (gp.getHeight()/2.77); // 52 512
                side = false;
                break;
            case 5:
                x = (int) (gp.getWidth()/1.16); // 220 1268
                y = (int) (gp.getHeight()/1.76); // 82 512
                side = false;
                break;


        }
    }

    public void update(){
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
        if(sprite!=null) {
            size = (int) (sprite.getWidth(null) * gp.globMult);
        }
        if(this.CheckDead()){this.visible = false;}

    }

    public void draw(Graphics2D g2){
        if (visible) {
            if (sprite != null) {
                g2.setColor(Color.BLACK);
                g2.fillRect(x, y - offset, size, offset / 2);
                int gap = 3;
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(x + gap, gap + y - offset, size - (gap * 2), (offset / 2) - gap*2);
                g2.setColor(Color.RED);
                g2.fillRect(x + gap, gap + y - offset, (int)((double)health/maxHealth*(size - (gap * 2))), (offset / 2) - gap*2);

                if(floatingDam){
                    g2.setFont(new Font("SansSerif",Font.BOLD,20));
                    FontMetrics fm = g2.getFontMetrics();
                    String text = "-"+lastDam;
                    int xText = (size - fm.stringWidth(text)) / 2;
                    int yText =  y - 40;
                    g2.drawString(text,x+xText,yText);
                    if(waiter.wait(2)){
                        floatingDam = false;
                        lastDam = 0;
                    }
                }

                g2.drawImage(sprite, side ? x : x + size, y, side ? size : -size, size, null);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif",Font.BOLD,10));
                g2.drawString(health + "/" + maxHealth, x+offset, y-20);
            } else {
                System.out.println("Nuh uh");
            }
        }
    }
}
