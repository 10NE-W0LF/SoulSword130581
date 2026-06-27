package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public enum EvoluzioneSoulSword {
    RISVEGLIATA(1, "SoulSword: Risvegliata"),
    LAMA_DI_SANGUE(10, "SoulSword: Lama di Sangue"),
    MIETITRICE_DELL_ALBA(20, "SoulSword: Mietitrice dell'Alba");

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
        if (livello >= MIETITRICE_DELL_ALBA.requiredLevel) {
            return MIETITRICE_DELL_ALBA;
        }
        if (livello >= LAMA_DI_SANGUE.requiredLevel) {
            return LAMA_DI_SANGUE;
        }
        return RISVEGLIATA;
    }
}
