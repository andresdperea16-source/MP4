import java.util.ArrayList;
import java.util.List;

public class Campo {
    private List<Monstruo> monstruos;
    private List<CartaMagica> cartasMagicas;
    private List<CartaTrampa> cartasTrampa;

    public Campo() {
        this.monstruos = new ArrayList<>();
        this.cartasMagicas = new ArrayList<>();
        this.cartasTrampa = new ArrayList<>();
    }

    public void colocarMonstruo(Monstruo monstruo){
        monstruos.add(monstruo);
    }
    public void colocarCartaMagica(CartaMagica cartaMagica){
        cartasMagicas.add(cartaMagica);
    }
    public void colocarCartaTrampa(CartaTrampa cartaTrampa){
        cartasTrampa.add(cartaTrampa);
    }
    public void quitarMonstruo(Monstruo monstruo){
        monstruos.remove(monstruo)
        ;}
    public int getCantidadMonstruos(){
        return monstruos.size();
    }

    public List<Monstruo>    getMonstruos(){
        return monstruos;
    }
    public void setMonstruos(List<Monstruo> m){
        this.monstruos = m;
    }
    public List<CartaMagica> getCartasMagicas(){
        return cartasMagicas;
    }
    public void setCartasMagicas(List<CartaMagica> c) {
        this.cartasMagicas = c;
    }
    public List<CartaTrampa> getCartasTrampa()  {
        return cartasTrampa;
    }
    public void setCartasTrampa(List<CartaTrampa> c){
        this.cartasTrampa = c;
    }
}
