package com.example.android.alphap.playgames;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.alphap.R;
import com.google.android.gms.games.multiplayer.Participant;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameFragment extends Fragment {

    private TextView countDownText;
    private TextView myScore;
    private TextView[] scores = new TextView[4];

    private Button clickMe;

    ImageView ivPoatato;

    public Listener listener;

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
        final View v = inflater.inflate(R.layout.fragment_game, container, false);


        ivPoatato = (ImageView) v.findViewById(R.id.potato_buddy_iv);
//        countDownText = (TextView) v.findViewById(R.id.countdown);
//        myScore = (TextView) v.findViewById(R.id.my_score);
//        scores[0] = (TextView) v.findViewById(R.id.score0);
//        scores[1] = (TextView) v.findViewById(R.id.score1);
//        scores[2] = (TextView) v.findViewById(R.id.score2);
//        scores[3] = (TextView) v.findViewById(R.id.score3);
//
//        clickMe = (Button) v.findViewById(R.id.button_sign_out);
//        clickMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.scoreOnePoint();
//            }
//        });

        return v;
    }

    public void updateScoreDisplay(int myScore) {
        this.myScore.setText(myScore);
    }

    //    public void updatePeerScoresDisplay(List<Participant> participants, Map<String, Integer> participantScore, String roomId, String myId, int myCurrentScore) {
//        scores[0].setText(String.format(Locale.US, "%d - Me", myCurrentScore));
//
//        int[] arr = {R.id.score1, R.id.score2, R.id.score3};
//
//        int i = 0;
//
//        if (roomId != null) {
//            for (Participant p : participants) {
//                String pid = p.getParticipantId();
//                if (pid.equals(myId))
//                    continue;
//                if (p.getStatus() != Participant.STATUS_JOINED)
//                    continue;
//                int score = participantScore.containsKey(pid) ? participantScore.get(pid) : 0;
//                scores[i].setText(score + " - " + p.getDisplayName());
//                i++;
//            }
//        }
//
//        for (; i < arr.length; i++) {
//            scores[i].setText("");
//        }
//    }
//
//    public void showClickMeButton(boolean show) {
//        clickMe.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//    }
//
//    public void updateCountDown(String time) {
//        countDownText.setText(time);
//    }
//
    interface Listener {
        void scoreOnePoint();
    }
}