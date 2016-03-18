package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import stockme.stockme.ListaDeArticulos;
import stockme.stockme.Principal;
import stockme.stockme.R;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

/**
 * Created by JuanMiguel on 08/03/2016.
 */
public class AdaptadorListItemListas extends ArrayAdapter<Lista> {
    private List<Lista> datos;
    private ImageButton btn_delete;
    private TextView lblNombre;
    private TextView lblSupermercado;
    private TextView lblFecha;
    private TextView lblNProductos;
    private Lista lista;
//    private Context context;

    public AdaptadorListItemListas(Context context, List<Lista> datos) {
        super(context, R.layout.listitem_lista, datos);
//        this.context = context;
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_lista, null);

        //se crea un elemento Lista que contiene los datos de la fila
        lista = new Lista();
        lista.setNombre(datos.get(position).getNombre());
        lista.setFechaCreacion(datos.get(position).getFechaCreacion());
        lista.setFechaModificacion(datos.get(position).getFechaModificacion());

        BDHandler manejador = new BDHandler(getContext());

        lblNombre = (TextView)item.findViewById(R.id.listitem_lista_nombre);
        lblNombre.setText(lista.getNombre());

        lblSupermercado = (TextView)item.findViewById(R.id.listitem_lista_supermercado);
        lblSupermercado.setText("FALTA SUPERM");
        //TODO: Falta modificar la bd para añadir el supermercado?

        lblFecha = (TextView)item.findViewById(R.id.listitem_lista_fecha);
        lblFecha.setText(lista.getFechaModificacion());

        lblNProductos = (TextView)item.findViewById(R.id.listitem_lista_nproductos);
        lblNProductos.setText(String.valueOf(manejador.numArticulosEnLista(lista.getNombre())));

        btn_delete = (ImageButton)item.findViewById(R.id.listitem_lista_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener borrarListaListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BDHandler manejador = new BDHandler(getContext());
                        if(!manejador.eliminarLista(new Lista(lblNombre.getText().toString(),"","")))
                            Util.mostrarToast(getContext(), "No se ha podido eliminar la lista");
                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quieres eliminar la lista?", borrarListaListener, v.getContext());
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case DialogInterface.BUTTON_POSITIVE:
//
//                                BDHandler manejador = new BDHandler(getContext());
//
//                                if(!manejador.eliminarLista(new Lista(lblNombre.getText().toString(),"","")))
//                                    Util.mostrarToast(getContext(), "No se ha podido eliminar la lista");
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//
//                                break;
//                        }
//                    }
//                };
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setMessage("¿Quieres eliminar la lista?").setPositiveButton("Si", dialogClickListener)
//                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        manejador.close();
        //TODO: esto debería ir en Fragment_lista pero no lo he conseguido >.<, así funciona
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ListaDeArticulos.class);
                String nombreLista = lista.getNombre();
                Util.mostrarToast(v.getContext(), nombreLista);
                i.putExtra("NombreLista", nombreLista);
                getContext().startActivity(i);
            }
        });
        return(item);
    }
}
