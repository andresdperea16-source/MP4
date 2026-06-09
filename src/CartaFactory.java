/**
 * CartaFactory — Factory Method para crear cartas.
 *
 * Las cartas magicas y trampa se crean via RegistroCartas (Reflection API),
 * por lo que agregar una nueva carta solo requiere:
 *   1. Crear la clase Java (p.ej. ChangeOfHeart.java)
 *   2. Escribir su nombre en cartas/magicas.txt o cartas/trampas.txt
 *   3. Recompilar esa clase nueva — CartaFactory NO se toca.
 *
 * Los monstruos siguen resolviendo desde InicializadorMazo porque sus stats
 * estan definidos ahi y no se cargan por Reflection.
 */
public class CartaFactory {

    /**
     * Crea una carta a partir de un codigo con formato "TIPO:NombreCarta".
     *   M → Monstruo
     *   G → CartaMagica  (via Reflection)
     *   T → CartaTrampa  (via Reflection)
     */
    public static Carta crearCarta(String codigo) {

        if (codigo == null || !codigo.contains(":")) {
            return null;
        }

        String tipo   = codigo.substring(0, 1);
        String nombre = codigo.substring(2);

        switch (tipo) {

            case "M":
                return crearMonstruo(nombre);

            case "G":
                // Delega en RegistroCartas para carga dinamica con Reflection
                CartaMagica magica = RegistroCartas.getInstancia().crearMagica(nombre);
                if (magica == null) {
                    System.err.println("[CartaFactory] Magia no encontrada en registro: " + nombre);
                }
                return magica;

            case "T":
                // Delega en RegistroCartas para carga dinamica con Reflection
                CartaTrampa trampa = RegistroCartas.getInstancia().crearTrampa(nombre);
                if (trampa == null) {
                    System.err.println("[CartaFactory] Trampa no encontrada en registro: " + nombre);
                }
                return trampa;
        }

        return null;
    }

    /** Recrea un monstruo desde InicializadorMazo buscando por nombre. */
    private static Monstruo crearMonstruo(String nombre) {
        Mazo mazo = InicializadorMazo.crearMazoCompleto();
        for (Carta c : mazo.getCartas()) {
            if (c instanceof Monstruo && c.getNombre().equals(nombre)) {
                return (Monstruo) c;
            }
        }
        return null;
    }
}
