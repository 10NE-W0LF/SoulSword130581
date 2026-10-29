package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EvoSoulSwordTest {
    @Parameterized.Parameters(name = "livello {0}: {1}")
    public static Object[][] livelli() {
        return new Object[][]{
                {1, EvoSoulSword.risvegliata}, {9, EvoSoulSword.risvegliata},
                {10, EvoSoulSword.lamaDiSangue}, {19, EvoSoulSword.lamaDiSangue},
                {20, EvoSoulSword.mietitriceDellAlba}, {30, EvoSoulSword.mietitriceDellAlba}
        };
    }

    private final int livello;
    private final EvoSoulSword attesa;

    public EvoSoulSwordTest(int livello, EvoSoulSword attesa) {
        this.livello = livello;
        this.attesa = attesa;
    }

    @Test
    public void evoluzioneCambiaSoloAlleSogliePreviste() {
        assertEquals(attesa, EvoSoulSword.daLivello(livello));
        assertEquals(attesa, new Eroe(livello, 0, 0, 100, 0, 0, 0, 0).getEvoSoulSword());
    }
}
