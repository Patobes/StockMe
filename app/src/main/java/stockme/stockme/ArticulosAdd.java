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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.InputFilterMinMax;
import stockme.stockme.util.Util;

public class ArticulosAdd extends AppCompatActivity implements Fragment_listas.OnFragmentInteractionListener {
    private EditText etNombre, etPrecio, etCantidad;
    private AutoCompleteTextView atv_marcas;
    private Spinner spTipos, spSupermercado;
    private Button btnCatalogo;
    private ImageButton btnAceptar;
    String nLista;
    ArticuloSupermercado articulo;
    int cantidad;
    float precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_add);
        //toolbar + navbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getResources().getString(R.string.Añadir_articulo));

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //contenido
        etNombre = (EditText)findViewById(R.id.articulos_add_et_nombre);
        etPrecio = (EditText)findViewById(R.id.articulos_add_et_precio);
        etCantidad = (EditText)findViewById(R.id.articulos_add_et_cantidad);
        etCantidad.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99")});
        spTipos = (Spinner)findViewById(R.id.articulos_add_sp_tipos);
        spSupermercado = (Spinner)findViewById(R.id.articulos_add_sp_supermercado);
        btnAceptar = (ImageButton)findViewById(R.id.articulos_add_btn_aceptar);
        btnCatalogo = (Button)findViewById(R.id.articulos_add_btn_catalogo);
        atv_marcas = (AutoCompleteTextView)findViewById(R.id.articulos_add_atv_marcas);
        atv_marcas.setThreshold(1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_array,
                        android.R.layout.simple_spinner_item);
        spTipos.setAdapter(adapter);

        final BDHandler manejador = new BDHandler(this);
        List<String> supermercados = manejador.obtenerSupermercados();
        List<String> marcas = manejador.obtenerMarcas();

        ArrayAdapter<String> adptMarcas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, marcas);
        atv_marcas.setAdapter(adptMarcas);

        ArrayAdapter<String> adapterSuperM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supermercados);
        //adapterSuperM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSupermercado.setAdapter(adapterSuperM);

        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CatalogoArticulos.class);
                i.putExtra("vieneDe", "articulosAdd");
                startActivityForResult(i, 1);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        Intent intent = getIntent();
        nLista = null;
        precio = -1;//para luego hacer la comprobación de introducción de precio
        if(intent != null) {
            nLista = intent.getExtras().getString("NombreLista");

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombre, marca, tipo, supermercado;
                    articulo = new ArticuloSupermercado();
                    nombre = etNombre.getText().toString();
                    marca = atv_marcas.getText().toString();
                    tipo = spTipos.getSelectedItem().toString();
                    supermercado = spSupermercado.getSelectedItem().toString();

                    final BDHandler manejador2 = new BDHandler(v.getContext());

                    if (!etPrecio.getText().toString().isEmpty())
                        precio = Float.parseFloat(etPrecio.getText().toString());

                    if (precio >= 0) {
                        if (nombre != null && !nombre.isEmpty()) {

                            //intento obtener el artículo correspondiente a estos valores
                            ArticuloSupermercado artAux = manejador2.obtenerArticuloSupermercado(nombre, marca, tipo, supermercado);
                            if (artAux == null) {
                                if (manejador2.insertarArticuloSupermercado(nombre, marca, tipo, supermercado, precio) != -1) {
                                    artAux = manejador2.obtenerArticuloSupermercado(nombre, marca, tipo, supermercado);
                                    Util.mostrarToast(v.getContext(), getResources().getString(R.string.Se_ha_creado_un_nuevo_articulo));
                                } else {
                                    Util.mostrarToast(v.getContext(), getResources().getString(R.string.No_se_ha_podido_crear_articulo));
                                    finish();
                                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                                }
                            }
                            articulo = artAux;

                            cantidad = 1;
                            if (!etCantidad.getText().toString().isEmpty())
                                cantidad = Integer.parseInt(etCantidad.getText().toString());

                            if (articulo != null) {
                                //una vez tengo un artículo, compruebo si está en la lista
                                if (manejador2.estaArticuloEnLista(articulo.getId(), nLista)) {
                                    DialogInterface.OnClickListener aceptar = new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (articulo.getPrecio() != precio) {
                                                articulo.setPrecio(precio);
                                                manejador2.modificarArticuloSupermercadoPrecio(articulo, precio);
                                                Util.mostrarToast(ArticulosAdd.this, getResources().getString(R.string.Precio_actualizado));
                                            }
                                            ListaArticulo la = manejador2.obtenerListaArticulo(articulo.getId(), nLista);
                                            manejador2.modificarArticuloEnListaCantidad(la, la.getCantidad() + cantidad);
                                            manejador.cerrar();
                                            manejador2.cerrar();
                                            finish();
                                            overridePendingTransition(R.anim.right_in, R.anim.right_out);
                                        }
                                    };
                                    Util.crearMensajeAlerta(getResources().getString(R.string.Ya_esta_articulo_lista_pregunta), getResources().getString(R.string.Articulo_existente),
                                            aceptar, ArticulosAdd.this);
                                } else {
                                    manejador2.insertarArticuloEnLista(new ListaArticulo(articulo.getId(), nLista, cantidad));
                                    manejador.cerrar();
                                    manejador2.cerrar();
                                    finish();
                                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                                }
                            }

                        } else {
                            Util.mostrarToast(v.getContext(), getResources().getString(R.string.Debes_insertar_nombre));
                            manejador.cerrar();
                            manejador2.cerrar();
                        }
                    } else {
                        Util.mostrarToast(v.getContext(), getResources().getString(R.string.El_precio_mayor_cero));
                        manejador.cerrar();
                        manejador2.cerrar();
                    }
                }
            });

        }else{
            Util.mostrarToast(this, getResources().getString(R.string.No_se_ha_podido_cargar_lista));
            manejador.cerrar();
            finish();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==1){
            Articulo articulo = data.getExtras().getParcelable("Articulo");
            etNombre.setText(articulo.getNombre());
            atv_marcas.setText(articulo.getMarca());
            for (int i = 0; i < spTipos.getCount(); i++){
                String tipo = (String)spTipos.getItemAtPosition(i);
                if (tipo.compareTo(articulo.getTipo()) == 0)
                    spTipos.setSelection(i);
            }
        }
    }
}
