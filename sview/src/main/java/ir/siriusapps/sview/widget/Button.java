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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.R;
import ir.siriusapps.sview.SView;
import ir.siriusapps.sview.TypefaceManager;
import ir.siriusapps.sview.view.CornerView;

@SuppressLint("AppCompatCustomView")
public class Button extends android.widget.Button implements CornerView {

    private Path basePath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;

    private Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int shadowColor = Color.parseColor("#80000000");
    private float shadowSize = 0;
    private float shadowDy = 0;
    private int strokeWidth = 0;
    private int strokeColor = Color.BLACK;

    private String typefacePath;

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
            strokeColor = typedArray.getColor(R.styleable.SView_sview_strokeColor, strokeColor);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.SView_sview_strokeWidth, strokeWidth);
            typefacePath = typedArray.getString(R.styleable.SView_sview_typeface);
            typedArray.recycle();
        }

        if (typefacePath != null)
            setTypeface(typefacePath);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);

        clipPaint.setXfermode(porterDuffXfermode);

        if (getBackground() instanceof ColorDrawable) {
            basePaint.setColor(((ColorDrawable) getBackground()).getColor());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
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
        }

        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        if (shadowSize > 0) {
            setBackground(null);
            basePaint.setShadowLayer(shadowSize, 0, shadowDy, shadowColor);
            setPadding((int) shadowSize + getPaddingLeft(),
                    (int) (shadowSize - shadowDy) + getPaddingTop(),
                    (int) shadowSize + getPaddingRight(),
                    (int) (shadowSize + shadowDy) + getPaddingBottom());
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (shadowSize > 0)
            rectF.set(
                    shadowSize,
                    (shadowSize - shadowDy) >= 0 ? (shadowSize - shadowDy) : 0,
                    w - shadowSize,
                    h - (shadowSize + shadowDy));
        else
            rectF.set(0, 0, w, h);

        float mCornerRadius = cornerRadius;
        if (mCornerRadius < 0)
            mCornerRadius = rectF.bottom / 2;

        basePath.reset();
        basePath.addRoundRect(rectF, mCornerRadius, mCornerRadius, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(
                SView.measureDimension(desiredWidth, widthMeasureSpec, (int) ((shadowSize * 2))),
                SView.measureDimension(desiredHeight, heightMeasureSpec, (int) ((shadowSize - shadowDy) + shadowSize + shadowDy)));
    }


    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            canvas.drawPath(basePath, basePaint);

            if (!isInEditMode()) {
                int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

                super.draw(canvas);

                Path.FillType fillType = basePath.getFillType();
                basePath.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(basePath, clipPaint);
                canvas.restoreToCount(saveCount);
                basePath.setFillType(fillType);
            }
        } else
            super.draw(canvas);

        if (strokeWidth > 0)
            canvas.drawPath(basePath, strokePaint);
    }

    /*   Corners   */
    @Override
    public void setCorerRadius(int corerRadius) {
        this.cornerRadius = corerRadius;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (shadowSize > 0) {
                setOutlineProvider(null);
                requestLayout();
            } else {
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
            }
        } else
            requestLayout();
    }

    @Override
    public void setBackgroundColor(int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || shadowSize > 0) {
            basePaint.setColor(color);
            invalidate();
        } else
            super.setBackgroundColor(color);
    }

    @Override
    public int getCornerRadius() {
        return cornerRadius;
    }

    /*   Typeface   */
    public void setTypeface(String path) {
        if (path == null)
            return;
        Typeface typeface = TypefaceManager.getInstance().getTypeface(path, getContext());
        setTypeface(typeface);
    }
}
