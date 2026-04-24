package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class GladiatorState implements HeroState{

    @Override
    public String getName(){
        return "Gladiator";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower + (basePower / 2);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage + 2;
    }

    @Override
    public void onTurnStart(Hero hero) {
        if (hero.getHp() > hero.getMaxHp() / 2) {
            System.out.println("   [STATE CHECK] " + hero.getName() + " feels calm again.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public void onTurnEnd(Hero hero) {
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
