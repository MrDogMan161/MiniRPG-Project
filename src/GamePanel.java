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
    int curRoom = 0;
    double pixelMult = 6.25;

    Integer[] path = new Integer[22]; // 22 events 1x2x3x2x3x2x3x2x3x1
    int curEvent;

    List<IClickable> clickableList = new ArrayList<>();

    bPanel panel = new bPanel(0.5,0.5,3,0,this); // Class Chooser Tower  1.75
    ActionPanel acPanel = new ActionPanel(616,607,this);

    Button startButton = new Button(width/2-400/2,height/2-75/2,400,75,this){
        @Override
        public void action() {
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
        this.setFocusable(true);

        Color cor = Color.WHITE.darker().darker();
        //acPanel.centerPanelAt(800,750);

        acPanel.panel.setRowsColsMargin(4,1,10);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);
        acPanel.panel.addButton("",20,4,Color.WHITE);

        //acPanel.panel.disableButtonsDisplay();
        acPanel.panel.setVisible(true);
        acPanel.panel.visible = false;

        startButton.display = false;
        startButton.displayText("Start Game",Color.WHITE.darker().darker());
        startButton.getPicture(4);
        startButton.clickable = true;
        startButton.displayImage = true;

        panel.setRowsColsMargin(3,1,28);

        panel.addButton("Warrior",20,-1,cor);
        panel.addButton("Mage",20,-1,cor);
        panel.addButton("Archer",20,-1,cor);
        panel.disableButtonsDisplay();

        startButton.font = 20;

        Chooser.gp = this;
        Artifacts.gp = this;
        loadImages();

        generatePath();

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

            if (checkEventStatus(curEvent) && player.visible && Chooser.choserList.isEmpty()) {
                curRoom++;
                clearBonuses();
                player.activateArtifacts(0);
                showPath();
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

        g2.drawImage(background,0,0, (int) (background.getWidth(null)* pixelMult),(int)(background.getHeight(null)* pixelMult),null);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect((this.getWidth() / 2) - 175, 70, 350, 50);
        g2.setColor(Color.GRAY.brighter());
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        g2.drawString("Choose", (this.getParent().getWidth() / 2) - 150, 100);

        panel.draw(g2);
        panel.drawButtons(g2);

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

    public String generateEvent(int id){
        String toReturn = null;
        return switch (id){
            case 0 ->
                switch (curRoom){
                    case 0,1,2 -> Entity.getEnemy(0);
                    case 3,4   -> Entity.getEnemy(1);
                    case 5,6   -> Entity.getEnemy(2);
                    case 7,8   -> Entity.getEnemy(3);
                    default -> "Archer";
                };
            case 1 -> EventStructure.getEvent();
            case 2 -> "Chest";
            case 3 -> "Shop";
            case 4 -> "Warrior";
            case 5 -> Entity.getEnemy(4);
            default -> "Acrcher";

        };
    }

    public void showPath(){
        if(player.name==null || curEvent<0){return;}
        if(curRoom == 0 || curRoom == 9){
            Chooser.addNewChooser(generateEvent(path[curRoom]),path[curRoom]); // 0
        }else if(curRoom%2==0){
            Chooser.addNewChooser(generateEvent(path[curRoom]),path[curRoom]); // 1,3,5,7
            Chooser.addNewChooser(generateEvent(path[curRoom+1]),path[curRoom+1]); // 1,2; 3,4; 5,6, 7,8
        }else {
            Chooser.addNewChooser(generateEvent(path[curRoom+1]),path[curRoom+1]); // 2,4,6,8
            Chooser.addNewChooser(generateEvent(path[curRoom+2]),path[curRoom+2]); // 3,4,5
            Chooser.addNewChooser(generateEvent(path[curRoom+3]),path[curRoom+3]);
        }
        System.out.println(curRoom);
    }

    public boolean checkEventStatus(int id){
        return switch (id){
            case 0,5 -> !enm1.visible && !enm2.visible && !enm3.visible;
            case 1,2,3,4 -> !event.visible; // event, chest , shop , ally
            default -> true;
        };
    }

    public void executeEvent(String name,int id){
        if(player.name==null){return;}
         switch (id){
             case 0,5 : spawnEnemy(name,name.equals("3Skeleton") ? 3 : name.equals("EyeMonsters")? 2 : 1);
             case 1,2,3,4 : event.spawnStructure(name);
        }
    }

    public void spawnEnemy(String name , int count){
        name = name.equals("3Skeleton")? "Skeleton" : name.equals("EyeMonsters")? "EyeMonster" : name;
        for(int i = 0; i <count; i++) {
            if (!enm1.visible || !enm2.visible || !enm3.visible) {
                System.out.println("Spawning enemy " + name);
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
        //player.curHealth =  player.health + "/" + player.maxHealth;
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
                    acPanel.name = but.text;
                    acPanel.hp = player.maxHealth+"";
                    acPanel.str = player.attack+"";
                    acPanel.def = player.defence+"";
                    acPanel.man = player.maxMana+"";
                    acPanel.visible = true;
                    executeEvent(generateEvent(path[0]),0);
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
            //acPanel.panel.buttons[num].displaySubText("d:"+at.power);
            //acPanel.panel.buttons[num].text3 = "m:" + at.manaUse;
        } else {
            acPanel.panel.buttons[num].atk = -1;
            acPanel.panel.buttons[num].displayText = false;
            acPanel.panel.buttons[num].subText = false;
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
        event = new EventStructure(this);
        acPanel.visible = false;
        curEvent =-1;
        curRoom=0;
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
    // end of Actions for Buttons
    // Utilities
    public void generatePath(){
        for (int i = 0; i < 10; i++) {
            switch (i){
                case 0 :
                    path[0] = 0;
                    break;
                case 1,5 :
                    int fNum =(int) (i*2.5-1.5),sNum = (int) (i*2.5-0.5) ;
                    path[fNum] = new Random().nextInt(2);
                    path[sNum] = path[fNum]!=0 ? 0 : new Random().nextInt(3);
                    break;
                case 2,4,6,8 :
                    fNum =(int) (i*2.5-2); sNum = (int) (i*2.5-1) ; int tNum =(int) (i*2.5) ;
                    path[fNum] = new Random().nextInt(2);
                    path[sNum] = path[fNum]!=0 ? 0 : new Random().nextInt(3) ;
                    path[tNum] = path[fNum]!=0 ^ path[sNum]!=0 ? 0 : new Random().nextInt(3) ;
                    break;
                case 3,7 :
                    fNum =(int) (i*2.5-1.5); sNum = (int) (i*2.5-0.5) ;
                    path[fNum] = new Random().nextInt(4);
                    path[sNum] = path[fNum]==3 ? new Random().nextInt(3) : 3 ;
                    break;
                case 9 :
                    path[21] = 5;
                    break;

            }
        }
        for (int i = 0; i < path.length; i++) {
            System.out.println(i+" "+path[i]);
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
}
