package stockme.stockme;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemArticulosLista;
import stockme.stockme.adaptadores.AdaptadorListItemListas;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class ListaDeArticulos extends AppCompatActivity{
    private DynamicListView articulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_articulos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        articulos = (DynamicListView)findViewById(R.id.lista_articulos_lista);

        final Lista lista = new Lista(getIntent().getStringExtra("NombreLista"),"","","");
        this.setTitle(lista.getNombre());

        final BDHandler manejador = new BDHandler(this);
        List<Articulo> listaArticulos = manejador.obtenerArticulosEnLista(lista);
        final AdaptadorListItemArticulosLista adaptador = new AdaptadorListItemArticulosLista(this, listaArticulos, lista);
        articulos.setAdapter(adaptador);

        manejador.close();

    }

}
