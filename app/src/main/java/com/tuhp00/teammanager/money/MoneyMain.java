package com.tuhp00.teammanager.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.squad.Member;

import java.util.ArrayList;
import java.util.List;

public class MoneyMain extends AppCompatActivity {

    public static final String MONEY_NAME = "name";
    public static final String MONEY_SURNAME = "surname";
    public static final String MONEY_ID = "id";
    public static final String MONEY_JN = "jerseyNumber";
    public static final String MONEY_NUMBER = "number";
    public static final String MONEY_POST = "post";
    public static final String MONEY_PHONE = "phone";
    public static final String MONEY_EMAIL = "email";

    Button btnShowCashdesk;
    ListView listViewMoney;
    List<Member> moneyList;
    DatabaseReference databaseMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_money);

        listViewMoney = (ListView) findViewById(R.id.listViewMoney);
        moneyList = new ArrayList<>();
        databaseMoney = FirebaseDatabase.getInstance().getReference("Members");

        listViewMoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = moneyList.get(position);

                Intent intent = new Intent(MoneyMain.this, MoneyInfo.class);

                intent.putExtra(MONEY_NAME, member.getName());
                intent.putExtra(MONEY_SURNAME, member.getSurname());
                intent.putExtra(MONEY_JN, member.getJerseyNumber());
                intent.putExtra(MONEY_NUMBER, member.getNumber());
                intent.putExtra(MONEY_POST, member.getPost());
                intent.putExtra(MONEY_PHONE, member.getPhone());
                intent.putExtra(MONEY_EMAIL, member.getEmail());
                intent.putExtra(MONEY_ID, member.getId());

                startActivity(intent);
            }
        });

        btnShowCashdesk = (Button) findViewById(R.id.btnShowCashdesk);
        btnShowCashdesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MoneyMain.this, CashdeskInfo.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseMoney.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moneyList.clear();
                for (DataSnapshot moneySnapshot: dataSnapshot.getChildren()) {
                    Member member = moneySnapshot.getValue(Member.class);

                    moneyList.add(member);
                }

                MoneyList adapter = new MoneyList(MoneyMain.this, moneyList);
                listViewMoney.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
