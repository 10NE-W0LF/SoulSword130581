package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

/**
 * Classe base dei personaggi del gioco.
 * Raccoglie le informazioni comuni a eroe e vampiri, lasciando alle sottoclassi
 * il calcolo specifico di attacco e vita massima.
 */
public abstract class Personaggio {

    private int livello;
    private int vita;
    private int forza;
    private int salute;

    // Inizializza i valori base impedendo livelli, forza, salute o vita negativi.
    protected Personaggio(int livello, int vita, int forza, int salute) {
        this.livello = Math.max(1, livello);
        this.forza = Math.max(0, forza);
        this.salute = Math.max(0, salute);
        this.vita = Math.max(0, vita);
    }

    /**
     * Applica danno al personaggio senza permettere alla vita di scendere sotto zero.
     *
     * @param danno danno da subire
     */
    public void subisciDanno(int danno) {
        vita = Math.max(0, vita - Math.max(0, danno));
    }

    /**
     * Indica se il personaggio non ha piu' vita.
     *
     * @return true se la vita e' pari a zero
     */
    public boolean isSconfitto() {
        return vita <= 0;
    }

    protected void impostaLivello(int livello) {
        this.livello = Math.max(1, livello);
    }

    protected void impostaVita(int vita) {
        this.vita = Math.max(0, Math.min(vita, getVitaMassima()));
    }

    protected void curaAlMassimo() {
        vita = getVitaMassima();
    }

    protected void aggiungiForza(int quantita) {
        forza += Math.max(0, quantita);
    }

    protected void aggiungiSalute(int quantita) {
        salute += Math.max(0, quantita);
    }

    public int getLivello() {
        return livello;
    }

    public int getVita() {
        return vita;
    }

    public int getForza() {
        return forza;
    }

    public int getSalute() {
        return salute;
    }

    /**
     * Restituisce la potenza offensiva del personaggio.
     *
     * @return valore di attacco calcolato dalla sottoclasse
     */
    public abstract int getPotenzaAttacco();

    /**
     * Restituisce la vita massima del personaggio.
     *
     * @return vita massima calcolata dalla sottoclasse
     */
    public abstract int getVitaMassima();
}
