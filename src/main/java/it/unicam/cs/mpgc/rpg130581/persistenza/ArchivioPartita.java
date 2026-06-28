package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;

import java.io.IOException;

/**
 * Astrazione del sistema di persistenza.
 * Permette al motore di gioco di salvare e caricare senza conoscere il formato concreto dei dati.
 */
public interface ArchivioPartita {

    /**
     * Salva lo stato della partita.
     *
     * @param stato stato da salvare
     * @throws IOException se il salvataggio fallisce
     */
    void salva(StatoPartita stato) throws IOException;

    /**
     * Carica lo stato della partita.
     *
     * @return stato caricato o nuova partita se non esiste un salvataggio
     * @throws IOException se il caricamento fallisce
     */
    StatoPartita carica() throws IOException;
}
