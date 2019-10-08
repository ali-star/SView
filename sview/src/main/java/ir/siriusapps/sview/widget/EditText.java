package ir.siriusapps.sview.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.input.InputManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

import ir.siriusapps.sview.R;
import ir.siriusapps.sview.SView;
import ir.siriusapps.sview.TypefaceManager;
import ir.siriusapps.sview.view.CornerView;

public class EditText extends android.widget.EditText implements CornerView {

    private Path clipPath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;
    private String typefacePath;

    public EditText(Context context) {
        super(context);
        init(null);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SView);
            cornerRadius = typedArray.getDimensionPixelSize(R.styleable.SView_sview_cornerRadius, cornerRadius);
            typefacePath = typedArray.getString(R.styleable.SView_sview_typeface);
            typedArray.recycle();
        }

        if (typefacePath != null)
            setTypeface(typefacePath);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            setWillNotDraw(false);
        }

        clipPath.setFillType(Path.FillType.INVERSE_WINDING);
        clipPaint.setXfermode(porterDuffXfermode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setClipToOutline(true);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (cornerRadius != 0)
            setCorerRadius(cornerRadius);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        SView.drawCorners(canvas, clipPath, clipPaint);
    }

    /*   Corners   */
    @Override
    public void setCorerRadius(int corerRadius) {
        this.cornerRadius = corerRadius;
        SView.setCorerRadius(this, rectF, clipPath, corerRadius);
    }

    @Override
    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setTypeface(String path) {
        if (path == null)
            return;
        Typeface typeface = TypefaceManager.getInstance().getTypeface(path, getContext());
        setTypeface(typeface);
    }
}
