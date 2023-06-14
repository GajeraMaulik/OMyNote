package com.example.omynote.Views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils.Companion.animationImages
import com.example.omynote.Commons.SharePref
import com.example.omynote.R
import com.example.omynote.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnboardingBinding
    var imageIndex = 0
    var mainIndex = 0 // index of the current string being displayed
    var subIndex = 0


   val  text:String = "Hi! My name\nis OlimpIA"



    var mainArrayList:ArrayList<SpannableString> = ArrayList()
    var subTitleArrayList:ArrayList<String> = ArrayList()

    lateinit var permissionsList: java.util.ArrayList<String>
    val permissionsStr = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_EXTERNAL_STORAGE,

    )
    var permissionsCount = 0

    var permissionsLauncher = registerForActivityResult<Array<String>, Map<String, Boolean>>(
        ActivityResultContracts.RequestMultiplePermissions(),
        ActivityResultCallback<Map<String, Boolean>> {
            val list = java.util.ArrayList(it.values)
            val pendingPermissionsList = java.util.ArrayList<String>()
            permissionsCount = 0
            for (i in list.indices) {
                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                    pendingPermissionsList.add(permissionsStr[i])
                } else if (!hasPermission(this@OnboardingActivity, permissionsStr[i])) {
                    permissionsCount++
                }
            }
            if (pendingPermissionsList.size > 0) {
                //Some permissions are denied and can be asked again.
                askForPermissions(pendingPermissionsList)
            }/* else if (permissionsCount > 0) {
                //Show alert dialog
                showPermissionDialog()
            } else {
                //loginApiCall()
                requestOverlayPermission()
                //All permissions granted. Do your stuff 🤞
                // binding.txtStatus.setText("All permissions are granted!")
            }*/
        })


    private fun hasPermission(context: Context, permissionStr: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permissionStr
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(permissionsList: java.util.ArrayList<String>) {
        val newPermissionStr: Array<String> = permissionsList.toTypedArray()
        if (newPermissionStr.size > 0) {
            //binding.txtStatus.setText("Asking for permissions")
            permissionsLauncher.launch(newPermissionStr)
        }/* else {


            *//* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
            which will lead them to app details page to enable permissions from there. *//*
            showPermissionDialog()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionsList = java.util.ArrayList<String>()
        permissionsList.addAll(permissionsStr.toList())

        askForPermissions(permissionsList)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        var spannableString:SpannableString = SpannableString(text)

        var color = ForegroundColorSpan(Color.parseColor("#CFB992"))
        spannableString.setSpan(color,15,22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        Log.d("spannableString","firstString :: $spannableString")

        val animation :Animation = AnimationUtils.loadAnimation(this@OnboardingActivity,R.anim.translate)

        mainArrayList = arrayListOf(
             spannableString,
            SpannableString("Perfectly\ndesigned"),
           SpannableString("Personal\nassistance"),
            SpannableString("You can try\nit now")
        )

        subTitleArrayList = arrayListOf(
             "I'm an artificial intelligence",
             "To find your personalized\nscents in seconds",
            "To recommend perfumes for your\ndesired emotions",
            "Let's start discovering your\nideal scents together"
        )

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

                binding.mainTitleBoarding.text = mainArrayList[mainIndex] // update the text view with the current string
                binding.subTitleBoarding.text = subTitleArrayList[subIndex]

                binding.mainTitleBoarding.startAnimation(animation) // start the animation on the text view
                binding.subTitleBoarding.startAnimation(animation)


                mainIndex = (mainIndex + 1) % mainArrayList.size // increment the current index and wrap around if necessary
                subIndex  = (subIndex + 1) % subTitleArrayList.size

                if (binding.mainTitleBoarding.text == mainArrayList.last()){


                    binding.btnContinue.visibility = View.VISIBLE
                    binding.btnContinues.visibility = View.GONE
                    binding.tvSkipBoarding.visibility = View.GONE

                }else{
                    handler.postDelayed(this, 3000) // schedule the task to run again after 3 seconds (adjust this value to your desired duration)

                    binding.btnContinues.visibility = View.GONE
                    binding.btnContinue.visibility = View.GONE
                }

            }
        }

        // Call the task to start displaying the strings one by one
        handler.post(updateTextTask)

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvSkipBoarding.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun showNextImage() {

        binding.animationBoarding.setImageResource(android.R.color.transparent)
        binding.animationBoarding.setBackgroundResource(animationImages[imageIndex])
        binding.animationBoarding.scaleType = ImageView.ScaleType.CENTER
        imageIndex = (imageIndex + 1) % animationImages.size
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mainTitleBoarding.clearAnimation()
        binding.subTitleBoarding.clearAnimation()
    }

    override fun onStart() {
        super.onStart()

    }

}