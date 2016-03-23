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
        lista.setSupermercado(datos.get(position).getSupermercado());

        BDHandler manejador = new BDHandler(getContext());

        lblNombre = (TextView)item.findViewById(R.id.listitem_lista_nombre);
        lblNombre.setText(lista.getNombre());

        lblSupermercado = (TextView)item.findViewById(R.id.listitem_lista_supermercado);
        lblSupermercado.setText(lista.getSupermercado());

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
                        if(!manejador.eliminarLista(new Lista(lblNombre.getText().toString(),"","","")))
                            Util.mostrarToast(getContext(), "No se ha podido eliminar la lista");
                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quieres eliminar la lista?", borrarListaListener, v.getContext());

            }
        });

        manejador.close();

        return(item);
    }
}
