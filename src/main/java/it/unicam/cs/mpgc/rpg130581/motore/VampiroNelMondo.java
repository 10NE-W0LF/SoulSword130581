package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

public class VampiroNelMondo {

    private final Vampiro vampiro;
    private final Posizione posizione;
    private double ultimoDx;
    private double ultimoDy = 1;
    private long ultimoAttaccoMillis;
    private long ultimoDannoMillis;

    public VampiroNelMondo(Vampiro vampiro, Posizione posizione) {
        this.vampiro = vampiro;
        this.posizione = posizione;
    }

    public Vampiro getVampiro() {
        return vampiro;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void registraMovimento(double dx, double dy) {
        if (Math.abs(dx) > 0.001 || Math.abs(dy) > 0.001) {
            ultimoDx = dx;
            ultimoDy = dy;
        }
    }

    public double getUltimoDx() {
        return ultimoDx;
    }

    public double getUltimoDy() {
        return ultimoDy;
    }

    public boolean puoAttaccare(long adessoMillis, long tempoRicaricaMillis) {
        return adessoMillis - ultimoAttaccoMillis >= tempoRicaricaMillis;
    }

    public void registraAttacco(long adessoMillis) {
        ultimoAttaccoMillis = adessoMillis;
    }

    public long getUltimoAttaccoMillis() {
        return ultimoAttaccoMillis;
    }

    public void registraDanno(long adessoMillis) {
        ultimoDannoMillis = adessoMillis;
    }

    public long getUltimoDannoMillis() {
        return ultimoDannoMillis;
    }
}
