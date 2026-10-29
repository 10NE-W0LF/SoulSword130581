package it.unicam.cs.mpgc.rpg130581.modello.impostazioni;

/** Lingue supportate; il codice stabile viene usato nelle preferenze. */
public enum Lingua {
    italiano("it", "Italiano"),
    inglese("en", "English");

    private final String codice;
    private final String nome;

    Lingua(String codice, String nome) {
        this.codice = codice;
        this.nome = nome;
    }

    public String getCodice() {
        return codice;
    }

    public static Lingua daCodice(String codice) {
        for (Lingua lingua : values()) {
            if (lingua.codice.equals(codice)) {
                return lingua;
            }
        }
        return italiano;
    }

    @Override
    public String toString() {
        return nome;
    }
}
