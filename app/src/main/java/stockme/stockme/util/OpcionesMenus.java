package stockme.stockme.util;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import stockme.stockme.R;

public class OpcionesMenus {
    public OpcionesMenus(){}

//    public static boolean onNavigationItemSelected(MenuItem item, Activity activity){
//        int id = item.getItemId();
//
//        Intent i = new Intent(activity, Principal.class);
//
//        if (id == R.id.nav_listas) {
//            i.putExtra("Opcion", "Listas");
//        } else if (id == R.id.nav_stock) {
//            i.putExtra("Opcion", "Stock");
//        } else if (id == R.id.nav_articulos) {
//            i.putExtra("Opcion", "Articulos");
//        } else if (id == R.id.nav_ajustes) {
//            i.putExtra("Opcion", "Ajustes");
//        }
////
//        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
//        activity.startActivity(i);
//        activity.overridePendingTransition(0, 0);

//        activity.finish();
//
//        return true;
//    }

//    public static boolean onOptionsItemSelected(MenuItem item, Activity activity) {
//        switch (item.getItemId()){
//            case R.id.accion_opciones:
//                return true;
//            default:
//                return true;
//                //return super.onOptionsItemSelected(item);
//        }
//    }

    public static void onBackPressed(final Activity activity){
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Util.crearMensajeAlerta(activity.getResources().getString(R.string.Pregunta_salida), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            }, activity);
        }
    }
}
