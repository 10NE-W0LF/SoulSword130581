package it.unicam.cs.mpgc.rpg130581.interfaccia;

public enum TipoAnimazione {
    idle("idle"),
    walk("walk"),
    run("run"),
    attack("attack"),
    walkAttack("walk_attack"),
    runAttack("run_attack"),
    hurt("hurt"),
    death("death");

    private final String nomeFile;

    TipoAnimazione(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getNomeFile() {
        return nomeFile;
    }
}
