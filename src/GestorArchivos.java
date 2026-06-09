import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que se encarga de guardar y cargar partidas,
 * y de manejar el historial de resultados.
 * Formato del archivo de partida (CSV simple):
 *   Linea 1: nombres de jugadores y LP
 *   Linea 2: turno actual y quien es el jugador actual (0 o 1)
 *   Linea 3: mano del jugador 1 (nombres de cartas separados por |)
 *   Linea 4: monstruos en campo del jugador 1 (nombre|atk|def|nivel|ataque|yaAtaco)
 *   Linea 5: magias en campo del jugador 1
 *   Linea 6: trampas en campo del jugador 1
 *   Lineas 7-10: lo mismo para jugador 2
 *   Linea 11: cartas restantes en mazo del jugador 1
 *   Linea 12: cartas restantes en mazo del jugador 2
 */

public class GestorArchivos {

    // Nombre fijo del archivo de partida guardada
    private static final String ARCHIVO_PARTIDA = "partida_guardada.csv";
    // Nombre fijo del archivo de historial de resultados
    private static final String ARCHIVO_RESULTADOS = "resultados.txt";

    // =====================================================================
    // GUARDAR PARTIDA
    // =====================================================================
    public static void guardarPartida(Juego juego) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_PARTIDA));

        Jugador j1 = juego.getJugador1();
        Jugador j2 = juego.getJugador2();

        // Determinar quien es el jugador actual (0 = j1, 1 = j2)
        int turnoActual = juego.getJugadorActual() == j1 ? 0 : 1;

        // Linea 1
        pw.println(j1.getNombre() + "," + j1.getPuntosVida() + ","
                + j2.getNombre() + "," + j2.getPuntosVida());

        // Linea 2
        pw.println(juego.getNumeroTurno() + "," + turnoActual);

        // Lineas 3-6
        guardarMano(pw, j1.getMano());
        guardarMonstruosCampo(pw, j1.getCampo().getMonstruos());
        guardarCartasMagicas(pw, j1.getCampo().getCartasMagicas());
        guardarCartasTrampa(pw, j1.getCampo().getCartasTrampa());

        // Lineas 7-10
        guardarMano(pw, j2.getMano());
        guardarMonstruosCampo(pw, j2.getCampo().getMonstruos());
        guardarCartasMagicas(pw, j2.getCampo().getCartasMagicas());
        guardarCartasTrampa(pw, j2.getCampo().getCartasTrampa());

        // Lineas 11-12
        guardarMazo(pw, j1.getMazo());
        guardarMazo(pw, j2.getMazo());

        pw.close();
    }

    private static void guardarMano(PrintWriter pw, List<Carta> mano) {
        // Tipo y nombre de cada carta en la manon: M (monstruo), G (magia), T (trampa)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mano.size(); i++) {
            Carta c = mano.get(i);
            if (i > 0) sb.append("|");
            sb.append(getTipoCarta(c)).append(":").append(c.getNombre());
        }
        pw.println(sb.toString());
    }

    private static void guardarMonstruosCampo(PrintWriter pw, List<Monstruo> monstruos) {
        // Formato: nombre|atk|def|nivel|posAtaque|yaAtaco
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < monstruos.size(); i++) {
            Monstruo m = monstruos.get(i);
            if (i > 0) sb.append("|");
            sb.append(m.getNombre()).append(",")
              .append(m.getAtk()).append(",")
              .append(m.getDef()).append(",")
              .append(m.getNivel()).append(",")
              .append(m.isEnPosicionAtaque() ? "1" : "0").append(",")
              .append(m.isYaAtaco() ? "1" : "0");
        }
        pw.println(sb.toString());
    }

    private static void guardarCartasMagicas(PrintWriter pw, List<CartaMagica> magias) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < magias.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(magias.get(i).getNombre());
        }
        pw.println(sb.toString());
    }

    private static void guardarCartasTrampa(PrintWriter pw, List<CartaTrampa> trampas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trampas.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(trampas.get(i).getNombre());
        }
        pw.println(sb.toString());
    }

    private static void guardarMazo(PrintWriter pw, Mazo mazo) {
        // Guarda el tipo y nombre de cada carta restante en el mazo
        StringBuilder sb = new StringBuilder();
        List<Carta> cartas = mazo.getCartas();
        for (int i = 0; i < cartas.size(); i++) {
            Carta c = cartas.get(i);
            if (i > 0) sb.append("|");
            sb.append(getTipoCarta(c)).append(":").append(c.getNombre());
        }
        pw.println(sb.toString());
    }

    private static String getTipoCarta(Carta c) {
        if (c instanceof Monstruo)   return "M";
        if (c instanceof CartaMagica) return "G";
        return "T"; // CartaTrampa
    }

    // =====================================================================
    // CARGAR PARTIDA
    // =====================================================================
    public static Juego cargarPartida() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PARTIDA));

        // Linea 1
        String linea1 = br.readLine();
        String[] datos1 = linea1.split(",");
        String nombre1 = datos1[0];
        int lp1 = Integer.parseInt(datos1[1]);
        String nombre2 = datos1[2];
        int lp2 = Integer.parseInt(datos1[3]);

        // Linea 2
        String linea2 = br.readLine();
        String[] datos2 = linea2.split(",");
        int numeroTurno = Integer.parseInt(datos2[0]);
        int jugadorActualIdx = Integer.parseInt(datos2[1]);

        // Cargar manos y campos de cada jugador
        List<Carta> manoJ1     = leerMano(br.readLine());
        List<Monstruo> monstJ1 = leerMonstruosCampo(br.readLine());
        List<CartaMagica> magiasJ1 = leerMagias(br.readLine());
        List<CartaTrampa> trampasJ1 = leerTrampas(br.readLine());

        List<Carta> manoJ2     = leerMano(br.readLine());
        List<Monstruo> monstJ2 = leerMonstruosCampo(br.readLine());
        List<CartaMagica> magiasJ2 = leerMagias(br.readLine());
        List<CartaTrampa> trampasJ2 = leerTrampas(br.readLine());

        Mazo mazoJ1 = leerMazo(br.readLine());
        Mazo mazoJ2 = leerMazo(br.readLine());

        br.close();

        // Reconstruir campos
        Campo campoJ1 = new Campo();
        campoJ1.setMonstruos(monstJ1);
        campoJ1.setCartasMagicas(magiasJ1);
        campoJ1.setCartasTrampa(trampasJ1);

        Campo campoJ2 = new Campo();
        campoJ2.setMonstruos(monstJ2);
        campoJ2.setCartasMagicas(magiasJ2);
        campoJ2.setCartasTrampa(trampasJ2);

        // Crear jugadores con los datos cargados
        Jugador j1 = new Jugador(nombre1, lp1, manoJ1, mazoJ1, campoJ1);
        Jugador j2 = new Jugador(nombre2, lp2, manoJ2, mazoJ2, campoJ2);

        // Crear el juego y restaurar el estado del turno
        Juego juego = new Juego(j1, j2, numeroTurno, jugadorActualIdx);

        return juego;
    }

    private static List<Carta> leerMano(String linea) {
        List<Carta> mano = new ArrayList<>();
        if (linea == null || linea.trim().isEmpty()) return mano;
        String[] partes = linea.split("\\|");
        for (String parte : partes) {
            Carta c = crearCartaDesdeCodigo(parte);
            if (c != null) mano.add(c);
        }
        return mano;
    }

    private static List<Monstruo> leerMonstruosCampo(String linea) {
        List<Monstruo> lista = new ArrayList<>();
        if (linea == null || linea.trim().isEmpty()) return lista;
        String[] partes = linea.split("\\|");
        for (String parte : partes) {
            String[] datos = parte.split(",");
            if (datos.length < 6) continue;
            String nombre = datos[0];
            int atk  = Integer.parseInt(datos[1]);
            int def  = Integer.parseInt(datos[2]);
            int nivel = Integer.parseInt(datos[3]);
            boolean posAtaque = datos[4].equals("1");
            boolean yaAtaco   = datos[5].equals("1");
            Monstruo m = new Monstruo(nombre, atk, def, nivel);
            m.setEnPosicionAtaque(posAtaque);
            m.setYaAtaco(yaAtaco);
            lista.add(m);
        }
        return lista;
    }

    private static List<CartaMagica> leerMagias(String linea) {
        List<CartaMagica> lista = new ArrayList<>();
        if (linea == null || linea.trim().isEmpty()) return lista;
        String[] partes = linea.split("\\|");
        for (String nombre : partes) {
            CartaMagica c = crearMagiaPorNombre(nombre.trim());
            if (c != null) lista.add(c);
        }
        return lista;
    }

    private static List<CartaTrampa> leerTrampas(String linea) {
        List<CartaTrampa> lista = new ArrayList<>();
        if (linea == null || linea.trim().isEmpty()) return lista;
        String[] partes = linea.split("\\|");
        for (String nombre : partes) {
            CartaTrampa c = crearTrampaPorNombre(nombre.trim());
            if (c != null) lista.add(c);
        }
        return lista;
    }

    private static Mazo leerMazo(String linea) {
        Mazo mazo = new Mazo();
        if (linea == null || linea.trim().isEmpty()) return mazo;
        String[] partes = linea.split("\\|");
        for (String parte : partes) {
            Carta c = crearCartaDesdeCodigo(parte);
            if (c != null) mazo.agregarCarta(c);
        }
        return mazo;
    }

    // Crea una carta a partir del codigo "tipo:nombre"
    private static Carta crearCartaDesdeCodigo(String codigo) {
        if (codigo == null || !codigo.contains(":")) return null;
        String tipo  = codigo.substring(0, 1);
        String nombre = codigo.substring(2);
        if (tipo.equals("M")) {
            // Para los monstruos en la mano usamos stats por defecto
            // (los del campo se guardan con todos sus datos)
            return crearMonstruoPorNombre(nombre);
        } else if (tipo.equals("G")) {
            return crearMagiaPorNombre(nombre);
        } else {
            return crearTrampaPorNombre(nombre);
        }
    }

    // Recrea un monstruo con sus stats originales a partir del nombre
    private static Monstruo crearMonstruoPorNombre(String nombre) {
        // Buscamos en el mazo completo la carta con ese nombre
        Mazo mazoRef = InicializadorMazo.crearMazoCompleto();
        for (Carta c : mazoRef.getCartas()) {
            if (c instanceof Monstruo && c.getNombre().equals(nombre)) {
                return (Monstruo) c;
            }
        }
        return null;
    }

    private static CartaMagica crearMagiaPorNombre(String nombre) {
        if (nombre.equals("Pot of Greed"))           return new PotOfGreed();
        if (nombre.equals("Dian Keto the Cure Master")) return new DianKetotheCureMaster();
        return null;
    }

    private static CartaTrampa crearTrampaPorNombre(String nombre) {
        if (nombre.equals("Trap Hole"))    return new TrapHole();
        if (nombre.equals("Mirror Force")) return new MirrorForce();
        return null;
    }

    // VERIFICAR SI HAY PARTIDA GUARDADA

    public static boolean hayPartidaGuardada() {
        File f = new File(ARCHIVO_PARTIDA);
        return f.exists() && f.length() > 0;
    }

    // GUARDAR RESULTADO EN EL HISTORIAL

    public static void guardarResultado(String nombre1, String nombre2,
                                         String ganador, int turnos,
                                         int lpFinal1, int lpFinal2) {
        try {
            // Usamos append=true para agregar al final del archivo
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_RESULTADOS, true));
            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            // Formato: fecha | duelista1 vs duelista2 | ganador | turnos | LP finales
            pw.println(fecha + " | " + nombre1 + " vs " + nombre2
                    + " | Ganador: " + ganador
                    + " | Turnos: " + turnos
                    + " | LP: " + nombre1 + "=" + lpFinal1
                    + "  " + nombre2 + "=" + lpFinal2);
            pw.close();
        } catch (IOException e) {
            System.out.println("No se pudo guardar el resultado: " + e.getMessage());
        }
    }

    // LEER ESTADISTICAS DEL HISTORIAL

    public static String leerEstadisticas() {
        File f = new File(ARCHIVO_RESULTADOS);
        if (!f.exists()) {
            return "No hay partidas registradas todavia.";
        }

        // Contamos victorias por jugador y buscamos la partida mas larga
        java.util.Map<String, Integer> victorias = new java.util.HashMap<>();
        int turnosMax = 0;
        String partidaMasLarga = "";
        int totalPartidas = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_RESULTADOS));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                totalPartidas++;

                // Extraemos el ganador
                int idxGanador = linea.indexOf("Ganador: ");
                int idxTurnos  = linea.indexOf(" | Turnos: ");
                if (idxGanador != -1 && idxTurnos != -1) {
                    String ganador = linea.substring(idxGanador + 9, idxTurnos);
                    victorias.put(ganador, victorias.getOrDefault(ganador, 0) + 1);
                }

                // Extraemos los turnos
                int idxT = linea.indexOf("Turnos: ");
                int idxLP = linea.indexOf(" | LP: ");
                if (idxT != -1 && idxLP != -1) {
                    String turnStr = linea.substring(idxT + 8, idxLP);
                    try {
                        int t = Integer.parseInt(turnStr.trim());
                        if (t > turnosMax) {
                            turnosMax = t;
                            partidaMasLarga = linea;
                        }
                    } catch (NumberFormatException e) { /* ignorar */ }
                }
            }
            br.close();
        } catch (IOException e) {
            return "Error leyendo el historial: " + e.getMessage();
        }

        // Construimos el resumen
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADISTICAS ===\n\n");
        sb.append("Total de partidas jugadas: ").append(totalPartidas).append("\n\n");
        sb.append("Victorias por duelista:\n");
        for (java.util.Map.Entry<String, Integer> entry : victorias.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" victoria(s)\n");
        }
        if (!partidaMasLarga.isEmpty()) {
            sb.append("\nPartida mas larga (").append(turnosMax).append(" turnos):\n");
            sb.append("  ").append(partidaMasLarga);
        }
        return sb.toString();
    }
}
