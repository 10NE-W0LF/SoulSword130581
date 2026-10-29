# SoulSword

È un gioco action RPG top-down realizzato in Java/JavaFX per interfaccia grafica, ispirato ad atmosfere gotiche e alla caccia contro i vampiri. Il giocatore controlla Zephyr Ragnar, l’unico essere umano ad aver risvegliato la SoulSword, un potere capace di distruggere i vampiri.

Tramite un sistema di livelli, esperienza e statistiche, il giocatore migliora progressivamente il proprio eroe. La SoulSword evolve insieme a Zephyr Ragnar, cambiando forma e potenza durante la partita.

## Descrizione

Il gioco è ambientato in luoghi oscuri dominati dai vampiri. Zephyr Ragnar deve attraversare diversi scenari, sconfiggere nemici sempre più forti e arrivare allo scontro finale contro l’Arciduca Vampiro.

Il protagonista parte dal livello 1 con la prima forma della SoulSword. Salendo di livello aumenta vita massima e danno, mentre la SoulSword cambia evoluzione:

- **SoulSword: Risvegliata**: forma iniziale dell’eroe.

- **SoulSword: Lama di Sangue**: evoluzione raggiunta dal livello 10.

- **SoulSword: Mietitrice dell’Alba**: evoluzione raggiunta dal livello 20.

Il livello massimo dell’eroe è 30. Ogni volta che Zephyr Ragnar sale di livello, la sua vita viene riportata al massimo e ottiene 1 punto statistica da assegnare a Forza o Salute.

## Come eseguire il progetto

### Prerequisiti

- Java 25 LTS
- Gradle 9.1.0

### Istruzioni

```bash
git clone https://github.com/10NE-W0LF/SoulSword130581.git
cd SoulSword130581
```

### Build del Progetto

```bash
./gradlew build
```
### Esecuzione

```bash
./gradlew run
```

### Test

I test JUnit sono in `src/test/java`, negli stessi package delle classi verificate.
La suite controlla progressione e statistiche dell’eroe, personaggi, scenari,
pozioni, combattimento, generazione dei nemici, movimento, avanzamento della
partita, annullamento dei tentativi, salvataggi JSON e preferenze di lingua.
Usa generatori casuali controllati, archivi di prova e cartelle temporanee;
non modifica i salvataggi della partita in `data/`.

```bash
./gradlew test
```

`./gradlew build` esegue anche questi test. Il report HTML viene generato in
`build/reports/tests/test/index.html`.

I test che richiedono JavaFX sono separati e richiedono una sessione grafica:

```bash
./gradlew testInterfaccia
```

Verificano menu lingua, conferma e annullamento, riavvio, errori di salvataggio,
pausa, collegamento tra vista e motore, disegno del frame, sprite e cache delle
immagini. Il report è in `build/reports/tests/testInterfaccia/index.html`.
Il punto di ingresso `Main` e le interfacce prive di implementazione non hanno
test di soli getter o di semplice avvio: i loro collegamenti sono verificati
attraverso le implementazioni e i test d’integrazione.

Su Windows la configurazione Gradle rimuove l’attributo DOS di sola lettura dai
soli output in `build/` prima della pulizia, per evitare errori di accesso sui
risultati generati.

## Funzionalità Principali

### Sistema di personaggio RPG con:

- Livello di esperienza
- Statistiche dell’eroe: Forza, che aumenta il danno, e Salute, che aumenta la vita massima
- 1 punto statistica ottenuto a ogni level up e assegnabile dal menu di selezione scenario
- Evoluzione della SoulSword in base al livello
- Cambio automatico dello sprite dell’eroe quando la SoulSword evolve

### Sistema di combattimento semplice

- Attacco base con la SoulSword
- Danno dell’eroe influenzato dal livello e dall’evoluzione della SoulSword
- Danno dei vampiri influenzato dal loro livello
- I vampiri possono danneggiare l’eroe a contatto

### Consumabili

- I vampiri hanno una bassa probabilità di rilasciare una **Pozione Cura** quando vengono sconfitti
- L’eroe può portare al massimo 3 Pozioni Cura
- Le pozioni non usate restano disponibili negli scenari successivi
- La Pozione Cura recupera una quantità moderata di vita e può essere usata durante il gioco con il tasto **T**

### Nemici

- **Vampiro**: nemico base con livello casuale tra 1 e 6
- **EliteVampiro**: nemico avanzato con livello casuale tra 7 e 15
- **Arciduca Vampiro**: boss finale affrontabile con eroe almeno al livello 20

### Scenari

- **Dead Forest**: primo scenario della foresta morta
- **Deep Dead Forest**: seconda parte della foresta morta
- **Castle**: scenario intermedio nel castello
- **Terrace**: scenario avanzato con nemici più forti
- **Throne Room**: scenario finale con l’Arciduca Vampiro
- Gli scenari vengono sbloccati in ordine: per accedere allo scenario successivo bisogna completare quello corrente

### Missione

- Superare i cinque scenari
- Eliminare i vampiri presenti nelle varie aree
- Raggiungere il livello necessario per affrontare il boss
- Sconfiggere l’Arciduca Vampiro nella Throne Room

### Salvataggi

- Salvataggio della partita in formato JSON
- File di salvataggio locale in `data/save.json`
- I progressi già salvati restano disponibili anche se l’eroe viene sconfitto
- Le Pozioni Cura possedute vengono salvate insieme ai progressi dell’eroe
- Il JSON viene letto con Gson in modalità rigorosa e con controllo dei tipi dei campi
- I salvataggi precedenti, inclusa la vecchia chiave `puntiStatistiche`, restano compatibili
- Un file malformato produce un errore di caricamento e non viene sovrascritto
- La scrittura usa un file temporaneo e una sostituzione atomica, quando supportata dal filesystem

### Grafica
Tutte le sprite usate in questo gioco sono state prese da questo sito https://craftpix.net
- Sprite dell’eroe provenienti dal pacchetto Craftpix Swordsman
- Sprite dei vampiri provenienti dal pacchetto Craftpix Vampire 
- Sfondi per foresta, castello, terrazza e sala del trono  
- Icona della Pozione Cura proveniente dal pacchetto Craftpix Magic Potions scaricato 
- Animazioni tramite sprite sheet 

### Comandi

- **WASD** o **Frecce direzionali** per muoversi.
- **SHIFT** per correre.
- **Spazio** per attaccare.
- **T** per usare una Pozione Cura.
- **ESC** per mettere in pausa.
- **F11** per passare da finestra a schermo intero e viceversa, in tutte le schermate.
- **Schermo intero / Modalità finestra** disponibile solo nel menu principale.
- **Salva** per salvare la partita dal menu di selezione scenario.
- **Carica partita** per caricare un salvataggio dal menu principale.
- **Lingua / Language** nel menu principale apre una schermata dedicata con
  **Italiano**, **English**, **Salva / Save** e **Indietro / Back**.
  Salva applica la lingua a menu, statistiche, pausa, HUD ed esiti e la memorizza
  in `data/preferenze.properties` per i successivi avvii. Indietro annulla la scelta
  non confermata. La lingua viene ricordata anche salvando la partita o vincendo
  uno scenario. Senza preferenze salvate si usa l’italiano.
  Nell’interfaccia inglese gli scenari sono indicati come **Stages**.

La modalità schermo intero mantiene le proporzioni di gioco e menu, con eventuali
bande nere sui monitor con proporzioni diverse. Rimane attiva durante i cambi di
schermata; **ESC** continua ad aprire la pausa anche a schermo intero.

## Struttura del progetto

- `avvio`: contiene il punto di ingresso e la composizione delle dipendenze concrete.
- `applicazione`: coordina le preferenze e il salvataggio della sessione, senza dipendere da JavaFX.
- `modello`: contiene personaggi, scenari, stato partita, eventi, esiti e lingua.
- `motore`: contiene movimento, combattimento, generazione nemici e gestione degli scenari.
- `persistenza`: contiene il sistema di salvataggio e caricamento JSON.
- `interfaccia`: contiene viste separate, coordinatore, localizzazione, rendering e sprite.
- `resources`: contiene traduzioni, fogli CSS, sprite, animazioni, sfondi e icone.

Schema dei file principali; i package Java partono da
`src/main/java/it/unicam/cs/mpgc/rpg130581/`:

```text
src/
├── main/
│   ├── java/it/unicam/cs/mpgc/rpg130581/
│   │   ├── avvio/          Main, AvvioJavaFx
│   │   ├── applicazione/   PreferenzeGioco, SalvataggioSessione, EsitoSalvataggio
│   │   ├── modello/
│   │   │   ├── personaggi/ Personaggio, Eroe, Vampiro, TipoVampiro, EvoSoulSword
│   │   │   ├── partita/    StatoPartita, ScenarioGioco, FaseGioco, EventoGioco, TipoEventoGioco
│   │   │   ├── combattimento/ EsitoCombattimento, PozioneCura
│   │   │   └── impostazioni/  Lingua
│   │   ├── motore/         MotoreGioco, Combattimento, ServizioCombattimento,
│   │   │                   GenerazioneVampiri, GeneratoreV, Posizione, VampiroNelMondo
│   │   ├── persistenza/    ArchivioPartita, Salvataggi, CodecPartitaJson,
│   │   │                   ArchivioPreferenze, SalvataggiPreferenze, ScritturaAtomica
│   │   └── interfaccia/    AppSoulSword, MenuPrincipale, MenuScenari, MenuLingua,
│   │                       MenuPausa, MenuEsito, SchermataPartita, GestoreFinestra,
│   │                       VistaGioco, RenderGioco, TestiInterfaccia e supporto grafico
│   └── resources/
│       ├── lingue/         testi_it.properties, testi_en.properties
│       ├── stili/          interfaccia.css, menu-lingua.css
│       └── sprites/, sfondi/, consumabili/
└── test/java/it/unicam/cs/mpgc/rpg130581/
    ├── applicazione/
    ├── interfaccia/
    ├── modello/
    ├── motore/
    └── persistenza/
```

La documentazione di dettaglio è nella [wiki del progetto](https://github.com/10NE-W0LF/SoulSword130581/wiki).

## Tecnologie utilizzate

- Java 25
- JavaFX 25.0.3
- Gradle Wrapper 9.1.0
- JSON per la persistenza
- Gson 2.13.2 per la conversione e la validazione del JSON

## Organizzazione del codice

Le schermate `MenuPrincipale`, `MenuScenari`, `MenuLingua`, `MenuPausa` e
`MenuEsito` presentano i controlli e ricevono le azioni dal coordinatore
`AppSoulSword`. `SchermataPartita` collega tastiera e pausa alla vista di gioco;
`GestoreFinestra` si occupa di scala e schermo intero. Gli stili dei controlli
sono nei fogli CSS in `src/main/resources/stili/`; il renderer disegna sul Canvas.

`AvvioJavaFx` crea e collega le dipendenze concrete senza variabili globali
modificabili. Il motore accetta gli archivi, un generatore casuale, un orologio
e i contratti `GenerazioneVampiri` e `Combattimento`. Nei test si possono quindi
controllare sia le estrazioni sia il tempo senza attendere realmente le ricariche.

Gli esiti sono rappresentati da `EventoGioco`, con un tipo e i relativi dati.
`TestiInterfaccia` li traduce soltanto nella GUI: le regole di gioco non contengono
frasi italiane o inglesi. `Salvataggi` gestisce il file e `CodecPartitaJson` lo
schema JSON. `PreferenzeGioco` e `SalvataggioSessione` distinguono gli errori
della partita da quelli delle preferenze.

## Uso di strumenti di AI

Per lo sviluppo di SoulSword è stato utilizzato un assistente AI come supporto alla programmazione. L’intelligenza artificiale è stata impiegata come strumento tecnico per superare ostacoli implementativi, chiarire concetti teorici e migliorare alcune parti del codice, mantenendo la piena paternità logica e progettuale del software.

Attività svolte con il supporto dell’AI:

- **Debugging e risoluzione errori**: supporto nell’analisi e nella correzione di problemi emersi durante lo sviluppo.
- **Approfondimento concettuale**: utilizzo dell’AI per comprendere meglio persistenza dei dati, separazione tra modello, motore, interfaccia e principi Clean/SOLID.
- **Sviluppo dell’interfaccia grafica**: supporto nella realizzazione e rifinitura delle schermate JavaFX, dei menu, dell’HUD di gioco e della gestione degli sprite.
- **Bilanciamento delle meccaniche**: supporto nella definizione di livelli, statistiche, evoluzioni della SoulSword, vampiri, scenari e consumabili.
