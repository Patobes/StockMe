package stockme.stockme.util;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import stockme.stockme.ArticulosAdd;
import stockme.stockme.Principal;
import stockme.stockme.R;

public class OpcionesMenus {
    public OpcionesMenus(){}

    public static boolean onNavigationItemSelected(MenuItem item, Activity activity){
        int id = item.getItemId();

        Intent i = new Intent(activity, Principal.class);

        if (id == R.id.nav_listas) {
            i.putExtra("Opcion", "Listas");
        } else if (id == R.id.nav_stock) {
            i.putExtra("Opcion", "Stock");
        } else if (id == R.id.nav_articulos) {
            i.putExtra("Opcion", "Articulos");
        } else if (id == R.id.nav_supermercados) {
            i.putExtra("Opcion", "Supermercados");
        } else if (id == R.id.nav_ajustes) {
            i.putExtra("Opcion", "Ajustes");
        }
//
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        activity.startActivity(i);

        //TODO: Finalizar la activity cuando se vaya a una opción? Esto impide que se vuelva con el botón de "atrás"
        //finish();

        return true;
    }

    public static boolean onOptionsItemSelected(MenuItem item, Activity activity) {
        switch (item.getItemId()){
            case R.id.accion_opciones:
                return true;
            default:
                return true;
                //return super.onOptionsItemSelected(item);
        }
    }
}
