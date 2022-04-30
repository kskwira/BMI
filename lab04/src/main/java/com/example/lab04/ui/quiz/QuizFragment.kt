package com.example.lab04.ui.quiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lab04.R
import com.example.lab04.databinding.FragmentQuizBinding
import java.io.IOException
import java.security.SecureRandom
import kotlin.math.max

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null

    private val binding get() = _binding!!

    private val TAG = "FlagQuiz Activity"
    private val flagsInQuiz = 10

    private lateinit var fileNameList : ArrayList<String> // flag file names
    private lateinit var quizCountriesList : ArrayList<String>
    private val regionsSet : Set<String> = setOf("Africa", "Asia","Europe", "North_America", "Oceania", "South_America") // world regions in current quiz
    private lateinit var correctAnswer : String // correct country for the current flag
    private var totalGuesses = 0 // number of guesses made
    private var correctAnswers = 0 // number of correct guesses
    private var guessRows = 0 // number of rows displaying guess Buttons
    private var random : SecureRandom? = null // used to randomize the quiz
    private var handler : Handler? = null // used to delay loading next flag
    private lateinit var shakeAnimation : Animation // animation for incorrect guess

    private var quizLinearLayout : LinearLayout? = null // layout that contains the quiz
    private lateinit var questionNumberTextView : TextView // shows current question #
    private var flagImageView : ImageView? = null // displays a flag
    private lateinit var guessLinearLayouts : Array<LinearLayout?>  // rows of answer Buttons
    private var answerTextView : TextView? = null // displays correct answer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fileNameList = ArrayList()
        quizCountriesList = ArrayList()
        random = SecureRandom()
        handler = Handler()

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(
            activity,
            R.anim.incorrect_shake
        )
        shakeAnimation.repeatCount = 3 // animation repeats 3 times

        quizLinearLayout = binding.quizLinearLayout
        questionNumberTextView = binding.questionNumberTextView
        flagImageView = binding.flagImageView
        guessLinearLayouts = arrayOfNulls(3)
        guessLinearLayouts[0] = binding.row1LinearLayout
        guessLinearLayouts[1] = binding.row2LinearLayout
        guessLinearLayouts[2] = binding.row3LinearLayout
        answerTextView = binding.answerTextView

        // set the number of possible answer rows - value between 1 and 3
        guessRows = 2

        // hide all quess button LinearLayouts
        for (layout in guessLinearLayouts) layout?.visibility = View.GONE

        // display appropriate guess button LinearLayouts
        for (row in 0 until guessRows) guessLinearLayouts[row]?.visibility = View.VISIBLE

        // configure listeners for the guess Buttons
        for (row in guessLinearLayouts) {
            for (column in 0 until row!!.childCount) {
                val button = row.getChildAt(column) as Button
                button.setOnClickListener(guessButtonListener)
            }
        }

        // set questionNumberTextView's text
        questionNumberTextView.text =
            getString(R.string.question, 1, flagsInQuiz)

        resetQuiz()

        return root
    }

    // set up and start the next quiz
    private fun resetQuiz() {
        // use AssetManager to get image file names for enabled regions
        val assets = requireActivity().assets
        fileNameList.clear() // empty list of image file names
        try {
            // loop through each region
            for (region in regionsSet) {
                // get a list of all flag image files in this region
                val paths = assets.list(region)
                for (path in paths!!) fileNameList.add(path.replace(".png", ""))
            }
        } catch (exception: IOException) {
            Log.e(
                TAG,
                "Error loading image file names",
                exception
            )
        }
        correctAnswers = 0 // reset the number of correct answers made
        totalGuesses = 0 // reset the total number of guesses the user made
        quizCountriesList.clear() // clear prior list of quiz countries
        var flagCounter = 1
        val numberOfFlags = fileNameList.size

        // add FLAGS_IN_QUIZ random file names to the quizCountriesList
        while (flagCounter <= flagsInQuiz) {
            val randomIndex = random!!.nextInt(numberOfFlags)

            // get the random file name
            val filename = fileNameList[randomIndex]

            // if the region is enabled and it hasn't already been chosen
            if (!quizCountriesList.contains(filename)) {
                quizCountriesList.add(filename) // add the file to the list
                ++flagCounter
            }
        }
        loadNextFlag() // start the quiz by loading the first flag
    }

    // after the user guesses a correct flag, load the next flag
    private fun loadNextFlag() {
        // get file name of the next flag and remove it from the list
        val nextImage: String = quizCountriesList.removeAt(0)
        correctAnswer = nextImage // update the correct answer
        answerTextView!!.text = "" // clear answerTextView

        // display current question number
        questionNumberTextView.text = getString(
            R.string.question,
            correctAnswers + 1,
            flagsInQuiz
        )

        // extract the region from the next image's name
        val region = nextImage.substring(0, nextImage.indexOf('-'))

        // use AssetManager to load next image from assets folder
        val assets = requireActivity().assets

        // get an InputStream to the asset representing the next flag
        // and try to use the InputStream
        try {
            assets.open("$region/$nextImage.png").use { stream ->
                // load the asset as a Drawable and display on the flagImageView
                val flag =
                    Drawable.createFromStream(stream, nextImage)
                flagImageView!!.setImageDrawable(flag)
                animate(false) // animate the flag onto the screen
            }
        } catch (exception: IOException) {
            Log.e(
                TAG,
                "Error loading $nextImage", exception
            )
        }
        fileNameList.shuffle() // shuffle file names

        // put the correct answer at the end of fileNameList
        val correct = fileNameList.indexOf(correctAnswer)
        fileNameList.add(fileNameList.removeAt(correct))

        // add 2, 4, 6 or 8 guess Buttons based on the value of guessRows
        for (row in 0 until guessRows) {
            // place Buttons in currentTableRow
            for (column in 0 until guessLinearLayouts[row]!!.childCount) {
                // get reference to Button to configure
                val newGuessButton = guessLinearLayouts[row]?.getChildAt(column) as Button
                newGuessButton.isEnabled = true

                // get country name and set it as newGuessButton's text
                val filename = fileNameList[row * 2 + column]
                newGuessButton.text = getCountryName(filename)
            }
        }

        // randomly replace one Button with the correct answer
        val row = random!!.nextInt(guessRows) // pick random row
        val column = random!!.nextInt(2) // pick random column
        val randomRow = guessLinearLayouts[row] // get the row
        val countryName: String = getCountryName(correctAnswer)
        (randomRow?.getChildAt(column) as Button).text = countryName
    }

    // parses the country flag file name and returns the country name
    private fun getCountryName(name: String): String {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ')
    }

    // animates the entire quizLinearLayout on or off screen
    private fun animate(animateOut: Boolean) {
        // prevent animation into the the UI for the first flag
        if (correctAnswers == 0) return

        // calculate center x and center y
        val centerX = (quizLinearLayout!!.left +
                quizLinearLayout!!.right) / 2 // calculate center x
        val centerY = (quizLinearLayout!!.top +
                quizLinearLayout!!.bottom) / 2 // calculate center y

        // calculate animation radius
        val radius = max(
            quizLinearLayout!!.width,
            quizLinearLayout!!.height
        )
        val animator: Animator

        // if the quizLinearLayout should animate out rather than in
        if (animateOut) {
            // create circular reveal animation
            animator = ViewAnimationUtils.createCircularReveal(
                quizLinearLayout, centerX, centerY, radius.toFloat(), 0f
            )
            animator.addListener(
                object : AnimatorListenerAdapter() {
                    // called when the animation finishes
                    override fun onAnimationEnd(animation: Animator) {
                        loadNextFlag()
                    }
                }
            )
        } else { // if the quizLinearLayout should animate in
            animator = ViewAnimationUtils.createCircularReveal(
                quizLinearLayout, centerX, centerY, 0f, radius.toFloat()
            )
        }
        animator.duration = 500 // set animation duration to 500 ms
        animator.start() // start the animation
    }

    // called when a guess Button is touched
    private val guessButtonListener =
        View.OnClickListener { v ->
            val guessButton = v as Button
            val guess = guessButton.text.toString()
            val answer = getCountryName(correctAnswer)
            ++totalGuesses // increment number of guesses the user has made
            if (guess == answer) { // if the guess is correct
                ++correctAnswers // increment the number of correct answers

                // display correct answer in green text
                answerTextView!!.text = "$answer!"
                answerTextView!!.setTextColor(
                    resources.getColor(
                        R.color.correct_answer,
                        requireContext().theme
                    )
                )
                disableButtons() // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == flagsInQuiz) {
                    // AlertDialog to display quiz stats and start new quiz
                    val alertDialogBuilder = AlertDialog.Builder(activity)
                    alertDialogBuilder.setMessage(
                        getString(
                            R.string.results,
                            totalGuesses,
                            1000 / totalGuesses.toDouble()
                        )
                    )
                        .setPositiveButton(R.string.reset_quiz
                        ) { _, _ -> resetQuiz() }
                        .setTitle("quiz results")

                    val alertDialog : AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    // optional CustomDialog, can be substituted for the above AlertDialog
//                    MyCustomDialog()
//                        .show(childFragmentManager, MyCustomDialog.TAG)

                } else { // answer is correct but quiz is not over
                    // load the next flag after a 2-second delay
                    handler!!.postDelayed(
                        {
                            animate(true) // animate the flag off the screen
                        }, 2000
                    ) // 2000 milliseconds for 2-second delay
                }
            } else { // answer was incorrect
                flagImageView!!.startAnimation(shakeAnimation) // play shake

                // display "Incorrect!" in red
                answerTextView!!.setText(R.string.incorrect_answer)
                answerTextView!!.setTextColor(
                    resources.getColor(
                        R.color.incorrect_answer, requireContext().theme
                    )
                )
                guessButton.isEnabled = false // disable incorrect answer
            }
        }

    // utility method that disables all answer Buttons
    private fun disableButtons() {
        for (row in 0 until guessRows) {
            val guessRow = guessLinearLayouts[row]
            for (i in 0 until guessRow!!.childCount) guessRow.getChildAt(i).isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}