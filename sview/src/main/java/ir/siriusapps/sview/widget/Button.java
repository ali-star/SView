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
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.R;
import ir.siriusapps.sview.view.CornerView;

@SuppressLint("AppCompatCustomView")
public class Button extends android.widget.Button implements CornerView {

    private Path basePath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;

    private Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path shadowPath = new Path();
    private int shadowColor = Color.parseColor("#80000000");
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
            shadowColor = typedArray.getColor(R.styleable.SView_sview_shadowColor, shadowColor);
            shadowSize = typedArray.getDimensionPixelSize(R.styleable.SView_sview_shadowSize, (int) shadowSize);
            shadowDy = typedArray.getDimensionPixelSize(R.styleable.SView_sview_shadowDy, (int) shadowDy);
            typedArray.recycle();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            setWillNotDraw(false);
        }

        clipPaint.setXfermode(porterDuffXfermode);


        if (getBackground() instanceof ColorDrawable) {
            basePaint.setColor(((ColorDrawable) getBackground()).getColor());
        }

        setBackground(null);

        basePaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (shadowSize > 0)
            rectF.set(shadowSize, (shadowSize - shadowDy) >= 0 ? (shadowSize - shadowDy) : 0, w - shadowSize, h - shadowSize - shadowDy);
        else
            rectF.set(0, 0, w, h);

        float mCornerRadius = cornerRadius;
        if (mCornerRadius < 0)
            mCornerRadius = rectF.bottom / 2;

        basePath.addRoundRect(rectF, mCornerRadius, mCornerRadius, Path.Direction.CW);

        setPadding((int) shadowSize, (int) (shadowSize - shadowDy), (int) shadowSize, (int) (shadowSize + shadowDy));
    }


    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            canvas.drawPath(basePath, basePaint);
        }

        if (!isInEditMode()) {

            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            super.draw(canvas);

            Path.FillType fillType = basePath.getFillType();
            basePath.setFillType(Path.FillType.INVERSE_WINDING);
            canvas.drawPath(basePath, clipPaint);
            canvas.restoreToCount(saveCount);
            basePath.setFillType(fillType);
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
