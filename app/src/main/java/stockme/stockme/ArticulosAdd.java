package stockme.stockme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.InputFilterMinMax;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Util;

public class ArticulosAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener {
    private EditText etNombre, etPrecio, etCantidad;
    private AutoCompleteTextView atv_marcas;
    private Spinner spTipos, spSupermercado;
    private Button btnAceptar, btnCancelar;
    String nLista;
    Articulo articulo;
    int cantidad;
    float precio;

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
        etCantidad = (EditText)findViewById(R.id.articulos_add_et_cantidad);
        etCantidad.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});
        spTipos = (Spinner)findViewById(R.id.articulos_add_sp_tipos);
        spSupermercado = (Spinner)findViewById(R.id.articulos_add_sp_supermercado);
        btnAceptar = (Button)findViewById(R.id.articulos_add_btn_aceptar);
        btnCancelar = (Button)findViewById(R.id.articulos_add_btn_cancelar);
        atv_marcas = (AutoCompleteTextView)findViewById(R.id.articulos_add_atv_marcas);
        atv_marcas.setThreshold(1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_array,
                        android.R.layout.simple_spinner_item);
        spTipos.setAdapter(adapter);

        final BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();
        List<String> marcas = manejador.obtenerMarcas();

        ArrayAdapter<String> adptMarcas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, marcas);
        atv_marcas.setAdapter(adptMarcas);

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
                    String nombre, marca, tipo, supermercado;
                    articulo = new Articulo();
                    nombre = etNombre.getText().toString();
                    marca = atv_marcas.getText().toString();
                    tipo = spTipos.getSelectedItem().toString();
                    supermercado = spSupermercado.getSelectedItem().toString();
                    if(!etPrecio.getText().toString().isEmpty())
                        precio = Float.parseFloat(etPrecio.getText().toString());

                    final BDHandler manejador2 = new BDHandler(v.getContext());
                    //intento obtener el artículo correspondiente a estos valores
                    //Esto es articulo complejo (con super y precio)
                    Articulo artAux = manejador2.obtenerArticulo(nombre, marca, tipo, supermercado);
                    if(artAux != null){
                        articulo = artAux;
                    }else{
                        articulo.setTipo(tipo);
                        articulo.setSupermercado(supermercado);
                        articulo.setNombre(nombre);
                        articulo.setPrecio(precio);
                        articulo.setMarca(marca);
                        if(manejador2.insertarArticulo(articulo) != null)
                            Util.mostrarToast(v.getContext(), "Se ha creado un nuevo artículo");
                        else
                            Util.mostrarToast(v.getContext(), "No se ha podido crear el artículo");
                        //TODO: podríamos meter función de autocompletar para los tipos y marcas que ya estén en la bd
                    }

                    cantidad = 1;
                    if(!etCantidad.getText().toString().isEmpty())
                        cantidad = Integer.parseInt(etCantidad.getText().toString());


                    //una vez tengo un artículo, compruebo si está en la lista
                    if(manejador2.estaArticuloEnLista(articulo.getId(), nLista)){
                        DialogInterface.OnClickListener aceptar = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(articulo.getPrecio() != precio){
                                    articulo.setPrecio(precio);
                                    manejador2.modificarArticulo(articulo);
                                    Util.mostrarToast(ArticulosAdd.this, "Precio actualizado");
                                }
                                ListaArticulo la = manejador2.obtenerListaArticulo(articulo.getId(), nLista);
                                manejador2.modificarArticuloEnLista(new ListaArticulo(articulo.getId(), nLista, la.getCantidad() + cantidad));
                                manejador2.cerrar();
                                finish();
                            }
                        };
                        Util.crearMensajeAlerta("Ya está el artículo en la lista. ¿Quieres sumar la cantidad?", "Artículo existente",
                                aceptar, ArticulosAdd.this);
                    }else{
                        manejador2.insertarArticuloEnLista(new ListaArticulo(articulo.getId(), nLista, cantidad));
                    }
                    manejador.cerrar();
                    manejador2.cerrar();
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
