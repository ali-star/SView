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

public class ShapeView extends RelativeLayout {

    private Path clipPath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public ShapeView(Context context) {
        super(context);
        init(null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(R.styleable.SView);
            typedArray.recycle();
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);

        clipPath.setFillType(Path.FillType.INVERSE_WINDING);
        clipPaint.setXfermode(porterDuffXfermode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setClipToOutline(true);
    }

    protected Path getClipPath(int width, int height) {
        float arcHeight = Utils.dipToPix(16);
        clipPath.moveTo(0, 0);
        clipPath.lineTo(0, height - arcHeight);
        clipPath.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
        clipPath.lineTo(width, 0);
        clipPath.close();
        return clipPath;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawPath(getClipPath(getWidth(), getHeight()), clipPaint);
    }
}
