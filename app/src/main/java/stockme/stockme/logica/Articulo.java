package stockme.stockme.logica;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Articulo {
    private Integer id;
    private String nombre;
    private String marca;
    private String tipo;

    public static String ID = "Id";
    public static String NOMBRE = "Nombre";
    public static String MARCA = "Marca";
    public static String TIPO = "Tipo";

    public Articulo(){
        this.nombre = "";
        this.marca = "";
    }

    public Articulo(Integer id, String nombre, String marca, String supermercado, float precio) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
