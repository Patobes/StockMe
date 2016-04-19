package stockme.stockme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;

public class StockAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener{
    private EditText et_nombre;
    private EditText et_marca;
    private Spinner sp_supermercado;
    private EditText et_cantidad;
    private Spinner sp_cantidad;
    private Button bt_añadir;

    //Al volver vuelve a la parte de listas y no a la de stock (da igual si es boton hacia atras o despues de añadir al stock)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Añadir stock");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        et_nombre = (EditText)findViewById(R.id.stock_add_et_nombre);
        et_marca = (EditText)findViewById(R.id.stock_add_et_marca);
        sp_supermercado = (Spinner) findViewById(R.id.stock_add_sp_supermercado);
        et_cantidad = (EditText)findViewById(R.id.stock_add_et_cantidad);
        sp_cantidad = (Spinner)findViewById(R.id.stock_add_sp_cantidad);
        bt_añadir = (Button)findViewById(R.id.stock_add_bt_añadir);


        BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, supermercados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_supermercado.setAdapter(adapter);

        final String []valores = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25"};
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores);
        sp_cantidad.setAdapter(adaptador);

        manejador.cerrar();

        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nombre = et_nombre.getText().toString();
                final String marca = et_marca.getText().toString();
                final String supermercado = (String)sp_supermercado.getSelectedItem();
                final int cantidad = Integer.valueOf(et_cantidad.getText().toString());

                if (nombre.isEmpty() || nombre.matches("") || nombre.matches(" ")) {
                    Util.mostrarToast(getApplicationContext(), "Introduce un nombre de articulo!");
                } else if (cantidad == 0) {
                    Util.mostrarToast(getApplicationContext(), "Introduce una cantidad mayor que 0!");
                } else {
                    BDHandler manejador = new BDHandler(v.getContext());
                    if(manejador.estaStock(nombre, marca, supermercado)) {
                        DialogInterface.OnClickListener sumarCantidad = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                BDHandler manejador = new BDHandler(getApplicationContext());
                                Articulo articulo = manejador.obtenerArticulo(nombre, marca, supermercado);
                                Stock stock = manejador.obtenerStocks(articulo);
                                stock.setCanitdad(stock.getCanitdad() + cantidad);
                                manejador.modificarStock(stock);
                                Util.mostrarToast(getApplicationContext(), cantidad + " unidades añadidas!");
                                manejador.cerrar();
                                finish();
                            }
                        };
                        String mensaje = "El articulo que has introducido ya está en Stock\r\n¿Deseas añadir " + cantidad + " unidades al Stock?";
                        Util.crearMensajeAlerta(mensaje, sumarCantidad, v.getContext());

                    } else if(manejador.estaArticulo(nombre, marca, supermercado)) {
                        //Esta rama funciona bien
                        Articulo articulo = manejador.obtenerArticulo(nombre, marca, supermercado);
                        Stock stock = new Stock();
                        stock.setArticulo(articulo.getId());
                        stock.setCanitdad(cantidad);
                        if(manejador.insertarStock(stock)){
                            Util.mostrarToast(getApplicationContext(), "Articulo añadido!");
                        } else {
                            Util.mostrarToast(getApplicationContext(), "No se ha podido añadir el articulo al Stock");
                        }
                        manejador.cerrar();
                        finish();
                    } else {
                        Articulo articulo = new Articulo();
                        articulo.setNombre(nombre);
                        articulo.setMarca(marca);
                        articulo.setSupermercado(supermercado);
                        //funciona la primera vez que se ejecuta -- las demas falla en este paso
                        if(manejador.insertarArticulo(articulo) != null) {
                            Articulo articulo1 = manejador.obtenerArticulo(nombre, marca, supermercado);
                            Stock stock = new Stock();
                            stock.setArticulo(articulo1.getId());
                            stock.setCanitdad(cantidad);
                            if (manejador.insertarStock(stock)) {
                                Util.mostrarToast(getApplicationContext(), "Articulo añadido!");
                            } else {
                                Util.mostrarToast(getApplicationContext(), "No se ha podido añadir el articulo al Stock");
                            }
                        } else {
                            Util.mostrarToast(getApplicationContext(), "No se ha podido añadir el articulo a la BBDD");
                        }
                        manejador.cerrar();
                        finish();
                    }
                }
            }
        });

        sp_cantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_cantidad.setText(parent.getItemAtPosition(position).toString());
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_cantidad.setText("1");
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

