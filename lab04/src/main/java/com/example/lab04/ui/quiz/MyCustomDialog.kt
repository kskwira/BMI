package com.example.lab04.ui.quiz

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.lab04.R;

class MyCustomDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.custom_dialog_message))
            .setPositiveButton(getString(R.string.custom_dialog_button)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "MyCustomDialog"
    }
}