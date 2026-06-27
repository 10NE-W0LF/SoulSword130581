package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public class Vampiro extends Personaggio {

    private final TipoVampiro tipo;

    public Vampiro(TipoVampiro tipo, int livello) {
        super(livello, calcolaVitaMassima(tipo, livello), calcolaForza(tipo, livello), calcolaSalute(tipo, livello));
        this.tipo = tipo;
    }

    private static int calcolaVitaMassima(TipoVampiro tipo, int livello) {
        int bonusElite = tipo == TipoVampiro.vampiroElite ? 18 : 0;
        int bonusBoss = tipo == TipoVampiro.arciduca ? 160 : 0;
        return 42 + calcolaSalute(tipo, livello) * 12 + bonusElite + bonusBoss;
    }

    private static int calcolaForza(TipoVampiro tipo, int livello) {
        int bonusElite = tipo == TipoVampiro.vampiroElite ? 4 : 0;
        int bonusBoss = tipo == TipoVampiro.arciduca ? 9 : 0;
        return livello + bonusElite + bonusBoss;
    }

    private static int calcolaSalute(TipoVampiro tipo, int livello) {
        int bonusElite = tipo == TipoVampiro.vampiroElite ? 5 : 0;
        int bonusBoss = tipo == TipoVampiro.arciduca ? 18 : 0;
        return livello + bonusElite + bonusBoss;
    }

    @Override
    public int getPotenzaAttacco() {
        return 6 + getForza() * 4;
    }

    public int getRicompensaEsperienza() {
        int bonusElite = tipo == TipoVampiro.vampiroElite ? 35 : 0;
        int bonusBoss = tipo == TipoVampiro.arciduca ? 500 : 0;
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
