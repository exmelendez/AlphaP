package com.example.android.alphap.eddie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.alphap.R;

import java.util.ArrayList;

public class MainActivityRVAdapter extends RecyclerView.Adapter<MainActivityRVAdapter.ViewHolder> {
    private ArrayList<Integer> mainRvPics;
    private ArrayList<String> mainRvTv;
    private Context context;
    private Listener listener;

    public MainActivityRVAdapter(ArrayList<Integer> mainRvPics, ArrayList<String> mainRvTv, Listener listener) {
        this.mainRvPics = mainRvPics;
        this.mainRvTv = mainRvTv;
        this.listener = listener;
    }


    @Override
    public MainActivityRVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_screen_rv_iv_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainActivityRVAdapter.ViewHolder viewHolder, int i) {

        viewHolder.mainRvIvPics.setImageResource(mainRvPics.get(i));
        viewHolder.mainRvTv.setText(mainRvTv.get(i));

    }

    @Override
    public int getItemCount() {
        return mainRvPics.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mainRvIvPics;
        private TextView mainRvTv;

        public ViewHolder(View view) {
            super(view);

            mainRvIvPics = (ImageView) view.findViewById(R.id.rv_image);
            mainRvTv = (TextView) view.findViewById(R.id.rv_tv);

            mainRvIvPics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {
                        case 0:
                            listener.onCreateGameClicked();
                            break;
                        case 1:
                            listener.onJoinGameClicked();
                            break;
                    }
                }
            });
        }
    }

    public interface Listener {
        void onCreateGameClicked();

        void onJoinGameClicked();
    }
}