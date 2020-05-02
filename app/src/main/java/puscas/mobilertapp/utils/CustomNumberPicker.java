package puscas.mobilertapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

/**
 * Custom {@link NumberPicker} for the User Interface with smaller text size
 * and black color for all the number pickers used.
 */
public final class CustomNumberPicker extends NumberPicker {

    /**
     * The {@link Logger} for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CustomNumberPicker.class.getName());

    /**
     * The constructor for this class.
     *
     * @param context The {@link Context} of the Android system.
     * @param attrs   The {@link AttributeSet} of the Android system.
     */
    public CustomNumberPicker(@Nonnull final Context context, @Nonnull final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(@Nonnull final View child, @Nonnull final ViewGroup.LayoutParams params) {
        super.addView(child, params);
        LOGGER.info("addView");

        final int color = Color.parseColor(ConstantsUI.COLOR_NUMBER_PICKER);
        ((TextView) child).setTextSize(ConstantsUI.TEXT_SIZE);
        ((TextView) child).setTextColor(color);
    }
}
