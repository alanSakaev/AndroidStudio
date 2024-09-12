package com.example.englishwordsapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.englishwordsapp.databinding.ActivityLearnWordBinding
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    //  private lateinit var binding: ActivityLearnWordBinding

    private var _binding: ActivityLearnWordBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityLearnWord must be not null")

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityLearnWordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val trainer = LearnWordsTrainer()
        showNextQuestions(trainer)

        with(binding) {
            btnContinue.setOnClickListener {
                layoutResult.isVisible = false
                markAnswerNeutral(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                markAnswerNeutral(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                markAnswerNeutral(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                markAnswerNeutral(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                showNextQuestions(trainer)
            }

            btnSkip.setOnClickListener {
                showNextQuestions(trainer)
            }
        }
    }

    private fun showNextQuestions(trainer: LearnWordsTrainer) {
        val firstQuestion: Question? = trainer.getNextQuestion()

        with(binding) {
            if (firstQuestion == null || firstQuestion.variants.size < NUMBER_OF_ANSWERS) {
                tvQuestionWord.isVisible = false
                layoutVariants.isVisible = false
                btnContinue.text = "Complete"
            } else {
                btnSkip.isVisible = true
                tvQuestionWord.isVisible = true

                tvQuestionWord.text = firstQuestion.correctAnswer.original

                tvVariantValue1.text = firstQuestion.variants[0].translate
                tvVariantValue2.text = firstQuestion.variants[1].translate
                tvVariantValue3.text = firstQuestion.variants[2].translate
                tvVariantValue4.text = firstQuestion.variants[3].translate

                layoutAnswer1.setOnClickListener {
                    if(trainer.checkAnswer(0)) {
                        markAnswerCorrect(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                        showResultMessage(true)
                    } else {
                        markAnswerWrong(layoutAnswer1, tvVariantNumber1, tvVariantValue1)
                        showResultMessage(false)
                    }
                }

                layoutAnswer2.setOnClickListener {
                    if(trainer.checkAnswer(1)) {
                        markAnswerCorrect(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                        showResultMessage(true)
                    } else {
                        markAnswerWrong(layoutAnswer2, tvVariantNumber2, tvVariantValue2)
                        showResultMessage(false)
                    }
                }

                layoutAnswer3.setOnClickListener {
                    if(trainer.checkAnswer(2)) {
                        markAnswerCorrect(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                        showResultMessage(true)
                    } else {
                        markAnswerWrong(layoutAnswer3, tvVariantNumber3, tvVariantValue3)
                        showResultMessage(false)
                    }
                }

                layoutAnswer4.setOnClickListener {
                    if (trainer.checkAnswer(3)) {
                        markAnswerCorrect(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                        showResultMessage(true)
                    } else {
                        markAnswerWrong(layoutAnswer4, tvVariantNumber4, tvVariantValue4)
                        showResultMessage(false)
                    }
                }
            }
        }
    }

    private fun markAnswerNeutral(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantValue: TextView) {

        layoutAnswer.background = ContextCompat.getDrawable(
            this@MainActivity, R.drawable.shape_rounded_conteiners)

        tvVariantValue.setTextColor(
            ContextCompat.getColor(this@MainActivity, R.color.textVariantsColor)
        )

        tvVariantNumber.apply {

            background = ContextCompat.getDrawable(
                this@MainActivity, R.drawable.shape_rounded_variants)

            setTextColor(
                ContextCompat.getColor(this@MainActivity, R.color.textVariantsColor)
            )
        }
    }

    private fun markAnswerWrong(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantValue: TextView) {

        layoutAnswer.background = ContextCompat.getDrawable(
            this, R.drawable.shape_rounded_conteiners_wrong
        )

        tvVariantNumber.background = ContextCompat.getDrawable(
            this, R.drawable.shape_rounded_variants_wrong
        )

        tvVariantNumber.setTextColor(
            ContextCompat.getColor(this, R.color.white)
        )

        tvVariantValue.setTextColor(
            ContextCompat.getColor(this, R.color.colorAnswerWrong)
        )
    }

    private fun markAnswerCorrect(
        layoutAnswer: LinearLayout,
        tvVariantNumber: TextView,
        tvVariantValue: TextView) {

        layoutAnswer.background = ContextCompat.getDrawable(
            this, R.drawable.shape_rounded_conteiners_correct
        )

        tvVariantNumber.background = ContextCompat.getDrawable(
            this, R.drawable.shape_rounded_variants_correct
        )

        tvVariantNumber.setTextColor(
            ContextCompat.getColor(this, R.color.white)
        )

        tvVariantValue.setTextColor(
            ContextCompat.getColor(this, R.color.colorAnswerCorrect)
        )
    }

    private fun showResultMessage(isCorrect: Boolean) {
        val messageText: String
        val color: Int
        val resultIcon: Int

        if(isCorrect) {
            messageText = ContextCompat.getString(this, R.string.title_correct)
            color = ContextCompat.getColor(this@MainActivity, R.color.colorAnswerCorrect)
            resultIcon = R.drawable.ic_correct
        } else {
            messageText = ContextCompat.getString(this, R.string.title_wrong)
            color = ContextCompat.getColor(this@MainActivity, R.color.colorAnswerWrong)
            resultIcon = R.drawable.ic_wrong
        }

        with(binding) {
            btnSkip.isVisible = false
            layoutResult.isVisible = true
            btnContinue.setTextColor(color)
            tvResultMessage.text = messageText
            layoutResult.setBackgroundColor(color)
            ivResultIcon.setImageResource(resultIcon)
        }
    }
}