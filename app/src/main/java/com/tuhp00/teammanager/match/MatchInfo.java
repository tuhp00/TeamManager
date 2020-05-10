package com.tuhp00.teammanager.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchInfo extends AppCompatActivity {

    TextView tvMatchTeamsInfo;
    TextView tvDateMatchInfo;
    TextView tvTimeMatchInfo;
    TextView tvNoteMatchInfo;
    TextView tvScore;
    TextView tvCircle;
    TextView btnAddScore, btnDeleteScore;
    DatabaseReference databaseMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        tvMatchTeamsInfo = (TextView) findViewById(R.id.tvMatchTeamsInfo);
        tvDateMatchInfo = (TextView) findViewById(R.id.tvDateMatchInfo);
        tvTimeMatchInfo = (TextView) findViewById(R.id.tvTimeMatchInfo);
        tvNoteMatchInfo = (TextView) findViewById(R.id.tvNoteMatchInfo);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvCircle = (TextView) findViewById(R.id.tvCircle);
        btnAddScore = (TextView) findViewById(R.id.btnAddScore);
        btnDeleteScore = (TextView) findViewById(R.id.btnDeleteScore);

        Intent intent = getIntent();

        final String teams = intent.getStringExtra(MatchMain.MATCH_TEAMS);
        final String date = intent.getStringExtra(MatchMain.MATCH_DATE);
        final String time = intent.getStringExtra(MatchMain.MATCH_TIME);
        final String note = intent.getStringExtra(MatchMain.MATCH_NOTE);
        final String id = intent.getStringExtra(MatchMain.MATCH_ID);

        databaseMatch = FirebaseDatabase.getInstance().getReference("Matches").child(id);

        tvMatchTeamsInfo.setText(teams);
        tvDateMatchInfo.setText(date);
        tvTimeMatchInfo.setText(time);
        tvNoteMatchInfo.setText(note);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseMatch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Match match = dataSnapshot.getValue(Match.class);

                final String s1 = match.getS1();
                final String s2 = match.getS2();
                final String teams = match.getMatchTeams();
                final String homeMatch = match.getHomeMatch();
                final String date = match.getDateMatch();
                final String id = match.getId();
                final String opponent = match.getOpponent();
                final String time = match.getTimeMatch();
                final String note = match.getNoteMatch();
                String scoreY = match.getScore();

                tvScore.setText(scoreY);
                String scoreText = tvScore.getText().toString();

                if(!scoreText.matches("")) {
                    int score1I = Integer.parseInt(s1);
                    int score2I = Integer.parseInt(s2);

                    if (homeMatch.matches("Domácí zápas")) {
                        if (score1I > score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_win));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        } else if (score1I < score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_loss));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        } else if (score1I == score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_draw));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        }
                    } else {
                        if (score1I < score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_win));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        } else if (score1I > score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_loss));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        } else if (score1I == score2I) {
                            tvCircle.setBackground(getResources().getDrawable(R.drawable.score_draw));
                            tvScore.setTextColor(getResources().getColor(R.color.score));
                        }
                    }
                } else {
                    tvCircle.setBackground(getResources().getDrawable(R.drawable.score));
                }

                // Kontrola zda už zápas proběhl, pokud ano, zobrazí se tlačítko přidat výsledek
                try  {
                    SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");

                    Date todayD = new Date();
                    String todayS = formatNew.format(todayD);

                    Date dateO = formatOld.parse(date);
                    formatOld.applyPattern("yyyy-MM-dd");
                    String newDate = formatOld.format(dateO);

                    Date d1 = formatNew.parse(todayS);
                    Date d2 = formatNew.parse(newDate);

                    if(d1.compareTo(d2) < 0) {
                        btnAddScore.setVisibility(View.INVISIBLE);
                        btnDeleteScore.setVisibility(View.INVISIBLE);
                    } else {
                        btnAddScore.setVisibility(View.VISIBLE);
                        btnDeleteScore.setVisibility(View.INVISIBLE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Kontrola zda už je výsledek zadán, pokud ano text tlačítka se změní
                if (scoreText.matches("")) {
                    btnAddScore.setText("Přidat výsledek");
                } else {
                    btnAddScore.setText("Upravit výsledek");
                    btnDeleteScore.setVisibility(View.VISIBLE);
                }
                btnAddScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openScoreDialog(id, s1, s2, teams, opponent, date, time, note, homeMatch);
                    }
                });

                btnDeleteScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteScore(id, teams, opponent, date, time, note, homeMatch);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public  void openScoreDialog(final String idUpdate, String s1Update, String s2Update,
                                 final String teamsUpdate, final String opponentUpdate, final String dateUpdate,
                                 final String timeUpdate, final String noteUpdate,
                                 final String homeMatchUpdate) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_score, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Výsledek");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final EditText etS1 = (EditText) dialogView.findViewById(R.id.etS1);
        etS1.setText(s1Update);
        final EditText etS2 = (EditText) dialogView.findViewById(R.id.etS2);
        etS2.setText(s2Update);

        final Button btnSaveScore = (Button) dialogView.findViewById(R.id.btnSaveScore);
        btnSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String score1v = etS1.getText().toString().trim();
                String score2v = etS2.getText().toString().trim();

                if (!score1v.matches("")) {
                    if (!score2v.matches("")) {
                        updateScore(idUpdate,score1v,score2v, teamsUpdate, opponentUpdate, dateUpdate,
                                timeUpdate, noteUpdate, homeMatchUpdate);
                        alertDialog.dismiss();
                    } else {
                        etS2.setError("Zadej výsledek");
                        etS2.requestFocus();
                    }
                } else {
                    etS1.setError("Zadej výsledek");
                    etS1.requestFocus();
                }
            }
        });
    }

    public void deleteScore(String id, String teams, String opponent, String date, String time,
                            String note, String homeMatch){
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference("Matches").child(id);
        Match match = new Match();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date dateOrder = dateFormat.parse(date);
            dateFormat.applyPattern("yyyy-MM-dd");

            String dateForOrder = dateFormat.format(dateOrder);
            match.setDateForOrderMatch(dateForOrder + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        match.setId(id);
        match.setMatchTeams(teams);
        match.setOpponent(opponent);
        match.setDateMatch(date);
        match.setTimeMatch(time);
        match.setNoteMatch(note);
        match.setHomeMatch(homeMatch);

        databaseReference.setValue(match);

        Toast.makeText(this, "Výsledek odstraněn",Toast.LENGTH_SHORT).show();
    }

    public void updateScore(String id, String score1, String score2, String teams, String opponent,
                            String date, String time, String note, String homeMatch){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Matches").child(id);

        Match match = new Match();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date dateOrder = dateFormat.parse(date);
            dateFormat.applyPattern("yyyy-MM-dd");

            String dateForOrder = dateFormat.format(dateOrder);
            match.setDateForOrderMatch(dateForOrder + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String score = score1 + " - " + score2;

        match.setId(id);
        match.setMatchTeams(teams);
        match.setOpponent(opponent);
        match.setDateMatch(date);
        match.setTimeMatch(time);
        match.setNoteMatch(note);
        match.setHomeMatch(homeMatch);
        match.setS1(score1);
        match.setS2(score2);
        match.setScore(score);

        databaseReference.setValue(match);
        Toast.makeText(this, "Výsledek přidán", Toast.LENGTH_SHORT).show();
    }
}
