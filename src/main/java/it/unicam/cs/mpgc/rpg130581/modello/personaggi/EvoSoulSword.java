package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

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

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

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
