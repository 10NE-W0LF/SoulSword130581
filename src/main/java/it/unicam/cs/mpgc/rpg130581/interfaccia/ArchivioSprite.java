package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ArchivioSprite {

    private final Map<String, Image> immagini = new HashMap<String, Image>();

    public Image eroe(EvoSoulSword evoluzione, TipoAnimazione animazione) {
        int livelloAsset = livelloAssetEroe(evoluzione);
        return caricaRisorsa("/sprites/hero/level" + livelloAsset + "/" + animazione.getNomeFile() + ".png");
    }

    public Image vampiro(TipoVampiro tipo, TipoAnimazione animazione) {
        return caricaRisorsa("/sprites/vampires/vampire" + tipo.getLivelloAsset() + "/" + animazione.getNomeFile() + ".png");
    }

    public Image sfondo(ScenarioGioco scenario) {
        switch (scenario) {
            case castello:
                return caricaRisorsa("/sfondi/castle.png");
            case terrazza:
                return caricaRisorsa("/sfondi/terrace.png");
            case salaDelTrono:
                return caricaRisorsa("/sfondi/throne_room.png");
            case margineForestaMorta:
            case profonditaForestaMorta:
            default:
                return caricaRisorsa("/sfondi/dead_forest.png");
        }
    }

    public Image iconaSoulSword() {
        return caricaRisorsa("/sfondi/SoulSwordIcon.png");
    }

    public Image pozioneCura() {
        return caricaRisorsa("/consumabili/pozione_cura.png");
    }

    private int livelloAssetEroe(EvoSoulSword evoluzione) {
        if (evoluzione == EvoSoulSword.mietitriceDellAlba) {
            return 3;
        }
        if (evoluzione == EvoSoulSword.lamaDiSangue) {
            return 2;
        }
        return 1;
    }

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
