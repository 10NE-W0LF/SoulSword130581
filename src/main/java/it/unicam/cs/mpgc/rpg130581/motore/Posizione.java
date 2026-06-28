package it.unicam.cs.mpgc.rpg130581.motore;

/**
 * Rappresenta una coordinata nel mondo di gioco.
 * Fornisce operazioni semplici per distanza e movimento entro i limiti dell'area.
 */
public class Posizione {

    private double x;
    private double y;

    public Posizione(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calcola la distanza da un'altra posizione.
     *
     * @param altra posizione di destinazione
     * @return distanza euclidea tra le due posizioni
     */
    public double distanzaDa(Posizione altra) {
        double dx = x - altra.x;
        double dy = y - altra.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Muove la posizione impedendo di uscire dai bordi del mondo.
     *
     * @param dx spostamento orizzontale
     * @param dy spostamento verticale
     * @param massimoX limite massimo orizzontale
     * @param massimoY limite massimo verticale
     */
    public void muovi(double dx, double dy, int massimoX, int massimoY) {
        x = Math.max(0, Math.min(massimoX, x + dx));
        y = Math.max(0, Math.min(massimoY, y + dy));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
