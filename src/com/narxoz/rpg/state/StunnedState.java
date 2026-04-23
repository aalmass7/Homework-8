package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState{

    private int turnsLeft;

    public StunnedState(int turnsLeft) {
        this.turnsLeft = Math.max(1, turnsLeft);
    }

    @Override
    public String getName() {
        return "Stunned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage + 1;
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("   [STATE EFFECT] " + hero.getName() + " is stunned and cannot act.");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsLeft--;
        System.out.println("   [STATE TIMER] Stun on " + hero.getName() + " will last for " + turnsLeft + " more turns.");
        if (turnsLeft <= 0 && hero.isAlive()) {
            System.out.println("   [STATE] Stun is gone.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return false;
    }
}
