package com.tuhp00.teammanager.training;

import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTraining extends AppCompatActivity{

    Button date, time;
    EditText info;
    FloatingActionButton fbSaveNewTraining;
  //  DatabaseReference databaseTraining;
  //  Training training;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = (Button) findViewById(R.id.btnDate);
        time = (Button) findViewById(R.id.btnTime);
        info = (EditText) findViewById(R.id.editTextInfo);
        fbSaveNewTraining = (FloatingActionButton) findViewById(R.id.fbSaveNewTraining);

        // DatePicker
        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Vyber datum");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraints.build());

        final MaterialDatePicker datePicker = builder.build();

        date.setOnClickListener(new View.OnClickListener() {
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

                    Date dateS = formatOld.parse(datePicker.getHeaderText());
                    formatOld.applyPattern("dd-MM-yyyy");
                    String newDate = formatOld.format(dateS);

                    date.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // TimePicker  změnit
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        fbSaveNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTraining();
            }
        });
    }

    private void timePicker(){
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                time.setText(String.format("%02d:%02d",hourOfDay,minute));
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private void addTraining() {
        DatabaseReference databaseTraining = FirebaseDatabase.getInstance().getReference().child("Trainings");
        Training training = new Training();

        String dateY = date.getText().toString().trim();
        String timeY = time.getText().toString().trim();
        String infoY = info.getText().toString().trim();
        String id = databaseTraining.push().getKey();

        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

            Date dateOrder = formatDate.parse(dateY);
            formatDate.applyPattern("yyyy-MM-dd");

            final String dateForOrder = formatDate.format(dateOrder);
            training.setDateForOrderTraining(dateForOrder + " " + timeY);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Kontrola zda nejsou prázná pole pro datum a čas
        if (!dateY.matches("Vybrat datum")) {
            if (!timeY.matches("Vybrat čas")) {
                training.setDateTraining(dateY);
                training.setTimeTraining(timeY);
                training.setNoteTraining(infoY);
                training.setId(id);

                databaseTraining.child(id).setValue(training);
                Toast.makeText(NewTraining.this, "Trénink vytvořen", Toast.LENGTH_SHORT).show();

                finish();
            } else {
                Toast.makeText(NewTraining.this, "Zadej čas!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(NewTraining.this, "Zadej datum!", Toast.LENGTH_SHORT).show();
        }
    }
}

