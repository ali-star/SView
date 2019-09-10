package ir.siriusapps.sview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.SView;
import ir.siriusapps.sview.view.CornerView;

public class ImageView extends android.widget.ImageView implements CornerView {

    private Path clipPath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;

    public ImageView(Context context) {
        super(context);
        init();
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
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
}
