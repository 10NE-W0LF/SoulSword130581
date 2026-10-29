package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

/** Collega tastiera e pausa alla vista della partita, senza gestire i salvataggi. */
final class SchermataPartita extends StackPane {
    private final MotoreGioco motore;
    private final TestiInterfaccia testi;
    private final Runnable abbandona;
    private final VistaGioco vista;
    private MenuPausa pausa;

    SchermataPartita(MotoreGioco motore, TestiInterfaccia testi, Runnable fine, Runnable abbandona) {
        this.motore = motore;
        this.testi = testi;
        this.abbandona = abbandona;
        vista = new VistaGioco(motore, fine, testi);
        getChildren().add(vista);
    }

    void collegaInput(Scene scena) {
        scena.setOnKeyPressed(evento -> {
            if (pausa != null) {
                return;
            }
            if (evento.getCode() == KeyCode.SPACE) {
                vista.attacca();
            } else if (evento.getCode() == KeyCode.T) {
                motore.usaPozioneCura();
                vista.disegna();
            } else if (evento.getCode() == KeyCode.ESCAPE) {
                vista.ferma();
                pausa = new MenuPausa(testi, this::riprendi, abbandona);
                getChildren().add(pausa);
            } else {
                vista.premi(evento.getCode());
            }
        });
        scena.setOnKeyReleased(evento -> vista.rilascia(evento.getCode()));
        vista.requestFocus();
    }

    private void riprendi() {
        getChildren().remove(pausa);
        pausa = null;
        vista.riprendi();
        vista.requestFocus();
    }

    void ferma() {
        vista.ferma();
    }
}
