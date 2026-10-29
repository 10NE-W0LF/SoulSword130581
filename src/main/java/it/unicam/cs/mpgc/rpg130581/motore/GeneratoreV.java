package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Responsabile della creazione dei vampiri.
 * Usa i dati dello scenario per decidere livello e probabilita' di nemici elite.
 */
public class GeneratoreV implements GenerazioneVampiri {

    private final RandomGenerator random;

    /**
     * Crea un generatore di vampiri.
     *
     * @param random generatore casuale condiviso con il motore
     */
    public GeneratoreV(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
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
