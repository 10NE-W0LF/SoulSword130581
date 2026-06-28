package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

/**
 * Tipologie di vampiro presenti nel gioco.
 * Ogni tipo definisce asset grafico e nome da mostrare al giocatore.
 */
public enum TipoVampiro {
    vampiroBase(1, "Vampiro"),
    vampiroElite(2, "EliteVampiro"),
    arciduca(3, "Arciduca Vampiro");

    private final int livelloAsset;
    private final String nomeVisualizzato;

    TipoVampiro(int livelloAsset, String nomeVisualizzato) {
        this.livelloAsset = livelloAsset;
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public int getLivelloAsset() {
        return livelloAsset;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }
}
