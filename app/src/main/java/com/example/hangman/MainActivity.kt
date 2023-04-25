package com.example.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.hangman.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var stringArray: Array<String>
    private val global = Global()
    private var word: String = ""
    private var imgToShow: Int = 1


    private val launchSettingsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == RESULT_OK){
                result.data?.let {data ->
                    global.setLang(data.getStringExtra(getString(R.string.language)))
                    resetGame()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = findViewById<TextView>(R.id.guess_word) //get text to display
        val nextWordBtn = findViewById<FloatingActionButton>(R.id.next_word_button) //next word button
        val checkLetterBtn = findViewById<Button>(R.id.letter_button)
        val checkWordBtn = findViewById<Button>(R.id.word_button)


        //initialization
        stringArray =
            if(global.language == "PL") resources.getStringArray(R.array.pl_words) //getting first array
            else resources.getStringArray(R.array.eng_words)

        word = getRandomWord() //getting first word
        text.text = getToDisp(word)


        //adding listener to next button
        nextWordBtn.setOnClickListener{
            resetGame()
        }

        //adding listener to check letter button
        checkLetterBtn.setOnClickListener{
             //IF ITS CHECK LETTER - CHECK ONLY FIRST LETTER OF THE WORD
            checkLetter()
        }

        checkWordBtn.setOnClickListener{
            checkWord()
        }

    }

    private fun getRandomWord():String{
        when (global.level) {
            "1" -> stringArray = stringArray.filter{ it.length == 4}.toTypedArray() //filtrowanie na dlugosci
            "2" -> stringArray = stringArray.filter{ it.length == 6}.toTypedArray()
            "3" -> stringArray = stringArray.filter{ it.length == 8}.toTypedArray()
        }

        val number = (stringArray.indices).random()
        return stringArray[number]
    }

    private fun getToDisp(text: String):String{
        var toDisp = ""
        for(i in text.indices) toDisp +="?"

        return toDisp
    }

    private fun checkLetter() {
            val input = findViewById<TextInputEditText>(R.id.text_input) //get input text
            val gallows = findViewById<ImageView>(R.id.hangman_image)
            val text = findViewById<TextView>(R.id.guess_word) //get text to disp

            val newString = StringBuilder(text.text) //getting current text string

            if(input?.editableText.toString().isEmpty()) { //if someone doesnt input anything
                Snackbar.make(
                    binding.root,
                    "Please enter a letter!",
                    Snackbar.LENGTH_SHORT
                ).setAction("Start over"){
                    resetGame()
                }.show()
                return //just return
            }
            else if(word.contains(input.editableText[0].lowercaseChar())){ //only getting the first character
                val index = word.indexOf(input.editableText[0].lowercaseChar(), 0)
                newString.setCharAt(index, input.editableText[0].lowercaseChar()) //modifying the string like shown in tutorial

                text.text = newString //update the text

                if(text.text == word){//check if user has won
                    endGame("You have won! Congrats!")
                    return
                }
            }
            else{ //losing "health"
                gallows.setImageResource(resolveDrawable(imgToShow))
                imgToShow++
            }

            if(imgToShow == 11){ //if chances have reached maximum
                endGame("You have lost!")
                return
            }
    }


    private fun checkWord(){
        val input = findViewById<TextInputEditText>(R.id.text_input) //get input text
        val gallows = findViewById<ImageView>(R.id.hangman_image)

        if(word == input.editableText.toString().lowercase().trim()){ //get rid of white chars too
            endGame("You have won!!! Congrats!")
        }
        else{
            endGame( "You have lost!")
            gallows.setImageResource(resolveDrawable(10))
        }
    }

    private fun resetGame(){
        val spinner = findViewById<Spinner>(R.id.level_spinner) //get spinner
        val text = findViewById<TextView>(R.id.guess_word) //get text to disp
        val gallows = findViewById<ImageView>(R.id.hangman_image)

        imgToShow = 1 //resetting the "health"

        //check language
        stringArray =
            if(global.language == "PL") resources.getStringArray(R.array.pl_words)
            else resources.getStringArray(R.array.eng_words)

        global.setLVL(spinner.selectedItem.toString()) //setting correct level

        word = getRandomWord()

        text.text = getToDisp(word) //getting next new word

        gallows.setImageResource(R.drawable.hangman_gray)//resetting image
    }

    private fun endGame(msg: String){
        val text = findViewById<TextView>(R.id.guess_word) //get text to disp
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).setAction("Start over"){
            resetGame()
        }.show()

        text.text = word
    }

    private fun resolveDrawable(value: Int): Int{
        return when (value){
            0 -> R.drawable.hangman0
            1 -> R.drawable.hangman1
            2 -> R.drawable.hangman2
            3 -> R.drawable.hangman3
            4 -> R.drawable.hangman4
            5 -> R.drawable.hangman5
            6 -> R.drawable.hangman6
            7 -> R.drawable.hangman7
            8 -> R.drawable.hangman8
            9 -> R.drawable.hangman9
            10 -> R.drawable.hangman10
            else -> R.drawable.hangman10
        }
    }

    //menu functions itd.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> startSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java).apply{
            putExtra(getString(R.string.language), global.language)
        }

        launchSettingsActivity.launch(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.i(localClassName, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(localClassName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(localClassName, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(localClassName, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(localClassName, "onDestroy")
    }
}