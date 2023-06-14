package com.example.omynote.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.omynote.R
import com.example.omynote.databinding.ActivityMainBinding
import com.example.omynote.databinding.ActivityOnboardingBinding

class MainActivity : AppCompatActivity() {

   lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}