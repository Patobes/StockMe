package stockme.stockme;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashScreenIntro extends AppCompatActivity {
    private ProgressBar prb_carrito;
    private final static int duracion = 3000;
    private final static int tick = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_intro);

        prb_carrito = (ProgressBar)findViewById(R.id.prb_carrito);

        prb_carrito.setMax(duracion/tick);

        //TODO: Elegir un *** color decente xD
        prb_carrito.getProgressDrawable().setColorFilter(Color.parseColor("#e51cdb26"), android.graphics.PorterDuff.Mode.SRC_IN);

        empezarProgreso();
    }

    private void empezarProgreso() {
        new CountDownTimer(duracion, tick){

            @Override
            public void onTick(long millisUntilFinished) {
                prb_carrito.setProgress((int)((duracion - millisUntilFinished)/tick) + 1);
            }

            @Override
            public void onFinish() {
                prb_carrito.setProgress(prb_carrito.getProgress() + 1);
                Intent i = new Intent(SplashScreenIntro.this, Principal.class);
                startActivity(i);
                finish();
            }
        }.start();
    }
}
