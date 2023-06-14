package com.example.omynote.Views

import android.app.Activity
import android.content.Intent
import android.hardware.Camera
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Adapter.LanguageAdapter
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.LanguageData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityUploadVideoBinding
import java.io.IOException

class UploadVideoActivity : AppCompatActivity() {

    lateinit var binding : ActivityUploadVideoBinding
    lateinit var languageAdapter: LanguageAdapter
    var mediaPlayer = MediaPlayer()
    var video:String? = ""
    val languageList :ArrayList<LanguageData> = ArrayList()

    var play:Boolean = false

    // declaring a null variable for VideoView
    var simpleVideoView: VideoView? = null

    // declaring a null variable for MediaController
  //  var mediaPlayer: MediaPlayer? = null
    var mCamera: android.hardware.Camera? = null

    lateinit var mSurfaceHolder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityUploadVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        val controller = MediaController(this)

        languageAdapter = LanguageAdapter()
        mediaPlayer = MediaPlayer()
        video = intent.getStringExtra(AppConstant.VIDEOPATH)

        mSurfaceHolder = binding.showVideoView.holder
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)



        setupRecyclerView()

        controller.setAnchorView(binding.showVideoView);
        // sets the media player to the videoView
        controller.setMediaPlayer(binding.showVideoView);


        binding.showVideoView.apply {
            setVideoURI(Uri.parse("$video"))
            setMediaController(controller)
           setKeepScreenOn(true);
            setZOrderOnTop(false);
           requestFocus();
          //  start()
            //binding.icPlay.setImageResource(R.drawable.ic_pause1)
        }
        mediaPlayer.setOnVideoSizeChangedListener { mp, width, height ->
            if (width < height) {
                //                       orientation = "vertical";
                binding.showVideoView.setRotation(100f);
            } else {
                var displayMetrics: DisplayMetrics = DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                var sHeight = displayMetrics.heightPixels;
                var sntWidth = displayMetrics.widthPixels;
                var parms: FrameLayout.LayoutParams = FrameLayout.LayoutParams(sHeight, sHeight);
                //binding.showVideoView.setLayoutParams(parms);
            }
        };

       // mediaPlayer.setDataSource(this@UploadVideoActivity,Uri.parse(video))
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
            binding.icPlay.setImageResource(R.drawable.ic_play)
        }



        binding.icPlay.setOnClickListener {
            if(mediaPlayer.isPlaying){
                binding.showVideoView.pause()

                mediaPlayer.pause()
                binding.icPlay.setImageResource(R.drawable.ic_play)
            }else{
                binding.showVideoView.start();

                mediaPlayer.start()
                binding.icPlay.setImageResource(R.drawable.ic_pause1)
            }
        }
        binding.icCloseUpvideo.setOnClickListener {
            finish()
        }
        binding.btnTryagainUpvideo.setOnClickListener {
            finish()
        }
        binding.btnNextUprec.setOnClickListener {
            SharePref.toast(this@UploadVideoActivity, lcKey)

            val i =  Intent(this, StoryTimeActivity::class.java)
            i.putExtra(AppConstant.VIDEOPATH,"${video}")
            i.putExtra(AppConstant.LANGUAGE,"$lcKey")
            startActivity(i)
            Log.d("lcKeytt","kkkkk $lcKey")
            Log.d("lcKey","play :: $play\n\n audio $video")

        }

       // setCameraDisplayOrientation(this@UploadVideoActivity,1,mCamera!!)

    }

    fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: Camera) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay
            .rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360
        }
        camera.setDisplayOrientation(result)
    }

    private fun setupRecyclerView() {

        languageList.add(LanguageData("","AUTO"))
        languageList.add(LanguageData("en","ENGLISH"))
        languageList.add(LanguageData("fr","FRENCH"))
        languageList.add(LanguageData("ar","ARABIC"))
        languageList.add(LanguageData("zh","CHINESE"))
        languageAdapter = LanguageAdapter(this@UploadVideoActivity,languageList)
        binding.rvLanguagelistUpvideo.adapter = languageAdapter

    }
    private fun startCamera() {
        /*    mCamera = android.hardware.Camera.open()

            mCamera!!.setDisplayOrientation(90)
            try {
                mCamera!!.setPreviewDisplay(mSurfaceHolder)
                mCamera!!.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }*/
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera?.setPreviewDisplay(mSurfaceHolder)
                mCamera?.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Log.d(View.VIEW_LOG_TAG, "Error setting camera preview: " + e.message)
        }
    }


}