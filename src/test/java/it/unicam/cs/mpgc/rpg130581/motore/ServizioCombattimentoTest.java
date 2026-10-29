package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.partita.TipoEventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ServizioCombattimentoTest {
    private ServizioCombattimento servizio(int incremento) {
        return new ServizioCombattimento(new Random() {
            @Override public int nextInt(int limite) { return Math.min(incremento, limite - 1); }
        });
    }

    @Test
    public void nemicoSopravvissutoContrattaccaSenzaAssegnareRicompense() {
        Eroe eroe = new Eroe();
        Vampiro vampiro = new Vampiro(TipoVampiro.vampiroBase, 1);
        EsitoCombattimento esito = servizio(0).attacca(eroe, vampiro);
        assertEquals(38, vampiro.getVita());
        assertEquals(98, eroe.getVita());
        assertEquals(10, esito.getDannoVampiro());
        assertFalse(esito.isVampiroSconfitto());
        assertFalse(esito.isEroeSconfitto());
        assertEquals(0, eroe.getEsperienza());
        assertEquals(0, eroe.getVSconfitti());
        assertEquals(TipoEventoGioco.colpoScambiato, esito.getEvento().tipo());
    }

    @Test
    public void colpoMortaleAssegnaRicompensaSenzaContrattacco() {
        Eroe eroe = new Eroe();
        Vampiro vampiro = new Vampiro(TipoVampiro.vampiroBase, 1);
        vampiro.subisciDanno(vampiro.getVita() - 1);
        EsitoCombattimento esito = servizio(0).attacca(eroe, vampiro);
        assertTrue(esito.isVampiroSconfitto());
        assertFalse(esito.isEroeSconfitto());
        assertEquals(0, esito.getDannoVampiro());
        assertEquals(108, eroe.getVita());
        assertEquals(35, eroe.getEsperienza());
        assertEquals(1, eroe.getVSconfitti());
        assertEquals(0, vampiro.getVita());
    }

    @Test
    public void contrattaccoMortaleSegnalaLaSconfittaDellEroe() {
        Eroe eroe = new Eroe(1, 0, 0, 1, 0, 0, 0, 0);
        EsitoCombattimento esito = servizio(0).attacca(eroe, new Vampiro(TipoVampiro.vampiroBase, 1));
        assertTrue(esito.isEroeSconfitto());
        assertFalse(esito.isVampiroSconfitto());
        assertEquals(0, eroe.getVita());
        assertEquals(0, eroe.getVSconfitti());
    }

    @Test
    public void evoluzioneDellaSpadaRiduceIlDannoDelContrattacco() {
        Eroe prima = new Eroe(9, 0, 0, 1000, 0, 0, 0, 0);
        Eroe dopo = new Eroe(10, 0, 0, 1000, 0, 0, 0, 0);
        int dannoPrima = servizio(0).attacca(prima, new Vampiro(TipoVampiro.arciduca, 20)).getDannoVampiro();
        int dannoDopo = servizio(0).attacca(dopo, new Vampiro(TipoVampiro.arciduca, 20)).getDannoVampiro();
        assertEquals(3, dannoPrima - dannoDopo);
    }

    @Test
    public void variabilitaCasualeRestaNeiLimitiPrevisti() {
        Eroe eroe = new Eroe();
        Vampiro vampiro = new Vampiro(TipoVampiro.vampiroBase, 1);
        EsitoCombattimento esito = servizio(99).attacca(eroe, vampiro);
        assertEquals(29, vampiro.getVita()); // 54 - (16 + 9).
        assertEquals(15, esito.getDannoVampiro());
        assertEquals(93, eroe.getVita());
    }

    @Test
    public void sconfittaDelBossAssegnaLaRicompensaFinale() {
        Eroe eroe = new Eroe(20, 0, 0, 1000, 0, 0, 0, 0);
        Vampiro boss = new Vampiro(TipoVampiro.arciduca, 20);
        boss.subisciDanno(boss.getVita() - 1);
        EsitoCombattimento esito = servizio(0).attacca(eroe, boss);
        assertTrue(esito.isVampiroSconfitto());
        assertEquals(782, eroe.getEsperienza());
        assertEquals(1, eroe.getVSconfitti());
        assertEquals(TipoEventoGioco.bossSconfitto, esito.getEvento().tipo());
    }
}
