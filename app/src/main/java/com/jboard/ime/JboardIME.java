package com.jboard.ime;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class JboardIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private SettingsManager settingsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        settingsManager = new SettingsManager(this);
    }

    @Override
    public View onCreateInputView() {
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboardView = layout.findViewById(R.id.keyboard);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        View codingRow = layout.findViewById(R.id.coding_row_container);
        if (codingRow != null) {
            codingRow.setVisibility(settingsManager.isCodingRowEnabled() ? View.VISIBLE : View.GONE);
        }

        setupCodingRow(layout);

        return layout;
    }

    private void setupCodingRow(View root) {
        findAndSetCodingKeys(root);
    }

    private void findAndSetCodingKeys(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;
            if (b.getText() != null && b.getText().length() == 1) {
                b.setOnClickListener(v -> {
                    InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        ic.commitText(b.getText(), 1);
                    }
                });
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                findAndSetCodingKeys(vg.getChildAt(i));
            }
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                keyboard.setShifted(!keyboard.isShifted());
                keyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER));
                ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && keyboard.isShifted()) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onPress(int primaryCode) {}
    @Override
    public void onRelease(int primaryCode) {}
    @Override
    public void onText(CharSequence text) {}
    @Override
    public void swipeLeft() {}
    @Override
    public void swipeRight() {}
    @Override
    public void swipeDown() {}
    @Override
    public void swipeUp() {}
}
