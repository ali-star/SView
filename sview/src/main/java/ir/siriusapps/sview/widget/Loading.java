package ir.siriusapps.sview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;

import ir.siriusapps.sview.R;
import ir.siriusapps.sview.Utils;

public class Loading extends View {

    private int progressColor = Color.parseColor("#3D72DE");
    private int backgroundStrokeColor = Color.parseColor("#1AB9C0CB");
    private float backgroundStrokeWidth = Utils.dipToPix(2);
    private float progressStrokeWidth = Utils.dipToPix(4);
    private Mode mode = Mode.LOADING;
    private float progress = 25;

    private Paint backgroundStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint progressStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    private float rotationAngle = 0;
    private boolean userRotationAnimation = false;
    private ValueAnimator rotateAnimator, progressAnimator;
    private boolean isAttached;

    public Loading(Context context) {
        super(context);
        init(null);
    }

    public Loading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Loading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Loading);
            progressColor = typedArray.getColor(R.styleable.Loading_lv_progressColor, progressColor);
            backgroundStrokeColor = typedArray.getColor(R.styleable.Loading_lv_backgroundStrokeColor, backgroundStrokeColor);
            backgroundStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.Loading_lv_backgroundStrokeWidth, (int) backgroundStrokeWidth);
            progressStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.Loading_lv_progressStrokeWidth, (int) progressStrokeWidth);
            mode = Mode.getByValue(typedArray.getInt(R.styleable.Loading_lv_mode, 0));
            typedArray.recycle();
        }

        progress = mode == Mode.LOADING ? 25 : 0;

        backgroundStrokePaint.setStyle(Paint.Style.STROKE);
        backgroundStrokePaint.setStrokeWidth(backgroundStrokeWidth);
        backgroundStrokePaint.setColor(backgroundStrokeColor);
        backgroundStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        progressStrokePaint.setStyle(Paint.Style.STROKE);
        progressStrokePaint.setStrokeWidth(progressStrokeWidth);
        progressStrokePaint.setColor(progressColor);
        progressStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotationAngle = (float) animation.getAnimatedValue();
                setRotation(rotationAngle);
            }
        });
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setDuration(500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float min = Math.min(w, h);
        float highStroke = (progressStrokeWidth > backgroundStrokeWidth) ? progressStrokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float angle = 360 * progress / 100;
        canvas.drawOval(rectF, backgroundStrokePaint);
        canvas.drawArc(rectF, -90, angle, false, progressStrokePaint);
    }

    public void setProgress(final float progress) {
        if (progressAnimator == null) {
            progressAnimator = ValueAnimator.ofFloat(this.progress, progress);
            progressAnimator.setInterpolator(new EasingInterpolator(Ease.CUBIC_OUT));
            progressAnimator.setDuration(500);
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Loading.this.progress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        } else {
            progressAnimator.setFloatValues(this.progress, progress);
        }
        progressAnimator.start();
    }

    public void rotationAnimation(boolean rotationAnimation) {
        this.userRotationAnimation = rotationAnimation;
        if (rotationAnimation && isAttached && rotateAnimator != null)
            rotateAnimator.start();
        else if (rotateAnimator != null)
            rotateAnimator.cancel();
    }

    public void setLoading() {
        setProgress(25);
        mode = Mode.LOADING;
        rotationAnimation(true);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && userRotationAnimation && rotateAnimator != null)
            rotateAnimator.start();
        else if (rotateAnimator != null)
            rotateAnimator.cancel();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        if (userRotationAnimation && rotateAnimator != null)
            rotateAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (rotateAnimator != null)
            rotateAnimator.cancel();
    }

    public enum Mode {
        LOADING(0), PROGRESS(1);

        private int value;

        Mode(int value) {
            this.value = value;
        }

        static Mode getByValue(int value) {
            for (Mode mode : values())
                if (mode.value == value)
                    return mode;
            return LOADING;
        }
    }

}
