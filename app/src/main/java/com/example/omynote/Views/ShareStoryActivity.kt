package com.example.omynote.Views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.R
import com.example.omynote.databinding.ActivityShareStoryBinding

class ShareStoryActivity : AppCompatActivity() {

    lateinit var binding : ActivityShareStoryBinding

    var imageIndex = 0
    var mainIndex = 0 // index of the current string being displayed
    var subIndex = 0

    var mainTitleArrayList:ArrayList<SpannableString> = ArrayList()
    var subTitleArrayList:ArrayList<String> = ArrayList()

    val  text:String = "I'm\nOlimpIA"
    val lastMaintext :String= "Share The Way\nYou Want"
    val lastSubtext :String= "You can use audio, video or text"
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        super.onCreate(savedInstanceState)

        binding = ActivityShareStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        val btnMic = findViewById<Button>(R.id.btnMic)

        var spannableString:SpannableString = SpannableString(text)

        var color = ForegroundColorSpan(Color.parseColor("#CFB992"))
        spannableString.setSpan(color,4,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        Log.d("spannableString","firstString :: $spannableString")

        val animation : Animation = AnimationUtils.loadAnimation(this@ShareStoryActivity, R.anim.translate)

        mainTitleArrayList = arrayListOf(
            spannableString,
            SpannableString("Ask Me\nAnything"),
            SpannableString("Share The Way\nYou Want"),

        )

        subTitleArrayList = arrayListOf(
            "Your Fragrance Advisor",
            "Tell me more about yourself:\nyour emotions, thoughts, desires",
            "You can use audio, video or text",
        )

        val userName = SharePref.getStringValue(this@ShareStoryActivity,AppConstant.userName)

        binding.tvUserNameShare.text = userName

        val handler = Handler()
        val delay = 25 // in milliseconds

        val runnable = object : Runnable {
            override fun run() {
                showNextImage()
                handler.postDelayed(this, delay.toLong())
            }
        }
        handler.postDelayed(runnable, delay.toLong())

        // Define the task to update the text view with the next string and start the animation
        val updateTextTask = object : Runnable {
            override fun run() {

                binding.mainTitleShare.text = mainTitleArrayList[mainIndex] // update the text view with the current string
                binding.subTitleShare.text = subTitleArrayList[subIndex]

                binding.mainTitleShare.startAnimation(animation) // start the animation on the text view
                binding.subTitleShare.startAnimation(animation)
                binding.btnMic.startAnimation(animation)
                binding.btnVideo.startAnimation(animation)
                binding.btnSms.startAnimation(animation)

                mainIndex = (mainIndex + 1) % mainTitleArrayList.size // increment the current index and wrap around if necessary
                subIndex  = (subIndex + 1) % subTitleArrayList.size


                if (binding.mainTitleShare.text == mainTitleArrayList.last()){
                    handler.postDelayed(Runnable {
                        binding.btnMic.clearAnimation()
                        binding.btnVideo.clearAnimation()
                        binding.btnSms.clearAnimation()
                    },900)
                    binding.btnMic.visibility = View.VISIBLE
                    binding.btnVideo.visibility = View.VISIBLE
                    binding.btnSms.visibility = View.VISIBLE
                    binding.tvSkipShare.visibility = View.GONE




                }else{
                    handler.postDelayed(this, 3000) // schedule the task to run again after 3 seconds (adjust this value to your desired duration)

                    binding.btnMic.visibility = View.GONE
                    binding.btnVideo.visibility = View.GONE
                    binding.btnSms.visibility = View.GONE
                    binding.tvSkipShare.visibility = View.VISIBLE

                }


                Log.d("mainIndex","in Run : mainIndex :: $mainIndex     subIndex :: $subIndex")
            }
        }

        binding.btnMic.setOnClickListener {
           // SharePref.toast(this@ShareStoryActivity,"Miccccccccccccc")
            audioFlag = true
            val intent = Intent(this@ShareStoryActivity, AudioRecodingActivity::class.java)
            startActivity(intent)
        }
        binding.btnSms.setOnClickListener {
            textFlag = true
          //  SharePref.toast(this@ShareStoryActivity,"Miccccccccccccc")
            val intent = Intent(this@ShareStoryActivity, TextStoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnVideo.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
                ActivityCompat.requestPermissions(this, permissions,0)
            }else {
                videoFlag = true
                val intent = Intent(this@ShareStoryActivity, VideoRecodingActivity::class.java)
                startActivity(intent)
            }
        }
        binding.tvSkipShare.setOnClickListener {
            handler.removeCallbacks(updateTextTask)

            val animations : Animation = AnimationUtils.loadAnimation(this@ShareStoryActivity, R.anim.translate)
            binding.mainTitleShare.text = lastMaintext // update the text view with the current string
            binding.subTitleShare.text = lastSubtext
            binding.btnMic.visibility = View.VISIBLE
            binding.btnVideo.visibility = View.VISIBLE
            binding.btnSms.visibility = View.VISIBLE
            binding.tvSkipShare.visibility = View.GONE
            binding.mainTitleShare.startAnimation(animations) // start the animation on the text view
            binding.subTitleShare.startAnimation(animations)
            binding.btnMic.startAnimation(animations)
            binding.btnVideo.startAnimation(animations)
            binding.btnSms.startAnimation(animations)
        }
        Log.d("mainIndex","mainIndex :: $mainIndex     subIndex :: $subIndex  main ${mainTitleArrayList.size}  sub  ${subTitleArrayList.size}")
        binding.btnBackShare.setOnClickListener {
            finish()
        }



        // Call the task to start displaying the strings one by one
        handler.post(updateTextTask)
        btnMic.setOnClickListener {
            val intent = Intent(this@ShareStoryActivity, AudioRecodingActivity::class.java)
            startActivity(intent)
        }



    }

    fun showNextImage() {

        binding.animationShare.setImageResource(android.R.color.transparent)
        binding.animationShare.setBackgroundResource(CommonUtils.animationImages[imageIndex])
        binding.animationShare.scaleType = ImageView.ScaleType.CENTER
        imageIndex = (imageIndex + 1) % CommonUtils.animationImages.size
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mainTitleShare.clearAnimation()
        binding.subTitleShare.clearAnimation()
        binding.btnMic.clearAnimation()
        binding.btnVideo.clearAnimation()
        binding.btnSms.clearAnimation()
    }
}