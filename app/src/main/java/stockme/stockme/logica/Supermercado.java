package stockme.stockme.logica;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Supermercado {
    private String nombre;

    public static String NOMBRE = "Nombre";

    public Supermercado(){
        this.nombre = "";
    }

    public Supermercado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
