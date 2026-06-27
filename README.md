# SoulSword

E' un gioco action RPG top-down realizzato in Java/JavaFX per interfaccia grafica, ispirato ad atmosfere gotiche e alla caccia contro i vampiri. Il giocatore controlla Zephyr Ragnar, l'unico essere umano ad aver risvegliato la SoulSword, un potere capace di distruggere i vampiri.

Tramite un sistema di livelli, esperienza e statistiche, il giocatore migliora progressivamente il proprio eroe. La SoulSword evolve insieme a Zephyr Ragnar, cambiando forma e potenza durante la partita.

## Descrizione

Il gioco e' ambientato in luoghi oscuri dominati dai vampiri. Zephyr Ragnar deve attraversare diversi scenari, sconfiggere nemici sempre piu' forti e arrivare allo scontro finale contro l'Arciduca Vampiro.

Il protagonista parte dal livello 1 con la prima forma della SoulSword. Salendo di livello aumenta vita massima e danno, mentre la SoulSword cambia evoluzione:

- **SoulSword I**: forma iniziale dell'eroe.

- **SoulSword II**: evoluzione raggiunta dal livello 10.

- **SoulSword III**: evoluzione raggiunta dal livello 20.

Il livello massimo dell'eroe e' 30. Ogni volta che Zephyr Ragnar sale di livello, la sua vita viene riportata al massimo.

## Come eseguire il progetto

### Prerequisiti

- Java 25 LTS
- Gradle Wrapper incluso nel progetto
- Connessione internet al primo avvio, se Gradle e JavaFX non sono gia' presenti in cache

### Istruzioni

```bash
git clone https://github.com/10NE-W0LF/SoulSword130581.git
cd SoulSword130581
```

### Build del Progetto

```bash
./gradlew build
```

Su Windows:

```cmd
gradlew.bat build
```

### Esecuzione

```bash
./gradlew run
```

Su Windows:

```cmd
gradlew.bat run
```

## Funzionalita' Principali

### Sistema di personaggio RPG con:

- Livello di esperienza
- Statistiche dell'eroe: vita e danno
- Evoluzione della SoulSword in base al livello
- Cambio automatico dello sprite dell'eroe quando la SoulSword evolve

### Sistema di combattimento semplice

- Attacco base con la SoulSword
- Danno dell'eroe influenzato dal livello e dall'evoluzione della SoulSword
- Danno dei vampiri influenzato dal loro livello
- I vampiri possono danneggiare l'eroe a contatto

### Nemici

- **Vampiro**: nemico base con livello casuale tra 1 e 6
- **EliteVampiro**: nemico avanzato con livello casuale tra 7 e 15
- **Arciduca Vampiro**: boss finale affrontabile con eroe almeno al livello 20

### Scenari

- **Dead Forest**: primo scenario della foresta morta
- **Deep Dead Forest**: seconda parte della foresta morta
- **Castle**: scenario intermedio nel castello
- **Terrace**: scenario avanzato con nemici piu' forti
- **Throne Room**: scenario finale con l'Arciduca Vampiro

### Missione

- Superare i cinque scenari
- Eliminare i vampiri presenti nelle varie aree
- Raggiungere il livello necessario per affrontare il boss
- Sconfiggere l'Arciduca Vampiro nella Throne Room

### Salvataggi

- Salvataggio della partita in formato JSON
- File di salvataggio locale in `data/save.json`
- I progressi gia' salvati restano disponibili anche se l'eroe viene sconfitto

### Grafica

- Sprite dell'eroe provenienti dal pacchetto Craftpix Swordsman
- Sprite dei vampiri provenienti dal pacchetto Craftpix Vampire
- Sfondi per foresta, castello, terrazza e sala del trono
- Animazioni tramite sprite sheet

### Comandi

- **WASD** o **Frecce direzionali** per muoversi.
- **SHIFT** per correre.
- **Spazio** per attaccare.
- **ESC** per mettere in pausa.
- **Salva stato eroe** per salvare la partita.
- **Carica partita** per caricare un salvataggio.

## Struttura del progetto

- `avvio`: contiene il punto di ingresso dell'applicazione.
- `modello`: contiene personaggi, scenari, stato partita ed esito dei combattimenti.
- `motore`: contiene movimento, combattimento, generazione nemici e gestione degli scenari.
- `persistenza`: contiene il sistema di salvataggio e caricamento JSON.
- `interfaccia`: contiene la GUI JavaFX, la vista di gioco e la gestione degli sprite.
- `resources`: contiene sprite, animazioni, sfondi e icona.

## Tecnologie utilizzate

- Java 25
- JavaFX 25.0.3
- Gradle Wrapper
- JSON per la persistenza

## Uso di strumenti di AI

Il progetto e' stato realizzato con supporto AI per chiarire dubbi, impostare alcune parti della GUI JavaFX e riuscirea a risolvere e comprendere errori di codice.
