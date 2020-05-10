package com.tuhp00.teammanager.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.training.Training;

public class CashdeskInfo extends AppCompatActivity {

    TextView tvMoney, btnAddMoney, btnDeleteMoney;
    DatabaseReference databaseCashdesk;
    Cashdesk cashdesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashdesk_info);

        tvMoney = (TextView) findViewById(R.id.tvMoney);
        btnAddMoney = (TextView) findViewById(R.id.btnAddMoney);
        btnDeleteMoney = (TextView) findViewById(R.id.btnDeleteMoney);

        cashdesk = new Cashdesk();
        databaseCashdesk = FirebaseDatabase.getInstance().getReference("Cashdesk");
        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMoneyDialog();
            }
        });
        btnDeleteMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteMoneyDialog();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseCashdesk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cashdesk cashdesk = dataSnapshot.getValue(Cashdesk.class);
                String valueX = cashdesk.getValue();
                tvMoney.setText(valueX);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openAddMoneyDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_money, null);
        dialogBuilder.setView(dialogView);

        Button btnMoney = (Button) dialogView.findViewById(R.id.btnMoney);
        final EditText etMoney = (EditText) dialogView.findViewById(R.id.etMoney);

        dialogBuilder.setTitle("Přidat peníze");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueY = etMoney.getText().toString().trim();

                if (valueY.matches("")){
                    etMoney.setError("Zadej částku!");
                    etMoney.requestFocus();
                } else {
                    addMoney(valueY);
                    alertDialog.dismiss();
                }
            }
        });
    }


    public void addMoney(String value) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cashdesk");

        String valueOld = tvMoney.getText().toString();
        int valueOldInt = Integer.parseInt(valueOld);
        int valueNewInt = Integer.parseInt(value);

        String valueNew = String.valueOf(valueOldInt + valueNewInt);

        cashdesk = new Cashdesk();

        cashdesk.setValue(valueNew);
        databaseReference.setValue(cashdesk);
        Toast.makeText(this, "Částka byla přidána do pokladny",Toast.LENGTH_SHORT).show();
    }

    public void openDeleteMoneyDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_money, null);
        dialogBuilder.setView(dialogView);

        Button btnMoney = (Button) dialogView.findViewById(R.id.btnMoney);
        final EditText etMoney = (EditText) dialogView.findViewById(R.id.etMoney);

        dialogBuilder.setTitle("Odebrat peníze");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueY = etMoney.getText().toString().trim();

                if (valueY.matches("")){
                    etMoney.setError("Zadej částku!");
                    etMoney.requestFocus();
                } else {
                    deleteMoney(valueY);
                    alertDialog.dismiss();
                }
            }
        });
    }

    public void deleteMoney(String value) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cashdesk");

        String valueOld = tvMoney.getText().toString();
        int valueOldInt = Integer.parseInt(valueOld);
        int valueNewInt = Integer.parseInt(value);
        int valueSum = valueOldInt - valueNewInt;

        if (valueSum < 0) {
            Toast.makeText(this, "Taková částka v pokladně není",Toast.LENGTH_LONG).show();
        } else {
            String valueNew = String.valueOf(valueOldInt - valueNewInt);

            cashdesk = new Cashdesk();

            cashdesk.setValue(valueNew);
            databaseReference.setValue(cashdesk);
            Toast.makeText(this, "Částka byla odebrána z pokladny",Toast.LENGTH_LONG).show();
        }
    }
}


