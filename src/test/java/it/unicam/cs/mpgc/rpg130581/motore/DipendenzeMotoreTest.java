package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.partita.EventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.partita.TipoEventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;

import static org.junit.Assert.*;

public class DipendenzeMotoreTest {
    private final ArchivioPartita archivio = new ArchivioPartita() {
        public void salva(StatoPartita stato) { }
        public StatoPartita carica() { return new StatoPartita(); }
    };

    @Test
    public void stessoSemeGeneraGliStessiNemiciNelleStessePosizioni() {
        Clock orologio = Clock.fixed(Instant.ofEpochMilli(10000), ZoneOffset.UTC);
        MotoreGioco primo = new MotoreGioco(archivio, new Random(1234), orologio);
        MotoreGioco secondo = new MotoreGioco(archivio, new Random(1234), orologio);
        primo.avviaScenario(ScenarioGioco.margineForestaMorta);
        secondo.avviaScenario(ScenarioGioco.margineForestaMorta);
        for (int i = 0; i < primo.getVampiri().size(); i++) {
            VampiroNelMondo a = primo.getVampiri().get(i);
            VampiroNelMondo b = secondo.getVampiri().get(i);
            assertEquals(a.getVampiro().getLivello(), b.getVampiro().getLivello());
            assertEquals(a.getPosizione().getX(), b.getPosizione().getX(), 0);
            assertEquals(a.getPosizione().getY(), b.getPosizione().getY(), 0);
        }
    }

    @Test
    public void ricaricaDelDannoSiVerificaSenzaAtteseReali() {
        OrologioControllato orologio = new OrologioControllato();
        MotoreGioco motore = new MotoreGioco(archivio, new Random(1), orologio);
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        avvicina(motore, motore.getVampiri().getFirst());
        motore.aggiornaMondo();
        int vita = motore.getStato().getEroe().getVita();
        assertTrue(vita < 108);
        assertEquals(10000, motore.getUltimoDannoEroeMillis());
        orologio.tempo = 11149;
        motore.aggiornaMondo();
        assertEquals(vita, motore.getStato().getEroe().getVita());
        orologio.tempo = 11150;
        motore.aggiornaMondo();
        assertTrue(motore.getStato().getEroe().getVita() < vita);
        assertEquals(11150, motore.getUltimoDannoEroeMillis());
    }

    @Test
    public void generazioneECombattimentoSiPossonoSostituireTramiteContratti() {
        GenerazioneVampiri generazione = new GenerazioneVampiri() {
            public Vampiro creaVampiroRegolare(ScenarioGioco scenario) {
                return new Vampiro(TipoVampiro.vampiroBase, 4);
            }
            public Vampiro creaBoss() { return new Vampiro(TipoVampiro.arciduca, 20); }
        };
        int[] attacchi = {0};
        Combattimento combattimento = (eroe, vampiro) -> {
            attacchi[0]++;
            return new EsitoCombattimento(0, false, false, EventoGioco.di(TipoEventoGioco.colpoScambiato));
        };
        MotoreGioco motore = new MotoreGioco(archivio, generazione, combattimento, new Random(1), Clock.systemUTC());
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        assertEquals(4, motore.getVampiri().getFirst().getVampiro().getLivello());
        avvicina(motore, motore.getVampiri().getFirst());
        motore.attaccaNemicoVicino();
        assertEquals(1, attacchi[0]);
        assertEquals(TipoEventoGioco.colpoScambiato, motore.getUltimoEvento().tipo());
        assertEquals(108, motore.getStato().getEroe().getVita());
    }

    private void avvicina(MotoreGioco motore, VampiroNelMondo vampiro) {
        vampiro.getPosizione().muovi(motore.getPosizioneEroe().getX() - vampiro.getPosizione().getX(),
                motore.getPosizioneEroe().getY() - vampiro.getPosizione().getY(), 880, 540);
    }

    private static final class OrologioControllato extends Clock {
        private long tempo = 10000;
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zona) { return this; }
        @Override public Instant instant() { return Instant.ofEpochMilli(tempo); }
    }
}
