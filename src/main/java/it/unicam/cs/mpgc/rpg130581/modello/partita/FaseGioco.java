package it.unicam.cs.mpgc.rpg130581.modello.partita;

/**
 * Fasi principali attraversate dal gioco.
 * Il motore le usa per decidere quali azioni sono permesse in ogni momento.
 */
public enum FaseGioco {
    selezioneScenario,
    inGioco,
    vittoria,
    gameOver
}
