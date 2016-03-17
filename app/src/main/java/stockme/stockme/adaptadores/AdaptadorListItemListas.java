package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;

/**
 * Created by JuanMiguel on 08/03/2016.
 */
public class AdaptadorListItemListas extends ArrayAdapter<Lista> {
    private List<Lista> datos;
    private ImageButton btn_delete;

    public AdaptadorListItemListas(Context context, List<Lista> datos) {
        super(context, R.layout.listitem_lista, datos);
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_lista, null);

        final TextView lblNombre = (TextView)item.findViewById(R.id.listitem_lista_nombre);
        lblNombre.setText(datos.get(position).getNombre());

        TextView lblSupermercado = (TextView)item.findViewById(R.id.listitem_lista_supermercado);
        lblSupermercado.setText("Supermercado");

        TextView lblFecha = (TextView)item.findViewById(R.id.listitem_lista_fecha);
        lblFecha.setText("fecha");

        TextView lblNProductos = (TextView)item.findViewById(R.id.listitem_lista_nproductos);
        lblNProductos.setText("numProductos");

        btn_delete = (ImageButton)item.findViewById(R.id.listitem_lista_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                BDHandler manejador = new BDHandler(getContext());

                                manejador.eliminarLista(new Lista(lblNombre.getText().toString(),"",""));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("¿Quieres eliminar la lista?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        return(item);
    }
}
