package com.example.representuapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.graphics.Rect;



public class Divider extends RecyclerView.ItemDecoration {
    private int space;

    public Divider(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;


        //for vertical scrolling
        outRect.bottom = space;
        outRect.top = space;
    }
}
