package Game.GUI;

import Game.DrawableObject;
import Game.GamePanel;

import java.awt.*;

public class ButtonPanel extends DrawableObject {

    int marginX,marginY,cols,rows,spX = 0,spY = 0;
    boolean display = false;
    public Button[] buttons;

    public ButtonPanel(int x, int y, int width, int height, int butCount, GamePanel gp){
        this.gp = gp;
        this.width = width;
        this.height = height;

        buttons = new Button[butCount];
        this.x = x;
        this.y = y;
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
        for (Game.GUI.Button but : buttons) {
            but.image = but.loadImage(name);
            but.displayImage = true;
            but.visible = false;
            but.display = false;

        }
    }
    public void setButtonsSize(int x ,int y){
        if (buttons != null) {
            for (Game.GUI.Button but : buttons) {
                but.width = x;
                but.height = y;
            }
        }
    }

    public void enableButtons(boolean enable){
        if (buttons != null) {
            for (Game.GUI.Button but : buttons) {
                but.clickable = enable;

            }
        }
    }
    public void updateButPos(){
        for (int y1 = 0; y1 < rows; y1++) {
            for (int x1 = 0; x1 < cols; x1++) {

                Game.GUI.Button but =  buttons[(y1*cols)+x1];
                but.setPosition(x+marginX+(x1 * but.width)+ (x1*marginX) , y+marginY+(y1*but.height)+((y1)*marginY));

            }

        }
    }

    public void setPos(int x,int y){
        this.x = x; this.y = y;
    }
    public void setVisible(boolean visible){
        this.visible = visible;
        for (Game.GUI.Button but : buttons){
            but.setVisible(visible);

        }
    }
    public void setButtonVisible(boolean visible){
        for (Game.GUI.Button but : buttons){
            but.visible = visible;
            if(but.image!=null) {but.displayImage = true;}
        }
    }

    public void setRowsColsMargin(int r,int  c,int mX,int mY){
        this.rows = r;
        this.cols = c;
        this.marginX = mX;
        this.marginY = mY;
    }

    public void disableButtonsDisplay(){
        for (Game.GUI.Button but : buttons){
            but.display = false;
            but.displayImage = false;
        }
    }

    public void displayImageButtons(){
        for (Game.GUI.Button but : buttons){
            but.display = false;
            but.displayImage = true;
        }
    }

    public void addButton(String text, int font ,Color textColor){
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i]==null){
                buttons[i] = new Game.GUI.Button(gp);
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
                but.draw(g2,gp.alpha);
            }
        }
    }

}
