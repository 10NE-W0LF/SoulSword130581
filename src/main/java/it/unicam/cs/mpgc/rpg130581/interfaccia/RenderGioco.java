package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.motore.Posizione;
import it.unicam.cs.mpgc.rpg130581.motore.VampiroNelMondo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Iterator;
import java.util.List;

class RenderGioco {

    static final int dimensioneEntita = 72;
    static final int dimensioneBoss = 94;
    static final long durataFotogrammaNanos = 135_000_000L;
    static final long durataHurtMillis = 360;
    static final long durataAttackMillis = 560;

    private static final int dimensioneFotogramma = 64;
    private static final double zoomCamera = 1.65;

    private final MotoreGioco motoreGioco;
    private final ArchivioSprite archivioSprite;

    RenderGioco(MotoreGioco motoreGioco, ArchivioSprite archivioSprite) {
        this.motoreGioco = motoreGioco;
        this.archivioSprite = archivioSprite;
    }

    void disegna(GraphicsContext grafica, double larghezzaCanvas, double altezzaCanvas,
                 List<AnimazioneMorte> mortiRecenti, AnimazioniEroe animazioniEroe,
                 int fotogrammaCorrente, long fineScenarioRilevataNanos) {
        grafica.clearRect(0, 0, larghezzaCanvas, altezzaCanvas);
        grafica.save();
        applicaCamera(grafica, larghezzaCanvas, altezzaCanvas);
        disegnaSfondo(grafica);
        disegnaVampiri(grafica, fotogrammaCorrente);
        disegnaMorteVampiri(grafica, mortiRecenti);
        disegnaEroe(grafica, animazioniEroe, fotogrammaCorrente, fineScenarioRilevataNanos);
        grafica.restore();
        disegnaInterfaccia(grafica);
    }

    int dimensioneNemico(Vampiro vampiro) {
        return vampiro.getTipo().getLivelloAsset() == 3 ? dimensioneBoss : dimensioneEntita;
    }

    int rigaDaDirezione(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx < 0 ? 1 : 2;
        }
        return dy < 0 ? 3 : 0;
    }

    private void applicaCamera(GraphicsContext grafica, double larghezzaCanvas, double altezzaCanvas) {
        Posizione posizione = motoreGioco.getPosizioneEroe();
        double larghezzaVisibile = larghezzaCanvas / zoomCamera;
        double altezzaVisibile = altezzaCanvas / zoomCamera;
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

    private void disegnaVampiri(GraphicsContext grafica, int fotogrammaCorrente) {
        long adessoMillis = System.currentTimeMillis();
        for (VampiroNelMondo vampiroNelMondo : motoreGioco.getVampiri()) {
            Vampiro vampiro = vampiroNelMondo.getVampiro();
            Posizione posizione = vampiroNelMondo.getPosizione();
            int dimensione = dimensioneNemico(vampiro);
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

    private void disegnaMorteVampiri(GraphicsContext grafica, List<AnimazioneMorte> mortiRecenti) {
        long adessoNanos = System.nanoTime();
        Iterator<AnimazioneMorte> iterator = mortiRecenti.iterator();
        while (iterator.hasNext()) {
            AnimazioneMorte morte = iterator.next();
            Image foglio = archivioSprite.vampiro(morte.getVampiro().getTipo(), TipoAnimazione.death);
            int indice = indiceAnimazioneUnaVolta(foglio, morte.getInizioNanos(), adessoNanos);
            disegnaFotogramma(grafica, foglio, morte.getPosizione().getX(), morte.getPosizione().getY(),
                    morte.getDimensione(), morte.getDimensione(), morte.getRiga(), indice, false);
            if (adessoNanos - morte.getInizioNanos() >= durataAnimazioneNanos(foglio)) {
                iterator.remove();
            }
        }
    }

    private void disegnaEroe(GraphicsContext grafica, AnimazioniEroe animazioniEroe,
                             int fotogrammaCorrente, long fineScenarioRilevataNanos) {
        Eroe eroe = motoreGioco.getStato().getEroe();
        Posizione posizione = motoreGioco.getPosizioneEroe();
        int riga = rigaDaDirezione(animazioniEroe.getDirezioneX(), animazioniEroe.getDirezioneY());
        TipoAnimazione animazione = animazioneEroe(animazioniEroe);
        Image foglio = archivioSprite.eroe(eroe.getEvoSoulSword(), animazione);
        int indice = indiceFotogrammaEroe(foglio, animazione, animazioniEroe.getAttaccoInizioNanos(),
                fineScenarioRilevataNanos, fotogrammaCorrente);
        boolean ciclica = animazione != TipoAnimazione.death && animazione != TipoAnimazione.attack
                && animazione != TipoAnimazione.walkAttack && animazione != TipoAnimazione.runAttack;
        disegnaFotogramma(grafica, foglio, posizione.getX(), posizione.getY(),
                dimensioneEntita, dimensioneEntita, riga, indice, ciclica);
    }

    private TipoAnimazione animazioneEroe(AnimazioniEroe animazioniEroe) {
        long adessoMillis = System.currentTimeMillis();
        long adessoNanos = System.nanoTime();
        if (motoreGioco.getFase() == FaseGioco.gameOver || motoreGioco.getStato().getEroe().isSconfitto()) {
            return TipoAnimazione.death;
        }
        if (adessoMillis - motoreGioco.getUltimoDannoEroeMillis() <= durataHurtMillis) {
            return TipoAnimazione.hurt;
        }
        if (adessoNanos <= animazioniEroe.getAttaccoFineNanos()) {
            if (animazioniEroe.isInMovimento() && animazioniEroe.isCorre()) {
                return TipoAnimazione.runAttack;
            }
            if (animazioniEroe.isInMovimento()) {
                return TipoAnimazione.walkAttack;
            }
            return TipoAnimazione.attack;
        }
        if (animazioniEroe.isInMovimento()) {
            return animazioniEroe.isCorre() ? TipoAnimazione.run : TipoAnimazione.walk;
        }
        return TipoAnimazione.idle;
    }

    private int indiceFotogrammaEroe(Image foglio, TipoAnimazione animazione, long attaccoInizioNanos,
                                     long fineScenarioRilevataNanos, int fotogrammaCorrente) {
        if (animazione == TipoAnimazione.attack || animazione == TipoAnimazione.walkAttack
                || animazione == TipoAnimazione.runAttack) {
            return indiceAnimazioneUnaVolta(foglio, attaccoInizioNanos, System.nanoTime());
        }
        if (animazione == TipoAnimazione.death) {
            long inizio = fineScenarioRilevataNanos == 0 ? System.nanoTime() : fineScenarioRilevataNanos;
            return indiceAnimazioneUnaVolta(foglio, inizio, System.nanoTime());
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
        int indiceUsato = ciclica
                ? Math.floorMod(indiceFotogramma, colonne)
                : Math.max(0, Math.min(colonne - 1, indiceFotogramma));
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
                + eroe.getEvoSoulSword().getNomeVisualizzato(), 236, 67);
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
}
