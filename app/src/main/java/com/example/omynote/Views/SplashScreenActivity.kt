package com.example.omynote.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.SharePref
import com.example.omynote.R
import com.example.omynote.databinding.ActivityOnboardingBinding
import com.example.omynote.databinding.ActivitySplashScreenBinding
import java.util.FormattableFlags

var animationFlag:Boolean = false
class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgLogin)

        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


    }
    override fun onStart() {
        super.onStart()
/*        Handler().postDelayed({

            val i = Intent(applicationContext, DiscoverActivity::class.java)
            startActivity(i)
            finish()
            animationFlag = true

        }, 2000)*/
        if (SharePref.getBooleanValue(this, AppConstant.isLogin)) {

            Handler().postDelayed({

                val i = Intent(applicationContext, DashboardActivity::class.java)
                startActivity(i)
                finish()
                animationFlag = true

            }, 2000)

        } else {
            Handler().postDelayed({

                val intent = Intent(applicationContext, OnboardingActivity::class.java)
                // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()

            }, 2000)
        }




    }
}