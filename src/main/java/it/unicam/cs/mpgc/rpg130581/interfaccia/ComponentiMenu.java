package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/** Componenti grafici comuni. Colori, font, bordi e spaziature sono definiti nel CSS. */
final class ComponentiMenu {
    private ComponentiMenu() {
    }

    static Text testo(String contenuto, String... stili) {
        Text testo = new Text(contenuto);
        testo.getStyleClass().add("testo-menu");
        testo.getStyleClass().addAll(stili);
        return testo;
    }

    static Button bottone(String testo, Runnable azione) {
        Button bottone = new Button(testo);
        bottone.getStyleClass().add("bottone-menu");
        bottone.setOnAction(ignored -> azione.run());
        return bottone;
    }

    static void configuraPannello(VBox pannello) {
        pannello.setAlignment(Pos.CENTER);
        pannello.getStyleClass().add("pannello-menu");
    }

    static StackPane conSfondo(Parent contenuto, ArchivioSprite immagini) {
        ImageView sfondo = new ImageView(immagini.iconaSoulSword());
        sfondo.setFitWidth(MotoreGioco.larghezzaMondo);
        sfondo.setFitHeight(MotoreGioco.altezzaMondo);
        sfondo.setPreserveRatio(false);
        sfondo.setSmooth(false);
        Rectangle ombra = new Rectangle(MotoreGioco.larghezzaMondo, MotoreGioco.altezzaMondo);
        ombra.getStyleClass().add("ombra-sfondo");
        return new StackPane(sfondo, ombra, contenuto);
    }
}
