package Game;

import java.awt.*;

public class ActionPanel extends DrawableObject {
    String name, str,man,def,hp;
    Image hpImage, strImage, defImage, mpImage, equipmentImage, armorImage, weaponImage, necklaceImage,skipImage;
    ButtonPanel panel;
    Button blockButton,skipButton;
    boolean overlay = false;
    String ovrButText,pow,mpUse,effect,type;

    ActionPanel(int x, int y, GamePanel gp){
        this.x = x;
        this.y = y;
        this.gp = gp;

        loadImages();
        setSize();

        panel = new ButtonPanel(this.x+gp.getPixel(32), this.y+gp.getPixel(2),250,150,4,gp);
        panel.visible = true;
        panel.setRowsColsMargin(4,1,0);

        blockButton = new Button(x+19,y+207,100,50,gp);
        blockButton.display = false;
        blockButton.visible = true;
        blockButton.font = 30;
        blockButton.text = "Block";
        blockButton.displayText = true;
        blockButton.clickable = true;

        skipButton = new Button(x+126,y+207,50,50,gp){
            @Override
            public void action() {
                if(gp.turn==0){
                    gp.turn=1;
                    gp.updateEffects();
                }

                // no need for isCLicked cuz active is not present
            }
        };
        skipButton.clickable = true;
        skipButton.image = skipImage;
        skipButton.visible = true;
        skipButton.displayImage = true;
        skipButton.display = false;

    }

    public void loadImages(){
        image          = loadImage("ActionBook");
        hpImage        = loadImage("HealthIcon");
        strImage       = loadImage("SwordIcon");
        defImage       = loadImage("DefenceIcon");
        mpImage        = loadImage("ManaIcon");
        equipmentImage = loadImage("EquipmentUI");
        armorImage     = loadImage("ArmorIcon");
        weaponImage    = loadImage("WeaponIcon");
        necklaceImage  = loadImage("NecklaceIcon");
        skipImage      = loadImage("ButtonSkip");
    }

    public void update(){
        if(gp.player.name != null){
            this.hp  = gp.player.getHp() + "";
            this.str = gp.player.getAt() + "";
            this.def = gp.player.getDf() + "";
            this.man = gp.player.getMp() + "";
        }else{
            this.hp  = "0";
            this.str = "0";
            this.def = "0";
            this.man = "0";
        }

        panel.update();
        blockButton.update();
        skipButton.update();

        for (Button but : panel.buttons) {
            if(but.visible && but.atk > -1 && but.checkCollision(gp.kH.curX,gp.kH.curY)){
                overlay = true;
                if(ovrButText==null || !ovrButText.equals(but.text)) {
                    Attack at = Attack.getAttacks(gp.player.name, but.atk);
                    ovrButText = at.name;
                    pow = "Power: " +at.power;
                    mpUse = "Mp Usage: " +at.manaUse;
                    type = at.curType.toString()+(at.effectPower>1 ? "x"+at.effectPower : "");
                    effect = at.effect;
                }
                break;
            }else {
                overlay = false;
            }
        }

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

            if (overlay){
                int mX = gp.kH.curX+gp.getPixel(2), mY = gp.kH.curY;

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(6));
                g2.drawRect(mX-3, mY-3,gp.getPixel(33),gp.getPixel(33));
                g2.setColor(Color.WHITE);
                g2.fillRect(mX, mY,gp.getPixel(32),gp.getPixel(32));

                g2.setFont(new Font("SansSerif",Font.BOLD,25));
                g2.setColor(Color.BLACK);
                g2.drawString(ovrButText,mX+gp.getPixel(1),mY+fm.getHeight());

                g2.setFont(new Font("SansSerif",Font.BOLD,15));

                g2.fillRect(mX+gp.getPixel(1),mY+fm.getHeight()+gp.getPixel(1),fm.stringWidth(type)/2+gp.getPixel(2),gp.getPixel(4));

                g2.setColor(type.split("x")[0].equals("FIRE")?Color.RED.darker() : type.split("x")[0].equals("WATER") ? Color.BLUE : Color.WHITE);

                g2.drawString(type,mX+gp.getPixel(2),mY+fm.getHeight()+gp.getPixel(4));

                g2.setColor(Color.BLACK);
                g2.drawString(pow,mX+gp.getPixel(2),mY+fm.getHeight()+gp.getPixel(8));
                g2.drawString(mpUse,mX+gp.getPixel(14),mY+fm.getHeight()+gp.getPixel(8));

            }
            //g2.drawString("Block",x+25,y+213+fm.getHeight());
            //g2.fillRect(x+126,y+207,50,50);
        }
    }

}
