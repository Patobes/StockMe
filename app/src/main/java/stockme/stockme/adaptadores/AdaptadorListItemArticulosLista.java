package stockme.stockme.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.persistencia.BDHandler;

/**
 * Created by Juanmi on 18/03/2016.
 */
public class AdaptadorListItemArticulosLista extends ArrayAdapter<Articulo> {
    private List<Articulo> datos;

    public AdaptadorListItemArticulosLista(Context context, List<Articulo> datos) {
        super(context, R.layout.listitem_articulos_lista, datos);
        this.datos = datos;
    }

    /*TODO: hay que obtener en en ListaDeArticulo el extra que se puso en el AdaptadorListItemListas con el nombre de la lista
    mirad cómo obtener datos de un intent. Luego se le tiene que pasar a este adapter y analizar el otro adaptador para ver
    cómo extraer y construir los datos*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_articulos_lista, null);
        BDHandler manejador = new BDHandler(getContext());

        manejador.close();
        return item;
    }
}
