package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Random;

public class GeneratoreVampiri {

    private final Random random;

    public GeneratoreVampiri(Random random) {
        this.random = random;
    }

    public Vampiro creaVampiroRegolare(ScenarioGioco scenario) {
        if (scenario.getProbabilitaElite() > 0 && random.nextDouble() < scenario.getProbabilitaElite()) {
            return new Vampiro(TipoVampiro.VAMPIRO_ELITE, casualeTra(scenario.getLivelloMinimoElite(), scenario.getLivelloMassimoElite()));
        }
        return new Vampiro(TipoVampiro.VAMPIRO_BASE, casualeTra(scenario.getLivelloMinimoVampiro(), scenario.getLivelloMassimoVampiro()));
    }

    public Vampiro creaBoss() {
        return new Vampiro(TipoVampiro.ARCIDUCA, 20);
    }

    private int casualeTra(int minimo, int massimo) {
        return minimo + random.nextInt(massimo - minimo + 1);
    }
}
