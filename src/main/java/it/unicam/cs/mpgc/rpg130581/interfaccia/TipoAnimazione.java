package it.unicam.cs.mpgc.rpg130581.interfaccia;

public enum TipoAnimazione {
    IDLE("idle"),
    WALK("walk"),
    RUN("run"),
    ATTACK("attack"),
    WALK_ATTACK("walk_attack"),
    RUN_ATTACK("run_attack"),
    HURT("hurt"),
    DEATH("death");

    private final String nomeFile;

    TipoAnimazione(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getNomeFile() {
        return nomeFile;
    }
}
