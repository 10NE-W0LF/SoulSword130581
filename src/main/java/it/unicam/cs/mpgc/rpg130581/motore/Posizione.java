package it.unicam.cs.mpgc.rpg130581.motore;

public class Posizione {

    private double x;
    private double y;

    public Posizione(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanzaDa(Posizione altra) {
        double dx = x - altra.x;
        double dy = y - altra.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

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

