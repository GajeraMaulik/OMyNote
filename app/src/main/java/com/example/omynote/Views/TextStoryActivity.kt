package com.example.omynote.Views

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.SharePref
import com.example.omynote.R
import com.example.omynote.databinding.ActivityTextStoryBinding


class TextStoryActivity : AppCompatActivity() {

    lateinit var binding : ActivityTextStoryBinding

    var notes:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityTextStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding.etNoteText.setBackgroundResource(R.drawable.edittext_selector)
        binding.btnNextText.isEnabled = true
        binding.btnNextText.setBackgroundResource(R.drawable.bg_btn_disable)
        binding.btnNextText.setTextColor(Color.parseColor("#B8B8B8"))

        binding.etNoteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(str: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(str: Editable) {
                if (str.toString().trim { it <= ' ' }.length > 0) {
                   // binding.btnNextText.isEnabled = false
                    binding.btnNextText.setBackgroundResource(R.drawable.bg_btn)
                    binding.btnNextText.setTextColor(Color.parseColor("#FFFFFFFF"))
                    Log.d("textStory","textStory if $str")
                } else {
                    Log.d("textStory","textStory else $str")

                  //  notes = str
                   // binding.btnNextText.isEnabled = true
                    binding.btnNextText.setBackgroundResource(R.drawable.bg_btn_disable)
                    binding.btnNextText.setTextColor(Color.parseColor("#B8B8B8"))


                }
            }
        })
//        notes = binding.etNoteText.text.toString().trim()

        binding.btnNextText.setOnClickListener {

            //SharePref.toast(this@TextStoryActivity,binding.etNoteText.text.toString())

            val intent = Intent(this, PersonalityDefaultStateActivity::class.java)
            intent.putExtra(AppConstant.NOTES,binding.etNoteText.text.toString().trim())
            intent.putExtra(AppConstant.LANGUAGE,"en")
            startActivity(intent)
        }



        binding.icCloseText.setOnClickListener {
            finish()
        }
        setupUI(binding.root)
    }
    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    hideSoftKeyboard(this@TextStoryActivity)
                    return false
                }

            })
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }
}