package stockme.stockme.logica;

/**
 * Created by JuanMiguel on 08/03/2016.
 */
public class Lista {

    private String nombre;
    private String fechaCreacion;
    private String fechaModificacion;

    public static final String NOMBRE = "Nombre";
    public static final String FECHA_CREACION = "FechaCreacion";
    public static final String FECHA_MODIFICACION = "FechaModificacion";

    public Lista(){
        this.nombre = "";
        this.fechaCreacion = "";
        this.fechaModificacion = "";
    }

    public Lista(String nombre, String fechaCreacion, String fechaModificacion) {

        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
