package stockme.stockme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemArticulosLista;
import stockme.stockme.adaptadores.AdaptadorListItemListas;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;

public class ListaDeArticulos extends AppCompatActivity {
    private ListView articulos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_articulos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        articulos = (ListView)findViewById(R.id.lista_articulos_lista);

        Lista lista = new Lista(getIntent().getStringExtra("NombreLista"),"","","");
        this.setTitle(lista.getNombre());

        BDHandler manejador = new BDHandler(this);
        List<Articulo> listaArticulos = manejador.obtenerArticulosEnLista(lista);
        AdaptadorListItemArticulosLista adaptador = new AdaptadorListItemArticulosLista(this, listaArticulos, lista);
        articulos.setAdapter(adaptador);


    }

}
