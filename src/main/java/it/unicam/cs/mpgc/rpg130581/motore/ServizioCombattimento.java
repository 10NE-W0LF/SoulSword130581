package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Random;

/**
 * Servizio che risolve uno scambio di combattimento tra eroe e vampiro.
 * Restituisce un esito senza occuparsi direttamente della grafica o degli scenari.
 */
public class ServizioCombattimento {

    private final Random random;

    /**
     * Crea il servizio di combattimento.
     *
     * @param random generatore casuale usato per variare i danni
     */
    public ServizioCombattimento(Random random) {
        this.random = random;
    }

    /**
     * Esegue l'attacco dell'eroe contro un vampiro e gestisce l'eventuale contrattacco.
     *
     * @param eroe eroe che attacca
     * @param vampiro vampiro che subisce l'attacco
     * @return esito dello scambio di combattimento
     */
    public EsitoCombattimento attacca(Eroe eroe, Vampiro vampiro) {
        int dannoEroe = eroe.getPotenzaAttacco() + random.nextInt(10);
        vampiro.subisciDanno(dannoEroe);

        // La ricompensa viene assegnata solo quando il vampiro viene sconfitto.
        if (vampiro.isSconfitto()) {
            eroe.guadagnaEsperienza(vampiro.getRicompensaEsperienza());
            eroe.registraVittoria();
            String messaggio = vampiro.getTipo() == TipoVampiro.arciduca
                    ? "L'Arciduca Vampiro e' caduto. L'alba torna sul mondo."
                    : "Vampiro sconfitto. La SoulSword assorbe nuova energia.";
            return new EsitoCombattimento(0, true, false, messaggio);
        }

        int dannoVampiro = Math.max(1,
                vampiro.getPotenzaAttacco() + random.nextInt(6) - eroe.getEvoSoulSword().ordinal() * 3);
        eroe.subisciDanno(dannoVampiro);
        boolean eroeSconfitto = eroe.isSconfitto();

        String messaggio = eroeSconfitto
                ? "Sei stato sconfitto dai vampiri."
                : "Colpo scambiato.";
        return new EsitoCombattimento(dannoVampiro, false, eroeSconfitto, messaggio);
    }
}
