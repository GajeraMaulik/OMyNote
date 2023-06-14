package com.example.omynote.Views

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.content.ContextCompat
import com.example.omynote.Adapter.LanguageAdapter
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.LanguageData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityStoryTimeBinding
import com.example.omynote.databinding.ActivityUploadVideoBinding

class StoryTimeActivity : AppCompatActivity() {


    lateinit var binding : ActivityStoryTimeBinding

    var mediaPlayer = MediaPlayer()
    var video:String? = ""

    var play:Boolean = false

    // declaring a null variable for VideoView
    var simpleVideoView: VideoView? = null

    // declaring a null variable for MediaController
    //  var mediaPlayer: MediaPlayer? = null
    var mCamera: android.hardware.Camera? = null

    lateinit var mSurfaceHolder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityStoryTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        val controller = MediaController(this)

        mediaPlayer = MediaPlayer()
        video = intent.getStringExtra(AppConstant.VIDEOPATH)

        mSurfaceHolder = binding.showVideoViewSt.holder
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        controller.setAnchorView(binding.showVideoViewSt);
        // sets the media player to the videoView
        controller.setMediaPlayer(binding.showVideoViewSt);

        binding.btnNextSt.setBackgroundResource(R.drawable.bg_btn_disable)
        binding.btnNextSt.setTextColor(Color.parseColor("#B8B8B8"))

        var handler = Handler()

        handler.postDelayed(Runnable {
            binding.btnNextSt.setBackgroundResource(R.drawable.bg_btn)
            binding.btnNextSt.setTextColor(Color.parseColor("#FFFFFFFF"))
        },2000)

        binding.showVideoViewSt.apply {
            setVideoURI(Uri.parse("$video"))
            setMediaController(controller)
            setKeepScreenOn(true);
            setZOrderOnTop(false);
            requestFocus();
        }

        mediaPlayer.apply {

            // 2
            try {
                setDataSource(applicationContext,
                    // 1
                    Uri.parse("$video"))
                setDisplay(mSurfaceHolder)

            }catch (e:Exception){
                e.printStackTrace()
            }
            // 3
            prepareAsync()
        }

        mediaPlayer.setOnCompletionListener {
            binding.icPlaySt.setImageResource(R.drawable.ic_play)
        }

        binding.icPlaySt.setOnClickListener {
            if(mediaPlayer.isPlaying){
                binding.showVideoViewSt.pause()

                mediaPlayer.pause()
                binding.icPlaySt.setImageResource(R.drawable.ic_play)
            }else{
                binding.showVideoViewSt.start();

                mediaPlayer.start()
                binding.icPlaySt.setImageResource(R.drawable.ic_pause1)
            }
        }

        binding.icCloseSt.setOnClickListener {
            finish()
        }
        binding.btnDeleteSt.setOnClickListener {
            finish()
        }
        binding.btnNextSt.setOnClickListener {
            SharePref.toast(this@StoryTimeActivity, lcKey)

            val intent = Intent(this, PersonalityDefaultStateActivity::class.java)
            intent.putExtra(AppConstant.LANGUAGE,"$lcKey")
            intent.putExtra(AppConstant.VIDEOPATH,"$video")
            startActivity(intent)

            Log.d("lcKeytt","kkkkk $lcKey")
            Log.d("lcKey","play :: $play\n\n audio $video")

        }


    }
}