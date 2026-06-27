package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.motore.Posizione;
import it.unicam.cs.mpgc.rpg130581.motore.VampiroNelMondo;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VistaGioco extends Canvas {

    private static final int dimensioneEntita = 72;
    private static final int dimensioneBoss = 94;
    private static final int dimensioneFotogramma = 64;
    private static final double zoomCamera = 1.65;
    private static final double velocitaEroe = 1.45;
    private static final double velocitaEroeCorsa = 1.95;
    private static final long durataFotogrammaNanos = 135_000_000L;
    private static final long durataHurtMillis = 360;
    private static final long durataAttackMillis = 560;

    private final MotoreGioco motoreGioco;
    private final ArchivioSprite archivioSprite = new ArchivioSprite();
    private final Set<KeyCode> tastiPremuti = new HashSet<KeyCode>();
    private final List<AnimazioneMorte> mortiRecenti = new ArrayList<AnimazioneMorte>();
    private final Runnable azioneFineScenario;

    private AnimationTimer cicloGioco;
    private long ultimoCambioFotogramma;
    private int fotogrammaCorrente;
    private boolean eroeInMovimento;
    private boolean eroeCorre;
    private double direzioneEroeX;
    private double direzioneEroeY = 1;
    private long attaccoEroeInizioNanos;
    private long attaccoEroeFineNanos;
    private boolean fineScenarioNotificata;
    private long fineScenarioRilevataNanos;

    public VistaGioco(MotoreGioco motoreGioco, Runnable azioneFineScenario) {
        super(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        this.motoreGioco = motoreGioco;
        this.azioneFineScenario = azioneFineScenario;
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
        attaccoEroeInizioNanos = adessoNanos;
        attaccoEroeFineNanos = adessoNanos + durataAttackMillis * 1_000_000L;

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
        grafica.clearRect(0, 0, getWidth(), getHeight());
        grafica.save();
        applicaCamera(grafica);
        disegnaSfondo(grafica);
        disegnaVampiri(grafica);
        disegnaMorteVampiri(grafica);
        disegnaEroe(grafica);
        grafica.restore();
        disegnaInterfaccia(grafica);
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
        if (ultimoCambioFotogramma == 0 || istante - ultimoCambioFotogramma >= durataFotogrammaNanos) {
            fotogrammaCorrente++;
            ultimoCambioFotogramma = istante;
        }
    }

    private void applicaMovimento() {
        double dx = 0;
        double dy = 0;
        eroeCorre = tastiPremuti.contains(KeyCode.SHIFT);
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

        eroeInMovimento = dx != 0 || dy != 0;
        if (eroeInMovimento) {
            if (dx != 0 && dy != 0) {
                dx *= 0.7071;
                dy *= 0.7071;
            }
            direzioneEroeX = dx;
            direzioneEroeY = dy;
        }
        motoreGioco.muoviEroe(dx, dy);
    }

    private void applicaCamera(GraphicsContext grafica) {
        Posizione posizione = motoreGioco.getPosizioneEroe();
        double larghezzaVisibile = getWidth() / zoomCamera;
        double altezzaVisibile = getHeight() / zoomCamera;
        double centroEroeX = posizione.getX() + dimensioneEntita / 2.0;
        double centroEroeY = posizione.getY() + dimensioneEntita / 2.0;
        double cameraX = limita(centroEroeX - larghezzaVisibile / 2.0, 0,
                MotoreGioco.larghezzaMondo - larghezzaVisibile);
        double cameraY = limita(centroEroeY - altezzaVisibile / 2.0, 0,
                MotoreGioco.altezzaMondo - altezzaVisibile);
        grafica.scale(zoomCamera, zoomCamera);
        grafica.translate(-cameraX, -cameraY);
    }

    private double limita(double valore, double minimo, double massimo) {
        if (massimo < minimo) {
            return minimo;
        }
        return Math.max(minimo, Math.min(massimo, valore));
    }

    private void disegnaSfondo(GraphicsContext grafica) {
        Image sfondo = archivioSprite.sfondo(motoreGioco.getScenarioCorrente());
        if (sfondo != null && !sfondo.isError()) {
            grafica.setImageSmoothing(true);
            grafica.drawImage(sfondo, 0, 0, MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
            grafica.setFill(Color.rgb(0, 0, 0, 0.18));
            grafica.fillRect(0, 0, MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
            return;
        }
        grafica.setFill(Color.rgb(37, 48, 43));
        grafica.fillRect(0, 0, MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
    }

    private void disegnaVampiri(GraphicsContext grafica) {
        long adessoMillis = System.currentTimeMillis();
        for (VampiroNelMondo vampiroNelMondo : motoreGioco.getVampiri()) {
            Vampiro vampiro = vampiroNelMondo.getVampiro();
            Posizione posizione = vampiroNelMondo.getPosizione();
            int dimensione = vampiro.getTipo().getLivelloAsset() == 3 ? dimensioneBoss : dimensioneEntita;
            int riga = rigaDaDirezione(vampiroNelMondo.getUltimoDx(), vampiroNelMondo.getUltimoDy());
            TipoAnimazione animazione = animazioneVampiro(vampiroNelMondo, adessoMillis);
            disegnaFotogramma(grafica, archivioSprite.vampiro(vampiro.getTipo(), animazione),
                    posizione.getX(), posizione.getY(), dimensione, dimensione, riga, fotogrammaCorrente, true);
            disegnaBarra(grafica, posizione.getX(), posizione.getY() - 8, dimensione,
                    vampiro.getVita(), vampiro.getVitaMassima(), Color.rgb(162, 42, 58));
            grafica.setFill(Color.WHITE);
            grafica.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
            grafica.fillText(vampiro.getTipo().getNomeVisualizzato() + " Lv " + vampiro.getLivello(),
                    posizione.getX() + 4, posizione.getY() + dimensione + 12);
        }
    }

    private TipoAnimazione animazioneVampiro(VampiroNelMondo vampiroNelMondo, long adessoMillis) {
        if (adessoMillis - vampiroNelMondo.getUltimoDannoMillis() <= durataHurtMillis) {
            return TipoAnimazione.hurt;
        }
        if (adessoMillis - vampiroNelMondo.getUltimoAttaccoMillis() <= durataAttackMillis) {
            return TipoAnimazione.attack;
        }
        return TipoAnimazione.walk;
    }

    private void disegnaMorteVampiri(GraphicsContext grafica) {
        long adessoNanos = System.nanoTime();
        Iterator<AnimazioneMorte> iterator = mortiRecenti.iterator();
        while (iterator.hasNext()) {
            AnimazioneMorte morte = iterator.next();
            Image foglio = archivioSprite.vampiro(morte.vampiro.getTipo(), TipoAnimazione.death);
            int indice = indiceAnimazioneUnaVolta(foglio, morte.inizioNanos, adessoNanos);
            disegnaFotogramma(grafica, foglio, morte.posizione.getX(), morte.posizione.getY(),
                    morte.dimensione, morte.dimensione, morte.riga, indice, false);
            if (adessoNanos - morte.inizioNanos >= durataAnimazioneNanos(foglio)) {
                iterator.remove();
            }
        }
    }

    private void disegnaEroe(GraphicsContext grafica) {
        Eroe eroe = motoreGioco.getStato().getEroe();
        Posizione posizione = motoreGioco.getPosizioneEroe();
        int riga = rigaDaDirezione(direzioneEroeX, direzioneEroeY);
        TipoAnimazione animazione = animazioneEroe();
        Image foglio = archivioSprite.eroe(eroe.getEvoluzioneSoulSword(), animazione);
        int indice = indiceFotogrammaEroe(foglio, animazione);
        boolean ciclica = animazione != TipoAnimazione.death && animazione != TipoAnimazione.attack
                && animazione != TipoAnimazione.walkAttack && animazione != TipoAnimazione.runAttack;
        disegnaFotogramma(grafica, foglio, posizione.getX(), posizione.getY(),
                dimensioneEntita, dimensioneEntita, riga, indice, ciclica);
    }

    private TipoAnimazione animazioneEroe() {
        long adessoMillis = System.currentTimeMillis();
        long adessoNanos = System.nanoTime();
        if (motoreGioco.getFase() == FaseGioco.gameOver || motoreGioco.getStato().getEroe().isSconfitto()) {
            return TipoAnimazione.death;
        }
        if (adessoMillis - motoreGioco.getUltimoDannoEroeMillis() <= durataHurtMillis) {
            return TipoAnimazione.hurt;
        }
        if (adessoNanos <= attaccoEroeFineNanos) {
            if (eroeInMovimento && eroeCorre) {
                return TipoAnimazione.runAttack;
            }
            if (eroeInMovimento) {
                return TipoAnimazione.walkAttack;
            }
            return TipoAnimazione.attack;
        }
        if (eroeInMovimento) {
            return eroeCorre ? TipoAnimazione.run : TipoAnimazione.walk;
        }
        return TipoAnimazione.idle;
    }

    private int indiceFotogrammaEroe(Image foglio, TipoAnimazione animazione) {
        if (animazione == TipoAnimazione.attack || animazione == TipoAnimazione.walkAttack
                || animazione == TipoAnimazione.runAttack) {
            return indiceAnimazioneUnaVolta(foglio, attaccoEroeInizioNanos, System.nanoTime());
        }
        if (animazione == TipoAnimazione.death) {
            return indiceAnimazioneUnaVolta(foglio, fineScenarioRilevataNanos == 0 ? System.nanoTime() : fineScenarioRilevataNanos,
                    System.nanoTime());
        }
        return fotogrammaCorrente;
    }

    private void disegnaFotogramma(GraphicsContext grafica, Image foglioSprite,
                                   double x, double y, int larghezza, int altezza,
                                   int riga, int indiceFotogramma, boolean ciclica) {
        if (foglioSprite == null || foglioSprite.isError()
                || foglioSprite.getWidth() < dimensioneFotogramma || foglioSprite.getHeight() < dimensioneFotogramma) {
            grafica.setFill(Color.rgb(80, 190, 220));
            grafica.fillRect(x, y, larghezza, altezza);
            return;
        }
        int colonne = Math.max(1, (int) Math.round(foglioSprite.getWidth() / dimensioneFotogramma));
        int righe = Math.max(1, (int) Math.round(foglioSprite.getHeight() / dimensioneFotogramma));
        int rigaUsata = Math.max(0, Math.min(righe - 1, riga));
        int indiceUsato;
        if (ciclica) {
            indiceUsato = Math.floorMod(indiceFotogramma, colonne);
        } else {
            indiceUsato = Math.max(0, Math.min(colonne - 1, indiceFotogramma));
        }
        grafica.setImageSmoothing(false);
        grafica.drawImage(foglioSprite,
                indiceUsato * dimensioneFotogramma, rigaUsata * dimensioneFotogramma,
                dimensioneFotogramma, dimensioneFotogramma,
                x, y, larghezza, altezza);
    }

    private int indiceAnimazioneUnaVolta(Image foglio, long inizioNanos, long adessoNanos) {
        if (inizioNanos <= 0) {
            return 0;
        }
        int colonne = numeroFotogrammi(foglio);
        int indice = (int) ((adessoNanos - inizioNanos) / durataFotogrammaNanos);
        return Math.max(0, Math.min(colonne - 1, indice));
    }

    private long durataAnimazioneNanos(Image foglio) {
        return numeroFotogrammi(foglio) * durataFotogrammaNanos;
    }

    private int numeroFotogrammi(Image foglio) {
        if (foglio == null || foglio.isError() || foglio.getWidth() < dimensioneFotogramma) {
            return 1;
        }
        return Math.max(1, (int) Math.round(foglio.getWidth() / dimensioneFotogramma));
    }

    private int rigaDaDirezione(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx < 0 ? 1 : 2;
        }
        return dy < 0 ? 3 : 0;
    }

    private void disegnaInterfaccia(GraphicsContext grafica) {
        Eroe eroe = motoreGioco.getStato().getEroe();
        grafica.setFill(Color.rgb(14, 17, 18, 0.82));
        grafica.fillRoundRect(14, 14, 610, 118, 8, 8);
        disegnaBarra(grafica, 28, 36, 190, eroe.getVita(), eroe.getVitaMassima(),
                Color.rgb(190, 42, 52));
        disegnaBarra(grafica, 28, 66, 190, eroe.getEsperienza(),
                eroe.esperienzaNuovoLivello(), Color.rgb(80, 130, 220));

        grafica.setFill(Color.WHITE);
        grafica.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        grafica.fillText("Scenario " + motoreGioco.getScenarioCorrente().getNumero() + " - "
                + motoreGioco.getScenarioCorrente().getNomeVisualizzato(), 236, 43);
        grafica.setFont(Font.font("SansSerif", 13));
        String livello = eroe.isLivelloMassimo() ? "Livello 30 MAX" : "Livello " + eroe.getLivello();
        grafica.fillText(eroe.getNome() + " - " + livello + " - "
                + eroe.getEvoluzioneSoulSword().getNomeVisualizzato(), 236, 67);
        grafica.fillText("Nemici: " + motoreGioco.getStato().getSconfittiNelloScenario()
                + "/" + motoreGioco.getScenarioCorrente().getNemiciRegolari(), 236, 91);
        grafica.fillText("WASD/Frecce: muovi | SHIFT: corsa | SPAZIO: attacca | ESC: pausa", 28, 108);
    }

    private void disegnaBarra(GraphicsContext grafica, double x, double y, double larghezza,
                              int valore, int massimo, Color colore) {
        int altezza = 12;
        double rapporto = massimo <= 0 ? 0 : Math.max(0, Math.min(1, (double) valore / massimo));
        grafica.setFill(Color.rgb(12, 14, 15));
        grafica.fillRect(x, y, larghezza, altezza);
        grafica.setFill(colore);
        grafica.fillRect(x, y, larghezza * rapporto, altezza);
        grafica.setStroke(Color.rgb(220, 220, 220));
        grafica.strokeRect(x, y, larghezza, altezza);
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
                int dimensione = vampiroNelMondo.getVampiro().getTipo().getLivelloAsset() == 3 ? dimensioneBoss : dimensioneEntita;
                int riga = rigaDaDirezione(vampiroNelMondo.getUltimoDx(), vampiroNelMondo.getUltimoDy());
                mortiRecenti.add(new AnimazioneMorte(vampiroNelMondo.getVampiro(),
                        new Posizione(vampiroNelMondo.getPosizione().getX(), vampiroNelMondo.getPosizione().getY()),
                        dimensione, riga, adessoNanos));
            }
        }
    }

    private static class AnimazioneMorte {
        private final Vampiro vampiro;
        private final Posizione posizione;
        private final int dimensione;
        private final int riga;
        private final long inizioNanos;

        private AnimazioneMorte(Vampiro vampiro, Posizione posizione, int dimensione, int riga, long inizioNanos) {
            this.vampiro = vampiro;
            this.posizione = posizione;
            this.dimensione = dimensione;
            this.riga = riga;
            this.inizioNanos = inizioNanos;
        }
    }
}
