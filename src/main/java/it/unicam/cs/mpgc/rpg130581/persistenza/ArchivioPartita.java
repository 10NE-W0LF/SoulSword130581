package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;

import java.io.IOException;

public interface ArchivioPartita {

    void salva(StatoPartita stato) throws IOException;

    StatoPartita carica() throws IOException;
}
