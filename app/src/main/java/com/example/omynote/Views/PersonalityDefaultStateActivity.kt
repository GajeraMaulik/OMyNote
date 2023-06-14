package com.example.omynote.Views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.CustomMarkerView
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.UploadStory.Personality
import com.example.omynote.Models.UploadStory.Resultat
import com.example.omynote.Models.UploadStory.Scores
import com.example.omynote.Models.UploadStory.UploadStoryData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityPersonalityDefaultStateBinding
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var textFlag :Boolean = false
var audioFlag :Boolean = false
var videoFlag :Boolean = false
var resultatList:ArrayList<Resultat> = ArrayList()

class PersonalityDefaultStateActivity : AppCompatActivity() {

    private val TAG: String = PersonalityDefaultStateActivity::class.java.getSimpleName()
    lateinit var binding: ActivityPersonalityDefaultStateBinding
    val handler = Handler()

    var imageIndex = 0
    var type:String? = ""
    var language:String? = ""
    var text:String? =""
    var videoPath:String? = ""
    var audioPath:String? = ""
    var mainIndex = 0 // index of the current string being displayed
    var subIndex = 0


    var mainArrayList:ArrayList<String> = ArrayList()
    var subTitleArrayList:ArrayList<String> = ArrayList()

    var RadarChart: RadarChart? = null
    var radarData: RadarData? = null
    var radarDataSet: RadarDataSet? = null
    //var radarEntries: ArrayList<DataEntry> = ArrayList()
    var radarEntries: ArrayList<RadarEntry> = ArrayList()
    val labels = arrayListOf("Sadness","Disgust","Fear","Anger","Anticipation","Negative","Positive","Trust","Joy","Surprise")
    var sadness:Float = 0.5f
    var disgust:Float = 0.5f
    var fear:Float = 0.5f
    var anger:Float = 0.5f
    var anticipation:Float = 0.5f
    var negative:Float = 0.5f
    var positive:Float = 0.5f
    var trust:Float = 0.5f
    var joy:Float = 0.5f
    var suprise:Float =0.5f
    var endTitle:String? =""
    lateinit var animation:Animation
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalityDefaultStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        animation  = AnimationUtils.loadAnimation(this@PersonalityDefaultStateActivity,
            com.example.omynote.R.anim.translate)


        mainArrayList = ArrayList()


        subTitleArrayList = arrayListOf(

            "Give me few seconds to create\nyour personality type"
        )

        endTitle = "Your personality type is ready"



        val delay = 25 // in milliseconds

        val runnable = object : Runnable {
            override fun run() {
                showEnimetion()
                handler.postDelayed(this, delay.toLong())
            }
        }
        handler.postDelayed(runnable, delay.toLong())

        language = intent.getStringExtra(AppConstant.LANGUAGE)

        text = intent.getStringExtra(AppConstant.NOTES)

        audioPath = intent.getStringExtra(AppConstant.AUDIOPATH)
        videoPath = intent.getStringExtra(AppConstant.VIDEOPATH)


        if (textFlag){
            type = "text"
            language = "en"

        }
        if (audioFlag){
            type = "voice"
            text = ""
        }
        if (videoFlag){
            type =  "story"
            text = ""
        }

        Log.d("$TAG","\ntype : $type\ntext : ${text}\n\nlanguage : $language\n\n audiopath : $audioPath\n\n videopath : $videoPath")



        // Define the task to update the text view with the next string and start the animation
        val updateTextTask = object : Runnable {
            override fun run() {

              //  binding.mainTitleDs.text = mainArrayList[mainIndex] // update the text view with the current string


                binding.subTitleDs.text = subTitleArrayList[subIndex]
                //    binding.mainTitleDs.startAnimation(animation) // start the animation on the text view
                binding.subTitleDs.startAnimation(animation)


             //   mainIndex = (mainIndex + 1) % mainArrayList.size // increment the current index and wrap around if necessary
             //   subIndex  = (subIndex + 1) % subTitleArrayList.size

                if (binding.subTitleDs.text == subTitleArrayList.last()){




                }else{
                    handler.postDelayed(this, 3000) // schedule the task to run again after 3 seconds (adjust this value to your desired duration)

                }

            }
        }

        binding.icBackDs.setOnClickListener {
            finish()
        }

        binding.btnNextDs.setOnClickListener {
            val i = Intent(this@PersonalityDefaultStateActivity,IngrediantsActivity::class.java)
            startActivity(i)
        }

        // Call the task to start displaying the strings one by one
        handler.post(updateTextTask)

        //anyChart()
        getEntries()


        // Api Calling
        handler.postDelayed(Runnable {  uploadStory_apicalling()},3000)


        // Hide Labele
        binding.radarChart.legend.isEnabled = false



       // binding.radarChart.xAxis.setDrawLabels(false) // hide bottom label


        val xAxis:XAxis =binding.radarChart.getXAxis()
            xAxis.setValueFormatter(IndexAxisValueFormatter(labels))


            xAxis.setTextColor(Color.WHITE)


            Log.d("$TAG"," xAxis.position :: ${xAxis.getFormattedLabel(0)}\n\n")
            xAxis.axisMinimum = 0F
            xAxis.axisMaximum = 1.0F
            xAxis.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            xAxis.isEnabled = true

        xAxis.yOffset = 10f
        xAxis.xOffset = 10f



        val yAxis: YAxis = binding.radarChart.getYAxis()
        yAxis.isEnabled = false
        yAxis.setStartAtZero(true)
        yAxis.axisMinimum = 0F
        yAxis.axisMaximum = 1F
       // yAxis.setPosition(yAxis.labelPosition)


        //hide  XAxis and YAxis Lines
        binding.radarChart.setDrawWeb(false)



        binding.radarChart.getDescription().setEnabled(false)
        binding.radarChart.setDrawMarkerViews(true)

        //binding.radarChart.setTextColor(R.color.white); // left y-axis
        //binding.radarChart.getXAxis().setTextColor(R.color.white);
        binding.radarChart.getYAxis().setTextColor(Color.parseColor("#FFFFFF"));
        binding.radarChart.getLegend().setTextColor(Color.parseColor("#FFFFFF"));
        binding.radarChart.getDescription().setTextColor(Color.parseColor("#FFFFFF"));

        val l: Legend = binding.radarChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
       // l.typeface = Typeface.create(R.font.pulpdisplay_light,0)

        l.textColor = Color.alpha(Color.parseColor("#FFFFFF"))

       // radarDataSet!!.setColors(Color.GRAY)
        radarDataSet!!.setFillColor(Color.rgb(103,110,129))
        radarDataSet!!.setColor(Color.rgb(103, 110, 129))
        radarDataSet!!.setDrawValues(false)
        radarDataSet!!.setDrawIcons(true)
      //  radarDataSet!!.setFillAlpha(200)
        radarDataSet!!.setLineWidth(1.0f)
        radarDataSet!!.setDrawHighlightCircleEnabled(true)
        radarDataSet!!.setDrawVerticalHighlightIndicator(true)
        for (set in binding.radarChart.getData()
            .getDataSets()) {
            if (set.isDrawFilledEnabled) set.setDrawFilled(false) else set.setDrawFilled(
                true
            )
        }

        // Add marker view to the chart
        val markerImage = MarkerImage(this@PersonalityDefaultStateActivity,R.drawable.marker_icon)
        binding.radarChart.marker =markerImage

//        binding.radarChart.marker = MarkerView(this,R.drawable.bg_marker)






    }

 /*   fun anyChart(){
        val anyChartView = findViewById<AnyChartView>(com.example.omynote.R.id.any_chart_view)
        val radar: Radar = AnyChart.radar()

        radar.yScale().minimum(0.0)
        radar.yScale().minimumGap(0.0)
        radar.yScale().ticks().interval(50.0)

        radar.xAxis().labels().padding(5.0, 5.0, 5.0, 5.0)

        radar.legend()
            .align(Align.CENTER)
            .enabled(true)



        radarEntries = ArrayList()

        radarEntries.add(CustomDataEntry( "sadness",sadness))
        radarEntries.add(CustomDataEntry( "disgust",disgust))
        radarEntries.add(CustomDataEntry("fear",fear))
        radarEntries.add(CustomDataEntry("anger",anger))
        radarEntries.add(CustomDataEntry("anticipation",anticipation))
        radarEntries.add(CustomDataEntry("negative",negative))
        radarEntries.add(CustomDataEntry( "positive",positive))
        radarEntries.add(CustomDataEntry( "trust",trust))
        radarEntries.add(CustomDataEntry( "joy",joy))
        radarEntries.add(CustomDataEntry( "suprise",suprise))

        val set: Set = com.anychart.data.Set.instantiate()
        set.data(radarEntries)
        val shamanData = set.mapAs("{ x: 'x', value: 'value' }")
        val shamanLine: Line = radar.line(shamanData)
        shamanLine.name("Shaman")
        shamanLine.markers()
            .enabled(true)
            .type(MarkerType.CIRCLE)
            .size(3.0)

        radar.tooltip().format("Value: {%Value}")

        anyChartView.setChart(radar)


    }


    private class CustomDataEntry(x: String?, value: Float?) :
        ValueDataEntry(x, value) {
        init {

        }
    }*/

    private fun getEntries()  {

        radarEntries = ArrayList()
        Log.d("getEntries","\nsadness :$sadness\n " +
                "disgust : $disgust\n" +
                " fear :$fear\n " +
                "anger: $anger" +
                " \n anticipation:$anticipation\n " +
                "negative :$negative\n " +
                "positive : $positive\n" +
                "trust : $trust\n " +
                "joy : $joy\n " +
                "suprise : $suprise")
        radarEntries.add(RadarEntry( sadness))
        radarEntries.add(RadarEntry( disgust))
        radarEntries.add(RadarEntry(fear))
        radarEntries.add(RadarEntry(anger))
        radarEntries.add(RadarEntry(anticipation))
        radarEntries.add(RadarEntry(negative))
        radarEntries.add(RadarEntry( positive))
        radarEntries.add(RadarEntry( trust))
        radarEntries.add(RadarEntry( joy))
        radarEntries.add(RadarEntry( suprise))

        // Customize the marker image

        val markerImage = CustomMarkerView(this@PersonalityDefaultStateActivity, com.example.omynote.R.layout.view_marker)

        val markerDrawable = ContextCompat.getDrawable(this, R.drawable.marker_icon)

        val markerImage1 = MarkerImage(this@PersonalityDefaultStateActivity,R.drawable.marker_icon)


        binding.radarChart.marker =markerImage1

        radarDataSet = RadarDataSet(radarEntries, "")
        radarData = RadarData(radarDataSet)

        radarDataSet!!.setDrawHighlightCircleEnabled(true)
        radarDataSet!!.setDrawVerticalHighlightIndicator(false)

        binding.radarChart.setData(radarData)
        binding.radarChart.invalidate()

    }

    fun uploadStory_apicalling(){
        if (CommonUtils.checkNetwork(this)) {
            /*
            token!!,type!!,text!!,language!!,"true"
            */
            val token = SharePref.getStringValue(this@PersonalityDefaultStateActivity,AppConstant.userToken)
            val url = "https://mocki.io/"
            val apiService: APIService = RetrofitClient().getApiResponse(url)
            val call: Call<UploadStoryData> = apiService.uploadStory()

            call.enqueue(object :Callback<UploadStoryData>{
                override fun onResponse(call: Call<UploadStoryData>, response: Response<UploadStoryData>
                ) {
                    if (response.code() == 200){

                        Log.d("uploadStory_apicalling","\n code :: ${response.code()}\n\n body ::  ${response.body()}\n ")
                        binding.btnNextDs.visibility = View.VISIBLE
                        binding.favoriteDs.visibility = View.VISIBLE
                        binding.btnNextDs.startAnimation(animation)
                        binding.favoriteDs.startAnimation(animation)
                        // Define the task to update the text view with the next string and start the animation
                        val updateTextTask = object : Runnable {
                            override fun run() {

                                //  binding.mainTitleDs.text = mainArrayList[mainIndex] // update the text view with the current string


                                binding.subTitleDs.text = endTitle
                                //    binding.mainTitleDs.startAnimation(animation) // start the animation on the text view
                                binding.subTitleDs.startAnimation(animation)


                            }
                        }
                        handler.post(updateTextTask)

                        var uploadStoryData :UploadStoryData? = response.body()

                        resultatList.clear()
                        resultatList.addAll(uploadStoryData!!.resultat)
                        val personality:Personality? = uploadStoryData.personality
                        val scores :Scores?= personality!!.scores



                        sadness = scores!!.sadness!!.toFloat()
                        disgust =  scores!!.disgust!!.toFloat()
                        fear = scores.fear!!.toFloat()
                        anger =  scores.anger!!.toFloat()
                       anticipation = scores.anticipation!!.toFloat()
                        negative =  scores.negative!!.toFloat()
                        positive =  scores.positive!!.toFloat()
                        trust =  scores.trust!!.toFloat()
                        joy =  scores.joy!!.toFloat()
                        suprise = scores.surprise!!.toFloat()

                        val xAxis:XAxis =binding.radarChart.getXAxis()

                        if(sadness == 1.0F ){
                         // xAxis.textColor = Color.parseColor("#BFB7A9")
                            sadness = 0.95F
                        }
                        if (sadness<0.5F){
                            sadness = 0.5F
                        }
                        if(disgust == 1.0F){
                            disgust = 0.95F
                        }
                        if (disgust<0.5){
                            disgust = 0.5F
                        }
                        if(fear == 1.0F){
                            fear = 0.95F
                        }
                        if (fear<0.5){
                            fear = 0.5F
                        }
                        if(anger == 1.0F){
                            anger = 0.95F
                        }
                        if (anger<0.5){
                            anger = 0.5F
                        }
                        if(anticipation == 1.0F){
                            anticipation = 0.95F
                        }
                        if (anticipation<0.5){
                            anticipation = 0.5F
                        }
                        if(negative == 1.0F){
                            negative = 0.95F
                        }
                        if (negative<0.5){
                            negative= 0.5F
                        }
                        if(positive == 1.0F){
                            positive = 0.95F
                        }
                        if (positive<0.5){
                            positive = 0.5F
                        }
                        if(trust == 1.0F){
                            trust = 0.95F
                        }
                        if (trust<0.5){
                            trust = 0.5F
                        }
                        if(joy == 1.0F){
                            joy = 0.95F
                        }
                        if (joy<0.5){
                            joy = 0.5F
                        }
                        if(suprise == 1.0F){
                            suprise = 0.95F
                        }
                        if (suprise<0.5){
                            suprise = 0.5F
                        }

                        Log.d("getEntries","uploadStory_apicalling : \nsedness : ${sadness.toFloat()} \n disgust : ${disgust.toFloat()}\n fear : ${fear.toFloat()}\n anger : ${anger.toFloat()}\n  anticipation : ${anticipation.toFloat()}\n negative : ${negative.toFloat()}\n positive : ${positive.toFloat()}\n trust : ${trust.toFloat()}\n joy :${joy.toFloat()}\n suprise :${suprise.toFloat()}")



                        val yAxis: YAxis = binding.radarChart.getYAxis()
                        yAxis.isEnabled = false

                        getEntries()


                        binding.radarChart.getDescription().setEnabled(false)
                        binding.radarChart.setTouchEnabled(false)
                        radarDataSet!!.setDrawValues(false)
                        radarDataSet!!.setDrawIcons(false)

                        radarDataSet!!.setColors(Color.rgb(103,110,129))
                        radarDataSet!!.setFillColor(Color.rgb(103,110,129))

                       // radarDataSet!!.setValueTextColors(arrayListOf(R.color.white))



                       // radarDataSet!!.setFillAlpha(200)
                        radarDataSet!!.setLineWidth(1.0f)
                        // Add marker view to the chart
                        val markerImage = CustomMarkerView(this@PersonalityDefaultStateActivity, R.layout.view_marker)
                        val markerImage1 = MarkerImage(this@PersonalityDefaultStateActivity,R.drawable.marker_icon)
                        binding.radarChart.setMarkerView(markerImage1)


                        radarDataSet!!.setDrawHighlightCircleEnabled(true)
                        radarDataSet!!.setDrawVerticalHighlightIndicator(false)
                        radarDataSet!!.setDrawHorizontalHighlightIndicator(false)

                        //  val markerImage = MarkerImage(this@PersonalityDefaultStateActivity,R.drawable.marker_icon)

                        // create a custom MarkerView (extend MarkerView) and specify the layout
                        // to use for it



                      //  radarDataSet!!.setValueTextColors(listOf(com.example.omynote.R.color.white))

                        for (set in binding.radarChart.getData()
                            .getDataSets()) {
                            if (set.isDrawFilledEnabled) set.setDrawFilled(false) else{
                                set.setDrawFilled(true)

                            }
                        }



                        binding.radarChart.notifyDataSetChanged();
                        binding.radarChart.invalidate();

                        binding.radarChart.animateY(1400)
                      //  val mv: MarkerView = CustomMarkerView(this@PersonalityDefaultStateActivity, R.layout.view_marker)
                    //    mv.chartView = binding.radarChart // For bounds control


                        // Log.d("getEntries","sedness : ${sedness} \n disgust : ${disgust}\n fear : ${fear}\n anger : ${anger}\n  anticipation : ${anticipation}\n negative : ${negative}\n positive : ${positive}\n trust : ${trust}\n joy :${joy}\n suprise :${suprise}")

                    }else if (response.code() == 400){
                        try {
                            CommonUtils.showLoginDialog(this@PersonalityDefaultStateActivity,true,"Story too short. Provide more detailed description.",View.GONE)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<UploadStoryData>, t: Throwable) {
                    t.printStackTrace()
                    t.message
                    t.cause
                }

            })
        }else{
            SharePref.toast(this@PersonalityDefaultStateActivity, AppConstant.noInternet)
        }
    }

    fun showEnimetion() {

        binding.animationDs.setImageResource(android.R.color.transparent)
        binding.animationDs.setBackgroundResource(CommonUtils.animationDs[imageIndex])
        binding.personalityAnimationDs.setBackgroundResource(CommonUtils.personalityImage[imageIndex])

        binding.animationDs.scaleType = ImageView.ScaleType.CENTER
        imageIndex = (imageIndex + 1) % CommonUtils.animationImages.size
        imageIndex = (imageIndex + 1) % CommonUtils.personalityImage.size
    }
}