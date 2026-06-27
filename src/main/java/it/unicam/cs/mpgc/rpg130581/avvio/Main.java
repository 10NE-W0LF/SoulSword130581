package it.unicam.cs.mpgc.rpg130581.avvio;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.interfaccia.AppSoulSword;
import it.unicam.cs.mpgc.rpg130581.persistenza.Salvataggi;
import javafx.application.Application;

import java.nio.file.Paths;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Salvataggi salvataggi = new Salvataggi(Paths.get("data", "save.json"));
        AppSoulSword.setMotoreGioco(new MotoreGioco(salvataggi));
        Application.launch(AppSoulSword.class, args);
    }
}
