package stockme.stockme.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.SwipeDetector;
import stockme.stockme.util.Util;

public class AdaptadorListItemArticulosLista extends ArrayAdapter<Articulo> {
    private DynamicListView articulos;
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

        //pintar los elementos de forma intercalada
        if ( position % 2 == 1) {
            item.setBackgroundColor(Color.parseColor("#E9EBEB"));
        }

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

        if (cantidad == 0)
            item.setBackgroundColor(Color.parseColor("#D887FF7D"));

        btnMas = (ImageButton)item.findViewById(R.id.listitem_articulos_mas);
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                int cantidad = manejador.obtenerCantidadArticuloEnLista(datos.get(position).getId(), lista);

                if (cantidad < 99)
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

                if (cantidad > 0)
                    manejador.modificarArticuloEnLista(new ListaArticulo(datos.get(position).getId(),lista.getNombre(), cantidad - 1));

                manejador.cerrar();
                notifyDataSetChanged();
            }
        });

        articulos = (DynamicListView)parent.findViewById(R.id.lista_articulos_lista);

        articulos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BDHandler manejador = new BDHandler(getContext());

                Articulo articulo = (Articulo) parent.getItemAtPosition(position);
                manejador.modificarArticuloEnLista(new ListaArticulo(articulo.getId(), lista.getNombre(), 0));
                manejador.cerrar();

                Util.mostrarToast(view.getContext(), "Comprado: " + articulo.getNombre());
                notifyDataSetChanged();
                return true;
            }
        });

        final SwipeDetector swipeDetector = new SwipeDetector();
        articulos.setOnTouchListener(swipeDetector);

        articulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL || swipeDetector.getAction() == SwipeDetector.Action.LR) {

                        DialogInterface.OnClickListener borrarArticuloListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BDHandler manejador = new BDHandler(getContext());

                                if (!manejador.eliminarArticuloEnLista(new ListaArticulo(datos.get(position).getId(), lista.getNombre(),0)))
                                    Util.mostrarToast(getContext(), "No se ha podido eliminar el articulo");
                                else {
                                    Util.mostrarToast(getContext(), "Articulo eliminado");
                                    notifyDataSetChanged();
                                    remove(datos.get(position));
                                }

                                manejador.cerrar();
                            }
                        };
                        Util.crearMensajeAlerta("¿Eliminar "+ datos.get(position).getNombre() +"?", borrarArticuloListener, getContext());
                    }
                }
            }
        });


        articulos.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                        for (final int position : reverseSortedPositions) {
                            DialogInterface.OnClickListener borrarArticuloListener = new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    BDHandler manejador = new BDHandler(getContext());

                                    if (!manejador.eliminarArticuloEnLista(new ListaArticulo(datos.get(position).getId(), lista.getNombre(),0)))
                                        Util.mostrarToast(getContext(), "No se ha podido eliminar el articulo");
                                    else {
                                        Util.mostrarToast(getContext(), "Articulo eliminado");
                                        remove(datos.get(position));
                                        notifyDataSetChanged();
                                    }

                                    manejador.cerrar();
                                }
                            };
                            Util.crearMensajeAlerta("¿Eliminar "+ datos.get(position).getNombre() +"?", borrarArticuloListener, getContext());

                        }
                    }
                }
        );

        manejador.close();
        return item;
    }

}
