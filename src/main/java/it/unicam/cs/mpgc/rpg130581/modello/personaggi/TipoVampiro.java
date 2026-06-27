package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import java.util.Random;

public enum TipoVampiro {
    vampiroBase(1, 1, 6, "Vampiro"),
    vampiroElite(2, 7, 15, "ÉliteVampiro"),
    arciduca(3, 20, 20, "Arciduca Vampiro");

    private final int livelloAsset;
    private final int livelloMinimo;
    private final int livelloMassimo;
    private final String nomeVisualizzato;

    TipoVampiro(int livelloAsset, int livelloMinimo, int livelloMassimo, String nomeVisualizzato) {
        this.livelloAsset = livelloAsset;
        this.livelloMinimo = livelloMinimo;
        this.livelloMassimo = livelloMassimo;
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public int livelloCasuale(Random random) {
        return livelloMinimo + random.nextInt(livelloMassimo - livelloMinimo + 1);
    }

    public int getLivelloAsset() {
        return livelloAsset;
    }

    public int getLivelloMinimo() {
        return livelloMinimo;
    }

    public int getLivelloMassimo() {
        return livelloMassimo;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }
}
