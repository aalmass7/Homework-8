package com.narxoz.rpg.util;

import com.narxoz.rpg.combatant.Hero;
import java.util.List;

public final class PartyUtils {

    private PartyUtils() {
    }

    public static boolean anyAlive(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public static int countAlive(List<Hero> party) {
        int count = 0;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }

    public static Hero firstAlive(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }

    public static String statusLine(List<Hero> party) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < party.size(); i++) {
            Hero hero = party.get(i);
            if (i > 0) {
                sb.append(" | ");
            }
            sb.append(hero.getName())
                    .append(": HP ")
                    .append(hero.getHp())
                    .append("/")
                    .append(hero.getMaxHp())
                    .append(", state ")
                    .append(hero.getStateName());
        }
        return sb.toString();
    }
}