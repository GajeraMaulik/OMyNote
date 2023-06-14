package com.example.omynote.Views

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Adapter.IngrediantAdapter
import com.example.omynote.Adapter.resultatIdList
import com.example.omynote.Commons.SharePref
import com.example.omynote.Interface.onItemSelectedListerner
import com.example.omynote.R
import com.example.omynote.databinding.ActivityIngrediantsBinding



class IngrediantsActivity : AppCompatActivity() {

    private val TAG: String = IngrediantsActivity::class.java.getSimpleName()
    var nextBtnFlag:Boolean = false

    lateinit var binding: ActivityIngrediantsBinding

    lateinit var ingrediantAdapter: IngrediantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityIngrediantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        val normalText = "Didn’t find the scent you’d like\nto include? Add it!"
        val str = SpannableString(normalText)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            45,
            normalText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAddIngrediant.setText(str)

        ingrediantAdapter = IngrediantAdapter()
        resultatIdList.clear()

        binding.btnNextIngrediant.setBackgroundResource(R.drawable.bg_btn_disable)
        binding.btnNextIngrediant.setTextColor(Color.parseColor("#B8B8B8"))
      //  binding.btnNextIngrediant.isEnabled = false
        Log.e("resultatIdList","\n$TAG \n\n:: list size :  ${resultatIdList.size}\n  list :: ${resultatIdList.toString()}")



        Log.e("$TAG","$nextBtnFlag\n\n arrayListSize :: ${ingrediantAdapter.resultatList.size}")




        binding.btnPrevious.setOnClickListener {
            finish()
        }

        binding.icCloseIngrediant.setOnClickListener {
            finish()
        }


        ingrediantAdapter = IngrediantAdapter(this@IngrediantsActivity,resultatList,binding, nextBtnFlag)
        binding.rvResultatList.adapter = ingrediantAdapter

        binding.btnNextIngrediant.setOnClickListener {
            Log.e("$TAG","$nextBtnFlag\n\n arrayListSize :: ${ingrediantAdapter.resultatList.size}")

            if (nextBtnFlag) {
                if (resultatIdList.isEmpty()){
                    nextBtnFlag = false
                }
                binding.btnNextIngrediant.isEnabled = true
                val intent = Intent(this, DiscoverActivity::class.java)
                startActivity(intent)
            }else{
                nextBtnFlag = true
                binding.btnNextIngrediant.isEnabled = false

            }

            // SharePref.toast(this@IngrediantsActivity,"hiiiiiiii")
        }


        binding.rvResultatList.setHasFixedSize(true)




    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStart() {
        super.onStart()
       // resultatIdList.clear()
    }


}