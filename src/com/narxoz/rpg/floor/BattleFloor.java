package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Monster;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.util.PartyUtils;
import java.util.List;

public class BattleFloor extends TowerFloor{

    private final String floorName;
    private final List<Monster> monsters;

    public BattleFloor(String floorName, List<Monster> monsters){
        this.floorName = floorName;
        this.monsters = monsters;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] Monsters gather on the floor.");
        for (Monster monster : monsters) {
            System.out.println("   - " + monster.getName() + " (HP " + monster.getHp()
                    + ", ATK " + monster.getAttackPower() + ")");
        }
        System.out.println("   Party: " + PartyUtils.statusLine(party));
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] Combat starts.");
        int totalDamageTaken = 0;

        for (Monster monster : monsters) {
            System.out.println("\n   [ENEMY] " + monster.getName() + " enters the fight.");
            while (monster.isAlive() && PartyUtils.anyAlive(party)) {
                for (Hero hero : party) {
                    if (!hero.isAlive() || !monster.isAlive()) {
                        continue;
                    }

                    hero.startTurn();
                    if (hero.isAlive()) {
                        if (hero.canActThisTurn()) {
                            hero.attack(monster);
                        } else {
                            System.out.println("   [SKIP] " + hero.getName() + " loses the action.");
                        }
                    }
                    hero.endTurn();
                }

                if (!monster.isAlive() || !PartyUtils.anyAlive(party)) {
                    break;
                }

                Hero target = PartyUtils.firstAlive(party);
                totalDamageTaken += target.receiveAttack(monster.getAttackPower(), monster.getName());
            }
        }

        boolean cleared = PartyUtils.anyAlive(party) && allMonstersDefeated();
        String summary = cleared
                ? floorName + " is cleared. The party survives the battle."
                : floorName + " defeats the party.";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] The party finds small healing potions.");
        if (!result.isCleared()) {
            System.out.println("   No loot is given because the floor was not cleared.");
            return;
        }

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.healWithLog(4, "combat loot");
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Battle floor cleanup is complete.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private boolean allMonstersDefeated() {
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                return false;
            }
        }
        return true;
    }
}
