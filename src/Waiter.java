public class Waiter {
    int timer = 0;
    int FPS = 60;

    public boolean wait(int secs){
        int frames = FPS*secs;
        boolean toReturn = false;
            if(timer==frames){
                timer = 0;
                toReturn = true;
            }else {
                timer++;
            }
        return toReturn;
    }
}
