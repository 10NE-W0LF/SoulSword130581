package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

/**
 * Risultato di uno scambio di combattimento.
 * Contiene danni, stato dei personaggi e messaggio da mostrare nell'interfaccia.
 */
public class EsitoCombattimento {

    private final int dannoVampiro;
    private final boolean vampiroSconfitto;
    private final boolean eroeSconfitto;
    private final String messaggio;

    /**
     * Crea l'esito di uno scambio di combattimento.
     *
     * @param dannoVampiro danno inflitto dal vampiro
     * @param vampiroSconfitto true se il vampiro e' stato sconfitto
     * @param eroeSconfitto true se l'eroe e' stato sconfitto
     * @param messaggio messaggio da mostrare al giocatore
     */
    public EsitoCombattimento(int dannoVampiro, boolean vampiroSconfitto, boolean eroeSconfitto, String messaggio) {
        this.dannoVampiro = dannoVampiro;
        this.vampiroSconfitto = vampiroSconfitto;
        this.eroeSconfitto = eroeSconfitto;
        this.messaggio = messaggio;
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
