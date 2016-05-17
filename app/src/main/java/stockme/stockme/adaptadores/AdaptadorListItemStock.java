package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.StockListaAdd;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.OnSwipeTouchListener;
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

        final BDHandler manejador = new BDHandler(getContext());

        stock = new Stock(datos.get(position).getArticulo(),
                datos.get(position).getCantidad(),
                datos.get(position).getMinimo());

        int cantidad = stock.getCantidad();
        int minimo = stock.getMinimo();
        final boolean pendiente = manejador.estaStockEnListaCompra(stock);

        if(cantidad > minimo)
            item.setBackgroundResource(R.drawable.esquinas_stock_verde);
        else if(pendiente)
            item.setBackgroundResource(R.drawable.esquinas_stock_amarillo);
        else if(cantidad == 0)
            item.setBackgroundResource(R.drawable.esquinas_stock_rojo);
        else
            item.setBackgroundResource(R.drawable.esquinas_stock_naranja);

        ib_borrar = (ImageButton)item.findViewById(R.id.listitem_stock_bt_delete);
        tv_nombre = (TextView)item.findViewById(R.id.listitem_stock_nombre);
        tv_marca = (TextView)item.findViewById(R.id.listitem_stock_marca);
        tv_cantidad = (TextView)item.findViewById(R.id.listitem_stock_cantidad);
        tv_minimo = (TextView)item.findViewById(R.id.listitem_stock_tv_minimo);
        ib_mas = (ImageButton) item.findViewById(R.id.listitem_stock_mas);
        ib_menos = (ImageButton) item.findViewById(R.id.listitem_stock_menos);

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
                if (stock1.getCantidad() < 99) {
                    int cantidad = stock1.getCantidad() + 1;
                    manejador.modificarStockCantidad(stock1, cantidad);
                    datos.get(position).setCantidad(cantidad);
                    manejador.cerrar();
                    notifyDataSetChanged();
                } else {
                    manejador.cerrar();
                }
            }
        });

        ib_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHandler manejador = new BDHandler(getContext());
                final Stock stock1 = manejador.obtenerStock(datos.get(position).getArticulo());
                if (stock1.getCantidad() > 0) {
                    int cantidad = stock1.getCantidad() - 1;
                    int minimo = stock1.getMinimo();
                    if(cantidad == 0){
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
                        DialogInterface.OnClickListener cancelar = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        };
                        String nombre = manejador.obtenerArticulo(datos.get(position).getArticulo()).getNombre();
                        String marca = manejador.obtenerArticulo(datos.get(position).getArticulo()).getMarca();
                        Util.crearMensajeAlerta("¿Conservar " + nombre + " " + marca + " en el Stock?", nombre + " " + marca + " agotado", "Eliminar", "Conservar", borrarStockListener, cancelar, getContext());
                    }
                    else if(cantidad == minimo)
                        Util.mostrarToast(getContext(), manejador.obtenerArticulo(datos.get(position).getArticulo()).getNombre() + " " +
                                manejador.obtenerArticulo(datos.get(position).getArticulo()).getMarca() + " ha alcanzado el mínimo establecido");
                    else if(cantidad < minimo && !pendiente){
                        DialogInterface.OnClickListener añadirStockListaListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Llamar a una actividad (vista) con un spinner con las listas de la compra o posibilidad de una nueva
                                //y que aparezca la cantidad que queremos añadir y el precio y super (como llamar al ArticuloListaAdd pero eligiendo tmbn la lista)
                                //Comprobar que no esté ya en lista (boolean pendiente usado para colear -- fondo amarillo)

                                Intent i = new Intent(getContext(), StockListaAdd.class);
                                i.putExtra("IdArticuloSimple", stock1.getArticulo());
                                getContext().startActivity(i);
                                //getContext().overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                                BDHandler manejador = new BDHandler(getContext());
//                                if (!manejador.eliminarStock(datos.get(position).getArticulo()))
//                                    Util.mostrarToast(getContext(), "No se ha podido quitar el articulo");
//                                else {
//                                    Util.mostrarToast(getContext(), "Articulo quitado del Stock");
//                                    remove(datos.get(position));
//                                    notifyDataSetChanged();
//                                }
//
//                                manejador.cerrar();
                            }
                        };
                        String nombre = manejador.obtenerArticulo(datos.get(position).getArticulo()).getNombre();
                        String marca = manejador.obtenerArticulo(datos.get(position).getArticulo()).getMarca();
                        Util.crearMensajeAlerta("¿Añadir " + nombre + " " + marca + " a una lista de la compra?", nombre + " " + marca + " bajo mínimos", añadirStockListaListener, getContext());
                    }
                    manejador.modificarStockCantidad(stock1, cantidad);
                    datos.get(position).setCantidad(cantidad);
                    manejador.cerrar();
                    notifyDataSetChanged();
                } else {
                    manejador.cerrar();
                }
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
                ib_borrar.performClick();
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
