package stockme.stockme;

import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class ArticuloSimpleAdd extends AppCompatActivity {
    private Button btnAceptar, btnCancelar;
    private EditText etNombre;
    private AutoCompleteTextView atv_marcas;
    private Spinner spTipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_simple_add);
        //toolbar + navbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Añadir artículo");

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //contenido

        etNombre = (EditText)findViewById(R.id.articulo_simple_add_et_nombre);
        spTipos = (Spinner)findViewById(R.id.articulo_simple_add_sp_tipos);
        atv_marcas = (AutoCompleteTextView)findViewById(R.id.articulo_simple_add_atv_marcas);
        atv_marcas.setThreshold(1);
        btnAceptar = (Button)findViewById(R.id.articulo_simple_add_btn_aceptar);
        btnCancelar = (Button)findViewById(R.id.articulo_simple_add_btn_cancelar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_array,
                android.R.layout.simple_spinner_item);
        spTipos.setAdapter(adapter);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre, marca, tipo;
                Articulo articulo = new Articulo();

                nombre = etNombre.getText().toString();
                marca = atv_marcas.getText().toString();
                tipo = spTipos.getSelectedItem().toString();

                final BDHandler manejador = new BDHandler(v.getContext());

                if (!nombre.isEmpty()) {

                    Articulo artAux = manejador.obtenerArticulo(nombre, marca, tipo);

                    if (artAux == null) {
                        if (manejador.insertarArticulo(nombre, marca, tipo) != -1) {
                            Util.mostrarToast(v.getContext(), "Se ha creado un nuevo artículo");
                            manejador.cerrar();
                            finish();
                        } else {
                            Util.mostrarToast(v.getContext(), "No se ha podido crear el artículo");
                            manejador.cerrar();
                            finish();
                        }
                    }else{
                        Util.mostrarToast(v.getContext(), "Ya existe el artículo");
                        manejador.cerrar();
                    }

                } else {
                    Util.mostrarToast(v.getContext(), "Debes insertar un nombre");
                }

            }
        });

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
