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
    int width = 1600,height = 900;
    boolean pause = true;
    double globMult = (double)width/256;

    String curEvent;

    List<IClickable> clickableList = new ArrayList<>();

    bPanel panel = new bPanel(0.5,0.5,3,this); // Class Chooser Tower  1.75
    ActionPanel acPanel = new ActionPanel(616,607,this);

    Button startButton = new Button(width/2-400/2,height/2-75/2,400,75,this){
        @Override
        public void action() {
            startGame();
        }
    };

    EventThing event = new EventThing(this);

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
        this.setFocusable(true);

        Color cor = Color.WHITE.darker().darker();
        //acPanel.centerPanelAt(800,750);

        acPanel.panel.setRowsColsMargin(4,1,10);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.displayButtons();
        acPanel.panel.setVisible(true);
        acPanel.panel.visible = false;

        startButton.display = false;
        startButton.displayText("Start Game",Color.WHITE.darker().darker());
        startButton.getPicture(4);
        startButton.clickable = true;
        startButton.displayImage = true;

        panel.setRowsColsMargin(3,1,28);
        panel.loadIcons(0);

        panel.addButton("Warrior",20,-1,cor);
        panel.addButton("Mage",20,-1,cor);
        panel.addButton("Archer",20,-1,cor);
        panel.displayButtons();

        startButton.font = 20;

        Chooser.gp = this;
        Artifacts.gp = this;
        loadImages();


    }

    public void loadImages(){
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("background.png")));

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

            if(Chooser.choserList.isEmpty() && !event.visible && !enm1.visible && !enm2.visible && !enm3.visible){
                curEvent = null;
            }
            if (curEvent == null && player.visible && Chooser.choserList.isEmpty()) {
                clearBonuses();

                player.activateArtifacts(0);
                Chooser.addNewChooser(generateEvent());
                Chooser.addNewChooser(generateEvent());

            }

            for (int i=0;i<4;i++){
                setAttackButtons(i);
            }

            if(player.name != null){
                acPanel.hp  = player.getHp() + "";
                acPanel.str = player.getAt() + "";
                acPanel.def = player.getDf() + "";
                acPanel.man = player.getMp() + "";
            }

            event.update();

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

        g2.drawImage(background,0,0, (int) (background.getWidth(null)*globMult),(int)(background.getHeight(null)*globMult),null);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect((this.getWidth() / 2) - 175, 70, 350, 50);
        g2.setColor(Color.GRAY.brighter());
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        g2.drawString("Choose", (this.getParent().getWidth() / 2) - 150, 100);

        panel.draw(g2);

        drawEach(g2,panel.buttons);

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
        startButton.draw(g2);

        g2.dispose();
    }

    public String generateEvent(){
        int rng = new Random().nextInt(10);
        return switch (rng){
            case 0 -> "Skeleton3";
            case 4 -> "Necromancer";
            case 2,3 -> "Zombie";
            case 7 -> "Skill";
            case 8,1 -> "Chest";
            case 9 -> "Heal";
            default -> "Skeleton";
        };
    }

    public void executeEvent(String eventName){
        if(player.name==null || eventName==null){return;}
        System.out.println("Executing event " + eventName );

        switch(eventName){
            case "Skeleton3" :
                this.spawnEnemy("Skeleton");
                this.spawnEnemy("Skeleton");
                this.spawnEnemy("Skeleton");
                break;
            case "Heal", "Chest","Skill"  :
                this.event.spawnThing(eventName);
                break;
            default:  this.spawnEnemy(eventName);
        }
    }

    public void spawnEnemy(String name){
        if( !enm1.visible || !enm2.visible || !enm3.visible) {
            System.out.println("Spawning enemy "+name);
            Entity enem = new Entity(this);
            enem.setClass(name);

            enem.ChangePos(!enm1.visible ? 3 : !enm2.visible ? 4 : 5);
            enem.visible = true;
            if (!enm1.visible) {
                enm1 = enem;
            } else if (!enm2.visible) {
                enm2 = enem;
            } else if (!enm3.visible) {
                enm3 = enem;
            }
        }else {
            System.out.println("There are no free space");
        }
    }

    public Entity getClothestEnemy(){
        return enm1.visible ? enm1 : enm2.visible ? enm2 : enm3.visible ? enm3 : null;
    }

    public void setAttackButtons(int num){
        
        Attack at = null;
        if(player.attacks[num]!=null){
            at = Attack.getAttacks(player.name,player.attacks[num]);
        }
       
        if(at!=null){
            acPanel.panel.buttons[num].atk = player.attacks[num];
            acPanel.panel.buttons[num].displayText(at.name, Color.WHITE.darker().darker());
            //acPanel.panel.buttons[num].displaySubText("d:"+at.power);
            //acPanel.panel.buttons[num].text3 = "m:" + at.manaUse;
        } else {
            acPanel.panel.buttons[num].atk = -1;
            acPanel.panel.buttons[num].displayText = false;
            acPanel.panel.buttons[num].subText = false;
        }
    }

    public void chooseClass(Button[] buttons){
        if (buttons != null) {
            for (Button but : buttons) {
                if (but.active) {
                    but.active = false;
                    player.setClass(but.text);
                    setAttackButtons(0);
                    player.visible = true;
                    panel.setVisible(false);
                    acPanel.name = but.text;
                    acPanel.hp = player.maxHealth+"";
                    acPanel.str = player.attack+"";
                    acPanel.def = player.defence+"";
                    acPanel.man = player.maxMana+"";
                    acPanel.visible = true;
                    break;
                }
            }
        }
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
    public void clearBonuses(){
        player.atBonus = 0;
        player.hpBonus = 0;
        player.mpBonus = 0;
        player.dfBonus = 0;
        for(Artifacts art : player.ownedArt){art.active = false;}

        player.health = player.maxHealth;
        player.curHealth =  player.health + "/" + player.maxHealth;
    }

    public void takeTurns(int waitTime){
        if(!event.visible){
            switch(turn){
                case 0 :  playerAttack(); break;
                case 1 :  if (wait(waitTime)) { enm1.attack(player); turn++;} break;
                case 2 :  if (wait(waitTime)) { enm2.attack(player); turn++;} break;
                case 3 :  if (wait(waitTime)) { enm3.attack(player); turn++;} break;
                default : turn = 0; break;
            }
        }else {
            event.doStaff(player);
        }

    }
    public void drawEach(Graphics2D g2,Button[] buttons){
        if (buttons != null) {
            for (Button but : buttons) {
                but.draw(g2);
            }
        }
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
        event = new EventThing(this);
        acPanel.visible = false;
        curEvent = null;
        turn = 0;

        startButton.clickable = true;
        startButton.visible = true;
    }

    public void startGame(){
        player.ChangePos(0);

        panel.setVisible(true);
        pause = false;
        startButton.clickable = false;
        startButton.visible = false;
    }

}
