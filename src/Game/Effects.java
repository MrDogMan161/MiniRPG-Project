package Game;


import java.awt.*;

public class Effects extends DrawableObject {
    Attack.AttackType curType;
    String effect;
    int power,duration;
    boolean isActivated = false;

    Effects(Attack.AttackType type,int power,int duration,String effect){
        this.curType = type;
        this.power = power;
        this.duration = duration;
        this.effect = effect;
        switch (effect){
            case "Fire"      : image = loadImage("FireIcon");    break;
            case "Freeze"    : image = loadImage("FreezeIcon");  break;
            case "Health",
                 "MaxHealth" : image = loadImage("HealthIcon8");  break;
            case "Attack"    : image = loadImage("SwordIcon8");   break;
            case "Defence"   : image = loadImage("DefenceIcon8"); break;
            case "Mana"      : image = loadImage("ManaIcon8");    break;

        }
    }

    public static Effects getEffect(String type,int level){

        return switch (type){
            case "Poison" -> new Effects(Attack.AttackType.POISON,level,3,"Attack");
            case "Holy"   -> new Effects(Attack.AttackType.HOLY,  level,3,"Defence");
            case "Dark"   -> new Effects(Attack.AttackType.DARK,  level,3,"MaxHealth");
            case "Ground" -> new Effects(Attack.AttackType.GROUND,level,3,"Defence");

            case "Fire"   -> new Effects(Attack.AttackType.FIRE,  level,3,"Fire");
            case "Freeze" -> new Effects(Attack.AttackType.WATER, level,3,"Freeze");


            default -> null;
        };
    }

    public static Effects createEffect(String type, String effect, int power, int duration){

        return switch (type){
            case "Poison" -> new Effects(Attack.AttackType.POISON,power,duration,effect);
            case "Fire"   -> new Effects(Attack.AttackType.FIRE,  power,duration,effect);
            case "Holy"   -> new Effects(Attack.AttackType.HOLY,  power,duration,effect);
            case "Dark"   -> new Effects(Attack.AttackType.DARK,  power,duration,effect);
            case "Ground" -> new Effects(Attack.AttackType.GROUND,power,duration,effect);
            case "Freeze" -> new Effects(Attack.AttackType.WATER, power,duration,effect);
            case "Debuff" -> new Effects(Attack.AttackType.DEBUFF,power,duration,effect);

            default -> new Effects(Attack.AttackType.BUFF,power,duration,effect);
        };
    }

    public void doEffect(Entity entity){
        if(curType != Attack.AttackType.BUFF){
            switch (effect){
                case "Health"    : if(!isActivated) {isActivated=true; entity.health -=power;} break;
                case "MaxHealth" : if(!isActivated) {isActivated=true; entity.hpBonus-=power;} break;
                case "Attack"    : if(!isActivated) {isActivated=true; entity.atBonus-=power;} break;
                case "Defence"   : if(!isActivated) {isActivated=true; entity.dfBonus-=power;} break;
                case "Mana"      : if(!isActivated) {isActivated=true; entity.mpBonus-=power;} break;

                case "Fire"      : entity.loadEffect("Fire"); entity.health -=power; break;
                case "Freeze"    : entity.loadEffect("Freeze"); entity.gp.turn++; break;

            }

        }else{
            entity.health = Math.min(entity.health+1, entity.maxHealth);
        }
        duration--;
        entity.mana = Math.min(entity.mana, entity.getMp());
        entity.health = Math.min(entity.health, entity.getHp());

        if(duration<=0){
            switch (effect){
                case "Health"    : entity.health +=power; break;
                case "MaxHealth" : entity.hpBonus+=power; break;
                case "Attack"    : entity.atBonus+=power; break;
                case "Defence"   : entity.dfBonus+=power; break;
                case "Mana"      : entity.mpBonus+=power; break;

            }
            entity.effectIm=null;
            entity.effects.remove(this);
        }
    }
}
