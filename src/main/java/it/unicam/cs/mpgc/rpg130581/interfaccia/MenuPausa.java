package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import static it.unicam.cs.mpgc.rpg130581.interfaccia.ComponentiMenu.*;

final class MenuPausa extends StackPane {
    MenuPausa(TestiInterfaccia testi, Runnable continua, Runnable scenari) {
        Rectangle ombra = new Rectangle(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        ombra.getStyleClass().add("ombra-pausa");
        VBox pannello = new VBox(testo(testi.testo("pausa.titolo"), "titolo-pausa"),
                testo(testi.testo("pausa.nota"), "nota-menu"),
                bottone(testi.testo("pausa.continua"), continua),
                bottone(testi.testo("scenari.elenco"), scenari));
        configuraPannello(pannello);
        getChildren().addAll(ombra, pannello);
    }
}
