package stockme.stockme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Configuracion;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Util;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_listas.OnFragmentInteractionListener, Fragment_stock.OnFragmentInteractionListener {

    private static NavigationView nav_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inicialziar preferencias
        Configuracion.inicializarPreferencias(this);
        //con esto ya podremos usar los métodos estáticos de Configuracion
        crearPreferenciasPorDefecto();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav_menu = (NavigationView) findViewById(R.id.nav_view);
        nav_menu.setNavigationItemSelectedListener(this);


        BDHandler handler = new BDHandler(this);
        handler.abrir();
        handler.cerrar();

    }

    private void crearPreferenciasPorDefecto() {
        Configuracion.setPreferencia("anterior", "Listas");
//        if (!Configuracion.isPreferencia("orden_listas"))
//            Configuracion.setPreferencia("orden_listas", "ASC");
        if(!Configuracion.isPreferencia("idioma"))
            Configuracion.setPreferencia("idioma", "es");
        if (!Configuracion.isPreferencia("moneda")) {
            Configuracion.setPreferencia("moneda", Util.moneda);
        }
        if(!Configuracion.isPreferencia("mostrar_precio")){
            Configuracion.setPreferencia("mostrar_precio", true);
        }
        if(!Configuracion.isPreferencia("mostrar_marca_stock"))
            Configuracion.setPreferencia("mostrar_marca_stock", false);
    }

    @Override
    public void onBackPressed() {
        OpcionesMenus.onBackPressed(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean fragTransact = false;
        Fragment fragmento = null;

        if (id == R.id.nav_listas) {
            fragmento = new Fragment_listas();
            fragTransact = true;
        } else if (id == R.id.nav_stock) {
            fragmento = new Fragment_stock();
            fragTransact = true;
        } else if (id == R.id.nav_articulos) {
            startActivity(new Intent(this, CatalogoArticulos.class));
            overridePendingTransition(0, 0);
            finish();
        } else if (id == R.id.nav_ajustes) {
            startActivity(new Intent(this, Preferencias.class));
            overridePendingTransition(0, 0);
            finish();
        }

        if(fragTransact){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido_principal, fragmento).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String anterior = Configuracion.getPreferenciaString("anterior");
        if(anterior != null){
            switch (anterior) {
                case "Listas":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_listas));
                    break;
                case "Stock":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_stock));
                    break;
                case "Articulos":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_articulos));
                    break;
                case "Ajustes":
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_ajustes));
                    break;
                default:
                    onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_listas));
                    break;
            }
        }else
            onNavigationItemSelected(nav_menu.getMenu().findItem(R.id.nav_listas));
    }
}
