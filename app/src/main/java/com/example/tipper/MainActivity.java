// BMI calculator
// Based on the Tip Calculator by Deitel & Associates, Inc. and Pearson Education, Inc.
// Author: Krzysztof Skwira

package com.example.tipper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.widget.EditText; // for weight/height input
import android.widget.TextView; // for displaying text
import java.text.NumberFormat; // for number formatting

public class MainActivity extends AppCompatActivity {

    // number formatter
    private static final NumberFormat numberFormat =
            NumberFormat.getNumberInstance();

    private int weight = 0; // weight entered by the user
    private double height = 0.0; // height entered by the user
    private TextView weightTextView; // shows formatted weight
    private TextView heightTextView; // shows formatted height
    private TextView bmiTextView; // shows calculated BMI

    // called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call superclass onCreate
        setContentView(R.layout.activity_main); // inflate the GUI

        // get references to programmatically manipulated TextViews
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        bmiTextView.setText(numberFormat.format(0));

        // set weightEditText's TextWatcher
        EditText amountEditText =
                findViewById(R.id.weightEditText);
        amountEditText.addTextChangedListener(weightEditTextWatcher);

        // set heightEditText's TextWatcher
        EditText heightEditText =
                findViewById(R.id.heightEditText);
        heightEditText.addTextChangedListener(heightEditTextWatcher);
    }

    // calculate and display BMI
    private void calculate() {
        // calculate the BMI
        double bmi = weight / (height * height);

        // display BMI formatted as number with two fraction digits
        numberFormat.setMaximumFractionDigits(2);
        bmiTextView.setText(numberFormat.format(bmi));
    }

    // listener object for the EditText's text-changed events
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
}


/*************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
