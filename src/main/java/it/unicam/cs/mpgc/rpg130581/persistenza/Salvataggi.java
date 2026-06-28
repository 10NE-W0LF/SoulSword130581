package it.unicam.cs.mpgc.rpg130581.persistenza;

import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.Eroe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementazione JSON dell'archivio partita.
 * Scrive e legge lo stato del gioco da un file locale, creando la cartella se necessario.
 */
public class Salvataggi implements ArchivioPartita {

    private final Path percorsoSalvataggio;

    /**
     * Crea il gestore dei salvataggi.
     *
     * @param percorsoSalvataggio percorso del file JSON
     */
    public Salvataggi(Path percorsoSalvataggio) {
        this.percorsoSalvataggio = percorsoSalvataggio;
    }

    /**
     * Salva lo stato della partita in formato JSON.
     *
     * @param stato stato da salvare
     * @throws IOException se la scrittura del file fallisce
     */
    @Override
    public void salva(StatoPartita stato) throws IOException {
        if (percorsoSalvataggio.getParent() != null) {
            Files.createDirectories(percorsoSalvataggio.getParent());
        }

        Eroe eroe = stato.getEroe();
        String contenutoJson = "{\n"
                + "  \"eroe\": {\n"
                + "    \"livello\": " + eroe.getLivello() + ",\n"
                + "    \"esperienza\": " + eroe.getEsperienza() + ",\n"
                + "    \"vSconfitti\": " + eroe.getVSconfitti() + ",\n"
                + "    \"forza\": " + eroe.getForza() + ",\n"
                + "    \"salute\": " + eroe.getSalute() + ",\n"
                + "    \"stats\": " + eroe.getStats() + ",\n"
                + "    \"pozioniCura\": " + eroe.getPozioniCura() + ",\n"
                + "    \"vita\": " + eroe.getVita() + "\n"
                + "  },\n"
                + "  \"numeroScenario\": " + stato.getNumeroScenario() + ",\n"
                + "  \"sconfittiNelloScenario\": " + stato.getSconfittiNelloScenario() + ",\n"
                + "  \"bossSconfitto\": " + stato.isBossSconfitto() + "\n"
                + "}\n";

        Files.write(percorsoSalvataggio, contenutoJson.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Carica lo stato dal file JSON.
     *
     * @return stato caricato o nuova partita se il file non esiste
     * @throws IOException se la lettura del file fallisce
     */
    @Override
    public StatoPartita carica() throws IOException {
        if (!Files.exists(percorsoSalvataggio)) {
            return new StatoPartita();
        }

        String contenutoJson = new String(Files.readAllBytes(percorsoSalvataggio), StandardCharsets.UTF_8);
        int livello = leggiIntero(contenutoJson, "livello", 1);
        int forza = leggiIntero(contenutoJson, "forza", 0);
        int salute = leggiIntero(contenutoJson, "salute", 0);
        int statsPredefiniti = Math.max(0, livello - 1 - forza - salute);
        String vecchiaChiaveStats = "punti" + "Statistiche";
        int stats = leggiIntero(contenutoJson, "stats",
                leggiIntero(contenutoJson, vecchiaChiaveStats, statsPredefiniti));
        int pozioniCura = leggiIntero(contenutoJson, "pozioniCura", 0);
        Eroe eroe = new Eroe(
                livello,
                leggiIntero(contenutoJson, "esperienza", 0),
                leggiIntero(contenutoJson, "vSconfitti", 0),
                leggiIntero(contenutoJson, "vita", 108),
                forza,
                salute,
                stats,
                pozioniCura
        );

        return new StatoPartita(
                eroe,
                leggiBooleano(contenutoJson, "bossSconfitto", false),
                leggiIntero(contenutoJson, "numeroScenario", 1),
                leggiIntero(contenutoJson, "sconfittiNelloScenario", 0)
        );
    }

    // Cerca una chiave numerica nel JSON e usa un valore predefinito se non viene trovata.
    private int leggiIntero(String contenutoJson, String chiave, int valorePredefinito) {
        Matcher corrispondenza = Pattern.compile("\"" + chiave + "\"\\s*:\\s*(\\d+)").matcher(contenutoJson);
        return corrispondenza.find() ? Integer.parseInt(corrispondenza.group(1)) : valorePredefinito;
    }

    // Cerca una chiave booleana nel JSON e usa un valore predefinito se non viene trovata.
    private boolean leggiBooleano(String contenutoJson, String chiave, boolean valorePredefinito) {
        Matcher corrispondenza = Pattern.compile("\"" + chiave + "\"\\s*:\\s*(true|false)").matcher(contenutoJson);
        return corrispondenza.find() ? Boolean.parseBoolean(corrispondenza.group(1)) : valorePredefinito;
    }
}
