package ir.siriusapps.sview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import ir.siriusapps.sview.R;
import ir.siriusapps.sview.ShapeView;
import ir.siriusapps.sview.Utils;

public class ArcLayout extends ShapeView {

    private float arcSize = Utils.dipToPix(16);
    private Direction direction = Direction.BOTTOM;

    public ArcLayout(Context context) {
        super(context);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ArcLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout);
            arcSize = typedArray.getDimensionPixelSize(R.styleable.ArcLayout_al_arcSize, (int) arcSize);
            direction = Direction.getByValue(typedArray.getInt(R.styleable.ArcLayout_al_arcDirection, Direction.BOTTOM.value));
            typedArray.recycle();
        }
    }

    @Override
    protected void initClipPath() {
        if (getWidth() == 0 || getHeight() == 0)
            return;
        switch (direction) {
            case LEFT:
                getClipPath().reset();
                getClipPath().moveTo(getWidth(), 0);
                getClipPath().lineTo(arcSize, 0);
                getClipPath().quadTo(-arcSize, getHeight()/ 2, arcSize, getHeight());
                getClipPath().lineTo(getWidth(), getHeight());
                getClipPath().close();
                break;

            case TOP:
                getClipPath().reset();
                getClipPath().moveTo(0, arcSize);
                getClipPath().quadTo(getWidth() / 2, -arcSize, getWidth(), arcSize);
                getClipPath().lineTo(getWidth(), getHeight());
                getClipPath().lineTo(0, getHeight());
                getClipPath().close();
                break;

            case RIGHT:
                getClipPath().reset();
                getClipPath().moveTo(0, 0);
                getClipPath().lineTo(getWidth() - arcSize, 0);
                getClipPath().quadTo(getWidth() + arcSize, getHeight() / 2, getWidth() - arcSize, getHeight());
                getClipPath().lineTo(0, getHeight());
                getClipPath().close();
                break;

            case BOTTOM:
                getClipPath().reset();
                getClipPath().moveTo(0, 0);
                getClipPath().lineTo(0, getHeight() - arcSize);
                getClipPath().quadTo(getWidth() / 2, getHeight() + arcSize, getWidth(), getHeight() - arcSize);
                getClipPath().lineTo(getWidth(), 0);
                getClipPath().close();
                break;
        }
    }

    public void setArcSize(float arcSize) {
        this.arcSize = arcSize;
        initClipPath();
        invalidate();
    }

    public enum Direction {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

        final int value;

        Direction(int value) {
            this.value = value;
        }

        public static Direction getByValue(int value) {
            for (Direction direction : values()) {
                if (direction.value == value)
                    return direction;
            }
            return BOTTOM;
        }
    }
}
