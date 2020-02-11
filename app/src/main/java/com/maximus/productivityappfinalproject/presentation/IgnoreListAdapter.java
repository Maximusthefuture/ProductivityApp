package com.maximus.productivityappfinalproject.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public class IgnoreListAdapter extends RecyclerView.Adapter<IgnoreListAdapter.IgnoreViewHolder> {

    private List<IgnoreItems> mIgnoreItems;

    @NonNull
    @Override
    public IgnoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ignore_list_items, parent, false);
        return new IgnoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IgnoreViewHolder holder, int position) {
        IgnoreItems ignoreItems = mIgnoreItems.get(position);
        holder.mAppName.setText(ignoreItems.getName());
    }

    public void setList(List<IgnoreItems> list) {
        mIgnoreItems = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIgnoreItems.size();
    }

    public class IgnoreViewHolder extends RecyclerView.ViewHolder{
        private TextView mAppName;
//        private ImageView mImageView;

        public IgnoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mAppName = itemView.findViewById(R.id.ignore_item_app_name);
        }
    }
}
