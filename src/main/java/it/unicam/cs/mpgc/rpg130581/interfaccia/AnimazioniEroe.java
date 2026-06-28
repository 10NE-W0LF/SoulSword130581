package it.unicam.cs.mpgc.rpg130581.interfaccia;

/**
 * Memorizza lo stato grafico dell'eroe.
 * RenderGioco usa questi dati per scegliere animazione e direzione dello sprite.
 */
class AnimazioniEroe {

    private boolean inMovimento;
    private boolean corre;
    private double direzioneX;
    private double direzioneY = 1;
    private long attaccoInizioNanos;
    private long attaccoFineNanos;

    // Aggiorna movimento e direzione, mantenendo l'ultima direzione valida quando l'eroe e' fermo.
    void aggiornaMovimento(boolean inMovimento, boolean corre, double direzioneX, double direzioneY) {
        this.inMovimento = inMovimento;
        this.corre = corre;
        if (inMovimento) {
            this.direzioneX = direzioneX;
            this.direzioneY = direzioneY;
        }
    }

    // Registra la finestra temporale in cui deve essere mostrata l'animazione d'attacco.
    void registraAttacco(long inizioNanos, long fineNanos) {
        this.attaccoInizioNanos = inizioNanos;
        this.attaccoFineNanos = fineNanos;
    }

    boolean isInMovimento() {
        return inMovimento;
    }

    boolean isCorre() {
        return corre;
    }

    double getDirezioneX() {
        return direzioneX;
    }

    double getDirezioneY() {
        return direzioneY;
    }

    long getAttaccoInizioNanos() {
        return attaccoInizioNanos;
    }

    long getAttaccoFineNanos() {
        return attaccoFineNanos;
    }
}
