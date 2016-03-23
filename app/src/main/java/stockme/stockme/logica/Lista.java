package stockme.stockme.logica;

public class Lista {

    private String nombre;
    private String fechaCreacion;
    private String fechaModificacion;
    private String supermercado;

    public static final String NOMBRE = "Nombre";
    public static final String FECHA_CREACION = "FechaCreacion";
    public static final String FECHA_MODIFICACION = "FechaModificacion";
    public static final String SUPERMERCADO = "Supermercado";

    public Lista(){
        this.nombre = "";
        this.fechaCreacion = "";
        this.fechaModificacion = "";
        this.supermercado = "";
    }

    public Lista(String nombre, String fechaCreacion, String fechaModificacion, String supermercado) {

        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.supermercado = supermercado;
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

    public void setFechaModificacion(String fechaModificacion) { this.fechaModificacion = fechaModificacion;}

    public String getSupermercado() { return supermercado;}

    public void setSupermercado(String supermercado) { this.supermercado = supermercado; }
}
