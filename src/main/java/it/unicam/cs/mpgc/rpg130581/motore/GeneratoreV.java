package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Random;

/**
 * Responsabile della creazione dei vampiri.
 * Usa i dati dello scenario per decidere livello e probabilita' di nemici elite.
 */
public class GeneratoreV {

    private final Random random;

    /**
     * Crea un generatore di vampiri.
     *
     * @param random generatore casuale condiviso con il motore
     */
    public GeneratoreV(Random random) {
        this.random = random;
    }

    /**
     * Crea un vampiro regolare coerente con lo scenario.
     *
     * @param scenario scenario corrente
     * @return vampiro base o elite generato casualmente
     */
    public Vampiro creaVampiroRegolare(ScenarioGioco scenario) {
        if (scenario.getProbabilitaElite() > 0 && random.nextDouble() < scenario.getProbabilitaElite()) {
            return new Vampiro(TipoVampiro.vampiroElite, casualeTra(scenario.getLivelloMinimoElite(), scenario.getLivelloMassimoElite()));
        }
        return new Vampiro(TipoVampiro.vampiroBase, casualeTra(scenario.getLivelloMinimoVampiro(), scenario.getLivelloMassimoVampiro()));
    }

    /**
     * Crea il boss finale.
     *
     * @return Arciduca Vampiro di livello 20
     */
    public Vampiro creaBoss() {
        return new Vampiro(TipoVampiro.arciduca, 20);
    }

    private int casualeTra(int minimo, int massimo) {
        return minimo + random.nextInt(massimo - minimo + 1);
    }
}
