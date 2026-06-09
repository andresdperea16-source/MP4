# MP4
## Commit 1

# Implementación y justificación de estructuras de datos

Durante esta iteración se analizaron las estructuras utilizadas en el proyecto con el objetivo de reducir el uso excesivo de ArrayList e incorporar estructuras más adecuadas según la funcionalidad requerida.

1. Stack para la gestión de mazos

La clase Mazo fue modificada para reemplazar la estructura utilizada anteriormente por una implementación basada en Stack<Carta>.

El mazo funciona similarmente a una pila, las cartas se agregan al mazo y posteriormente se extraen desde la parte superior mediante robo. Con stack nos permite modelas el comportamiento usando push() y pop()

Se eliminó el uso de remove(0) sobre listas, evitando el desplazamiento innecesario de elementos cada vez que se roba una carta.

2. Queue para el historial de eventos del duelo

Se añadio una estructura Queue<String> implementada mediante LinkedList para almacenar los eventos importantes ocurridos durante la partida.

Los eventos del juego ocurren en orden cronológico y deben conservar dicho orden para consultas posteriores. Una cola permite registrar cada acción manteniendo exactamente la secuencia en que ocurrió.

Ejemplos de eventos registrados:

Cartas jugadas.
Activación de trampas.
Ataques realizados.
Destrucción de monstruos.
Cambios de turno.

3. LinkedList como implementación de Queue

LinkedList implementa la interfaz Queue y permite inserciones eficientes al final de la cola mediante offer().

Mientras Queue define el comportamiento esperado de la colección, LinkedList proporciona la implementación concreta utilizada para almacenar los eventos.


4. Set para registro de cartas utilizadas

El objetivo es almacenar únicamente cartas únicas utilizadas durante la partida. No es necesario registrar múltiples veces una misma carta dentro de esta colección.

Esto nos evita automaticamente elementos duplicados, facilita la obtencion de estadisticas sobre las cartas usadas y simplifica la validacion de existencia de una carta dentro del registro

5. HashSet como implementacion del conjunto de cartas utilizadas 

HashSet implementa la interfaz Set y proporciona inserciones y búsquedas rápidas manteniendo la restricción de elementos únicos.

Mientras Set define el comportamiento de conjunto sin duplicados, HashSet proporciona la implementación concreta utilizada para almacenar las cartas utilizadas.

6. LinkedList para la mano de los jugadores

En ControladorMenu se modificó la creación de los jugadores:

Jugador j1 = new Jugador(n1, 8000, new LinkedList<>(), null, new Campo());
Jugador j2 = new Jugador(n2, 8000, new LinkedList<>(), null, new Campo());

La mano del jugador es una colección dinámica donde constantemente se agregan y eliminan cartas durante la partida.

* Esto puede que sea modificado a posterior ya que puede que encontremos otras partes en las cuales usar diferentes estructuras a pesar de ya haberlo analizado.


## Autores

* Santiago Romero Restrepo - 2559949
* Diego Fernando Pérez - 2559956
* Andrés David Perea - 2559770

