package stockme.stockme;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class ListaAdd extends AppCompatActivity implements Fragment_listas.OnFragmentInteractionListener{
    private ImageButton btn_aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Crear lista");

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();
        Spinner spinner = (Spinner) findViewById(R.id.lista_add_spinner_supermercados);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, supermercados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        manejador.cerrar();

        btn_aceptar = (ImageButton)findViewById(R.id.lista_add_btn_aceptar);
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
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    }
                    manejador.cerrar();
                } else {
                    Util.mostrarToast(v.getContext(), "¡Falta un nombre!");
                }

            }
        });

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
