import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Attack {
    int power, manaUse,maxUses,curUses, effectPower;
    String name , effect;
    boolean target = true;
    AttackType curType;

    enum AttackType{
        MELEE,RANGE,MAGIC,BUFF,DEBUFF,SPEC
    }

    Attack(String name,AttackType type,int power,int manaUse,int maxUses,String effect,int effectPower){
        this.power = power;
        this.manaUse = manaUse;
        this.maxUses = maxUses;
        this.curType = type;
        this.effectPower = effectPower;
        curUses = maxUses;
        this.name = name;
        this.effect = effect;
    }

    public void doEffect(Entity target){
        switch (effect){
            case "Defence"   : target.defence   += effectPower; break;
            case "Mana"      : target.mana      += effectPower; break;
            case "MaxMana"   : target.maxMana   += effectPower; break;
            case "Health"    : target.health    += effectPower; break;
            case "MaxHealth" : target.maxHealth += effectPower; break;
            case "Attack"    : target.attack    += effectPower; break;
        }
        target.mana = Math.min(target.mana, target.maxMana);
        target.health = Math.min(target.health, target.maxHealth);
    }

    public void doAttack(Entity user,Entity enemy){
        if(user.CheckDead()){return;}
        if(curUses<=0){return;}

        if(effect!=null) { doEffect(this.curType == AttackType.BUFF ? user : enemy ); }

        double dam = ((this.power+ new Random().nextInt((int)(user.attack/2),user.attack))- enemy.defence);
        enemy.health = (int) (enemy.health - Math.max(dam, 1));
        enemy.lastDam = (int) dam;
        enemy.floatingDam = true;
        if(enemy.health<=0){enemy.health=0;}
        System.out.println(this.power + " " +dam + " " + enemy.health + " " + ((double) enemy.health / enemy.maxHealth));


    }

    public static Attack getAttacks(int num){
        return switch (num){
            case 0 -> new Attack("Sword Slash",  AttackType.MELEE,3,0,5,"",0);
            case 1 -> new Attack("Heavy Blow",   AttackType.MELEE,4,2,2,"",0);
            case 2 -> new Attack("Hit",          AttackType.MELEE,1,0,10,"",0);
            case 3 -> new Attack("Strong Hit",   AttackType.MELEE,3,2,5,"",0);
            case 4 -> new Attack("Weakening hit",AttackType.MELEE,5,0,2,"Attack",-1);

            case 5 -> new Attack("Missile",      AttackType.MAGIC,2,0,5,"",0);
            case 6 -> new Attack("FireBall",     AttackType.MAGIC,5,5,3,"",0);
            case 7 -> new Attack("Dark missile", AttackType.MAGIC,3,5,3,"",0);

            case 8 -> new Attack("Arrow Shot",   AttackType.RANGE,3,0,5,"",0);
            case 9 -> new Attack("Precise Shot", AttackType.RANGE,6,3,3,"",0);
            case 10 -> new Attack("Triple Shot",  AttackType.RANGE,8,10,1,"",0);

            case 11 -> new Attack("Damage Buff", AttackType.BUFF,0,5,5,"Attack",1);
            case 12 -> new Attack("Strong Heal", AttackType.BUFF,0,10,5,"Health",5);
            case 13 -> new Attack("Heal",        AttackType.BUFF,0,5,5,"Health",2);

            case 14 -> new Attack("Debuff",      AttackType.DEBUFF,0,5,5,"Defence",-1);
            case 15 -> new Attack("Debuff",      AttackType.DEBUFF,0,5,5,"Attack",-1);

            case 16 -> new Attack("Counter",     AttackType.SPEC, 0,10,5,"",0);

            default ->  new Attack("Slash",      AttackType.MELEE,100,1,1,"",0);
        };

    }
}
