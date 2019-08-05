package com.example.sentinel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is NotificationsFragment it should have R.layout.home_fragment
        //if it is LocationFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_notification, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_notif = view.findViewById(R.id.rv_notif);
        rv_notif.setLayoutManager(new LinearLayoutManager(getActivity()));
        DBManager dbManager = Master.getDataBase();
        rv_notif.setAdapter(new MyListAdapter(dbManager.getNotifications()));
    }
}