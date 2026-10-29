package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

public interface GenerazioneVampiri {
    Vampiro creaVampiroRegolare(ScenarioGioco scenario);
    Vampiro creaBoss();
}
