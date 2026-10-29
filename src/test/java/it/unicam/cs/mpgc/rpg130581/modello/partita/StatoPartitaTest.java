package it.unicam.cs.mpgc.rpg130581.modello.partita;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatoPartitaTest {
    @Test
    public void soloIlPrimoScenarioEAccessibileAllInizio() {
        StatoPartita stato = new StatoPartita();
        assertTrue(stato.isScenarioSbloccato(ScenarioGioco.margineForestaMorta));
        assertFalse(stato.isScenarioSbloccato(ScenarioGioco.profonditaForestaMorta));
        assertFalse(stato.isScenarioSbloccato(ScenarioGioco.salaDelTrono));
        assertEquals(0, stato.getSconfittiNelloScenario());
        assertFalse(stato.isBossSconfitto());
    }

    @Test
    public void caricamentoLimitaScenarioEConteggioInferiore() {
        StatoPartita alto = new StatoPartita(new Eroe(), false, 99, -5);
        assertEquals(5, alto.getNumeroScenario());
        assertEquals(ScenarioGioco.salaDelTrono, alto.getScenarioCorrente());
        assertEquals(0, alto.getSconfittiNelloScenario());
        assertEquals(1, new StatoPartita(new Eroe(), false, -2, 0).getNumeroScenario());
    }

    @Test
    public void vittoriaSbloccaSoloLoScenarioSuccessivo() {
        StatoPartita stato = new StatoPartita();
        stato.registraVittoriaScenario();
        stato.registraVittoriaScenario();
        assertEquals(2, stato.getSconfittiNelloScenario());
        stato.sbloccaScenarioSuccessivo();
        assertEquals(2, stato.getNumeroScenario());
        assertEquals(0, stato.getSconfittiNelloScenario());
        assertEquals(ScenarioGioco.margineForestaMorta, stato.getScenarioCorrente());
        assertFalse(stato.isScenarioSbloccato(ScenarioGioco.castello));
    }

    @Test
    public void rigiocareUnoScenarioNonPerdeQuelliGiaSbloccati() {
        StatoPartita stato = new StatoPartita(new Eroe(), false, 4, 0);
        stato.impostaScenario(ScenarioGioco.margineForestaMorta);
        stato.sbloccaScenarioSuccessivo();
        assertEquals(4, stato.getNumeroScenario());
    }

    @Test
    public void ingressoNelloScenarioCuraEAzzeraIProgressiDelTentativo() {
        Eroe eroe = new Eroe(1, 0, 0, 30, 0, 0, 0, 2);
        StatoPartita stato = new StatoPartita(eroe, true, 5, 6);
        stato.impostaScenario(ScenarioGioco.castello);
        assertEquals(ScenarioGioco.castello, stato.getScenarioCorrente());
        assertEquals(eroe.getVitaMassima(), eroe.getVita());
        assertEquals(2, eroe.getPozioniCura());
        assertEquals(0, stato.getSconfittiNelloScenario());
        assertFalse(stato.isBossSconfitto());
        assertEquals(5, stato.getNumeroScenario());
    }

    @Test
    public void ritornoAlMenuPreparaLoScenarioMassimoSbloccato() {
        StatoPartita stato = new StatoPartita(new Eroe(), true, 4, 6);
        stato.impostaScenario(ScenarioGioco.margineForestaMorta);
        stato.setBossSconfitto(true);
        stato.registraVittoriaScenario();
        stato.preparaScenarioSbloccato();
        assertEquals(ScenarioGioco.terrazza, stato.getScenarioCorrente());
        assertFalse(stato.isBossSconfitto());
        assertEquals(0, stato.getSconfittiNelloScenario());
    }

    @Test
    public void completamentoDelFinaleNonCreaUnSestoScenario() {
        StatoPartita stato = new StatoPartita(new Eroe(), true, 5, 8);
        stato.sbloccaScenarioSuccessivo();
        assertEquals(5, stato.getNumeroScenario());
    }
}
