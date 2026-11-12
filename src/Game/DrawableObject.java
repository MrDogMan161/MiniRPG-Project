package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public abstract class DrawableObject {
    public int x,y,width,height;
    public float prevX,prevY,velX,velY;
    public Image image;
    public boolean visible = false , mirrored = false;
    public GamePanel gp;

    public Image loadImage(String name){
        try {
            return switch (name) {
                // Entity
                case "HealthBar"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/healthBar.png")));
                case "EntityShadow"-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/shadow.png")));
                case "Warrior"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/warrior.png")));
                case "Mage"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/mage.png")));
                case "Archer"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/archer.png")));
                case "Zombie"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/zombie.png")));
                case "Skeleton",
                     "3Skeleton"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/skeleton.png")));
                case "Vampire"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/vamp.png")));
                case "EyeMonster",
                     "EyeMonsters" -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/eyeEnemy.png")));
                case "FireSkull"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/fireSkull.png")));
                case "WaterHand"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/waterEnemy.png")));
                case "Knight"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/knight.png")));

                case "FireEffect"  ->  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/fireEffect.png")));
                case "FreezeEffect"->  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/freezeEffect.png")));
                case "FireIcon"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/fireIcon.png")));
                case "FreezeIcon"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/freezeIcon.png")));

                //case "EyeEnemy"  : sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/eyeEnemy.png")));break;
                case "Necromancer" -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/necromancer.png")));
                // Event
                case "Chest"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestClosed.png")));
                case "OpenChest"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/chestOpened.png")));
                case "Heal"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/healFountain.png")));
                case "Skill"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/skillStone.png")));
                case "Shop"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/shop.png")));
                // Artifact
                case "Test Shield" -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightShield.png")));
                case "Test Sword"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/knightSword.png")));

                case "Training Arrow"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/trainingArrow.png")));
                case "Novice Scroll"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/noviceScroll.png")));
                case "Wooden Shield"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/woodenShield.png")));
                case "Sharpening Stone"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/sharpeningStone.png")));
                case "Veteran Helmet"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/veteranHelmet.png")));
                case "Iron Shield"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/ironShield.png")));
                case "Steel Heart"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/steelHeart.png")));
                case "Copper Brace"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/copperBraclet.png")));
                case "Lucky Coin"         -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/luckyCoin.png")));
                case "Lucky Dice"         -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/luckyDice.png")));
                case "Thorn"              -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/thorn.png")));
                case "Berserk Heart"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/berserkHeart.png")));
                case "Skull of Suffering" -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/skullOfSuffering.png")));
                case "Fire Scroll"        -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/fireScroll.png")));
                case "Icicle"             -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/icicle.png")));
                case "Magic Crystal"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Artifacts/magicCrystal.png")));
                // GUI Components
                case "ActionBook"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/actionBook.png")));
                case "ButtonSkip"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/skip.png")));
                case "SignUI"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/signUI.png")));
                case "SignUIA"       -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/signUIA.png")));

                case "SwordIcon"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/swordIcon.png")));
                case "HealthIcon"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/hpIcon.png")));
                case "ManaIcon"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/manaIcon.png")));
                case "DefenceIcon"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/defenceIcon.png")));

                case "SwordIcon8"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/swordIcon.png")));
                case "HealthIcon8"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/hpIcon.png")));
                case "ManaIcon8"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/manaIcon.png")));
                case "DefenceIcon8"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/chars/defenceIcon.png")));

                case "EquipmentUI"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/equipment.png")));
                case "ArmorImage"   -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/armor.png")));
                case "WeaponImage"  -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/weapon.png")));
                case "NecklaceImage"-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/actionBookFolder/necklace.png")));


                case "MapBackground"-> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapBackground.png")));
                case "ScrollBut"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/scrollBut.png")));

                //case "BridgeD"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/bridgeD.png")));
                case "MapTower"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/towerIconMap.png")));
                case "MapEnemy"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapEnemy.png")));
                case "MapHeal"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapHeal.png")));
                case "MapEvent"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapEvent.png")));
                case "MapShop"      -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapShop.png")));
                case "MapEnemyC"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapEnemyC.png")));
                case "MapHealC"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapHealC.png")));
                case "MapEventC"    -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapEventC.png")));
                case "MapShopC"     -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/event/mapShopC.png")));

                default -> {System.out.println("Something wong " + name);yield null;}
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2,float alpha){
        if(visible){
            float renderX = prevX+alpha*(x-prevX);
            float renderY = prevY+alpha*(y-prevY);
            g2.drawImage(image, (int) renderX, (int) renderY,width,height,null);
        }
    }

    public void setSize(){
        width  = (int)(image.getWidth(null)*gp.pixelMult);
        height = (int)(image.getHeight(null)*gp.pixelMult);
    }
    public void update(){
        this.x += velX;
        this.y += velY;
    }
    public void savePrevious(){
        prevX = x;
        prevY = y;
    }

    public void setVisible(boolean visible){this.visible = visible;}
    public boolean haveImage(){return image!=null;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
}
