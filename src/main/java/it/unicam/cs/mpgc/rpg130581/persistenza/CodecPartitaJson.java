package it.unicam.cs.mpgc.rpg130581.persistenza;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;

import java.io.IOException;

/** Converte lo schema del salvataggio, senza conoscere file o interfaccia grafica. */
final class CodecPartitaJson {
    private final Gson gson = new GsonBuilder().setStrictness(Strictness.STRICT).setPrettyPrinting().create();

    String codifica(StatoPartita stato) {
        Eroe eroe = stato.getEroe();
        JsonObject datiEroe = new JsonObject();
        datiEroe.addProperty("livello", eroe.getLivello());
        datiEroe.addProperty("esperienza", eroe.getEsperienza());
        datiEroe.addProperty("vSconfitti", eroe.getVSconfitti());
        datiEroe.addProperty("forza", eroe.getForza());
        datiEroe.addProperty("salute", eroe.getSalute());
        datiEroe.addProperty("stats", eroe.getStats());
        datiEroe.addProperty("pozioniCura", eroe.getPozioniCura());
        datiEroe.addProperty("vita", eroe.getVita());
        JsonObject partita = new JsonObject();
        partita.add("eroe", datiEroe);
        partita.addProperty("numeroScenario", stato.getNumeroScenario());
        partita.addProperty("sconfittiNelloScenario", stato.getSconfittiNelloScenario());
        partita.addProperty("bossSconfitto", stato.isBossSconfitto());
        return gson.toJson(partita) + "\n";
    }

    StatoPartita decodifica(String json) throws IOException {
        try {
            JsonObject partita = oggetto(gson.fromJson(json, JsonElement.class), "partita");
            // Compatibilità con i salvataggi che avevano i campi dell'eroe alla radice.
            JsonObject datiEroe = partita.has("eroe") ? oggetto(partita.get("eroe"), "eroe") : partita;
            int livello = intero(datiEroe, "livello", 1);
            int forza = intero(datiEroe, "forza", 0);
            int salute = intero(datiEroe, "salute", 0);
            int puntiDisponibili = (int) Math.max(0L, Math.min(29L,
                    (long) livello - 1 - Math.max(0, forza) - Math.max(0, salute)));
            int stats = datiEroe.has("stats") ? intero(datiEroe, "stats", 0)
                    : intero(datiEroe, "puntiStatistiche", puntiDisponibili);
            Eroe eroe = new Eroe(livello, intero(datiEroe, "esperienza", 0),
                    intero(datiEroe, "vSconfitti", 0), intero(datiEroe, "vita", 108),
                    forza, salute, stats, intero(datiEroe, "pozioniCura", 0));
            return new StatoPartita(eroe, booleano(partita, "bossSconfitto", false),
                    intero(partita, "numeroScenario", 1), intero(partita, "sconfittiNelloScenario", 0));
        } catch (JsonParseException | IllegalArgumentException | ArithmeticException exception) {
            throw new IOException("Salvataggio JSON non valido", exception);
        }
    }

    private JsonObject oggetto(JsonElement elemento, String nome) {
        if (elemento == null || !elemento.isJsonObject()) {
            throw new IllegalArgumentException("Oggetto richiesto: " + nome);
        }
        return elemento.getAsJsonObject();
    }

    private int intero(JsonObject oggetto, String chiave, int predefinito) {
        if (!oggetto.has(chiave)) {
            return predefinito;
        }
        JsonElement valore = oggetto.get(chiave);
        if (!valore.isJsonPrimitive() || !valore.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException("Numero intero richiesto: " + chiave);
        }
        return valore.getAsBigDecimal().intValueExact();
    }

    private boolean booleano(JsonObject oggetto, String chiave, boolean predefinito) {
        if (!oggetto.has(chiave)) {
            return predefinito;
        }
        JsonElement valore = oggetto.get(chiave);
        if (!valore.isJsonPrimitive() || !valore.getAsJsonPrimitive().isBoolean()) {
            throw new IllegalArgumentException("Booleano richiesto: " + chiave);
        }
        return valore.getAsBoolean();
    }
}
