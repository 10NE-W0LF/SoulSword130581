package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.motore.Posizione;
import it.unicam.cs.mpgc.rpg130581.motore.VampiroNelMondo;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VistaGioco extends Canvas {

    private static final double velocitaEroe = 1.45;
    private static final double velocitaEroeCorsa = 1.95;

    private final MotoreGioco motoreGioco;
    private final ArchivioSprite archivioSprite = new ArchivioSprite();
    private final RenderGioco renderGioco;
    private final AnimazioniEroe animazioniEroe = new AnimazioniEroe();
    private final Set<KeyCode> tastiPremuti = new HashSet<KeyCode>();
    private final List<AnimazioneMorte> mortiRecenti = new ArrayList<AnimazioneMorte>();
    private final Runnable azioneFineScenario;

    private AnimationTimer cicloGioco;
    private long ultimoCambioFotogramma;
    private int fotogrammaCorrente;
    private boolean fineScenarioNotificata;
    private long fineScenarioRilevataNanos;

    public VistaGioco(MotoreGioco motoreGioco, Runnable azioneFineScenario) {
        super(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        this.motoreGioco = motoreGioco;
        this.azioneFineScenario = azioneFineScenario;
        this.renderGioco = new RenderGioco(motoreGioco, archivioSprite);
        setFocusTraversable(true);
        avviaCicloGioco();
    }

    public void premi(KeyCode tasto) {
        tastiPremuti.add(tasto);
    }

    public void rilascia(KeyCode tasto) {
        tastiPremuti.remove(tasto);
    }

    public void attacca() {
        long adessoNanos = System.nanoTime();
        animazioniEroe.registraAttacco(adessoNanos,
                adessoNanos + RenderGioco.durataAttackMillis * 1_000_000L);

        List<VampiroNelMondo> prima = new ArrayList<VampiroNelMondo>(motoreGioco.getVampiri());
        motoreGioco.attaccaNemicoVicino();
        registraVampiriEliminati(prima, adessoNanos);
        disegna();
    }

    public void ferma() {
        if (cicloGioco != null) {
            cicloGioco.stop();
        }
        tastiPremuti.clear();
    }

    public void riprendi() {
        if (cicloGioco != null && motoreGioco.getFase() == FaseGioco.inGioco) {
            cicloGioco.start();
        }
    }

    public void disegna() {
        GraphicsContext grafica = getGraphicsContext2D();
        renderGioco.disegna(grafica, getWidth(), getHeight(), mortiRecenti,
                animazioniEroe, fotogrammaCorrente, fineScenarioRilevataNanos);
    }

    private void avviaCicloGioco() {
        cicloGioco = new AnimationTimer() {
            @Override
            public void handle(long istante) {
                aggiornaFotogramma(istante);
                if (motoreGioco.getFase() == FaseGioco.inGioco) {
                    applicaMovimento();
                    motoreGioco.aggiornaMondo();
                }
                disegna();
                notificaFineScenarioSeNecessario(istante);
            }
        };
        cicloGioco.start();
    }

    private void aggiornaFotogramma(long istante) {
        if (ultimoCambioFotogramma == 0
                || istante - ultimoCambioFotogramma >= RenderGioco.durataFotogrammaNanos) {
            fotogrammaCorrente++;
            ultimoCambioFotogramma = istante;
        }
    }

    private void applicaMovimento() {
        double dx = 0;
        double dy = 0;
        boolean eroeCorre = tastiPremuti.contains(KeyCode.SHIFT);
        double velocita = eroeCorre ? velocitaEroeCorsa : velocitaEroe;
        if (tastiPremuti.contains(KeyCode.LEFT) || tastiPremuti.contains(KeyCode.A)) {
            dx -= velocita;
        }
        if (tastiPremuti.contains(KeyCode.RIGHT) || tastiPremuti.contains(KeyCode.D)) {
            dx += velocita;
        }
        if (tastiPremuti.contains(KeyCode.UP) || tastiPremuti.contains(KeyCode.W)) {
            dy -= velocita;
        }
        if (tastiPremuti.contains(KeyCode.DOWN) || tastiPremuti.contains(KeyCode.S)) {
            dy += velocita;
        }

        boolean eroeInMovimento = dx != 0 || dy != 0;
        if (eroeInMovimento) {
            if (dx != 0 && dy != 0) {
                dx *= 0.7071;
                dy *= 0.7071;
            }
        }
        animazioniEroe.aggiornaMovimento(eroeInMovimento, eroeCorre, dx, dy);
        motoreGioco.muoviEroe(dx, dy);
    }

    private void notificaFineScenarioSeNecessario(long istante) {
        if (fineScenarioNotificata || motoreGioco.getFase() == FaseGioco.inGioco) {
            return;
        }
        if (fineScenarioRilevataNanos == 0) {
            fineScenarioRilevataNanos = istante;
        }
        long attesa = motoreGioco.getFase() == FaseGioco.gameOver ? 900_000_000L : 450_000_000L;
        if (istante - fineScenarioRilevataNanos >= attesa) {
            fineScenarioNotificata = true;
            ferma();
            Platform.runLater(azioneFineScenario);
        }
    }

    private void registraVampiriEliminati(List<VampiroNelMondo> prima, long adessoNanos) {
        List<VampiroNelMondo> dopo = new ArrayList<VampiroNelMondo>(motoreGioco.getVampiri());
        for (VampiroNelMondo vampiroNelMondo : prima) {
            if (!dopo.contains(vampiroNelMondo) && vampiroNelMondo.getVampiro().isSconfitto()) {
                int dimensione = renderGioco.dimensioneNemico(vampiroNelMondo.getVampiro());
                int riga = renderGioco.rigaDaDirezione(vampiroNelMondo.getUltimoDx(), vampiroNelMondo.getUltimoDy());
                mortiRecenti.add(new AnimazioneMorte(vampiroNelMondo.getVampiro(),
                        new Posizione(vampiroNelMondo.getPosizione().getX(), vampiroNelMondo.getPosizione().getY()),
                        dimensione, riga, adessoNanos));
            }
        }
    }
}
