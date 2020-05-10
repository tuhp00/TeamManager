package com.tuhp00.teammanager.training;

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

public class TrainingMain extends AppCompatActivity {

    public static final String TRAINING_DATE = "date";
    public static final String TRAINING_TIME = "time";
    public static final String TRAINING_NOTE = "note";

    Button btnTimeUpdate;
    ListView listViewTrainings;
    List<Training> trainingList;
    DatabaseReference databaseTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_training);

        FloatingActionButton fbAddTraining = (FloatingActionButton) findViewById(R.id.fbAddTraining);
        fbAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(TrainingMain.this, NewTraining.class);
                startActivity(in);
            }
        });
        listViewTrainings = (ListView) findViewById(R.id.listViewTrainings);
        trainingList = new ArrayList<>();
        databaseTraining = FirebaseDatabase.getInstance().getReference("Trainings");
        listViewTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Training training = trainingList.get(position);

                Intent intent = new Intent(TrainingMain.this, TrainingInfo.class);

                intent.putExtra(TRAINING_DATE, training.getDateTraining());
                intent.putExtra(TRAINING_TIME, training.getTimeTraining());
                intent.putExtra(TRAINING_NOTE, training.getNoteTraining());

                startActivity(intent);
            }
        });

        listViewTrainings.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Training training = trainingList.get(position);

                openUpdateTrainingDialog(training.getDateTraining(), training.getTimeTraining(),
                        training.getId(), training.getNoteTraining());
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseTraining.orderByChild("dateForOrderTraining").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trainingList.clear();
                for (DataSnapshot trainingSnapshot: dataSnapshot.getChildren()) {
                    Training training = trainingSnapshot.getValue(Training.class);

                    trainingList.add(training);
                }
                TrainingList adapter = new TrainingList(TrainingMain.this, trainingList);
                listViewTrainings.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TrainingMain.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUpdateTrainingDialog(String dateUpdate, String timeUpdate, final String idUpdate, String noteUpdate) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TrainingMain.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_training_update, null);
        dialogBuilder.setView(dialogView);
        final Button btnDateUpdate = (Button) dialogView.findViewById(R.id.btnUpdateDateTraining);
        btnDateUpdate.setText(dateUpdate);

        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder();
        constraints.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Vyber datum");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraints.build());
        final MaterialDatePicker datePicker = builder.build();
        btnDateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(TrainingMain.this.getSupportFragmentManager(), "DATE_PICKER");
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

                    btnDateUpdate.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnTimeUpdate = (Button) dialogView.findViewById(R.id.btnUpdateTimeTraining);
        btnTimeUpdate.setText(timeUpdate);
        btnTimeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        final EditText etNoteUpdate = (EditText) dialogView.findViewById(R.id.etUpdateNoteTraining);
        etNoteUpdate.setText(noteUpdate);

        final Button btnUpdateTraining = (Button) dialogView.findViewById(R.id.btnUpdateTraining);
        final Button btnDeleteTraining = (Button) dialogView.findViewById(R.id.btnDeleteTraining);

        dialogBuilder.setTitle("Upravit trénink");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdateTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datev = btnDateUpdate.getText().toString().trim();
                String timev = btnTimeUpdate.getText().toString().trim();
                String notev = etNoteUpdate.getText().toString().trim();

                updateTraining(datev, timev, idUpdate, notev);

                alertDialog.dismiss();
            }
        });

        btnDeleteTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTraining(idUpdate);

                alertDialog.dismiss();
            }
        });
    }

    private void timePicker(){
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(TrainingMain.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btnTimeUpdate.setText(String.format("%02d:%02d",hourOfDay,minute));
            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    private void deleteTraining(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trainings").child(id);
        databaseReference.removeValue();
        Toast.makeText(TrainingMain.this, "Trénink odstraněn",Toast.LENGTH_SHORT).show();
    }

    private boolean updateTraining(String date, String time, String id, String note){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trainings").child(id);

        Training training = new Training();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date dateOrder = dateFormat.parse(date);
            dateFormat.applyPattern("yyyy-MM-dd");

            String dateForOrder = dateFormat.format(dateOrder);
            training.setDateForOrderTraining(dateForOrder + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        training.setDateTraining(date);
        training.setTimeTraining(time);
        training.setNoteTraining(note);
        training.setId(id);

        databaseReference.setValue(training);

        Toast.makeText(TrainingMain.this, "Trénink upraven",Toast.LENGTH_SHORT).show();

        return true;
    }

}
