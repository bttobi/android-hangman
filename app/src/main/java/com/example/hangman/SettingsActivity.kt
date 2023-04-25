package com.example.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btn_pl = findViewById<Button>(R.id.pl_button)
        btn_pl.setOnClickListener{
            val result = Intent().apply{
                putExtra(getString(R.string.language), "PL")
            }

            setResult(RESULT_OK, result)
            finish()
        }

        val btn_eng = findViewById<Button>(R.id.eng_button)
        btn_eng.setOnClickListener{

            val result = Intent().apply{
                putExtra(getString(R.string.language), "ENG")
            }

            setResult(RESULT_OK, result)
            finish()
        }
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