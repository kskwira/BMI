package com.example.lab02.ui.calories;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab02.databinding.FragmentCaloriesBinding;
import java.text.NumberFormat;

public class CaloriesFragment extends Fragment {

    // number formatter
    private static final NumberFormat numberFormat =
            NumberFormat.getNumberInstance();

    private int weight = 0; // weight entered by the user
    private int height = 0; // height entered by the user
    private int age = 0; // height entered by the user
    private TextView weightTextView; // shows formatted weight
    private TextView heightTextView; // shows formatted height
    private TextView ageTextView; // shows formatted height
    private TextView bmrTextView; // shows calculated BMR
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private FragmentCaloriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        CaloriesViewModel caloriesViewModel =
                new ViewModelProvider(this).get(CaloriesViewModel.class);

    binding = FragmentCaloriesBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textCalories;
        weightTextView = binding.weightTextView;
        heightTextView = binding.heightTextView;
        ageTextView = binding.ageTextView;
        maleRadioButton = binding.maleRadioButton;
        femaleRadioButton = binding.femaleRadioButton;
        bmrTextView = binding.bmrTextView;
        bmrTextView.setText(numberFormat.format(0));
        maleRadioButton.setOnClickListener(genderClickListener);
        femaleRadioButton.setOnClickListener(genderClickListener);

        final EditText weightEditText = binding.weightEditText;
        weightEditText.addTextChangedListener(weightEditTextWatcher);

        final EditText heightEditText = binding.heightEditText;
        heightEditText.addTextChangedListener(heightEditTextWatcher);

        final EditText ageEditText = binding.ageEditText;
        ageEditText.addTextChangedListener(ageEditTextWatcher);

        caloriesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    // calculate and display BMR
    private void calculate() {
        // calculate the BMR
        if (maleRadioButton.isChecked()) {
            double bmr = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
            // display BMR formatted as number with one fraction digits
            numberFormat.setMaximumFractionDigits(1);
            bmrTextView.setText(numberFormat.format(bmr));
        } else if (femaleRadioButton.isChecked()){
            double bmr = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
            // display BMR formatted as number with one fraction digits
            numberFormat.setMaximumFractionDigits(1);
            bmrTextView.setText(numberFormat.format(bmr));
        }
    }

    // listener object for radio button click
    private final View.OnClickListener genderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean checked = ((RadioButton) view).isChecked();
            if (checked) {calculate();}
        }
    };

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

            calculate(); // update the BMR TextView
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
                height = (int) (Double.parseDouble(s.toString()));
                heightTextView.setText(numberFormat.format(height));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                heightTextView.setText("");
                height = 0;
            }

            calculate(); // update the BMR TextView
        }


        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

    // listener object for the EditText's text-changed events
    private final TextWatcher ageEditTextWatcher = new TextWatcher() {
        // called when the user modifies the age
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get weight and display integer formatted value
                age = (int) (Double.parseDouble(s.toString()));
                ageTextView.setText(numberFormat.format(age));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                ageTextView.setText("");
                age = 0;
            }

            calculate(); // update the BMR TextView
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