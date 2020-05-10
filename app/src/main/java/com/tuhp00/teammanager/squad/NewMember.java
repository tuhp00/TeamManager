package com.tuhp00.teammanager.squad;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuhp00.teammanager.R;

public class NewMember extends AppCompatActivity {

    EditText name, surname, phone, email, jerseyNumber;
    Spinner spinnerPosts;
    FloatingActionButton fbSaveMember;
    DatabaseReference databaseMembers;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseMembers = FirebaseDatabase.getInstance().getReference("Members");

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        jerseyNumber = (EditText) findViewById(R.id.jerseyNumber);
        spinnerPosts = (Spinner) findViewById(R.id.spinnerPost);
        fbSaveMember = (FloatingActionButton) findViewById(R.id.fbSaveMember);
        member = new Member();

        fbSaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });
    }

    private void addMember() {
        String namep = name.getText().toString().trim();
        String surnamep = surname.getText().toString().trim();
        String phonep = phone.getText().toString().trim();
        String emailp = email.getText().toString().trim();
        String jnp = jerseyNumber.getText().toString().trim();
        String postp = spinnerPosts.getSelectedItem().toString();
        String id =  databaseMembers.push().getKey();



        // Kontrola zda nejsou prázná pole
        if (!namep.matches("")) {
            if (!surnamep.matches("")) {
                if (!jnp.matches("")) {
                    if (!phonep.matches("")) {
                        int number = Integer.parseInt(jnp);

                        member.setId(id);
                        member.setName(namep);
                        member.setSurname(surnamep);
                        member.setPhone(phonep);
                        member.setJerseyNumber(jnp);
                        member.setPost(postp);
                        member.setEmail(emailp);
                        member.setSuma("0 Kč");

                        if (number <= 9) {
                            String number1 = "0"+String.valueOf(number);
                            member.setNumber(number1);
                        } else {
                            String number2 = String.valueOf(number);
                            member.setNumber(number2);
                        }

                        databaseMembers.child(id).setValue(member);

                        Toast.makeText(NewMember.this, "Člen vytvořen", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        phone.setError("Zadej telefonní číslo!");
                        phone.requestFocus();
                    }
                } else {
                    jerseyNumber.setError("Zadej číslo dresu!");
                    jerseyNumber.requestFocus();
                }
            } else {
                surname.setError("Zadej příjmení!");
                surname.requestFocus();
            }
        } else {
            name.setError("Zadej jméno!");
            name.requestFocus();
        }
    }

}
