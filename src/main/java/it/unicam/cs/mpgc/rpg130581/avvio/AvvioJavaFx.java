package it.unicam.cs.mpgc.rpg130581.avvio;

import it.unicam.cs.mpgc.rpg130581.interfaccia.AppSoulSword;
import it.unicam.cs.mpgc.rpg130581.motore.GeneratoreV;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.motore.ServizioCombattimento;
import it.unicam.cs.mpgc.rpg130581.persistenza.Salvataggi;
import it.unicam.cs.mpgc.rpg130581.persistenza.SalvataggiPreferenze;
import javafx.application.Application;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.time.Clock;
import java.util.random.RandomGenerator;

/** Punto di composizione: solo l'avvio decide quali implementazioni concrete utilizzare. */
public final class AvvioJavaFx extends Application {
    private AppSoulSword applicazione;

    @Override
    public void start(Stage finestra) {
        RandomGenerator random = RandomGenerator.getDefault();
        MotoreGioco motore = new MotoreGioco(new Salvataggi(Path.of("data", "save.json")),
                new GeneratoreV(random), new ServizioCombattimento(random), random, Clock.systemUTC());
        applicazione = new AppSoulSword(motore,
                new SalvataggiPreferenze(Path.of("data", "preferenze.properties")));
        applicazione.start(finestra);
    }

    @Override
    public void stop() {
        if (applicazione != null) {
            applicazione.ferma();
        }
    }
}
