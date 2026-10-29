package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;

import java.io.IOException;

/** Persiste la lingua dell'interfaccia indipendentemente dai progressi della partita. */
public interface ArchivioPreferenze {
    Lingua caricaLingua() throws IOException;

    void salvaLingua(Lingua lingua) throws IOException;
}
