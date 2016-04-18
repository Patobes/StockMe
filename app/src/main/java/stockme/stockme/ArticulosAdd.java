package stockme.stockme;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Util;

public class ArticulosAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener {
    private EditText etNombre, etPrecio, etMarca, etCantidad;
    private Spinner spTipos, spSupermercado;
    private Button btnAceptar, btnCancelar;
    String nLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_add);
        //toolbar + navbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Crear lista");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //contenido
        etNombre = (EditText)findViewById(R.id.articulos_add_et_nombre);
        etPrecio = (EditText)findViewById(R.id.articulos_add_et_precio);
        etMarca = (EditText)findViewById(R.id.articulos_add_et_marca);
        etCantidad = (EditText)findViewById(R.id.articulos_add_et_cantidad);
        spTipos = (Spinner)findViewById(R.id.articulos_add_sp_tipos);
        spSupermercado = (Spinner)findViewById(R.id.articulos_add_sp_supermercado);
        btnAceptar = (Button)findViewById(R.id.articulos_add_btn_aceptar);
        btnCancelar = (Button)findViewById(R.id.articulos_add_btn_cancelar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_array,
                        android.R.layout.simple_spinner_item);

        spTipos.setAdapter(adapter);

        BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();

        ArrayAdapter<String> adapterSuperM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supermercados);
        //adapterSuperM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSupermercado.setAdapter(adapterSuperM);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        nLista = null;
        if(intent != null) {
            nLista = intent.getExtras().getString("NombreLista");

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Articulo articulo = new Articulo();
                    //TODO: hay que comprobar si ya está en la lista para preguntar si se suma o que
                    //habrá que crear un métod para ver si hay un artículo en la bd con ese mismo nombre, tipo y supermercado

                    //si NO está en la lista, se añade:
                    articulo.setId(null);//esto es para que lo añada como nuevo
                    if(!spTipos.getSelectedItem().toString().equals("Cualquiera"))
                        articulo.setTipo(spTipos.getSelectedItem().toString());
                    if(!spSupermercado.getSelectedItem().toString().equals("Cualquiera"))
                        articulo.setSupermercado(spSupermercado.getSelectedItem().toString());
                    articulo.setNombre(etNombre.getText().toString());
                    if(!etPrecio.getText().toString().isEmpty())
                        articulo.setPrecio(Float.parseFloat(etPrecio.getText().toString()));
                    else
                        articulo.setPrecio(0.0f);
                    articulo.setMarca(etMarca.getText().toString());
                    BDHandler manejador = new BDHandler(v.getContext());
                    if(manejador.insertarArticulo(articulo) != null)
                        Util.mostrarToast(v.getContext(), "Añadido artículo");
                    else
                        Util.mostrarToast(v.getContext(), "No se ha podido insertar el artículo");

                    //lo añado a la lista
                    int nArtic = 0;
                    if(!etCantidad.getText().toString().isEmpty())
                        nArtic = Integer.parseInt(etCantidad.getText().toString());
                    manejador.insertarArticuloEnLista(new ListaArticulo(articulo.getId(), nLista, nArtic));

                    manejador.cerrar();
                    finish();
                }
            });
        }else{
            Util.mostrarToast(this, "No se ha podido cargar la lista");
            manejador.cerrar();
            finish();
        }
        manejador.cerrar();
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
