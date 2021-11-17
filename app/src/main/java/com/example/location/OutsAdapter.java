package com.example.location;

import android.content.Intent;
import android.icu.util.Output;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.location.data.Sender;
import com.example.location.data.Out;

public class OutsAdapter extends RecyclerView.Adapter<OutsAdapter.OutsViewHolder> {

    private List<Out> listOuts = new ArrayList<>();

    private static final String TAG = "OutsAdapter";

    @NonNull
    @Override
    public OutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_out, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutsViewHolder holder, int position) {
        Out itemOuts = listOuts.get(position);
        holder.title.setText(itemOuts.getEmail());
        if (itemOuts.getLastLocation() != null){
            holder.last_location.setText(itemOuts.getLastLocation().getText());
            holder.time.setText(itemOuts.getLastLocation().getTime());
        }

        holder.clicker.setOnClickListener(v -> {
            MapsActivity.out = itemOuts;
            v.getContext().startActivity(new Intent(v.getContext(),MapsActivity.class));
        });

        holder.refresh.setOnClickListener(v -> {
            holder.refresh.setVisibility(View.INVISIBLE);
            Sender.sendNotifications(itemOuts.getToken(), "ll", new Sender.NotifyCallback() {
                @Override
                public void onSuccess() {
                    holder.refresh.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed() {
                    holder.refresh.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listOuts.size();
    }

    public void setList(List<Out> listOuts) {
        this.listOuts = listOuts;
        notifyDataSetChanged();
    }

    public static class OutsViewHolder extends RecyclerView.ViewHolder {
        TextView title,last_location,time;

        ImageView refresh;

        Button clicker;

        public OutsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            last_location = itemView.findViewById(R.id.last_locotion);
            time = itemView.findViewById(R.id.time);
            refresh = itemView.findViewById(R.id.refresh);
            clicker = itemView.findViewById(R.id.clicker);
        }
    }
}

