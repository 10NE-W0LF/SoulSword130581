package it.unicam.cs.mpgc.rpg130581.modello.partita;

import java.util.List;
import java.util.Objects;

/** Codice dell'esito e dati da presentare; non contiene frasi tradotte. */
public record EventoGioco(TipoEventoGioco tipo, List<Object> dati) {
    public EventoGioco {
        Objects.requireNonNull(tipo);
        dati = List.copyOf(dati);
    }

    public static EventoGioco di(TipoEventoGioco tipo, Object... dati) {
        return new EventoGioco(tipo, List.of(dati));
    }
}
