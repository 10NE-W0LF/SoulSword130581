package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import org.junit.Test;

import static org.junit.Assert.*;

public class VampiroNelMondoTest {
    private VampiroNelMondo crea() {
        return new VampiroNelMondo(new Vampiro(TipoVampiro.vampiroBase, 1), new Posizione(30, 40));
    }

    @Test
    public void fermarsiConservaLUltimaDirezioneUtile() {
        VampiroNelMondo vampiro = crea();
        vampiro.registraMovimento(-2, 1);
        vampiro.registraMovimento(0, 0);
        vampiro.registraMovimento(0.001, -0.001);
        assertEquals(-2, vampiro.getUltimoDx(), 0);
        assertEquals(1, vampiro.getUltimoDy(), 0);
        vampiro.registraMovimento(0, -0.002);
        assertEquals(0, vampiro.getUltimoDx(), 0);
        assertEquals(-0.002, vampiro.getUltimoDy(), 0);
    }

    @Test
    public void attaccoDiventaDisponibileEsattamenteAllaFineDellaRicarica() {
        VampiroNelMondo vampiro = crea();
        vampiro.registraAttacco(10000);
        assertFalse(vampiro.puoAttaccare(11149, 1150));
        assertTrue(vampiro.puoAttaccare(11150, 1150));
        assertTrue(vampiro.puoAttaccare(12000, 1150));
    }

    @Test
    public void dannoSubitoNonRiavviaLaRicaricaDiAttacco() {
        VampiroNelMondo vampiro = crea();
        vampiro.registraAttacco(10000);
        vampiro.registraDanno(11000);
        assertEquals(11000, vampiro.getUltimoDannoMillis());
        assertEquals(10000, vampiro.getUltimoAttaccoMillis());
        assertTrue(vampiro.puoAttaccare(11150, 1150));
    }
}
