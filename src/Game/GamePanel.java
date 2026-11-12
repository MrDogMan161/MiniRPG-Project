package Game;

import Game.GUI.ActionPanel;
import Game.GUI.Button;
import Game.GUI.ButtonPanel;
import Game.GUI.Chooser;
import Game.Gameplay.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable{

    Image background;
    int FPS = 60;
    Thread gameThread;
    public KeyHandler kH = new KeyHandler();

    public float alpha =0;
    boolean pause = true;
    public double pixelMult = 6;
    public int width = getPixel(256),height = getPixel(144);

    public List<IClickable> clickableList = new ArrayList<>();

    ButtonPanel panel = new ButtonPanel(0,0,getPixel(46),getPixel(59),3,this); // Class Game.Chooser Tower  1.75

    ActionPanel acPanel = new ActionPanel(getPixel(98),getPixel(97),this);
    public Map map = new Map(this);

    DrawableObject scroll2 = new DrawableObject() {
        @Override
        public void draw(Graphics2D g2, float alpha) {
            if(visible){
                float renderX = prevX+alpha*(x-prevX);
                float renderY = prevY+alpha*(y-prevY);

                g2.setColor(new Color(251, 205, 111));
                g2.fillRect(x + getPixel(6), y - (scroll2.y - (gp.startButton.getY()+gp.startButton.getHeight())), getPixel(46), y - (gp.startButton.getY()+gp.startButton.getHeight()));
                g2.setColor(new Color(91, 74, 41));
                g2.fillRect(x + getPixel(5),  y - (scroll2.y - (gp.startButton.getY()+gp.startButton.getHeight())), getPixel(1), y - (gp.startButton.getY()+gp.startButton.getHeight()));
                g2.fillRect(x + getPixel(52), y - (scroll2.y - (gp.startButton.getY()+gp.startButton.getHeight())), getPixel(1), y - (gp.startButton.getY()+gp.startButton.getHeight()));//g2.setColor(Color.BLACK);

                g2.setFont(new Font("SansSerif",Font.BOLD,30));
                FontMetrics fm = g2.getFontMetrics();
                if(velY==0) g2.drawString("Choose Class",x+getPixel(6)+(getPixel(23)-(fm.stringWidth("Choose Class")/2)),y-gp.getPixel(63));
                g2.drawImage(image, (int) renderX, (int) renderY, width, height, null);
            }
        }

        @Override
        public void update() {
            if(visible){
                if(y>=getPixel(107)){velY=0;}
                this.x = (int) (this.x + velX);
                this.y = (int) (this.y + velY);
            }
        }
    };

    Button startButton = new Button(width/2-getPixel(29),height/2-getPixel(5),getPixel(58),getPixel(11),this){
        @Override
        public void action() {
            this.velY = -24;
            scroll2.velY = 24;

            scroll2.setVisible(true);

            this.displayText(null,null);
            startGame();
        }
        @Override
        public void update() {
            if(visible){
                if(y<=getPixel(27) && velY!=0){
                    velY=0;
                    panel.x = startButton.x+getPixel(6);
                    panel.y = this.y+this.height+getPixel(10);
                    panel.updateButPos();
                    panel.setButtonVisible(true);

                    panel.setVisible(true);
                    panel.enableButtons(true);
                }
                this.x = (int) (this.x + velX);
                this.y = (int) (this.y + velY);
            }
        }
    };

    public EventStructure event = new EventStructure(this);

    public Player player = new Player(this);
    public Entity dude = new Entity(this);
    public Entity dude1 = new Entity(this);

    public Entity enm1 = new Entity(this);
    public Entity enm2 = new Entity(this);
    public Entity enm3 = new Entity(this);

    public int turn = -1;
    int timer;

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

        scroll2.image  = scroll2.loadImage("ScrollBut");
        scroll2.gp     = this;
        scroll2.width  = getPixel(58);
        scroll2.height = getPixel(11);
        scroll2.x = startButton.x;
        scroll2.y = startButton.y;

        startButton.displayText("Start Game",Color.WHITE);
        startButton.image = startButton.loadImage("ScrollBut");
        startButton.setClickable();
        startButton.setBVisible(true);

        panel.setRowsColsMargin(3,1,getPixel(1),getPixel(1));
        panel.addButton("Warrior",20,cor);
        panel.addButton("Mage",20,cor);
        panel.addButton("Archer",20,cor);
        panel.setButtonImage("SignUI");
        panel.setButtonsSize(getPixel(44),getPixel(18));
        panel.disableButtonsDisplay();

        startButton.setFont(20);

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
                delta--;
                alpha = (float) delta;
                repaint();

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


            scroll2.savePrevious();
            scroll2.update();
            event.savePrevious();
            event.update();
            map.update();
            acPanel.update();
            //Chooser.updateAll();
            enm3.update();
            enm2.update();
            enm1.update();
            dude.update();
            dude1.update();
            player.update();

            startButton.savePrevious();
            startButton.update();

        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(background,0,0, (int) (background.getWidth(null)* pixelMult),(int)(background.getHeight(null)* pixelMult),null);


        event.draw(g2,alpha);
        enm3.draw(g2);
        enm2.draw(g2);
        enm1.draw(g2);
        dude.draw(g2);
        dude1.draw(g2);
        player.draw(g2);
        player.drawArtifacts(g2);
        acPanel.draw(g2);
        //Chooser.drawAll(g2);
        map.draw(g2);
        if (scroll2!= null && scroll2.visible){
            scroll2.draw(g2,alpha);
        }
        panel.draw(g2);
        panel.drawButtons(g2);

        startButton.draw(g2,alpha);

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
        if(map.currentFloor<8){count = 1;}
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
                Game.GUI.Button but = acPanel.panel.buttons[i];

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
                    acPanel.enableButtons(false);
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
                case -1:
                    updateEffects(); player.activateEffect(); turn++; break;
                case 0 :
                    acPanel.enableButtons(true); playerAttack(); if(turn==1){getClothestEnemy().activateEffect();} break;
                case 1 :
                    if(enm1.skipping){turn++; break;}
                    if (wait(waitTime)) { enm1.attack(player); turn++;} break;
                case 2 :
                    if(enm2.skipping){turn++; break;}
                    if (wait(waitTime)) { enm2.attack(player); turn++;} break;
                case 3 :
                    if(enm3.skipping){turn++; break;}
                    if (wait(waitTime)) { enm3.attack(player); turn++;}break;
                default : turn = -1; break;
            }
        }else {
            //event.doStaff(player);
        }

    }
    // end of Components for Battle

    // Actions for Buttons
    public void chooseClass(Button[] buttons){
        if (buttons != null) {
            for (Button but : buttons) {
                if (but.active) {
                    but.active = false;
                    panel.enableButtons(false);

                    player.setClass(but.text);
                    setAttackButtons(0);
                    player.visible = true;
                    panel.setVisible(false);
                    startButton.visible = false;
                    scroll2.visible = false;

                    acPanel.setParameters(but.text,player.getAt(),player.getHp(),player.getMp(),player.getDf());
                    acPanel.setVisible(true);

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
            acPanel.panel.buttons[num].clickable = false;
        }
    }

    public void updateEffects(){

        if(!player.effects.isEmpty()){
            for(Effects effect : player.effects){
                effect.decreaseEffect(player);
            }
        }
        if(!enm1.effects.isEmpty()){
            for(Effects effect : enm1.effects){
                effect.decreaseEffect(enm1);
            }
        }
        if(!enm2.effects.isEmpty()){
            for(Effects effect : enm2.effects){
                effect.decreaseEffect(enm3);
            }
        }
        if(!enm3.effects.isEmpty()){
            for(Effects effect : enm3.effects){
                effect.decreaseEffect(enm3);
            }
        }
    }

    public void restartGame(){
        pause = true;
        Chooser.clear();
        player = new Player(this);
        player.setVisible(false);

        enm1 = new Entity(this);
        enm1.setVisible(false);
        enm2 = new Entity(this);
        enm2.setVisible(false);
        enm3 = new Entity(this);
        enm3.setVisible(false);
        event = new EventStructure(this);
        acPanel.setVisible(false);
        map.resetMap();
        turn = 0;
        scroll2.visible = false;

        startButton.setPosition(width/2-getPixel(29),height/2-getPixel(5));
        startButton.displayText("Restart Game",Color.WHITE);
        startButton.setClickable();
        startButton.setVisible(true);

        scroll2.x = startButton.x;
        scroll2.y = startButton.y;
    }

    public void startGame(){
        player.ChangePos(0);

        pause = false;
        startButton.setClickable();

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
