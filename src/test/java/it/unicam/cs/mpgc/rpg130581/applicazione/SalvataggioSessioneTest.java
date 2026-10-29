package it.unicam.cs.mpgc.rpg130581.applicazione;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPreferenze;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SalvataggioSessioneTest {
    @Test
    public void esitoDistingueSuccessoFallimentoTotaleEParziale() {
        for (boolean errorePartita : new boolean[]{false, true}) {
            for (boolean errorePreferenze : new boolean[]{false, true}) {
                int[] scritturePreferenze = {0};
                ArchivioPartita partite = new ArchivioPartita() {
                    public void salva(StatoPartita stato) throws IOException {
                        if (errorePartita) throw new IOException("test");
                    }
                    public StatoPartita carica() { return new StatoPartita(); }
                };
                ArchivioPreferenze lingue = new ArchivioPreferenze() {
                    public Lingua caricaLingua() { return Lingua.inglese; }
                    public void salvaLingua(Lingua lingua) throws IOException {
                        scritturePreferenze[0]++;
                        assertEquals(Lingua.inglese, lingua);
                        if (errorePreferenze) throw new IOException("test");
                    }
                };
                PreferenzeGioco preferenze = new PreferenzeGioco(lingue);
                preferenze.carica();
                EsitoSalvataggio esito = new SalvataggioSessione(new MotoreGioco(partite), preferenze).salva();
                assertEquals(errorePartita ? EsitoSalvataggio.partitaFallita
                        : errorePreferenze ? EsitoSalvataggio.preferenzeFallite : EsitoSalvataggio.riuscito, esito);
                assertEquals(errorePartita ? 0 : 1, scritturePreferenze[0]);
            }
        }
    }

    @Test
    public void errorePreferenzeNonApplicaLaSceltaENonImpedisceIlRipristinoItaliano() {
        PreferenzeGioco preferenze = new PreferenzeGioco(new ArchivioPreferenze() {
            public Lingua caricaLingua() throws IOException { throw new IOException("test"); }
            public void salvaLingua(Lingua lingua) throws IOException { throw new IOException("test"); }
        });
        preferenze.carica();
        assertTrue(preferenze.isErroreLettura());
        assertEquals(Lingua.italiano, preferenze.getLingua());
        assertFalse(preferenze.salva(Lingua.inglese));
        assertEquals(Lingua.italiano, preferenze.getLingua());
    }
}
