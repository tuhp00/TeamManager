package com.tuhp00.teammanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.tuhp00.teammanager.match.MatchMain;
import com.tuhp00.teammanager.money.MoneyMain;
import com.tuhp00.teammanager.squad.SquadMain;
import com.tuhp00.teammanager.training.TrainingMain;

public class HomeActivity extends AppCompatActivity {

    LinearLayout menuTraining, menuMatch, menuSquad, menuMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        menuTraining = findViewById(R.id.menuTraining);
        menuTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TrainingMain.class);
                startActivity(intent);
            }
        });

        menuMatch = findViewById(R.id.menuMatch);
        menuMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MatchMain.class);
                startActivity(intent);
            }
        });

        menuSquad = findViewById(R.id.menuSquad);
        menuSquad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SquadMain.class);
                startActivity(intent);
            }
        });

        menuMoney = findViewById(R.id.menuMoney);
        menuMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MoneyMain.class);
                startActivity(intent);
            }
        });
    }
}
