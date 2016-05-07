package stockme.stockme.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class AdaptadorGridItemCatalogoTipos extends BaseAdapter {
    private Context context;
    private List<Articulo> datos;
    private ImageButton borrar;

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
    public View getView(final int position, View view, final ViewGroup viewGroup) {

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
            view.setBackgroundResource(R.drawable.esquinas_congelados);
        }else if(tipo.equals("Dulces")){
            view.setBackgroundResource(R.drawable.esquinas_dulces);
        }else if(tipo.equals("Embutidos")){
            view.setBackgroundResource(R.drawable.esquinas_embutidos);
        }else if(tipo.equals("Frutas")){
            view.setBackgroundResource(R.drawable.esquinas_frutas);
        }else if(tipo.equals("Frutos secos")){
            view.setBackgroundResource(R.drawable.esquinas_frutos_secos);
        }else if(tipo.equals("Lácteos")){
            view.setBackgroundResource(R.drawable.esquinas_lacteos);
        }else if(tipo.equals("Panadería")){
            view.setBackgroundResource(R.drawable.esquinas_panaderia);
        }else if(tipo.equals("Pastas")){
            view.setBackgroundResource(R.drawable.esquinas_pastas);
        }else if(tipo.equals("Salsas")){
            view.setBackgroundResource(R.drawable.esquinas_salsas);
        }else if(tipo.equals("Verduras")){
            view.setBackgroundResource(R.drawable.esquinas_verduras);
        }else{
            view.setBackgroundResource(R.drawable.esquinas);
        }

        borrar = (ImageButton) view.findViewById(R.id.catalogo_borrar);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener borrarArticuloListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BDHandler manejador = new BDHandler(v.getContext());

                        if (!manejador.eliminarArticulo(datos.get(position)))
                            Util.mostrarToast(v.getContext(), "No se ha podido eliminar el artículo");
                        else {
                            Util.mostrarToast(v.getContext(), "Artículo eliminado");
                            datos.remove(position);
                        }

                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quieres eliminar el artículo?", borrarArticuloListener, v.getContext());
            }
        });

        return view;
    }

}