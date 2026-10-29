package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class SalvataggiTest {
    @Rule public TemporaryFolder cartella = new TemporaryFolder();

    private Path percorso() {
        return cartella.getRoot().toPath().resolve("data/save.json");
    }

    private StatoPartita caricaJson(String json) throws IOException {
        Path file = cartella.newFile().toPath();
        Files.writeString(file, json);
        return new Salvataggi(file).carica();
    }

    @Test
    public void fileAssenteRestituisceUnaNuovaPartitaSenzaCreareFile() throws IOException {
        StatoPartita stato = new Salvataggi(percorso()).carica();
        assertEquals(1, stato.getEroe().getLivello());
        assertEquals(1, stato.getNumeroScenario());
        assertFalse(stato.isBossSconfitto());
        assertFalse(Files.exists(percorso()));
    }

    @Test
    public void salvataggioECaricamentoConservanoTuttiIProgressi() throws IOException {
        Eroe eroe = new Eroe(12, 77, 31, 123, 3, 2, 6, 2);
        StatoPartita origine = new StatoPartita(eroe, true, 5, 7);
        new Salvataggi(percorso()).salva(origine);
        StatoPartita letto = new Salvataggi(percorso()).carica();
        Eroe copia = letto.getEroe();
        assertNotSame(origine, letto);
        assertEquals(12, copia.getLivello());
        assertEquals(77, copia.getEsperienza());
        assertEquals(31, copia.getVSconfitti());
        assertEquals(123, copia.getVita());
        assertEquals(3, copia.getForza());
        assertEquals(2, copia.getSalute());
        assertEquals(6, copia.getStats());
        assertEquals(2, copia.getPozioniCura());
        assertTrue(letto.isBossSconfitto());
        assertEquals(5, letto.getNumeroScenario());
        assertEquals(7, letto.getSconfittiNelloScenario());
    }

    @Test
    public void nuovoSalvataggioSostituisceIPrecedentiProgressi() throws IOException {
        Salvataggi archivio = new Salvataggi(percorso());
        archivio.salva(new StatoPartita(new Eroe(20, 0, 40, 100, 0, 0, 19, 3), true, 5, 8));
        archivio.salva(new StatoPartita());
        StatoPartita letto = archivio.carica();
        assertEquals(1, letto.getEroe().getLivello());
        assertEquals(0, letto.getEroe().getPozioniCura());
        assertEquals(1, letto.getNumeroScenario());
        assertFalse(letto.isBossSconfitto());
    }

    @Test
    public void vecchiSalvataggiRiconosconoPuntiStatistiche() throws IOException {
        Eroe eroe = caricaJson("{\"livello\":5,\"forza\":1,\"salute\":1,\"puntiStatistiche\":1}").getEroe();
        assertEquals(1, eroe.getStats());
        assertEquals(0, eroe.getPozioniCura());
    }

    @Test
    public void chiaveStatsHaPrecedenzaSullaVecchiaChiave() throws IOException {
        Eroe eroe = caricaJson("{\"livello\":5,\"stats\":2,\"puntiStatistiche\":1}").getEroe();
        assertEquals(2, eroe.getStats());
    }

    @Test
    public void campiAssentiUsanoDefaultCompatibiliConIVecchiSalvataggi() throws IOException {
        StatoPartita stato = caricaJson("{\"livello\":5,\"forza\":1,\"salute\":1}");
        assertEquals(2, stato.getEroe().getStats());
        assertEquals(0, stato.getEroe().getPozioniCura());
        assertEquals(1, stato.getNumeroScenario());
        assertEquals(0, stato.getSconfittiNelloScenario());
        assertFalse(stato.isBossSconfitto());
    }

    @Test
    public void valoriCaricatiSonoNormalizzatiDalModello() throws IOException {
        StatoPartita stato = caricaJson("{\"livello\":99,\"vita\":9999,\"stats\":99,"
                + "\"pozioniCura\":99,\"numeroScenario\":99}");
        assertEquals(30, stato.getEroe().getLivello());
        assertEquals(stato.getEroe().getVitaMassima(), stato.getEroe().getVita());
        assertEquals(29, stato.getEroe().getStats());
        assertEquals(3, stato.getEroe().getPozioniCura());
        assertEquals(5, stato.getNumeroScenario());
    }

    @Test
    public void erroriDiLetturaEScritturaSonoSegnalatiAlChiamante() throws IOException {
        Path directory = cartella.newFolder().toPath();
        Salvataggi archivio = new Salvataggi(directory);
        assertThrows(IOException.class, archivio::carica);
        assertThrows(IOException.class, () -> archivio.salva(new StatoPartita()));
    }
}
