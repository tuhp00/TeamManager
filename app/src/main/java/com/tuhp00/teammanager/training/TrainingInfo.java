package com.tuhp00.teammanager.training;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tuhp00.teammanager.R;

public class TrainingInfo extends AppCompatActivity {

    TextView tvDateTrainingInfo;
    TextView tvTimeTrainingInfo;
    TextView tvNoteTrainingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_info);

        tvDateTrainingInfo = (TextView) findViewById(R.id.tvDateTrainingInfo);
        tvTimeTrainingInfo = (TextView) findViewById(R.id.tvTimeTrainingInfo);
        tvNoteTrainingInfo = (TextView) findViewById(R.id.tvNoteTrainingInfo);

        Intent intent = getIntent();

        String date = intent.getStringExtra(TrainingMain.TRAINING_DATE);
        String time = intent.getStringExtra(TrainingMain.TRAINING_TIME);
        String note = intent.getStringExtra(TrainingMain.TRAINING_NOTE);

        tvDateTrainingInfo.setText(date);
        tvTimeTrainingInfo.setText(time);
        tvNoteTrainingInfo.setText(note);
    }
}
