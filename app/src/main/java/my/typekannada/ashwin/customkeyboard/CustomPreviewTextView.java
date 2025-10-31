package my.typekannada.ashwin.customkeyboard;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Custom TextView for key preview that automatically applies selected font
 */
public class CustomPreviewTextView extends AppCompatTextView {

    public CustomPreviewTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomPreviewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPreviewTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Apply the selected font from preferences
        Typeface customFont = FontHelper.getSelectedFont(context);
        if (customFont != null) {
            setTypeface(customFont);
        }
    }

    /**
     * Refresh font from preferences (useful after font change)
     */
    public void refreshFont() {
        Typeface customFont = FontHelper.getSelectedFont(getContext());
        if (customFont != null) {
            setTypeface(customFont);
        }
    }
}
