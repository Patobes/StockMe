package stockme.stockme;

import android.content.DialogInterface;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.InputFilterMinMax;
import stockme.stockme.util.Util;

public class StockAdd extends AppCompatActivity implements Fragment_listas.OnFragmentInteractionListener{
    private EditText et_nombre;
    private AutoCompleteTextView atv_marca;
    private Spinner sp_tipo;
    private EditText et_cantidad;
    private Spinner sp_cantidad;
    private EditText et_minimo;
    private Spinner sp_minimo;
    private Button bt_añadir;
    private ImageButton bt_aceptar;
    Stock stock;
    int cantidad;
    int minimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_add);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Añadir stock");

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setHomeButtonEnabled(true);

        //Contenido
        et_nombre = (EditText)findViewById(R.id.stock_add_et_nombre);
        atv_marca = (AutoCompleteTextView) findViewById(R.id.stock_add_atv_marca);
        sp_tipo = (Spinner) findViewById(R.id.stock_add_sp_tipo);
        et_cantidad = (EditText)findViewById(R.id.stock_add_et_cantidad);
        sp_cantidad = (Spinner)findViewById(R.id.stock_add_sp_cantidad);
        et_minimo = (EditText)findViewById(R.id.stock_add_et_minimo);
        sp_minimo = (Spinner)findViewById(R.id.stock_add_sp_minimo);
        bt_añadir = (Button)findViewById(R.id.stock_add_bt_añadir);
        bt_aceptar = (ImageButton)findViewById(R.id.stock_add_bt_aceptar);

        //Propiedades del contenido
        final BDHandler manejador = new BDHandler(this);
        String []valores_cantidad = new String[]{"1","2","3","4","5","6","7","8","9","10"};
        String []valores_minimo = new String[]{"0","1","2","3","4","5","6","7","8","9","10"};
        List<String> marcas = manejador.obtenerMarcas();

        ArrayAdapter<String> adapter_marcas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, marcas);
        ArrayAdapter<CharSequence> adapter_tipos = ArrayAdapter.createFromResource(this, R.array.tipos_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter_cantidad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores_cantidad);
        ArrayAdapter<String> adapter_minimo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, valores_minimo);

        atv_marca.setAdapter(adapter_marcas);
        sp_tipo.setAdapter(adapter_tipos);
        et_cantidad.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});
        sp_cantidad.setAdapter(adapter_cantidad);
        et_minimo.setFilters(new InputFilter[]{new InputFilterMinMax("0", "10")});
        sp_minimo.setAdapter(adapter_minimo);

        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CatalogoArticulos.class);
                i.putExtra("vieneDe", "stockAdd");
                startActivityForResult(i, 1);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        bt_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = et_nombre.getText().toString();
                String marca = atv_marca.getText().toString();
                String tipo = sp_tipo.getSelectedItem().toString();
                cantidad = Integer.parseInt(et_cantidad.getText().toString());
                minimo = Integer.parseInt(et_minimo.getText().toString());

                if (nombre == null || nombre.isEmpty() || nombre.matches("") || nombre.matches(" ")) {
                    Util.mostrarToast(getApplicationContext(), "Introduce el nombre del articulo!");
                } else if (marca == null || marca.isEmpty() || marca.matches("") || marca.matches(" ")) {
                    Util.mostrarToast(getApplicationContext(), "Introduce la marca del articulo!");
                } else if (cantidad == 0) {
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

        sp_minimo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_minimo.setText(parent.getItemAtPosition(position).toString());
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_minimo.setText("0");
            }
        });
        manejador.cerrar();
    }

    //TODO: hay que controlar que al añadir el artículo al stock vuelve a la principal y carga la lista
    //debería cargar stock


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case android.R.id.home:
//                // ProjectsActivity is my 'home' activity
//                startActivityAfterCleanup(Principal.class);
//                return true;
//        }
//        return (super.onOptionsItemSelected(menuItem));
//    }
//
//    private void startActivityAfterCleanup(Class<?> cls) {
//        //if (projectsDao != null) projectsDao.close();
//        Intent intent = new Intent(getApplicationContext(), cls);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//    }


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            Articulo articulo = data.getExtras().getParcelable("Articulo");
            et_nombre.setText(articulo.getNombre());
            atv_marca.setText(articulo.getMarca());
            for (int i = 0; i < sp_tipo.getCount(); i++){
                String tipo = (String)sp_tipo.getItemAtPosition(i);
                if (tipo.compareTo(articulo.getTipo()) == 0)
                    sp_tipo.setSelection(i);
            }
        }
    }
}

