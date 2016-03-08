package stockme.stockme.logica;

/**
 * Created by JuanMiguel on 08/03/2016.
 */
public class Lista {
    private int id;
    private String nombre;
    private String supermercado;
    private int numProductos;
    private String fecha;

    public Lista(){
        this.id = 0;
        this.nombre = "";
        this.supermercado ="";
        this.numProductos = 0;
        this.fecha = "";
    }

    public Lista(int id, String nombre, String supermercado, int numProductos, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.supermercado = supermercado;
        this.numProductos = numProductos;
        this.fecha = fecha;
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

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public int getNumProductos() {
        return numProductos;
    }

    public void setNumProductos(int numProductos) {
        this.numProductos = numProductos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
