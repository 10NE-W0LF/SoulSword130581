package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.impostazioni.Lingua;
import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/** Testi della GUI, separati dai nomi e dai dati usati dal modello e dai salvataggi. */
final class TestiInterfaccia {

    private Lingua lingua;
    private Locale locale;
    private ResourceBundle risorse;

    TestiInterfaccia() {
        impostaLingua(Lingua.italiano);
    }

    Lingua getLingua() {
        return lingua;
    }

    void impostaLingua(Lingua lingua) {
        this.lingua = Objects.requireNonNull(lingua);
        locale = Locale.forLanguageTag(lingua.getCodice());
        risorse = ResourceBundle.getBundle("lingue.testi", locale,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    String testo(String chiave, Object... argomenti) {
        return String.format(locale, risorse.getString(chiave), argomenti);
    }

    String nome(EvoSoulSword evoluzione) {
        return testo("soulSword." + evoluzione.name());
    }

    String nome(TipoVampiro tipo) {
        return testo("vampiro." + tipo.name());
    }

    String messaggioEsito(boolean vittoria, ScenarioGioco scenario) {
        if (!vittoria) {
            return testo("esito.sconfitta");
        }
        return scenario.isScenarioBoss() ? testo("esito.vittoriaFinale")
                : testo("esito.vittoriaScenario", scenario.getNomeVisualizzato());
    }
}
