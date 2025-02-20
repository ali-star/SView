package ir.siriusapps.sviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import ir.siriusapps.sview.widget.Button;
import ir.siriusapps.sview.widget.EditText;
import ir.siriusapps.sview.widget.ImageView;
import ir.siriusapps.sview.widget.Loading;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ImageView imageView = findViewById(R.id.imageView2);
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.icon);
        imageView.setImageDrawable(svg.createPictureDrawable());*/

        /*final ImageView svgView = findViewById(R.id.imageView2);
        svgView.setSvgResource(R.raw.menu);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svgView.setSvgResource(R.raw.icon, Color.parseColor("#999999"));
            }
        }, 1500);*/

        final Loading loading = findViewById(R.id.loadingView);

        final Handler handler = new Handler();

        loading.setProgress(70);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setLoading();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setUserRotationAnimationWithProgress(false);
                        loading.setProgress((float) Math.floor(Math.random() * 100));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loading.setUserRotationAnimationWithProgress(true);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.setUserRotationAnimationWithProgress(false);
                                        loading.setProgress((float) Math.floor(Math.random() * 100));
                                        handler.postDelayed(this, 3000);
                                    }
                                }, 3000);
                            }
                        }, 3000);
                    }
                }, 3000);
            }
        }, 6000);


    }
}
