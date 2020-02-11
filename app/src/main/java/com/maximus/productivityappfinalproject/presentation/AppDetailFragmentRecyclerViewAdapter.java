package com.maximus.productivityappfinalproject.presentation;

import android.app.usage.UsageEvents;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppDetailFragmentRecyclerViewAdapter extends RecyclerView.Adapter<AppDetailFragmentRecyclerViewAdapter.AppDetailViewHolder> {
    private static final String TAG = "AppDetailFragmentRecycl";
    private List<AppsModel> mAppsModelList;
    @NonNull
    @Override
    public AppDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list_items, parent, false);
        return new AppDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppDetailViewHolder holder, int position) {
        AppsModel appsModel = mAppsModelList.get(position);
        String formatDate = new SimpleDateFormat(
                "yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(appsModel.getEventTime()));
        if (appsModel.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
            holder.mLinearLayout.setPadding(16, 9, 16, 4);
        } else if (appsModel.getEventType() == -1) {
            holder.mLinearLayout.setPadding(16, 4, 16, 4);
//            formatDate = Utils.formatMillisToSeconds(appsModel.getAppUsageTime()); //TODO
            Log.d(TAG, "onBindViewHolder: " + appsModel.getLastTimeUsed());
        } else if (appsModel.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
            holder.mLinearLayout.setPadding(16, 12, 16, 0);
        }
        holder.mEventTime.setText(String.format("%s %s", addDelimiter(appsModel.getEventType()), formatDate));
    }

    private String addDelimiter(int event) {
        switch (event) {
            case 1:
                return "┏ ";
            case 2:
                return "┗ ";
            default:
                return "┣  ";
        }
    }

    @Override
    public int getItemCount() {
        return mAppsModelList.size();
    }

    public void setList(List<AppsModel> modelList) {
        mAppsModelList = modelList;
        notifyDataSetChanged();
    }

    public class AppDetailViewHolder extends RecyclerView.ViewHolder{
        private TextView mEventTime;
        private LinearLayout mLinearLayout;
        public AppDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mEventTime = itemView.findViewById(R.id.event);
            mLinearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
