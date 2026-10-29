package it.unicam.cs.mpgc.rpg130581.motore;

import it.unicam.cs.mpgc.rpg130581.modello.combattimento.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg130581.modello.partita.EventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.TipoEventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Vampiro;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Servizio che risolve uno scambio di combattimento tra eroe e vampiro.
 * Restituisce un esito senza occuparsi direttamente della grafica o degli scenari.
 */
public class ServizioCombattimento implements Combattimento {

    private final RandomGenerator random;

    /**
     * Crea il servizio di combattimento.
     *
     * @param random generatore casuale usato per variare i danni
     */
    public ServizioCombattimento(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
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
            TipoEventoGioco tipo = vampiro.getTipo() == TipoVampiro.arciduca
                    ? TipoEventoGioco.bossSconfitto : TipoEventoGioco.vampiroSconfitto;
            return new EsitoCombattimento(0, true, false, EventoGioco.di(tipo));
        }

        int dannoVampiro = Math.max(1,
                vampiro.getPotenzaAttacco() + random.nextInt(6) - eroe.getEvoSoulSword().ordinal() * 3);
        eroe.subisciDanno(dannoVampiro);
        boolean eroeSconfitto = eroe.isSconfitto();

        TipoEventoGioco tipo = eroeSconfitto
                ? TipoEventoGioco.eroeSconfitto : TipoEventoGioco.colpoScambiato;
        return new EsitoCombattimento(dannoVampiro, false, eroeSconfitto, EventoGioco.di(tipo));
    }
}
