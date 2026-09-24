import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Button implements IClickable{

    int width, height,x,y,xText,yText ,font,atk;
    boolean active = false , clickable = false, visible = true,displayText = false,subText = false,display = true,displayImage = false;
    String text, text2;
    Color textColor = Color.BLACK;
    Image imageIcon;
    GamePanel gp;

    Button(GamePanel gp){
        this.gp = gp;
        gp.clickableList.add(this);
    }

    Button(int x, int y, int width, int height, GamePanel gp){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gp = gp;
        gp.clickableList.add(this);
    }

    public void getPicture(int type){
        try {
            switch (type){
                case 0 :
                    imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/chest.png"))).getScaledInstance(100,100,Image.SCALE_SMOOTH);
                    break;
                case 1 :
                    imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/block.png"))).getScaledInstance(100,100,Image.SCALE_SMOOTH);
                    break;
                case 2 :
                    imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/dodge.png"))).getScaledInstance(100,100,Image.SCALE_SMOOTH);
                    break;
                case 3 :
                    imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/skip.png"))).getScaledInstance(100,100,Image.SCALE_SMOOTH);
                    break;
                default: imageIcon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("signUI.png")));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void draw(Graphics2D g2){
        if(visible) {

            if(display) {
                g2.fillRect(x, y, width, height);
            }
            if (displayImage) {
                g2.drawImage(imageIcon, x, y, width, height, null);
            }
            if (text != null && displayText){
                g2.setFont(new Font("SansSerif",Font.BOLD,font));
                g2.setColor(textColor);

                if(subText) {
                    FontMetrics fm = g2.getFontMetrics();
                    xText = (width - fm.stringWidth(text)) / 2;
                    yText = ((height - fm.getHeight()*2)/2) + fm.getAscent();
                    g2.drawString(text, x + xText, y + yText);

                    xText = (width - fm.stringWidth(text2)) / 2;
                    yText = ((height - fm.getHeight()/2) / 2) + fm.getAscent();
                    g2.drawString(text2, x + xText, y + yText);
                }else{
                    FontMetrics fm = g2.getFontMetrics();
                    xText = (width - fm.stringWidth(text)) / 2;
                    yText = ((height - fm.getHeight()) / 2) + fm.getAscent();

                    g2.drawString(text, x + xText, y + yText);
                }
            }
        }
    }
    public void update(){

    }

    public void displaySubText(String text){
        this.text2 = text;
        this.subText = true;
    }

    public void displayText(String text , Color color){
        this.text = text;
        this.textColor = color;
        displayText = true;
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

    public void setClickable(){this.clickable = !this.clickable;}
    public boolean getClickable(){return clickable;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public boolean getBool(){return active;}
}
