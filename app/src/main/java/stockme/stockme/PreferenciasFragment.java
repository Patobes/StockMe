package stockme.stockme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;


public class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addPreferencesFromResource(R.xml.preferencias);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("idioma")) {
            //TODO: aquí habrá que hacer que se cambie el locale para que las opciones salgan en inglés
            //sin tener que reiniciar
        }
    }
}
