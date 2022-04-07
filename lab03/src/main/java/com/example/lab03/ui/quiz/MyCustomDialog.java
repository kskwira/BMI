package com.example.lab03.ui.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lab03.R;

public class MyCustomDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.custom_dialog_message))
                .setPositiveButton(getString(R.string.custom_dialog_button), (dialog, which) -> {} )
                .create();
    }

    public static String TAG = "MyCustomDialog";
}
