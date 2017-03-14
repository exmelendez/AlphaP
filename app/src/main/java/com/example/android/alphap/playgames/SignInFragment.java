package com.example.android.alphap.playgames;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.alphap.R;
import com.google.android.gms.common.SignInButton;

public class SignInFragment extends Fragment
{
    private Listener listener;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof Listener)
        {
            listener = (Listener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        SignInButton signInButton = (SignInButton) v.findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSignInClicked();
            }
        });

        return v;
    }

    interface Listener {
        void onSignInClicked();
    }
}
