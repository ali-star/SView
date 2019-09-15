package ir.siriusapps.sview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

import ir.siriusapps.sview.svg.RenderOptions;
import ir.siriusapps.sview.svg.SVG;
import ir.siriusapps.sview.svg.SVGParseException;

import ir.siriusapps.sview.R;
import ir.siriusapps.sview.SView;
import ir.siriusapps.sview.view.CornerView;

public class ImageView extends android.widget.ImageView implements CornerView {

    private Path clipPath = new Path();
    private Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private RectF rectF = new RectF();
    private int cornerRadius;

    private int svgResource;
    private int color;
    private int iconSize;

    public ImageView(Context context) {
        super(context);
        init(null);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SView);
            cornerRadius = typedArray.getDimensionPixelSize(R.styleable.SView_sview_cornerRadius, cornerRadius);
            color = typedArray.getColor(R.styleable.SView_sview_color, color);
            svgResource = typedArray.getResourceId(R.styleable.SView_sview_res, svgResource);
            iconSize = typedArray.getDimensionPixelSize(R.styleable.SView_sview_resSize, iconSize);
            typedArray.recycle();
        }

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

        setDrawable();

        if (cornerRadius != 0)
            setCorerRadius(cornerRadius);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isInEditMode())
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

    /*   SvgDrawable   */
    public void setSvgResource(int svgResource, int color) {
        this.svgResource = svgResource;
        this.color = color;
        setDrawable();
    }

    public void setSvgResource(int svgResource) {
        this.svgResource = svgResource;
        setDrawable();
    }

    public void setColor(int color) {
        this.color = color;
        setDrawable();
    }

    private void setDrawable() {
        if (getWidth() <= 0 || getHeight() <= 0 || svgResource == 0)
            return;
        setImageDrawable(null);
        SVG svg;
        try {
            if (iconSize == 0) {
                iconSize = Math.min(getWidth(), getHeight());
            }
            svg = SVG.getFromResource(getContext(), svgResource);
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate((getWidth() / 2) - (iconSize / 2), (getHeight() / 2) - (iconSize / 2));
            svg.setDocumentWidth(iconSize);
            svg.setDocumentHeight(iconSize);
            RenderOptions renderOpts = RenderOptions.create().css("path { fill: " + String.format("#%06X", (0xFFFFFF & color)) + "; }");
            svg.renderToCanvas(canvas, renderOpts);
            setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }
}
