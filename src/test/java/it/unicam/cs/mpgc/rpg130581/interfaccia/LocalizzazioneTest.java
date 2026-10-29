package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.EventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.TipoEventoGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.StatoPartita;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPartita;
import it.unicam.cs.mpgc.rpg130581.persistenza.SalvataggiPreferenze;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/** Regressioni sulle traduzioni e sulla lingua ricordata ai successivi avvii. */
public final class LocalizzazioneTest {
    @Test
    public void eventiDelMotoreSonoTradottiConDatiENomiLocalizzati() {
        TestiInterfaccia testi = new TestiInterfaccia();
        EventoGioco colpo = EventoGioco.di(TipoEventoGioco.colpoSubito, TipoVampiro.vampiroBase, 12);
        richiedi(testi.evento(colpo).equals("Vampiro ti colpisce: -12 vita."), "Evento italiano");
        testi.impostaLingua(Lingua.inglese);
        richiedi(testi.evento(colpo).equals("Vampire hits you: -12 HP."), "Evento inglese");
        richiedi(testi.evento(EventoGioco.di(TipoEventoGioco.vittoriaScenario, ScenarioGioco.castello))
                .equals("Victory! You have completed Castle."), "Scenario localizzato");
        for (TipoEventoGioco tipo : TipoEventoGioco.values()) {
            richiedi(ResourceBundle.getBundle("lingue.testi", Locale.ITALIAN).containsKey("evento." + tipo.name()),
                    "Evento privo di traduzione: " + tipo);
        }
    }

    @Test
    public void verificaTraduzioni() {
        ResourceBundle italiano = ResourceBundle.getBundle("lingue.testi", Locale.ITALIAN);
        ResourceBundle inglese = ResourceBundle.getBundle("lingue.testi", Locale.ENGLISH);
        richiedi(italiano.keySet().equals(inglese.keySet()), "Chiavi mancanti nelle traduzioni");
        Pattern formato = Pattern.compile("%[ds]");
        for (String chiave : italiano.keySet()) {
            String it = italiano.getString(chiave);
            String en = inglese.getString(chiave);
            richiedi(!it.isBlank() && !en.isBlank(), "Traduzione vuota: " + chiave);
            richiedi(formato.matcher(it).results().map(r -> r.group()).toList()
                    .equals(formato.matcher(en).results().map(r -> r.group()).toList()),
                    "Segnaposti incompatibili: " + chiave);
        }

        Locale precedente = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ENGLISH);
            TestiInterfaccia testi = new TestiInterfaccia();
            richiedi(testi.testo("menu.nuovaPartita").equals("Nuova partita"), "Default italiano");
            testi.impostaLingua(Lingua.inglese);
            richiedi(testi.testo("menu.nuovaPartita").equals("New game"), "Cambio immediato");
            richiedi(testi.testo("eroe.forza", 2, 10).equals("Strength: 2  (Damage +10)"), "Statistiche");
            richiedi(testi.testo("hud.nemici", 3, 8).equals("Enemies: 3/8"), "HUD");
            for (EvoSoulSword evoluzione : EvoSoulSword.values()) {
                richiedi(!testi.nome(evoluzione).isBlank(), "Evoluzione senza traduzione");
            }
            for (TipoVampiro tipo : TipoVampiro.values()) {
                richiedi(!testi.nome(tipo).isBlank(), "Nemico senza traduzione");
            }
            richiedi(testi.nome(EvoSoulSword.lamaDiSangue).equals("SoulSword: Blood Blade"), "SoulSword");
            richiedi(testi.nome(TipoVampiro.arciduca).equals("Vampire Archduke"), "Boss");
            richiedi(testi.evento(EventoGioco.di(TipoEventoGioco.vittoriaScenario, ScenarioGioco.castello))
                    .equals("Victory! You have completed Castle."), "Vittoria scenario");
            richiedi(testi.evento(EventoGioco.di(TipoEventoGioco.vittoriaFinale)).contains("hunt is over"),
                    "Vittoria finale");
            richiedi(testi.evento(EventoGioco.di(TipoEventoGioco.notteVincente)).contains("defeated"), "Sconfitta");
            testi.impostaLingua(Lingua.italiano);
            richiedi(testi.nome(EvoSoulSword.mietitriceDellAlba)
                    .equals("SoulSword: Mietitrice dell'Alba"), "Ritorno all'italiano e apostrofi");
        } finally {
            Locale.setDefault(precedente);
        }
    }

    @Test
    public void verificaPreferenze() throws IOException {
        Path cartella = Files.createTempDirectory("soulsword-lingua-");
        Path file = cartella.resolve("preferenze.properties");
        try {
            SalvataggiPreferenze archivio = new SalvataggiPreferenze(file);
            richiedi(archivio.caricaLingua() == Lingua.italiano, "Primo avvio senza preferenze");
            archivio.salvaLingua(Lingua.inglese);
            richiedi(new SalvataggiPreferenze(file).caricaLingua() == Lingua.inglese, "Riavvio in inglese");
            TestiInterfaccia sessione = new TestiInterfaccia();
            sessione.impostaLingua(Lingua.italiano);
            richiedi(archivio.caricaLingua() == Lingua.inglese, "Cambio non salvato");
            archivio.salvaLingua(sessione.getLingua());
            richiedi(new SalvataggiPreferenze(file).caricaLingua() == Lingua.italiano, "Riavvio in italiano");
            Files.writeString(file, "lingua=xx\n");
            richiedi(archivio.caricaLingua() == Lingua.italiano, "Codice sconosciuto");
            Files.writeString(file, "altraPreferenza=true\n");
            richiedi(archivio.caricaLingua() == Lingua.italiano, "Chiave assente");
            Files.writeString(file, "lingua=" + "\\" + "uZZZZ\n");
            richiedi(archivio.caricaLingua() == Lingua.italiano, "File malformato");
            try {
                new SalvataggiPreferenze(cartella).salvaLingua(Lingua.inglese);
                throw new AssertionError("Errore di scrittura ignorato");
            } catch (IOException attesa) {
                // La GUI deve poter segnalare il salvataggio parziale.
            }
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(cartella);
        }
    }

    @Test
    public void verificaEsitoSalvataggio() {
        for (boolean fallisce : new boolean[]{false, true}) {
            ArchivioPartita archivio = new ArchivioPartita() {
                public void salva(StatoPartita stato) throws IOException {
                    if (fallisce) {
                        throw new IOException("Errore simulato");
                    }
                }

                public StatoPartita carica() {
                    return new StatoPartita();
                }
            };
            richiedi(new MotoreGioco(archivio).salva() != fallisce, "Esito salvataggio partita");
        }
    }

    private static void richiedi(boolean condizione, String messaggio) {
        if (!condizione) {
            throw new AssertionError(messaggio);
        }
    }
}
