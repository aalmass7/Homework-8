package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.StunnedState;
import com.narxoz.rpg.util.PartyUtils;

import java.util.List;

public class BossFloor extends TowerFloor{

    private final String floorName;
    private final Monster boss;

    public BossFloor(String floorName, Monster boss) {
        this.floorName = floorName;
        this.boss = boss;
    }

    @Override
    protected void announce() {
        System.out.println("\n[ANNOUNCE] The air freezes. The boss of " + floorName + " is waiting.");
    }
    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] Boss appears: " + boss.getName() + " (HP " + boss.getHp()
                + ", ATK " + boss.getAttackPower() + ")");
        System.out.println("   Party: " + PartyUtils.statusLine(party));
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] Boss battle starts.");
        int totalDamageTaken = 0;
        int round = 1;

        while (boss.isAlive() && PartyUtils.anyAlive(party)) {
            System.out.println("\n   [ROUND] " + round);

            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }

                hero.startTurn();
                if (hero.isAlive()) {
                    if (hero.canActThisTurn()) {
                        hero.attack(boss);
                    } else {
                        System.out.println("   [SKIP] " + hero.getName() + " misses the turn because of the current state.");
                    }
                }
                hero.endTurn();
            }

            if (!boss.isAlive() || !PartyUtils.anyAlive(party)) {
                break;
            }

            Hero target = PartyUtils.firstAlive(party);
            totalDamageTaken += target.receiveAttack(boss.getAttackPower(), boss.getName());

            if (round == 1 && target != null && target.isAlive()) {
                System.out.println("   [BOSS SKILL] " + boss.getName() + " uses Fear Roar on " + target.getName() + ".");
                target.setState(new StunnedState(1));
            }

            round++;
        }

        boolean cleared = PartyUtils.anyAlive(party) && !boss.isAlive();
        String summary = cleared
                ? floorName + " falls. The tower is conquered."
                : floorName + " remains undefeated.";
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] The boss drops a large healing crystal.");
        if (!result.isCleared()) {
            System.out.println("   The crystal remains with the boss.");
            return;
        }

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.healWithLog(6, "boss crystal");
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Boss floor cleanup is complete.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}
