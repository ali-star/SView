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

    public static void makeRoundedCornersPath(Path path, RectF rect,
                                              float topLeftDiameter,
                                              float topRightDiameter,
                                              float bottomRightDiameter,
                                              float bottomLeftDiameter){
        path.reset();

        topLeftDiameter = topLeftDiameter < 0 ? 0 : topLeftDiameter;
        topRightDiameter = topRightDiameter < 0 ? 0 : topRightDiameter;
        bottomLeftDiameter = bottomLeftDiameter < 0 ? 0 : bottomLeftDiameter;
        bottomRightDiameter = bottomRightDiameter < 0 ? 0 : bottomRightDiameter;

        path.moveTo(rect.left + topLeftDiameter, rect.top);
        path.lineTo(rect.right - topRightDiameter, rect.top);
        path.quadTo(rect.right, rect.top, rect.right, rect.top + topRightDiameter);
        path.lineTo(rect.right ,rect.bottom - bottomRightDiameter);
        path.quadTo(rect.right ,rect.bottom, rect.right - bottomRightDiameter, rect.bottom);
        path.lineTo(rect.left + bottomLeftDiameter, rect.bottom);
        path.quadTo(rect.left,rect.bottom,rect.left, rect.bottom - bottomLeftDiameter);
        path.lineTo(rect.left,rect.top + topLeftDiameter);
        path.quadTo(rect.left,rect.top, rect.left + topLeftDiameter, rect.top);
        path.close();
    }

    public static void setCorerRadius(View view, RectF rectF, Path clipPath, final int cornerRadius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
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
            SView.makeRoundedCornersPath(clipPath, rectF, mCornerRadius, mCornerRadius,
                    cornerRadius, cornerRadius);
            view.invalidate();
        }
    }

    public static void drawCorners(Canvas canvas, Path clipPath, Paint clipPaint) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            canvas.drawPath(clipPath, clipPaint);
    }

}
