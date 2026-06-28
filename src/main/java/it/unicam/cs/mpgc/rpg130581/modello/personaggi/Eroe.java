package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.PozioneCura;

/**
 * Rappresenta Zephyr Ragnar, quindi il personaggio controllato dal giocatore.
 * Gestisce esperienza, level up, punti stats, evoluzione della SoulSword e pozioni cura.
 */
public class Eroe extends Personaggio {

    public static final int livelloMassimo = 30;
    public static final int bonusForza = 4;
    public static final int bonusSalute = 18;
    private static final String nome = "Zephyr Ragnar";

    private int esperienza;
    private int vSconfitti;
    private int stats;
    private int pozioniCura;

    public Eroe() {
        this(1, 0, 0, 108, 0, 0, 0, 0);
    }

    public Eroe(int livello, int esperienza, int vSconfitti, int vita,
                int forza, int salute, int stats, int pozioniCura) {
        super(Math.max(1, Math.min(livelloMassimo, livello)), vita, forza, salute);
        this.esperienza = getLivello() == livelloMassimo ? 0 : Math.max(0, esperienza);
        this.vSconfitti = Math.max(0, vSconfitti);
        this.stats = normalizzaStats(stats);
        this.pozioniCura = Math.max(0, Math.min(PozioneCura.maxPozioni, pozioniCura));
        impostaVita(vita);
    }

    /**
     * Aggiunge esperienza e gestisce tutti i level up possibili.
     * Ogni livello ottenuto assegna un punto stats e riporta la vita al massimo.
     *
     * @param quantita esperienza da aggiungere
     */
    public void guadagnaEsperienza(int quantita) {
        if (isLivelloMassimo()) {
            esperienza = 0;
            return;
        }

        esperienza += Math.max(0, quantita);
        while (!isLivelloMassimo() && esperienza >= esperienzaNuovoLivello()) {
            esperienza -= esperienzaNuovoLivello();
            impostaLivello(getLivello() + 1);
            stats++;
            curaAlMassimo();
        }

        if (isLivelloMassimo()) {
            esperienza = 0;
        }
    }

    /**
     * Registra una vittoria dell'eroe contro un vampiro.
     */
    public void registraVittoria() {
        vSconfitti++;
    }

    /**
     * Riporta in vita l'eroe dopo una sconfitta con meta' della vita massima.
     */
    public void rinascita() {
        impostaVita(Math.max(1, getVitaMassima() / 2));
    }

    /**
     * Cura completamente l'eroe.
     */
    public void curaCompleta() {
        curaAlMassimo();
    }

    /**
     * Spende un punto stats per aumentare la forza.
     *
     * @return true se il punto e' stato assegnato
     */
    public boolean aumentaForza() {
        if (stats <= 0) {
            return false;
        }
        aggiungiForza(1);
        stats--;
        return true;
    }

    /**
     * Spende un punto stats per aumentare la salute.
     *
     * @return true se il punto e' stato assegnato
     */
    public boolean aumentaSalute() {
        if (stats <= 0) {
            return false;
        }
        aggiungiSalute(1);
        stats--;
        impostaVita(getVita() + bonusSalute);
        return true;
    }

    /**
     * Aggiunge una Pozione Cura all'inventario dell'eroe.
     *
     * @return true se la pozione e' stata aggiunta
     */
    public boolean aggiungiPozioneCura() {
        if (pozioniCura >= PozioneCura.maxPozioni) {
            return false;
        }
        pozioniCura++;
        return true;
    }

    /**
     * Usa una Pozione Cura se disponibile.
     *
     * @return true se la pozione e' stata consumata
     */
    public boolean usaPozioneCura() {
        if (pozioniCura <= 0 || getVita() >= getVitaMassima()) {
            return false;
        }
        pozioniCura--;
        impostaVita(getVita() + PozioneCura.valoreCura);
        return true;
    }

    /**
     * Calcola il danno dell'eroe in base a livello, evoluzione SoulSword e stat Forza.
     *
     * @return potenza d'attacco dell'eroe
     */
    @Override
    public int getPotenzaAttacco() {
        return 12 + getLivello() * 4 + getEvoSoulSword().ordinal() * 10
                + getForza() * bonusForza;
    }

    /**
     * Calcola la vita massima dell'eroe in base al livello e alla stat Salute.
     *
     * @return vita massima dell'eroe
     */
    @Override
    public int getVitaMassima() {
        return 100 + getLivello() * 8 + getSalute() * bonusSalute;
    }

    /**
     * Calcola l'esperienza necessaria per raggiungere il prossimo livello.
     *
     * @return esperienza richiesta, oppure zero se l'eroe e' al livello massimo
     */
    public int esperienzaNuovoLivello() {
        if (isLivelloMassimo()) {
            return 0;
        }
        return 45 + getLivello() * getLivello() * 10;
    }

    /**
     * Restituisce l'evoluzione della SoulSword in base al livello attuale.
     *
     * @return evoluzione corrente della SoulSword
     */
    public EvoSoulSword getEvoSoulSword() {
        return EvoSoulSword.daLivello(getLivello());
    }

    /**
     * Indica se l'eroe puo' affrontare il boss finale.
     *
     * @return true se il livello e' almeno 20
     */
    public boolean puoSfidareBoss() {
        return getLivello() >= 20;
    }

    /**
     * Indica se l'eroe ha raggiunto il livello massimo.
     *
     * @return true se il livello e' pari al massimo consentito
     */
    public boolean isLivelloMassimo() {
        return getLivello() >= livelloMassimo;
    }

    public String getNome() {
        return nome;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public int getVSconfitti() {
        return vSconfitti;
    }

    public int getStats() {
        return stats;
    }

    public int getPozioniCura() {
        return pozioniCura;
    }

    // Evita salvataggi incoerenti con piu' punti stats di quelli ottenibili dal livello.
    private int normalizzaStats(int punti) {
        int puntiMassimi = Math.max(0, getLivello() - 1);
        int puntiUsati = getForza() + getSalute();
        return Math.max(0, Math.min(punti, puntiMassimi - puntiUsati));
    }
}
