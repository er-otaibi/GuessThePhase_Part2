package com.example.guessthephrase

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var myText: EditText
    private lateinit var myButton: Button
    private lateinit var phaseText: TextView
    private lateinit var scoreText: TextView
    private var highscore = 0
    private var score = 0
    private lateinit var myRV: RecyclerView
    private var myList = arrayListOf<String>()
    private  var myPhase = "NO PAIN NO GAIN"
    private var letters = arrayListOf<Char>()
    private lateinit var sharedPreferences: SharedPreferences
    private var str=""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(
        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highscore = sharedPreferences.getInt("highscore", 0)

        myText = findViewById(R.id.guessPhase)
        myButton = findViewById(R.id.guessButton)
        phaseText = findViewById(R.id.tvMain)
        myRV = findViewById(R.id.rvMain)
        scoreText = findViewById(R.id.tvHS)
        myRV.adapter = RecyclerViewAdapter(myList)
        myRV.layoutManager = LinearLayoutManager(this)
        for (i in myPhase){
            letters.add(i)
        }

        for (i in myPhase){
            if (i.isWhitespace()){
                str+=" "
            }else{
                str+="*"
            }
        }

        phaseText.text = "Phrase: $str \n Guessed letter: "
        scoreText.text = "High Score: $score"
        myButton.setOnClickListener { guessingGame() }




    }

    @SuppressLint("SetTextI18n")
    fun guessingGame() {
        var guessAnswer1 = myText.text.toString()
        var counter = 0
        var guesses = 10
        if (guessAnswer1.length > 1) {
            if ((guessAnswer1).toUpperCase() != myPhase) {
                myList.add("Wrong Guess: i don't know")
                guesses--
                myList.add("$guesses guesses remaining")
            } else {
                myList.add("Correct Guess: $myPhase")
            }
        }else {
             val guessAnswer = myText.text.toString().trim()[0].toUpperCase()
            when {

                (!letters.contains(guessAnswer))-> {myList.add("wrong guess")
                    score++
                    save()
                    scoreText.text = "High Score: $score"}
                else -> {
                    myList.add("found letter")
                    for ((index, i) in letters.withIndex()) {
                        if (i == guessAnswer) {
                            str = str.substring(0, index) + i + str.substring(index + 1)
                            counter++
                            phaseText.text = "Phrase: $str \n Guessed letter:$i "

                        }

                    }
                    guesses--
                    myList.add("Found $counter $guessAnswer(s)")
                    myList.add("$guesses guesses remaining")


                }
            }

        }
           myRV.adapter?.notifyDataSetChanged()
            if (str == myPhase) {
                myList.add("yes you have the guessing word! $myPhase")
                phaseText.text = "Phrase: $myPhase \n Guessed letter:  "
            }
            myRV.adapter?.notifyDataSetChanged()
        }

    fun save() {
        if (score<highscore) {
            with(sharedPreferences.edit()) {
                putInt("highscore", score)
                apply()
            }
        }
    }




}
