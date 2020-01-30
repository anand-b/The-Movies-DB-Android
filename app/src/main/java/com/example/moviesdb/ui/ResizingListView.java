package com.example.moviesdb.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import androidx.core.view.NestedScrollingChild;

public class ResizingListView extends ListView implements NestedScrollingChild {
    public ResizingListView(Context context) {
        super(context);
    }

    public ResizingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, getMeasuredHeight() * getAdapter().getCount());
    }
}
