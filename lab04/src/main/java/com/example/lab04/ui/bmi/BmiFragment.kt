package com.example.lab04.ui.bmi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab04.databinding.FragmentBmiBinding
import java.text.NumberFormat

class BmiFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val numberFormat = NumberFormat.getNumberInstance()
    private var weight = 0 // weight entered by the user
    private var height = 0.0 // height entered by the user
    private lateinit var weightTextView : TextView // shows formatted weight
    private lateinit var heightTextView : TextView // shows formatted height
    private lateinit var bmiTextView : TextView // shows calculated BMI

    private val unEncodedHtml = """  
<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Month',    'Target BMI', 'Your BMI'],
          ['October',   24.34,        25.02],
          ['November',  24.34,        24.87],
          ['December',  24.34,        24.90],
          ['January',   24.34,        24.66],
          ['February',  24.34,        24.46],
          ['March',     24.34,        24.40]
        ]);

        var options = {
          title: 'BMI over last 6 months',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
    <div id="curve_chart" style="width: 420px; height: 250px"></div>
  </body>
</html>
"""

    private val encodedHtml = Base64.encodeToString(unEncodedHtml.toByteArray(), Base64.NO_PADDING)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        weightTextView = binding.weightTextView
        heightTextView = binding.heightTextView
        bmiTextView = binding.bmiTextView
        bmiTextView.text = numberFormat.format(0)

        val bmiWebView: WebView = binding.bmiWebView
        bmiWebView.settings.javaScriptEnabled = true
        bmiWebView.loadData(encodedHtml, "text/html", "base64")

        val weightEditText: EditText = binding.weightEditText
        weightEditText.addTextChangedListener(weightEditTextWatcher)

        val heightEditText: EditText = binding.heightEditText
        heightEditText.addTextChangedListener(heightEditTextWatcher)

        return root
    }

    // calculate and display BMI
    private fun calculate() {
        // calculate the BMI
        val bmi = weight / (height * height)

        // display BMI formatted as number with two fraction digits
        numberFormat.maximumFractionDigits = 2
        bmiTextView.text = numberFormat.format(bmi)
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
            calculate() // update the BMI TextView
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
                height = s.toString().toDouble() / 100.0
                heightTextView.text = numberFormat.format(height)
            } catch (e: NumberFormatException) { // if s is empty or non-numeric
                heightTextView.text = ""
                height = 0.0
            }
            calculate() // update the BMI TextView
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