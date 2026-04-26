package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class RestFloor extends TowerFloor{

    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = healAmount;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] A quiet shrine glows with blue light.");
        System.out.println("   Party: " + com.narxoz.rpg.util.PartyUtils.statusLine(party));
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] There is no fight here. The heroes catch their breath.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.healWithLog(healAmount, "the shrine");
            }
        }
        return new FloorResult(true, 0, floorName + " gives the party a safe rest.");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] This message should not appear because loot is disabled here.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] The shrine fades as the heroes leave in peace.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}
