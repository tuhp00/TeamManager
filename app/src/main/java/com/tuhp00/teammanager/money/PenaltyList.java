package com.tuhp00.teammanager.money;

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

public class PenaltyList extends ArrayAdapter<Penalty> {

    private Activity context;
    private List<Penalty> penaltyList;

    public PenaltyList(Activity context, List<Penalty> penaltyList){
        super(context, R.layout.content_penalty_list, penaltyList);
        this.context = context;
        this.penaltyList = penaltyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.content_penalty_list, null, true);

        TextView tvDatePenalty = (TextView) listViewItem.findViewById(R.id.tvDatePenalty);
        TextView tvPenaltyType = (TextView) listViewItem.findViewById(R.id.tvPenaltyType);
        TextView tvValue = (TextView) listViewItem.findViewById(R.id.tvValue);

        Penalty penalty = penaltyList.get(position);

        tvDatePenalty.setText(penalty.getDate());
        tvPenaltyType.setText(penalty.getPenaltyType());
        tvValue.setText(penalty.getValueStr());

        return listViewItem;
    }
}
