package stockme.stockme.logica;


public class Stock {
    private int articulo;
    private int cantidad;
    private int minimo;

    public static String ARTICULO = "Articulo";
    public static String CANTIDAD = "Cantidad";
    public static String MINIMO = "Minimo";

    public Stock(){
        articulo = 0;
        cantidad = 0;
        minimo = 0;
    }

    public Stock(int articulo, int cantidad, int minimo) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.minimo = minimo;
    }

    public int getArticulo() {
        return articulo;
    }

    public void setArticulo(int articulo) {
        this.articulo = articulo;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
