
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class bPanel {

    int x,y,width,height,widthH,heightH,margin,cols,rows,spX = 0,spY = 0;
    double multX, multY;
    boolean visible = false;
    Image image, head;
    GamePanel gp;
    Button[] buttons;

    bPanel(double x,double y,int butCount,GamePanel gp){
        this.multX = x;
        this.multY = y;
        buttons = new Button[butCount];
        this.gp = gp;
    }

    public void loadIcons(int type){
        try {
            switch (type) {
                case 0 :
                    image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("classTowerWall.png")));
                    head  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("classTowerHead.png")));
                    break;
                case 1 :
                    image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("attackCastleUI.png")));
                    head  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("castleHead.png")));
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(){
        this.x = multX==0 ? 0 : (int) (gp.getWidth()* multX - (double) this.width /2);
        this.y = multY==0 ? 0: (int) (gp.getHeight()*multY - (double) this.height /2);

        this.width = (int) (image.getWidth(null)* gp.globMult);
        this.height = (int) (image.getHeight(null)*gp.globMult);
        if(head!=null) {
            this.widthH = (int) (head.getWidth(null) * gp.globMult);
            this.heightH = (int) (head.getHeight(null) * gp.globMult);
        }

        for (int y1 = 0; y1 < rows; y1++) {
            for (int x1 = 0; x1 < cols; x1++) {

                Button but =  buttons[(y1*cols)+x1];
                but.setSize(((this.width-spX) - (cols * margin + margin)) / cols, ((this.height-spY)- (rows * margin + margin)) / rows);
                but.setPosition(x+margin+(x1 * but.width)+ (x1*margin) + (x>0?spX:0), y+margin+(y1*but.height)+y1*margin+(y>0?spY:0));

            }

        }
        for (Button but : buttons) {
            but.update();
        }
    }

    public void draw(Graphics2D g2){
        if(visible) {
            if (image != null){
                g2.drawImage(image, x, y, width, height, null);
                if (head !=null){
                    g2.drawImage(head, (int) (x-((widthH-width)/2)), y-heightH, widthH, heightH, null);
                }

            }
            else {g2.fillRect(x, y, width, height);}

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

    public void displayButtons(){
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

    public void addButton(String text, int font, int type, Color textColor){
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i]==null){
                buttons[i] = new Button(gp);
                buttons[i].displayText(text,textColor);
                buttons[i].getPicture(type);
                buttons[i].font = font;
                buttons[i].visible = false;
                break;
            }
        }


    }

}
