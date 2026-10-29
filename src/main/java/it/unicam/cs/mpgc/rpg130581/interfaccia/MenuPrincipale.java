package it.unicam.cs.mpgc.rpg130581.interfaccia;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static it.unicam.cs.mpgc.rpg130581.interfaccia.ComponentiMenu.*;

/** Vista del menu iniziale; le azioni sono fornite dal coordinatore. */
final class MenuPrincipale extends VBox {
    MenuPrincipale(TestiInterfaccia testi, ReadOnlyBooleanProperty schermoIntero,
                   Runnable nuova, Runnable carica, Runnable alternaSchermo,
                   Runnable lingua, Runnable esci, String avviso) {
        configuraPannello(this);
        Button schermo = bottone("", alternaSchermo);
        schermo.textProperty().bind(Bindings.when(schermoIntero)
                .then(testi.testo("menu.finestra")).otherwise(testi.testo("menu.schermoIntero")));
        getChildren().addAll(testo("SoulSword", "titolo-principale"),
                testo(testi.testo("menu.sottotitolo")),
                bottone(testi.testo("menu.nuovaPartita"), nuova),
                bottone(testi.testo("menu.caricaPartita"), carica), schermo,
                bottone(testi.testo("menu.lingua"), lingua), bottone(testi.testo("menu.esci"), esci));
        if (!avviso.isBlank()) {
            getChildren().add(testo(avviso, "testo-dettaglio"));
        }
    }
}
