package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

public class EsitoCombattimento {

    private final int dannoEroe;
    private final int dannoVampiro;
    private final boolean vampiroSconfitto;
    private final boolean eroeSconfitto;
    private final String messaggio;

    public EsitoCombattimento(int dannoEroe, int dannoVampiro, boolean vampiroSconfitto, boolean eroeSconfitto, String messaggio) {
        this.dannoEroe = dannoEroe;
        this.dannoVampiro = dannoVampiro;
        this.vampiroSconfitto = vampiroSconfitto;
        this.eroeSconfitto = eroeSconfitto;
        this.messaggio = messaggio;
    }

    public int getDannoEroe() {
        return dannoEroe;
    }

    public int getDannoVampiro() {
        return dannoVampiro;
    }

    public boolean isVampiroSconfitto() {
        return vampiroSconfitto;
    }

    public boolean isEroeSconfitto() {
        return eroeSconfitto;
    }

    public String getMessaggio() {
        return messaggio;
    }
}
