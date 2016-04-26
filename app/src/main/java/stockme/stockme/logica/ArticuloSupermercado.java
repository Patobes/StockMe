package stockme.stockme.logica;

/**
 * Created by paris on 26/04/2016.
 */
public class ArticuloSupermercado {
    private Integer id;
    private Integer articulo;
    private String supermercado;
    private float precio;

    public static String ID = "Id";
    public static String ARTICULO = "Articulo";
    public static String SUPERMERCADO = "Supermercado";
    public static String PRECIO = "Precio";

    public ArticuloSupermercado(){
        this.articulo = 0;
        this.supermercado = "";
        this.precio = 0.0f;
    }

    public ArticuloSupermercado(Integer id, Integer articulo, String supermercado, float precio) {
        this.id = id;
        this.articulo = articulo;
        this.supermercado = supermercado;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticulo() {
        return articulo;
    }

    public void setArticulo(Integer articulo) {
        this.articulo = articulo;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
