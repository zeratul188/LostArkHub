package com.lostark.lostarkapplication.ui.home;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.lostark.lostarkapplication.R;

public class DisplayImageView extends AppCompatImageView {
    private String mFit = "normal";

    public DisplayImageView(Context context) {
        super(context);
    }

    public DisplayImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mFit = context.obtainStyledAttributes(attrs, R.styleable.DisplayImageView).getString(R.styleable.DisplayImageView_fits);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec*2/3);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, widthSize*2/3);
    }
}
