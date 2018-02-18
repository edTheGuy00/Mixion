/*
 * Copyright (c) 2017-2018 Gregor Santner and Markor contributors
 *
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */
package com.taskail.mixion.ui.markortexteditor.moduleactions;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.taskail.mixion.ui.markortexteditor.highlighter.HighlightingEditor;


public class CommonTextModuleActions {
   // public static final int ACTION_SPECIAL_KEY__ICON = R.drawable.ic_keyboard_black_24dp;
    public static final String ACTION_SPECIAL_KEY = "special_key";

    private final Activity _activity;
    private final HighlightingEditor _hlEditor;

    public CommonTextModuleActions(Activity activity, HighlightingEditor hlEditor) {
        _activity = activity;
        _hlEditor = hlEditor;
    }

    private String rstr(@StringRes int resKey) {
        return _activity.getString(resKey);
    }

    // Returns true when handled
    //public boolean runAction(String action) {
    //   switch (action) {
    //        case ACTION_SPECIAL_KEY: {
    //            SearchOrCustomTextDialogCreator.showSpecialKeyDialog(_activity, (callbackPayload) -> {
    //                if (!_hlEditor.hasSelection() && _hlEditor.length() > 0) {
    //                    _hlEditor.requestFocus();
    //                }
    //                if (callbackPayload.equals(rstr(R.string.key_page_down))) {
    //                    _hlEditor.simulateKeyPress(KeyEvent.KEYCODE_PAGE_DOWN);
    //                } else if (callbackPayload.equals(rstr(R.string.key_page_up))) {
    //                    _hlEditor.simulateKeyPress(KeyEvent.KEYCODE_PAGE_UP);
    //                } else if (callbackPayload.equals(rstr(R.string.key_pos_1))) {
    //                    _hlEditor.simulateKeyPress(KeyEvent.KEYCODE_MOVE_HOME);
    //                } else if (callbackPayload.equals(rstr(R.string.key_pos_end))) {
    //                    _hlEditor.simulateKeyPress(KeyEvent.KEYCODE_MOVE_END);
    //                } else if (callbackPayload.equals(rstr(R.string.key_pos_1_document))) {
    //                    _hlEditor.setSelection(0);
    //                } else if (callbackPayload.equals(rstr(R.string.key_pos_end_document))) {
    //                    _hlEditor.setSelection(_hlEditor.length());
    //                } else if (callbackPayload.equals(rstr(R.string.key_ctrl_a))) {
    //                    _hlEditor.setSelection(0, _hlEditor.length());
    //                } else if (callbackPayload.equals(rstr(R.string.key_tab))) {
    //                    _hlEditor.insertOrReplaceTextOnCursor("\u0009");
    //                }
    //            });
    //            return true;
    //        }
    //    }
    //    return false;
    //}
}
