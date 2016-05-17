package stockme.stockme.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import stockme.stockme.R;
import stockme.stockme.logica.Articulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Util;

public class AdaptadorGridItemCatalogo extends BaseAdapter {
    private Context context;
    private List<Articulo> datos;
    private ImageButton borrar;
    boolean porTipo;
    private GridView articulos;
    View vistaModificar;

    public AdaptadorGridItemCatalogo(Context context, List<Articulo> articulos, boolean porTipo) {
        this.context = context;
        this.datos = articulos;
        this.porTipo = porTipo;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Articulo getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.griditem_catalogo, viewGroup, false);
        }

        TextView nombre = (TextView) view.findViewById(R.id.catalogo_art_nombre);
        nombre.setText(datos.get(position).getNombre());

        TextView marca = (TextView) view.findViewById(R.id.catalogo_art_marca);
        marca.setText(datos.get(position).getMarca());

        String tipo = datos.get(position).getTipo();

        TextView tvtipo = (TextView) view.findViewById(R.id.catalogo_art_tipo);
        tvtipo.setText(tipo);

        borrar = (ImageButton) view.findViewById(R.id.catalogo_borrar);

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener borrarArticuloListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BDHandler manejador = new BDHandler(v.getContext());

                        if (!manejador.eliminarArticulo(datos.get(position)))
                            Util.mostrarToast(v.getContext(), "No se ha podido eliminar el artículo");
                        else {
                            Util.mostrarToast(v.getContext(), "Artículo eliminado");
                            datos.remove(position);
                            notifyDataSetChanged();

                        }

                        manejador.cerrar();
                    }
                };
                Util.crearMensajeAlerta("¿Quieres eliminar el artículo?", borrarArticuloListener, v.getContext());
            }
        });

        if(porTipo){
            if (tipo.equals(view.getContext().getResources().getString(R.string.Congelados))) {
                view.setBackgroundResource(R.drawable.esquinas_congelados);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Dulces))) {
                view.setBackgroundResource(R.drawable.esquinas_dulces);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Bebidas))) {
                view.setBackgroundResource(R.drawable.esquinas_bebidas);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Frutas))) {
                view.setBackgroundResource(R.drawable.esquinas_frutas);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Conservas))) {
                view.setBackgroundResource(R.drawable.esquinas_conservas);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Lacteos))) {
                view.setBackgroundResource(R.drawable.esquinas_lacteos);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Panadería))) {
                view.setBackgroundResource(R.drawable.esquinas_panaderia);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Drogeria))) {
                view.setBackgroundResource(R.drawable.esquinas_drogeria);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Salsas_especias))) {
                view.setBackgroundResource(R.drawable.esquinas_salsas);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Verduras))) {
                view.setBackgroundResource(R.drawable.esquinas_verduras);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Farmacia))) {
                view.setBackgroundResource(R.drawable.esquinas_farmacia);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Carniceria))) {
                view.setBackgroundResource(R.drawable.esquinas_carniceria);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Legumbres))) {
                view.setBackgroundResource(R.drawable.esquinas_legumbres);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Mascotas))) {
                view.setBackgroundResource(R.drawable.esquinas_mascotas);
            } else if (tipo.equals(view.getContext().getResources().getString(R.string.Pescaderia))) {
                view.setBackgroundResource(R.drawable.esquinas_pescaderia);
            }else{
                view.setBackgroundResource(R.drawable.esquinas);
            }

            articulos = (GridView) viewGroup.findViewById(R.id.gridView_catalogo_articulos_tipos);
        }else{
            articulos = (GridView) viewGroup.findViewById(R.id.gridView_catalogo_articulos);
        }

        articulos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int pos, long id) {
                final BDHandler manejador = new BDHandler(view.getContext());
                final Articulo articulo = ((Articulo) parent.getItemAtPosition(pos));

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Editar artículo");
                vistaModificar = LayoutInflater.from(view.getContext()).inflate(R.layout.dialogo_modificar_articulo, parent,false);
                builder.setView(vistaModificar);

                final EditText nombre = (EditText) vistaModificar.findViewById(R.id.dialogo_modificar_articulo_et_nombre);
                final EditText marca = (EditText) vistaModificar.findViewById(R.id.dialogo_modificar_articulo_et_marca);
                final Spinner tipo = (Spinner) vistaModificar.findViewById(R.id.dialogo_modificar_articulo_sp_tipo);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.tipos_array,
                        android.R.layout.simple_spinner_item);
                tipo.setAdapter(adapter);

                nombre.setText(String.valueOf(datos.get(pos).getNombre()));
                marca.setText(String.valueOf(datos.get(pos).getMarca()));

                for (int i = 0; i < tipo.getCount(); i++){
                    String ntipo = (String)tipo.getItemAtPosition(i);
                    if (ntipo.compareTo(datos.get(pos).getTipo()) == 0)
                        tipo.setSelection(i);
                }

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nNombre = nombre.getText().toString();
                        String nMarca = marca.getText().toString();
                        String nTipo = tipo.getSelectedItem().toString();

                        if(nNombre.isEmpty()){
                            Util.mostrarToast(view.getContext(),"Nombre vacío");
                        }else{
                            if(!manejador.estaArticulo(nNombre,nMarca)){
                                manejador.modificarArticuloNombre(articulo, nNombre);
                                manejador.modificarArticuloMarca(articulo, nMarca);
                                manejador.modificarArticuloTipo(articulo, nTipo);
                            }else{
                                Util.mostrarToast(view.getContext(),"Ya existe ");
                            }
                        }
                        datos.get(pos).setNombre(nNombre);
                        datos.get(pos).setMarca(nMarca);
                        datos.get(pos).setTipo(nTipo);
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

                manejador.cerrar();

                return true;
            }
        });

        return view;
    }

}