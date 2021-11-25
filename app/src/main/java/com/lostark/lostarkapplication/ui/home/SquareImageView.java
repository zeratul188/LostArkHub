package com.lostark.lostarkapplication.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.lostark.lostarkapplication.R;

public class SquareImageView extends AppCompatImageView {
    private String mFit = "normal";

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mFit = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView).getString(R.styleable.SquareImageView_fit);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        /*if(TextUtils.equals(mFit, "width")) { // fit width
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else if(TextUtils.equals(mFit, "height")) { // fit height
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }*/
    }
}
