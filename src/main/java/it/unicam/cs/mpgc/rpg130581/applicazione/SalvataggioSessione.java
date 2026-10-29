package it.unicam.cs.mpgc.rpg130581.applicazione;

import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;

import java.util.Objects;

/** Caso d'uso condiviso dal salvataggio manuale e da quello dopo una vittoria. */
public final class SalvataggioSessione {
    private final MotoreGioco motore;
    private final PreferenzeGioco preferenze;

    public SalvataggioSessione(MotoreGioco motore, PreferenzeGioco preferenze) {
        this.motore = Objects.requireNonNull(motore);
        this.preferenze = Objects.requireNonNull(preferenze);
    }

    public EsitoSalvataggio salva() {
        if (!motore.salva()) {
            return EsitoSalvataggio.partitaFallita;
        }
        return preferenze.salva(preferenze.getLingua())
                ? EsitoSalvataggio.riuscito : EsitoSalvataggio.preferenzeFallite;
    }
}
