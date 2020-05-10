package com.tuhp00.teammanager.match;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.TimePicker;
import com.tuhp00.teammanager.squad.NewMember;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;

public class NewMatch extends AppCompatActivity
        implements //DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
{

    EditText opponent, noteMatch;
    Button btnDateMatch, btnTimeMatch;
    Spinner spinnerMatch;
    FloatingActionButton fbSaveMatch;
    DatabaseReference databaseMatch;
    Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        opponent = (EditText) findViewById(R.id.opponent);
        noteMatch = (EditText) findViewById(R.id.noteMatch);
        btnDateMatch = (Button) findViewById(R.id.btnDateMatch);
        btnTimeMatch = (Button) findViewById(R.id.btnTimeMatch);
        spinnerMatch = (Spinner) findViewById(R.id.spinnerMatch);
        fbSaveMatch = (FloatingActionButton) findViewById(R.id.fbSaveMatch);
        match = new Match();

        databaseMatch = FirebaseDatabase.getInstance().getReference().child("Matches");

      //  Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      //  calendar.clear();
        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Vyber datum");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraints.build());

        final MaterialDatePicker datePicker = builder.build();

        btnDateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                try {
                    SimpleDateFormat formatOld = new SimpleDateFormat("d. MMM yyyy");
                    SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");

                    Date dateS = formatOld.parse(datePicker.getHeaderText());
                    formatOld.applyPattern("dd-MM-yyyy");
                    String newDate = formatOld.format(dateS);

                    btnDateMatch.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnTimeMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        fbSaveMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMatch();
            }
        });
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        btnTimeMatch = (Button) findViewById(R.id.btnTimeMatch);
        btnTimeMatch.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    private void addMatch(){
        String opponentp = opponent.getText().toString().trim();
        String homeMatch = spinnerMatch.getSelectedItem().toString();
        String noteMatchp = noteMatch.getText().toString().trim();
        String dateMatchp = btnDateMatch.getText().toString().trim();
        String timeMatchp = btnTimeMatch.getText().toString().trim();
        String id = databaseMatch.push().getKey();

        try {
            SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");

            Date dateOrder = formatOld.parse(dateMatchp);
            formatOld.applyPattern("yyyy-MM-dd");
            final String dateForOrder = formatOld.format(dateOrder);
            match.setDateForOrderMatch(dateForOrder + " " + timeMatchp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!opponentp.matches("")) {
            if (!dateMatchp.matches("Vybrat datum")) {
                if (!timeMatchp.matches("Vybrat čas")) {
                    if (homeMatch.matches("Domácí zápas")) {
                        match.setId(id);
                        match.setTimeMatch(timeMatchp);
                        match.setDateMatch(dateMatchp);
                        match.setNoteMatch(noteMatchp);
                        match.setOpponent(opponentp);
                        match.setMatchTeams("Křešice - " + opponentp);
                        match.setHomeMatch(homeMatch);

                        databaseMatch.child(id).setValue(match);
                        Toast.makeText(NewMatch.this, "Zápas vytvořen", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        match.setId(id);
                        match.setTimeMatch(timeMatchp);
                        match.setDateMatch(dateMatchp);
                        match.setNoteMatch(noteMatchp);
                        match.setOpponent(opponentp);
                        match.setMatchTeams(opponentp + " - Křešice");
                        match.setHomeMatch(homeMatch);

                        databaseMatch.child(id).setValue(match);
                        Toast.makeText(NewMatch.this, "Zápas vytvořen", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(NewMatch.this, "Zadej čas!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewMatch.this, "Zadej datum!", Toast.LENGTH_SHORT).show();
            }
        } else {
            opponent.setError("Zadej soupeře!");
            opponent.requestFocus();
        }
    }

}
