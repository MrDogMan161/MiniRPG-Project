import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity{
    int level = 0,moneyCount=0,expCount=0,expMax=100;
    Map<String,Integer> inventory = new ConcurrentHashMap<>();

    Player(GamePanel gp) {
        super(gp);
    }

    public void addItemToInv(String name,int count){
        if(inventory.containsKey(name)){
            inventory.put(name,inventory.get(name)+count);
        }else{
            inventory.put(name,count);
        }

    }
}
