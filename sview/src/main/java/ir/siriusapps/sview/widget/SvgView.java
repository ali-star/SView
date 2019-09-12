package ir.siriusapps.sview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import com.caverock.androidsvg.RenderOptions;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import ir.siriusapps.sview.R;

public class SvgView extends View {

    private int svgResource;
    private int color;

    public SvgView(Context context) {
        super(context);
        init(null);
    }

    public SvgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SvgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SView);
            color = typedArray.getColor(R.styleable.SView_sview_color, color);
            typedArray.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setDrawable();
    }

    private void setDrawable() {
        setBackground(null);
        if (getWidth() <= 0 || getHeight() <= 0 || svgResource == 0)
            return;
        SVG svg;
        try {
            svg = SVG.getFromResource(getContext(), svgResource);
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            svg.setDocumentWidth(getWidth());
            svg.setDocumentHeight(getHeight());
            RenderOptions renderOpts = RenderOptions.create().css("path { fill: " + String.format("#%06X", (0xFFFFFF & color)) + "; }");
            svg.renderToCanvas(canvas, renderOpts);
            setBackground(new BitmapDrawable(getResources(), bitmap));
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }

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
}
