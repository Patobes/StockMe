package stockme.stockme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemListas;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;

public class Listas extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Button btn_articulos;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        btn_articulos = (Button) findViewById(R.id.lista_btn_articulos);
        btn_articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (v.getContext(),ListaArticulos.class);
                startActivity(i);
            }
        });

        lista = (ListView)findViewById(R.id.listas_lv_listas);
        //recojo las listas existenes
        BDHandler manejador = new BDHandler(this);
        List<Lista> listaListas = manejador.obtenerListas();
        //las añado al adaptador
        AdaptadorListItemListas adaptador = new AdaptadorListItemListas(this, listaListas);
        //asigno el adaptador a la list view
        lista.setAdapter(adaptador);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}