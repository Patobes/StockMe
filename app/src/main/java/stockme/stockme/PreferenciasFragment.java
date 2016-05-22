package stockme.stockme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;

import java.util.Locale;

import stockme.stockme.util.Util;


public class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.preferencias);

        Preference tutorial = findPreference("activar_tutoriales");
        tutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();

                sharedPreferences.edit().putBoolean("tutoListas", false).apply();
                sharedPreferences.edit().putBoolean("tutoStock", false).apply();
                sharedPreferences.edit().putBoolean("tutoArticulo", false).apply();

                Util.mostrarToast(getActivity().getApplicationContext(), getResources().getString(R.string.Tutoriales_reset));

                return true;
            }
        });

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
