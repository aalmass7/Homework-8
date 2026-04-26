package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.util.PartyUtils;
import java.util.ArrayList;
import java.util.List;


public class TowerRunner {
    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = new ArrayList<>(floors);
    }

    public TowerRunResult run(List<Hero> party) {
        int floorsCleared = 0;

        System.out.println("===== TOWER RUN START =====");
        for (int i = 0; i < floors.size(); i++) {
            if (!PartyUtils.anyAlive(party)) {
                System.out.println("All heroes have fallen. The run ends early.");
                break;
            }

            System.out.println("\n===== FLOOR " + (i + 1) + " / " + floors.size() + " =====");
            System.out.println("[PARTY BEFORE] " + PartyUtils.statusLine(party));

            FloorResult result = floors.get(i).explore(party);

            System.out.println("[RESULT] " + result.getSummary());
            System.out.println("[PARTY AFTER] " + PartyUtils.statusLine(party));
            System.out.println("[TOTAL FLOOR DAMAGE] " + result.getDamageTaken());

            if (!result.isCleared()) {
                System.out.println("The floor was not cleared. The tower run stops here.");
                break;
            }

            floorsCleared++;
        }

        int heroesSurviving = PartyUtils.countAlive(party);
        boolean reachedTop = floorsCleared == floors.size() && heroesSurviving > 0;
        return new TowerRunResult(floorsCleared, heroesSurviving, reachedTop);
    }
}
