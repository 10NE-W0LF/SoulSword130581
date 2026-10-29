package it.unicam.cs.mpgc.rpg130581.applicazione;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPreferenze;

import java.io.IOException;
import java.util.Objects;

/** Coordina la preferenza della sessione senza dipendere da JavaFX o dal formato del file. */
public final class PreferenzeGioco {
    private final ArchivioPreferenze archivio;
    private Lingua lingua = Lingua.italiano;
    private boolean erroreLettura;

    public PreferenzeGioco(ArchivioPreferenze archivio) {
        this.archivio = Objects.requireNonNull(archivio);
    }

    public void carica() {
        try {
            lingua = Objects.requireNonNull(archivio.caricaLingua());
            erroreLettura = false;
        } catch (IOException exception) {
            lingua = Lingua.italiano;
            erroreLettura = true;
        }
    }

    public boolean salva(Lingua scelta) {
        Objects.requireNonNull(scelta);
        try {
            archivio.salvaLingua(scelta);
            lingua = scelta;
            erroreLettura = false;
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public Lingua getLingua() {
        return lingua;
    }

    public boolean isErroreLettura() {
        return erroreLettura;
    }
}
