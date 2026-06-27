package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public class Eroe extends Personaggio {

    public static final int livelloMassimo = 30;
    public static final int bonusForza = 4;
    public static final int bonusSalute = 18;
    private static final String nome = "Zephyr Ragnar";

    private int esperienza;
    private int vampiriSconfitti;
    private int stats;

    public Eroe() {
        this(1, 0, 0, 108, 0, 0, 0);
    }

    public Eroe(int livello, int esperienza, int vampiriSconfitti, int vita) {
        this(livello, esperienza, vampiriSconfitti, vita, 0, 0, 0);
    }

    public Eroe(int livello, int esperienza, int vampiriSconfitti, int vita,
                int forza, int salute, int stats) {
        super(Math.max(1, Math.min(livelloMassimo, livello)), vita, forza, salute);
        this.esperienza = getLivello() == livelloMassimo ? 0 : Math.max(0, esperienza);
        this.vampiriSconfitti = Math.max(0, vampiriSconfitti);
        this.stats = normalizzaStats(stats);
        impostaVita(vita);
    }

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

    public void registraVittoria() {
        vampiriSconfitti++;
    }

    public void rinascita() {
        impostaVita(Math.max(1, getVitaMassima() / 2));
    }

    public void curaCompleta() {
        curaAlMassimo();
    }

    public boolean aumentaForza() {
        if (stats <= 0) {
            return false;
        }
        aggiungiForza(1);
        stats--;
        return true;
    }

    public boolean aumentaSalute() {
        if (stats <= 0) {
            return false;
        }
        aggiungiSalute(1);
        stats--;
        impostaVita(getVita() + bonusSalute);
        return true;
    }

    @Override
    public int getPotenzaAttacco() {
        return 12 + getLivello() * 4 + getEvoluzioneSoulSword().ordinal() * 10
                + getForza() * bonusForza;
    }

    @Override
    public int getVitaMassima() {
        return 100 + getLivello() * 8 + getSalute() * bonusSalute;
    }

    public int esperienzaNuovoLivello() {
        if (isLivelloMassimo()) {
            return 0;
        }
        return 45 + getLivello() * getLivello() * 10;
    }

    public EvoluzioneSoulSword getEvoluzioneSoulSword() {
        return EvoluzioneSoulSword.daLivello(getLivello());
    }

    public boolean puoSfidareBoss() {
        return getLivello() >= 20;
    }

    public boolean isLivelloMassimo() {
        return getLivello() >= livelloMassimo;
    }

    public String getNome() {
        return nome;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public int getVampiriSconfitti() {
        return vampiriSconfitti;
    }

    public int getStats() {
        return stats;
    }

    private int normalizzaStats(int punti) {
        int puntiMassimi = Math.max(0, getLivello() - 1);
        int puntiUsati = getForza() + getSalute();
        return Math.max(0, Math.min(punti, puntiMassimi - puntiUsati));
    }
}
