package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OnSwipeTouchListener;
import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;

/**
 * Created by Paris on 17/03/2016.
 */
public class AdaptadorListItemStock extends ArrayAdapter<Stock>{
    private List<Stock> datos;
    private ImageButton ib_borrar;
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

    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View item = inflater.inflate(R.layout.listitem_stock, null);

        stock = new Stock(datos.get(position).getArticulo(),
                datos.get(position).getCantidad(),
                datos.get(position).getMinimo());

        ib_borrar = (ImageButton)item.findViewById(R.id.listitem_stock_bt_delete);
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

        ib_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public boolean onDoubleClick(){
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
                return true;
            }
            @Override
            public void onLongClick(){
                final BDHandler manejador = new BDHandler(getContext());
                final Stock stock1 = datos.get(position);

                //Diálogo para cambiar nombre
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Nuevo mínimo");

                View vista_minimo = LayoutInflater.from(getContext()).inflate(R.layout.dialogo_cambiar_minimo, null);
                builder.setView(vista_minimo);

                final TextView tv_actual = (TextView)vista_minimo.findViewById(R.id.dialogo_cambiar_minimo_tv_actual);
                final EditText input = (EditText)vista_minimo.findViewById(R.id.dialogo_cambiar_minimo_et_minimo);

                tv_actual.setText("Actual: " + String.valueOf(datos.get(position).getMinimo()) + " unidades");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String aux = input.getText().toString();
                        int minimo = 0;
                        if(!aux.isEmpty()){
                            minimo = Integer.parseInt(aux);
                        }
                        manejador.modificarStockMinimo(stock1, minimo);
                        datos.get(position).setMinimo(minimo);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog ad = builder.show();
                if(input.requestFocus())
                    ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                manejador.cerrar();
            }
        });

        manejador.cerrar();
        return(item);
    }
}
