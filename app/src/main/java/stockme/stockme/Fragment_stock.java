package stockme.stockme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemStock;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;

public class Fragment_stock extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView stock;
    private Button btnMas;

    public Fragment_stock() {
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
        return inflater.inflate(R.layout.fragment_stock, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stock = (ListView)view.findViewById(R.id.fragment_stock_listview);
        //recojo las listas existenes
        BDHandler manejador = new BDHandler(view.getContext());
        List<Stock> listaArticulos = manejador.obtenerStocks();
        //las añado al adaptador
        AdaptadorListItemStock adaptador = new AdaptadorListItemStock(view.getContext(), listaArticulos);
        //asigno el adaptador a la list view
        stock.setAdapter(adaptador);

        //Click sobre el boton +
        btnMas = (Button)view.findViewById(R.id.fragment_stock_button);
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StockAdd.class);
                startActivityForResult(i, 1);
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
    }

    //este método recoje los datos obtenidos al añadir elementos a la lista
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
