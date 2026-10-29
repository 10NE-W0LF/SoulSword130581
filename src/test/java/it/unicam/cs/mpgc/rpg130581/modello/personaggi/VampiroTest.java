package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class VampiroTest {
    @Parameterized.Parameters(name = "{0}")
    public static Object[][] tipi() {
        return new Object[][]{
                {TipoVampiro.vampiroBase, 102, 26, 87},
                {TipoVampiro.vampiroElite, 180, 42, 122},
                {TipoVampiro.arciduca, 478, 62, 587}
        };
    }

    private final TipoVampiro tipo;
    private final int vita;
    private final int danno;
    private final int esperienza;

    public VampiroTest(TipoVampiro tipo, int vita, int danno, int esperienza) {
        this.tipo = tipo;
        this.vita = vita;
        this.danno = danno;
        this.esperienza = esperienza;
    }

    @Test
    public void statisticheERicompenseRispettanoIlBilanciamentoDelTipo() {
        Vampiro vampiro = new Vampiro(tipo, 5);
        assertEquals(tipo, vampiro.getTipo());
        assertEquals(vita, vampiro.getVita());
        assertEquals(vita, vampiro.getVitaMassima());
        assertEquals(danno, vampiro.getPotenzaAttacco());
        assertEquals(esperienza, vampiro.getRicompensaEsperienza());
        assertFalse(vampiro.isSconfitto());
    }

    @Test
    public void livelloPiuAltoAumentaDifficoltaERicompensa() {
        Vampiro basso = new Vampiro(tipo, 5);
        Vampiro alto = new Vampiro(tipo, 6);
        assertTrue(alto.getVitaMassima() > basso.getVitaMassima());
        assertTrue(alto.getPotenzaAttacco() > basso.getPotenzaAttacco());
        assertTrue(alto.getRicompensaEsperienza() > basso.getRicompensaEsperienza());
    }
}
