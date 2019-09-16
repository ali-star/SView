package ir.siriusapps.sview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class ShapeView extends RelativeLayout {

    private Path clipPath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public ShapeView(Context context) {
        super(context);
        init();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("CustomViewStyleable")
    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setWillNotDraw(false);

        clipPath.setFillType(Path.FillType.INVERSE_WINDING);
        clipPaint.setXfermode(porterDuffXfermode);
    }

    protected abstract void initClipPath();

    public Path getClipPath() {
        return clipPath;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initClipPath();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawPath(clipPath, clipPaint);
    }
}
