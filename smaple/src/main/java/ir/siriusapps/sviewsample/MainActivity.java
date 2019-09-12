package ir.siriusapps.sviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ir.siriusapps.sview.widget.Button;
import ir.siriusapps.sview.widget.SvgView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ImageView imageView = findViewById(R.id.imageView2);
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.icon);
        imageView.setImageDrawable(svg.createPictureDrawable());*/

        final SvgView svgView = findViewById(R.id.imageView2);
        svgView.setSvgResource(R.raw.android);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svgView.setSvgResource(R.raw.icon, Color.parseColor("#999999"));
            }
        }, 3000);

        final Button button = findViewById(R.id.view);
        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setCorerRadius(2);
            }
        });


    }
}
