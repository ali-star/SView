package ir.siriusapps.sview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.Utils;

public class BlurBackgroundImageView extends ImageView {

    private RenderScript renderScript;

    private static float BRIGHTNESS = -25f;
    private static float SATURATION = 1.3f;
    float radius = 7f;

    public BlurBackgroundImageView(Context context) {
        super(context);
        init();
    }

    public BlurBackgroundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurBackgroundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        renderScript = RenderScript.create(getContext());

        setScaleType(ScaleType.CENTER_CROP);

        int padding = Utils.dipToPix(28);
        setPadding(padding, padding, padding, padding);
    }

    @Override
    public ScaleType getScaleType() {
        return ScaleType.CENTER_CROP;
    }

    @Override
    public void setImageBitmap(final Bitmap bm) {
        setBlurShadow(new Fun() {
            @Override
            public void call() {
                BlurBackgroundImageView.super.setImageBitmap(bm);
            }
        });
    }

    @Override
    public void setImageResource(final int resId) {
        setBlurShadow(new Fun() {
            @Override
            public void call() {
                BlurBackgroundImageView.super.setImageResource(resId);
            }
        });
    }

    @Override
    public void setImageDrawable(@Nullable final Drawable drawable) {
        setBlurShadow(new Fun() {
            @Override
            public void call() {
                BlurBackgroundImageView.super.setImageDrawable(drawable);
            }
        });
    }

    private void setBlurShadow(final Fun fun) {
        setBackground(null);
        if (getHeight() != 0 || getWidth() != 0) {
            fun.call();
            makeBlurShadow();
        } else {
            final ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    fun.call();
                    makeBlurShadow();
                    return false;
                }
            };
            getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        }
    }

    interface Fun {
        void call();
    }

    private void makeBlurShadow() {
        if (getWidth() == 0 || getHeight() == 0)
            return;
        Bitmap blur = blur(radius);
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1f, 0f, 0f, 0f, BRIGHTNESS,
                0f, 1f, 0f, 0f, BRIGHTNESS,
                0f, 0f, 1f, 0f, BRIGHTNESS,
                0f, 0f, 0f, 1f, 0f});
        colorMatrix.setSaturation(SATURATION);

        setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        BitmapDrawable background = new BitmapDrawable(getResources(), blur);
        setBackground(background);
    }

    private Bitmap blur(float radius) {
        Bitmap src = getBitmapForView(0.25f);
        Allocation input = Allocation.createFromBitmap(renderScript, src);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(src);
        return src;
    }

    private Bitmap getBitmapForView(float downScale) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (getWidth() * downScale),
                (int) ((getHeight()) * downScale),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(downScale, downScale);
        canvas.setMatrix(matrix);
        canvas.translate(0, Utils.dipToPix(6));
        draw(canvas);
        return bitmap;
    }

}
