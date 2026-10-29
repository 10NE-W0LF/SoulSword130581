package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class SalvataggiPreferenzeTest {
    @Rule public TemporaryFolder cartella = new TemporaryFolder();

    @Test
    public void creaLeCartelleMancantiERiscriveLaLinguaPrecedente() throws IOException {
        Path percorso = cartella.getRoot().toPath().resolve("data/impostazioni/preferenze.properties");
        SalvataggiPreferenze archivio = new SalvataggiPreferenze(percorso);
        archivio.salvaLingua(Lingua.inglese);
        assertEquals(Lingua.inglese, new SalvataggiPreferenze(percorso).caricaLingua());
        archivio.salvaLingua(Lingua.italiano);
        assertEquals(Lingua.italiano, new SalvataggiPreferenze(percorso).caricaLingua());
        assertEquals("lingua=it\n", Files.readString(percorso));
    }

    @Test
    public void fileAssenteNonVieneCreatoDuranteLaLettura() throws IOException {
        Path percorso = cartella.getRoot().toPath().resolve("preferenze.properties");
        assertEquals(Lingua.italiano, new SalvataggiPreferenze(percorso).caricaLingua());
        assertFalse(Files.exists(percorso));
    }

    @Test
    public void erroreDiAccessoNonVieneScambiatoPerPreferenzaAssente() {
        SalvataggiPreferenze archivio = new SalvataggiPreferenze(cartella.getRoot().toPath());
        assertThrows(IOException.class, archivio::caricaLingua);
        assertThrows(IOException.class, () -> archivio.salvaLingua(Lingua.inglese));
    }
}
