package com.example.omynote.Views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.Timer
import com.example.omynote.R
import com.example.omynote.databinding.ActivityVideoRecodingBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class VideoRecodingActivity : AppCompatActivity(),com.example.omynote.Commons.Timer.OnTimerTickListener,SurfaceHolder.Callback {

    lateinit var binding : ActivityVideoRecodingBinding

    private val TAG: String = VideoRecodingActivity::class.java.getSimpleName()
    lateinit var mSurfaceHolder: SurfaceHolder
    var mCamera: android.hardware.Camera? = null


   // private var mServiceCamera: android.hardware.Camera? = null
    private var mRecordingStatus = false
    private var mMediaRecorder: MediaRecorder? = null

    var videoSavePathInDevice: String? = null
    var dateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
    var date = dateFormat.format(Date())
    private val VIDEO_RECORDER_FILE_EXT_MP4 = "_${date}.mp4"
    private val VIDEO_RECORDER_FOLDER = "AudioRecorder"
    private val currentFormat = 0
    private val file_exts = arrayOf(VIDEO_RECORDER_FILE_EXT_MP4)
    lateinit var timer: Timer
    private val VIDEO_CAPTURE = 101
    var cameraId = 0
    var uri: Uri?= null
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
            val list = ArrayList(it.values)
            val pendingPermissionsList = ArrayList<String>()
            permissionsCount = 0
            for (i in list.indices) {
                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                    pendingPermissionsList.add(permissionsStr[i])
                } else if (!hasPermission(this@VideoRecodingActivity, permissionsStr[i])) {
                    permissionsCount++
                }
            }
            if (pendingPermissionsList.size > 0) {
                //Some permissions are denied and can be asked again.
                askForPermissions(pendingPermissionsList)
            }
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityVideoRecodingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


        permissionsList = java.util.ArrayList<String>()
        permissionsList.addAll(permissionsStr.toList())


        timer = Timer(this  )

        mSurfaceHolder = binding.surfaceView.holder.apply {
            addCallback(this@VideoRecodingActivity)
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        }

   /*     mCamera!!.autoFocus { b, camera ->

            if (b){
                setCamera(mCamera)
            }
        }*/


        if (mCamera == null){
            setCamera(mCamera)
            try{
                mCamera!!.unlock()
                mCamera!!.setPreviewDisplay(mSurfaceHolder)
                mCamera!!.startPreview()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }


      //  mSurfaceHolder = binding.surfaceView.holder
        binding.buttonShutterRecord.setBackgroundResource(R.drawable.ic_record)


        val simpleDateFormat = SimpleDateFormat("ddMMyyyyhhmm")
        val format: String = simpleDateFormat.format(Date())
        Log.e(TAG, "ts_: $format")
//        videoSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + format + "videoRecording.mp4";
        //        videoSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + format + "videoRecording.mp4";
        videoSavePathInDevice = getFilename()

        binding.buttonClose.setOnClickListener {
            finish()
        }

        binding.icCloseVideo.setOnClickListener {
            finish()
        }

        binding.buttonShutterRecord.setOnClickListener {
            Log.d("$TAG","$mRecordingStatus")
            if (!mRecordingStatus){

                try {
                    //startService(Intent(this@VideoRecodingActivity,VideoJobService::class.java))
                    startRecording()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                mRecordingStatus = true
                binding.buttonShutterRecord.setBackgroundResource(R.drawable.ic_recoding)
                // timer.start()
            }else{
                mRecordingStatus = false
                //timer.stop()
                stopRecording()
                binding.buttonShutterRecord.setBackgroundResource(R.drawable.ic_record)

            }


        }


        mCamera = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK)


        binding.buttonLensFlip.setOnClickListener {

            when (cameraId) {
              0-> {
                                openFrontCamera()
                  mCamera =  Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                  setCameraDisplayOrientation(this,cameraId,mCamera!!)


                }
              1-> {
                  mCamera =   Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
                  setCameraDisplayOrientation(this,cameraId,mCamera!!)


                }
            }

        }

        setCameraDisplayOrientation(this,cameraId,mCamera!!)
        //setCameraDisplayOrientation(this,0,mCamera!!)

       Log.d("$TAG","$mRecordingStatus")

    }


    fun openFrontCamera() {
        Log.e("$TAG","get Id: ${getFrontCameraId()}")
        cameraId = getFrontCameraId()
        if (cameraId != -1) mCamera = Camera.open(cameraId) //try catch omitted for brevity
    }

    fun getFrontCameraId(): Int {
        val ci = Camera.CameraInfo()
        for (i in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(i, ci)
            if (ci.facing === Camera.CameraInfo.CAMERA_FACING_FRONT) return i
        }
        return -1 // No front-facing camera found
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

    fun startRecording() {
        try {
            timer.start()

            //  Toast.makeText(baseContext, "Recording Started", Toast.LENGTH_SHORT).show()

            setCameraDisplayOrientation(this,cameraId,mCamera!!)

            try {
                if (mCamera == null){
                mCamera!!.setPreviewDisplay(mSurfaceHolder)
                mCamera!!.startPreview()
                }
            } catch (e: IOException) {
                Log.e(TAG, Objects.requireNonNull(e.message!!))
                e.printStackTrace()
            }
            mCamera!!.unlock()

            mMediaRecorder = MediaRecorder()
            mMediaRecorder!!.setCamera(mCamera)
            mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mMediaRecorder!!.setOutputFile(videoSavePathInDevice)

            uri =  Uri.fromFile(File("$videoSavePathInDevice"))
            //            mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/"+format+".mp4");
//            mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+"recordVideo"+"/"+format+".mp4");
            mMediaRecorder!!.setPreviewDisplay(mSurfaceHolder.surface)
            mMediaRecorder!!.prepare()
            mMediaRecorder!!.start()
            mRecordingStatus = true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            //  Log.d(TAG, e.message!!)
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if (null != mMediaRecorder) {
            timer.stop()
            Toast.makeText(baseContext, "Recording Stopped", Toast.LENGTH_SHORT).show()

            setCameraDisplayOrientation(this,cameraId,mCamera!!)

            try {
                if (mCamera == null){
                    mCamera!!.reconnect()

                }
                mMediaRecorder!!.reset()
                mCamera!!.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                mMediaRecorder!!.stop()
            } catch (stopException: RuntimeException) {
                // handle cleanup here
            }


            //mServiceCamera.stopPreview();
            mMediaRecorder!!.release()

            mCamera = null
            val i =  Intent(this, UploadVideoActivity::class.java)
            i.putExtra(AppConstant.VIDEOPATH,"${uri}")
            startActivity(i)
            Log.e("$TAG","back : ${Camera.CameraInfo.CAMERA_FACING_BACK}\n\n front : ${Camera.CameraInfo.CAMERA_FACING_FRONT}\n\n" +
                    "path : $videoSavePathInDevice \n\n uri :: $uri")
        }
    }
    private fun getFilename(): String? {
        var dateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
        var date = dateFormat.format(Date())
        val filepath = (Environment.getExternalStorageDirectory().toString() + File.separator
                + Environment.DIRECTORY_DCIM + File.separator + "VIDEO_$date.mp4")
        val file = File(filepath, VIDEO_RECORDER_FOLDER)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + file_exts[currentFormat]
    }



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
    private fun startCamera() {

        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera?.setPreviewDisplay(mSurfaceHolder)
                mCamera?.startPreview()
                setCameraDisplayOrientation(this,cameraId,mCamera!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
           // Log.d(View.VIEW_LOG_TAG, "Error setting camera preview: " + e.message)
        }
    }

    override fun onStart() {
        super.onStart()
        askForPermissions(permissionsList)
        mCamera = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK)
        setCameraDisplayOrientation(this,cameraId,mCamera!!)
    }


    fun refreshCamera(camera: Camera?) {
        if (mSurfaceHolder.surface == null) {
            // preview surface does not exist
            return
        }
        // stop preview before making changes
        try {
            mCamera!!.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera)
        try {
            mCamera!!.setPreviewDisplay(mSurfaceHolder)
            mCamera!!.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
           // Log.d(View.VIEW_LOG_TAG, "Error starting camera preview: " + e.message)
        }

    }


    fun setCamera(camera: Camera?) {
        //method to set a camera instance
        mCamera = camera
    }
    override fun onTimerTick(duration: String) {
        binding.tvSecondeCouterVideo.text = duration
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        startCamera()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        refreshCamera(mCamera)
        setCameraDisplayOrientation(this,cameraId,mCamera!!)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
       // mCamera!!.release()
    }
}
