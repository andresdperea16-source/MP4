public class CartaFactory {

    public static Carta crearCarta(String codigo) {

        if(codigo == null || !codigo.contains(":")) {
            return null;
        }

        String tipo = codigo.substring(0,1);
        String nombre = codigo.substring(2);

        switch(tipo) {

            case "M":
                return crearMonstruo(nombre);
                
            case "G":
                
                if(nombre.equals("Pot of Greed"))
                    return new PotOfGreed();
            
                if(nombre.equals("Dian Keto the Cure Master"))
                    return new DianKetotheCureMaster();
            
                break;
        
            case "T":
        
                if(nombre.equals("Trap Hole"))
                    return new TrapHole();
            
                if(nombre.equals("Mirror Force"))
                    return new MirrorForce();
            
                break;
        }
        return null;
    }

    private static Monstruo crearMonstruo(String nombre) {

        Mazo mazo = InicializadorMazo.crearMazoCompleto();

        for(Carta c : mazo.getCartas()) {

            if(c instanceof Monstruo &&
               c.getNombre().equals(nombre)) {

                return (Monstruo)c;
            }
        }

        return null;
    }
}