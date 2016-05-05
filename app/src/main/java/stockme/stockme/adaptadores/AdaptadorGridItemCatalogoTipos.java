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

        TextView marca = (TextView) view.findViewById(R.id.catalogo_art_marca);
        marca.setText(datos.get(position).getMarca());

        String tipo = datos.get(position).getTipo();

        TextView tv_tipo = (TextView) view.findViewById(R.id.catalogo_art_tipo);
        tv_tipo.setText(datos.get(position).getTipo());

        if(tipo.equals("Congelados")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Congelados));
        }else if(tipo.equals("Dulces")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Dulces));
        }else if(tipo.equals("Embutidos")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Embutidos));
        }else if(tipo.equals("Frutas")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Frutas));
        }else if(tipo.equals("Frutos secos")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Frutos_secos));
        }else if(tipo.equals("Lácteos")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Lácteos));
        }else if(tipo.equals("Panadería")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Panadería));
        }else if(tipo.equals("Pastas")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Pastas));
        }else if(tipo.equals("Salsas")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Salsas));
        }else if(tipo.equals("Verduras")){
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Verduras));
        }else{
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.Cualquiera));
        }

        return view;
    }

}