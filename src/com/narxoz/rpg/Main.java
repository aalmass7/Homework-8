package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.BattleFloor;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Hero maximus = new Hero("Maximus", 40, 10, 3);
        Hero lucius = new Hero("Lucius", 38, 9, 4);

        // Heroes start with different states.
        lucius.setState(new PoisonedState(2));

        List<Hero> party = Arrays.asList(maximus, lucius);

        List<TowerFloor> floors = Arrays.asList(
                new BattleFloor(
                        "Skeleton Hall",
                        Arrays.asList(
                                new Monster("Skeleton Guard", 18, 8),
                                new Monster("Cursed Bat Swarm", 10, 6)
                        )
                ),
                new TrapFloor("Venom Staircase", 5),
                new RestFloor("Silent Shrine", 8),
                new BossFloor("Phantom Throne", new Monster("Phantom Lord", 34, 11))
        );

        TowerRunner runner = new TowerRunner(floors);
        TowerRunResult result = runner.run(party);

        System.out.println("\n===== FINAL TOWER RESULT =====");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());

    }
}
