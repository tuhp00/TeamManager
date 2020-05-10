package com.tuhp00.teammanager.squad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tuhp00.teammanager.R;

public class MemberInfo extends AppCompatActivity {

    TextView tvNameInfo;
    TextView tvSurnameInfo;
    TextView tvJerseyNumberInfo;
    TextView tvPostInfo;
    TextView tvPhoneInfo;
    TextView tvEmailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        tvNameInfo = (TextView) findViewById(R.id.tvNameInfo);
        tvSurnameInfo = (TextView) findViewById(R.id.tvSurnameInfo);
        tvJerseyNumberInfo = (TextView) findViewById(R.id.tvJerseyNumberInfo);
        tvPostInfo = (TextView) findViewById(R.id.tvPostInfo);
        tvPhoneInfo = (TextView) findViewById(R.id.tvPhoneInfo);
        tvEmailInfo = (TextView) findViewById(R.id.tvEmailInfo);

        Intent intent = getIntent();

        String name = intent.getStringExtra(SquadMain.MEMBER_NAME);
        String surname = intent.getStringExtra(SquadMain.MEMBER_SURNAME);
        String jerseyNumber = intent.getStringExtra(SquadMain.MEMBER_JERSEYNUMBER);
        String post = intent.getStringExtra(SquadMain.MEMBER_POST);
        String phone = intent.getStringExtra(SquadMain.MEMBER_PHONE);
        String email = intent.getStringExtra(SquadMain.MEMBER_EMAIL);

        tvNameInfo.setText(name);
        tvSurnameInfo.setText(surname);
        tvJerseyNumberInfo.setText(jerseyNumber);
        tvPostInfo.setText(post);
        tvPhoneInfo.setText(phone);
        tvEmailInfo.setText(email);
    }
}
