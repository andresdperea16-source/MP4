# Yu-Gi-Oh! — Mini Proyecto 4

Juego de duelo de cartas basado en Yu-Gi-Oh! desarrollado en Java 21 para la asignatura Programación Orientada a Eventos.

## Integrantes

|           Nombre         |  Código |
|--------------------------|---------|
| Andres David Perea Rios  | 2559770 |
| Diego Fernando Pérez     | 2559956 |
| Santiago Romero Restrepo | 2559949 |

---

## Instrucciones de ejecución

### Requisitos
- Java 21 o superior
- (Opcional) Un IDE como IntelliJ IDEA o Eclipse

### Compilar y ejecutar

```bash
# Desde la raiz del proyecto, compilar todo el src/
javac -d bin src/*.java

# Ejecutar
java -cp bin App
```

> Los archivos de cartas (`cartas/magicas.txt`, `cartas/trampas.txt`) deben estar
> en el directorio de trabajo (donde se ejecuta el comando `java`).

---

## Estructuras de datos — Justificación

El proyecto usa cuatro estructuras de datos, cada una donde tiene sentido semántico:

### 1. `Queue<String>` — Historial de eventos (`Juego.java`)
El historial de eventos del turno usa una cola (`LinkedList` como implementación de `Queue`).
**Justificación:** Los eventos se procesan en orden FIFO — el primero en ocurrir es el primero en mostrarse en el log. Una cola modela exactamente esa semántica. Un `ArrayList` serviría, pero la cola deja clara la intención de "procesamiento en orden de llegada".

### 2. `Set<String>` — Cartas ya utilizadas (`Juego.java`)
Se usa un `HashSet<String>` para registrar los nombres de cartas activadas durante la partida.
**Justificación:** El set garantiza unicidad sin duplicados y la verificación `contains()` es O(1). Semánticamente modela "conjunto de cartas únicas ya jugadas", que es exactamente un conjunto matemático.

### 3. `HashMap<String, Constructor<?>>` — Registro de cartas (`RegistroCartas.java`)
El registro de Reflection usa un `HashMap` para indexar constructores de cartas por nombre.
**Justificación:** Se necesita acceso por clave (nombre de carta) en O(1). Un HashMap es la estructura natural para este índice. Permite crear cualquier carta por nombre sin if/switch.

### 4. `LinkedList` — Mano del jugador (`ControladorMenu.java`, `Juego.java`)
La mano de cada jugador y la implementación interna de la `Queue` de historial usan `LinkedList`.
**Justificación:** La mano del jugador necesita inserciones y eliminaciones frecuentes en cualquier posición (robar carta, jugar carta). `LinkedList` es O(1) para esas operaciones, mientras que un `ArrayList` sería O(n) por el desplazamiento. También implementa `Queue` de forma natural al soportar operaciones en ambos extremos.

### 5. `Stack` — Mazo de robo (`Mazo.java`)
El mazo del jugador modela una pila: las cartas se sacan desde la parte superior.
**Justificación:** Un mazo de Yu-Gi-Oh funciona como una pila LIFO — robas la carta de arriba. La `Stack` o `LinkedList` usada como pila modela exactamente este comportamiento.

---

## Patrones de diseño implementados

### 1. Singleton — `GestorArchivos` y `RegistroCartas`
**Ubicación:** `src/GestorArchivos.java`, `src/RegistroCartas.java`
**Por qué:** Solo debe existir una instancia del gestor de archivos (para evitar condiciones de carrera en escritura) y del registro de cartas (para que la carga por Reflection ocurra una sola vez al arrancar). Se penaliza el abuso de Singleton, por eso se usó solo donde garantiza un recurso compartido único.

### 2. Memento — `JuegoMemento`
**Ubicación:** `src/JuegoMemento.java`, `src/Juego.java` (métodos `guardarEstado` / `restaurarEstado`)
**Por qué:** El guardado de partida necesita capturar una instantánea completa del estado del juego sin exponer sus internos. El Memento encapsula ese estado y lo entrega a `GestorArchivos` para serialización.

### 3. Factory Method — `CartaFactory`
**Ubicación:** `src/CartaFactory.java`
**Por qué:** Centraliza la creación de cartas. El cliente solo llama `CartaFactory.crearCarta("G:Pot of Greed")` sin saber qué clase concreta se instancia. Con Reflection, el factory delega la construcción de magias/trampas a `RegistroCartas`, eliminando cualquier `switch`.

---

## Reflection API — Carga dinámica de cartas

**Ubicación:** `src/RegistroCartas.java`, `cartas/magicas.txt`, `cartas/trampas.txt`

Al iniciar el juego, `RegistroCartas` lee los archivos de texto y, para cada línea (nombre de clase), ejecuta:

```java
Class<?>    clase       = Class.forName(nombreClase);
Constructor<?> ctor     = clase.getDeclaredConstructor();
Carta       instancia   = (Carta) ctor.newInstance();
```

Esto permite agregar una nueva carta al juego sin recompilar `CartaFactory`:
1. Crear `NuevaCarta.java` que extienda `CartaMagica` o `CartaTrampa`.
2. Compilar solo esa clase nueva.
3. Agregar su nombre al `.txt` correspondiente.

**Clases usadas de `java.lang.reflect`:** `Class`, `Constructor`.
**Métodos clave:** `Class.forName()`, `getDeclaredConstructor()`, `setAccessible()`, `newInstance()`.

---

## Archivos de cartas (`/cartas`)

```
cartas/
  magicas.txt   ← una clase de CartaMagica por línea
  trampas.txt   ← una clase de CartaTrampa por línea
```

**`magicas.txt`:**
```
PotOfGreed
DianKetotheCureMaster
```

**`trampas.txt`:**
```
TrapHole
MirrorForce
```

---

## Tablero KanbanFlow

[Ver tablero del equipo](https://sharing.clickup.com/90171297915/l/h/2kza1m3v-457/077dcd5aae0bfb2)

---

## Historial de resultados

El archivo `resultados.txt` se genera automáticamente al terminar cada duelo. Formato:

```
2026-06-09 18:45 | Yugi vs Kaiba | Ganador: Yugi | Turnos: 12 | LP: Yugi=2400  Kaiba=0
```

---

## Declaración de uso de IA

Se utilizó Claude (Anthropic) para resolver dudas conceptuales sobre la Reflection API (`Class.forName`, `getDeclaredConstructor`) y el patrón Memento. El código fue escrito y adaptado por los integrantes del grupo.
