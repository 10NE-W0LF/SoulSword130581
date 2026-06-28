package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import it.unicam.cs.mpgc.rpg130581.motore.Posizione;

/**
 * Dati necessari per disegnare l'animazione di morte di un vampiro appena eliminato.
 * La posizione viene copiata per non dipendere piu' dall'oggetto rimosso dal mondo.
 */
class AnimazioneMorte {

    private final Vampiro vampiro;
    private final Posizione posizione;
    private final int dimensione;
    private final int riga;
    private final long inizioNanos;

    AnimazioneMorte(Vampiro vampiro, Posizione posizione, int dimensione, int riga, long inizioNanos) {
        this.vampiro = vampiro;
        this.posizione = posizione;
        this.dimensione = dimensione;
        this.riga = riga;
        this.inizioNanos = inizioNanos;
    }

    Vampiro getVampiro() {
        return vampiro;
    }

    Posizione getPosizione() {
        return posizione;
    }

    int getDimensione() {
        return dimensione;
    }

    int getRiga() {
        return riga;
    }

    long getInizioNanos() {
        return inizioNanos;
    }
}
