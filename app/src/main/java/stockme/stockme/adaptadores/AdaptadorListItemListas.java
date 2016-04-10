package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import stockme.stockme.ListaDeArticulos;
import stockme.stockme.Principal;
import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class AdaptadorListItemListas extends ArrayAdapter<Lista> {
    private ListView listas;
    private List<Lista> datos;
    private ImageButton btn_delete;
    private TextView lblNombre;
    private TextView lblSupermercado;
    private TextView lblFecha;
    private TextView lblNProductos;
    private Lista lista;
    private String nuevoNombre;

    public AdaptadorListItemListas(Context context, List<Lista> datos) {
        super(context, R.layout.listitem_lista, datos);
//        this.context = context;
        this.datos = datos;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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

                        //TODO esto debería estar en una transaction pero no se como se hacen
                        List<Articulo> articulos = manejador.obtenerArticulosEnLista(datos.get(position));
                        for(Articulo articulo : articulos){
                            manejador.eliminarArticuloEnLista(new ListaArticulo(articulo.getId(),datos.get(position).getNombre(),0));
                        }

                        if(!manejador.eliminarLista(datos.get(position)))
                            Util.mostrarToast(getContext(), "No se ha podido eliminar la lista");

                        Util.mostrarToast(getContext(), "Lista eliminada");

                        manejador.cerrar();
                        remove(datos.get(position));
                    }
                };
                Util.crearMensajeAlerta("¿Quieres eliminar la lista?", borrarListaListener, v.getContext());


            }
        });

        listas = (ListView)parent.findViewById(R.id.fragment_listas_listview);
        listas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final BDHandler manejador = new BDHandler(getContext());
                final Lista lista = ((Lista) parent.getItemAtPosition(position));

                //Diálogo para cambiar nombre
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Nuevo nombre");

                final EditText input = new EditText(view.getContext());
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nuevoNombre = input.getText().toString();
                        Lista nuevaLista = new Lista(nuevoNombre, lista.getFechaCreacion(), Util.diaMesAnyo.format(new Date()), lista.getSupermercado());

                        manejador.insertarLista(nuevaLista);

                        List<Articulo> articulos = manejador.obtenerArticulosEnLista(lista);
                        for (Articulo articulo : articulos) {
                            int cantidad = manejador.obtenerCantidadArticuloEnLista(articulo.getId(), lista);
                            manejador.insertarArticuloEnLista(new ListaArticulo(articulo.getId(), nuevoNombre, cantidad));
                            manejador.eliminarArticuloEnLista(new ListaArticulo(articulo.getId(), lista.getNombre(), cantidad));
                        }

                        manejador.eliminarLista(lista);
                        Util.mostrarToast(getContext(),"Lista renombrada");
                        remove(lista);
                        add(nuevaLista);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });

        manejador.close();

        return(item);
    }
}
