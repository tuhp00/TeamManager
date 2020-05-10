package com.tuhp00.teammanager.money;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuhp00.teammanager.R;

public class PenaltyDialog extends AppCompatDialogFragment {

    private EditText dialogCastka;
    private Spinner spinnerPenaltyType;
    private Button btnDatePenalty;

    DatabaseReference databasePenalty;


    private PenaltyDialogListener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_penalty_dialog, null);

        databasePenalty = FirebaseDatabase.getInstance().getReference("Penalty");
        spinnerPenaltyType = (Spinner) view.findViewById(R.id.spinnerPenaltyType);
        btnDatePenalty = (Button) view.findViewById(R.id.btnDatePenalty);
        dialogCastka = view.findViewById(R.id.etValue);
                builder.setView(view)
                .setTitle("Přidat pokutu")
                .setPositiveButton("Potvrdit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String datep = btnDatePenalty.getText().toString().trim();
                        String penaltyp = spinnerPenaltyType.getSelectedItem().toString();
                        String  castka = dialogCastka.getText().toString();
                        listener.addPenalty(datep,penaltyp,castka);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PenaltyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must");
        }
    }

    public interface PenaltyDialogListener {
        void addPenalty(String datep, String penaltyp, String castka);
    }

}
