package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public class Vampiro extends Personaggio {

    private final TipoVampiro tipo;

    public Vampiro(TipoVampiro tipo, int livello) {
        super(livello, calcolaVitaMassima(tipo, livello));
        this.tipo = tipo;
    }

    private static int calcolaVitaMassima(TipoVampiro tipo, int livello) {
        int bonusElite = tipo == TipoVampiro.VAMPIRO_ELITE ? 35 : 0;
        int bonusBoss = tipo == TipoVampiro.ARCIDUCA ? 120 : 0;
        return 34 + livello * 10 + bonusElite + bonusBoss;
    }

    @Override
    public int getPotenzaAttacco() {
        int bonusElite = tipo == TipoVampiro.VAMPIRO_ELITE ? 7 : 0;
        int bonusBoss = tipo == TipoVampiro.ARCIDUCA ? 15 : 0;
        return 5 + getLivello() * 3 + bonusElite + bonusBoss;
    }

    public int getRicompensaEsperienza() {
        int bonusElite = tipo == TipoVampiro.VAMPIRO_ELITE ? 35 : 0;
        int bonusBoss = tipo == TipoVampiro.ARCIDUCA ? 500 : 0;
        return 22 + getLivello() * 13 + bonusElite + bonusBoss;
    }

    public TipoVampiro getTipo() {
        return tipo;
    }

    @Override
    public int getVitaMassima() {
        return calcolaVitaMassima(tipo, getLivello());
    }
}
