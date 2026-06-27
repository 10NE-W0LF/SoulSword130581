package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Random;

public class ServizioCombattimento {

    private final Random random;

    public ServizioCombattimento(Random random) {
        this.random = random;
    }

    public EsitoCombattimento attacca(Eroe eroe, Vampiro vampiro) {
        int dannoEroe = eroe.getPotenzaAttacco() + random.nextInt(10);
        vampiro.subisciDanno(dannoEroe);

        if (vampiro.isSconfitto()) {
            eroe.guadagnaEsperienza(vampiro.getRicompensaEsperienza());
            eroe.registraVittoria();
            String messaggio = vampiro.getTipo() == TipoVampiro.arciduca
                    ? "L'Arciduca Vampiro e' caduto. L'alba torna sul mondo."
                    : "Vampiro sconfitto. La SoulSword assorbe nuova energia.";
            return new EsitoCombattimento(dannoEroe, 0, true, false, messaggio);
        }

        int dannoVampiro = Math.max(1,
                vampiro.getPotenzaAttacco() + random.nextInt(6) - eroe.getEvoluzioneSoulSword().ordinal() * 3);
        eroe.subisciDanno(dannoVampiro);
        boolean eroeSconfitto = eroe.isSconfitto();

        String messaggio = eroeSconfitto
                ? "Sei stato sconfitto dai vampiri."
                : "Colpo scambiato.";
        return new EsitoCombattimento(dannoEroe, dannoVampiro, false, eroeSconfitto, messaggio);
    }
}
