package com.tesi.client.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.tesi.client.R;

public class InfoTextView extends androidx.appcompat.widget.AppCompatTextView {

	public InfoTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		Drawable update= ResourcesCompat.getDrawable(getResources(), R.drawable._53430_checkbox_edit_pen_pencil_107516, null);
		if (update != null)
			update.setBounds(0, 0, update.getIntrinsicWidth(), update.getIntrinsicHeight());

		setCompoundDrawables(null, null, update, null);

	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
}
