package com.example.omynote.Views

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.OrientationListener.ORIENTATION_UNKNOWN
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Commons.Timer
import com.example.omynote.R
import com.example.omynote.databinding.ActivityRecodingBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AudioRecodingActivity : AppCompatActivity() ,Timer.OnTimerTickListener{


    lateinit var binding : ActivityRecodingBinding

    private var output: String? = null
    private var mediaRecorder = MediaRecorder()
    private var state: Boolean = false
    private var recordingStopped: Boolean = false


    // creating a variable for mediaplayer class
    private val mPlayer: MediaPlayer? = null

    // string variable is created for storing a file name
    private var mFileName: String? = null

    // constant for storing audio permission
    val REQUEST_AUDIO_PERMISSION_CODE = 1


    lateinit var timer: Timer
    var uri:Uri?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityRecodingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        timer = Timer(this  )
        binding.icCloseRec.setOnClickListener {
            finish()
        }



        binding.icRec.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                Log.d("Recording","recogig $state")
                if (!state){
                    state = true
                    binding.icRec.setBackgroundResource(com.example.omynote.R.drawable.ic_pause)
                    binding.tvTitle.visibility = View.GONE
                    startRecording()

                }else{
                    binding.icRec.setBackgroundResource(R.drawable.bg_rec)
                    binding.tvTitle.visibility = View.VISIBLE
                    state = false

                    stopRecording()
                }
            }
        }



    }


    private fun startRecording() {
        try {

            Log.d("Recording","  start")

            state = true

            timer.start()



            // we are here initializing our filename variable
            // with the path of the recorded audio file.

            val cw = ContextWrapper(this@AudioRecodingActivity)
            output = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString()
            val directory = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
         //   output = Environment.getExternalStorageDirectory().absolutePath

            var dateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
            var date = dateFormat.format(Date())
            output += "/audio_record_$date.mp3"
          //  output += "/AudioRecording.3gp"

            uri =  Uri.fromFile(File("$output"))

            Log.d("Recording","\nAudiopath $output")

            // below method is used to initialize
            // the media recorder class
         //   mediaRecorder = MediaRecorder()

            /*    mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    */
            // below method is used to set the audio
            // source which we are using a mic.
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)

            // below method is used to set
            // the output format of the audio.
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

            // below method is used to set the
            // audio encoder for our recorded audio.
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            // below method is used to set the
            // output file location for our recorded audio
            mediaRecorder.setOutputFile(output)
            try {
                // below method will prepare
                // our audio recorder class
                mediaRecorder.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
              //  Log.e("Recording", "prepare() failed")
            }
            // start method will start
            // the audio recording.
            mediaRecorder.start()

            //   SharePref.toast(this, "Recording started!")
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun stopRecording(){
        try {

        //    if (!state) {
            timer.stop()

         //   mediaRecorder.stop()
                mediaRecorder.release()
                state = false

                val i =  Intent(this, UploadAudioActivity::class.java)
                i.putExtra(AppConstant.AUDIOPATH,"${uri}")
                startActivity(i)
                Log.d("Recording","\n  stop")
                Log.d("Recording","\nAudiopath $output \n\n uri : ${uri}")

       //     } else{





              //  SharePref.toast(this, "You are not recording right now!")

       //     }
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        if(state) {
            if(!recordingStopped){
               SharePref.toast(this,"Stopped!")
                mediaRecorder?.pause()
                recordingStopped = true
                timer.pause()
            }else{
                resumeRecording()
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        SharePref.toast(this,"Resume!")
        mediaRecorder?.resume()
        recordingStopped = false
        timer.start()
    }

    override fun onTimerTick(duration: String) {
        print(duration)
        if (duration == "60"){
            CommonUtils.showLoginDialog(this,true,"You've reached the limit of the audio story which is 1 minute.",View.GONE)
        }
        Log.d("duration","Sencod $duration")
        binding.tvSecondeCouter.text = duration
    }
}