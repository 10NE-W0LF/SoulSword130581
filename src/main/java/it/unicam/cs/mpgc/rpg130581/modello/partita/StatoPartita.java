package it.unicam.cs.mpgc.rpg130581.modello.partita;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;

/**
 * Contiene lo stato persistente della partita.
 * Tiene traccia dell'eroe, dello scenario sbloccato, dello scenario corrente e del boss.
 */
public class StatoPartita {

    private final Eroe eroe;
    private boolean bossSconfitto;
    private int numeroScenarioSbloccato;
    private ScenarioGioco scenarioCorrente;
    private int sconfittiNelloScenario;

    /**
     * Crea una partita iniziale con un nuovo eroe.
     */
    public StatoPartita() {
        this(new Eroe(), false, 1, 0);
    }

    /**
     * Crea uno stato con eroe e stato del boss.
     *
     * @param eroe eroe della partita
     * @param bossSconfitto true se il boss e' stato sconfitto
     */
    public StatoPartita(Eroe eroe, boolean bossSconfitto) {
        this(eroe, bossSconfitto, 1, 0);
    }

    /**
     * Crea uno stato completo della partita, utile per il caricamento da JSON.
     *
     * @param eroe eroe della partita
     * @param bossSconfitto true se il boss e' stato sconfitto
     * @param numeroScenario scenario massimo sbloccato
     * @param sconfittiNelloScenario nemici gia' sconfitti nello scenario
     */
    public StatoPartita(Eroe eroe, boolean bossSconfitto, int numeroScenario, int sconfittiNelloScenario) {
        this.eroe = eroe;
        this.bossSconfitto = bossSconfitto;
        this.numeroScenarioSbloccato = Math.max(1, Math.min(5, numeroScenario));
        this.scenarioCorrente = ScenarioGioco.daNumero(this.numeroScenarioSbloccato);
        this.sconfittiNelloScenario = Math.max(0, sconfittiNelloScenario);
    }

    public Eroe getEroe() {
        return eroe;
    }

    public boolean isBossSconfitto() {
        return bossSconfitto;
    }

    public void setBossSconfitto(boolean bossSconfitto) {
        this.bossSconfitto = bossSconfitto;
    }

    public ScenarioGioco getScenarioCorrente() {
        return scenarioCorrente;
    }

    public int getNumeroScenario() {
        return numeroScenarioSbloccato;
    }

    public int getSconfittiNelloScenario() {
        return sconfittiNelloScenario;
    }

    /**
     * Registra la sconfitta di un nemico regolare nello scenario corrente.
     */
    public void registraVittoriaScenario() {
        sconfittiNelloScenario++;
    }

    /**
     * Controlla se uno scenario e' accessibile.
     *
     * @param scenario scenario da verificare
     * @return true se lo scenario e' sbloccato
     */
    public boolean isScenarioSbloccato(ScenarioGioco scenario) {
        return scenario.getNumero() <= numeroScenarioSbloccato;
    }

    /**
     * Sblocca lo scenario successivo dopo una vittoria.
     */
    public void sbloccaScenarioSuccessivo() {
        ScenarioGioco scenarioSuccessivo = scenarioCorrente.successivo();
        numeroScenarioSbloccato = Math.max(numeroScenarioSbloccato, scenarioSuccessivo.getNumero());
        sconfittiNelloScenario = 0;
    }

    /**
     * Prepara lo stato per il ritorno al menu degli scenari.
     */
    public void preparaScenarioSbloccato() {
        scenarioCorrente = ScenarioGioco.daNumero(numeroScenarioSbloccato);
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
    }

    /**
     * Imposta lo scenario scelto e ripristina la vita dell'eroe.
     *
     * @param scenario scenario da avviare
     */
    public void impostaScenario(ScenarioGioco scenario) {
        scenarioCorrente = scenario;
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
        eroe.curaCompleta();
    }
}
