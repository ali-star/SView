package ir.siriusapps.sview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import ir.siriusapps.sview.R;

public class GradientView extends View {

    private int startColor = Color.parseColor("#90000000");
    private int endColor = Color.TRANSPARENT;
    private Shader shader;
    private Paint paint = new Paint();
    private int start = 0, end = 2;

    public GradientView(Context context) {
        super(context);
        init();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientView);
        startColor = typedArray.getColor(R.styleable.GradientView_gradientStartColor, startColor);
        endColor = typedArray.getColor(R.styleable.GradientView_gradientEndColor, endColor);
        start = typedArray.getInt(R.styleable.GradientView_gradientStart, start);
        end = typedArray.getInt(R.styleable.GradientView_gradientEnd, end);
        typedArray.recycle();
    }

    private void init () {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int[] startXY = getXY(start);
                int[] endXY = getXY(end);
                shader = new LinearGradient(startXY[0], startXY[1], endXY[0], endXY[1], startColor, endColor, Shader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private int[] getXY (int position) {
        int x = 0;
        int y = 0;
        switch (position){
            case 0:
                x = getWidth() / 2;
                y = 0;
                break;
            case 1:
                x = getWidth();
                y = getHeight() / 2;
                break;
            case 2:
                x = getWidth() / 2;
                y = getHeight();
                break;
            case 3:
                x = 0;
                y = getHeight() / 2;
                break;
            case 4:
                x = 0;
                y = 0;
                break;
            case 5:
                x = getWidth();
                y = 0;
                break;
            case 6:
                x = 0;
                y = getHeight();
                break;
            case 7:
                x = getWidth();
                y = getHeight();
                break;
        }
        return new int[] {x, y};
    }
}
