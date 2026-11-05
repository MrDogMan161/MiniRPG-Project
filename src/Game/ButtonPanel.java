package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ButtonPanel {

    int x,y,width,height,widthH,heightH,margin,cols,rows,spX = 0,spY = 0;
    boolean visible = false,display = false;
    Image image, head;
    GamePanel gp;
    Button[] buttons;

    ButtonPanel(int x, int y,int width,int height, int butCount, GamePanel gp){
        this.gp = gp;
        this.width = width;
        this.height = height;

        buttons = new Button[butCount];
       this.x = x;
       this.y = y;
    }

    public void update(){
        for (Button but : buttons) {
            but.update();
        }
    }

    public void draw(Graphics2D g2){
        if(visible) {
            if (image != null){
                g2.drawImage(image, x, y, width, height, null);

            }else if(display) {
                g2.fillRect(x, y, width, height);
            }

        }
    }
    public void setButtonImage(String name){
        for (Button but : buttons) {
            but.image = but.loadImage(name);
            but.displayImage = true;
            but.visible = true;
            but.display = false;

        }
    }
    public void setButtonsSize(int x ,int y){
        if (buttons != null) {
            for (Button but : buttons) {
                but.width = x;
                but.height = y;
            }
        }
    }
    public void updateButPos(){
        for (int y1 = 0; y1 < rows; y1++) {
            for (int x1 = 0; x1 < cols; x1++) {

                Button but =  buttons[(y1*cols)+x1];
                but.setPosition(x+margin+(x1 * but.width)+ (x1*margin) + (x>0?spX:0), y+margin+(y1*but.height)+y1*margin+(y>0?spY:0));

            }

        }
    }

    public void setVisible(boolean visible){
        this.visible = visible;
        for (Button but : buttons){
            but.visible = visible;
            but.clickable = visible;
        }
    }

    public void setRowsColsMargin(int r,int  c,int m){
        this.rows = r;
        this.cols = c;
        this.margin = m;
    }

    public void disableButtonsDisplay(){
        for (Button but : buttons){
            but.display = false;
            but.displayImage = false;
        }
    }

    public void displayImageButtons(){
        for (Button but : buttons){
            but.display = false;
            but.displayImage = true;
        }
    }

    public void addButton(String text, int font ,Color textColor){
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i]==null){
                buttons[i] = new Button(gp);
                buttons[i].displayText(text,textColor);
                buttons[i].font = font;
                buttons[i].visible = false;
                break;
            }
        }


    }

    public void drawButtons(Graphics2D g2){
        if (buttons != null) {
            for (Button but : buttons) {
                but.draw(g2);
            }
        }
    }

}
