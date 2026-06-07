import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InicializadorMazo {

    public static Mazo crearMazoCompleto() {
        List<Carta> todasLasCartas = new ArrayList<>();

        // Los 30 bichos
        todasLasCartas.add(new Monstruo("Blue-Eyes White Dragon",3000, 2500, 8));
        todasLasCartas.add(new Monstruo("Tri-Horned Dragon",2850, 2350, 8));
        todasLasCartas.add(new Monstruo("Dark Magician",2500, 2100, 7));
        todasLasCartas.add(new Monstruo("Gaia the Fierce Knight",2300, 2100, 7));
        todasLasCartas.add(new Monstruo("Summoned Skull",2500, 1200, 6));
        todasLasCartas.add(new Monstruo("Aqua Madoor",1200, 2000, 4));
        todasLasCartas.add(new Monstruo("Celtic Guardian",1400, 1200, 4));
        todasLasCartas.add(new Monstruo("Hitotsu-Me Giant",1200, 1000, 4));
        todasLasCartas.add(new Monstruo("Trial of Nightmare",1300,  900, 4));
        todasLasCartas.add(new Monstruo("Fiend Reflection #2",1100, 1400, 4));
        todasLasCartas.add(new Monstruo("Turtle Tiger",1000, 1500, 4));
        todasLasCartas.add(new Monstruo("Masaki the Legendary Swordsman",1100, 1100, 4));
        todasLasCartas.add(new Monstruo("Flame Manipulator",900, 1000, 3));
        todasLasCartas.add(new Monstruo("The 13th Grave",1200,  900, 3));
        todasLasCartas.add(new Monstruo("Mammoth Graveyard",1200,  800, 3));
        todasLasCartas.add(new Monstruo("Silver Fang",1200,  800, 3));
        todasLasCartas.add(new Monstruo("Dark King of the Abyss",1200,  800, 3));
        todasLasCartas.add(new Monstruo("Green Phantom King",500, 1600, 3));
        todasLasCartas.add(new Monstruo("Two-Mouth Darkruler",900,  700, 3));
        todasLasCartas.add(new Monstruo("Dissolverock",900, 1000, 3));
        todasLasCartas.add(new Monstruo("Root Water",900,  800, 3));
        todasLasCartas.add(new Monstruo("The Furious Sea King",800,  700, 3));
        todasLasCartas.add(new Monstruo("Ray & Temperature",1000, 1000, 3));
        todasLasCartas.add(new Monstruo("King Fog",1000,  900, 3));
        todasLasCartas.add(new Monstruo("Mystical Sheep #2",800, 1000, 3));
        todasLasCartas.add(new Monstruo("Nemuriko",800,  700, 3));
        todasLasCartas.add(new Monstruo("Dark Gray",800,  900, 3));
        todasLasCartas.add(new Monstruo("Kurama",800,  800, 3));
        todasLasCartas.add(new Monstruo("Skull Servant",300,  200, 1));
        todasLasCartas.add(new Monstruo("Basic Insect",500,  700, 2));

        // Las cartas de magia
        todasLasCartas.add(new PotOfGreed());
        todasLasCartas.add(new PotOfGreed());
        todasLasCartas.add(new PotOfGreed());
        todasLasCartas.add(new PotOfGreed());
        todasLasCartas.add(new PotOfGreed());
        todasLasCartas.add(new DianKetotheCureMaster());
        todasLasCartas.add(new DianKetotheCureMaster());
        todasLasCartas.add(new DianKetotheCureMaster());
        todasLasCartas.add(new DianKetotheCureMaster());
        todasLasCartas.add(new DianKetotheCureMaster());

        // Cartas Trampa
        todasLasCartas.add(new TrapHole());
        todasLasCartas.add(new TrapHole());
        todasLasCartas.add(new TrapHole());
        todasLasCartas.add(new TrapHole());
        todasLasCartas.add(new TrapHole());
        todasLasCartas.add(new MirrorForce());
        todasLasCartas.add(new MirrorForce());
        todasLasCartas.add(new MirrorForce());
        todasLasCartas.add(new MirrorForce());
        todasLasCartas.add(new MirrorForce());


        Collections.shuffle(todasLasCartas);

        Mazo mazo = new Mazo();
        for (Carta carta : todasLasCartas) {
            mazo.agregarCarta(carta);
        }

        return mazo;
    }

    // Reparte 25 cartas a los jugadores
    public static void repartirCartas(Jugador jugador1, Jugador jugador2) {
        Mazo mazoCompleto = crearMazoCompleto();
        List<Carta> todas = mazoCompleto.getCartas();

        List<Carta> cartasJ1 = new ArrayList<>(todas.subList(0, 25));
        List<Carta> cartasJ2 = new ArrayList<>(todas.subList(25, 50));

        Mazo mazoJ1 = new Mazo();
        Mazo mazoJ2 = new Mazo();

        for (Carta c : cartasJ1) mazoJ1.agregarCarta(c);
        for (Carta c : cartasJ2) mazoJ2.agregarCarta(c);

        jugador1.setMazo(mazoJ1);
        jugador2.setMazo(mazoJ2);
    }
}
