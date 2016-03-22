package stockme.stockme.util;


public class GlobalVars {
    private static GlobalVars instancia = null;
    private String moneda;

    public static GlobalVars getInstancia(){
        if(instancia == null)
            instancia = new GlobalVars();
        return instancia;
    }

    private GlobalVars(){
        this.moneda = "€";
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
