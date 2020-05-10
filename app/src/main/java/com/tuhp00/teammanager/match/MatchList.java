package com.tuhp00.teammanager.match;

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

public class MatchList extends ArrayAdapter<Match> {

    private Activity context;
    private List<Match> matchList;

    public MatchList(Activity context, List<Match> matchList) {
        super(context, R.layout.content_match_list, matchList);
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.content_match_list, null, true);

        TextView tvDateMatch = (TextView) listViewItem.findViewById(R.id.tvDateMatch);
        TextView tvTimeMatch = (TextView) listViewItem.findViewById(R.id.tvTimeMatch);
        TextView tvTeams = (TextView) listViewItem.findViewById(R.id.tvTeams);

        Match match = matchList.get(position);

        tvDateMatch.setText(match.getDateMatch());
        tvTimeMatch.setText(match.getTimeMatch());
        tvTeams.setText(match.getMatchTeams());

        return listViewItem;
    }

}
