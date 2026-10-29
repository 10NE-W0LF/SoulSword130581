package it.unicam.cs.mpgc.rpg130581.interfaccia;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnimazioniEroeTest {
    @Test
    public void fermarsiConservaLaDirezioneDelloSprite() {
        AnimazioniEroe animazioni = new AnimazioniEroe();
        animazioni.aggiornaMovimento(true, false, -1, 0);
        animazioni.aggiornaMovimento(false, false, 0, 0);
        assertFalse(animazioni.isInMovimento());
        assertFalse(animazioni.isCorre());
        assertEquals(-1, animazioni.getDirezioneX(), 0);
        assertEquals(0, animazioni.getDirezioneY(), 0);
    }

    @Test
    public void camminoECorsaAggiornanoStatoEDirezione() {
        AnimazioniEroe animazioni = new AnimazioniEroe();
        animazioni.aggiornaMovimento(true, true, 1, -1);
        assertTrue(animazioni.isInMovimento());
        assertTrue(animazioni.isCorre());
        assertEquals(1, animazioni.getDirezioneX(), 0);
        assertEquals(-1, animazioni.getDirezioneY(), 0);
        animazioni.aggiornaMovimento(true, false, 0, 1);
        assertFalse(animazioni.isCorre());
        assertEquals(0, animazioni.getDirezioneX(), 0);
        assertEquals(1, animazioni.getDirezioneY(), 0);
    }

    @Test
    public void nuovoAttaccoSostituisceLaFinestraDiAnimazionePrecedente() {
        AnimazioniEroe animazioni = new AnimazioniEroe();
        animazioni.registraAttacco(100, 660);
        animazioni.registraAttacco(500, 1060);
        assertEquals(500, animazioni.getAttaccoInizioNanos());
        assertEquals(1060, animazioni.getAttaccoFineNanos());
    }
}
