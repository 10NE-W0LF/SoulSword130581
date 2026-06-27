package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class BarraComandi extends HBox {

    public BarraComandi(MotoreGioco motoreGioco, VistaGioco vistaGioco) {
        setPadding(new Insets(8));
        setSpacing(8);
        setStyle("-fx-background-color: #15191a;");

        Button salva = new Button("Salva");
        Button carica = new Button("Carica");
        Button boss = new Button("Boss");

        salva.setOnAction(event -> {
            motoreGioco.salva();
            vistaGioco.requestFocus();
            vistaGioco.disegna();
        });
        carica.setOnAction(event -> {
            motoreGioco.carica();
            vistaGioco.requestFocus();
            vistaGioco.disegna();
        });
        boss.setOnAction(event -> {
            motoreGioco.evocaBoss();
            vistaGioco.requestFocus();
            vistaGioco.disegna();
        });

        getChildren().addAll(salva, carica, boss);
    }
}

