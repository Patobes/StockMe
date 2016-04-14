package stockme.stockme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;

public class StockAdd extends AppCompatActivity {
    Spinner sp_cantidad;
    EditText et_cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Añadir a stock");

        Util.mostrarToast(this, "Anterior: " + Preferencias.getPreferenciaString("anterior"));

        sp_cantidad = (Spinner)findViewById(R.id.stock_add_sp_cantidad);
        et_cantidad = (EditText)findViewById(R.id.stock_add_et_cantidad);

        final String []valores = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23","24","25"};
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores);
        sp_cantidad.setAdapter(adaptador);

        //TODO: mirar cómo se oculta el texto del spinner para que solo salga la flecha y poder seleccionar un número
        sp_cantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_cantidad.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_cantidad.setText("1");
            }
        });

//        et_cantidad.setInputType(InputType.TYPE_NULL);
//        et_cantidad.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                new AlertDialog.Builder(v.getContext())
//                        .setTitle("Select Countries")
//                        .setAdapter(adaptador, new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int which) {
//                                et_cantidad.setText(valores[which].toString());
//                                dialog.dismiss();
//                            }
//                        }).create().show();
//            }
//        });
    }
}
