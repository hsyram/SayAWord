package com.barantech.sayaword.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.barantech.sayaword.R;


/**
 * custom TextView with custom typeface
 * <br/>
 * Created by mary on 10/18/2014.
 */
public class CustomEditText extends EditText {
    int fontType;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs){
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.font,
                0, 0);

        try {
            fontType = a.getInteger(R.styleable.font_fontName, 0);
        } finally {
            a.recycle();
        }
        if(!isInEditMode())
            setTypeface(fontType);
    }

    private void setTypeface(int fontType){
        Typeface typeface;
        switch (fontType){
            case 0:
                typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/SkarpaLt.ttf");
                break;
            case 1:
                typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/penna.otf");
                break;
            case 2:
                typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/penna.otf");
                break;
            case 3:
                typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/penna.otf");
                break;
            default:
                typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/SkarpaLt.ttf");
                break;
        }
        setTypeface(typeface);
    }
}
