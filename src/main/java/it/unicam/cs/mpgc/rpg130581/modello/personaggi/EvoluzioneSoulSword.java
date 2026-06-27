package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public enum EvoluzioneSoulSword {
    risvegliata(1, "SoulSword: Risvegliata"),
    lamaDiSangue(10, "SoulSword: Lama di Sangue"),
    mietitriceDellAlba(20, "SoulSword: Mietitrice dell'Alba");

    private final int requiredLevel;
    private final String nomeVisualizzato;

    EvoluzioneSoulSword(int requiredLevel, String nomeVisualizzato) {
        this.requiredLevel = requiredLevel;
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

    public static EvoluzioneSoulSword daLivello(int livello) {
        if (livello >= mietitriceDellAlba.requiredLevel) {
            return mietitriceDellAlba;
        }
        if (livello >= lamaDiSangue.requiredLevel) {
            return lamaDiSangue;
        }
        return risvegliata;
    }
}
