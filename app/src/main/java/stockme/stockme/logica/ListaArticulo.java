package stockme.stockme.logica;

/**
 * Created by Juanmi on 10/03/2016.
 */
public class ListaArticulo {
    private int articulo;
    private String nombre;
    private int cantidad;

    public static int ARTICULO = 0;
    public static String NOMBRE = "Nombre";
    public static int CANTIDAD = 0;

    public ListaArticulo(){
        this.articulo = 0;
        this.nombre = "";
        this.cantidad = 0;
    }

    public ListaArticulo(int articulo, String nombre, int cantidad) {
        this.articulo = articulo;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getArticulo() {
        return articulo;
    }

    public void setArticulo(int articulo) {
        this.articulo = articulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
