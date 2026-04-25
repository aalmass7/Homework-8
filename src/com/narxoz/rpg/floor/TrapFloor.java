package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import com.narxoz.rpg.util.PartyUtils;

import java.util.List;

public class TrapFloor extends TowerFloor{

    private final String floorName;
    private final int trapDamage;

    public TrapFloor(String floorName, int trapDamage) {
        this.floorName = floorName;
        this.trapDamage = trapDamage;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("[SETUP] Thin wires and hidden needles cover the corridor.");
        System.out.println("   Party: " + PartyUtils.statusLine(party));
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("[CHALLENGE] The heroes try to cross the trapped corridor.");
        int totalDamageTaken = 0;

        for (int i = 0; i < party.size(); i++) {
            Hero hero = party.get(i);
            if (!hero.isAlive()) {
                continue;
            }

            totalDamageTaken += hero.receiveHazardDamage(trapDamage, "the trap floor");
            if (!hero.isAlive()) {
                continue;
            }

            if (i == 0) {
                System.out.println("   [EVENT] Poison darts hit " + hero.getName() + ".");
                hero.setState(new PoisonedState(2));
            } else {
                System.out.println("   [EVENT] A shock rune stuns " + hero.getName() + ".");
                hero.setState(new StunnedState(1));
            }
        }

        boolean cleared = PartyUtils.anyAlive(party);
        String summary = cleared
                ? floorName + " is crossed with difficulty."
                : floorName + " wipes the party.";
        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("[LOOT] The heroes collect antivenom herbs.");
        if (!result.isCleared()) {
            System.out.println("   No one is left to collect them.");
            return;
        }

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.healWithLog(2, "minor herbs");
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("[CLEANUP] Trap floor cleanup is complete.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}
