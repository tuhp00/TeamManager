package com.tuhp00.teammanager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePicker extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar t = Calendar.getInstance();
        int hour = t.get(Calendar.HOUR_OF_DAY);
        int minute = t.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)
                getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
