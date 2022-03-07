package com.lostark.lostarkapplication.ui.commander;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()-1) {
            outRect.bottom = 10;
        }
    }
}
