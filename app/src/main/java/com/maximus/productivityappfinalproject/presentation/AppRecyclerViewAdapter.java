package com.maximus.productivityappfinalproject.presentation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.ItemTouchHelperAdapter;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.Utils;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.ArrayList;
import java.util.List;

public class AppRecyclerViewAdapter extends RecyclerView.Adapter<AppRecyclerViewAdapter.AddAppViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "AddAppRecyclerViewAdapt";
    private List<AppsModel> mAppsNameList = new ArrayList<>();
    private OnAppClickListener mClickListener;
    private List<IgnoreItems> mIgnoreItemsList;
    OnSwipeAppToIgnoreList mOnSwipeAppToIgnoreList;

    public AppRecyclerViewAdapter(OnAppClickListener clickListener, OnSwipeAppToIgnoreList onSwipeAppToIgnoreList) {
        mClickListener = clickListener;
        mOnSwipeAppToIgnoreList = onSwipeAppToIgnoreList;
    }

    @NonNull
    @Override
    public AddAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_list_items, parent, false);
        return new AddAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAppViewHolder holder, int position) {
//        AppsModel appsModel = getDataFromPosition(position);
        holder.bind(mAppsNameList.get(position));

    }

    public void setList(List<AppsModel> list) {
        this.mAppsNameList = list;
        notifyDataSetChanged();
    }

    public AppsModel getDataFromPosition(int position) {
        if (mAppsNameList.size() >= position) {
            return mAppsNameList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return (long) (position);
    }

    @Override
    public int getItemCount() {
        return mAppsNameList.size();

    }

    @Override
    public void onItemMoveToIgnoreList(int position) {
        mAppsNameList.remove(position);
        mOnSwipeAppToIgnoreList.onSwiped(position);
        notifyItemRemoved(position);


    }


    public class AddAppViewHolder extends RecyclerView.ViewHolder {

        private TextView mAppNameTextView;
        private ImageView mAppIconImageView;
        private TextView mAppLastTimeUsedTextView;
        private TextView mUsageTimeTextView;

        public AddAppViewHolder(@NonNull View itemView) {
            super(itemView);

            mAppNameTextView = itemView.findViewById(R.id.add_app_name_text_view);
            mAppIconImageView = itemView.findViewById(R.id.app_icon_image_view);
            mAppLastTimeUsedTextView = itemView.findViewById(R.id.app_last_time_used_text_view);
            mUsageTimeTextView = itemView.findViewById(R.id.usage_time);


        }

        public void bind(@NonNull AppsModel appsModel) {
            mAppNameTextView.setText(appsModel.getAppName());
            mAppIconImageView.setImageDrawable(appsModel.getAppIcon());
            mAppLastTimeUsedTextView.setText(itemView.getContext().getString(R.string.last_time_used, appsModel.getLastTimeUsed()));
            mUsageTimeTextView.setText(Utils.formatMillisToSeconds(appsModel.getAppUsageTime()));

//            if (appsModel.getLastTimeUsed() == null) {
//                mAppLastTimeUsedTextView.setText("");
//            }
            itemView.setOnClickListener(v -> {
                mClickListener.onAppClickListener(appsModel);
                Log.d(TAG, "onClick: " + appsModel);
            });
        }
    }


}




