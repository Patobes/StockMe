package stockme.stockme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemArticulosListaCompra;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;

public class ListaCompra extends AppCompatActivity implements /*NavigationView.OnNavigationItemSelectedListener,*/
        Fragment_listas.OnFragmentInteractionListener{
    private static String nombreLista;
    private DynamicListView articulos;
    private Button lista_compra_btn_mas;
    private ImageButton ibtn_reset;
    private TextView tv_precio_total;
    private TextView tv_precio_compra;
    private Lista lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getResources().getString(R.string.Articulos));

        //para flecha de atrás de navegación
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //las instancias tmb deberían hacerse una sola vez
        articulos = (DynamicListView)findViewById(R.id.lista_compra_lista);
        lista_compra_btn_mas = (Button)findViewById(R.id.lista_compra_btn_mas);
        ibtn_reset = (ImageButton)findViewById(R.id.lista_compra_btn_reset);
        tv_precio_total = (TextView)findViewById(R.id.lista_compra_tv_precio_total);
        tv_precio_compra = (TextView)findViewById(R.id.lista_compra_tv_precio_compra);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mon = prefs.getString("moneda", "€");

        TextView moneda1 = (TextView) findViewById(R.id.lista_compra_tv_simb_moneda);
        moneda1.setText(mon);

        TextView moneda2 = (TextView) findViewById(R.id.lista_compra_tv_simb_moneda2);
        moneda2.setText(mon);

        ibtn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdaptadorListItemArticulosListaCompra.resetCostes();
                tv_precio_compra.setText("0.0");
            }
        });

        AdaptadorListItemArticulosListaCompra.resetCostes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //contenido
        //controlo con el atributo estático ya que al implementar el botón de arriba (flecha)
        //se llama de nuevo la activity, no solo se cierra, por lo que getIntent() no obtenría el
        //extra NombreLista al volver de ArticulosAdd
        String nomL = getIntent().getStringExtra("NombreLista");
        if(nomL != null){//viene de presionar la lista de la compra
            lista = new Lista(nomL, "", "", "");
            nombreLista = nomL; //esto para la traza de cuando se vuelva
        }else{
            lista = new Lista(nombreLista, "", "", "");
        }
        this.setTitle(lista.getNombre());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final BDHandler manejador = new BDHandler(this);
        List<ArticuloSupermercado> listaArticulos = manejador.obtenerArticulosEnLista(lista, prefs.getString("orden_listas", "ASC"));
//        List<ArticuloSupermercado> listaArticulos = manejador.obtenerArticulosEnLista(lista);
        final AdaptadorListItemArticulosListaCompra adaptador = new AdaptadorListItemArticulosListaCompra(this, listaArticulos, lista);


        tv_precio_total.setText(String.valueOf(AdaptadorListItemArticulosListaCompra.round(manejador.obtenerPrecioTotal(lista.getNombre()), 2)));

        AdaptadorListItemArticulosListaCompra.setTv_precio_total(tv_precio_total);
        AdaptadorListItemArticulosListaCompra.setTv_precio_compra(tv_precio_compra);

        articulos.setAdapter(adaptador);
        manejador.close();

        lista_compra_btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaCompra.this, ArticulosAdd.class);
                i.putExtra("NombreLista", lista.getNombre());
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
