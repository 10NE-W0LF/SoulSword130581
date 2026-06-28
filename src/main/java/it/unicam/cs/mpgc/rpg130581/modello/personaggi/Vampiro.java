package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

/**
 * Rappresenta un nemico del gioco.
 * Le statistiche vengono calcolate in base al tipo di vampiro e al livello generato.
 */
public class Vampiro extends Personaggio {

    private final TipoVampiro tipo;

    /**
     * Crea un vampiro di un certo tipo e livello.
     *
     * @param tipo tipo del vampiro
     * @param livello livello del vampiro
     */
    public Vampiro(TipoVampiro tipo, int livello) {
        super(livello, calcolaVitaMassima(tipo, livello), calcolaForza(tipo, livello), calcolaSalute(tipo, livello));
        this.tipo = tipo;
    }

    // Calcola una vita piu' alta per elite e boss, mantenendo il vampiro base piu' leggero.
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

    /**
     * Calcola il danno del vampiro.
     *
     * @return potenza d'attacco del vampiro
     */
    @Override
    public int getPotenzaAttacco() {
        return 6 + getForza() * 4;
    }

    /**
     * Calcola l'esperienza ottenuta dall'eroe quando il vampiro viene sconfitto.
     *
     * @return esperienza assegnata alla sconfitta del vampiro
     */
    public int getRicompensaEsperienza() {
        int bonusElite = tipo == TipoVampiro.vampiroElite ? 35 : 0;
        int bonusBoss = tipo == TipoVampiro.arciduca ? 500 : 0;
        return 22 + getLivello() * 13 + bonusElite + bonusBoss;
    }

    /**
     * Restituisce il tipo del vampiro.
     *
     * @return tipo del vampiro
     */
    public TipoVampiro getTipo() {
        return tipo;
    }

    /**
     * Calcola la vita massima del vampiro.
     *
     * @return vita massima del vampiro
     */
    @Override
    public int getVitaMassima() {
        return calcolaVitaMassima(tipo, getLivello());
    }
}
