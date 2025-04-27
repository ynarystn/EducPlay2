package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import java.io.File
import kotlin.random.Random

class SolvingGame : FullscreenActivity() {

    private lateinit var answerButtons: List<ImageButton>
    private lateinit var answerTexts: List<TextView>
    private lateinit var resultPopup: ImageView
    private var lastWrongButton: ImageButton? = null
    private var correctAnswerIndex: Int = -1
    private var score = 0
    private var questionsAnswered = 0
    private lateinit var stageProgressImage: ImageView
    private var level: Int = 1
    private lateinit var operationIcon: ImageView

    private val isSubtractionLevel: Boolean
        get() = level in 6..10
    private val isMultiplicationLevel: Boolean
        get() = level in 11..15
    private val isDivisionLevel: Boolean
        get() = level in 16..20

    private var dividend: Int = 0
    private var divisor: Int = 0
    private var num1: Int = 0
    private var num2: Int = 0
    private var mul1: Int = 0
    private var mul2: Int = 0

    private val progressImages = arrayOf(
        R.drawable.sg_0_over_5,
        R.drawable.sg_1_over_5,
        R.drawable.sg_2_over_5,
        R.drawable.sg_3_over_5,
        R.drawable.sg_4_over_5,
        R.drawable.sg_5_over_5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solving_game)

        // Disable back button using OnBackPressedDispatcher while in solving
        onBackPressedDispatcher.addCallback(this) {
            // Do nothing — back button disabled
        }

        resultPopup = findViewById(R.id.resultPopup)
        stageProgressImage = findViewById(R.id.h_learning_text)
        level = intent.getIntExtra("LEVEL", 1) // Default to 1 if not passed
        operationIcon = findViewById(R.id.operationIcon)

        answerButtons = listOf(
            findViewById(R.id.answer_button_1),
            findViewById(R.id.answer_button_2),
            findViewById(R.id.answer_button_3),
            findViewById(R.id.answer_button_4),
            findViewById(R.id.answer_button_5)
        )
        answerTexts = listOf(
            findViewById(R.id.answer_text_1),
            findViewById(R.id.answer_text_2),
            findViewById(R.id.answer_text_3),
            findViewById(R.id.answer_text_4),
            findViewById(R.id.answer_text_5)
        )

        answerButtons.forEachIndexed { index, button ->
            button.setSoundClickListener(this) {
                if (index == correctAnswerIndex) {
                    button.setImageResource(R.drawable.sg_button_correct)
                    score++
                    showPopup(true)
                } else {
                    button.setImageResource(R.drawable.sg_button_wrong)
                    lastWrongButton = button
                    showPopup(false)
                }

                questionsAnswered++

                Handler(Looper.getMainLooper()).postDelayed({
                    loadNextQuestion()
                }, 800)
            }
        }

        when (level) {
            in 16..20 -> {
                operationIcon.setImageResource(R.drawable.sg_division_2)
                operationIcon.contentDescription = "Division"
            }
            in 11..15 -> {
                operationIcon.setImageResource(R.drawable.sg_multi)
                operationIcon.contentDescription = "Multiplication"
            }
            in 6..10 -> {
                operationIcon.setImageResource(R.drawable.sg_subtract)
                operationIcon.contentDescription = "Subtraction"
            }
            else -> {
                operationIcon.setImageResource(R.drawable.sg_addition)
                operationIcon.contentDescription = "Addition"
            }
        }

        loadNextQuestion()

        val settingsBtn: ImageButton = findViewById(R.id.button_settings)
        settingsBtn.setSoundClickListener(this) {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        val sgInstructBtn: ImageButton = findViewById(R.id.sg_instruct)
        sgInstructBtn.setSoundClickListener(this) {
            val intent = Intent(this, Instruction::class.java)
            startActivity(intent)
        }
    }

    private fun showPopup(isCorrect: Boolean) {
        resultPopup.setImageResource(if (isCorrect) R.drawable.sg_correct else R.drawable.sg_wrong)
        resultPopup.visibility = ImageView.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            resultPopup.visibility = ImageView.GONE
        }, 1000)
    }
    private fun generateRandomNumber1(): Int = Random.nextInt(1, 1000)
    private fun generateRandomNumber2(): Int = Random.nextInt(1, 1000)
    private fun generateRNMultiplication1(): Int = Random.nextInt(1, 100)
    private fun generateRNMultiplication2(): Int = Random.nextInt(1, 100)

    private fun generateNumberWithDigits(digits: Int): Int {
        val min = Math.pow(10.0, (digits - 1).toDouble()).toInt()
        val max = Math.pow(10.0, digits.toDouble()).toInt() - 1
        return Random.nextInt(min, max + 1)
    }

    private fun loadNextQuestion() {
        if (questionsAnswered >= 5) {
            // ✅ Update visuals one last time
            stageProgressImage.setImageResource(progressImages[score])
            updateStarDisplay(score)

            // ✅ Save score to internal storage
            saveScoreToFile(score)

            // ✅ Show result dialog immediately
            val dialog = LevelResultDialogFragment(score, level)
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "LevelResultDialog")

            return
        }

        lastWrongButton = null

        // ✅ Reset button visuals
        answerButtons.forEach { it.setImageResource(R.drawable.sg_answer_button) }

        // ✅ Generate question

        when {
            isDivisionLevel -> {
                if (level in 16..18) {
                    // 2 digits ÷ 1 digit
                    do {
                        dividend = generateNumberWithDigits(2)
                        divisor = generateNumberWithDigits(1)
                    } while (dividend % divisor != 0 || divisor == 0)
                } else {
                    // Level 19–20: 2 digits ÷ 2 digits
                    do {
                        dividend = generateNumberWithDigits(2)
                        divisor = generateNumberWithDigits(2)
                    } while (dividend % divisor != 0 || divisor == 0)
                }

                findViewById<TextView>(R.id.randomNumberText1).text = dividend.toString()
                findViewById<TextView>(R.id.randomNumberText2).text = divisor.toString()
            }

            isMultiplicationLevel -> {
                mul1 = Random.nextInt(1, 10)
                mul2 = Random.nextInt(1, 10)

                findViewById<TextView>(R.id.randomNumberText1).text = mul1.toString()
                findViewById<TextView>(R.id.randomNumberText2).text = mul2.toString()
            }

            isSubtractionLevel -> {
                if (level in 6..7) {
                    num1 = generateNumberWithDigits(2)
                    num2 = generateNumberWithDigits(2)
                } else if (level == 8) {
                    num1 = generateNumberWithDigits(3)
                    num2 = generateNumberWithDigits(2)
                } else {
                    num1 = generateNumberWithDigits(3)
                    num2 = generateNumberWithDigits(3)
                }

                if (num1 < num2) {
                    val temp = num1
                    num1 = num2
                    num2 = temp
                }

                findViewById<TextView>(R.id.randomNumberText1).text = num1.toString()
                findViewById<TextView>(R.id.randomNumberText2).text = num2.toString()
            }

            else -> { // Addition
                if (level in 1..2) {
                    num1 = generateNumberWithDigits(2)
                    num2 = generateNumberWithDigits(2)
                } else {
                    num1 = generateNumberWithDigits(3)
                    num2 = generateNumberWithDigits(3)
                }

                findViewById<TextView>(R.id.randomNumberText1).text = num1.toString()
                findViewById<TextView>(R.id.randomNumberText2).text = num2.toString()
            }
        }

//        if (isDivisionLevel) {
//            while (true) {
//                val potentialDividend = Random.nextInt(1, 10_000) // 1 to 9999
//                val possibleDivisors = (1..potentialDividend).filter { potentialDividend % it == 0 }
//
//                // Skip if only divisor is 1 (not fun)
//                if (possibleDivisors.size > 1) {
//                    dividend = potentialDividend
//                    divisor = possibleDivisors.random()
//                    break
//                }
//            }
//
//            findViewById<TextView>(R.id.randomNumberText1).text = dividend.toString()
//            findViewById<TextView>(R.id.randomNumberText2).text = divisor.toString()
//        } else if (isMultiplicationLevel) {
//            // For multiplication
//            mul1 = generateRNMultiplication1()
//            mul2 = generateRNMultiplication2()
//
//            findViewById<TextView>(R.id.randomNumberText1).text = mul1.toString()
//            findViewById<TextView>(R.id.randomNumberText2).text = mul2.toString()
//
//        } else {
//            // For addition and subtraction
//            num1 = generateRandomNumber1()
//            num2 = generateRandomNumber2()
//
//            findViewById<TextView>(R.id.randomNumberText1).text = num1.toString()
//            findViewById<TextView>(R.id.randomNumberText2).text = num2.toString()
//        }

        // ✅ Compute correct answer based on the level
        val correctAnswer = when {
            isDivisionLevel -> dividend / divisor
            isMultiplicationLevel -> mul1 * mul2
            isSubtractionLevel -> num1 - num2
            else -> num1 + num2
        }


        // ✅ Generate wrong answers
        val wrongAnswers = mutableSetOf<Int>()
        while (wrongAnswers.size < 4) {
            val wrong = when {
                isDivisionLevel -> Random.nextInt(1, 100)
                isMultiplicationLevel -> Random.nextInt(1, 100)
                isSubtractionLevel -> generateNumberWithDigits(3) - generateNumberWithDigits(2)
                else -> generateNumberWithDigits(3) + generateNumberWithDigits(2)
            }

            if (wrong != correctAnswer) wrongAnswers.add(wrong)
        }


        // ✅ Insert correct answer randomly
        val allAnswers = wrongAnswers.toMutableList()
        correctAnswerIndex = Random.nextInt(5)
        allAnswers.add(correctAnswerIndex, correctAnswer)

        answerTexts.forEachIndexed { index, textView ->
            textView.text = allAnswers[index].toString()
        }

        // ✅ Update progress bar and star image every time
        stageProgressImage.setImageResource(progressImages[score])
        updateStarDisplay(score)
    }

    //sa right side na star to
    private fun updateStarDisplay(score: Int) {
        val starImageView = findViewById<ImageView>(R.id.sg_ver_star_0)
        val starResource = when (score) {
            5 -> R.drawable.sg_ver_star_3
            3, 4 -> R.drawable.sg_ver_star_2
            2 -> R.drawable.sg_ver_star_1
            else -> R.drawable.sg_ver_star_0
        }
        starImageView.setImageResource(starResource)
    }

    // Save the score to internal storage
    private fun saveScoreToFile(score: Int) {
        try {
            val fileName = "level_${level}_score.txt"
            val file = File(getExternalFilesDir(null), fileName)

            val existingScore = if (file.exists()) {
                file.readText().trim().toIntOrNull() ?: 0
            } else {
                0
            }

            // Save only if new score is higher
            if (score > existingScore) {
                file.writeText(score.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}