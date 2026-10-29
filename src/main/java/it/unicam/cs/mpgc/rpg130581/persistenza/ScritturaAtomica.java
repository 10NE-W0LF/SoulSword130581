package it.unicam.cs.mpgc.rpg130581.persistenza;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Sostituisce il file soltanto dopo aver completato la scrittura del temporaneo. */
final class ScritturaAtomica {
    private ScritturaAtomica() {
    }

    static void scrivi(Path percorso, String contenuto) throws IOException {
        Path destinazione = percorso.toAbsolutePath();
        Files.createDirectories(destinazione.getParent());
        Path temporaneo = Files.createTempFile(destinazione.getParent(), ".soulsword-", ".tmp");
        try {
            Files.writeString(temporaneo, contenuto, StandardCharsets.UTF_8);
            try {
                Files.move(temporaneo, destinazione, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaneo, destinazione, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaneo);
        }
    }
}
