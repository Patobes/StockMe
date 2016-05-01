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
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class AdaptadorListItemArticulosListaCompra extends ArrayAdapter<Articulo> {
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

    //////atributos static para gestionar el precio. Necesario al no pertenecer los controles a esta vista
    private static TextView tv_precio_total;
    private static TextView tv_precio_compra;
    private static Map<Integer, Float> costes = new TreeMap<>();

    public static TextView getTv_precio_total() {
        return tv_precio_total;
    }
    public static void setTv_precio_total(TextView tv_precio_total) {
        AdaptadorListItemArticulosListaCompra.tv_precio_total = tv_precio_total;
    }

    private void actualizarPrecioTotal(){
        BDHandler manejador = new BDHandler(getContext());
        tv_precio_total.setText(String.valueOf(manejador.obtenerPrecioTotal(lista.getNombre())));
        manejador.cerrar();
    }

    public static TextView getTv_precio_compra() {
        return tv_precio_total;
    }
    public static void setTv_precio_compra(TextView tv_precio_compra) {
        AdaptadorListItemArticulosListaCompra.tv_precio_compra = tv_precio_compra;
    }

    private void actualizarPrecioCompra(){
        tv_precio_compra.setText(String.valueOf(getTotalCostes()));
    }

    private static void addCoste(Integer id, Float coste){
        if(isCoste(id)) {
            costes.put(id, getCoste(id) + coste);//actualiza su valor
        }else
            costes.put(id, coste);
    }
    private static void delCoste(Integer id){
        costes.remove(id);
    }
    private static Float getCoste(Integer id){
        return costes.get(id);
    }
    private static boolean isCoste(Integer id){
        return costes.containsKey(id);
    }
    public static void resetCostes(){
        costes.clear();
    }
    private static Float getTotalCostes(){
        Float total = 0.0f;
        for(Float f: costes.values())
            total += f;
        return roundTwoDecimals(total);
    }

//    public static float round(float d, int decimalPlace) {
//        BigDecimal bd = new BigDecimal(Float.toString(d));
//        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
//        return bd.floatValue();
//    }
    static float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(d));
    }
    /////////////

    public AdaptadorListItemArticulosListaCompra(Context context, List<Articulo> datos, Lista lista) {
        super(context, R.layout.listitem_articulos_lista_compra, datos);
        this.datos = datos;
        this.lista = lista;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_articulos_lista_compra, null);
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
                int idArticulo = datos.get(position).getId();
                int cantidad = manejador.obtenerCantidadArticuloEnLista(idArticulo, lista);

                if (cantidad < 99) {
                    manejador.modificarArticuloEnLista(new ListaArticulo(idArticulo, lista.getNombre(), cantidad + 1));
                    if(isCoste(idArticulo))
                        delCoste(idArticulo);
                }

                manejador.cerrar();
                actualizarPrecioCompra();
                actualizarPrecioTotal();
                notifyDataSetChanged();
            }
        });

        btnMenos = (ImageButton)item.findViewById(R.id.listitem_articulos_menos);
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                int idArticulo = datos.get(position).getId();
                int cantidad = manejador.obtenerCantidadArticuloEnLista(idArticulo, lista);

                if (cantidad > 0) {
                    manejador.modificarArticuloEnLista(new ListaArticulo(idArticulo, lista.getNombre(), cantidad - 1));
                    addCoste(idArticulo, datos.get(position).getPrecio());
                }

                manejador.cerrar();
                actualizarPrecioTotal();
                actualizarPrecioCompra();
                notifyDataSetChanged();
            }
        });

        articulos = (DynamicListView)parent.findViewById(R.id.lista_compra_lista);

        articulos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BDHandler manejador = new BDHandler(getContext());

                Articulo articulo = (Articulo) parent.getItemAtPosition(position);
                manejador.modificarArticuloEnLista(new ListaArticulo(articulo.getId(), lista.getNombre(), 0));

                Util.mostrarToast(view.getContext(), "Comprado: " + articulo.getNombre());
                actualizarPrecioTotal();
                addCoste(articulo.getId(), articulo.getPrecio() * manejador.numArticulosEnLista(lista.getNombre()));
                manejador.cerrar();

                actualizarPrecioCompra();
                notifyDataSetChanged();
                return true;
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
                                        actualizarPrecioTotal();
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
