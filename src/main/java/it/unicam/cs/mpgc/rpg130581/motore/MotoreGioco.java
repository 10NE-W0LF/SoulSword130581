package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.combattimento.PozioneCura;
import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Classe centrale della logica di gioco.
 * Coordina stato partita, scenari, movimento, combattimento, generazione nemici e salvataggi.
 */
public class MotoreGioco {

    public static final int larghezzaMondo = 960;
    public static final int altezzaMondo = 620;
    public static final int raggioAttacco = 88;
    private static final double raggioDannoContatto = 38.0;
    private static final long ricaricaDannoVampiroMs = 1150;

    private final Random random = new Random();
    private final GeneratoreV generatoreV = new GeneratoreV(random);
    private final ServizioCombattimento servizioCombattimento = new ServizioCombattimento(random);
    private final ArchivioPartita archivioPartita;
    private final List<VampiroNelMondo> vampiri = new ArrayList<VampiroNelMondo>();
    private StatoPartita stato = new StatoPartita();
    private StatoPartita statoPrimaScenario;
    private Posizione posizioneEroe = nuovaPosizioneCentrale();
    private FaseGioco fase = FaseGioco.selezioneScenario;
    private String ultimoMessaggio = "La SoulSword attende una nuova caccia.";
    private long ultimoDannoEroeMillis;

    /**
     * Crea il motore di gioco usando l'archivio indicato per salvataggi e caricamenti.
     *
     * @param archivioPartita archivio usato per la persistenza della partita
     */
    public MotoreGioco(ArchivioPartita archivioPartita) {
        this.archivioPartita = archivioPartita;
    }

    /**
     * Inizializza una nuova partita e torna alla selezione degli scenari.
     */
    public void nuovaPartita() {
        stato = new StatoPartita();
        statoPrimaScenario = null;
        vampiri.clear();
        posizioneEroe = nuovaPosizioneCentrale();
        fase = FaseGioco.selezioneScenario;
        ultimoMessaggio = "Nuova partita creata. Scegli uno scenario.";
    }

    /**
     * Avvia uno scenario solo se e' sbloccato.
     * Prima dell'ingresso salva una copia dello stato, utile per annullare i progressi in caso di abbandono.
     *
     * @param scenario scenario da avviare
     * @return true se lo scenario e' stato avviato
     */
    public boolean avviaScenario(ScenarioGioco scenario) {
        if (!stato.isScenarioSbloccato(scenario)) {
            ultimoMessaggio = "Completa prima lo scenario precedente.";
            return false;
        }

        statoPrimaScenario = copiaStato(stato);
        stato.impostaScenario(scenario);
        vampiri.clear();
        posizioneEroe = nuovaPosizioneCentrale();
        fase = FaseGioco.inGioco;
        ultimoDannoEroeMillis = 0;
        ultimoMessaggio = "Scenario avviato: " + scenario.getNomeVisualizzato() + ".";
        generaVampiriIniziali();
        return true;
    }

    /**
     * Annulla lo scenario corrente senza salvare i progressi ottenuti durante il tentativo.
     */
    public void annullaScenarioSenzaProgressi() {
        if (statoPrimaScenario != null && fase != FaseGioco.vittoria) {
            stato = copiaStato(statoPrimaScenario);
            ultimoMessaggio = "Scenario interrotto. I progressi non sono stati salvati.";
        }
        statoPrimaScenario = null;
        vampiri.clear();
        posizioneEroe = nuovaPosizioneCentrale();
        fase = FaseGioco.selezioneScenario;
    }

    /**
     * Conferma la vittoria dello scenario e prepara il ritorno al menu.
     */
    public void confermaVittoriaScenario() {
        stato.preparaScenarioSbloccato();
        statoPrimaScenario = null;
        vampiri.clear();
        posizioneEroe = nuovaPosizioneCentrale();
        fase = FaseGioco.selezioneScenario;
    }

    /**
     * Muove l'eroe nel mondo di gioco.
     *
     * @param dx spostamento orizzontale
     * @param dy spostamento verticale
     */
    public void muoviEroe(double dx, double dy) {
        if (fase != FaseGioco.inGioco) {
            return;
        }
        posizioneEroe.muovi(dx, dy, larghezzaMondo - 80, altezzaMondo - 80);
    }

    /**
     * Aggiorna lo stato del mondo a ogni frame:
     * genera nuovi nemici, controlla il boss e muove i vampiri verso l'eroe.
     */
    public void aggiornaMondo() {
        if (fase != FaseGioco.inGioco) {
            return;
        }
        if (deveGenerareVampiroRegolare()) {
            generaVampiroRegolare();
        }
        provaAEvocareBossAutomaticamente();
        for (VampiroNelMondo vampiroNelMondo : vampiri) {
            inseguiEroe(vampiroNelMondo);
            provaADanneggiareEroe(vampiroNelMondo);
            if (fase != FaseGioco.inGioco) {
                return;
            }
        }
    }

    /**
     * Esegue l'attacco dell'eroe contro il vampiro piu' vicino nel raggio della SoulSword.
     */
    public void attaccaNemicoVicino() {
        if (fase != FaseGioco.inGioco) {
            return;
        }
        VampiroNelMondo vampiroVicino = vampiroVicinoNelRaggio();
        if (vampiroVicino == null) {
            ultimoMessaggio = "Nessun vampiro nel raggio della SoulSword.";
            return;
        }

        long adesso = System.currentTimeMillis();
        vampiroVicino.registraDanno(adesso);
        EsitoCombattimento esito = servizioCombattimento.attacca(stato.getEroe(), vampiroVicino.getVampiro());
        ultimoMessaggio = esito.getMessaggio();
        if (esito.getDannoVampiro() > 0) {
            ultimoDannoEroeMillis = adesso;
        }
        if (esito.isEroeSconfitto()) {
            terminaConGameOver("La notte ha vinto. Sei stato sconfitto.");
            return;
        }
        if (esito.isVampiroSconfitto()) {
            if (vampiroVicino.getVampiro().getTipo() == TipoVampiro.arciduca) {
                stato.setBossSconfitto(true);
            } else {
                stato.registraVittoriaScenario();
            }
            vampiri.remove(vampiroVicino);
            provaDropPozione(vampiroVicino.getVampiro());
            gestisciConclusioneScenario();
        }
    }

    /**
     * Usa una Pozione Cura durante lo scenario, se disponibile.
     */
    public void usaPozioneCura() {
        if (fase != FaseGioco.inGioco) {
            return;
        }
        Eroe eroe = stato.getEroe();
        if (eroe.usaPozioneCura()) {
            ultimoMessaggio = "Pozione Cura usata: +" + PozioneCura.valoreCura + " vita.";
            return;
        }
        ultimoMessaggio = eroe.getPozioniCura() <= 0
                ? "Non hai Pozioni Cura."
                : "La vita e' gia' al massimo.";
    }

    /**
     * Salva la partita corrente tramite l'archivio configurato.
     */
    public void salva() {
        try {
            archivioPartita.salva(stato);
            ultimoMessaggio = "Partita salvata.";
        } catch (IOException exception) {
            ultimoMessaggio = "Salvataggio fallito: " + exception.getMessage();
        }
    }

    /**
     * Carica una partita salvata e torna alla selezione scenario.
     */
    public void carica() {
        try {
            stato = archivioPartita.carica();
            statoPrimaScenario = null;
            vampiri.clear();
            posizioneEroe = nuovaPosizioneCentrale();
            fase = FaseGioco.selezioneScenario;
            ultimoMessaggio = "Partita caricata. Scegli uno scenario.";
        } catch (IOException exception) {
            ultimoMessaggio = "Caricamento fallito: " + exception.getMessage();
        }
    }

    // Popola l'arena fino al limite di nemici contemporanei previsto dallo scenario.
    private void generaVampiriIniziali() {
        int iniziali = Math.min(stato.getScenarioCorrente().getMassimoNemiciContemporanei(), nemiciRegolariRimanenti());
        for (int i = 0; i < iniziali; i++) {
            generaVampiroRegolare();
        }
    }

    private void generaVampiroRegolare() {
        Vampiro vampiro = generatoreV.creaVampiroRegolare(stato.getScenarioCorrente());
        vampiri.add(new VampiroNelMondo(vampiro, posizioneCasualeSulBordo()));
    }

    // Genera nuovi vampiri finche' lo scenario non ha raggiunto il numero richiesto.
    private boolean deveGenerareVampiroRegolare() {
        return nemiciRegolariRimanenti() > 0
                && vampiriRegolariAttivi() < stato.getScenarioCorrente().getMassimoNemiciContemporanei();
    }

    private int nemiciRegolariRimanenti() {
        return stato.getScenarioCorrente().getNemiciRegolari()
                - stato.getSconfittiNelloScenario()
                - vampiriRegolariAttivi();
    }

    private int vampiriRegolariAttivi() {
        int conteggio = 0;
        for (VampiroNelMondo vampiroNelMondo : vampiri) {
            if (vampiroNelMondo.getVampiro().getTipo() != TipoVampiro.arciduca) {
                conteggio++;
            }
        }
        return conteggio;
    }

    private boolean nemiciRegolariEliminati() {
        return stato.getSconfittiNelloScenario() >= stato.getScenarioCorrente().getNemiciRegolari()
                && vampiriRegolariAttivi() == 0;
    }

    private boolean bossAttivo() {
        for (VampiroNelMondo vampiroNelMondo : vampiri) {
            if (vampiroNelMondo.getVampiro().getTipo() == TipoVampiro.arciduca) {
                return true;
            }
        }
        return false;
    }

    // Nel livello finale evoca automaticamente il boss quando l'arena e' pronta.
    private void provaAEvocareBossAutomaticamente() {
        if (stato.getScenarioCorrente().isScenarioBoss()
                && nemiciRegolariEliminati()
                && stato.getEroe().puoSfidareBoss()
                && !stato.isBossSconfitto()
                && !bossAttivo()) {
            vampiri.add(new VampiroNelMondo(generatoreV.creaBoss(), posizioneCasualeSulBordo()));
            ultimoMessaggio = "L'Arciduca Vampiro scende dal trono.";
        }
    }

    // Tenta il drop della pozione dopo la sconfitta di un vampiro.
    private void provaDropPozione(Vampiro vampiro) {
        if (PozioneCura.esceDalDrop(random) && stato.getEroe().aggiungiPozioneCura()) {
            ultimoMessaggio = vampiro.getTipo().getNomeVisualizzato() + " lascia una Pozione Cura.";
        }
    }

    // Controlla se lo scenario corrente deve passare alla fase di vittoria.
    private void gestisciConclusioneScenario() {
        ScenarioGioco scenario = stato.getScenarioCorrente();
        if (scenario.isScenarioBoss()) {
            if (stato.isBossSconfitto()) {
                stato.sbloccaScenarioSuccessivo();
                fase = FaseGioco.vittoria;
                ultimoMessaggio = "Vittoria! La Throne Room e' libera e la caccia e' conclusa.";
            }
            return;
        }

        if (nemiciRegolariEliminati()) {
            stato.sbloccaScenarioSuccessivo();
            fase = FaseGioco.vittoria;
            ultimoMessaggio = "Vittoria! Hai completato " + scenario.getNomeVisualizzato() + ".";
        }
    }

    private void terminaConGameOver(String messaggio) {
        fase = FaseGioco.gameOver;
        vampiri.clear();
        ultimoMessaggio = messaggio;
    }

    // Piazza i nemici ai bordi della mappa per farli entrare gradualmente nell'area di gioco.
    private Posizione posizioneCasualeSulBordo() {
        int lato = random.nextInt(4);
        if (lato == 0) {
            return new Posizione(20, random.nextInt(altezzaMondo - 80) + 20);
        }
        if (lato == 1) {
            return new Posizione(larghezzaMondo - 100, random.nextInt(altezzaMondo - 80) + 20);
        }
        if (lato == 2) {
            return new Posizione(random.nextInt(larghezzaMondo - 100) + 20, 20);
        }
        return new Posizione(random.nextInt(larghezzaMondo - 100) + 20, altezzaMondo - 100);
    }

    private Posizione nuovaPosizioneCentrale() {
        return new Posizione(larghezzaMondo / 2.0, altezzaMondo / 2.0);
    }

    // Muove un vampiro nella direzione dell'eroe e registra la direzione usata dallo sprite.
    private void inseguiEroe(VampiroNelMondo vampiroNelMondo) {
        Posizione posizioneNemico = vampiroNelMondo.getPosizione();
        double dx = posizioneEroe.getX() - posizioneNemico.getX();
        double dy = posizioneEroe.getY() - posizioneNemico.getY();
        double distanza = Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
        double velocita = velocitaPer(vampiroNelMondo.getVampiro().getTipo());
        double movimentoX = dx / distanza * velocita;
        double movimentoY = dy / distanza * velocita;
        posizioneNemico.muovi(movimentoX, movimentoY, larghezzaMondo - 80, altezzaMondo - 80);
        vampiroNelMondo.registraMovimento(movimentoX, movimentoY);
    }

    private double velocitaPer(TipoVampiro tipo) {
        if (tipo == TipoVampiro.arciduca) {
            return 0.42;
        }
        if (tipo == TipoVampiro.vampiroElite) {
            return 0.58;
        }
        return 0.48;
    }

    // Infligge danno a contatto rispettando un tempo di ricarica tra un colpo e l'altro.
    private void provaADanneggiareEroe(VampiroNelMondo vampiroNelMondo) {
        double distanza = vampiroNelMondo.getPosizione().distanzaDa(posizioneEroe);
        long adesso = System.currentTimeMillis();
        if (distanza <= raggioDannoContatto
                && vampiroNelMondo.puoAttaccare(adesso, ricaricaDannoVampiroMs)) {
            int danno = Math.max(1, vampiroNelMondo.getVampiro().getPotenzaAttacco() / 2 + random.nextInt(4));
            stato.getEroe().subisciDanno(danno);
            vampiroNelMondo.registraAttacco(adesso);
            ultimoDannoEroeMillis = adesso;
            ultimoMessaggio = vampiroNelMondo.getVampiro().getTipo().getNomeVisualizzato()
                    + " ti colpisce: -" + danno + " vita.";
            if (stato.getEroe().isSconfitto()) {
                terminaConGameOver("Game Over. I vampiri hanno spezzato la SoulSword.");
            }
        }
    }

    // Cerca il bersaglio piu' vicino tra i vampiri raggiungibili dalla SoulSword.
    private VampiroNelMondo vampiroVicinoNelRaggio() {
        VampiroNelMondo vampiroVicino = null;
        double distanzaMinima = Double.MAX_VALUE;
        for (VampiroNelMondo vampiroNelMondo : vampiri) {
            double distanza = vampiroNelMondo.getPosizione().distanzaDa(posizioneEroe);
            if (distanza <= raggioAttacco && distanza < distanzaMinima) {
                vampiroVicino = vampiroNelMondo;
                distanzaMinima = distanza;
            }
        }
        return vampiroVicino;
    }

    // Crea una copia dello stato per poter annullare uno scenario non concluso.
    private StatoPartita copiaStato(StatoPartita origine) {
        Eroe eroe = origine.getEroe();
        Eroe copiaEroe = new Eroe(eroe.getLivello(), eroe.getEsperienza(), eroe.getVSconfitti(),
                eroe.getVita(), eroe.getForza(), eroe.getSalute(), eroe.getStats(), eroe.getPozioniCura());
        return new StatoPartita(copiaEroe, origine.isBossSconfitto(),
                origine.getNumeroScenario(), origine.getSconfittiNelloScenario());
    }

    public StatoPartita getStato() {
        return stato;
    }

    public Posizione getPosizioneEroe() {
        return posizioneEroe;
    }

    public List<VampiroNelMondo> getVampiri() {
        return Collections.unmodifiableList(vampiri);
    }

    public String getUltimoMessaggio() {
        return ultimoMessaggio;
    }

    public ScenarioGioco getScenarioCorrente() {
        return stato.getScenarioCorrente();
    }

    public FaseGioco getFase() {
        return fase;
    }

    public long getUltimoDannoEroeMillis() {
        return ultimoDannoEroeMillis;
    }
}
