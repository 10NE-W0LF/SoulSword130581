package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

import java.util.Random;

/**
 * Definisce le regole della Pozione Cura.
 * La classe non viene istanziata: contiene solo limite inventario, cura e probabilita' di drop.
 */
public final class PozioneCura {

    public static final int maxPozioni = 3;
    public static final int valoreCura = 36;
    public static final int dropPercentuale = 14;

    private PozioneCura() {
    }

    /**
     * Verifica se una pozione viene rilasciata da un vampiro sconfitto.
     *
     * @param random generatore casuale usato dal motore
     * @return true se il drop della pozione riesce
     */
    public static boolean esceDalDrop(Random random) {
        return random.nextInt(100) < dropPercentuale;
    }
}
