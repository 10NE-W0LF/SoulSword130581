package it.unicam.cs.mpgc.rpg130581.modello.partita;

import java.util.Arrays;
import java.util.List;

/**
 * Elenco degli scenari giocabili.
 * Ogni scenario definisce sfondo, numero di nemici, difficolta' e presenza del boss.
 */
public enum ScenarioGioco {
    margineForestaMorta(1, "Dead Forest", "/sfondi/dead_forest.png", 8, 3, 1, 3, 0, 0, 0.0, false),
    profonditaForestaMorta(2, "Deep Dead Forest", "/sfondi/dead_forest.png", 12, 4, 3, 6, 0, 0, 0.0, false),
    castello(3, "Castle", "/sfondi/castle.png", 15, 5, 5, 6, 7, 10, 0.45, false),
    terrazza(4, "Terrace", "/sfondi/terrace.png", 16, 6, 5, 6, 10, 15, 0.85, false),
    salaDelTrono(5, "Throne Room", "/sfondi/throne_room.png", 8, 5, 4, 6, 12, 15, 0.55, true);

    private static final List<ScenarioGioco> scenariOrdinati = Arrays.asList(values());

    private final int numero;
    private final String nomeVisualizzato;
    private final String percorsoSfondo;
    private final int nemiciRegolari;
    private final int massimoNemiciContemporanei;
    private final int livelloMinimoVampiro;
    private final int livelloMassimoVampiro;
    private final int livelloMinimoElite;
    private final int livelloMassimoElite;
    private final double probabilitaElite;
    private final boolean scenarioBoss;

    ScenarioGioco(int numero, String nomeVisualizzato, String percorsoSfondo, int nemiciRegolari,
                  int massimoNemiciContemporanei, int livelloMinimoVampiro, int livelloMassimoVampiro,
                  int livelloMinimoElite, int livelloMassimoElite, double probabilitaElite, boolean scenarioBoss) {
        this.numero = numero;
        this.nomeVisualizzato = nomeVisualizzato;
        this.percorsoSfondo = percorsoSfondo;
        this.nemiciRegolari = nemiciRegolari;
        this.massimoNemiciContemporanei = massimoNemiciContemporanei;
        this.livelloMinimoVampiro = livelloMinimoVampiro;
        this.livelloMassimoVampiro = livelloMassimoVampiro;
        this.livelloMinimoElite = livelloMinimoElite;
        this.livelloMassimoElite = livelloMassimoElite;
        this.probabilitaElite = probabilitaElite;
        this.scenarioBoss = scenarioBoss;
    }

    /**
     * Cerca uno scenario tramite il suo numero progressivo.
     *
     * @param numero numero dello scenario
     * @return scenario corrispondente o primo scenario se il numero non e' valido
     */
    public static ScenarioGioco daNumero(int numero) {
        for (ScenarioGioco scenario : values()) {
            if (scenario.numero == numero) {
                return scenario;
            }
        }
        return margineForestaMorta;
    }

    /**
     * Restituisce lo scenario successivo.
     *
     * @return scenario successivo o scenario corrente se si e' gia' all'ultimo
     */
    public ScenarioGioco successivo() {
        int indice = scenariOrdinati.indexOf(this);
        if (indice < 0 || indice == scenariOrdinati.size() - 1) {
            return this;
        }
        return scenariOrdinati.get(indice + 1);
    }

    public int getNumero() {
        return numero;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }

    public String getPercorsoSfondo() {
        return percorsoSfondo;
    }

    public int getNemiciRegolari() {
        return nemiciRegolari;
    }

    public int getMassimoNemiciContemporanei() {
        return massimoNemiciContemporanei;
    }

    public int getLivelloMinimoVampiro() {
        return livelloMinimoVampiro;
    }

    public int getLivelloMassimoVampiro() {
        return livelloMassimoVampiro;
    }

    public int getLivelloMinimoElite() {
        return livelloMinimoElite;
    }

    public int getLivelloMassimoElite() {
        return livelloMassimoElite;
    }

    public double getProbabilitaElite() {
        return probabilitaElite;
    }

    public boolean isScenarioBoss() {
        return scenarioBoss;
    }
}
