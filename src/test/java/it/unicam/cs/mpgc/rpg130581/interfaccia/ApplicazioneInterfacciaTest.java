package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.motore.Posizione;
import it.unicam.cs.mpgc.rpg130581.motore.VampiroNelMondo;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPreferenze;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/** Test JavaFX opzionali: ./gradlew testInterfaccia. Nessun accesso ai salvataggi reali. */
public class ApplicazioneInterfacciaTest {
    private Stage finestra;
    private MotoreGioco motore;
    private ArchivioProva archivio;
    private PreferenzeProva preferenze;

    @BeforeClass
    public static void avviaJavaFx() throws InterruptedException {
        System.setProperty("prism.order", "sw");
        CountDownLatch pronto = new CountDownLatch(1);
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            pronto.countDown();
        });
        assertTrue("JavaFX non inizializzato entro il tempo previsto", pronto.await(10, TimeUnit.SECONDS));
    }

    @AfterClass
    public static void arrestaJavaFx() {
        Platform.exit();
    }

    @Before
    public void preparaApplicazione() throws Exception {
        sulThreadGrafico(() -> {
            archivio = new ArchivioProva();
            preferenze = new PreferenzeProva();
            avviaApplicazione();
        });
    }

    @After
    public void chiudiApplicazione() throws Exception {
        sulThreadGrafico(() -> {
            for (Node nodo : nodi()) {
                if (nodo instanceof VistaGioco vista) {
                    vista.ferma();
                }
            }
            if (finestra != null) {
                finestra.close();
            }
            AppSoulSword.setMotoreGioco(null);
            AppSoulSword.setArchivioPreferenze(null);
        });
    }

    @Test
    public void linguaSiConfermaConSalvaERestaAttivaAlRiavvio() throws Exception {
        sulThreadGrafico(() -> {
            bottone("Lingua").fire();
            assertTrue(scelta("Italiano").isSelected());
            scelta("Italiano").fire();
            assertTrue(scelta("Italiano").isSelected());
            scelta("English").fire();
            assertFalse(scelta("Italiano").isSelected());
            assertTrue(scelta("English").isSelected());
            assertEquals(Lingua.italiano, preferenze.lingua);
            bottone("Salva").fire();
            assertNotNull(bottone("New game"));
            assertEquals(Lingua.inglese, preferenze.lingua);
            assertEquals(0, archivio.salvataggi);
            finestra.close();
            avviaApplicazione();
            assertNotNull(bottone("Language"));
            bottone("New game").fire();
            assertTrue(contieneTesto("Stage selection"));
            bottone("Save").fire();
            assertEquals(1, archivio.salvataggi);
            assertTrue(contieneTesto("Game and language saved."));
        });
    }

    @Test
    public void indietroScartaLaSceltaNonConfermata() throws Exception {
        sulThreadGrafico(() -> {
            bottone("Lingua").fire();
            scelta("English").fire();
            bottone("Indietro").fire();
            assertNotNull(bottone("Nuova partita"));
            assertEquals(Lingua.italiano, preferenze.lingua);
            bottone("Lingua").fire();
            assertTrue(scelta("Italiano").isSelected());
            assertEquals(0, preferenze.scritture);
        });
    }

    @Test
    public void errorePreferenzeRestaVisibileSenzaApplicareLaLingua() throws Exception {
        sulThreadGrafico(() -> {
            preferenze.fallisciScrittura = true;
            bottone("Lingua").fire();
            scelta("English").fire();
            bottone("Salva").fire();
            Label avviso = nodi().stream().filter(Label.class::isInstance).map(Label.class::cast)
                    .filter(label -> label.getText().equals("Impossibile salvare la lingua. Riprova."))
                    .findFirst().orElseThrow();
            assertTrue(avviso.isVisible());
            assertTrue(avviso.isManaged());
            assertEquals(Lingua.italiano, preferenze.lingua);
            bottone("Indietro").fire();
            assertNotNull(bottone("Nuova partita"));
        });
    }

    @Test
    public void pausaRiprendeIlGiocoOAnnullaIlTentativo() throws Exception {
        sulThreadGrafico(() -> {
            avviaPrimoScenario();
            motore.getStato().getEroe().guadagnaEsperienza(10);
            motore.getStato().getEroe().subisciDanno(30);
            premiEscape();
            assertTrue(contieneTesto("PAUSA"));
            bottone("Continua").fire();
            assertFalse(contieneTesto("PAUSA"));
            premiEscape();
            bottone("Scenari").fire();
            assertTrue(contieneTesto("Selezione scenario"));
            assertEquals(0, motore.getStato().getEroe().getEsperienza());
            assertEquals(108, motore.getStato().getEroe().getVita());
            assertTrue(motore.getVampiri().isEmpty());
        });
    }

    @Test
    public void attaccoDellaVistaAggiornaIlModelloEDisegnaUnFrame() throws Exception {
        sulThreadGrafico(() -> {
            VistaGioco vista = avviaPrimoScenario();
            vista.ferma();
            VampiroNelMondo bersaglio = motore.getVampiri().getFirst();
            Posizione posizione = bersaglio.getPosizione();
            posizione.muovi(motore.getPosizioneEroe().getX() - posizione.getX(),
                    motore.getPosizioneEroe().getY() - posizione.getY(), 880, 540);
            bersaglio.getVampiro().subisciDanno(bersaglio.getVampiro().getVita() - 1);
            vista.attacca();
            assertEquals(1, motore.getStato().getSconfittiNelloScenario());
            assertFalse(motore.getVampiri().contains(bersaglio));
            int vita = motore.getStato().getEroe().getVita();
            vista.disegna();
            assertEquals(vita, motore.getStato().getEroe().getVita());
            WritableImage frame = vista.snapshot(null, null);
            assertEquals(960, (int) frame.getWidth());
            assertEquals(620, (int) frame.getHeight());
            assertTrue(frame.getPixelReader().getColor(0, 0).getOpacity() > 0);
        });
    }

    @Test
    public void spriteEIconeEsistonoERiutilizzanoLaCache() throws Exception {
        sulThreadGrafico(() -> {
            ArchivioSprite immagini = new ArchivioSprite();
            for (EvoSoulSword evoluzione : EvoSoulSword.values()) {
                for (TipoAnimazione animazione : TipoAnimazione.values()) {
                    Image sprite = immagini.eroe(evoluzione, animazione);
                    verificaSprite(sprite);
                    assertSame(sprite, immagini.eroe(evoluzione, animazione));
                }
            }
            for (TipoVampiro tipo : TipoVampiro.values()) {
                for (TipoAnimazione animazione : List.of(TipoAnimazione.idle, TipoAnimazione.walk,
                        TipoAnimazione.run, TipoAnimazione.attack, TipoAnimazione.hurt, TipoAnimazione.death)) {
                    verificaSprite(immagini.vampiro(tipo, animazione));
                }
            }
            for (ScenarioGioco scenario : ScenarioGioco.values()) {
                assertFalse(immagini.sfondo(scenario).isError());
            }
            assertSame(immagini.sfondo(ScenarioGioco.margineForestaMorta),
                    immagini.sfondo(ScenarioGioco.profonditaForestaMorta));
            assertFalse(immagini.iconaSoulSword().isError());
            assertFalse(immagini.pozioneCura().isError());
        });
    }

    @Test
    public void rendererRimuoveLeAnimazioniDiMorteTerminateSenzaAlterareLaPartita() throws Exception {
        sulThreadGrafico(() -> {
            motore.avviaScenario(ScenarioGioco.margineForestaMorta);
            RenderGioco renderer = new RenderGioco(motore, new ArchivioSprite(), new TestiInterfaccia());
            VampiroNelMondo vampiro = motore.getVampiri().getFirst();
            List<AnimazioneMorte> morti = new ArrayList<>();
            morti.add(new AnimazioneMorte(vampiro.getVampiro(), new Posizione(100, 100),
                    renderer.dimensioneNemico(vampiro.getVampiro()), 0, 1));
            Canvas canvas = new Canvas(960, 620);
            renderer.disegna(canvas.getGraphicsContext2D(), 960, 620, morti, new AnimazioniEroe(), 0, 0);
            assertTrue(morti.isEmpty());
            assertEquals(3, motore.getVampiri().size());
            assertEquals(108, motore.getStato().getEroe().getVita());
            assertEquals(0, motore.getStato().getSconfittiNelloScenario());
        });
    }

    private void verificaSprite(Image immagine) {
        assertFalse(immagine.isError());
        assertTrue(immagine.getWidth() >= 64);
        assertTrue(immagine.getHeight() >= 64);
        assertEquals(0, immagine.getWidth() % 64, 0);
        assertEquals(0, immagine.getHeight() % 64, 0);
    }

    private void avviaApplicazione() {
        motore = new MotoreGioco(archivio);
        AppSoulSword.setMotoreGioco(motore);
        AppSoulSword.setArchivioPreferenze(preferenze);
        finestra = new Stage();
        new AppSoulSword().start(finestra);
        finestra.hide();
    }

    private VistaGioco avviaPrimoScenario() {
        bottone("Nuova partita").fire();
        nodi().stream().filter(Button.class::isInstance).map(Button.class::cast)
                .filter(button -> button.getText().startsWith("1  ")).findFirst().orElseThrow().fire();
        return nodi().stream().filter(VistaGioco.class::isInstance)
                .map(VistaGioco.class::cast).findFirst().orElseThrow();
    }

    private void premiEscape() {
        finestra.getScene().getOnKeyPressed().handle(new KeyEvent(KeyEvent.KEY_PRESSED,
                "", "", KeyCode.ESCAPE, false, false, false, false));
    }

    private Button bottone(String testo) {
        return nodi().stream().filter(Button.class::isInstance).map(Button.class::cast)
                .filter(button -> button.getText().equals(testo)).findFirst()
                .orElseThrow(() -> new AssertionError("Pulsante assente: " + testo));
    }

    private ToggleButton scelta(String testo) {
        return nodi().stream().filter(ToggleButton.class::isInstance).map(ToggleButton.class::cast)
                .filter(button -> button.getText().equals(testo)).findFirst().orElseThrow();
    }

    private boolean contieneTesto(String testo) {
        return nodi().stream().anyMatch(nodo -> nodo instanceof Labeled label && label.getText().equals(testo)
                || nodo instanceof Text text && text.getText().equals(testo));
    }

    private List<Node> nodi() {
        List<Node> risultato = new ArrayList<>();
        if (finestra != null && finestra.getScene() != null) {
            visita(finestra.getScene().getRoot(), risultato);
        }
        return risultato;
    }

    private void visita(Node nodo, List<Node> risultato) {
        risultato.add(nodo);
        if (nodo instanceof Parent contenitore) {
            for (Node figlio : contenitore.getChildrenUnmodifiable()) {
                visita(figlio, risultato);
            }
        }
    }

    private static void sulThreadGrafico(AzioneGrafica azione) throws Exception {
        FutureTask<Void> esecuzione = new FutureTask<>(() -> {
            azione.esegui();
            return null;
        });
        Platform.runLater(esecuzione);
        esecuzione.get(15, TimeUnit.SECONDS);
    }

    @FunctionalInterface
    private interface AzioneGrafica {
        void esegui() throws Exception;
    }

    private static class ArchivioProva implements ArchivioPartita {
        private int salvataggi;
        @Override public void salva(StatoPartita stato) { salvataggi++; }
        @Override public StatoPartita carica() { return new StatoPartita(); }
    }

    private static class PreferenzeProva implements ArchivioPreferenze {
        private Lingua lingua = Lingua.italiano;
        private int scritture;
        private boolean fallisciScrittura;
        @Override public Lingua caricaLingua() { return lingua; }
        @Override public void salvaLingua(Lingua nuovaLingua) throws IOException {
            if (fallisciScrittura) {
                throw new IOException("Errore simulato");
            }
            lingua = nuovaLingua;
            scritture++;
        }
    }
}
