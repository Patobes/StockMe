package stockme.stockme;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.InputFilterMinMax;
import stockme.stockme.util.Util;

/**
 * Created by paris on 17/05/2016.
 */
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
        this.setTitle("Añadir Stock a Lista");

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

                String lista, supermercado;
                int id_articulo = (Integer) getIntent().getExtras().get("IdArticuloSimple");
                Articulo articulo = manejador.obtenerArticulo(id_articulo);
                lista = et_lista.getText().toString();
                supermercado = et_super.getText().toString();

                if (!et_precio.getText().toString().isEmpty())
                    precio = Float.parseFloat(et_precio.getText().toString());

                if (precio >= 0) {

                    //intento obtener el artículo correspondiente a estos valores
                    ArticuloSupermercado artAux = manejador.obtenerArticuloSupermercado(id_articulo, supermercado);
                    if (artAux == null) {
                        if (manejador.insertarArticuloSupermercado(id_articulo, supermercado, precio) != -1) {
                            artAux = manejador.obtenerArticuloSupermercado(id_articulo, supermercado);
                            Util.mostrarToast(v.getContext(), "Se ha creado un nuevo artículo");
                        } else {
                            Util.mostrarToast(v.getContext(), "No se ha podido crear el artículo");
                            finish();
                            overridePendingTransition(R.anim.right_in, R.anim.right_out);
                        }
                    }
                    ArticuloSupermercado articulo_super = artAux;

                    cantidad = 1;
                    if (!et_cantidad.getText().toString().isEmpty())
                        cantidad = Integer.parseInt(et_cantidad.getText().toString());

                    if (articulo != null) {
                        manejador.insertarArticuloEnLista(new ListaArticulo(id_articulo, lista, cantidad));
                        manejador.cerrar();
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }

                } else {
                    Util.mostrarToast(v.getContext(), "El precio debe ser mayor o igual a 0");
                    manejador.cerrar();
                }

                ///////////////
                /*
                String nombre = et_nombre.getText().toString();
                String marca = atv_marca.getText().toString();
                String tipo = sp_tipo.getSelectedItem().toString();
                cantidad = Integer.parseInt(et_cantidad.getText().toString());
                minimo = Integer.parseInt(et_minimo.getText().toString());

                if (cantidad == 0) {
                    Util.mostrarToast(getApplicationContext(), "Introduce una cantidad mayor que 0!");
                } else {
                    final BDHandler manejador1 = new BDHandler(v.getContext());
                    if(manejador1.estaStock(nombre, marca)) {
                        stock = manejador1.obtenerStock(nombre, marca);
                        DialogInterface.OnClickListener sumarCantidad = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                manejador1.modificarStockCantidad(stock, stock.getCantidad() + cantidad);
                                Util.mostrarToast(getApplicationContext(), cantidad + " unidades añadidas!");
                                manejador.cerrar();
                                manejador1.cerrar();
                                finish();
                                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                            }
                        };
                        String mensaje = "El articulo que has introducido ya está en Stock\r\n¿Deseas añadir " + cantidad + " unidades al Stock?";
                        Util.crearMensajeAlerta(mensaje, sumarCantidad, v.getContext());

                    } else{
                        if(manejador1.insertarStock(nombre, marca, tipo, cantidad, minimo))
                            Util.mostrarToast(getApplicationContext(), "Articulo añadido!");
                        else
                            Util.mostrarToast(getApplicationContext(), "No se ha podido añadir el articulo al Stock");
                        manejador.cerrar();
                        manejador1.cerrar();
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }
                }*/
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
                et_lista.setText("Nueva lista");
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
                et_super.setText("Nuevo supermercado");
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