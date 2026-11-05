package Game;

import java.awt.event.*;

public class KeyHandler implements KeyListener, MouseListener,MouseMotionListener {

    public boolean upPressed,downPressed,leftPressed,rightPressed,lMousePressed,mouseMoving;
    public int lastPressX,lastPressY,curX,curY,dragToX,dragToY,lastX,lastY;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lMousePressed = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPressX = e.getX();
        lastPressY = e.getY();
        lMousePressed = false;
        dragToX=0;
        dragToY=0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragToX = e.getX();
        dragToY = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        curX = e.getX();
        curY = e.getY();
    }
}
