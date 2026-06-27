package it.unicam.cs.mpgc.rpg130581.modello.partita;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;

public class StatoPartita {

    private final Eroe eroe;
    private boolean bossSconfitto;
    private int numeroScenarioSbloccato;
    private ScenarioGioco scenarioCorrente;
    private int sconfittiNelloScenario;

    public StatoPartita() {
        this(new Eroe(), false, 1, 0);
    }

    public StatoPartita(Eroe eroe, boolean bossSconfitto) {
        this(eroe, bossSconfitto, 1, 0);
    }

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

    public void registraVittoriaScenario() {
        sconfittiNelloScenario++;
    }

    public boolean isScenarioSbloccato(ScenarioGioco scenario) {
        return scenario.getNumero() <= numeroScenarioSbloccato;
    }

    public void sbloccaScenarioSuccessivo() {
        ScenarioGioco scenarioSuccessivo = scenarioCorrente.successivo();
        numeroScenarioSbloccato = Math.max(numeroScenarioSbloccato, scenarioSuccessivo.getNumero());
        sconfittiNelloScenario = 0;
    }

    public void preparaScenarioSbloccato() {
        scenarioCorrente = ScenarioGioco.daNumero(numeroScenarioSbloccato);
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
    }

    public void impostaScenario(ScenarioGioco scenario) {
        scenarioCorrente = scenario;
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
        eroe.curaCompleta();
    }
}
