package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoluzioneSoulSword;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ApplicazioneSoulSword extends Application {

    private static MotoreGioco motoreGioco;

    private Stage finestra;
    private final ArchivioSprite archivioSprite = new ArchivioSprite();

    public static void setMotoreGioco(MotoreGioco nuovoMotoreGioco) {
        motoreGioco = nuovoMotoreGioco;
    }

    @Override
    public void start(Stage finestra) {
        if (motoreGioco == null) {
            throw new IllegalStateException("MotoreGioco non inizializzato.");
        }

        this.finestra = finestra;
        finestra.setTitle("SoulSword");
        finestra.getIcons().add(archivioSprite.iconaSoulSword());
        finestra.setResizable(false);
        mostraMenuPrincipale();
        finestra.show();
    }

    private void mostraMenuPrincipale() {
        Text titolo = testo("SoulSword", 58, FontWeight.EXTRA_BOLD);
        titolo.setFill(Color.rgb(210, 238, 255));
        Text sottotitolo = testo("La caccia dei vampiri inizia dalla tua lama.", 18, FontWeight.NORMAL);

        Button nuovaPartita = bottone("Nuova partita");
        Button caricaPartita = bottone("Carica partita");
        Button esci = bottone("Esci");

        nuovaPartita.setOnAction(ignored -> {
            motoreGioco.nuovaPartita();
            mostraMenuScenari();
        });
        caricaPartita.setOnAction(ignored -> {
            motoreGioco.carica();
            mostraMenuScenari();
        });
        esci.setOnAction(ignored -> finestra.close());

        VBox pannello = pannelloMenu();
        pannello.getChildren().addAll(titolo, sottotitolo, nuovaPartita, caricaPartita, esci);
        impostaScena(schermataConSfondo(pannello));
    }

    private void mostraMenuScenari() {
        Eroe eroe = motoreGioco.getStato().getEroe();

        ImageView immagineEroe = new ImageView(archivioSprite.eroe(eroe.getEvoluzioneSoulSword(), TipoAnimazione.IDLE));
        immagineEroe.setViewport(new Rectangle2D(0, 0, 64, 64));
        immagineEroe.setFitWidth(170);
        immagineEroe.setFitHeight(170);
        immagineEroe.setPreserveRatio(true);
        immagineEroe.setSmooth(false);

        Text titolo = testo("Selezione scenario", 36, FontWeight.EXTRA_BOLD);
        Text nomeEroe = testo(eroe.getNome(), 18, FontWeight.BOLD);
        Text livelloEroe = testo("Livello " + eroe.getLivello(), 18, FontWeight.BOLD);
        Text soulSword = testo(eroe.getEvoluzioneSoulSword().getNomeVisualizzato(), 18, FontWeight.BOLD);
        Color coloreEvoluzione = coloreSoulSword(eroe.getEvoluzioneSoulSword());
        nomeEroe.setFill(Color.rgb(80, 170, 255));
        livelloEroe.setFill(coloreEvoluzione);
        soulSword.setFill(coloreEvoluzione);

        VBox schedaEroe = new VBox(12, immagineEroe, nomeEroe, livelloEroe, soulSword);
        schedaEroe.setAlignment(Pos.CENTER);
        schedaEroe.setPadding(new Insets(28));
        schedaEroe.setMinWidth(280);
        schedaEroe.setStyle("-fx-background-color: rgba(8, 10, 16, 0.82); -fx-background-radius: 14;");

        VBox listaScenari = new VBox(10);
        listaScenari.setAlignment(Pos.CENTER_LEFT);
        for (ScenarioGioco scenario : ScenarioGioco.values()) {
            Button bottoneScenario = bottone(scenario.getNumero() + "  " + scenario.getNomeVisualizzato()
                    + "   | Nemici: " + scenario.getNemiciRegolari()
                    + (scenario.isScenarioBoss() ? "   | Boss" : ""));
            bottoneScenario.setMaxWidth(Double.MAX_VALUE);
            bottoneScenario.setOnAction(ignored -> mostraGioco(scenario));
            listaScenari.getChildren().add(bottoneScenario);
        }

        Button tornaMenu = bottone("Menu principale");
        tornaMenu.setOnAction(ignored -> mostraMenuPrincipale());
        Button salva = bottone("Salva");
        salva.setOnAction(ignored -> motoreGioco.salva());

        VBox destra = new VBox(16, titolo, listaScenari, salva, tornaMenu);
        destra.setAlignment(Pos.CENTER_LEFT);
        destra.setPadding(new Insets(28));
        destra.setStyle("-fx-background-color: rgba(8, 10, 16, 0.72); -fx-background-radius: 14;");

        HBox contenuto = new HBox(26, schedaEroe, destra);
        contenuto.setAlignment(Pos.CENTER);
        contenuto.setPadding(new Insets(32));
        impostaScena(schermataConSfondo(contenuto));
    }

    private void mostraGioco(ScenarioGioco scenario) {
        motoreGioco.avviaScenario(scenario);
        VistaGioco vistaGioco = new VistaGioco(motoreGioco, this::mostraSchermataEsito);
        StackPane radice = new StackPane(vistaGioco);
        Scene scena = new Scene(radice, MotoreGioco.LARGHEZZA_MONDO, MotoreGioco.ALTEZZA_MONDO);
        final boolean[] pausaAperta = {false};
        scena.setOnKeyPressed(evento -> {
            if (pausaAperta[0]) {
                return;
            }
            if (evento.getCode() == KeyCode.SPACE) {
                vistaGioco.attacca();
            } else if (evento.getCode() == KeyCode.ESCAPE) {
                pausaAperta[0] = true;
                mostraPausa(radice, vistaGioco, pausaAperta);
            } else {
                vistaGioco.premi(evento.getCode());
            }
        });
        scena.setOnKeyReleased(evento -> vistaGioco.rilascia(evento.getCode()));
        finestra.setScene(scena);
        vistaGioco.requestFocus();
    }

    private void mostraPausa(StackPane radice, VistaGioco vistaGioco, boolean[] pausaAperta) {
        vistaGioco.ferma();

        Text titolo = testo("PAUSA", 48, FontWeight.EXTRA_BOLD);
        titolo.setFill(Color.rgb(210, 238, 255));
        Text nota = testo("I progressi dello scenario vengono salvati solo in caso di vittoria.", 15, FontWeight.NORMAL);

        Button gioca = bottone("Continua");
        Button selezioneScenario = bottone("Scenari");

        VBox pannello = pannelloMenu();
        pannello.getChildren().addAll(titolo, nota, gioca, selezioneScenario);

        Rectangle ombra = new Rectangle(MotoreGioco.LARGHEZZA_MONDO, MotoreGioco.ALTEZZA_MONDO);
        ombra.setFill(Color.rgb(0, 0, 0, 0.62));
        StackPane overlay = new StackPane(ombra, pannello);

        gioca.setOnAction(ignored -> {
            radice.getChildren().remove(overlay);
            pausaAperta[0] = false;
            vistaGioco.riprendi();
            vistaGioco.requestFocus();
        });

        selezioneScenario.setOnAction(ignored -> {
            vistaGioco.ferma();
            motoreGioco.annullaScenarioSenzaProgressi();
            mostraMenuScenari();
        });

        radice.getChildren().add(overlay);
    }

    private void mostraSchermataEsito() {
        boolean vittoria = motoreGioco.getFase() == FaseGioco.VITTORIA;
        ScenarioGioco scenarioConcluso = motoreGioco.getScenarioCorrente();
        String messaggioFinale = motoreGioco.getUltimoMessaggio();
        if (vittoria) {
            motoreGioco.salva();
        }

        Text titolo = testo(vittoria ? "Vittoria" : "Sconfitta", 56, FontWeight.EXTRA_BOLD);
        titolo.setFill(vittoria ? Color.rgb(80, 220, 255) : Color.rgb(255, 70, 82));
        Text messaggio = testo(messaggioFinale, 18, FontWeight.NORMAL);

        Button restart = bottone("Riprova");
        restart.setOnAction(ignored -> {
            if (!vittoria) {
                motoreGioco.annullaScenarioSenzaProgressi();
            }
            mostraGioco(scenarioConcluso);
        });
        Button selezioneScenario = bottone("Scenari");
        selezioneScenario.setOnAction(ignored -> {
            if (vittoria) {
                motoreGioco.confermaVittoriaScenario();
            } else {
                motoreGioco.annullaScenarioSenzaProgressi();
            }
            mostraMenuScenari();
        });
        Button menuPrincipale = bottone("Menu principale");
        menuPrincipale.setOnAction(ignored -> {
            if (vittoria) {
                motoreGioco.confermaVittoriaScenario();
            } else {
                motoreGioco.annullaScenarioSenzaProgressi();
            }
            mostraMenuPrincipale();
        });

        VBox pannello = pannelloMenu();
        pannello.getChildren().addAll(titolo, messaggio);
        if (!vittoria) {
            pannello.getChildren().add(restart);
        }
        pannello.getChildren().addAll(selezioneScenario, menuPrincipale);
        impostaScena(schermataConSfondo(pannello));
    }

    private StackPane schermataConSfondo(Parent contenuto) {
        StackPane radice = new StackPane();
        Image immagine = archivioSprite.iconaSoulSword();
        ImageView sfondo = new ImageView(immagine);
        sfondo.setFitWidth(MotoreGioco.LARGHEZZA_MONDO);
        sfondo.setFitHeight(MotoreGioco.ALTEZZA_MONDO);
        sfondo.setPreserveRatio(false);
        sfondo.setSmooth(false);

        Rectangle ombra = new Rectangle(MotoreGioco.LARGHEZZA_MONDO, MotoreGioco.ALTEZZA_MONDO);
        ombra.setFill(Color.rgb(0, 0, 0, 0.58));

        radice.getChildren().addAll(sfondo, ombra, contenuto);
        return radice;
    }

    private VBox pannelloMenu() {
        VBox pannello = new VBox(18);
        pannello.setAlignment(Pos.CENTER);
        pannello.setPadding(new Insets(36));
        pannello.setMaxWidth(620);
        pannello.setStyle("-fx-background-color: rgba(8, 10, 16, 0.78); -fx-background-radius: 18;");
        return pannello;
    }

    private Button bottone(String testo) {
        Button bottone = new Button(testo);
        bottone.setMinWidth(250);
        bottone.setMinHeight(44);
        bottone.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
        bottone.setStyle("-fx-background-color: #0f2230; -fx-text-fill: white;"
                + " -fx-border-color: #54d7ff; -fx-border-radius: 8; -fx-background-radius: 8;"
                + " -fx-cursor: hand;");
        return bottone;
    }

    private Color coloreSoulSword(EvoluzioneSoulSword evoluzione) {
        if (evoluzione == EvoluzioneSoulSword.LAMA_DI_SANGUE) {
            return Color.rgb(176, 96, 255);
        }
        if (evoluzione == EvoluzioneSoulSword.MIETITRICE_DELL_ALBA) {
            return Color.rgb(235, 190, 70);
        }
        return Color.WHITE;
    }

    private Text testo(String contenuto, int dimensione, FontWeight peso) {
        Text testo = new Text(contenuto);
        testo.setFill(Color.WHITE);
        testo.setFont(Font.font("SansSerif", peso, dimensione));
        return testo;
    }

    private void impostaScena(Parent radice) {
        Scene scena = new Scene(radice, MotoreGioco.LARGHEZZA_MONDO, MotoreGioco.ALTEZZA_MONDO);
        finestra.setScene(scena);
    }
}
