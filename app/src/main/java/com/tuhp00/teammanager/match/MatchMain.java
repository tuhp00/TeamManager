package com.tuhp00.teammanager.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MatchMain extends AppCompatActivity {

    public static final String MATCH_TEAMS = "matchTeams";
    public static final String MATCH_OPPONENT = "opponent";
    public static final String MATCH_DATE = "dateMatch";
    public static final String MATCH_TIME = "timeMatch";
    public static final String MATCH_NOTE = "noteMatch";
    public static final String MATCH_HOME = "homeMatch";
    public static final String MATCH_ID = "id";
    public static final String MATCH_S1 = "s1";
    public static final String MATCH_S2 = "s2";
    public static final String MATCH_SCORE = "score";

    EditText etOpponentUpdate;
    Button btnTimeUpdateMatch;
    ListView listViewMatches;
    List<Match> matchList;
    DatabaseReference databaseMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_match);

        FloatingActionButton fbAddMatch = (FloatingActionButton) findViewById(R.id.fbAddMatch);
        fbAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MatchMain.this, NewMatch.class);
                startActivity(in);
            }
        });

        listViewMatches = (ListView) findViewById(R.id.listViewMatches);
        matchList = new ArrayList<>();
        databaseMatch = FirebaseDatabase.getInstance().getReference("Matches");

        // Otevření jednotlivých informací o zápase
        listViewMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Match match = matchList.get(position);

                Intent intent = new Intent(MatchMain.this, MatchInfo.class);

                intent.putExtra(MATCH_TEAMS, match.getMatchTeams());
                intent.putExtra(MATCH_OPPONENT, match.getOpponent());
                intent.putExtra(MATCH_DATE, match.getDateMatch());
                intent.putExtra(MATCH_TIME, match.getTimeMatch());
                intent.putExtra(MATCH_NOTE, match.getNoteMatch());
                intent.putExtra(MATCH_HOME, match.getHomeMatch());
                intent.putExtra(MATCH_ID, match.getId());
                intent.putExtra(MATCH_S1, match.getS1());
                intent.putExtra(MATCH_S2, match.getS2());
                intent.putExtra(MATCH_SCORE, match.getScore());

                startActivity(intent);
            }
        });

        listViewMatches.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Match match = matchList.get(position);
                openUpdateMatchDialog(match.getId(), match.getMatchTeams(), match.getOpponent(),
                        match.getDateMatch(), match.getTimeMatch(), match.getNoteMatch(),
                        match.getS1(), match.getS2(), match.getScore());
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseMatch.orderByChild("dateForOrderMatch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();
                for (DataSnapshot matchSnapshot: dataSnapshot.getChildren()) {
                    Match match = matchSnapshot.getValue(Match.class);

                    matchList.add(match);
                }
                MatchList adapter = new MatchList(MatchMain.this, matchList);
                listViewMatches.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MatchMain.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUpdateMatchDialog(final String idUpdate, String matchTeamsUdate,
                                       String opponentUpdate, String dateUpdate,
                                       String timeUpdate, String noteUpdate,
                                       final String s1U, final String s2U, final String scoreU) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MatchMain.this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_update_match, null);

        dialogBuilder.setView(dialogView);

        final TextView tvMatchTeams = (TextView) dialogView.findViewById(R.id.tvMatchTeams);
        tvMatchTeams.setText(matchTeamsUdate);

        final Button btnDateUpdateMatch = (Button) dialogView.findViewById(R.id.btnUpdateDateMatch);
        btnDateUpdateMatch.setText(dateUpdate);

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Vyber datum");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraints.build());

        final MaterialDatePicker datePicker = builder.build();
        btnDateUpdateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(MatchMain.this.getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                try {
                    SimpleDateFormat formatOld = new SimpleDateFormat("d. MMM yyyy");

                    Date dateS = formatOld.parse(datePicker.getHeaderText());
                    formatOld.applyPattern("dd-MM-yyyy");
                    String newDate = formatOld.format(dateS);

                    btnDateUpdateMatch.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnTimeUpdateMatch = (Button) dialogView.findViewById(R.id.btnUpdateTimeMatch);
        btnTimeUpdateMatch.setText(timeUpdate);
        btnTimeUpdateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        final Spinner spinnerMatchUpdate = (Spinner) dialogView.findViewById(R.id.spinnerMatchUpdate);
        etOpponentUpdate = (EditText) dialogView.findViewById(R.id.etUpdateOpponent);
        etOpponentUpdate.setText(opponentUpdate);
        final EditText etNoteUpdate = (EditText) dialogView.findViewById(R.id.etUpdateNoteMatch);
        etNoteUpdate.setText(noteUpdate);

        final Button btnUpdateMatch = (Button) dialogView.findViewById(R.id.btnUpdateMatch);
        final Button btnDeleteMatch = (Button) dialogView.findViewById(R.id.btnDeleteMatch);

        dialogBuilder.setTitle("Upravit zápas");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opponentv = etOpponentUpdate.getText().toString().trim();
                String homeMatchv = spinnerMatchUpdate.getSelectedItem().toString();
                String datev = btnDateUpdateMatch.getText().toString().trim();
                String timev = btnTimeUpdateMatch.getText().toString().trim();
                String notev = etNoteUpdate.getText().toString().trim();

                if (opponentv.matches("")) {
                    etOpponentUpdate.setError("Zadej soupeře!");
                    etOpponentUpdate.requestFocus();
                } else {
                    updateMatch(idUpdate, opponentv, homeMatchv, datev, timev, notev, s1U, s2U, scoreU);
                    alertDialog.dismiss();
                }
            }
        });

        btnDeleteMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMatch(idUpdate);
                alertDialog.dismiss();
            }
        });

    }

    private void timePicker(){
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MatchMain.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btnTimeUpdateMatch.setText(String.format("%02d:%02d",hourOfDay,minute));
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private boolean updateMatch(String id, String opponent, String homeMatch, String date,
                                String time, String note, String s1, String s2, String score){
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

        if (homeMatch.matches("Domácí zápas")) {
            match.setId(id);
            match.setDateMatch(date);
            match.setTimeMatch(time);
            match.setNoteMatch(note);
            match.setOpponent(opponent);
            match.setHomeMatch(homeMatch);
            String kresice = "Křešice";
            kresice.toUpperCase();
            match.setMatchTeams(kresice + " - " + opponent);
            match.setS1(s1);
            match.setS2(s2);
            match.setScore(score);

            databaseReference.setValue(match);
            Toast.makeText(MatchMain.this, "Zápas upraven", Toast.LENGTH_SHORT).show();
        } else {
            match.setId(id);
            match.setDateMatch(date);
            match.setTimeMatch(time);
            match.setNoteMatch(note);
            match.setOpponent(opponent);
            match.setMatchTeams(opponent + " - Křešice");
            match.setS1(s1);
            match.setS2(s2);
            match.setScore(score);

            databaseReference.setValue(match);
            Toast.makeText(MatchMain.this, "Zápas upraven", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void deleteMatch (String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Matches").child(id);

        databaseReference.removeValue();

        Toast.makeText(MatchMain.this, "Zápas odstraněn",Toast.LENGTH_SHORT).show();
    }
}
