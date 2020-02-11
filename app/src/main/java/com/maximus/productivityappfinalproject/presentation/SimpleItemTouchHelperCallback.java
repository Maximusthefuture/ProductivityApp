package com.maximus.productivityappfinalproject.presentation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.ItemTouchHelperAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
//    private final OnSwipeAppToIgnoreList mSwipe;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
//        mSwipe = swipe;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemMoveToIgnoreList(viewHolder.getAdapterPosition());
//        mSwipe.onSwiped((int) viewHolder.getItemId());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
