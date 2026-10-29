package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.applicazione.EsitoSalvataggio;
import it.unicam.cs.mpgc.rpg130581.applicazione.PreferenzeGioco;
import it.unicam.cs.mpgc.rpg130581.applicazione.SalvataggioSessione;
import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.modello.partita.FaseGioco;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg130581.persistenza.ArchivioPreferenze;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;

/** Coordina navigazione e casi d'uso. Le viste e la configurazione JavaFX sono componenti separati. */
public final class AppSoulSword {
    private final MotoreGioco motore;
    private final PreferenzeGioco preferenze;
    private final SalvataggioSessione salvataggio;
    private final ArchivioSprite immagini = new ArchivioSprite();
    private final TestiInterfaccia testi = new TestiInterfaccia();
    private Stage finestra;
    private GestoreFinestra gestoreFinestra;
    private SchermataPartita partita;

    public AppSoulSword(MotoreGioco motore, ArchivioPreferenze archivioPreferenze) {
        this.motore = Objects.requireNonNull(motore);
        preferenze = new PreferenzeGioco(archivioPreferenze);
        salvataggio = new SalvataggioSessione(motore, preferenze);
    }

    public void start(Stage finestra) {
        this.finestra = Objects.requireNonNull(finestra);
        gestoreFinestra = new GestoreFinestra(finestra);
        preferenze.carica();
        testi.impostaLingua(preferenze.getLingua());
        finestra.setTitle("SoulSword");
        finestra.getIcons().add(immagini.iconaSoulSword());
        finestra.setOnCloseRequest(ignored -> ferma());
        mostraMenuPrincipale();
        finestra.show();
    }

    private void mostraMenuPrincipale() {
        mostraMenuPrincipale(preferenze.isErroreLettura() ? testi.testo("preferenze.erroreLettura") : "");
    }

    private void mostraMenuPrincipale(String avviso) {
        mostra(new MenuPrincipale(testi, finestra.fullScreenProperty(), () -> {
            motore.nuovaPartita();
            mostraMenuScenari();
        }, this::caricaPartita, gestoreFinestra::alternaSchermoIntero,
                this::mostraMenuLingua, finestra::close, avviso));
    }

    private void caricaPartita() {
        if (motore.carica()) {
            mostraMenuScenari();
        } else {
            mostraMenuPrincipale(testi.evento(motore.getUltimoEvento()));
        }
    }

    private void mostraMenuLingua() {
        mostra(new MenuLingua(testi, this::salvaLingua, this::mostraMenuPrincipale));
    }

    private boolean salvaLingua(Lingua lingua) {
        if (!preferenze.salva(lingua)) {
            return false;
        }
        testi.impostaLingua(preferenze.getLingua());
        mostraMenuPrincipale();
        return true;
    }

    private void mostraMenuScenari() {
        mostra(new MenuScenari(motore.getStato(), immagini, testi, this::mostraGioco,
                () -> {
                    if (motore.getStato().getEroe().aumentaForza()) {
                        mostraMenuScenari();
                    }
                }, () -> {
                    if (motore.getStato().getEroe().aumentaSalute()) {
                        mostraMenuScenari();
                    }
                }, salvataggio::salva, this::mostraMenuPrincipale));
    }

    private void mostraGioco(ScenarioGioco scenario) {
        if (!motore.avviaScenario(scenario)) {
            mostraMenuScenari();
            return;
        }
        ferma();
        partita = new SchermataPartita(motore, testi, this::mostraSchermataEsito, () -> {
            motore.annullaScenarioSenzaProgressi();
            mostraMenuScenari();
        });
        partita.collegaInput(gestoreFinestra.mostra(partita));
    }

    private void mostraSchermataEsito() {
        boolean vittoria = motore.getFase() == FaseGioco.vittoria;
        ScenarioGioco scenario = motore.getScenarioCorrente();
        // Il salvataggio produce un altro evento: conserva prima quello della conclusione.
        String messaggio = testi.evento(motore.getUltimoEvento());
        EsitoSalvataggio esito = vittoria ? salvataggio.salva() : null;
        mostra(new MenuEsito(testi, vittoria, messaggio, esito, () -> {
            motore.annullaScenarioSenzaProgressi();
            mostraGioco(scenario);
        }, () -> terminaScenario(vittoria, this::mostraMenuScenari),
                () -> terminaScenario(vittoria, this::mostraMenuPrincipale)));
    }

    private void terminaScenario(boolean vittoria, Runnable destinazione) {
        if (vittoria) {
            motore.confermaVittoriaScenario();
        } else {
            motore.annullaScenarioSenzaProgressi();
        }
        destinazione.run();
    }

    private void mostra(Parent menu) {
        ferma();
        gestoreFinestra.mostra(ComponentiMenu.conSfondo(menu, immagini));
    }

    public void ferma() {
        if (partita != null) {
            partita.ferma();
            partita = null;
        }
    }
}
