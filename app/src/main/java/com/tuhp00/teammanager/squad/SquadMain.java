package com.tuhp00.teammanager.squad;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuhp00.teammanager.R;

import java.util.ArrayList;
import java.util.List;

public class SquadMain extends AppCompatActivity {

    public static final String MEMBER_NAME = "name";
    public static final String MEMBER_SURNAME = "surname";
    public static final String MEMBER_POST = "post";
    public static final String MEMBER_JERSEYNUMBER = "jerseyNumber";
    public static final String MEMBER_PHONE = "phone";
    public static final String MEMBER_EMAIL = "email";

    ListView listViewMembers;
    List<Member> memberList;
    DatabaseReference databaseMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_squad);

        FloatingActionButton fbAddMember = (FloatingActionButton)
                findViewById(R.id.fbAddMember);
        fbAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SquadMain.this, NewMember.class);
                startActivity(in);
            }
        });

        listViewMembers = (ListView) findViewById(R.id.listViewMembers);
        memberList = new ArrayList<>();
        databaseMembers = FirebaseDatabase.getInstance().getReference("Members");

        listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = memberList.get(position);

                Intent intent = new Intent(SquadMain.this.getApplicationContext(), MemberInfo.class);

                intent.putExtra(MEMBER_NAME, member.getName());
                intent.putExtra(MEMBER_SURNAME, member.getSurname());
                intent.putExtra(MEMBER_JERSEYNUMBER, member.getJerseyNumber());
                intent.putExtra(MEMBER_POST, member.getPost());
                intent.putExtra(MEMBER_PHONE, member.getPhone());
                intent.putExtra(MEMBER_EMAIL, member.getEmail());

                startActivity(intent);
            }
        });

        listViewMembers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = memberList.get(position);

                openUpdateMemberDialog(member.getId(), member.getName(), member.getSurname(),
                        member.getJerseyNumber(), member.getPost(), member.getPhone(),
                        member.getEmail(), member.getSuma());
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseMembers.orderByChild("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                memberList.clear();

                for (DataSnapshot memberSnapshot: dataSnapshot.getChildren()) {
                    Member member = memberSnapshot.getValue(Member.class);

                    memberList.add(member);
                }

                MemberList adapter = new MemberList(SquadMain.this, memberList);
                listViewMembers.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SquadMain.this, "ahoj", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUpdateMemberDialog(final String idUpdate, final String nameUpdate,
                                        String surnameUpdate, String jnUpdate,
                                        String postUpdate, String phoneUpdate,
                                        String emailUpdate, final String sumaUpdate) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SquadMain.this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_update_member, null);

        dialogBuilder.setView(dialogView);

        final EditText etNameUpdate = (EditText) dialogView.findViewById(R.id.nameUpdate);
        etNameUpdate.setText(nameUpdate);
        final EditText etSurnameUpdate = (EditText) dialogView.findViewById(R.id.surnameUpdate);
        etSurnameUpdate.setText(surnameUpdate);
        final EditText etJerNumberUpdate = (EditText) dialogView.findViewById(R.id.jerseyNumberUpdate);
        etJerNumberUpdate.setText(jnUpdate);
        final Spinner spinnerPostUpdate = (Spinner) dialogView.findViewById(R.id.spinnerPostUpdate);
        final EditText etPhoneUpdate = (EditText) dialogView.findViewById(R.id.phoneUpdate);
        etPhoneUpdate.setText(phoneUpdate);
        final EditText etEmailUpdate = (EditText) dialogView.findViewById(R.id.emailUpdate);
        etEmailUpdate.setText(emailUpdate);

        final Button btnUpdateMember = (Button) dialogView.findViewById(R.id.btnUpdateMember);
        final Button btnDeleteMember = (Button) dialogView.findViewById(R.id.btnDeleteMember);

        dialogBuilder.setTitle("Upravit člena");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdateMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namev = etNameUpdate.getText().toString().trim();
                String surnamev = etSurnameUpdate.getText().toString().trim();
                String jnv = etJerNumberUpdate.getText().toString().trim();
                String postv = spinnerPostUpdate.getSelectedItem().toString();
                String phonev = etPhoneUpdate.getText().toString().trim();
                String emailv = etEmailUpdate.getText().toString().trim();

                if (!namev.matches("")) {
                    if (!surnamev.matches("")) {
                        if (!jnv.matches("")) {
                            if (!phonev.matches("")) {
                                updateMember(idUpdate, namev, surnamev, jnv, postv, phonev, emailv, sumaUpdate);
                                alertDialog.dismiss();
                            } else {
                                etPhoneUpdate.setError("Zadej telefonní číslo!");
                                etPhoneUpdate.requestFocus();
                            }
                        } else {
                            etJerNumberUpdate.setError("Zadej číslo dresu!");
                            etJerNumberUpdate.requestFocus();
                        }
                    } else {
                        etSurnameUpdate.setError("Zadej příjmení!");
                        etSurnameUpdate.requestFocus();
                    }
                } else {
                    etNameUpdate.setError("Zadej jméno!");
                    etNameUpdate.requestFocus();
                }
            }
        });

        btnDeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(idUpdate);
                alertDialog.dismiss();
            }
        });
    }

    private boolean updateMember(String id, String name, String surname, String jerseyNumber,
                                 String post, String phone, String email, String suma){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Members").child(id);

        Member member = new Member();

        int number = Integer.parseInt(jerseyNumber);
        if (number <= 9) {
            String number1 = "0"+String.valueOf(number);
            member.setNumber(number1);
        } else {
            String number2 = String.valueOf(number);
            member.setNumber(number2);
        }

        member.setId(id);
        member.setName(name);
        member.setSurname(surname);
        member.setJerseyNumber(jerseyNumber);
        member.setPost(post);
        member.setPhone(phone);
        member.setEmail(email);
        member.setSuma(suma);

        databaseReference.setValue(member);
        Toast.makeText(SquadMain.this, "Člen upraven",Toast.LENGTH_SHORT).show();

        return true;
    }

    private void deleteMember (String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Members").child(id);
        DatabaseReference databaseMoney = FirebaseDatabase.getInstance().getReference("Penalty").child(id);

        databaseReference.removeValue();
        databaseMoney.removeValue();

        Toast.makeText(SquadMain.this, "Člen odstraněn",Toast.LENGTH_SHORT).show();
    }
}
