package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

import it.unicam.cs.mpgc.rpg130581.modello.partita.EventoGioco;

/**
 * Risultato di uno scambio di combattimento.
 * Contiene danni, stato dei personaggi ed evento indipendente dalla presentazione.
 */
public class EsitoCombattimento {

    private final int dannoVampiro;
    private final boolean vampiroSconfitto;
    private final boolean eroeSconfitto;
    private final EventoGioco evento;

    /**
     * Crea l'esito di uno scambio di combattimento.
     *
     * @param dannoVampiro danno inflitto dal vampiro
     * @param vampiroSconfitto true se il vampiro e' stato sconfitto
     * @param eroeSconfitto true se l'eroe e' stato sconfitto
     * @param evento esito da presentare al giocatore
     */
    public EsitoCombattimento(int dannoVampiro, boolean vampiroSconfitto, boolean eroeSconfitto, EventoGioco evento) {
        this.dannoVampiro = dannoVampiro;
        this.vampiroSconfitto = vampiroSconfitto;
        this.eroeSconfitto = eroeSconfitto;
        this.evento = evento;
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

    public EventoGioco getEvento() {
        return evento;
    }
}
