package com.example.sentinel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<NotificationObject> notificationObjects;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<NotificationObject> listdata) {
        this.notificationObjects = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_notif, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationObject notificationObject = notificationObjects.get(position);
        holder.title.setText(notificationObject.getAppName());
        holder.desc.setText(notificationObject.getDesc());
        holder.time.setText(notificationObject.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return notificationObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc,time;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.time);
        }
    }
}
