package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public abstract class Personaggio {

    private int livello;
    private int vita;

    protected Personaggio(int livello, int vita) {
        this.livello = Math.max(1, livello);
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

    public int getLivello() {
        return livello;
    }

    public int getVita() {
        return vita;
    }

    public abstract int getPotenzaAttacco();

    public abstract int getVitaMassima();
}
