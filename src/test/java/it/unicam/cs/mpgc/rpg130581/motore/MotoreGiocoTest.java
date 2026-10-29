package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MotoreGiocoTest {
    private ArchivioFinto archivio;
    private MotoreGioco motore;

    @Before
    public void preparaMotore() {
        archivio = new ArchivioFinto();
        motore = new MotoreGioco(archivio);
    }

    @Test
    public void scenarioBloccatoNonAvviaLaPartitaNeGeneraNemici() {
        StatoPartita prima = motore.getStato();
        assertFalse(motore.avviaScenario(ScenarioGioco.castello));
        assertSame(prima, motore.getStato());
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
    }

    @Test
    public void avvioCuraLEroeEGeneraIlLimiteInizialeDiNemici() {
        motore.getStato().getEroe().subisciDanno(70);
        assertTrue(motore.avviaScenario(ScenarioGioco.margineForestaMorta));
        assertEquals(FaseGioco.inGioco, motore.getFase());
        assertEquals(108, motore.getStato().getEroe().getVita());
        assertEquals(3, motore.getVampiri().size());
        for (VampiroNelMondo vampiro : motore.getVampiri()) {
            assertEquals(TipoVampiro.vampiroBase, vampiro.getVampiro().getTipo());
            assertTrue(vampiro.getPosizione().getX() >= 0);
            assertTrue(vampiro.getPosizione().getX() <= MotoreGioco.larghezzaMondo);
            assertTrue(vampiro.getPosizione().getY() >= 0);
            assertTrue(vampiro.getPosizione().getY() <= MotoreGioco.altezzaMondo);
        }
    }

    @Test
    public void movimentoEAggiornamentoSonoInattiviFuoriDallaPartita() {
        Posizione posizione = motore.getPosizioneEroe();
        double x = posizione.getX();
        double y = posizione.getY();
        motore.muoviEroe(100, 100);
        motore.aggiornaMondo();
        motore.attaccaNemicoVicino();
        assertEquals(x, posizione.getX(), 0);
        assertEquals(y, posizione.getY(), 0);
        assertTrue(motore.getVampiri().isEmpty());
        assertEquals(108, motore.getStato().getEroe().getVita());
    }

    @Test
    public void eroeNonPuoUscireDalMondoDuranteLaPartita() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        motore.muoviEroe(10000, -10000);
        assertEquals(880, motore.getPosizioneEroe().getX(), 0);
        assertEquals(0, motore.getPosizioneEroe().getY(), 0);
        motore.muoviEroe(-10000, 10000);
        assertEquals(0, motore.getPosizioneEroe().getX(), 0);
        assertEquals(540, motore.getPosizioneEroe().getY(), 0);
    }

    @Test
    public void elencoNemiciNonPermetteRimozioniDallEsterno() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        assertThrows(UnsupportedOperationException.class, () -> motore.getVampiri().clear());
        assertEquals(3, motore.getVampiri().size());
    }

    @Test
    public void vampiriSiAvvicinanoSenzaSuperareIlLimiteContemporaneo() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        VampiroNelMondo vampiro = motore.getVampiri().getFirst();
        double prima = vampiro.getPosizione().distanzaDa(motore.getPosizioneEroe());
        motore.aggiornaMondo();
        assertTrue(vampiro.getPosizione().distanzaDa(motore.getPosizioneEroe()) < prima);
        assertEquals(3, motore.getVampiri().size());
    }

    @Test
    public void attaccoSenzaBersagliNelRaggioNonInfliggeDanni() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        int vitaNemico = motore.getVampiri().getFirst().getVampiro().getVita();
        motore.attaccaNemicoVicino();
        assertEquals(vitaNemico, motore.getVampiri().getFirst().getVampiro().getVita());
        assertEquals(108, motore.getStato().getEroe().getVita());
        assertEquals(0, motore.getStato().getEroe().getVSconfitti());
    }

    @Test
    public void attaccoSceglieIlNemicoPiuVicinoNelRaggio() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        VampiroNelMondo vicino = motore.getVampiri().get(0);
        VampiroNelMondo lontano = motore.getVampiri().get(1);
        portaVicino(vicino, 10);
        portaVicino(lontano, 60);
        vicino.getVampiro().subisciDanno(vicino.getVampiro().getVita() - 1);
        int vitaLontano = lontano.getVampiro().getVita();
        motore.attaccaNemicoVicino();
        assertFalse(motore.getVampiri().contains(vicino));
        assertTrue(motore.getVampiri().contains(lontano));
        assertEquals(vitaLontano, lontano.getVampiro().getVita());
        assertEquals(1, motore.getStato().getSconfittiNelloScenario());
        assertEquals(1, motore.getStato().getEroe().getVSconfitti());
    }

    @Test
    public void annullamentoRipristinaUnaCopiaCompletaDelloStatoPrecedente() {
        Eroe originale = new Eroe(5, 20, 2, 85, 1, 1, 2, 2);
        carica(originale, 3);
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        Eroe tentativo = motore.getStato().getEroe();
        tentativo.aumentaForza();
        tentativo.aumentaSalute();
        tentativo.subisciDanno(80);
        tentativo.usaPozioneCura();
        tentativo.guadagnaEsperienza(50);
        tentativo.registraVittoria();
        motore.getStato().registraVittoriaScenario();
        motore.muoviEroe(50, 50);
        motore.annullaScenarioSenzaProgressi();
        Eroe ripristinato = motore.getStato().getEroe();
        assertNotSame(tentativo, ripristinato);
        assertEquals(5, ripristinato.getLivello());
        assertEquals(20, ripristinato.getEsperienza());
        assertEquals(2, ripristinato.getVSconfitti());
        assertEquals(85, ripristinato.getVita());
        assertEquals(1, ripristinato.getForza());
        assertEquals(1, ripristinato.getSalute());
        assertEquals(2, ripristinato.getStats());
        assertEquals(2, ripristinato.getPozioniCura());
        assertEquals(3, motore.getStato().getNumeroScenario());
        assertEquals(ScenarioGioco.castello, motore.getScenarioCorrente());
        assertEquals(0, motore.getStato().getSconfittiNelloScenario());
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
        assertEquals(480, motore.getPosizioneEroe().getX(), 0);
        assertEquals(310, motore.getPosizioneEroe().getY(), 0);
    }

    @Test
    public void eliminazioneDiTuttiINemiciPortaAllaVittoriaESbloccaIlSuccessivo() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        eliminaRegolari();
        assertEquals(FaseGioco.vittoria, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
        assertEquals(2, motore.getStato().getNumeroScenario());
        assertEquals(8, motore.getStato().getEroe().getVSconfitti());
        int livello = motore.getStato().getEroe().getLivello();
        motore.confermaVittoriaScenario();
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
        assertEquals(ScenarioGioco.profonditaForestaMorta, motore.getScenarioCorrente());
        assertEquals(livello, motore.getStato().getEroe().getLivello());
    }

    @Test
    public void finaleEvocaUnSoloBossDopoIRegolariEFinisceAllaSuaSconfitta() {
        carica(new Eroe(20, 0, 0, 1000, 0, 0, 0, 0), 5);
        motore.avviaScenario(ScenarioGioco.salaDelTrono);
        eliminaRegolari();
        assertEquals(FaseGioco.inGioco, motore.getFase());
        motore.aggiornaMondo();
        motore.aggiornaMondo();
        assertEquals(1, motore.getVampiri().size());
        VampiroNelMondo boss = motore.getVampiri().getFirst();
        assertEquals(TipoVampiro.arciduca, boss.getVampiro().getTipo());
        elimina(boss);
        assertEquals(FaseGioco.vittoria, motore.getFase());
        assertTrue(motore.getStato().isBossSconfitto());
        assertEquals(5, motore.getStato().getNumeroScenario());
        assertTrue(motore.getVampiri().isEmpty());
    }

    @Test
    public void bossNonCompareSeLEroeNonHaIlLivelloRichiesto() {
        carica(new Eroe(), 5);
        motore.avviaScenario(ScenarioGioco.salaDelTrono);
        eliminaRegolari();
        motore.aggiornaMondo();
        assertFalse(motore.getStato().getEroe().puoSfidareBoss());
        assertEquals(FaseGioco.inGioco, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
    }

    @Test
    public void dannoMortaleAContattoChiudeLoScenarioEPermetteIlRipristino() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        Eroe eroe = motore.getStato().getEroe();
        eroe.subisciDanno(eroe.getVita() - 1);
        portaVicino(motore.getVampiri().getFirst(), 0);
        motore.aggiornaMondo();
        assertEquals(FaseGioco.gameOver, motore.getFase());
        assertTrue(eroe.isSconfitto());
        assertTrue(motore.getVampiri().isEmpty());
        assertTrue(motore.getUltimoDannoEroeMillis() > 0);
        motore.annullaScenarioSenzaProgressi();
        assertEquals(108, motore.getStato().getEroe().getVita());
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
    }

    @Test
    public void contrattaccoMortalePassaAllaFaseGameOver() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        motore.getStato().getEroe().subisciDanno(107);
        portaVicino(motore.getVampiri().getFirst(), 0);
        motore.attaccaNemicoVicino();
        assertEquals(FaseGioco.gameOver, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
        assertEquals(0, motore.getStato().getEroe().getVSconfitti());
    }

    @Test
    public void pozioniSiUsanoSoloDuranteLaPartita() {
        Eroe eroe = motore.getStato().getEroe();
        eroe.aggiungiPozioneCura();
        eroe.subisciDanno(80);
        motore.usaPozioneCura();
        assertEquals(28, eroe.getVita());
        assertEquals(1, eroe.getPozioniCura());
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        eroe.subisciDanno(80);
        motore.usaPozioneCura();
        assertEquals(64, eroe.getVita());
        assertEquals(0, eroe.getPozioniCura());
        motore.usaPozioneCura();
        assertEquals(64, eroe.getVita());
    }

    @Test
    public void nuovaPartitaEliminaProgressiENemiciDelTentativoPrecedente() {
        carica(new Eroe(12, 30, 20, 100, 2, 2, 7, 3), 4);
        motore.avviaScenario(ScenarioGioco.castello);
        motore.nuovaPartita();
        assertEquals(1, motore.getStato().getEroe().getLivello());
        assertEquals(0, motore.getStato().getEroe().getPozioniCura());
        assertEquals(1, motore.getStato().getNumeroScenario());
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
        motore.annullaScenarioSenzaProgressi();
        assertEquals(1, motore.getStato().getEroe().getLivello());
    }

    @Test
    public void salvataggioComunicaSuccessoEFallimentoSenzaPerdereLoStato() {
        assertTrue(motore.salva());
        assertSame(motore.getStato(), archivio.salvato);
        archivio.fallisciSalvataggio = true;
        StatoPartita prima = motore.getStato();
        assertFalse(motore.salva());
        assertSame(prima, motore.getStato());
        assertTrue(motore.getUltimoMessaggio().contains("simulato"));
    }

    @Test
    public void caricamentoRiuscitoAbbandonaLoScenarioInCorso() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        carica(new Eroe(12, 0, 10, 120, 0, 0, 0, 2), 4);
        assertEquals(12, motore.getStato().getEroe().getLivello());
        assertEquals(4, motore.getStato().getNumeroScenario());
        assertEquals(FaseGioco.selezioneScenario, motore.getFase());
        assertTrue(motore.getVampiri().isEmpty());
        motore.annullaScenarioSenzaProgressi();
        assertEquals(12, motore.getStato().getEroe().getLivello());
    }

    @Test
    public void caricamentoFallitoMantieneLaPartitaAttuale() {
        motore.avviaScenario(ScenarioGioco.margineForestaMorta);
        StatoPartita prima = motore.getStato();
        archivio.fallisciCaricamento = true;
        motore.carica();
        assertSame(prima, motore.getStato());
        assertEquals(FaseGioco.inGioco, motore.getFase());
        assertEquals(3, motore.getVampiri().size());
        assertTrue(motore.getUltimoMessaggio().contains("simulato"));
    }

    private void carica(Eroe eroe, int scenarioSbloccato) {
        archivio.caricato = new StatoPartita(eroe, false, scenarioSbloccato, 0);
        motore.carica();
    }

    private void portaVicino(VampiroNelMondo vampiro, double distanza) {
        Posizione posizione = vampiro.getPosizione();
        posizione.muovi(motore.getPosizioneEroe().getX() + distanza - posizione.getX(),
                motore.getPosizioneEroe().getY() - posizione.getY(), 880, 540);
    }

    private void elimina(VampiroNelMondo vampiro) {
        portaVicino(vampiro, 0);
        vampiro.getVampiro().subisciDanno(vampiro.getVampiro().getVita() - 1);
        motore.attaccaNemicoVicino();
    }

    private void eliminaRegolari() {
        int richiesti = motore.getScenarioCorrente().getNemiciRegolari();
        for (int i = 0; i < richiesti; i++) {
            motore.aggiornaMondo();
            assertFalse("Nemici regolari terminati prima del previsto", motore.getVampiri().isEmpty());
            assertTrue(motore.getVampiri().size() <= motore.getScenarioCorrente().getMassimoNemiciContemporanei());
            VampiroNelMondo bersaglio = motore.getVampiri().getFirst();
            assertNotEquals(TipoVampiro.arciduca, bersaglio.getVampiro().getTipo());
            elimina(bersaglio);
        }
    }

    private static class ArchivioFinto implements ArchivioPartita {
        private StatoPartita caricato = new StatoPartita();
        private StatoPartita salvato;
        private boolean fallisciSalvataggio;
        private boolean fallisciCaricamento;

        @Override
        public void salva(StatoPartita stato) throws IOException {
            if (fallisciSalvataggio) {
                throw new IOException("Errore di salvataggio simulato");
            }
            salvato = stato;
        }

        @Override
        public StatoPartita carica() throws IOException {
            if (fallisciCaricamento) {
                throw new IOException("Errore di caricamento simulato");
            }
            return caricato;
        }
    }
}
