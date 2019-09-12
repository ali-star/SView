package ir.siriusapps.sview;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class SView {

    public static void setCorerRadius(View view, RectF rectF, Path clipPath, final int cornerRadius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setAlpha(0);
                    int mCornerRadius = cornerRadius;
                    if (mCornerRadius < 0)
                        mCornerRadius = view.getHeight() / 2;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), mCornerRadius);
                }
            });
        } else {
            int mCornerRadius = cornerRadius;
            if (mCornerRadius < 0)
                mCornerRadius = view.getHeight() / 2;

            rectF.set(0, 0, view.getWidth(), view.getHeight());
            clipPath.reset();
            clipPath.addRoundRect(rectF, mCornerRadius, mCornerRadius, Path.Direction.CW);
            view.invalidate();
        }
    }

    public static void drawCorners(Canvas canvas, Path clipPath, Paint clipPaint) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            canvas.drawPath(clipPath, clipPaint);
    }

}
