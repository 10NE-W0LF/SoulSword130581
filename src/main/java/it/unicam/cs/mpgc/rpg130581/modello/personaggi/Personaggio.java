package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public abstract class Personaggio {

    private int livello;
    private int vita;
    private int forza;
    private int salute;

    protected Personaggio(int livello, int vita) {
        this(livello, vita, 0, 0);
    }

    protected Personaggio(int livello, int vita, int forza, int salute) {
        this.livello = Math.max(1, livello);
        this.forza = Math.max(0, forza);
        this.salute = Math.max(0, salute);
        this.vita = Math.max(0, vita);
    }

    public void subisciDanno(int danno) {
        vita = Math.max(0, vita - Math.max(0, danno));
    }

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

    public abstract int getPotenzaAttacco();

    public abstract int getVitaMassima();
}
