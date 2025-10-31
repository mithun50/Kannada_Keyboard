package my.typekannada.ashwin.customkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

public class FontHelper {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FONT_PREFS_KEY = "selected_font_style";
    private static final String TAG = "FontHelper";

    /**
     * Get the currently selected font typeface
     * @param context Application context
     * @return Typeface of selected font, or default if none selected
     */
    public static Typeface getSelectedFont(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String fontName = prefs.getString(FONT_PREFS_KEY, "default");

        return getFontByName(context, fontName);
    }

    /**
     * Get font typeface by name
     * @param context Application context
     * @param fontName Font resource name (without extension)
     * @return Typeface or default
     */
    public static Typeface getFontByName(Context context, String fontName) {
        if (fontName == null || fontName.equals("default")) {
            return Typeface.DEFAULT;
        }

        try {
            int fontId = context.getResources().getIdentifier(fontName, "font", context.getPackageName());
            if (fontId != 0) {
                Typeface typeface = ResourcesCompat.getFont(context, fontId);
                if (typeface != null) {
                    return typeface;
                }
            }
            Log.w(TAG, "Font not found: " + fontName + ", using default");
        } catch (Exception e) {
            Log.e(TAG, "Error loading font: " + fontName, e);
        }

        return Typeface.DEFAULT;
    }

    /**
     * Save font selection
     * @param context Application context
     * @param fontName Font resource name
     */
    public static void saveSelectedFont(Context context, String fontName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(FONT_PREFS_KEY, fontName).apply();
    }

    /**
     * Get currently selected font name
     * @param context Application context
     * @return Font name string
     */
    public static String getSelectedFontName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(FONT_PREFS_KEY, "default");
    }
}
