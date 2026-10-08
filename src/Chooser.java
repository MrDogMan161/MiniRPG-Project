import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chooser {
    int x,y,width = 181,height = 262 , displayWidth = 100,displayHeight = 100;
    boolean visible = false , choped = false;
    static List<Chooser> choserList = new CopyOnWriteArrayList<>();
    Image image , displayImage;
    String title,name,description,desc2 = " ",desc3 = " ";
    Button choseBut;
    static GamePanel gp;

    Chooser(String event,int id){
        //System.out.println("Current " + event);

        loadImage(event,id);
        width = (int) (image.getWidth(null)*gp.pixelMult);
        height = (int) (image.getHeight(null)* gp.pixelMult);
        choseBut = new Button(x+18,y+325,237,50,gp){
            @Override
            public void action(){
                gp.curEvent = id;
                gp.executeEvent(event,id);
                clear();
            }
        };
        choseBut.display = false;
        choseBut.clickable = true;
        choseBut.visible = true;
        choseBut.font = 30;
        choseBut.displayText("Choose",Color.BLACK);
        switch (event){
            case "Skill" :
                this.title = "Event";
                this.name = "Skill Stone";
                this.description = "Stone that adds 1 Attack to your character";
                break;
            case "Chest" :
                this.title = "Event";
                this.name = "Mysterious Chest";
                this.description = "Chest that might give you an artifact";
                break;
            case "Heal"   :
                this.title = "Event";
                this.name = "Health Fountain";
                this.description = "Fountain that adds 1 Max Health to your character";
                break;
            case "Shop"   :
                this.title = "Special";
                this.name = "Shop";
                this.description = "Shop where you might by some goods";
                break;
            case "Zombie", "Skeleton","Vampire","FireSkull","WaterHand","Knight","Warrior","Archer","Mage"  :
                this.title = "Fight";
                this.name = event;
                this.description = "Begin fight with one "+ event;
                break;
            case "EyeMonsters" :
                this.title = "Fight";
                this.name = event;
                this.description = "Begin fight with two Eye Monsters";
                break;
            case "Necromancer" :
                this.title = "Fight";
                this.name = event;
                this.description = "Fight with boss";
                break;
            case "3Skeleton" :
                this.title = "Fight";
                this.name = "Skeleton Triplet";
                this.description = "Fight three skeletons at once";
                break;
            default:
                System.out.println(event);
                break;

        }
        visible = true;
    }

    Chooser(Artifacts art){
        //System.out.println("Current " + event);
        loadImage(art.name,-1);
        width = (int) (image.getWidth(null)*gp.pixelMult);
        height = (int) (image.getHeight(null)* gp.pixelMult);
        choseBut = new Button(x+18,y+325,237,50,gp){
            @Override
            public void action(){
                gp.player.ownedArt.add(art);
                art.x += (gp.player.ownedArt.size()-1)*100;
                clear();
            }
        };
        choseBut.display = false;
        choseBut.clickable = true;
        choseBut.visible = true;
        choseBut.font = 30;
        choseBut.displayText("Choose",Color.BLACK);

        this.title = "Artifact";
        this.name = art.name;
        if(!art.flatOrScale){ // flat
            this.description = "Artifacts that adds " +
                    (art.hpBonus!=0? ((int)art.hpBonus) + "Hp" : "") + " " +
                    (art.atBonus!=0? ((int)art.atBonus) + "At" : "") + " " +
                    (art.dfBonus!=0? ((int)art.dfBonus) + "Df" : "") + " " +
                    (art.mpBonus!=0? ((int)art.mpBonus) + "Mp" : "") + " " +
                     art.condDesc;
        }else{ // not flat
            this.description = "Artifacts that adds " +
                    (art.hpBonus!=0? ((art.hpBonus-1)*100) + "%Hp" : "") + " " +
                    (art.atBonus!=0? ((art.atBonus-1)*100) + "%At" : "") + " " +
                    (art.dfBonus!=0? ((art.dfBonus-1)*100) + "%Df" : "") + " " +
                    (art.mpBonus!=0? ((art.mpBonus-1)*100) + "%Mp" : "") + " " +
                    art.condDesc;
        }

        visible = true;
    }

    public void loadImage(String name , int id){
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/tornPaper.png")));
            switch (id){
                case 1,2,3,4  : displayImage = new EventStructure(gp).loadImage(name);  break;
                case 0,5      : displayImage =  new Entity(gp).LoadImage(name);         break;

                default: displayImage = Artifacts.getArtifact(name).image;
            }

        } catch (IOException ignored) {

        }
    }

    public static void updatePos(){
        switch (choserList.size()){
            case 1 :
                Chooser chooser = choserList.getFirst();
                chooser.x = 1600/2 - chooser.width/2;
                chooser.y = (int) (900/2.5  - (double) chooser.height /2);
                chooser.choseBut.setPosition(chooser.x+18,chooser.y+325);
                break;
            case 2 :
                Chooser chooser1 = choserList.getFirst();
                chooser1.x = (int) ((double) 1600 /2 - (chooser1.width*1.5));
                chooser1.y = (int) (900/2.5  - (double) chooser1.height /2);
                chooser1.choseBut.setPosition(chooser1.x+18,chooser1.y+325);

                Chooser chooser2 = choserList.getLast();
                chooser2.x = 1600/2 + chooser2.width/2;
                chooser2.y = (int) (900/2.5  - (double) chooser2.height /2);
                chooser2.choseBut.setPosition(chooser2.x+18,chooser2.y+325);
                break;
            case 3 :
                chooser1 = choserList.getFirst();
                chooser1.x = (int) ((double) 1600 /2 + (chooser1.width*0.6));
                chooser1.y = (int) (900/2.5  - (double) chooser1.height /2);
                chooser1.choseBut.setPosition(chooser1.x+18,chooser1.y+325);

                chooser2 = choserList.getLast();
                chooser2.x = 1600/2 - chooser2.width/2;
                chooser2.y = (int) (900/2.5  - (double) chooser2.height /2);
                chooser2.choseBut.setPosition(chooser2.x+18,chooser2.y+325);

                Chooser chooser3 = choserList.get(1);
                chooser3.x = (int) ((double) 1600 /2 - (chooser3.width*1.6));
                chooser3.y = (int) (900/2.5  - (double) chooser3.height /2);
                chooser3.choseBut.setPosition(chooser3.x+18,chooser3.y+325);
                break;
            default:
                System.out.println(choserList.size());
        }
    }

    public void draw(Graphics2D g2){
        if(visible) {
            g2.drawImage(image, x, y, width, height, null);
            g2.drawImage(displayImage, x + (width / 2 - displayWidth / 2), y + 50, displayWidth, displayHeight, null);
            g2.setFont(new Font("SansSerif", Font.BOLD, 30));

            if(title.equals("Fight")){g2.setColor(Color.RED);}
            if(title.equals("Artifact")){g2.setColor(Color.YELLOW.darker());}
            if(title.equals("Event")){g2.setColor(Color.MAGENTA.darker());}

            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(title, x + (width / 2 - fm.stringWidth(title) / 2), y + fm.getHeight());

            if(title.equals("Artifact")){g2.setColor(Color.YELLOW);}
            if(title.equals("Event")){g2.setColor(Color.BLUE.brighter());}
            g2.drawString(name, x + (width / 2 - fm.stringWidth(name) / 2), y + 200);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            fm = g2.getFontMetrics();
            if(!choped && fm.stringWidth(description)>(width-50)){ chopDescription(fm);}

            if(!desc3.equals(" ")){
                g2.drawString(desc2, x + (width / 2 - fm.stringWidth(desc2) / 2), y + 200 + fm.getHeight());
                g2.drawString(desc3, x + (width / 2 - fm.stringWidth(desc3) / 2), y + 200 + fm.getHeight()*2);
                g2.drawString(description, x + (width / 2 - fm.stringWidth(description) / 2), y + 200 + fm.getHeight()*3);
            }else if(!desc2.equals(" ")) {
                g2.drawString(desc2, x + (width / 2 - fm.stringWidth(desc2) / 2), y + 200 + fm.getHeight());
                g2.drawString(description, x + (width / 2 - fm.stringWidth(description) / 2), y + 200 + fm.getHeight()*2);

            }else {
                g2.drawString(description, x + (width / 2 - fm.stringWidth(description) / 2), y + 200 + fm.getHeight());
            }


            choseBut.draw(g2);
            //g2.fillRect(x+18,y+325,237,50);
        }
    }

    public void chopDescription(FontMetrics fm){
        choped=true;
        if (fm.stringWidth(description) > (width-50)) {
            String[] words = description.split(" ");
            int i = 0;
            while (fm.stringWidth(desc2) + fm.stringWidth(words[i]) < (width-50)) {
                desc2 += words[i] + " ";
                words[i] = "";
                if(i<words.length-1){i++;}else{
                    break;
                }
            }

            description = "";
            for (String word : words) {
                description += word + " ";
            }
            if (fm.stringWidth(description) > (width-50)) {
                words = description.split(" ");
                i = 0;
                while (fm.stringWidth(desc3) + fm.stringWidth(words[i]) < (width-50)) {
                    desc3 += words[i] + " ";
                    words[i] = "";
                    i++;
                }
                description = "";
                for (String word : words) {
                    description += word + " ";
                }
                desc3 = desc3.trim();
            }

            description = description.trim();
            desc2 = desc2.trim();

        }
    }

    public static void drawAll(Graphics2D g2){
        if(!choserList.isEmpty()){
            for(Chooser chooser : choserList){

                chooser.draw(g2);
            }
        }

    }
    public static void clear(){
        for(Chooser ch : Chooser.choserList){
            gp.clickableList.remove(ch.choseBut);
        }
        choserList.clear();
    }

    public static void addNewChooser(String event,int id){
        choserList.add(new Chooser(event,id));
        updatePos();
    }
    public static void addNewChooser(Artifacts artifacts){
        choserList.add(new Chooser(artifacts));
        updatePos();
    }
}
