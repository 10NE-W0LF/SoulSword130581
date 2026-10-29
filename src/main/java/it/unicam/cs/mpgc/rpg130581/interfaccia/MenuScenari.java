package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.applicazione.EsitoSalvataggio;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static it.unicam.cs.mpgc.rpg130581.interfaccia.ComponentiMenu.*;

/** Presenta eroe e scenari, delegando al coordinatore tutte le modifiche alla partita. */
final class MenuScenari extends HBox {
    MenuScenari(StatoPartita stato, ArchivioSprite immagini, TestiInterfaccia testi,
                Consumer<ScenarioGioco> avvia, Runnable forza, Runnable salute,
                Supplier<EsitoSalvataggio> salva, Runnable menu) {
        setAlignment(Pos.CENTER);
        getStyleClass().add("menu-scenari");
        Eroe eroe = stato.getEroe();
        VBox scheda = schedaEroe(eroe, immagini, testi, forza, salute);
        VBox elenco = new VBox();
        elenco.setAlignment(Pos.CENTER_LEFT);
        elenco.getStyleClass().add("elenco-scenari");
        for (ScenarioGioco scenario : ScenarioGioco.values()) {
            boolean sbloccato = stato.isScenarioSbloccato(scenario);
            Button scegli = bottone(scenario.getNumero() + "  " + scenario.getNomeVisualizzato()
                    + "   | " + testi.testo("scenari.nemici", scenario.getNemiciRegolari())
                    + (scenario.isScenarioBoss() ? "   | " + testi.testo("scenari.boss") : "")
                    + (sbloccato ? "" : "   | " + testi.testo("scenari.bloccato")), () -> avvia.accept(scenario));
            scegli.setMaxWidth(Double.MAX_VALUE);
            scegli.setDisable(!sbloccato);
            elenco.getChildren().add(scegli);
        }
        Text messaggio = testo("", "testo-dettaglio");
        Button salvaPartita = bottone(testi.testo("scenari.salva"),
                () -> messaggio.setText(testi.salvataggio(salva.get())));
        VBox destra = new VBox(testo(testi.testo("scenari.titolo"), "titolo-scenari"),
                elenco, salvaPartita, messaggio, bottone(testi.testo("menu.principale"), menu));
        destra.setAlignment(Pos.CENTER_LEFT);
        destra.getStyleClass().add("scheda-scenari");
        getChildren().addAll(scheda, destra);
    }

    private VBox schedaEroe(Eroe eroe, ArchivioSprite immagini, TestiInterfaccia testi,
                            Runnable forza, Runnable salute) {
        ImageView immagine = new ImageView(immagini.eroe(eroe.getEvoSoulSword(), TipoAnimazione.idle));
        immagine.setViewport(new Rectangle2D(0, 0, 64, 64));
        immagine.setFitWidth(170);
        immagine.setFitHeight(170);
        immagine.setPreserveRatio(true);
        immagine.setSmooth(false);
        String evoluzione = "evoluzione-" + eroe.getEvoSoulSword().name();
        Button aumentaForza = bottone(testi.testo("eroe.aumentaForza"), forza);
        Button aumentaSalute = bottone(testi.testo("eroe.aumentaSalute"), salute);
        for (Button bottone : new Button[]{aumentaForza, aumentaSalute}) {
            bottone.getStyleClass().add("bottone-statistica");
            bottone.setDisable(eroe.getStats() <= 0);
        }
        HBox comandi = new HBox(8, aumentaForza, aumentaSalute);
        comandi.setAlignment(Pos.CENTER);
        VBox scheda = new VBox(immagine, testo(eroe.getNome(), "nome-eroe"),
                testo(testi.testo("eroe.livello", eroe.getLivello()), "testo-eroe", evoluzione),
                testo(testi.nome(eroe.getEvoSoulSword()), "testo-eroe", evoluzione),
                testo(testi.testo("eroe.stats", eroe.getStats()), "testo-stats"),
                testo(testi.testo("eroe.forza", eroe.getForza(), eroe.getForza() * Eroe.bonusForza), "testo-dettaglio"),
                testo(testi.testo("eroe.salute", eroe.getSalute(), eroe.getSalute() * Eroe.bonusSalute), "testo-dettaglio"), comandi);
        scheda.setAlignment(Pos.CENTER);
        scheda.getStyleClass().add("scheda-eroe");
        return scheda;
    }
}
