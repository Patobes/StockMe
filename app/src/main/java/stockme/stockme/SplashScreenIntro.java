package stockme.stockme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;

import java.util.Locale;

public class SplashScreenIntro extends AppCompatActivity {
    private final static int duracion = 1400;
    private final static int tick = 200;
    private ProgressBar prb_carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_intro);

        prb_carrito = (ProgressBar) findViewById(R.id.prb_carrito);

        prb_carrito.setMax(duracion / tick);

        prb_carrito.getProgressDrawable().setColorFilter(getResources().getColor(R.color.carga), PorterDuff.Mode.SRC_IN);


        empezarProgreso();
    }

    private void empezarProgreso() {
        new CountDownTimer(duracion, tick) {

            @Override
            public void onTick(long millisUntilFinished) {
                prb_carrito.setProgress((int) ((duracion - millisUntilFinished) / tick) + 1);
            }

            @Override
            public void onFinish() {
                prb_carrito.setProgress(prb_carrito.getProgress() + 1);
                Intent i = new Intent(SplashScreenIntro.this, Principal.class);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(prefs == null) {
                    Locale myLocale = new Locale("es");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }else{
                    String lang = prefs.getString("idioma", "es");
                    Locale myLocale;
                    if(lang != null)
                        myLocale = new Locale(lang);
                    else
                        myLocale = new Locale("es");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }

                startActivity(i);
                finish();
            }
        }.start();
    }
}
