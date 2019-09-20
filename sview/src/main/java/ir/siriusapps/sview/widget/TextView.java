package ir.siriusapps.sview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import ir.siriusapps.sview.R;
import ir.siriusapps.sview.TypefaceManager;

@SuppressLint("AppCompatCustomView")
public class TextView extends android.widget.TextView {

    private String typefacePath;

    public TextView(Context context) {
        super(context);
        init(null);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SView);
            typefacePath = typedArray.getString(R.styleable.SView_sview_typeface);
            typedArray.recycle();
        }

        if (typefacePath != null)
            setTypeface(typefacePath);
    }

    /*   Typeface   */
    public void setTypeface(String path) {
        if (path == null)
            return;
        Typeface typeface = TypefaceManager.getInstance().getTypeface(path, getContext());
        setTypeface(typeface);
    }
}
