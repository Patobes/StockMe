package stockme.stockme.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;

/**
 * Created by Paris on 17/03/2016.
 */
public class AdaptadorListItemStock extends ArrayAdapter<Stock>{
    private List<Stock> datos;
    private TextView lblNombre;
    private TextView lblMarca;
    private TextView lblCantidad;
    private TextView lblMinimo;
    private Stock stock;

    public AdaptadorListItemStock(Context context, List<Stock> datos) {
        super(context, R.layout.listitem_stock, datos);
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_stock, null);

        stock = new Stock();
        stock.setArticulo(datos.get(position).getArticulo());
        stock.setCanitdad(datos.get(position).getCanitdad());
        stock.setMinimo(datos.get(position).getMinimo());

        BDHandler manejador = new BDHandler(getContext());

        //Componentes de listitem_stock (de momento vacío)
        lblNombre = (TextView)item.findViewById(R.id.listitem_stock_nombre);
        lblNombre.setText(manejador.obtenerArticulo(stock.getArticulo()).getNombre());

        lblCantidad = (TextView)item.findViewById(R.id.listitem_stock_cantidad);
        lblCantidad.setText(String.valueOf(stock.getCanitdad()));

        lblMarca = (TextView)item.findViewById(R.id.listitem_stock_marca);
        lblMarca.setText(manejador.obtenerArticulo(stock.getArticulo()).getMarca());
        //repetir esto con todos los lbl
        //añadir boton para añadir stock y darle funcionalidad

        return(item);
    }
}
