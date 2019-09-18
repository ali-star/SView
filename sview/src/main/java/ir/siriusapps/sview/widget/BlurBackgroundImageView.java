package ir.siriusapps.sview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
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

    private float brightness = -25f;
    private float saturation = 1.0f;
    private float blurRadius = 10f;
    private int blurAlpha = 255;
    private float blurTopOffset = Utils.dipToPix(6);

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

        int padding = Utils.dipToPix(74);
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
        Bitmap blur = blur(blurRadius, blurAlpha);
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f});
        colorMatrix.setSaturation(saturation);

        setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        BitmapDrawable background = new BitmapDrawable(getResources(), blur);
        setBackground(background);
    }

    private Bitmap blur(float radius, int blurAlpha) {
        Bitmap src = getBitmapForView(0.25f, blurAlpha);
        Allocation input = Allocation.createFromBitmap(renderScript, src);
        Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(src);
        return src;
    }

    private Bitmap getBitmapForView(float downScale, int blurAlpha) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (getWidth() * downScale),
                (int) ((getHeight()) * downScale),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.preScale(downScale, downScale);
        canvas.setMatrix(matrix);
        canvas.translate(0, blurTopOffset);
        draw(canvas);
        int color = (blurAlpha & 0xFF) << 24;
        canvas.drawColor(color, PorterDuff.Mode.DST_IN);
        return bitmap;
    }

    public void setBlurRadius(float blurRadius, boolean updateDrawable) {
        this.blurRadius = blurRadius;
        if (updateDrawable && getDrawable() != null)
            setBlurShadow(new Fun() {
                @Override
                public void call() {
                    setImageDrawable(getDrawable());
                }
            });

    }

    public void setBlurRadius(float blurRadius) {
        setBlurRadius(blurRadius, true);
    }

    public void setBrightness(float brightness, boolean updateDrawable) {
        this.brightness = brightness;
        if (updateDrawable && getDrawable() != null)
            setBlurShadow(new Fun() {
                @Override
                public void call() {
                    setImageDrawable(getDrawable());
                }
            });
    }

    public void setBrightness(float brightness) {
        setBrightness(brightness, true);
    }

    public void setSaturation(float saturation, boolean updateDrawable) {
        this.saturation = saturation;
        if (updateDrawable && getDrawable() != null)
            setBlurShadow(new Fun() {
                @Override
                public void call() {
                    setImageDrawable(getDrawable());
                }
            });
    }

    public void setSaturation(float saturation) {
        setSaturation(saturation, true);
    }

    public void setBlurAlpha(int blurAlpha, boolean updateDrawable) {
        this.blurAlpha = blurAlpha;
        if (updateDrawable && getDrawable() != null)
            setBlurShadow(new Fun() {
                @Override
                public void call() {
                    setImageDrawable(getDrawable());
                }
            });
    }

    public void setBlurAlpha(int blurAlpha) {
        setBlurAlpha(blurAlpha, true);
    }

    public void setBlurTopOffset(float blurTopOffset, boolean updateDrawable) {
        this.blurTopOffset = blurTopOffset;
        if (updateDrawable && getDrawable() != null)
            setBlurShadow(new Fun() {
                @Override
                public void call() {
                    setImageDrawable(getDrawable());
                }
            });
    }

    public void setBlurTopOffset(float blurTopOffset) {
        setBlurTopOffset(blurTopOffset, true);
    }

    public void setPadding(int padding) {
        setPadding(padding, padding, padding, padding);
    }
}
