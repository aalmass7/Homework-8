package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState{

    private int turnsLeft;

    public PoisonedState(int turnsLeft) {
        this.turnsLeft = Math.max(1, turnsLeft);
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(1, basePower - 2);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage + 1;
    }

    @Override
    public void onTurnStart(Hero hero) {
        hero.receivePureDamage(3, "poison");
        if (!hero.isAlive()) {
            System.out.println("   [STATE] " + hero.getName() + " falls because of poison.");
        }
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsLeft--;
        System.out.println("   [STATE TIMER] Poison on " + hero.getName() + " will last for " + turnsLeft + " more turns.");
        if (turnsLeft <= 0 && hero.isAlive()) {
            System.out.println("   [STATE] Poison wears off.");
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
