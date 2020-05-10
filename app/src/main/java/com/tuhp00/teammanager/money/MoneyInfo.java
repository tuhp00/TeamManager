package com.tuhp00.teammanager.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;
import com.tuhp00.teammanager.squad.Member;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneyInfo extends AppCompatActivity {

    TextView tvNameMoneyInfo, textView9, tvMoneyInvisible;
    TextView tvSurnameMoneyInfo, tvPayInvisible;
    TextView tvSum;
    ImageButton ibAddPenalty;

    DatabaseReference databaseMoney;
    DatabaseReference databaseCashdesk;
    DatabaseReference databaseMember;
    Penalty penalty;

    ListView listViewPenalty;
    List<Penalty> penaltyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_info);

        tvNameMoneyInfo = (TextView) findViewById(R.id.tvNameMoneyInfo);
        tvSurnameMoneyInfo = (TextView) findViewById(R.id.tvSurnameMoneyInfo);
        listViewPenalty = (ListView) findViewById(R.id.listViewPenalty);

        Intent intent = getIntent();

        penaltyList = new ArrayList<>();

        String name = intent.getStringExtra(MoneyMain.MONEY_NAME);
        String surname = intent.getStringExtra(MoneyMain.MONEY_SURNAME);
        final String idM = intent.getStringExtra(MoneyMain.MONEY_ID);

        databaseMoney = FirebaseDatabase.getInstance().getReference("Penalty").child(idM);
        databaseMember = FirebaseDatabase.getInstance().getReference("Members").child(idM);

        tvNameMoneyInfo.setText(name);
        tvSurnameMoneyInfo.setText(surname);
        penalty = new Penalty();

        ibAddPenalty = (ImageButton) findViewById(R.id.ibAddPenalty);
        ibAddPenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPenaltyDialog(penalty.getDate(), penalty.getPenaltyType(),
                        penalty.getValue());
            }
        });

        textView9 = (TextView) findViewById(R.id.textView9);

        listViewPenalty.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Penalty penalty = penaltyList.get(position);
                openUpdatePenaltyDialog(idM, penalty.getId(), penalty.getValue());
                return false;
            }
        });

        tvSum = (TextView) findViewById(R.id.tvOverall);
    }

    //TODO: Při odečtení poslední pokuty se nezmění stav dluhu u hráče na MoneyMain
    @Override
    protected void onStart() {
        super.onStart();
        databaseMoney.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                penaltyList.clear();
                Intent intent = getIntent();
                String name = intent.getStringExtra(MoneyMain.MONEY_NAME);
                String surname = intent.getStringExtra(MoneyMain.MONEY_SURNAME);
                final String idM = intent.getStringExtra(MoneyMain.MONEY_ID);
                final String jn = intent.getStringExtra(MoneyMain.MONEY_JN);
                String number = intent.getStringExtra(MoneyMain.MONEY_NUMBER);
                String post = intent.getStringExtra(MoneyMain.MONEY_POST);
                String phone = intent.getStringExtra(MoneyMain.MONEY_PHONE);
                String email = intent.getStringExtra(MoneyMain.MONEY_EMAIL);
                Member member = new Member();
                int sum = 0;
                for(DataSnapshot penaltySnapshot : dataSnapshot.getChildren()) {
                    Penalty penaltyY = penaltySnapshot.getValue(Penalty.class);
                    penaltyList.add(penaltyY);

                    int value = penaltySnapshot.child("value").getValue(Integer.class);
                    sum = sum + value;
                    String suma = String.valueOf(sum) + " Kč";

                    tvSum.setText(suma);

                    member.setId(idM);
                    member.setName(name);
                    member.setSurname(surname);
                    member.setJerseyNumber(jn);
                    member.setNumber(number);
                    member.setPost(post);
                    member.setPhone(phone);
                    member.setEmail(email);
                    member.setSuma(suma);
                    databaseMember.setValue(member);
                }
                PenaltyList adapter = new PenaltyList(MoneyInfo.this, penaltyList);
                listViewPenalty.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tvMoneyInvisible = (TextView) findViewById(R.id.tvMoneyInvisible);
        databaseCashdesk = FirebaseDatabase.getInstance().getReference("Cashdesk");

        databaseCashdesk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cashdesk cashdesk = dataSnapshot.getValue(Cashdesk.class);
                String valueX = cashdesk.getValue();
                tvMoneyInvisible.setText(valueX);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void openUpdatePenaltyDialog(final String idMember, final String idPenalty, final int valueUpdate) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_penalty, null);
        dialogBuilder.setView(dialogView);

        final Button btnDeletePenalty= (Button) dialogView.findViewById(R.id.btnDeletePenalty);
        final Button btnPayPenalty= (Button) dialogView.findViewById(R.id.btnPayPenalty);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnDeletePenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePenalty(idMember, idPenalty);

                alertDialog.dismiss();
            }
        });

        btnPayPenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payPenalty(idMember, idPenalty, valueUpdate);

                alertDialog.dismiss();
            }
        });
    }

    public void deletePenalty(String idMem, String idPen) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("Penalty").child(idMem).child(idPen);
        databaseReference.removeValue();
        Toast.makeText(this, "Pokuta odstraněna",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    public void payPenalty(String idMem, String idPen, int value) {
        DatabaseReference dataMoney = FirebaseDatabase.getInstance().getReference("Cashdesk");

        String valueY = tvMoneyInvisible.getText().toString();
        int valueZ = Integer.parseInt(valueY);

        String valueNew = String.valueOf(value + valueZ);

        Cashdesk cashdesk = new Cashdesk();
        cashdesk.setValue(valueNew);
        dataMoney.setValue(cashdesk);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("Penalty").child(idMem).child(idPen);
        databaseReference.removeValue();
        Toast.makeText(this, "Pokuta byla zaplacena",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    public void addPenaltyDialog(String dateUpdate, String typeUpdate, int valueUpdate) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_penalty_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button btnDatePenalty = (Button) dialogView.findViewById(R.id.btnDatePenalty);
        final Spinner spinnerType = (Spinner) dialogView.findViewById(R.id.spinnerPenaltyType);
        final EditText etValue = (EditText) dialogView.findViewById(R.id.etValue);
        etValue.setText("");

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Vybrat datum");
        builder.setSelection(today);

        final MaterialDatePicker datePicker = builder.build();
        btnDatePenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                try {
                    SimpleDateFormat formatOld = new SimpleDateFormat("d. MMM yyyy");

                    Date dateS = formatOld.parse(datePicker.getHeaderText());
                    formatOld.applyPattern("dd-MM-yyyy");
                    String newDate = formatOld.format(dateS);

                    btnDatePenalty.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button btnAddPenalty = (Button) dialogView.findViewById(R.id.btnAddPenalty);

        dialogBuilder.setTitle("Přidat pokutu");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnAddPenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datev = btnDatePenalty.getText().toString().trim();
                String typev = spinnerType.getSelectedItem().toString();
                String valueInt = etValue.getText().toString().trim();



                if (!datev.matches("Vyber datum")) {
                    if(!valueInt.matches("")) {
                        int valuev = Integer.parseInt(valueInt);
                        addPenalty(datev, typev, valuev);
                        alertDialog.dismiss();
                    } else {
                        etValue.setError("Zadej částku!");
                        etValue.requestFocus();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Zadej datum!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void addPenalty(String date, String type, int value) {
        String idv = databaseMoney.push().getKey();
        String datev = date;
        String typev = type;
        int valuev = value;
        String valueString = String.valueOf(value);

        penalty.setDate(datev);
        penalty.setId(idv);
        penalty.setPenaltyType(typev);
        penalty.setValue(valuev);
        penalty.setValueStr(valueString + " Kč");

        databaseMoney.child(idv).setValue(penalty);
    }
}



