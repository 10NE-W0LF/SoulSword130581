package it.unicam.cs.mpgc.rpg130581.modello.personaggi;

public class Eroe extends Personaggio {

    public static final int LIVELLO_MASSIMO = 30;
    private static final String NOME = "Zephyr Ragnar";

    private int esperienza;
    private int vampiriSconfitti;

    public Eroe() {
        this(1, 0, 0, 108);
    }

    public Eroe(int livello, int esperienza, int vampiriSconfitti, int vita) {
        super(Math.max(1, Math.min(LIVELLO_MASSIMO, livello)), vita);
        this.esperienza = getLivello() == LIVELLO_MASSIMO ? 0 : Math.max(0, esperienza);
        this.vampiriSconfitti = Math.max(0, vampiriSconfitti);
        impostaVita(vita);
    }

    public void guadagnaEsperienza(int quantita) {
        if (isLivelloMassimo()) {
            esperienza = 0;
            return;
        }

        esperienza += Math.max(0, quantita);
        while (!isLivelloMassimo() && esperienza >= esperienzaPerProssimoLivello()) {
            esperienza -= esperienzaPerProssimoLivello();
            impostaLivello(getLivello() + 1);
            curaAlMassimo();
        }

        if (isLivelloMassimo()) {
            esperienza = 0;
        }
    }

    public void registraVittoria() {
        vampiriSconfitti++;
    }

    public void recuperaDopoSconfitta() {
        impostaVita(Math.max(1, getVitaMassima() / 2));
    }

    public void curaCompleta() {
        curaAlMassimo();
    }

    @Override
    public int getPotenzaAttacco() {
        return 12 + getLivello() * 4 + getEvoluzioneSoulSword().ordinal() * 10;
    }

    @Override
    public int getVitaMassima() {
        return 100 + getLivello() * 8;
    }

    public int esperienzaPerProssimoLivello() {
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
        return getLivello() >= LIVELLO_MASSIMO;
    }

    public String getNome() {
        return NOME;
    }

    public int getEsperienza() {
        return esperienza;
    }

    public int getVampiriSconfitti() {
        return vampiriSconfitti;
    }
}
