import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Artifacts {
    String name, statBonus, setName;
    boolean active = true,flatOrScale = false; // false = flat ; true =  % ;
    double bonusNum;
    int x,y,width = 100,height = 100 , id;
    Image image;
    GamePanel gp;

    Artifacts(){
    }
    Artifacts(String name,String setName,double bNum, boolean fOs,String statBonus,int id){
        this.name = name;
        this.setName = setName;
        this.bonusNum = bNum;
        this.flatOrScale = fOs;
        this.statBonus = statBonus;
        this.id = id;
        loadImages(id);
    }

    public void loadImages(int num){
        try{
            switch (num){
                case 0 : image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightShield.png"))); break;
                case 1 : image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightSword.png"))); break;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,width,height,null);
    }

    public static Artifacts getArtifact(int num){
        return switch(num){
            case 0 -> new Artifacts("Test Shield","Knight",5,false,"Defence",num);
            case 1 -> new Artifacts("Test Sword" ,"Knight",5,false,"Attack",num);

            default -> new Artifacts();
        };
    }

    public boolean checkCondition(){
        return switch (id){
            case 1 -> gp.player.defence > 0;

            default -> true;
        };
    }

}
