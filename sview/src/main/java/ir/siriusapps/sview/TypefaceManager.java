package ir.siriusapps.sview;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;

/**
 * this class used for caching typefaces
 */
public class TypefaceManager {

    private static TypefaceManager instance;
    private HashMap<String, Typeface> typefaceHashMap = new HashMap<>();

    public static TypefaceManager getInstance() {
        if (instance == null)
            instance = new TypefaceManager();
        return instance;
    }

    public Typeface createTypeface(String path, Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
        typefaceHashMap.put(path, typeface);
        return typeface;
    }

    public Typeface getTypeface(String path, Context context) {
        if (typefaceHashMap.containsKey(path))
            return typefaceHashMap.get(path);
        else
            return createTypeface(path, context);
    }

}
