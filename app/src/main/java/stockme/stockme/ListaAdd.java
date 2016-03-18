package stockme.stockme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class ListaAdd extends AppCompatActivity {
    private Button btn_aceptar;
    private Button btn_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Crear lista");

        btn_aceptar = (Button)findViewById(R.id.lista_add_btn_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                String nombre = ((EditText) findViewById(R.id.lista_add_et_nombre)).getText().toString();

                if (!nombre.matches("")) {
                    String fecha = Util.diaMesAnyo.format(new Date());

                    Lista nueva = new Lista(nombre, fecha, fecha);
                    BDHandler manejador = new BDHandler(v.getContext());

                    if(!manejador.insertarLista(nueva))
                        Toast.makeText(v.getContext(), "Ya existe la lista", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    CharSequence text = "¡Falta un nombre!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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
}
