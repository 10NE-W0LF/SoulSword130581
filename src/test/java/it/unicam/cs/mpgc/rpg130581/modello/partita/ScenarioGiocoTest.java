package it.unicam.cs.mpgc.rpg130581.modello.partita;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScenarioGiocoTest {
    @Test
    public void progressioneVisitaTuttiGliScenariESiFermaAlFinale() {
        ScenarioGioco corrente = ScenarioGioco.margineForestaMorta;
        for (int numero = 1; numero <= 5; numero++) {
            assertEquals(numero, corrente.getNumero());
            assertSame(corrente, ScenarioGioco.daNumero(numero));
            corrente = corrente.successivo();
        }
        assertSame(ScenarioGioco.salaDelTrono, corrente);
        assertSame(corrente, corrente.successivo());
    }

    @Test
    public void numeroSconosciutoUsaIlPrimoScenario() {
        assertSame(ScenarioGioco.margineForestaMorta, ScenarioGioco.daNumero(0));
        assertSame(ScenarioGioco.margineForestaMorta, ScenarioGioco.daNumero(6));
    }

    @Test
    public void configurazioniConsentonoGenerazioneValidaERisorseEsistenti() {
        int scenariBoss = 0;
        for (ScenarioGioco scenario : ScenarioGioco.values()) {
            assertTrue(scenario.getNemiciRegolari() >= scenario.getMassimoNemiciContemporanei());
            assertTrue(scenario.getMassimoNemiciContemporanei() > 0);
            assertTrue(scenario.getLivelloMinimoVampiro() > 0);
            assertTrue(scenario.getLivelloMassimoVampiro() >= scenario.getLivelloMinimoVampiro());
            assertTrue(scenario.getProbabilitaElite() >= 0 && scenario.getProbabilitaElite() <= 1);
            if (scenario.getProbabilitaElite() > 0) {
                assertTrue(scenario.getLivelloMinimoElite() > 0);
                assertTrue(scenario.getLivelloMassimoElite() >= scenario.getLivelloMinimoElite());
            }
            assertNotNull(scenario.getNomeVisualizzato(), getClass().getResource(scenario.getPercorsoSfondo()));
            if (scenario.isScenarioBoss()) {
                scenariBoss++;
                assertSame(ScenarioGioco.salaDelTrono, scenario);
            }
        }
        assertEquals(1, scenariBoss);
    }
}
