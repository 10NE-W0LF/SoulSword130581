package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import org.junit.Test;

import static org.junit.Assert.*;

public class PersonaggioTest {
    @Test
    public void dannoNonPuoCurareNePortareLaVitaSottoZero() {
        Personaggio personaggio = new Eroe();
        personaggio.subisciDanno(-20);
        assertEquals(108, personaggio.getVita());
        personaggio.subisciDanno(8);
        assertEquals(100, personaggio.getVita());
        personaggio.subisciDanno(1000);
        assertEquals(0, personaggio.getVita());
        assertTrue(personaggio.isSconfitto());
    }

    @Test
    public void impostazioneVitaRispettaEntrambiILimiti() {
        Personaggio personaggio = new Eroe();
        personaggio.impostaVita(10000);
        assertEquals(personaggio.getVitaMassima(), personaggio.getVita());
        personaggio.impostaVita(-2);
        assertEquals(0, personaggio.getVita());
        personaggio.curaAlMassimo();
        assertFalse(personaggio.isSconfitto());
    }

    @Test
    public void aggiornamentiProtettiIgnoranoIncrementiNegativi() {
        Personaggio personaggio = new Eroe();
        personaggio.aggiungiForza(3);
        personaggio.aggiungiForza(-5);
        personaggio.aggiungiSalute(2);
        personaggio.aggiungiSalute(-9);
        personaggio.impostaLivello(-10);
        assertEquals(3, personaggio.getForza());
        assertEquals(2, personaggio.getSalute());
        assertEquals(1, personaggio.getLivello());
    }
}
