package com.maximus.productivityappfinalproject.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.List;

public class LimitedListAdapter extends RecyclerView.Adapter<LimitedListAdapter.IgnoreViewHolder> {

    private static final String TAG = "LimitedListAdapter";
    private List<IgnoreItems> mIgnoreItems;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private Context mContext;
    private OnIgnoreItemClickListener mItemClickListener;

    public LimitedListAdapter(Context context, OnIgnoreItemClickListener clickListener) {
        mContext = context;
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public IgnoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_list_items, parent, false);
        return new IgnoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IgnoreViewHolder holder, int position) {
        IgnoreItems ignoreItems = mIgnoreItems.get(position);
        holder.bind(ignoreItems, holder);
    }

    public void setList(List<IgnoreItems> list) {
        mIgnoreItems = list;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
         return mIgnoreItems != null ? mIgnoreItems.size() : 0;
    }

    public class IgnoreViewHolder extends RecyclerView.ViewHolder{
        private TextView mAppName;
        private TextView mLastTimeUsed;
        private TextView mTimeUsed;
        private ImageView mIcon;

        public IgnoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mAppName = itemView.findViewById(R.id.ignore_item_app_name);
            mLastTimeUsed = itemView.findViewById(R.id.time);
            mTimeUsed = itemView.findViewById(R.id.time_used);
            mIcon = itemView.findViewById(R.id.app_icon_image_view);
        }

        public void bind(IgnoreItems item, IgnoreViewHolder holder) {
            mAppName.setText(item.getName());
            mLastTimeUsed.setText(item.getLastTimeUsed());
            mTimeUsed.setText(Utils.formatMillisToSeconds(item.getTimeUsed()));
//            for (IgnoreItems ignoreItem : mIgnoreItems) {
//                if (ignoreItem.getPackageName().equals(appUsageLimitModel.packageName())) {
//                    //some icon that show item in limit list
//
//                }
//            }
            Glide.with(mIcon.getContext())
                    .load(mMyUsageStatsManagerWrapper.getAppIcon(item.getPackageName()))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(mIcon);

            itemView.setOnClickListener(v -> {
//                mIgnoreItems.remove(holder.getAdapterPosition());
                //TODO
                mItemClickListener.onItemClickListener(item);
//                notifyItemRemoved(getAdapterPosition());
            });
        }

    }
}
