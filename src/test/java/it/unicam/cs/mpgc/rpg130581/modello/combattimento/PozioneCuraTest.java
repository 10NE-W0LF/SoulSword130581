package it.unicam.cs.mpgc.rpg130581.modello.combattimento;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PozioneCuraTest {
    @Parameterized.Parameters(name = "estrazione {0}: drop {1}")
    public static Object[][] soglie() {
        return new Object[][]{{0, true}, {13, true}, {14, false}, {99, false}};
    }

    private final int estrazione;
    private final boolean drop;

    public PozioneCuraTest(int estrazione, boolean drop) {
        this.estrazione = estrazione;
        this.drop = drop;
    }

    @Test
    public void dropRispettaLaSogliaDelQuattordiciPerCento() {
        Random controllato = new Random() {
            @Override
            public int nextInt(int limite) {
                assertEquals(100, limite);
                return estrazione;
            }
        };
        assertEquals(drop, PozioneCura.esceDalDrop(controllato));
    }
}
