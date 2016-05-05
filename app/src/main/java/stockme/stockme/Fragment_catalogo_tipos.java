package stockme.stockme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.adaptadores.AdaptadorGridItemCatalogo;
import stockme.stockme.adaptadores.AdaptadorGridItemCatalogoTipos;
import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;


public class Fragment_catalogo_tipos extends Fragment {
    private OnFragmentInteractionListener mListener;
    private GridView articulos;
    List<Articulo> listaArticulos;

    private String querySeacrh;

    public void setQuerySearch(String querySeacrh){
        this.querySeacrh = querySeacrh;
    }

    public Fragment_catalogo_tipos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogo_tipos, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaArticulos = new ArrayList<Articulo>();
        articulos = (GridView) view.findViewById(R.id.gridView_catalogo_articulos_tipos);

        final BDHandler manejador = new BDHandler(view.getContext());

        String[] tipos = getResources().getStringArray(R.array.tipos_array);

        for(String tipo : tipos){
            if(querySeacrh != null){
                listaArticulos.addAll(manejador.obtenerArticulosPorTipoYQuerySearch(tipo, querySeacrh));
            }else
                listaArticulos.addAll(manejador.obtenerArticulosPorTipo(tipo));
        }



        final AdaptadorGridItemCatalogoTipos adaptador = new AdaptadorGridItemCatalogoTipos(view.getContext(), listaArticulos);
        articulos.setAdapter(adaptador);

        articulos = (GridView) view.findViewById(R.id.gridView_catalogo_articulos_tipos);

        articulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Articulo articulo = (Articulo) parent.getItemAtPosition(position);
                mListener.onArticuloSeleccionado(articulo);

            }
        });

        manejador.cerrar();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == Activity.RESULT_OK)){
            //TODO actualizar al volver
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onArticuloSeleccionado(Articulo articulo);
    }

}
