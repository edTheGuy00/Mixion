/*
 * Copyright (c) 2017-2018 Gregor Santner and Markor contributors
 *
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */
package com.taskail.mixion.ui.markortexteditor.moduleactions;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.taskail.mixion.R;
import com.taskail.mixion.ui.markortexteditor.highlighter.HighlightingEditor;


@SuppressWarnings("WeakerAccess")
public abstract class TextModuleActions {
    protected HighlightingEditor _hlEditor;
    protected Activity _activity;
    protected Context _context;

    public TextModuleActions(Activity activity) {
        _activity = activity;
        _context = activity != null ? activity : _hlEditor.getContext();
    }

    public abstract void appendTextModuleActionsToBar(ViewGroup viewGroup);

    protected void appendTextModuleActionToBar(ViewGroup barLayout, @DrawableRes int iconRes, View.OnClickListener l) {
        ImageView btn = (ImageView) _activity.getLayoutInflater().inflate(R.layout.ui__quick_keyboard_button, (ViewGroup) null);
        btn.setImageResource(iconRes);
        btn.setOnClickListener(l);

        btn.setColorFilter(ContextCompat.getColor(_context, R.color.colorPrimaryDark));
        barLayout.addView(btn);
    }

    protected void setBarVisible(ViewGroup barLayout, boolean visible) {
        if (barLayout.getId() == R.id.edit_post_action_bar && barLayout.getParent() instanceof HorizontalScrollView) {
            ((HorizontalScrollView) barLayout.getParent())
                    .setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    //
    //
    //
    //
    public HighlightingEditor getHighlightingEditor() {
        return _hlEditor;
    }

    public TextModuleActions setHighlightingEditor(HighlightingEditor hlEditor) {
        _hlEditor = hlEditor;
        return this;
    }


    public Activity getActivity() {
        return _activity;
    }

    public TextModuleActions setActivity(Activity activity) {
        _activity = activity;
        return this;
    }

    public Context getContext() {
        return _context;
    }

    public TextModuleActions setContext(Context context) {
        _context = context;
        return this;
    }
}
