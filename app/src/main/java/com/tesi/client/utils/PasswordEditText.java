package com.tesi.client.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.tesi.client.R;

public class PasswordEditText extends AppCompatEditText {

	public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		Drawable mostra=ResourcesCompat.getDrawable(getResources(), R.drawable.icons8_visibile_24, null);
		Drawable nascondi=ResourcesCompat.getDrawable(getResources(), R.drawable.icons8_nascondere_24, null);

		if (mostra != null) {
			mostra.setBounds(0, 0, mostra.getIntrinsicWidth(), mostra.getIntrinsicHeight());
		}
		if (nascondi != null) {
			nascondi.setBounds(0, 0, nascondi.getIntrinsicWidth(), nascondi.getIntrinsicHeight());
		}


		setOnTouchListener((v, event)->{
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (event.getRawX() >= (getRight()-getCompoundDrawables()[2].getBounds().width()))
					if (getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)) {
						setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
						setCompoundDrawables(null, null, nascondi, null);
					} else {
						setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
						setCompoundDrawables(null, null, mostra, null);
					}
				setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
				setTypeface(Typeface.DEFAULT);
				setSelection(getText().length());
			}
			return performClick();
		});
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
}
