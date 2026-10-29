package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Archivio locale delle preferenze. I salvataggi JSON esistenti restano compatibili. */
public final class SalvataggiPreferenze implements ArchivioPreferenze {
    private final Path percorso;

    public SalvataggiPreferenze(Path percorso) {
        this.percorso = percorso;
    }

    @Override
    public Lingua caricaLingua() throws IOException {
        if (Files.notExists(percorso)) {
            return Lingua.italiano;
        }
        Properties preferenze = new Properties();
        try (Reader lettore = Files.newBufferedReader(percorso, StandardCharsets.UTF_8)) {
            preferenze.load(lettore);
        } catch (IllegalArgumentException exception) {
            // Un file malformato non deve impedire l'avvio del gioco.
            return Lingua.italiano;
        }
        return Lingua.daCodice(preferenze.getProperty("lingua"));
    }

    @Override
    public void salvaLingua(Lingua lingua) throws IOException {
        if (percorso.getParent() != null) {
            Files.createDirectories(percorso.getParent());
        }
        Files.writeString(percorso, "lingua=" + lingua.getCodice() + "\n", StandardCharsets.UTF_8);
    }
}
