package stockme.stockme.util;

import java.text.SimpleDateFormat;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Util {
    private static final int BD_VERSION = 5;

    public static int getBD_VERSION(){
        return BD_VERSION;
    }
    public static SimpleDateFormat diaMesAnyo = new SimpleDateFormat("dd/MM/yyyy");
}
