package com.example.omynote.Views


import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Adapter.LanguageAdapter
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.LanguageData
import com.example.omynote.databinding.ActivityUploadAudioBinding
import java.io.IOException


var lcKey :String = ""
class UploadAudioActivity : AppCompatActivity() {

    lateinit var binding : ActivityUploadAudioBinding
    lateinit var languageAdapter: LanguageAdapter
     var mediaPlayer = MediaPlayer()
    var audio:String? = ""
    val languageList :ArrayList<LanguageData> = ArrayList()

    var play:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityUploadAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        languageAdapter = LanguageAdapter()
        mediaPlayer = MediaPlayer()

        setupRecyclerView()

         audio = intent.getStringExtra(AppConstant.AUDIOPATH)


        //var audio = intent.getStringExtra("Audiopath")
        binding.icCloseUprec.setOnClickListener {
            finish()
        }
        binding.btnTryagainUprec.setOnClickListener {
            finish()
        }

        binding.btnUploadUprec.setOnClickListener {
            SharePref.toast(this@UploadAudioActivity, lcKey)
            Log.d("lcKeytt","kkkkk $lcKey")
            Log.d("lcKey","play :: $play\n\n audio $audio")

            val intent = Intent(this, PersonalityDefaultStateActivity::class.java)
            intent.putExtra(AppConstant.LANGUAGE,"$lcKey")
            intent.putExtra(AppConstant.AUDIOPATH,"$audio")
            startActivity(intent)


       /*     if (!play){
                playAudio()

            }else{
                pausePlaying()
            }*/
        }
    }
    fun playAudio() {

        // for playing our recorded audio
        // we are using media player class.
        try {
            play = true
            Log.d("Recording","$audio")

            // below method is used to set the
            // data source which will be our file name
            mediaPlayer!!.setDataSource(audio)

            // below method will prepare our media player
            mediaPlayer!!.prepare()

            // below method will start our media player.
            mediaPlayer!!.start()
            //statusTV.setText("Recording Started Playing")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Recording", "prepare() failed")
        }
    }

    fun pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        play = false
        mediaPlayer.release()

        //statusTV.setText("Recording Play Stopped")
    }
    private fun setupRecyclerView() {

        languageList.add(LanguageData("","AUTO"))
        languageList.add(LanguageData("en","ENGLISH"))
        languageList.add(LanguageData("fr","FRENCH"))
        languageList.add(LanguageData("ar","ARABIC"))
        languageList.add(LanguageData("zh","CHINESE"))
        languageAdapter = LanguageAdapter(this@UploadAudioActivity,languageList)
        binding.rvLanguagelist.adapter = languageAdapter

    }

    fun uploadStory(){




    }


}