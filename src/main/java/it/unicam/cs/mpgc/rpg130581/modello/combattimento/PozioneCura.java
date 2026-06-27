package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

import java.util.Random;

public final class PozioneCura {

    public static final int maxPozioni = 3;
    public static final int valoreCura = 36;
    public static final int dropPercentuale = 14;

    private PozioneCura() {
    }

    public static boolean esceDalDrop(Random random) {
        return random.nextInt(100) < dropPercentuale;
    }
}
