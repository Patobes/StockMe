package stockme.stockme.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OnSwipeTouchListener;
import stockme.stockme.util.Util;

/**
 * Created by Paris on 17/03/2016.
 */
public class AdaptadorListItemStock extends ArrayAdapter<Stock>{
    private List<Stock> datos;
    private TextView tv_nombre;
    private TextView tv_marca;
    private TextView tv_cantidad;
    private TextView tv_minimo;
    private ImageButton ib_mas;
    private ImageButton ib_menos;
    private Stock stock;

    public AdaptadorListItemStock(Context context, List<Stock> datos) {
        super(context, R.layout.listitem_stock, datos);
        this.datos = datos;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View item = inflater.inflate(R.layout.listitem_stock, null);

        stock = new Stock(datos.get(position).getArticulo(),
                datos.get(position).getCantidad(),
                datos.get(position).getMinimo());

        tv_nombre = (TextView)item.findViewById(R.id.listitem_stock_nombre);
        tv_marca = (TextView)item.findViewById(R.id.listitem_stock_marca);
        tv_cantidad = (TextView)item.findViewById(R.id.listitem_stock_cantidad);
        tv_minimo = (TextView)item.findViewById(R.id.listitem_stock_tv_minimo);
        ib_mas = (ImageButton) item.findViewById(R.id.listitem_stock_mas);
        ib_menos = (ImageButton) item.findViewById(R.id.listitem_stock_menos);

        final BDHandler manejador = new BDHandler(getContext());

        tv_nombre.setText(manejador.obtenerArticulo(stock.getArticulo()).getNombre());
        tv_marca.setText(manejador.obtenerArticulo(stock.getArticulo()).getMarca());
        tv_cantidad.setText(String.valueOf(stock.getCantidad()));
        tv_minimo.setText(String.valueOf(stock.getMinimo()));

        ib_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                Stock stock1 = manejador.obtenerStock(datos.get(position).getArticulo());
                int cantidad = stock1.getCantidad() + 1;
                if(stock1.getCantidad() < 99)
                    manejador.modificarStockCantidad(stock1, cantidad);
                manejador.cerrar();
                datos.get(position).setCantidad(cantidad);
                notifyDataSetChanged();
            }
        });

        ib_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                Stock stock1 = manejador.obtenerStock(datos.get(position).getArticulo());
                int cantidad = stock1.getCantidad() - 1;
                if (stock1.getCantidad() > 0)
                    manejador.modificarStockCantidad(stock1, cantidad);
                manejador.cerrar();
                datos.get(position).setCantidad(cantidad);
                notifyDataSetChanged();
            }
        });

        /*
        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener borrarStockListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BDHandler manejador = new BDHandler(getContext());
                        if (!manejador.eliminarStock(datos.get(position).getArticulo()))
                            Util.mostrarToast(getContext(), "No se ha podido quitar el articulo");
                        else {
                            Util.mostrarToast(getContext(), "Articulo quitado del Stock");
                            remove(datos.get(position));
                            notifyDataSetChanged();
                        }

                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quitar " + manejador.obtenerArticulo(datos.get(position).getArticulo()).getNombre() + " " +
                        manejador.obtenerArticulo(datos.get(position).getArticulo()).getMarca() + " del Stock?", borrarStockListener, getContext());

                return false;
            }
        });*/

        item.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                ib_menos.performClick();
            }
            @Override
            public void onSwipeRight() {
                ib_mas.performClick();
            }
            @Override
            public void onLongClick(){
                DialogInterface.OnClickListener borrarStockListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BDHandler manejador = new BDHandler(getContext());
                        if (!manejador.eliminarStock(datos.get(position).getArticulo()))
                            Util.mostrarToast(getContext(), "No se ha podido quitar el articulo");
                        else {
                            Util.mostrarToast(getContext(), "Articulo quitado del Stock");
                            remove(datos.get(position));
                            notifyDataSetChanged();
                        }

                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quitar " + manejador.obtenerArticulo(datos.get(position).getArticulo()).getNombre() + " " +
                        manejador.obtenerArticulo(datos.get(position).getArticulo()).getMarca() + " del Stock?", borrarStockListener, getContext());
            }
        });

        manejador.cerrar();
        return(item);
    }
}
