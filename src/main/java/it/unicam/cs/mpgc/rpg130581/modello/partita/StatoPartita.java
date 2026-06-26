package it.unicam.cs.mpgc.rpg130581.modello.partita;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;

public class StatoPartita {

    private final Eroe eroe;
    private boolean bossSconfitto;
    private int numeroScenario;
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
        this.numeroScenario = Math.max(1, Math.min(5, numeroScenario));
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
        return ScenarioGioco.daNumero(numeroScenario);
    }

    public int getNumeroScenario() {
        return numeroScenario;
    }

    public int getSconfittiNelloScenario() {
        return sconfittiNelloScenario;
    }

    public void registraVittoriaScenario() {
        sconfittiNelloScenario++;
    }

    public void avanzaScenario() {
        ScenarioGioco scenarioSuccessivo = getScenarioCorrente().successivo();
        numeroScenario = scenarioSuccessivo.getNumero();
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
        eroe.curaCompleta();
    }

    public void impostaScenario(ScenarioGioco scenario) {
        numeroScenario = scenario.getNumero();
        sconfittiNelloScenario = 0;
        bossSconfitto = false;
        eroe.curaCompleta();
    }
}
