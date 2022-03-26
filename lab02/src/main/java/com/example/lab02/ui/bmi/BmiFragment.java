package com.example.lab02.ui.bmi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab02.databinding.FragmentBmiBinding;
import java.text.NumberFormat;

public class BmiFragment extends Fragment {

    // number formatter
    private static final NumberFormat numberFormat =
            NumberFormat.getNumberInstance();

    private int weight = 0; // weight entered by the user
    private double height = 0.0; // height entered by the user
    private TextView weightTextView; // shows formatted weight
    private TextView heightTextView; // shows formatted height
    private TextView bmiTextView; // shows calculated BMI

    private FragmentBmiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        BmiViewModel bmiViewModel =
                new ViewModelProvider(this).get(BmiViewModel.class);

    binding = FragmentBmiBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textBmi;
        weightTextView = binding.weightTextView;
        heightTextView = binding.heightTextView;
        bmiTextView = binding.bmiTextView;
        bmiTextView.setText(numberFormat.format(0));

        final EditText weightEditText = binding.weightEditText;
        weightEditText.addTextChangedListener(weightEditTextWatcher);

        final EditText heightEditText = binding.heightEditText;
        heightEditText.addTextChangedListener(heightEditTextWatcher);

        bmiViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    // calculate and display BMI
    private void calculate() {
        // calculate the BMI
        double bmi = weight / (height * height);

        // display BMI formatted as number with two fraction digits
        numberFormat.setMaximumFractionDigits(2);
        bmiTextView.setText(numberFormat.format(bmi));
    }

    // listener object for the weight EditText's text-changed events
    private final TextWatcher weightEditTextWatcher = new TextWatcher() {
        // called when the user modifies the weight
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get weight and display integer formatted value
                weight = (int) (Double.parseDouble(s.toString()));
                weightTextView.setText(numberFormat.format(weight));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                weightTextView.setText("");
                weight = 0;
            }

            calculate(); // update the BMI TextView
        }


        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

    // listener object for the EditText's text-changed events
    private final TextWatcher heightEditTextWatcher = new TextWatcher() {
        // called when the user modifies the height
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get height and display double formatted value
                height = Double.parseDouble(s.toString()) / 100.0;
                heightTextView.setText(numberFormat.format(height));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                heightTextView.setText("");
                height = 0.0;
            }

            calculate(); // update the BMI TextView
        }


        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}