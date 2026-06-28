package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

/**
 * Evoluzioni della SoulSword.
 * Ogni valore indica il livello minimo richiesto e il nome mostrato nell'interfaccia.
 */
public enum EvoSoulSword {
    risvegliata(1, "SoulSword: Risvegliata"),
    lamaDiSangue(10, "SoulSword: Lama di Sangue"),
    mietitriceDellAlba(20, "SoulSword: Mietitrice dell'Alba");

    private final int requiredLevel;
    private final String nomeVisualizzato;

    EvoSoulSword(int requiredLevel, String nomeVisualizzato) {
        this.requiredLevel = requiredLevel;
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

    /**
     * Restituisce l'evoluzione corretta in base al livello dell'eroe.
     *
     * @param livello livello attuale dell'eroe
     * @return evoluzione della SoulSword corrispondente
     */
    public static EvoSoulSword daLivello(int livello) {
        if (livello >= mietitriceDellAlba.requiredLevel) {
            return mietitriceDellAlba;
        }
        if (livello >= lamaDiSangue.requiredLevel) {
            return lamaDiSangue;
        }
        return risvegliata;
    }
}
