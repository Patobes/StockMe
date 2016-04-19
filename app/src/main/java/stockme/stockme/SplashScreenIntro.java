package stockme.stockme;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreenIntro extends AppCompatActivity {
    private ProgressBar prb_carrito;
    private final static int duracion = 3000;
    private final static int tick = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            Fade fade = new Fade();
//            fade.setDuration(500); // Duración en milisegundos
//            getWindow().setExitTransition(fade);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_intro);

        prb_carrito = (ProgressBar) findViewById(R.id.prb_carrito);

        prb_carrito.setMax(duracion / tick);

        prb_carrito.getProgressDrawable().setColorFilter(Color.parseColor("#e5b5b6f9"), PorterDuff.Mode.SRC_IN);


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
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(i,ActivityOptions.makeSceneTransitionAnimation(SplashScreenIntro.this).toBundle());
//                }else
                    startActivity(i);
                finish();
            }
        }.start();
    }
}
