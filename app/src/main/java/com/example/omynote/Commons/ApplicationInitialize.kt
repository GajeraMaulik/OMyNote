package com.example.omynote.Commons

import android.app.Application
import com.example.omynote.Models.Suggestion.MatchedPerfumes

class ApplicationInitialize:Application() {

    companion object{
        val filterSuggestionMap: HashMap<Int, MatchedPerfumes> = HashMap()
    }
}