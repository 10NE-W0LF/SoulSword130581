package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Archivio su file: la conversione e la validazione JSON sono delegate al codec. */
public final class Salvataggi implements ArchivioPartita {
    private final Path percorso;
    private final CodecPartitaJson codec = new CodecPartitaJson();

    public Salvataggi(Path percorso) {
        this.percorso = Objects.requireNonNull(percorso);
    }

    @Override
    public void salva(StatoPartita stato) throws IOException {
        ScritturaAtomica.scrivi(percorso, codec.codifica(stato));
    }

    @Override
    public StatoPartita carica() throws IOException {
        if (Files.notExists(percorso)) {
            return new StatoPartita();
        }
        return codec.decodifica(Files.readString(percorso, StandardCharsets.UTF_8));
    }
}
