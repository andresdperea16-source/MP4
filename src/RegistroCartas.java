import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * RegistroCartas — Singleton que carga dinamicamente las clases
 * de cartas magicas y trampa usando la Reflection API de Java.
 *
 * Uso de Reflection:
 *   - Class.forName(nombreClase)  → obtiene la Class<?> en tiempo de ejecucion
 *   - getDeclaredConstructor()    → obtiene el constructor sin parametros
 *   - constructor.newInstance()   → instancia el objeto sin saber su tipo en compile-time
 *
 * Esto permite agregar nuevas cartas al juego editando solo los archivos
 * cartas/magicas.txt o cartas/trampas.txt, SIN recompilar ni tocar CartaFactory.
 */
public class RegistroCartas {

    // ── Singleton ──────────────────────────────────────────────────────────
    private static RegistroCartas instancia;

    public static RegistroCartas getInstancia() {
        if (instancia == null) {
            instancia = new RegistroCartas();
        }
        return instancia;
    }

    // ── Mapa nombre → constructor de la carta ──────────────────────────────
    // HashMap: indexa cartas por nombre para acceso O(1)
    private final Map<String, Constructor<?>> registroMagicas  = new HashMap<>();
    private final Map<String, Constructor<?>> registroTrampas  = new HashMap<>();

    private RegistroCartas() {
        cargarDesdeArchivo("cartas/magicas.txt",  registroMagicas);
        cargarDesdeArchivo("cartas/trampas.txt",  registroTrampas);
    }

    /**
     * Lee un archivo de texto linea a linea.
     * Cada linea es el nombre exacto de la clase Java de la carta.
     * Usa Reflection para obtener y almacenar su Constructor.
     */
    private void cargarDesdeArchivo(String ruta, Map<String, Constructor<?>> registro) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                try {
                    // ── REFLECTION: paso 1 ──────────────────────────────
                    // Class.forName busca la clase por su nombre en tiempo de ejecucion.
                    // Equivale a escribir PotOfGreed.class, pero sin conocer el tipo
                    // en tiempo de compilacion.
                    Class<?> clase = Class.forName(linea);

                    // ── REFLECTION: paso 2 ──────────────────────────────
                    // Obtenemos el constructor publico sin argumentos de esa clase.
                    Constructor<?> constructor = clase.getDeclaredConstructor();
                    constructor.setAccessible(true); // por si fuera package-private

                    // La clave del mapa es el nombre de la carta (lo obtenemos
                    // instanciando una vez y llamando getNombre()).
                    Carta carta = (Carta) constructor.newInstance();
                    registro.put(carta.getNombre(), constructor);

                    System.out.println("[RegistroCartas] Cargada: " + carta.getNombre()
                            + " ← " + linea);

                } catch (ClassNotFoundException e) {
                    System.err.println("[RegistroCartas] Clase no encontrada: " + linea);
                } catch (NoSuchMethodException e) {
                    System.err.println("[RegistroCartas] Sin constructor vacio: " + linea);
                } catch (Exception e) {
                    System.err.println("[RegistroCartas] Error cargando " + linea
                            + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[RegistroCartas] No se pudo leer " + ruta
                    + ": " + e.getMessage());
        }
    }

    // ── API publica ────────────────────────────────────────────────────────

    /**
     * Crea una instancia de CartaMagica por nombre usando Reflection.
     * Devuelve null si el nombre no esta registrado.
     */
    public CartaMagica crearMagica(String nombre) {
        Constructor<?> ctor = registroMagicas.get(nombre);
        if (ctor == null) return null;
        try {
            // ── REFLECTION: paso 3 ──────────────────────────────────────
            // newInstance() invoca el constructor y devuelve el objeto.
            return (CartaMagica) ctor.newInstance();
        } catch (Exception e) {
            System.err.println("[RegistroCartas] Error instanciando magica: " + nombre);
            return null;
        }
    }

    /**
     * Crea una instancia de CartaTrampa por nombre usando Reflection.
     * Devuelve null si el nombre no esta registrado.
     */
    public CartaTrampa crearTrampa(String nombre) {
        Constructor<?> ctor = registroTrampas.get(nombre);
        if (ctor == null) return null;
        try {
            return (CartaTrampa) ctor.newInstance();
        } catch (Exception e) {
            System.err.println("[RegistroCartas] Error instanciando trampa: " + nombre);
            return null;
        }
    }

    /** Verifica si una magica esta registrada. */
    public boolean tieneMagica(String nombre) {
        return registroMagicas.containsKey(nombre);
    }

    /** Verifica si una trampa esta registrada. */
    public boolean tieneTrampa(String nombre) {
        return registroTrampas.containsKey(nombre);
    }
}
