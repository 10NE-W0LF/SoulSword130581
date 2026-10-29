package it.unicam.cs.mpgc.rpg130581.modello.partita;

/** Esiti del gioco indipendenti dalla lingua e dalla tecnologia dell'interfaccia. */
public enum TipoEventoGioco {
    attesaCaccia, nuovaPartita, scenarioBloccato, scenarioAvviato, scenarioInterrotto,
    nessunBersaglio, colpoScambiato, vampiroSconfitto, bossSconfitto, eroeSconfitto,
    notteVincente, pozioneUsata, pozioniEsaurite, vitaMassima, partitaSalvata,
    salvataggioFallito, partitaCaricata, caricamentoFallito, bossEvocato,
    pozioneRilasciata, vittoriaFinale, vittoriaScenario, colpoSubito, gameOver
}
