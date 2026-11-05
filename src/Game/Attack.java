package Game;

import java.util.Random;

public class Attack {
    public int power, manaUse, effectPower;
    String name , effect;
    AttackType curType;

    enum AttackType{
        MELEE,RANGE,MAGIC,BUFF,DEBUFF,SPEC,FIRE,WATER,GROUND,HOLY,DARK,POISON,NORMAL
    }

    Attack(String name,AttackType type,int power,int manaUse,String effect,int effectPower){
        this.power = power;
        this.manaUse = manaUse;
        this.curType = type;
        this.effectPower = effectPower;
        this.name = name;
        this.effect = effect;
    }

    public void doEffect(Entity target){
        switch (effect){

            case "Poison", "Fire" , "Freeze", "Holy", "Dark" , "Ground" :
                target.effects.add(Effects.getEffect(effect,1));
                target.lastDam = effect;
                break;


            default:
                target.effects.add(Effects.createEffect(curType==AttackType.DEBUFF? "Debuff" : "Buff",effect,effectPower,3));
                target.lastDam = curType==AttackType.BUFF? "+" +effectPower+" "+effect : effectPower+" "+effect;
                break;
        }

        target.floatingDam = true;
        target.mana = Math.min(target.mana, target.getMp());
        target.health = Math.min(target.health, target.getHp());
    }

    // an Actual Game.Attack method
    public void doAttack(Entity user,Entity enemy){
        if(user.CheckDead()){return;}
        if(user.getClass().getName().equals("Player") && user.mana==1 && manaUse>1){((Player)user).activateArtifacts(1);}
        if(user.mana<manaUse){return;}
        if(this.curType == AttackType.SPEC){
            switch (this.name){
                case "Undead Summon" : user.gp.spawnEnemy("Zombie",5); break;
                case "Bat Summon"    : user.gp.spawnEnemy("EyeMonster",5); break;
            }
            return;
        }

        if(effect!=null && !effect.isEmpty()) { doEffect(this.curType == AttackType.BUFF ? user : enemy ); }
        if(this.power>0){
            if(user.getClass().getName().equals("Player")){((Player)user).activateArtifacts(1);}
            if(enemy.getClass().getName().equals("Player")){((Player)enemy).activateArtifacts(3);}
            int rngDam =  new Random().nextInt((user.getAt()/2),user.getAt());
            double dam = (((this.power*user.powBonus) + rngDam )- enemy.getDf());
            user.powBonus = 1;
            enemy.getHurt(dam,this.curType);

            if(enemy.health<=0){enemy.health=0;}

        }
        user.mana -= manaUse;
        user.curHealth  = String.valueOf(user.health);
        enemy.curHealth = String.valueOf(enemy.health);
    }

    public static Attack getAttacks(String name,int num){
        Attack toReturn = null;
         switch (name){
             case "Warrior" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Sword Slash", AttackType.MELEE, 1, 0,  "", 0); break;
                     case 1 : toReturn = new Attack("Fire Slash",  AttackType.FIRE, 1, 0,  "Fire", 2); break;
                     case 2 : toReturn = new Attack("Freeze Slash",AttackType.WATER, 1, 0,  "Freeze", 1); break;
                     // 0,1,2,3 - attack by default
                     case 4 : toReturn = new Attack("Heavy Smash", AttackType.MELEE, 3, 2,  "", 0); break;
                     case 3 : toReturn = new Attack("Debuff",      AttackType.DEBUFF,0,0,"MaxHealth",10); break;

                     default : break;
                 } break;
             case "Mage" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Weak Missile",      AttackType.MAGIC,2,0,"",0); break;
                     // 0,1,2,3 - attack by default
                     case 4 : toReturn = new Attack("FireBall",     AttackType.FIRE,5,5,"",0); break;
                     case 5 : toReturn = new Attack("Dark missile", AttackType.DARK,3,5,"",0); break;
                     case 6 : toReturn = new Attack("Heal",         AttackType.BUFF,0,5,"Health",2); break;
                     case 7 : toReturn = new Attack("Strong Heal",  AttackType.BUFF,0,10,"Health",5); break;

                     default : break;
                 } break;
             case "Archer" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Arrow Shot",   AttackType.RANGE,2,0,"",0); break;
                     // 0,1,2,3 - attack by default
                     case 4 : toReturn = new Attack("Precise Shot", AttackType.RANGE,6,3,"",0); break;
                     case 5 : toReturn = new Attack("Triple Shot",  AttackType.RANGE,8,10,"",0); break;
                     case 6 : toReturn = new Attack("Debuff",       AttackType.DEBUFF,0,5,"Attack",-1); break;

                     default : break;
                 } break;
             case "Zombie" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Hit",          AttackType.MELEE,1,0,"",0);      break;
                     case 1 : toReturn = new Attack("Strong Hit",   AttackType.MELEE,3,2,"",0);      break;
                     case 2 : toReturn = new Attack("Weak Heal",    AttackType.BUFF,0,5,"Health",1); break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;
             case "Skeleton" :
                 switch (num) {
                     //case 0 : toReturn = new Attack("Hit",          AttackType.MELEE,0,0,"",0); break;
                     //case 1 : toReturn = new Attack("Bone Arrow",   AttackType.RANGE,0,2,"",0); break;
                     case 0 : toReturn = new Attack("Fire",         AttackType.DEBUFF,0,0,"MaxHealth",2); break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;
             case "Vampire" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Heavy Hit",    AttackType.MELEE,2,0,"",0); break;
                     case 1 : toReturn = new Attack("Blood Suck",   AttackType.BUFF,3,2,"Health",3); break;
                     case 2 : toReturn = new Attack("Bat Summon",   AttackType.SPEC,0,5,"Health",1); break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;
             case "EyeMonster" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Byte",    AttackType.MELEE,2,0,"",0); break;
                     // 1 : toReturn = new Game.Attack("Blood Suck",   AttackType.BUFF,3,2,"Health",3); break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;
             case "FireSkull","WaterHand","Knight" :
                 switch (num) {
                     case 0 : toReturn = new Attack("hit",    AttackType.MELEE,2,0,"",0); break;
                     // 1 : toReturn = new Game.Attack("Blood Suck",   AttackType.BUFF,3,2,"Health",3); break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;
             case "Necromancer" :
                 switch (num) {
                     case 0 : toReturn = new Attack("Strong Dark Missile", AttackType.MAGIC,10,5,"",0);      break;
                     case 1 : toReturn = new Attack("Strong Heal",         AttackType.BUFF,0,10,"Health",5); break;
                     case 2 : toReturn = new Attack("Strong DebuffA",      AttackType.BUFF,0,10,"Attack",5); break;
                     case 3 : toReturn = new Attack("Undead Summon",       AttackType.SPEC,0,5,"",0);        break;
                     // 0,1,2,3 - attack by default
                     default : break;
                 } break;

         }

         return toReturn;
         /*
        return switch (num){
            case 0 -> new Game.Attack("Sword Slash",  AttackType.MELEE,3,0,5,"",0);
            case 1 -> new Game.Attack("Heavy Smash",  AttackType.MELEE,4,2,2,"",0);
            case 2 -> new Game.Attack("Hit",          AttackType.MELEE,1,0,10,"",0);
            case 3 -> new Game.Attack("Strong Hit",   AttackType.MELEE,3,2,5,"",0);
            case 4 -> new Game.Attack("Weakening hit",AttackType.MELEE,5,0,2,"Game.Attack",-1);

            case 5 -> new Game.Attack("Missile",      AttackType.MAGIC,2,0,5,"",0);
            case 6 -> new Game.Attack("FireBall",     AttackType.MAGIC,5,5,3,"",0);
            case 7 -> new Game.Attack("Dark missile", AttackType.MAGIC,3,5,3,"",0);

            case 8 -> new Game.Attack("Arrow Shot",   AttackType.RANGE,3,0,5,"",0);
            case 9 -> new Game.Attack("Precise Shot", AttackType.RANGE,6,3,3,"",0);
            case 10 -> new Game.Attack("Triple Shot",  AttackType.RANGE,8,10,1,"",0);

            case 11 -> new Game.Attack("Damage Buff", AttackType.BUFF,0,5,5,"Game.Attack",1);
            case 12 -> new Game.Attack("Strong Heal", AttackType.BUFF,0,10,5,"Health",5);
            case 13 -> new Game.Attack("Heal",        AttackType.BUFF,0,5,5,"Health",2);

            case 14 -> new Game.Attack("Debuff",      AttackType.DEBUFF,0,5,5,"Defence",-1);
            case 15 -> new Game.Attack("Debuff",      AttackType.DEBUFF,0,5,5,"Game.Attack",-1);

            case 16 -> new Game.Attack("Counter",     AttackType.SPEC, 0,10,5,"",0);

            default ->  new Game.Attack("Slash",      AttackType.MELEE,100,1,1,"",0);
        };
*/
    }
}
