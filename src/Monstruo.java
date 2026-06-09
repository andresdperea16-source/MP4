public class Monstruo extends Carta {
    private int atk;
    private int def;
    private int nivel;
    private boolean enPosicionAtaque;
    private boolean yaAtaco;

    public Monstruo(String nombre, int atk, int def, int nivel) {
        super(nombre);
        this.atk = atk;
        this.def = def;
        this.nivel = nivel;
        this.enPosicionAtaque = true;
        this.yaAtaco = false;
    }

    public int getAtk()  { return atk; }
    public void setAtk(int atk) { this.atk = atk; }
    public int getDef()  { return def; }
    public void setDef(int def) { this.def = def; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public boolean isEnPosicionAtaque() { return enPosicionAtaque; }
    public void setEnPosicionAtaque(boolean enPosicionAtaque) { this.enPosicionAtaque = enPosicionAtaque; }
    public boolean isYaAtaco() { return yaAtaco; }
    public void setYaAtaco(boolean yaAtaco) { this.yaAtaco = yaAtaco; }

    public String getEstrellas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nivel; i++) sb.append("★");
        return sb.toString();
    }
}
