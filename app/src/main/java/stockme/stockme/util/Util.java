package stockme.stockme.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import stockme.stockme.R;

public class Util {

    public static SimpleDateFormat diaMesAnyo = new SimpleDateFormat("dd/MM/yyyy");
    public static String moneda = "€";
    public static String vieneDe = "Principal";
    //hay que INCREMENTAR en 1 cada vez que se hace una modificación en la estructura o en contenido inicial
    private static int BD_VERSION = 31;

    private Util() {
    }

    public static int getBD_VERSION() {
        return BD_VERSION;
    }

    //método para crear un mensaje de Diálogo. Los listener pasados por parámetro se crean de la siguiente forma:
    /*
    * DialogInterface.OnClickListener listenerBorrar = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                //acción al pulsar el botón de borrar
			}
		};
    * */
    public static void crearMensajeAlerta(String mensaje, String titulo, String msgConfirm, String msgCancel,
            DialogInterface.OnClickListener listenerConfirm, DialogInterface.OnClickListener listenerCancel, Context ctx){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ctx);
        dlgAlert.setMessage(mensaje);
        dlgAlert.setTitle(titulo);
        dlgAlert.setPositiveButton(msgConfirm, listenerConfirm);
        dlgAlert.setNegativeButton(msgCancel, listenerCancel);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    //esta sobrecarga del método es para usarlo de la forma más habitual, sin un evento para el botón de cancelar y
    //con valores por defecto titulo=Confirmación, acp=Sí, cancl=No
    public static void crearMensajeAlerta(String mensaje, DialogInterface.OnClickListener listenerConfirm, Context ctx){
        crearMensajeAlerta(mensaje, ctx.getResources().getString(R.string.Confirmacion), ctx.getResources().getString(R.string.Si), ctx.getResources().getString(R.string.No), listenerConfirm, null, ctx);
    }
    //esta sobrecarga permite además establecer el título del diálogo
    public static void crearMensajeAlerta(String mensaje, String titulo, DialogInterface.OnClickListener listenerConfirm, Context ctx){
        crearMensajeAlerta(mensaje, titulo, ctx.getResources().getString(R.string.Si), ctx.getResources().getString(R.string.No), listenerConfirm, null, ctx);
    }

    public static void mostrarToast(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
