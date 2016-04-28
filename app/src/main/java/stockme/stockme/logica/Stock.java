package stockme.stockme.logica;


public class Stock {
    private int articulo;
    private int minimo;
    private int canitdad;

    public static String ARTICULO = "Articulo";
    public static String MINIMO = "Minimo";
    public static String CANTIDAD = "Cantidad";

    public Stock(){
        articulo = 0;
        minimo = 0;
        canitdad = 0;
    }

    public Stock(int articulo, int minimo, int canitdad) {
        this.articulo = articulo;
        this.minimo = minimo;
        this.canitdad = canitdad;
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

    public int getCanitdad() {
        return canitdad;
    }

    public void setCantidad(int canitdad) {
        this.canitdad = canitdad;
    }
}
