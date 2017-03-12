package com.example.android.alphap.playgames;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.alphap.R;

public class MenuFragment extends Fragment {
    private Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Button createGameButton = (Button) v.findViewById(R.id.button_create_game);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCreateGameClicked();
            }
        });

        Button joinGameButton = (Button) v.findViewById(R.id.button_join_game);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onJoinGameClicked();
            }
        });

        Button signOutButton = (Button) v.findViewById(R.id.button_sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignOutClicked();
            }
        });

        return v;
    }

    interface Listener {
        void onCreateGameClicked();

        void onJoinGameClicked();

        void onSignOutClicked();
    }
}
