package com.example.lab04.ui.calories

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab04.databinding.FragmentCaloriesBinding
import java.text.NumberFormat

class CaloriesFragment : Fragment() {

    private var _binding: FragmentCaloriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val numberFormat = NumberFormat.getNumberInstance()
    private var weight = 0 // weight entered by the user
    private var height = 0 // height entered by the user
    private var age = 0 // age entered by the user
    private lateinit var weightTextView : TextView // shows formatted weight
    private lateinit var heightTextView : TextView // shows formatted height
    private lateinit var ageTextView : TextView // shows formatted height
    private lateinit var bmrTextView : TextView // shows calculated BMI
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val caloriesViewModel =
            ViewModelProvider(this)[CaloriesViewModel::class.java]

        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        weightTextView = binding.weightTextView
        heightTextView = binding.heightTextView
        ageTextView = binding.ageTextView
        maleRadioButton = binding.maleRadioButton
        femaleRadioButton = binding.femaleRadioButton
        bmrTextView = binding.bmrTextView

        bmrTextView.text = numberFormat.format(0)
        maleRadioButton.setOnClickListener(genderClickListener)
        femaleRadioButton.setOnClickListener(genderClickListener)

        val weightEditText: EditText = binding.weightEditText
        weightEditText.addTextChangedListener(weightEditTextWatcher)

        val heightEditText: EditText = binding.heightEditText
        heightEditText.addTextChangedListener(heightEditTextWatcher)

        val ageEditText: EditText = binding.ageEditText
        ageEditText.addTextChangedListener(ageEditTextWatcher)

        val textView: TextView = binding.textCalories
        caloriesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    // calculate and display BMR
    private fun calculate() {
        // calculate the BMR
        if (maleRadioButton.isChecked) {
            val bmr = 66.5 + 13.75 * weight + 5.003 * height - 6.75 * age
            // display BMR formatted as number with one fraction digits
            numberFormat.maximumFractionDigits = 1
            bmrTextView.text = numberFormat.format(bmr)
        } else if (femaleRadioButton.isChecked) {
            val bmr = 655.1 + 9.563 * weight + 1.850 * height - 4.676 * age
            // display BMR formatted as number with one fraction digits
            numberFormat.maximumFractionDigits = 1
            bmrTextView.text = numberFormat.format(bmr)
        }
    }

    // listener object for radio button click
    private val genderClickListener =
        View.OnClickListener { view ->
            val checked = (view as RadioButton).isChecked
            if (checked) {
                calculate()
            }
        }

    // listener object for the weight EditText's text-changed events
    private val weightEditTextWatcher: TextWatcher = object : TextWatcher {
        // called when the user modifies the weight
        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            try { // get weight and display integer formatted value
                weight = s.toString().toDouble().toInt()
                weightTextView.text = numberFormat.format(weight.toLong())
            } catch (e: NumberFormatException) { // if s is empty or non-numeric
                weightTextView.text = ""
                weight = 0
            }
            calculate() // update the BMR TextView
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int, after: Int
        ) {
        }
    }

    // listener object for the EditText's text-changed events
    private val heightEditTextWatcher: TextWatcher = object : TextWatcher {
        // called when the user modifies the height
        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            try { // get height and display double formatted value
                height = s.toString().toDouble().toInt()
                heightTextView.text = numberFormat.format(height.toLong())
            } catch (e: NumberFormatException) { // if s is empty or non-numeric
                heightTextView.text = ""
                height = 0
            }
            calculate() // update the BMR TextView
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int, after: Int
        ) {
        }
    }

    // listener object for the EditText's text-changed events
    private val ageEditTextWatcher: TextWatcher = object : TextWatcher {
        // called when the user modifies the age
        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            try { // get weight and display integer formatted value
                age = s.toString().toDouble().toInt()
                ageTextView.text = numberFormat.format(age.toLong())
            } catch (e: NumberFormatException) { // if s is empty or non-numeric
                ageTextView.text = ""
                age = 0
            }
            calculate() // update the BMR TextView
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int, after: Int
        ) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
