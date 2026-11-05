package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{

    Image background;
    int FPS = 60;
    Thread gameThread;
    KeyHandler kH = new KeyHandler();

    boolean pause = true;
    public double pixelMult = 6;
    int width = getPixel(256),height = getPixel(144);

    List<IClickable> clickableList = new ArrayList<>();

    ButtonPanel panel = new ButtonPanel(0,0,getPixel(46),getPixel(59),3,this); // Class Game.Chooser Tower  1.75

    ActionPanel acPanel = new ActionPanel(getPixel(98),getPixel(97),this);
    Map map = new Map(this);

    DrawableObject scroll2 = new DrawableObject() {
        @Override
        public void draw(Graphics2D g2) {
            if(visible){
                g2.setColor(new Color(251, 205, 111));
                g2.fillRect(x + getPixel(6), y - (scroll2.y - (gp.startButton.y+gp.startButton.height)), getPixel(46), scroll2.y - (gp.startButton.y+gp.startButton.height));
                g2.setColor(new Color(91, 74, 41));
                g2.fillRect(x + getPixel(5),  y - (scroll2.y - (gp.startButton.y+gp.startButton.height)), getPixel(1), scroll2.y - (gp.startButton.y+gp.startButton.height));
                g2.fillRect(x + getPixel(52), y - (scroll2.y - (gp.startButton.y+gp.startButton.height)), getPixel(1), scroll2.y - (gp.startButton.y+gp.startButton.height));//g2.setColor(Color.BLACK);

                g2.setFont(new Font("SansSerif",Font.BOLD,30));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("Choose Class",x+getPixel(6)+(getPixel(23)-(fm.stringWidth("Choose Class")/2)),y-gp.getPixel(63));
                g2.drawImage(image, x, y, width, height, null);
            }
        }
    };

    Button startButton = new Button(width/2-getPixel(29),height/2-getPixel(5),getPixel(58),getPixel(11),this){
        @Override
        public void action() {
            this.y    -= gp.getPixel(40);
            scroll2.y += gp.getPixel(40);

            panel.x = startButton.x+getPixel(6);
            panel.y = this.y+this.height+getPixel(10);
            panel.updateButPos();
            scroll2.visible = true;
            this.text = null;
            startGame();
        }
    };

    EventStructure event = new EventStructure(this);

    Player player = new Player(this);
    Entity dude = new Entity(this);
    Entity dude1 = new Entity(this);

    Entity enm1 = new Entity(this);
    Entity enm2 = new Entity(this);
    Entity enm3 = new Entity(this);

    int turn = 0, timer;

    GamePanel(){
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.addMouseListener(kH);
        this.addMouseMotionListener(kH);
        this.setFocusable(true);

        Color cor = Color.WHITE.darker().darker();
        //acPanel.centerPanelAt(800,750);

        acPanel.panel.setRowsColsMargin(4,1,10);
        acPanel.panel.addButton("",20,Color.WHITE);
        acPanel.panel.addButton("",20,Color.WHITE);
        acPanel.panel.addButton("",20,Color.WHITE);
        acPanel.panel.addButton("",20,Color.WHITE);

        acPanel.panel.disableButtonsDisplay();
        acPanel.panel.setVisible(true);
        acPanel.panel.visible = false;

        scroll2.image  = scroll2.loadImage("ScrollBut");
        scroll2.gp     = this;
        scroll2.width  = getPixel(58);
        scroll2.height = getPixel(11);
        scroll2.x = startButton.x;
        scroll2.y = startButton.y;

        startButton.display = false;
        startButton.displayText("Start Game",Color.WHITE);
        startButton.image = startButton.loadImage("ScrollBut");
        startButton.visible = true;
        startButton.clickable = true;
        startButton.displayImage = true;

        panel.setRowsColsMargin(3,1,getPixel(1));
        panel.addButton("Warrior",20,cor);
        panel.addButton("Mage",20,cor);
        panel.addButton("Archer",20,cor);
        panel.setButtonImage("SignUI");
        panel.setButtonsSize(getPixel(44),getPixel(18));
        //panel.disableButtonsDisplay();

        startButton.font = 20;

        Chooser.stGp = this;
        Artifacts.gp = this;
        loadImages();

    }

    public void loadImages(){
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/background2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){

            currentTime = System.nanoTime();
            delta  += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        checkAllClickable();
        chooseClass(panel.buttons);

        if(!pause){
            panel.update();
            // attacking
            takeTurns(1);
            if(player.CheckDead() && player.name != null){
                restartGame();
            }

            map.checkEventCompletion();

            for (int i=0;i<4;i++){
                setAttackButtons(i);
            }


            event.update();
            map.update();
            acPanel.update();
            enm3.update();
            enm2.update();
            enm1.update();
            dude.update();
            dude1.update();
            player.update();

            startButton.update();

        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(background,0,0, (int) (background.getWidth(null)* pixelMult),(int)(background.getHeight(null)* pixelMult),null);


        event.draw(g2);
        enm3.draw(g2);
        enm2.draw(g2);
        enm1.draw(g2);
        dude.draw(g2);
        dude1.draw(g2);
        player.draw(g2);
        player.drawArtifacts(g2);
        acPanel.draw(g2);
        Chooser.drawAll(g2);
        map.draw(g2);
        if (scroll2!= null && scroll2.visible){
            scroll2.draw(g2);
        }
        panel.draw(g2);
        panel.drawButtons(g2);

        startButton.draw(g2);

        g2.dispose();
    }

    public void executeEvent(int id){
        if(player.name==null){return;}
        switch (id){
            case 1,5 : spawnEnemy("Skeleton",1); break;
            case 2,3,4 : event.spawnStructure(id); break;
            default:
                System.out.println("There are no " +id);
        }
    }

    public void spawnEnemy(String name , int count){
        if(map.currentFloor<8){count = 3;}
        else if (map.currentFloor<13 && !name.equals("Vampire") && !name.equals("Necromancer")) {count = 2;}
        else if (map.currentFloor>12 && !name.equals("Vampire") && !name.equals("Necromancer") && count!=5) {count = 3;}

        System.out.println("Spawning enemy " + name);
        for(int i = 0; i <count; i++) {
            if (!enm1.visible || !enm2.visible || !enm3.visible) {

                Entity enem = new Entity(this);
                enem.setClass(name);
                enem.ChangePos(!enm1.visible ? 3 : !enm2.visible ? 4 : 5);
                enem.mirrored = true;
                enem.visible = true;
                if (!enm1.visible) {
                    enm1 = enem;
                } else if (!enm2.visible) {
                    enm2 = enem;
                } else if (!enm3.visible) {
                    enm3 = enem;
                }
            } else {
                System.out.println("There are no free space");
                break;
            }
        }
    }

    // Components for Battle

    public Entity getClothestEnemy(){
        return enm1.visible ? enm1 : enm2.visible ? enm2 : enm3.visible ? enm3 : null;
    }

    public void playerAttack(){
        if (acPanel.panel.buttons[0] != null) {
            for (int i = 0; i < acPanel.panel.buttons.length; i++) {
                Button but = acPanel.panel.buttons[i];
                if (but.active && but.atk>-1) {
                    but.active = false;

                    Entity target = getClothestEnemy();
                    Attack at = Attack.getAttacks(player.name,but.atk);
                    if (target != null) {
                        at.doAttack(player, target);
                        player.activateArtifacts(2);
                        if (target.CheckDead() && !enm2.visible && !enm3.visible) {
                            player.expCount++;
                        } else {
                            turn++;
                        }
                    }

                    break;
                }
            }
        }
    }

    public void clearBonuses(){
        player.atBonus = 0;
        player.hpBonus = 0;
        player.mpBonus = 0;
        player.dfBonus = 0;
        for(Artifacts art : player.ownedArt){art.active = false;}

        //player.health = player.maxHealth;
        //player.curHealth = String.valueOf(player.health);
    }

    public void takeTurns(int waitTime){
        if(!event.visible){

            switch(turn){
                case 0 :  playerAttack();if(turn!=0) {updateEffects();}       break;
                case 1 :  if (wait(waitTime)) { enm1.attack(player); turn++;} break;
                case 2 :  if (wait(waitTime)) { enm2.attack(player); turn++;} break;
                case 3 :  if (wait(waitTime)) { enm3.attack(player); turn++;} break;
                default : turn = 0; break;
            }
        }else {
            event.doStaff(player);
        }

    }
    // end of Components for Battle

    // Actions for Buttons
    public void chooseClass(Button[] buttons){
        if (buttons != null) {
            for (Button but : buttons) {
                if (but.active) {
                    but.active = false;
                    player.setClass(but.text);
                    setAttackButtons(0);
                    player.visible = true;
                    panel.setVisible(false);
                    startButton.visible = false;
                    scroll2.visible = false;

                    acPanel.name = but.text;
                    acPanel.hp = player.maxHealth+"";
                    acPanel.str = player.attack+"";
                    acPanel.def = player.defence+"";
                    acPanel.man = player.maxMana+"";
                    acPanel.visible = true;
                    map.generatePath();
                    map.showMap();
                    break;
                }
            }
        }
    }

    public void setAttackButtons(int num){

        Attack at = null;
        if(player.attacks[num]!=null){
            at = Attack.getAttacks(player.name,player.attacks[num]);
        }

        if(at!=null){
            acPanel.panel.buttons[num].atk = player.attacks[num];
            acPanel.panel.buttons[num].displayText(at.name, Color.WHITE.darker().darker());
        } else {
            acPanel.panel.buttons[num].atk = -1;
            acPanel.panel.buttons[num].displayText = false;
            acPanel.panel.buttons[num].subText = false;
        }
    }

    public void updateEffects(){
        player.activateEffect();
        enm1.activateEffect();
        enm2.activateEffect();
        enm3.activateEffect();
    }

    public void restartGame(){
        pause = true;
        Chooser.choserList.clear();
        player = new Player(this);
        player.visible = false;
        enm1 = new Entity(this);
        enm1.visible = false;
        enm2 = new Entity(this);
        enm2.visible = false;
        enm3 = new Entity(this);
        enm3.visible = false;
        event = new EventStructure(this);
        acPanel.visible = false;
        map.currentRoom = -1;
        map.currentFloor= -1;
        map.path = new int[15][7][4];
        turn = 0;

        startButton.clickable = true;
        startButton.visible = true;
    }

    public void startGame(){
        player.ChangePos(0);

        panel.setVisible(true);
        pause = false;
        startButton.clickable = false;

        //startButton.visible = false;

    }
    // end of Actions for Buttons
    // Utilities
    public boolean wait(int secs){
        int frames = FPS *secs;
        boolean toReturn = false;
        if(timer == frames){
            timer = 0;
            toReturn = true;
        }else{
            timer++;
        }
        return toReturn;
    }

    public void checkAllClickable(){
        for (IClickable thing : clickableList) {
            if (kH.lMousePressed && thing.checkCollision(kH.lastPressX, kH.lastPressY)) {
                //System.out.println("Clicked");
                kH.lastPressX = 0;
                kH.lastPressY = 0;
                kH.lMousePressed = false;
                thing.action();
                break;
            }
        }
    }

    public int getPixel(int pixels){return (int) (pixels*pixelMult);}
}
