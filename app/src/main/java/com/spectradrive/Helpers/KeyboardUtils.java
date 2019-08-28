package com.spectradrive.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void hideKeyboard(Activity activity, View rootView) {
        if(rootView == null){
                rootView = ((ViewGroup) activity
                        .findViewById(android.R.id.content)).getChildAt(0);
        }

        if (rootView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void addKeyboardVisibilityListener(final View rootLayout, final OnKeyboardVisibiltyListener onKeyboardVisibiltyListener) {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                boolean isVisible = keypadHeight > screenHeight * 0.15; // 0.15 ratio is perhaps enough to determine keypad height.
                onKeyboardVisibiltyListener.onVisibilityChange(isVisible);
            }
        });
    }

    public interface OnKeyboardVisibiltyListener {
        void onVisibilityChange(boolean isVisible);
    }
}