package stockme.stockme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemArticulosListaCompra;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OpcionesMenus;

public class ListaCompra extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener{
    private DynamicListView articulos;
    private Button lista_articulos_btn_mas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Artículos");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //contenido
        articulos = (DynamicListView)findViewById(R.id.lista_articulos_lista);

        final Lista lista = new Lista(getIntent().getStringExtra("NombreLista"),"","","");
        this.setTitle(lista.getNombre());

        final BDHandler manejador = new BDHandler(this);
        List<ArticuloSupermercado> listaArticulos = manejador.obtenerArticulosEnLista(lista);
        final AdaptadorListItemArticulosListaCompra adaptador = new AdaptadorListItemArticulosListaCompra(this, listaArticulos, lista);
        articulos.setAdapter(adaptador);

        manejador.close();

        lista_articulos_btn_mas = (Button)findViewById(R.id.lista_articulos_btn_mas);
        lista_articulos_btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaCompra.this, ArticulosAdd.class);
                i.putExtra("NombreLista", lista.getNombre());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return OpcionesMenus.onOptionsItemSelected(item, this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return OpcionesMenus.onNavigationItemSelected(item, this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
