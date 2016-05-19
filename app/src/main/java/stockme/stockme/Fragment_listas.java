package stockme.stockme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

import stockme.stockme.adaptadores.AdaptadorListItemListas;
import stockme.stockme.logica.Lista;
import stockme.stockme.persistencia.BDHandler;
import stockme.stockme.util.Configuracion;
import stockme.stockme.util.Util;

public class Fragment_listas extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView listas;
    private Button btn_mas;
    private ShowcaseView showcaseView;
    private int counter = 0;


    public Fragment_listas() {
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
        return inflater.inflate(R.layout.fragment_listas, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuracion.setPreferencia("anterior", "Listas");

        view.setVerticalScrollBarEnabled(false);

        listas = (ListView)view.findViewById(R.id.fragment_listas_listview);
        //recojo las listas existenes
        final BDHandler manejador = new BDHandler(view.getContext());
//        List<Lista> listaListas = manejador.obtenerListas();
        List<Lista> listaListas = manejador.obtenerListasOrdenadas(Lista.FECHA_MODIFICACION);
        //las añado al adaptador
        AdaptadorListItemListas adaptador = new AdaptadorListItemListas(view.getContext(), listaListas);
        //asigno el adaptador a la list view
        listas.setAdapter(adaptador);

        btn_mas = (Button)view.findViewById(R.id.fragment_listas_btn_mas);
        btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ListaAdd.class);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        //al click en un elemento de la lista voy a inspeccionar sus artículos
        listas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(parent.getContext(), ListaCompra.class);
                String nombreLista = ((Lista) parent.getItemAtPosition(position)).getNombre();
                Util.mostrarToast(parent.getContext(), nombreLista);
                i.putExtra("NombreLista", nombreLista);
                startActivityForResult(i, 1);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        manejador.cerrar();

        //TUTORIAL

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!prefs.getBoolean("tutoListas", false)) {

            showcaseView = new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(view.findViewById(R.id.fragment_listas_listview)))
                    .setContentText(getResources().getString(R.string.Tuto_lista1))
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
                                        showcaseView.setShowcase(new ViewTarget(btn_mas), true);
                                        showcaseView.setContentText(getResources().getString(R.string.Tuto_lista2));
                                        break;

                                    case 1:
                                        showcaseView.setTarget(Target.NONE);
                                        showcaseView.setContentText(getResources().getString(R.string.Tuto_lista3));
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

            prefs.edit().putBoolean("tutoListas", true).apply();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == Activity.RESULT_OK)) {
            //con esto refrescamos el fragment para actualizar la lista
            Fragment fragmento = new Fragment_listas();
            FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.contenido_principal, fragmento);
            ft.attach(fragmento).commit();
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
    }
}
