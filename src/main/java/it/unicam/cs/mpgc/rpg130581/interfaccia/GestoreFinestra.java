package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Gestisce dimensioni, scala e schermo intero, indipendentemente dalla schermata mostrata. */
final class GestoreFinestra {
    private final Stage finestra;
    private boolean f11Premuto;

    GestoreFinestra(Stage finestra) {
        this.finestra = finestra;
        finestra.setResizable(false);
        finestra.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        finestra.setFullScreenExitHint("");
        finestra.focusedProperty().addListener((proprieta, prima, adesso) -> {
            if (!adesso) {
                f11Premuto = false;
            }
        });
    }

    void alternaSchermoIntero() {
        finestra.setFullScreen(!finestra.isFullScreen());
    }

    Scene mostra(StackPane radice) {
        radice.setMinSize(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        radice.setPrefSize(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        radice.setMaxSize(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        StackPane contenitore = new StackPane(new Group(radice));
        contenitore.getStyleClass().add("viewport");
        NumberBinding scala = Bindings.min(
                contenitore.widthProperty().divide(MotoreGioco.larghezzaMondo),
                contenitore.heightProperty().divide(MotoreGioco.altezzaMondo));
        radice.scaleXProperty().bind(scala);
        radice.scaleYProperty().bind(scala);
        Scene scena = finestra.getScene();
        if (scena == null) {
            scena = new Scene(contenitore, MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
            scena.getStylesheets().add(GestoreFinestra.class.getResource("/stili/interfaccia.css").toExternalForm());
            scena.addEventFilter(KeyEvent.KEY_PRESSED, evento -> {
                if (evento.getCode() == KeyCode.F11) {
                    if (!f11Premuto) {
                        f11Premuto = true;
                        alternaSchermoIntero();
                    }
                    evento.consume();
                }
            });
            scena.addEventFilter(KeyEvent.KEY_RELEASED, evento -> {
                if (evento.getCode() == KeyCode.F11) {
                    f11Premuto = false;
                    evento.consume();
                }
            });
            finestra.setScene(scena);
        } else {
            scena.setOnKeyPressed(null);
            scena.setOnKeyReleased(null);
            scena.setRoot(contenitore);
        }
        return scena;
    }
}
