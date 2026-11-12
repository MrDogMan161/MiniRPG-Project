package Game.GUI;

import Game.DrawableObject;
import Game.GamePanel;
import Game.IClickable;

import java.awt.*;


public class Button extends DrawableObject implements IClickable {

    public int xText,yText ,font,atk = -1;
    public boolean active = false , clickable = false, displayText = false,subText = false,display = false,displayImage = false;
    boolean overlay = false;
    public String text, text2,text3;
    Color textColor = Color.BLACK;


    public Button(GamePanel gp){
        this.gp = gp;
        gp.clickableList.add(this);
    }

    public Button(int x, int y, int width, int height, GamePanel gp){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gp = gp;
        gp.clickableList.add(this);
    }

    public void draw(Graphics2D g2,float alpha){
        if(visible) {
            if(display) {
                g2.fillRect(x, y, width, height);
            }
            if (displayImage) {
                g2.drawImage(image, x, y, width, height, null);
            }
            g2.setFont(new Font("SansSerif",Font.BOLD,font));
            FontMetrics fm = g2.getFontMetrics();
            if (text != null && displayText){

                g2.setColor(textColor);

                if(subText && text3!=null){

                    xText = fm.stringWidth(text3);
                    yText = (fm.getHeight() + fm.getAscent())/2;

                    g2.drawString(text3, x + width - xText, y + yText);

                    xText = fm.stringWidth(text2);
                    yText = fm.getHeight() + fm.getAscent();

                    g2.drawString(text2, x + width - xText, y + yText);

                    yText = ((height - fm.getHeight()) / 2) + fm.getAscent();

                    g2.drawString(text, x, y + yText);
                }else if(subText) {
                    xText = (width - fm.stringWidth(text)) / 2;
                    yText = ((height - fm.getHeight()*2)/2) + fm.getAscent();
                    g2.drawString(text, x + xText, y + yText);

                    xText = (width - fm.stringWidth(text2)) / 2;
                    yText = ((height - fm.getHeight()/2) / 2) + fm.getAscent();
                    g2.drawString(text2, x + xText, y + yText);
                }else{
                    xText = (width - fm.stringWidth(text)) / 2;
                    yText = ((height - fm.getHeight()) / 2) + fm.getAscent();

                    g2.drawString(text, x + xText, y + yText);
                }
            }

        }
    }

    public void displaySubText(String text){
        this.text2 = text;
        this.subText = true;
    }

    public void displayText(String text , Color color){
        this.text = text;
        this.textColor = color;
        displayText = text!=null;
    }

    public void setSize(int x, int y){
        this.width = x;
        this.height = y;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void action(){
        active = !active;
        //System.out.println("button");
    }

    public boolean checkCollision(int mouseX, int mouseY){
        // if less then max X and larger then min X
        boolean condX = mouseX >= this.getX() && mouseX <= this.getX()+this.getWidth();
        // if less then max Y and larger then min Y
        boolean condY = mouseY >= this.getY() && mouseY <= this.getY()+this.getHeight();
        return  condX && condY && clickable;
    }

    public void setBVisible(boolean visible){
        this.setVisible(visible);
        if(haveImage()) {displayImage = true;}
    }
    public void setFont(int f){this.font = f;};
    public void setClickable(){this.clickable = !this.clickable;}
    public boolean getClickable(){return clickable;}
    public boolean getBool(){return active;}
}
