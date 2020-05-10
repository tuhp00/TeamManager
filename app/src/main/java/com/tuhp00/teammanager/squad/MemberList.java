package com.tuhp00.teammanager.squad;

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

public class MemberList extends ArrayAdapter<Member> {

    private Activity context;
    private List<Member> memberList;

    public MemberList(Activity context, List<Member> memberList){
        super(context, R.layout.content_member_list, memberList);
        this.context = context;
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.content_member_list, null, true);

        TextView textViewJerseyNumber = (TextView) listViewItem.findViewById(R.id.textViewJerseyNumber);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewSurname = (TextView) listViewItem.findViewById(R.id.textViewSurname);

        Member member = memberList.get(position);

        textViewJerseyNumber.setText(member.getJerseyNumber());
        textViewName.setText(member.getName());
        textViewSurname.setText(member.getSurname());

        return listViewItem;
    }
}
