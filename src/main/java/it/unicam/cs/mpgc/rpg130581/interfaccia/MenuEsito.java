package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.applicazione.EsitoSalvataggio;
import javafx.scene.layout.VBox;

import static it.unicam.cs.mpgc.rpg130581.interfaccia.ComponentiMenu.*;

final class MenuEsito extends VBox {
    MenuEsito(TestiInterfaccia testi, boolean vittoria, String messaggio, EsitoSalvataggio salvataggio,
              Runnable riprova, Runnable scenari, Runnable principale) {
        configuraPannello(this);
        getChildren().addAll(testo(testi.testo(vittoria ? "esito.titoloVittoria" : "esito.titoloSconfitta"),
                "titolo-esito", vittoria ? "vittoria" : "sconfitta"), testo(messaggio));
        if (salvataggio != null && salvataggio != EsitoSalvataggio.riuscito) {
            getChildren().add(testo(testi.salvataggio(salvataggio), "testo-dettaglio"));
        }
        if (!vittoria) {
            getChildren().add(bottone(testi.testo("esito.riprova"), riprova));
        }
        getChildren().addAll(bottone(testi.testo("scenari.elenco"), scenari),
                bottone(testi.testo("menu.principale"), principale));
    }
}
