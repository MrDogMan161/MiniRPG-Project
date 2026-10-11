


public class Effects {
    Attack.AttackType curType;
    String effect;
    int power,duration;

    Effects(Attack.AttackType type,int power,int duration,String effect){
        this.curType = type;
        this.power = power;
        this.duration = duration;
        this.effect = effect;
    }

    public static Effects createEffect(String type, String effect, int power, int duration){

        return switch (type){
            case "Poison" -> new Effects(Attack.AttackType.POISON,power,duration,effect);
            case "Fire"   -> new Effects(Attack.AttackType.FIRE,  power,duration,effect);
            case "Holy"   -> new Effects(Attack.AttackType.HOLY,  power,duration,effect);
            case "Dark"   -> new Effects(Attack.AttackType.DARK,  power,duration,effect);
            case "Ground" -> new Effects(Attack.AttackType.GROUND,power,duration,effect);

            default -> new Effects(Attack.AttackType.BUFF,power,duration,effect);
        };
    }

    public void doEffect(Entity entity){
        if (curType != Attack.AttackType.BUFF && curType != Attack.AttackType.DEBUFF) {
            entity.getHurt(1, curType); // poison
        }else{
            entity.health = Math.min(entity.health++, entity.maxHealth);
        }
    }
}
