public interface IClickable {

    boolean checkCollision(int mouseX, int mouseY);
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    boolean getClickable();
    void setClickable();
    boolean getBool();
    void action();
}
