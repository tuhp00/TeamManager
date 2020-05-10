package com.tuhp00.teammanager.training;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tuhp00.teammanager.R;

import java.util.List;

public class TrainingList extends ArrayAdapter <Training> {

    private Activity context;
    private List<Training> trainingList;

    public TrainingList(Activity context, List<Training> trainingList) {
        super(context, R.layout.content_training_list, trainingList);
        this.context = context;
        this.trainingList = trainingList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.content_training_list, null, true);

        TextView tvDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView tvTime = (TextView) listViewItem.findViewById(R.id.textViewTime);

        Training training = trainingList.get(position);

        tvDate.setText(training.getDateTraining());
        tvTime.setText(training.getTimeTraining());

        return listViewItem;
    }
}
