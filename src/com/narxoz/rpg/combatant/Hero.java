package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState();
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }

    public String getStateName() {
        return state.getName();
    }

    public boolean isLowHp() {
        return hp * 2 <= maxHp;
    }

    public void setState(HeroState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Hero state cannot be null");
        }

        String oldName = state == null ? "None" : state.getName();
        this.state = newState;

        if (oldName.equals(newState.getName())) {
            System.out.println("   [STATE] " + name + " remains " + newState.getName() + ".");
        } else {
            System.out.println("   [STATE] " + name + ": " + oldName + " -> " + newState.getName());
        }
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - Math.max(0, amount));
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + Math.max(0, amount));
    }

    public void startTurn() {
        if (!isAlive()) {
            return;
        }
        System.out.println("   [TURN START] " + name + " (HP " + hp + "/" + maxHp + ", state: " + state.getName() + ")");
        state.onTurnStart(this);
    }

    public void endTurn() {
        if (!isAlive()) {
            return;
        }
        state.onTurnEnd(this);
        System.out.println("   [TURN END] " + name + " finishes the turn with HP " + hp + "/" + maxHp + ".");
    }

    public boolean canActThisTurn() {
        return isAlive() && state.canAct();
    }

    public int attack(Monster monster) {
        if (!isAlive() || monster == null || !monster.isAlive()) {
            return 0;
        }

        int damage = Math.max(1, state.modifyOutgoingDamage(attackPower));
        monster.takeDamage(damage);
        System.out.println("   [ACTION] " + name + " attacks " + monster.getName()
                + " for " + damage + " damage. Monster HP: " + monster.getHp());
        return damage;
    }

    public int receiveAttack(int enemyPower, String sourceName) {
        int reducedByDefense = Math.max(1, enemyPower - defense);
        int finalDamage = Math.max(0, state.modifyIncomingDamage(reducedByDefense));
        takeDamage(finalDamage);
        System.out.println("   [HIT] " + name + " receives " + finalDamage + " damage from " + sourceName
                + ". HP: " + hp + "/" + maxHp);
        return finalDamage;
    }

    public int receiveHazardDamage(int rawDamage, String sourceName) {
        int finalDamage = Math.max(0, state.modifyIncomingDamage(Math.max(1, rawDamage)));
        takeDamage(finalDamage);
        System.out.println("   [HAZARD] " + name + " receives " + finalDamage + " damage from " + sourceName
                + ". HP: " + hp + "/" + maxHp);
        return finalDamage;
    }

    public int receivePureDamage(int amount, String reason) {
        int finalDamage = Math.max(0, amount);
        takeDamage(finalDamage);
        System.out.println("   [EFFECT] " + name + " suffers " + finalDamage + " damage from " + reason
                + ". HP: " + hp + "/" + maxHp);
        return finalDamage;
    }

    public int healWithLog(int amount, String reason) {
        int before = hp;
        heal(amount);
        int healed = hp - before;
        System.out.println("   [HEAL] " + name + " restores " + healed + " HP from " + reason
                + ". HP: " + hp + "/" + maxHp);
        return healed;
    }

}
