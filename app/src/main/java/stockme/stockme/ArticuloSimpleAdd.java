package stockme.stockme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class ArticuloSimpleAdd extends AppCompatActivity {
    private ImageButton btnAceptar;
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
        this.setTitle(getResources().getString(R.string.Añadir_articulo));

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //contenido
        final BDHandler manejador = new BDHandler(this);

        etNombre = (EditText)findViewById(R.id.articulo_simple_add_et_nombre);
        spTipos = (Spinner)findViewById(R.id.articulo_simple_add_sp_tipos);
        atv_marcas = (AutoCompleteTextView)findViewById(R.id.articulo_simple_add_atv_marcas);

        List<String> marcas = manejador.obtenerMarcas();
        ArrayAdapter<String> adapter_marcas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, marcas);
        atv_marcas.setAdapter(adapter_marcas);

        btnAceptar = (ImageButton)findViewById(R.id.articulo_simple_add_btn_aceptar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_array,
                R.layout.spinner_item);
        spTipos.setAdapter(adapter);

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
                            Util.mostrarToast(v.getContext(), getResources().getString(R.string.Se_ha_creado_un_nuevo_articulo));
                        } else {
                            Util.mostrarToast(v.getContext(), getResources().getString(R.string.No_se_ha_podido_crear_articulo));
                        }
                        manejador.cerrar();
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }else{
                        Util.mostrarToast(v.getContext(), getResources().getString(R.string.Ya_existe));
                        manejador.cerrar();
                    }

                } else {
                    Util.mostrarToast(v.getContext(), getResources().getString(R.string.Debes_insertar_nombre));
                }

            }
        });
        manejador.close();
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
