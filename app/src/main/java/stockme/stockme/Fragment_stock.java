package stockme.stockme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemStock;
import stockme.stockme.logica.Stock;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Configuracion;

public class Fragment_stock extends Fragment {
    AdaptadorListItemStock adaptador;
    private OnFragmentInteractionListener mListener;
    private ListView lv_stock;
    private Button bt_añadir;
    private ShowcaseView showcaseView;
    private int counter = 0;

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
        Configuracion.setPreferencia("anterior", "Stock");

        lv_stock = (ListView) view.findViewById(R.id.fragment_stock_listview);
        //recojo las listas existenes
        BDHandler manejador = new BDHandler(view.getContext());
        List<Stock> listaArticulos = manejador.obtenerStocks();
        //las añado al adaptador
        adaptador = new AdaptadorListItemStock(view.getContext(), listaArticulos);
        //asigno el adaptador a la list view
        lv_stock.setAdapter(adaptador);

        bt_añadir = (Button) view.findViewById(R.id.fragment_stock_button);
        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), StockAdd.class);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        manejador.cerrar();

        //TUTORIAL

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!prefs.getBoolean("tutoStock", false)) {

            showcaseView = new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(lv_stock))
                    .setContentText(getResources().getString(R.string.Tuto_stock1))
                    .setStyle(R.style.ShowcaseTheme)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (counter < 3) {
                                switch (counter) {
                                    case 0:
                                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                                        showcaseView.setButtonPosition(params);
                                        showcaseView.setShowcase(new ViewTarget(bt_añadir), true);
                                        showcaseView.setContentText(getResources().getString(R.string.Tuto_stock2));
                                        break;

                                    case 1:
                                        showcaseView.setTarget(Target.NONE);
                                        showcaseView.setContentText(getResources().getString(R.string.Tuto_stock3));
                                        showcaseView.setButtonText(getString(R.string.Aceptar));
                                        break;

                                    case 2:
                                        showcaseView.hide();
                                        break;
                                }
                                counter++;
                            }
                        }
                    })
                    .build();
            showcaseView.setButtonText(getString(R.string.Aceptar));

            prefs.edit().putBoolean("tutoStock", true).apply();
        }

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

    //este método recoje los datos obtenidos al añadir elementos a la lista
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
