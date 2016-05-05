package stockme.stockme.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;

public class AdaptadorGridItemCatalogoTipos extends BaseAdapter {
    private Context context;
    private List<Articulo> datos;
    private GridView articulos;

    public AdaptadorGridItemCatalogoTipos(Context context, List<Articulo> articulos) {
        this.context = context;
        this.datos = articulos;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Articulo getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.griditem_catalogo, viewGroup, false);
        }

        TextView nombre = (TextView) view.findViewById(R.id.catalogo_art_nombre);
        nombre.setText(datos.get(position).getNombre());

        TextView tipo = (TextView) view.findViewById(R.id.catalogo_art_tipo);
        tipo.setText(datos.get(position).getTipo());

        switch (datos.get(position).getTipo()){
            case "Cualquiera":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Cualquiera));
            }
            case "Congelados":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Congelados));
            }
            case "Dulces":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Dulces));
            }
            case "Embutidos":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Embutidos));
            }
            case "Frutas":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Frutas));
            }
            case "Frutos secos":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Frutos_secos));
            }
            case "Lácteos":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Lácteos));
            }
            case "Panadería":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Panadería));
            }
            case "Pastas":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Pastas));
            }
            case "Salsas":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Salsas));
            }
            case "Verduras":{
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.Verduras));
            }
        }

        return view;
    }

}