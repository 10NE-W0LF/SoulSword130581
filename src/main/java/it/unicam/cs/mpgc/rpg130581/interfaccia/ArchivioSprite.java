package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Archivio centralizzato delle immagini.
 * Carica le risorse una sola volta e le riusa tramite una piccola cache interna.
 */
public class ArchivioSprite {

    private final Map<String, Image> immagini = new HashMap<String, Image>();

    /**
     * Restituisce lo sprite dell'eroe per evoluzione e animazione richieste.
     *
     * @param evoluzione evoluzione corrente della SoulSword
     * @param animazione animazione da mostrare
     * @return immagine dello sprite dell'eroe
     */
    public Image eroe(EvoSoulSword evoluzione, TipoAnimazione animazione) {
        int livelloAsset = livelloAssetEroe(evoluzione);
        return caricaRisorsa("/sprites/hero/level" + livelloAsset + "/" + animazione.getNomeFile() + ".png");
    }

    /**
     * Restituisce lo sprite del vampiro richiesto.
     *
     * @param tipo tipo del vampiro
     * @param animazione animazione da mostrare
     * @return immagine dello sprite del vampiro
     */
    public Image vampiro(TipoVampiro tipo, TipoAnimazione animazione) {
        return caricaRisorsa("/sprites/vampires/vampire" + tipo.getLivelloAsset() + "/" + animazione.getNomeFile() + ".png");
    }

    /**
     * Restituisce lo sfondo dello scenario.
     *
     * @param scenario scenario corrente
     * @return immagine dello sfondo
     */
    public Image sfondo(ScenarioGioco scenario) {
        return caricaRisorsa(scenario.getPercorsoSfondo());
    }

    /**
     * Restituisce l'icona principale del gioco.
     *
     * @return immagine dell'icona SoulSword
     */
    public Image iconaSoulSword() {
        return caricaRisorsa("/sfondi/SoulSwordIcon.png");
    }

    /**
     * Restituisce l'icona della Pozione Cura.
     *
     * @return immagine della pozione cura
     */
    public Image pozioneCura() {
        return caricaRisorsa("/consumabili/pozione_cura.png");
    }

    // Traduce l'evoluzione della SoulSword nella cartella sprite corretta.
    private int livelloAssetEroe(EvoSoulSword evoluzione) {
        if (evoluzione == EvoSoulSword.mietitriceDellAlba) {
            return 3;
        }
        if (evoluzione == EvoSoulSword.lamaDiSangue) {
            return 2;
        }
        return 1;
    }

    // Carica l'immagine dalla cartella resources e la conserva per richieste successive.
    private Image caricaRisorsa(String percorso) {
        if (immagini.containsKey(percorso)) {
            return immagini.get(percorso);
        }
        InputStream stream = ArchivioSprite.class.getResourceAsStream(percorso);
        if (stream == null) {
            throw new IllegalArgumentException("Risorsa grafica non trovata: " + percorso);
        }
        Image immagine = new Image(stream);
        immagini.put(percorso, immagine);
        return immagine;
    }
}
