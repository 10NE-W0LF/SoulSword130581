package it.unicam.cs.mpgc.rpg130581.motore;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PosizioneTest {
    @Test
    public void distanzaEuclideaESimmetrica() {
        Posizione prima = new Posizione(1, 2);
        Posizione seconda = new Posizione(4, 6);
        assertEquals(5, prima.distanzaDa(seconda), 0.000001);
        assertEquals(5, seconda.distanzaDa(prima), 0.000001);
        assertEquals(0, prima.distanzaDa(prima), 0.000001);
    }

    @Test
    public void movimentoMantieneLeCoordinateDecimali() {
        Posizione posizione = new Posizione(10, 20);
        posizione.muovi(1.45, -1.95, 100, 100);
        assertEquals(11.45, posizione.getX(), 0.000001);
        assertEquals(18.05, posizione.getY(), 0.000001);
    }

    @Test
    public void movimentoNonSuperaIBordiInferioriOSuperiori() {
        Posizione posizione = new Posizione(10, 20);
        posizione.muovi(-1000, 1000, 880, 540);
        assertEquals(0, posizione.getX(), 0);
        assertEquals(540, posizione.getY(), 0);
        posizione.muovi(1000, -1000, 880, 540);
        assertEquals(880, posizione.getX(), 0);
        assertEquals(0, posizione.getY(), 0);
    }
}
