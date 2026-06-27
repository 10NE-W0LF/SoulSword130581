# SoulSword

E' un gioco action RPG top-down realizzato in Java/JavaFX per interfaccia grafica, ispirato ad atmosfere gotiche e alla caccia contro i vampiri. Il giocatore controlla Zephyr Ragnar, l'unico essere umano ad aver risvegliato la SoulSword, un potere capace di distruggere i vampiri.

Tramite un sistema di livelli, esperienza e statistiche, il giocatore migliora progressivamente il proprio eroe. La SoulSword evolve insieme a Zephyr Ragnar, cambiando forma e potenza durante la partita.

## Descrizione

Il gioco e' ambientato in luoghi oscuri dominati dai vampiri. Zephyr Ragnar deve attraversare diversi scenari, sconfiggere nemici sempre piu' forti e arrivare allo scontro finale contro l'Arciduca Vampiro.

Il protagonista parte dal livello 1 con la prima forma della SoulSword. Salendo di livello aumenta vita massima e danno, mentre la SoulSword cambia evoluzione:

- **SoulSword: Risvegliata**: forma iniziale dell'eroe.

- **SoulSword: Lama di Sangue**: evoluzione raggiunta dal livello 10.

- **SoulSword: Mietitrice dell'Alba**: evoluzione raggiunta dal livello 20.

Il livello massimo dell'eroe e' 30. Ogni volta che Zephyr Ragnar sale di livello, la sua vita viene riportata al massimo e ottiene 1 punto statistica da assegnare a Forza o Salute.

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

## Funzionalita' Principali

### Sistema di personaggio RPG con:

- Livello di esperienza
- Statistiche dell'eroe: Forza, che aumenta il danno, e Salute, che aumenta la vita massima
- 1 punto statistica ottenuto a ogni level up e assegnabile dal menu di selezione scenario
- Evoluzione della SoulSword in base al livello
- Cambio automatico dello sprite dell'eroe quando la SoulSword evolve

### Sistema di combattimento semplice

- Attacco base con la SoulSword
- Danno dell'eroe influenzato dal livello e dall'evoluzione della SoulSword
- Danno dei vampiri influenzato dal loro livello
- I vampiri possono danneggiare l'eroe a contatto

### Consumabili

- I vampiri hanno una bassa probabilita' di rilasciare una **Pozione Cura** quando vengono sconfitti
- L'eroe puo' portare al massimo 3 Pozioni Cura
- Le pozioni non usate restano disponibili negli scenari successivi
- La Pozione Cura recupera una quantita' moderata di vita e puo' essere usata durante il gioco con il tasto **T**

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
- Gli scenari vengono sbloccati in ordine: per accedere allo scenario successivo bisogna completare quello corrente

### Missione

- Superare i cinque scenari
- Eliminare i vampiri presenti nelle varie aree
- Raggiungere il livello necessario per affrontare il boss
- Sconfiggere l'Arciduca Vampiro nella Throne Room

### Salvataggi

- Salvataggio della partita in formato JSON
- File di salvataggio locale in `data/save.json`
- I progressi gia' salvati restano disponibili anche se l'eroe viene sconfitto
- Le Pozioni Cura possedute vengono salvate insieme ai progressi dell'eroe

### Grafica
Tutte le sprite usate in questo gioco sono state prese da questo sito https://craftpix.net
- Sprite dell'eroe provenienti dal pacchetto Craftpix Swordsman 
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
- **Salva** per salvare la partita dal menu di selezione scenario.
- **Carica partita** per caricare un salvataggio dal menu principale.

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
- Gradle Wrapper 9.1.0
- JSON per la persistenza

## Uso di strumenti di AI

Per lo sviluppo di SoulSword e' stato utilizzato un assistente AI come supporto alla programmazione. L'intelligenza artificiale e' stata impiegata come strumento tecnico per superare ostacoli implementativi, chiarire concetti teorici e migliorare alcune parti del codice, mantenendo la piena paternita' logica e progettuale del software.

Attivita' svolte con il supporto dell'AI:

- **Debugging e risoluzione errori**: supporto nell'analisi e nella correzione di problemi emersi durante lo sviluppo.
- **Approfondimento concettuale**: utilizzo dell'AI per comprendere meglio persistenza dei dati, separazione tra modello, motore, interfaccia e principi Clean/SOLID.
- **Sviluppo dell'interfaccia grafica**: supporto nella realizzazione e rifinitura delle schermate JavaFX, dei menu, dell'HUD di gioco e della gestione degli sprite.
- **Bilanciamento delle meccaniche**: supporto nella definizione di livelli, statistiche, evoluzioni della SoulSword, vampiri, scenari e consumabili.
