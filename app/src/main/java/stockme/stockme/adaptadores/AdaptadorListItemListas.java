package stockme.stockme.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Lista;

/**
 * Created by JuanMiguel on 08/03/2016.
 */
public class AdaptadorListItemListas extends ArrayAdapter<Lista> {
    private List<Lista> datos;

    public AdaptadorListItemListas(Context context, List<Lista> datos) {
        super(context, R.layout.listitem_lista, datos);
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_lista, null);

        TextView lblNombre = (TextView)item.findViewById(R.id.listitem_lista_nombre);
        lblNombre.setText(datos.get(position).getNombre());

        TextView lblSupermercado = (TextView)item.findViewById(R.id.listitem_lista_supermercado);
        lblSupermercado.setText(datos.get(position).getSupermercado());

        TextView lblFecha = (TextView)item.findViewById(R.id.listitem_lista_fecha);
        lblFecha.setText(datos.get(position).getFecha());

        TextView lblNProductos = (TextView)item.findViewById(R.id.listitem_lista_nproductos);
        lblNProductos.setText(String.valueOf(datos.get(position).getNumProductos()));

        return(item);
    }
}
