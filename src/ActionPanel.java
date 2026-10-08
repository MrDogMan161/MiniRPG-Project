import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ActionPanel {
    int x,y,width,height;
    boolean visible = false;
    String name, str,man,def,hp;
    GamePanel gp;
    Image image, hpImage, strImage, defImage, mpImage, equipmentImage, armorImage, weaponImage, necklaceImage,skipImage;
    bPanel panel;
    Button blockButton,skipButton;

    ActionPanel(int x,int y,GamePanel gp){
        this.x = x;
        this.y = y;
        this.gp = gp;
        loadImages();
        width  = (int)(image.getWidth(null)*gp.pixelMult);
        height = (int)(image.getHeight(null)*gp.pixelMult);

        panel = new bPanel(((x+(width*0.74))/1600), (double) (y+height/2.1)/900,4,-1,gp);
        panel.visible = false;
        panel.height = 250;
        panel.width = 150;
        panel.updatePos(((x+(width*0.74))/1600), (double) (y+height/2.1)/900);
        panel.setRowsColsMargin(4,1,0);

        blockButton = new Button(x+19,y+207,100,50,gp);
        blockButton.display = false;
        blockButton.font = 30;
        blockButton.text = "Block";
        blockButton.displayText = true;
        blockButton.clickable = true;

        skipButton = new Button(x+126,y+207,50,50,gp){
            @Override
            public void action() {
                gp.turn=1;
            }
        };
        skipButton.clickable = true;
        skipButton.imageIcon = skipImage;
        skipButton.displayImage = true;
        skipButton.display = false;

    }

    public void loadImages(){
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/actionBook.png")));
            hpImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/hpIcon.png")));
            strImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/swordIcon.png")));
            defImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/defenceIcon.png")));
            mpImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/manaIcon.png")));
            equipmentImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/equipment.png")));
            armorImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/armor.png")));
            weaponImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/weapon.png")));
            necklaceImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/necklace.png")));
            skipImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/skip.png")));
        }catch (IOException e){

        }
    }

    public void update(){

        panel.update();
        blockButton.update();
        skipButton.update();

    }

    public void draw(Graphics2D g2){
        if (visible){
            g2.drawImage(image,x,y,width,height,null);
            panel.draw(g2);
                if (panel.buttons != null) {
                    for (Button but : panel.buttons) {
                        but.draw(g2);
                    }
                }
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif",Font.BOLD,30));
            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(name + " " + gp.player.level, x + 19, y + 37);

            int imWidth  = (int) (hpImage.getWidth(null) *gp.pixelMult);
            int imHeight = (int) (hpImage.getHeight(null)*gp.pixelMult);

            g2.drawImage(hpImage,x+19,y+50, imWidth, imHeight, null);
            g2.drawString(hp , x + 25+imWidth, y + 50 + ((imWidth - fm.getHeight())/2)+fm.getAscent());

            g2.drawImage(strImage,x+25+(imWidth*2),y + 50, imWidth, imHeight, null);
            g2.drawString(str , x + 37+(imWidth*3), y + 50 + ((imWidth - fm.getHeight())/2)+fm.getAscent());

            g2.drawImage(defImage,x+19,y+62+imHeight, imWidth, imHeight, null);
            g2.drawString(def , x + 25+imWidth, y+62+imHeight + ((imWidth - fm.getHeight())/2)+fm.getAscent());

            g2.drawImage(mpImage,x+25+(imWidth*2),y+62+imHeight, imWidth, imHeight, null);
            g2.drawString(man , x + 36+(imWidth*3), y+62+imHeight + ((imWidth - fm.getHeight())/2)+fm.getAscent());

            g2.drawImage(equipmentImage,x+19,y+144, (int) (equipmentImage.getWidth(null)*gp.pixelMult), (int) (equipmentImage.getHeight(null)*gp.pixelMult),null);

            blockButton.draw(g2);
            skipButton.draw(g2);
            //g2.drawString("Block",x+25,y+213+fm.getHeight());
            //g2.fillRect(x+126,y+207,50,50);
        }
    }

    public void centerPanelAt(int cX,int cY){
        x = cX-width/2;
        y = cY-height/2;
    }

}
