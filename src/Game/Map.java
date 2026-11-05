package Game;

import java.awt.*;
import java.util.Random;

public class Map extends DrawableObject{
    Image bridgeU,bridgeS,bridgeD,towerIm;
    Image enemy,eventF, eventE,eventSh,enemyC,eventFC, eventEC,eventShC;
    int[][][] path = new int[15][8][4]; // 0,1,2 connected points; 3 - value
    int currentRoom = -1,currentFloor = -1;
    Button path1,path2,path3;
    Waiter waiter = new Waiter();
    int start1,start2;
    // Chance for room to appear
    int mRoom = 45;
    int hRoom = 20;
    int eRoom = 30;
    int shRoom = 5;

    //int bridgeWidth,bridgeHeight, bridgeWidthS,bridgeHeightS;
    int towerWidth,towerHeight,marginX=100,marginY=100,offsetX,offsetY;

    Map(GamePanel gp){
        this.gp = gp;
        image = loadImage("MapBackground");
        //bridgeU = loadImage("BridgeU");
        //bridgeS = loadImage("BridgeS");
        //bridgeD = loadImage("BridgeD");
        towerIm = loadImage("MapTower");
        enemy   = loadImage("MapEnemy");
        eventF  = loadImage("MapHeal");
        eventE  = loadImage("MapEvent");
        eventSh = loadImage("MapShop");
        enemyC   = loadImage("MapEnemyC");
        eventFC  = loadImage("MapHealC");
        eventEC  = loadImage("MapEventC");
        eventShC = loadImage("MapShopC");

        towerWidth = (int) (enemy.getWidth(null)*gp.pixelMult);
        towerHeight = (int) (enemy.getHeight(null)*gp.pixelMult);
        //bridgeWidthS = (int) (bridgeS.getWidth(null)*gp.pixelMult);
        //bridgeHeightS = (int) (bridgeS.getHeight(null)*gp.pixelMult);
        //bridgeWidth = (int) (bridgeD.getWidth(null)*gp.pixelMult);
        //bridgeHeight = (int) (bridgeD.getHeight(null)*gp.pixelMult);


        setSize();
        this.x = gp.width/2 -this.width/2;
        this.y = gp.height/2-this.height/2;

    }

    public void update(){
        if(gp.kH.dragToX!=0 && gp.kH.dragToY!= 0) {

            offsetX += offsetX+(gp.kH.dragToX - gp.kH.curX)/5>1100 || offsetX+(gp.kH.dragToX - gp.kH.curX)/5<-2000 ? 0 : (gp.kH.dragToX - gp.kH.curX)/5;
            offsetY += offsetY+(gp.kH.dragToY - gp.kH.curY)/5<-700 || offsetY+(gp.kH.dragToY - gp.kH.curY)/5> 450 ? 0 : (gp.kH.dragToY - gp.kH.curY)/5;
        }
       if(currentFloor!=-1) {
           if (path1 != null) {path1.setPosition(((currentFloor +1) * marginX) + ((currentFloor +1) * towerWidth) + x + offsetX, ((currentRoom - 2) * marginY) + towerHeight + marginY + y + offsetY);}
           if (path2 != null) {path2.setPosition(((currentFloor +1) * marginX) + ((currentFloor +1) * towerWidth) + x + offsetX, ((currentRoom-1) * marginY) + towerHeight + marginY + y + offsetY);}
           if (path3 != null) {path3.setPosition(((currentFloor +1) * marginX) + ((currentFloor +1) * towerWidth) + x + offsetX, ((currentRoom) * marginY) + towerHeight + marginY + y + offsetY);}
       }else{
           if (path1 != null) {path1.setPosition(x + offsetX,((start1-1) * marginY) + towerHeight + marginY + y + offsetY);}
           if (path2 != null) {path2.setPosition(x + offsetX,((start2-1) * marginY) + towerHeight + marginY + y + offsetY);}
       }
    }

    public void draw(Graphics2D g2){
        if (visible){
            g2.drawImage(image,x,y,width,height,null);

            for (int i = 0; i < path.length; i++) {
                for (int j = 0; j < path[0].length; j++) {
                    int posX = (i*marginX)+(i*towerWidth)+x+offsetX, posY = (j*marginY)+towerHeight+marginY+y+offsetY;

                    if(i==currentFloor && j==currentRoom-1 && path[i][j][3]>0){path[i][j][3] *= -1;}

                    if(posX<x+this.width-towerWidth && posY<y+this.height-towerHeight && posX>x && posY>y) {

                        switch (path[i][j][3]){
                            case 1 : g2.drawImage(enemy,  posX,posY,towerWidth,towerHeight,null); break;
                            case 2 : g2.drawImage(eventE, posX,posY,towerWidth,towerHeight,null); break;
                            case 3 : g2.drawImage(eventSh,posX,posY,towerWidth,towerHeight,null); break;
                            case 4 : g2.drawImage(eventF, posX,posY,towerWidth,towerHeight,null); break;
                            case -1 : g2.drawImage(enemyC,  posX,posY,towerWidth,towerHeight,null); break;
                            case -2 : g2.drawImage(eventEC, posX,posY,towerWidth,towerHeight,null); break;
                            case -3 : g2.drawImage(eventShC,posX,posY,towerWidth,towerHeight,null); break;
                            case -4 : g2.drawImage(eventFC, posX,posY,towerWidth,towerHeight,null); break;
                        }


                        /*if (path[i][j][3] == 0) {
                            g2.drawImage(towerIm,posX,posY,towerWidth, towerHeight,null);
                        }*/
                        if (path[i][j][3] != 0) {
                            g2.setStroke(new BasicStroke(5,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10, new float[]{10, 5}, 0));
                            int lineStartX = posX+towerWidth,lineStartY = posY+towerHeight/2;
                            int lineEndX = posX+marginX+towerWidth;
                            //if(posY==((currentRoom-1)*marginY)+towerHeight+marginY+y+offsetY && posX == ((currentFloor)*marginX)+((currentFloor)*towerWidth)+x+offsetX ){g2.setColor(Color.RED);}else{g2.setColor(Color.BLACK);}

                            if (j != 0 && path[i][j][0] != 0 && posX + towerWidth + marginX<x+width-towerWidth && posY - marginY<y+this.height-towerHeight && posY-marginY>y) {

                                g2.drawLine(lineStartX,lineStartY,lineEndX,lineStartY-marginY);
                                //g2.drawImage(bridgeU,posX+towerWidth-18,posY-150,bridgeWidth,bridgeHeight,null);
                            }
                            if ( posX + towerWidth / 2 + marginX<x+width-towerWidth && posY + towerHeight / 2<y+this.height-towerHeight && path[i][j][1] != 0 || (i != 14 && j == 0 && path[i + 1][j][3] != 0)) {
                                g2.drawLine(lineStartX,lineStartY,lineEndX,lineStartY);
                                //g2.drawImage(bridgeS,posX+towerWidth,posY,bridgeWidthS,bridgeHeightS,null);
                            }
                            if ( posX + towerWidth / 2 + marginX<x+width-towerWidth && posY + towerHeight / 2 + marginY<y+this.height-towerHeight && path[i][j][2] != 0 ) {
                                g2.drawLine(lineStartX,lineStartY,lineEndX,lineStartY+marginY);
                                // g2.drawImage(bridgeD,posX+towerWidth-6,posY+12,bridgeWidth,bridgeHeight,null);
                            }
                        }
                    }

                    /*if (path2 != null) {path2.draw(g2);}
                    if (path1 != null) {path1.draw(g2);}
                    if (path3 != null) {path3.draw(g2);}*/
                }
            }
        }
    }

    public int generateEvent(){
        int rng = new Random().nextInt(100);
        int toReturn = 0;

        if(rng<eRoom){ // 0 - 4
            toReturn = 2; // chest
        }else if(rng<shRoom+eRoom){ // 5 - 9
            toReturn = 3; // shop
        }else if(rng<hRoom+shRoom+eRoom){ // 10 - 19
            toReturn = 4; // heal
        }else if(rng<mRoom+hRoom+shRoom+eRoom){
            toReturn = 1;
        }

        return toReturn;
    }

    public void generatePath() {
        start1 = new Random().nextInt(1, path[0].length + 1); // from 1 to 8
        start2 = new Random().nextInt(1, path[0].length + 1); // from 1 to 8
        while (start2 == start1) {
            start2 = new Random().nextInt(1,path[0].length+1);
        }

        path[0][start1-1][3] = 1; // first room always monster
        path[0][start2-1][3] = 1; // first room always monster

        int times = 6;

        for (int i = 0; i < times; i++) { // 8
            int rng = new Random().nextInt(2);
            int prevP = rng == 0 ? start1 : start2; // form 1 to 8

            for (int y = 1; y < path.length; y++) { // 15
                // min = -1 =>

                int minRange = prevP != 1 && path[y - 1][prevP - 2][2] == 0 ? prevP - 1 : prevP;
                int maxRange = prevP != path[0].length && path[y - 1][prevP][0] == 0 ? prevP + 1 : prevP;

                int rng12 = new Random().nextInt(minRange, maxRange + 1); // next room pos

                path[y - 1][prevP - 1][rng12 - 1 < prevP - 1 ? 0 : rng12 - 1 == prevP - 1 ? 1 : 2] = rng12 == 1 ? -1 : rng12 - 1;

                path[y][rng12 == 1 ? 0 : rng12 - 1][3] = generateEvent(); // Assignig Events
                prevP = rng12;
            }

        }
    }

    public void showMap(){
        visible=true;
        gp.clearBonuses();
        gp.player.activateArtifacts(0);
        if(currentFloor==-1){
            //System.out.println("Starting");
            int posX = x+offsetX, posY = ((start1)*marginY)+towerHeight+marginY+y+offsetY;

            path1 = new Button(posX, posY, towerWidth, towerHeight, gp){
                @Override
                public void action(){
                    currentRoom = start1;
                    currentFloor++;
                    System.out.println((currentRoom-1) + " "+ currentFloor);
                    gp.executeEvent(path[currentFloor][currentRoom-1][3]);
                    gp.map.visible = false;
                    if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}
                    if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}
                    if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}
                }
            };
            int posX1 = x+offsetX, posY1 = (start2*marginY)+towerHeight+marginY+y+offsetY;
            path2 = new Button(posX1, posY1, towerWidth, towerHeight, gp){
                @Override
                public void action(){
                    currentRoom = start2;
                    currentFloor++;
                    System.out.println((currentRoom-1) + " "+ currentFloor);
                    gp.executeEvent(path[currentFloor][currentRoom-1][3]);
                    gp.map.visible = false;
                    if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}
                    if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}
                    if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}
                }
            };
            path1.visible = true;
            path1.clickable = true;
            path2.visible = true;
            path2.clickable = true;
        }else {
            //System.out.println("Check next");
            checkNextRoom();
        }

    }

    public void checkNextRoom(){
        int[] curRm = getCurRoom();

        if(curRm[0] != 0){
            int posX = ((currentFloor+2)*marginX)+((currentFloor+1)*towerWidth)+x+offsetX, posY = ((currentRoom-3)*marginY)+towerHeight+marginY+y+offsetY;
            if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}

            path1 = new Button(posX, posY, towerWidth, towerHeight, gp){
                @Override
                public void action(){
                    currentRoom--;
                    currentFloor++;
                    System.out.println(currentRoom + " "+ currentFloor);
                    gp.executeEvent(path[currentFloor][currentRoom-1][3]);
                    gp.map.visible = false;
                    if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}
                    if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}
                    if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}

                }
            };
            path1.visible = true;
            path1.clickable = true;

        }
        if(curRm[1] != 0){

            int posX = ((currentFloor+2)*marginX)+((currentFloor+1)*towerWidth)+x+offsetX, posY = ((currentRoom-2)*marginY)+towerHeight+marginY+y+offsetY;
            if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}

            path2 = new Button(posX, posY, towerWidth, towerHeight, gp){
                @Override
                public void action(){
                    currentFloor++;
                    System.out.println(currentRoom + " "+ currentFloor);
                    gp.executeEvent(path[currentFloor][currentRoom-1][3]);
                    gp.map.visible = false;
                    if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}
                    if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}
                    if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}
                }
            };
            path2.visible = true;
            path2.clickable = true;
        }
        if(curRm[2] != 0) {
            int posX = ((currentFloor + 2) * marginX) + ((currentFloor) * towerWidth) + x + offsetX, posY = ((currentRoom) * marginY) + towerHeight + marginY + y + offsetY;
            if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}

            path3 = new Button(posX, posY, towerWidth, towerHeight, gp) {
                @Override
                public void action() {
                    currentRoom++;
                    currentFloor++;
                    System.out.println(currentRoom + " "+ currentFloor);
                    gp.executeEvent(path[currentFloor][currentRoom-1][3]);
                    gp.map.visible = false;
                    if(path1!=null) {gp.clickableList.remove(path1);path1 = null;}
                    if(path2!=null) {gp.clickableList.remove(path2);path2 = null;}
                    if (path3 != null) {gp.clickableList.remove(path3);path3 = null;}
                }
            };
            path3.visible = true;
            path3.clickable = true;
        }
    }

    public void checkEventCompletion(){
        if(currentFloor==-1){return;}
        switch (getCurEvent()){
            case 1 :  if(!gp.enm1.visible && !gp.enm2.visible && !gp.enm3.visible && Chooser.choserList.isEmpty()){showMap();} break;
            case 2,3,4 :  if(!gp.event.visible && Chooser.choserList.isEmpty()){ if(waiter.wait(1)){showMap();} } break;
        }
    }

    public int[] getCurRoom(){return path[currentFloor][currentRoom-1];}
    public int getCurEvent(){return path[currentFloor][currentRoom-1][3];}

}
