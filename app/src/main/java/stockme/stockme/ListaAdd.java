package stockme.stockme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Util;

public class ListaAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener{
    private Button btn_aceptar;
    private Button btn_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Crear lista");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();
        Spinner spinner = (Spinner) findViewById(R.id.lista_add_spinner_supermercados);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, supermercados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        manejador.cerrar();

        btn_aceptar = (Button)findViewById(R.id.lista_add_btn_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = ((EditText) findViewById(R.id.lista_add_et_nombre)).getText().toString();

                if (!nombre.matches("")) {
                    String fecha = Util.diaMesAnyo.format(new Date());

                    Spinner spinner = (Spinner) findViewById(R.id.lista_add_spinner_supermercados);
                    String supermercado = spinner.getSelectedItem().toString();

                    Lista nueva = new Lista(nombre, fecha, fecha, supermercado);
                    BDHandler manejador = new BDHandler(v.getContext());

                    if (!manejador.insertarLista(nueva))
                        Toast.makeText(v.getContext(), "Ya existe la lista '" + nueva.getNombre() + "'", Toast.LENGTH_SHORT).show();
                    else {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    manejador.cerrar();
                } else {
                    Util.mostrarToast(v.getContext(), "¡Falta un nombre!");
                }

            }
        });

        btn_cancelar = (Button)findViewById(R.id.lista_add_btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return OpcionesMenus.onOptionsItemSelected(item, this);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return OpcionesMenus.onNavigationItemSelected(item, this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
