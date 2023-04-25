package com.example.hangman

class Global {
    var language = "PL"
    var level = "1"

    fun setLang(lang: String?){
        if (lang != null) {
            language = lang
        }
    }

    fun setLVL(lvl: String){
        level = lvl
    }
}