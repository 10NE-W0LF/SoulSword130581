package it.unicam.cs.mpgc.rpg130581.interfaccia;

import it.unicam.cs.mpgc.rpg130581.modello.partita.ScenarioGioco;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.EvoluzioneSoulSword;
import it.unicam.cs.mpgc.rpg130581.modello.personaggi.TipoVampiro;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ArchivioSprite {

    private final Map<String, Image> immagini = new HashMap<String, Image>();

    public Image eroe(EvoluzioneSoulSword evoluzione, TipoAnimazione animazione) {
        int livelloAsset = livelloAssetEroe(evoluzione);
        return caricaRisorsa("/sprites/hero/level" + livelloAsset + "/" + animazione.getNomeFile() + ".png");
    }

    public Image vampiro(TipoVampiro tipo, TipoAnimazione animazione) {
        return caricaRisorsa("/sprites/vampires/vampire" + tipo.getLivelloAsset() + "/" + animazione.getNomeFile() + ".png");
    }

    public Image sfondo(ScenarioGioco scenario) {
        switch (scenario) {
            case CASTELLO:
                return caricaRisorsa("/sfondi/castle.png");
            case TERRAZZA:
                return caricaRisorsa("/sfondi/terrace.png");
            case SALA_DEL_TRONO:
                return caricaRisorsa("/sfondi/throne_room.png");
            case MARGINE_FORESTA_MORTA:
            case PROFONDITA_FORESTA_MORTA:
            default:
                return caricaRisorsa("/sfondi/dead_forest.png");
        }
    }

    public Image iconaSoulSword() {
        return caricaRisorsa("/sfondi/SoulSwordIcon.png");
    }

    private int livelloAssetEroe(EvoluzioneSoulSword evoluzione) {
        if (evoluzione == EvoluzioneSoulSword.MIETITRICE_DELL_ALBA) {
            return 3;
        }
        if (evoluzione == EvoluzioneSoulSword.LAMA_DI_SANGUE) {
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
