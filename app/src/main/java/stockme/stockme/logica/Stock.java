package stockme.stockme.logica;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Stock {
    private int producto;
    private int cantidad;

    public Stock(){
        this.producto = 0;
        this.cantidad = 0;
    }

    public Stock(int producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
