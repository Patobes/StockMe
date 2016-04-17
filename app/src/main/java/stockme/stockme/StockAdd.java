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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;

public class StockAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener {
    EditText et_nombre;
    EditText et_marca;
    EditText et_cantidad;
    Spinner sp_cantidad;
    Button bt_añadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_add);
//toolbar + navbar
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

        //contenido

        et_nombre = (EditText)findViewById(R.id.stock_add_et_nombre);
        et_marca = (EditText)findViewById(R.id.stock_add_et_marca);
        et_cantidad = (EditText)findViewById(R.id.stock_add_et_cantidad);
        sp_cantidad = (Spinner)findViewById(R.id.stock_add_sp_cantidad);
        bt_añadir = (Button)findViewById(R.id.stock_add_bt_añadir);

        final String []valores = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25"};
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores);
        sp_cantidad.setAdapter(adaptador);

        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = et_nombre.getText().toString();
                int cantidad = Integer.valueOf(et_cantidad.getText().toString());
                String marca = et_marca.getText().toString();

                if (nombre.isEmpty() || nombre.matches("") || nombre.matches(" ")) {
                    Util.mostrarToast(getApplicationContext(), "Introduce un nombre de articulo!");
                } else if (cantidad == 0) {
                    Util.mostrarToast(getApplicationContext(), "Introduce una cantidad mayor que 0!");
                } else {
                    BDHandler manejador = new BDHandler(v.getContext());
                    if(manejador.estaStock(nombre, marca)) {
                        Util.mostrarToast(getApplicationContext(), "El articulo ya esta en stock");
                        //modificar el stock para sumar cantidades
                        //mensaje de alerta con: Ese producto ya esta en stock. ¿Desea añadir la cantidad introducida?
                    } else if(manejador.estaArticulo(nombre, marca)) {
                        Util.mostrarToast(getApplicationContext(), "El articulo no esta en stock pero si en la BBDD");
                        //crear stock a partir del articulo ya existente en la bbdd
                    } else {
                        Util.mostrarToast(getApplicationContext(), "El articulo no esta ni en stock ni en la BBDD");
                        //añadir el articulo a la bbdd y luego al stock
                    /*if(manejador.estaStock(nombre, marca)){
                        DialogInterface.OnClickListener sumarCantidad = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Articulo articulo = manejador.obtenerArticulo(nombre, marca);
                                Stock stock = manejador.obtenerStocks(articulo);
                                stock.setCanitdad(stock.getCanitdad() + cantidad);
                                manejador.modificarStock(stock);
                                Util.mostrarToast(getApplicationContext(), cantidad + "unidades añadidas!");
                                /*
                                List<Articulo> articulos = manejador.obtenerArticulosEnLista(datos.get(position));
                                for(Articulo articulo : articulos){
                                    manejador.eliminarArticuloEnLista(new ListaArticulo(articulo.getId(),datos.get(position).getNombre(),0));
                                }

                                if(!manejador.eliminarLista(datos.get(position)))
                                    Util.mostrarToast(getContext(), "No se ha podido eliminar la lista");

                                Util.mostrarToast(getContext(), "Lista eliminada");

                                manejador.cerrar();
                                remove(datos.get(position));
                            }
                        };
                        String mensaje = "El articulo que has introducido ya está en Stock\r\n¿Deseas añadir " + cantidad + " unidades al Stock?";
                        Util.crearMensajeAlerta(mensaje, sumarCantidad, v.getContext());
                    }*/
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

