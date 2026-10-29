package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import org.junit.Test;

import static org.junit.Assert.*;

/** Le regole geometriche non richiedono l'avvio del toolkit JavaFX. */
public class RenderGiocoTest {
    private final RenderGioco renderer = new RenderGioco(null, new ArchivioSprite(), new TestiInterfaccia());

    @Test
    public void direzioniCardinaliUsanoLeRigheCorretteDegliSprite() {
        assertEquals(0, renderer.rigaDaDirezione(0, 1));
        assertEquals(1, renderer.rigaDaDirezione(-1, 0));
        assertEquals(2, renderer.rigaDaDirezione(1, 0));
        assertEquals(3, renderer.rigaDaDirezione(0, -1));
    }

    @Test
    public void diagonaliPrivilegianoLAsseDominanteEIlVerticaleInCasoDiParita() {
        assertEquals(2, renderer.rigaDaDirezione(2, -1));
        assertEquals(1, renderer.rigaDaDirezione(-2, 1));
        assertEquals(3, renderer.rigaDaDirezione(1, -1));
        assertEquals(0, renderer.rigaDaDirezione(0, 0));
    }

    @Test
    public void bossOccupaPiuSpazioDeiNemiciRegolari() {
        int base = renderer.dimensioneNemico(new Vampiro(TipoVampiro.vampiroBase, 5));
        int elite = renderer.dimensioneNemico(new Vampiro(TipoVampiro.vampiroElite, 10));
        int boss = renderer.dimensioneNemico(new Vampiro(TipoVampiro.arciduca, 20));
        assertEquals(base, elite);
        assertTrue(boss > base);
    }
}
