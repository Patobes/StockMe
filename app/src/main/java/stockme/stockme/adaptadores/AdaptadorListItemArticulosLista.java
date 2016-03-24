package stockme.stockme.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class AdaptadorListItemArticulosLista extends ArrayAdapter<Articulo> {
    private List<Articulo> datos;
    private Lista lista;
    private Articulo articulo;
    private TextView lblNombre;
    private TextView lblSupermercado;
    private TextView lblMarca;
    private TextView lblPrecio;
    private TextView lblCantidad;
    private ImageButton btnMas;
    private ImageButton btnMenos;
    private CheckBox cbCompletar;


    public AdaptadorListItemArticulosLista(Context context, List<Articulo> datos, Lista lista) {
        super(context, R.layout.listitem_articulos_lista, datos);
        this.datos = datos;
        this.lista = lista;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_articulos_lista, null);
        BDHandler manejador = new BDHandler(getContext());

        //se crea un elemento Articulo que contiene los datos de la fila
        articulo = new Articulo();
        articulo.setId(datos.get(position).getId());
        articulo.setNombre(datos.get(position).getNombre());
        articulo.setPrecio(datos.get(position).getPrecio());
        articulo.setSupermercado(datos.get(position).getSupermercado());
        articulo.setMarca(datos.get(position).getMarca());

        lblNombre = (TextView)item.findViewById(R.id.listitem_articulos_nombre);
        lblNombre.setText(articulo.getNombre());

        lblMarca = (TextView)item.findViewById(R.id.listitem_articulos_marca);
        lblMarca.setText(articulo.getMarca());

        lblSupermercado = (TextView)item.findViewById(R.id.listitem_articulos_superm);
        lblSupermercado.setText(articulo.getSupermercado());

        lblPrecio = (TextView)item.findViewById(R.id.listitem_articulos_tv_precio);
        lblPrecio.setText(Float.toString(articulo.getPrecio()));

        int cantidad = manejador.obtenerCantidadArticuloEnLista(articulo.getId(), lista);

        lblCantidad = (TextView)item.findViewById(R.id.listitem_articulos_cantidad);
        lblCantidad.setText(Integer.toString(cantidad));

        btnMas = (ImageButton)item.findViewById(R.id.listitem_articulos_mas);
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                int cantidad = manejador.obtenerCantidadArticuloEnLista(datos.get(position).getId(), lista);
                manejador.modificarArticuloEnLista(new ListaArticulo(datos.get(position).getId(), lista.getNombre(), cantidad + 1));
                manejador.cerrar();
                notifyDataSetChanged();
            }
        });

        btnMenos = (ImageButton)item.findViewById(R.id.listitem_articulos_menos);
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                int cantidad = manejador.obtenerCantidadArticuloEnLista(datos.get(position).getId(), lista);
                manejador.modificarArticuloEnLista(new ListaArticulo(datos.get(position).getId(),lista.getNombre(), cantidad - 1));
                manejador.cerrar();
                notifyDataSetChanged();
            }
        });


        //TODO arreglar el checkbox para que se chekee y arreglar la vista para cantidades >10, no dejar q metan <0
        cbCompletar = (CheckBox)item.findViewById(R.id.listitem_articulos_completar);
        cbCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());

                if (!cbCompletar.isChecked()) {
                    manejador.modificarArticuloEnLista(new ListaArticulo(datos.get(position).getId(), lista.getNombre(), 0));
                } else {
                    manejador.modificarArticuloEnLista(new ListaArticulo(datos.get(position).getId(), lista.getNombre(), 1));
                }

                cbCompletar.setChecked(!cbCompletar.isChecked());

                manejador.cerrar();
                notifyDataSetChanged();
            }
        });

        manejador.close();
        return item;
    }
}
