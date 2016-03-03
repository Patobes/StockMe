package logica;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Supermercado {
    private int id;
    private String nombre;
    private String direccion;

    public Supermercado(){
        this.id = 0;
        this.nombre = "";
        this.direccion = "";
    }

    public Supermercado(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
