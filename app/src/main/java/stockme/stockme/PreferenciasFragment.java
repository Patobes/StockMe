package stockme.stockme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;

import java.util.Locale;


public class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*ListPreference tutoriales = (ListPreference) findPreference("activar_tutoriales");
        ListPreference orden = (ListPreference) findPreference("orden_listas");
        ListPreference idioma = (ListPreference) findPreference("idioma");

        tutoriales.setValueIndex(1);
        orden.setValueIndex(0);
        idioma.setValueIndex(0);*/

        addPreferencesFromResource(R.xml.preferencias);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("idioma")) {
            String lang = sharedPreferences.getString(key, "es");
            setLocale(lang);
        }
        if (key.equals("activar_tutoriales")) {
            if (sharedPreferences.getString(key, "no").equals("si")) {
                sharedPreferences.edit().putBoolean("tutoListas", false).apply();
                sharedPreferences.edit().putBoolean("tutoStock", false).apply();
                sharedPreferences.edit().putBoolean("tutoArticulo", false).apply();
                sharedPreferences.edit().putString(key, "no");
            }

        }
    }

    public void setLocale(String lang) {

        Activity activity = getActivity();

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(activity.getApplicationContext(), Preferencias.class);
        startActivity(refresh);
        activity.finish();
    }
}
