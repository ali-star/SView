package ir.siriusapps.sview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.R;
import ir.siriusapps.sview.SView;
import ir.siriusapps.sview.view.CornerView;

public class Button extends android.widget.Button implements CornerView {

    private Path basePath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;

    private Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path shadowPath = new Path();
    private float shadowSize = 20;
    private float shadowDy = 10;

    public Button(Context context) {
        super(context);
        init(null);
    }

    public Button(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Button(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SView);
            cornerRadius = typedArray.getDimensionPixelSize(R.styleable.SView_sview_cornerRadius, cornerRadius);
            typedArray.recycle();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            setWillNotDraw(false);
        }

        setBackground(null);

        /*if (getBackground() instanceof ColorDrawable) {
            basePaint.setColor(((ColorDrawable) getBackground()).getColor());
        }*/

        basePaint.setColor(Color.BLACK);
        basePaint.setShadowLayer(shadowSize, 0, shadowDy, Color.parseColor("#3D72DE"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (shadowSize > 0)
            rectF.set(shadowSize, shadowSize - shadowDy, w - shadowSize, h - (shadowSize + shadowDy));
        else
            rectF.set(0, 0, w, h);

        int mCornerRadius = cornerRadius;
        if (mCornerRadius < 0)
            mCornerRadius = h / 2;

        SView.makeRoundedCornersPath(basePath, rectF, mCornerRadius, mCornerRadius,
                cornerRadius, cornerRadius);

        if (cornerRadius != 0)
            setCorerRadius(cornerRadius);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            canvas.drawPath(basePath, basePaint);
        }

        int saveCount = canvas.save();


        if (!isInEditMode()) {
            clipPaint.setXfermode(porterDuffXfermode);
            canvas.drawPath(basePath, clipPaint);
            canvas.restoreToCount(saveCount);
            clipPaint.setXfermode(null);
        }
    }

    /*   Corners   */
    @Override
    public void setCorerRadius(int corerRadius) {
        this.cornerRadius = corerRadius;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (shadowSize > 0)
                setOutlineProvider(null);
            else
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setAlpha(0);
                        int mCornerRadius = cornerRadius;
                        if (mCornerRadius < 0)
                            mCornerRadius = view.getHeight() / 2;
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mCornerRadius);
                    }
                });
        } else
            invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            basePaint.setColor(color);
            invalidate();
        }
    }

    @Override
    public int getCornerRadius() {
        return cornerRadius;
    }
}
