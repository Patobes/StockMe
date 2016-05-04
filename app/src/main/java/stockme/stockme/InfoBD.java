package stockme.stockme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class InfoBD extends AppCompatActivity {
    private TextView tv_articulos, tv_articulos_super, tv_superm, tv_stock, tv_listas, tv_lista_articulo;
    private Spinner sp_articulos, sp_articulos_super, sp_superm, sp_stock, sp_listas, sp_lista_articulos;
    private Button btn_borrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_bd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tv_articulos = (TextView)findViewById(R.id.info_tv_articulos);
        tv_articulos_super = (TextView)findViewById(R.id.info_tv_articulos_super);
        tv_stock = (TextView)findViewById(R.id.info_tv_stock);
        tv_superm = (TextView)findViewById(R.id.info_tv_superm);
        tv_listas = (TextView)findViewById(R.id.info_tv_listas);
        tv_lista_articulo = (TextView)findViewById(R.id.info_tv_lista_articulo);
        sp_articulos = (Spinner)findViewById(R.id.info_sp_articulos);
        sp_articulos_super = (Spinner)findViewById(R.id.info_sp_articulos_super);
        sp_stock = (Spinner)findViewById(R.id.info_sp_stock);
        sp_superm = (Spinner)findViewById(R.id.info_sp_superm);
        sp_listas = (Spinner)findViewById(R.id.info_sp_listas);
        sp_lista_articulos = (Spinner)findViewById(R.id.info_sp_lista_articulo);
        btn_borrar = (Button)findViewById(R.id.info_btn_borrar);

        rellenarContenido();

        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().deleteDatabase("Productos");
                Util.mostrarToast(v.getContext(), "Se ha restablecido la base de datos");
                rellenarContenido();
            }
        });
    }

    private void rellenarContenido(){
        BDHandler manejador = new BDHandler(this);
        List<Articulo> listaArticulos = manejador.obtenerArticulos();
        List<ArticuloSupermercado> listaArticulosSupermercado = manejador.obtenerArticulosSupermercado();
        List<Stock> listaStocks = manejador.obtenerStocks();
        List<String> supermercados = manejador.obtenerSupermercados();
        List<Lista> listaListas = manejador.obtenerListas();
        List<ListaArticulo> listaListaArt = manejador.obtenerListasArticulos();
        manejador.cerrar();

        List<String> articulos = new ArrayList<>();
        for(Articulo art: listaArticulos){
            articulos.add(art.getId() + ", " + art.getNombre() + ", " + art.getMarca() + ", " + art.getTipo());
        }
        List<String> articulosSupermercado = new ArrayList<>();
        for(ArticuloSupermercado artSm: listaArticulosSupermercado){
            articulosSupermercado.add(artSm.getId() + ", " + artSm.getArticulo() + ", " + artSm.getSupermercado() + ", " + artSm.getPrecio());
        }
        List<String> stocks = new ArrayList<>();
        for(Stock stock: listaStocks){
            stocks.add("Art: " + stock.getArticulo() + ", x" + stock.getCantidad());
        }
        List<String> listas = new ArrayList<>();
        for(Lista l: listaListas){
            listas.add(l.getNombre() + ", " + l.getSupermercado() + ", " + l.getFechaModificacion());
        }
        List<String> listasArticulos = new ArrayList<>();
        for(ListaArticulo la: listaListaArt){
            listasArticulos.add(la.getArticulo() + ", " + la.getNombre() + ", " + la.getCantidad());
        }

        tv_articulos.setText("Artículos (" + articulos.size() + "):");
        tv_articulos_super.setText("Artículos Supermercado (" + articulosSupermercado.size() + "):");
        tv_stock.setText("Stock (" + stocks.size() + "):");
        tv_superm.setText("SuperM (" + supermercados.size() + "):");
        tv_listas.setText("Listas (" + listas.size() + "):");
        tv_lista_articulo.setText("Lista_articulo (" + listasArticulos.size() + "):");

        sp_articulos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, articulos));
        sp_articulos_super.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, articulosSupermercado));
        sp_stock.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stocks));
        sp_superm.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, supermercados));
        sp_listas.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listas));
        sp_lista_articulos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listasArticulos));
    }
}
