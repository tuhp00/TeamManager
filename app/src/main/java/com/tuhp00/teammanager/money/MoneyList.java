package com.tuhp00.teammanager.money;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.squad.Member;

import java.util.List;

public class MoneyList extends ArrayAdapter<Member> {

    private Activity context;
    private List<Member> moneyList;

    public MoneyList(Activity context, List<Member> moneyList){
        super(context, R.layout.content_money_list, moneyList);
        this.context = context;
        this.moneyList = moneyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.content_money_list, null, true);

        TextView tvPenalty = (TextView) listViewItem.findViewById(R.id.tvPenalty);
        TextView tvNameMoney = (TextView) listViewItem.findViewById(R.id.tvNameMoney);
        TextView tvSurnameMoney = (TextView) listViewItem.findViewById(R.id.tvSurnameMoney);

        Member member = moneyList.get(position);

        tvNameMoney.setText(member.getName());
        tvSurnameMoney.setText(member.getSurname());
        tvPenalty.setText(member.getSuma());

        return listViewItem;
    }
}
