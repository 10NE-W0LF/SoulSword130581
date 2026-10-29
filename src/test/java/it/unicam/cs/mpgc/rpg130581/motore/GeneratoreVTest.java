package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class GeneratoreVTest {
    private Random casuale(double decimale, boolean massimo) {
        return new Random() {
            @Override public double nextDouble() { return decimale; }
            @Override public int nextInt(int limite) { return massimo ? limite - 1 : 0; }
        };
    }

    @Test
    public void vampiriBasePossonoRaggiungereEntrambiILivelliLimiteDelloScenario() {
        for (ScenarioGioco scenario : ScenarioGioco.values()) {
            Vampiro minimo = new GeneratoreV(casuale(0.999, false)).creaVampiroRegolare(scenario);
            Vampiro massimo = new GeneratoreV(casuale(0.999, true)).creaVampiroRegolare(scenario);
            assertEquals(TipoVampiro.vampiroBase, minimo.getTipo());
            assertEquals(TipoVampiro.vampiroBase, massimo.getTipo());
            assertEquals(scenario.getLivelloMinimoVampiro(), minimo.getLivello());
            assertEquals(scenario.getLivelloMassimoVampiro(), massimo.getLivello());
        }
    }

    @Test
    public void eliteUsanoIlProprioIntervalloDiLivelli() {
        for (ScenarioGioco scenario : ScenarioGioco.values()) {
            if (scenario.getProbabilitaElite() == 0) {
                continue;
            }
            Vampiro minimo = new GeneratoreV(casuale(0, false)).creaVampiroRegolare(scenario);
            Vampiro massimo = new GeneratoreV(casuale(0, true)).creaVampiroRegolare(scenario);
            assertEquals(TipoVampiro.vampiroElite, minimo.getTipo());
            assertEquals(TipoVampiro.vampiroElite, massimo.getTipo());
            assertEquals(scenario.getLivelloMinimoElite(), minimo.getLivello());
            assertEquals(scenario.getLivelloMassimoElite(), massimo.getLivello());
        }
    }

    @Test
    public void probabilitaNullaNonGeneraEliteNemmenoConEstrazioneZero() {
        GeneratoreV generatore = new GeneratoreV(casuale(0, false));
        assertEquals(TipoVampiro.vampiroBase,
                generatore.creaVampiroRegolare(ScenarioGioco.margineForestaMorta).getTipo());
        assertEquals(TipoVampiro.vampiroBase,
                generatore.creaVampiroRegolare(ScenarioGioco.profonditaForestaMorta).getTipo());
    }

    @Test
    public void estrazioneUgualeAllaProbabilitaNonGeneraElite() {
        ScenarioGioco scenario = ScenarioGioco.castello;
        GeneratoreV generatore = new GeneratoreV(casuale(scenario.getProbabilitaElite(), false));
        assertEquals(TipoVampiro.vampiroBase, generatore.creaVampiroRegolare(scenario).getTipo());
    }

    @Test
    public void bossEUnArciducaDiLivelloVenti() {
        Vampiro boss = new GeneratoreV(casuale(0, false)).creaBoss();
        assertEquals(TipoVampiro.arciduca, boss.getTipo());
        assertEquals(20, boss.getLivello());
        assertEquals(boss.getVitaMassima(), boss.getVita());
    }
}
