package it.unicam.cs.mpgc.rpg130581.avvio;

import javafx.application.Application;

/**
 * Punto di ingresso del progetto.
 * Avvia il ciclo di vita JavaFX; la composizione delle dipendenze è in AvvioJavaFx.
 */
public final class Main {

    private Main() {
    }

    /**
     * Configura le dipendenze principali dell'applicazione prima dell'avvio grafico.
     *
     * @param args argomenti ricevuti dalla riga di comando
     */
    public static void main(String[] args) {
        Application.launch(AvvioJavaFx.class, args);
    }
}
