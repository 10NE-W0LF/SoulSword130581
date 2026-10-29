package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Function;

/** Vista della scelta lingua: delega conferma e navigazione senza conoscere la persistenza. */
final class MenuLingua extends VBox {
    MenuLingua(TestiInterfaccia testi, Function<Lingua, Boolean> salva, Runnable indietro) {
        super(18);
        setAlignment(Pos.CENTER);
        setMaxWidth(460);
        setMaxHeight(USE_PREF_SIZE);
        getStyleClass().add("menu-lingua");
        getStylesheets().add(MenuLingua.class.getResource("/stili/menu-lingua.css").toExternalForm());

        Label titolo = etichetta(testi.testo("menu.lingua"), "titolo-lingua");
        Label descrizione = etichetta(testi.testo("lingua.descrizione"), "nota-lingua");
        ToggleGroup gruppo = new ToggleGroup();
        VBox scelte = new VBox(12);
        scelte.setAlignment(Pos.CENTER);
        for (Lingua lingua : Lingua.values()) {
            ToggleButton scelta = new ToggleButton(lingua.toString());
            scelta.setUserData(lingua);
            scelta.setToggleGroup(gruppo);
            scelta.setSelected(lingua == testi.getLingua());
            scelta.getStyleClass().add("scelta-lingua");
            // Deve restare selezionata esattamente una lingua, anche con tastiera.
            scelta.setOnAction(ignored -> scelta.setSelected(true));
            scelte.getChildren().add(scelta);
        }

        Label nota = etichetta(testi.testo("lingua.notaSalvataggio"), "nota-lingua");
        Label errore = etichetta(testi.testo("lingua.erroreSalvataggio"), "errore-lingua");
        errore.setVisible(false);
        errore.setManaged(false);

        Button conferma = new Button(testi.testo("lingua.salva"));
        conferma.getStyleClass().add("salva-lingua");
        conferma.setOnAction(ignored -> {
            Lingua selezionata = (Lingua) gruppo.getSelectedToggle().getUserData();
            if (!salva.apply(selezionata)) {
                errore.setVisible(true);
                errore.setManaged(true);
            }
        });

        Button torna = new Button(testi.testo("lingua.indietro"));
        torna.getStyleClass().add("indietro-lingua");
        torna.setOnAction(ignored -> indietro.run());
        HBox azioni = new HBox(12, torna, conferma);
        azioni.setAlignment(Pos.CENTER);

        getChildren().addAll(titolo, descrizione, scelte, nota, azioni, errore);
    }

    private Label etichetta(String testo, String stile) {
        Label etichetta = new Label(testo);
        etichetta.getStyleClass().add(stile);
        etichetta.setWrapText(true);
        etichetta.setMaxWidth(340);
        etichetta.setAlignment(Pos.CENTER);
        etichetta.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        return etichetta;
    }
}
