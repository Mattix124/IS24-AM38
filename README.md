# Prova finale Ingegneria del Software 2024

<img src="https://www.craniocreations.it/storage/media/products/19/41/Codex_scatola+ombra.png" alt="Game box" width="350"/>

## Team AM38

- Mattia Corona ([@Mattix124](https://github.com/Mattix124))<br> mattia.corona@mail.polimi.it
- Davide Grisanti ([@davide-grisanti](https://github.com/davide-grisanti))<br> davide.grisanti@mail.polimi.it
- Tommaso Mori ([@moritommaso](https://github.com/moritommaso))<br> tommaso.mori@mail.polimi.it
- Francesco Giuseppe Spelta([@basketshoe](https://github.com/basketshoe))<br> francescogiuseppe.spelta@mail.polimi.it

### About Codex Naturalis

The goal of the project is to create an online version of **Codex Naturalis**, a board game created by [Cranio Creation](https://www.craniocreations.it/).
Rulebooks: [ITA 🇮🇹](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf) [ENG 🇬🇧]()

### Tools

| Tool												| About						|
|:--------------------------------------------------|:--------------------------|
| [IntelliJ IDEA](https://www.jetbrains.com/idea/)	| IDE						|
| [draw.io](https://draw.io)						| UML Class Diagram			|
| [Lucid Charts](https://www.lucidchart.com/)		| UML Sequence Diagram		|
| [maven](https://maven.apache.org/)				| Build automation tool 	|
| [java FX](https://openjfx.io/)					| Graphical Library			|
| [jUnit 5](https://junit.org/junit5/)				| Unit testing framework	|
<br>

### Requirements

| Functionality             |  State  |
|:--------------------------|:-------:|
| Full Rules                |    🟢   |
| UML                       |    🟢   |
| Model                     |    🟢   |
| Controller                |    🟢   |
| TCP                       |    🟢   |
| RMI                       |    🟢   |
| CLI                       |    🟢   |
| GUI                       |    🟢   |
| Multiple games            |    🟢   |
| Chat                      |    🟢   |
| Disconnection Resilience  |    🟢   |
| Reconnection              |    🟡   |
| Game saving               |    🔴   |

🟢 Fully implemented
🟡 WIP
🔴 Not implemented

### JAR execution

To run the server:
```java -jar <name>.jar server```

To run the client:
```java -jar <name>.jar client [connection type] [interface] [ip]```
* Connection type: [TCP/RMI]
* Interface: [CLI/GUI]
* IP: server address (not required, default is localhost)

### License

> NOTA: Codex Naturalis è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.
