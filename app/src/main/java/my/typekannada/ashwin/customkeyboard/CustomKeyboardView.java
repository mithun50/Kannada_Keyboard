package my.typekannada.ashwin.customkeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.lang.reflect.Field;

public class CustomKeyboardView extends KeyboardView {
    private Typeface customTypeface;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Load the selected font
        customTypeface = FontHelper.getSelectedFont(context);
    }

    public void setCustomTypeface(Typeface typeface) {
        this.customTypeface = typeface;
        invalidate(); // Redraw the keyboard
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Apply custom font to the Paint object used for drawing keys
        if (customTypeface != null) {
            try {
                // Use reflection to access the private Paint field in KeyboardView
                Field[] fields = KeyboardView.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == Paint.class) {
                        field.setAccessible(true);
                        Paint paint = (Paint) field.get(this);
                        if (paint != null) {
                            paint.setTypeface(customTypeface);
                        }
                    }
                }
            } catch (Exception e) {
                // If reflection fails, font won't be applied but keyboard will still work
                e.printStackTrace();
            }
        }
        super.onDraw(canvas);
    }

    /**
     * Refresh font from preferences
     */
    public void refreshFont() {
        customTypeface = FontHelper.getSelectedFont(getContext());
        invalidate();
    }
}
