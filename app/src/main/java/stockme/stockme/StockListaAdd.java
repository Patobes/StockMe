package stockme.stockme;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.InputFilterMinMax;
import stockme.stockme.util.Util;

public class StockListaAdd extends AppCompatActivity implements Fragment_listas.OnFragmentInteractionListener{
    private EditText et_lista;
    private Spinner sp_lista;
    private EditText et_super;
    private Spinner sp_super;
    private EditText et_cantidad;
    private Spinner sp_cantidad;
    private EditText et_precio;
    private ImageButton bt_aceptar;
    int cantidad;
    float precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_lista_add);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getResources().getString(R.string.Añadir_stock_a_lista));

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Contenido
        et_lista = (EditText)findViewById(R.id.stock_lista_add_et_lista);
        sp_lista = (Spinner)findViewById(R.id.stock_lista_add_sp_lista);
        et_super = (EditText)findViewById(R.id.stock_lista_add_et_super);
        sp_super = (Spinner)findViewById(R.id.stock_lista_add_sp_super);
        et_cantidad = (EditText)findViewById(R.id.stock_lista_add_et_cantidad);
        sp_cantidad = (Spinner)findViewById(R.id.stock_lista_add_sp_cantidad);
        et_precio = (EditText)findViewById(R.id.stock_lista_add_et_precio);
        bt_aceptar = (ImageButton)findViewById(R.id.stock_lista_add_bt_aceptar);

        //Propiedades del contenido
        final BDHandler manejador = new BDHandler(this);
        List<Lista> listas = manejador.obtenerListas();
        List<String> supermercados = manejador.obtenerSupermercados();
        String []valores_cantidad = new String[]{"1","2","3","4","5","6","7","8","9","10"};

        List<String> nombre_listas = new ArrayList<String>();
        for (Lista l :
                listas) {
            nombre_listas.add(l.getNombre());
        }
        ArrayAdapter<String> adapter_listas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nombre_listas);
        ArrayAdapter<String> adapter_supermercados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, supermercados);
        ArrayAdapter<String> adapter_cantidad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores_cantidad);

        sp_lista.setAdapter(adapter_listas);
        sp_super.setAdapter(adapter_supermercados);
        et_cantidad.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});
        sp_cantidad.setAdapter(adapter_cantidad);

        bt_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BDHandler manejador1 = new BDHandler(getApplicationContext());
                String lista, supermercado;
                int id_articulo = (Integer) getIntent().getExtras().get("IdArticuloSimple");
                Articulo articulo = manejador1.obtenerArticulo(id_articulo);
                lista = et_lista.getText().toString();
                supermercado = et_super.getText().toString();

                if (!et_precio.getText().toString().isEmpty())
                    precio = Float.parseFloat(et_precio.getText().toString());

                if (precio >= 0) {

                    //intento obtener el artículo correspondiente a estos valores
                    ArticuloSupermercado artAux = manejador1.obtenerArticuloSupermercado(id_articulo, supermercado);
                    if (artAux == null) {
                        if (manejador1.insertarArticuloSupermercado(id_articulo, supermercado, precio) != -1) {
                            artAux = manejador1.obtenerArticuloSupermercado(id_articulo, supermercado);
                            Util.mostrarToast(v.getContext(), getResources().getString(R.string.Se_ha_creado_un_nuevo_articulo));
                        } else {
                            Util.mostrarToast(v.getContext(), getResources().getString(R.string.No_se_ha_podido_crear_articulo));
                            manejador1.close();
                            manejador.close();
                            finish();
                            overridePendingTransition(R.anim.right_in, R.anim.right_out);
                        }
                    }
                    ArticuloSupermercado articulo_super = artAux;

                    cantidad = 1;
                    if (!et_cantidad.getText().toString().isEmpty())
                        cantidad = Integer.parseInt(et_cantidad.getText().toString());

                    if (articulo != null) {
                        if (manejador1.estaLista(lista))
                            manejador1.insertarArticuloEnLista(id_articulo, lista, cantidad);
                        else{
                            if (manejador1.insertarLista(lista, supermercado))
                                manejador1.insertarArticuloEnLista(id_articulo, lista, cantidad);
                        }
                        manejador1.cerrar();
                        manejador.cerrar();
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }

                } else {
                    Util.mostrarToast(v.getContext(), getResources().getString(R.string.El_precio_mayor_cero));
                    manejador.cerrar();
                }
            }
        });

        sp_lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_lista.setText(parent.getItemAtPosition(position).toString());
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_lista.setText(getResources().getString(R.string.Nueva_lista));
            }
        });

        sp_super.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_super.setText(parent.getItemAtPosition(position).toString());
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_super.setText(getResources().getString(R.string.Nuevo_supermercado));
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

        manejador.cerrar();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}