package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import org.junit.Test;

import static org.junit.Assert.*;

public class EroeTest {
    @Test
    public void nuovaPartitaParteConEroeSanoSenzaProgressi() {
        Eroe eroe = new Eroe();
        assertEquals(1, eroe.getLivello());
        assertEquals(108, eroe.getVita());
        assertEquals(eroe.getVitaMassima(), eroe.getVita());
        assertEquals(0, eroe.getEsperienza());
        assertEquals(0, eroe.getStats());
        assertEquals(0, eroe.getPozioniCura());
        assertEquals(EvoSoulSword.risvegliata, eroe.getEvoSoulSword());
        assertFalse(eroe.puoSfidareBoss());
    }

    @Test
    public void caricamentoNormalizzaValoriFuoriLimite() {
        Eroe eroe = new Eroe(99, 800, -1, 10000, -3, -2, 99, 99);
        assertEquals(30, eroe.getLivello());
        assertEquals(0, eroe.getEsperienza());
        assertEquals(0, eroe.getVSconfitti());
        assertEquals(0, eroe.getForza());
        assertEquals(0, eroe.getSalute());
        assertEquals(29, eroe.getStats());
        assertEquals(3, eroe.getPozioniCura());
        assertEquals(eroe.getVitaMassima(), eroe.getVita());
        Eroe minimo = new Eroe(-1, -1, -1, -1, -1, -1, -1, -1);
        assertEquals(1, minimo.getLivello());
        assertEquals(0, minimo.getEsperienza());
        assertEquals(0, minimo.getStats());
        assertEquals(0, minimo.getPozioniCura());
        assertTrue(minimo.isSconfitto());
    }

    @Test
    public void esperienzaAllaSogliaAssegnaUnPuntoERipristinaLaVita() {
        Eroe eroe = new Eroe();
        eroe.subisciDanno(70);
        eroe.guadagnaEsperienza(54);
        assertEquals(1, eroe.getLivello());
        assertEquals(38, eroe.getVita());
        eroe.guadagnaEsperienza(1);
        assertEquals(2, eroe.getLivello());
        assertEquals(0, eroe.getEsperienza());
        assertEquals(1, eroe.getStats());
        assertEquals(116, eroe.getVita());
    }

    @Test
    public void unaRicompensaPuoAssegnarePiuLivelliConEsperienzaResidua() {
        Eroe eroe = new Eroe();
        eroe.guadagnaEsperienza(165); // Soglie: 55 e 85, residuo 25.
        assertEquals(3, eroe.getLivello());
        assertEquals(25, eroe.getEsperienza());
        assertEquals(2, eroe.getStats());
        assertEquals(eroe.getVitaMassima(), eroe.getVita());
    }

    @Test
    public void esperienzaNegativaNonRimuoveProgressi() {
        Eroe eroe = new Eroe();
        eroe.guadagnaEsperienza(20);
        eroe.guadagnaEsperienza(-100);
        assertEquals(20, eroe.getEsperienza());
        assertEquals(1, eroe.getLivello());
    }

    @Test
    public void livelloMassimoFermaProgressioneEScartaEsperienza() {
        Eroe eroe = new Eroe(29, 0, 0, 1, 0, 0, 0, 0);
        eroe.guadagnaEsperienza(1000000);
        assertEquals(30, eroe.getLivello());
        assertEquals(1, eroe.getStats());
        assertEquals(0, eroe.getEsperienza());
        assertEquals(0, eroe.esperienzaNuovoLivello());
        eroe.guadagnaEsperienza(1000000);
        assertEquals(1, eroe.getStats());
        assertEquals(0, eroe.getEsperienza());
    }

    @Test
    public void forzaConsumaUnPuntoEAumentaIlDanno() {
        Eroe eroe = new Eroe(4, 0, 0, 100, 1, 1, 1, 0);
        int dannoPrima = eroe.getPotenzaAttacco();
        assertTrue(eroe.aumentaForza());
        assertEquals(2, eroe.getForza());
        assertEquals(dannoPrima + 4, eroe.getPotenzaAttacco());
        assertEquals(0, eroe.getStats());
        assertFalse(eroe.aumentaForza());
        assertEquals(2, eroe.getForza());
    }

    @Test
    public void saluteAumentaVitaAttualeEMassimaSenzaCancellareIlDanno() {
        Eroe eroe = new Eroe(4, 0, 0, 100, 0, 0, 3, 0);
        int massimoPrima = eroe.getVitaMassima();
        assertTrue(eroe.aumentaSalute());
        assertEquals(118, eroe.getVita());
        assertEquals(massimoPrima + 18, eroe.getVitaMassima());
        assertEquals(2, eroe.getStats());
    }

    @Test
    public void puntiCaricatiNonSuperanoQuelliDisponibiliDalLivello() {
        Eroe eroe = new Eroe(5, 0, 0, 100, 2, 1, 99, 0);
        assertEquals(1, eroe.getStats());
        assertTrue(eroe.aumentaSalute());
        assertFalse(eroe.aumentaSalute());
        assertEquals(0, eroe.getStats());
    }

    @Test
    public void inventarioNonAccettaPiuDiTrePozioni() {
        Eroe eroe = new Eroe();
        assertTrue(eroe.aggiungiPozioneCura());
        assertTrue(eroe.aggiungiPozioneCura());
        assertTrue(eroe.aggiungiPozioneCura());
        assertFalse(eroe.aggiungiPozioneCura());
        assertEquals(3, eroe.getPozioniCura());
    }

    @Test
    public void pozioneCuraSenzaSuperareIlMassimo() {
        Eroe eroe = new Eroe(1, 0, 0, 90, 0, 0, 0, 2);
        assertTrue(eroe.usaPozioneCura());
        assertEquals(108, eroe.getVita());
        assertEquals(1, eroe.getPozioniCura());
        assertFalse(eroe.usaPozioneCura());
        assertEquals(1, eroe.getPozioniCura());
    }

    @Test
    public void pozioneCuraTrentaseiPuntiENonPuoEssereUsataSenzaScorte() {
        Eroe eroe = new Eroe(1, 0, 0, 20, 0, 0, 0, 1);
        assertTrue(eroe.usaPozioneCura());
        assertEquals(56, eroe.getVita());
        assertFalse(eroe.usaPozioneCura());
        assertEquals(56, eroe.getVita());
    }

    @Test
    public void rinascitaECuraCompletaRipristinanoLaVitaPrevista() {
        Eroe eroe = new Eroe();
        eroe.subisciDanno(1000);
        eroe.rinascita();
        assertEquals(54, eroe.getVita());
        assertFalse(eroe.isSconfitto());
        eroe.curaCompleta();
        assertEquals(108, eroe.getVita());
    }

    @Test
    public void bossDiventaAccessibileDalLivelloVenti() {
        assertFalse(new Eroe(19, 0, 0, 100, 0, 0, 0, 0).puoSfidareBoss());
        assertTrue(new Eroe(20, 0, 0, 100, 0, 0, 0, 0).puoSfidareBoss());
    }
}
