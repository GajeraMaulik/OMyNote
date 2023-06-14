package com.example.omynote.Views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.omynote.Adapter.PerfumeIngrediantAdapter
import com.example.omynote.Adapter.SharedUserAdapter
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.PerfumeDetails.Layer
import com.example.omynote.Models.PerfumeDetails.Notes
import com.example.omynote.Models.PerfumeDetails.PerfumeDetails
import com.example.omynote.Models.ShareUserData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityPerfumeDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfume_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var expFlag:Boolean = false
class PerfumeDetailsActivity : AppCompatActivity() {

    private val TAG: String = PerfumeDetailsActivity::class.java.getSimpleName()
    lateinit var binding : ActivityPerfumeDetailsBinding

     var expDialog: Dialog? = null

    val notesList:ArrayList<Notes> = ArrayList()
    val layerList:ArrayList<Layer> = ArrayList()
    var shareUserList: ArrayList<ShareUserData> = ArrayList()

    var id1:Int = 0
    var id2:Int = 0

    lateinit var perfumeIngrediantAdapter: PerfumeIngrediantAdapter
    lateinit var sharedUserAdapter: SharedUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
       // window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        super.onCreate(savedInstanceState)
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        binding = ActivityPerfumeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding.buyItIcon.setColorFilter(Color.parseColor("#9C9C9C"))
        perfumeIngrediantAdapter = PerfumeIngrediantAdapter()
        sharedUserAdapter = SharedUserAdapter()

        shareUserList.clear()

        shareUserList.add(ShareUserData(1,R.drawable.profile,"Katie Sims","Relaxing and empowering."))
        shareUserList.add(ShareUserData(2,R.drawable.profile1,"Alex Buckmaster","Donec vitae mi vulputate, suscipit urna in, maleisl. "))
        shareUserList.add(ShareUserData(3,R.drawable.profile2,"Stephanie Nicol","Relaxing."))

        sharedUserAdapter = SharedUserAdapter(this@PerfumeDetailsActivity,shareUserList)


        viewGone()
        setupAnim()

        binding.rvSharedUserPd.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.HORIZONTAL
            )
        )
        binding.rvSharedUserPd.adapter = sharedUserAdapter

        val id = intent.getIntExtra(AppConstant.perfumeId,0)

        expDialog = Dialog(this@PerfumeDetailsActivity)


        Log.e("$TAG","\n\nid $id\n\n")
        perfumeDetails_apicalling(id)
        val fetchAccuracy = intent.getIntExtra(AppConstant.fetchAccuracy,0)

        if (fetchAccuracy == null){
            binding.percentagePd.text = 50.toString()
            binding.progressBarPd.progress = 50
        }else{
            binding.percentagePd.text = fetchAccuracy.toString()
            binding.progressBarPd.progress = fetchAccuracy
        }





        binding.cdTryitView.setOnClickListener {
            tryItUi()
        }
        binding.cdBuyItView.setOnClickListener {
            buyItUi()
        }
        binding.cdNotIntView.setOnClickListener {
            notItUi()
        }
        binding.btnBackPd.setOnClickListener {
            finish()
        }

        binding.sharePd.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)

            // type of the content to be shared

            // type of the content to be shared
            sharingIntent.type = "text/plain"

            // Body of the content

            // Body of the content
            val shareBody = "I found my dream perfume with OlimpIA \uD83D\uDE0A omynote.io iOS : https://apps.apple.com/us/app/o-my-note-perfume-advisor/id1561737457 Android : https://play.google.com/store/apps/details?id=io.omynote&pli=1"

            // subject of the content. you can share anything

            // subject of the content. you can share anything
            val shareSubject = "omynote.io iOS : https://apps.apple.com/us/app/o-my-note-perfume-advisor/id1561737457 Android : https://play.google.com/store/apps/details?id=io.omynote&pli=1"

            // passing body of the content

            // passing body of the content
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

            // passing subject of the content

            // passing subject of the content
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }

        binding.cdLayerPd.setOnClickListener {
            Log.e("$TAG","cdLayerPd: id1 $id1 \n ")
            perfumeDetails_apicalling(id1)
            finish();
            startActivity(getIntent());
        }

        binding.cdLayer1Pd.setOnClickListener {
            Log.e("$TAG","cdLayerPd: id2 $id2 \n ")
            perfumeDetails_apicalling(id2)
            finish();
            startActivity(getIntent());
        }

        binding.nestedScrollPd.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("$TAG","nestedScrollPd :: \n v : $v \n\n scrollX: $scrollX\n\n scrollY :$scrollY\n\n oldScrollX: $oldScrollX \n\n oldScrollY : $oldScrollY\n")
            if(scrollY>2300 && expFlag){
                expDialog(scrollY)
            }else{
                expDialog!!.dismiss()
            }
        }

    }

    private fun setupAnim() {
        binding.progressPd.setAnimation(R.raw.progress)
        binding.progressPd.repeatCount = LottieDrawable.INFINITE
        binding.progressPd.playAnimation()
    }

    fun viewGone(){
        binding.nestedScrollPd.visibility = View.GONE
        binding.viewPd.setBackgroundColor(Color.parseColor("#F0F2F3"))
    }
    fun viewVisible(){
        binding.nestedScrollPd.visibility = View.VISIBLE
        binding.progressPd.visibility = View.GONE
        binding.progressPd.clearAnimation()

        binding.viewPd.setBackgroundColor(Color.parseColor("#FFFFFF"))
    }

    fun perfumeDetails_apicalling(id:Int){

        if (CommonUtils.checkNetwork(this@PerfumeDetailsActivity)) {
            val token = SharePref.getStringValue(this@PerfumeDetailsActivity,AppConstant.userToken)
            val apiService: APIService = RetrofitClient().getApiResponse("https://api.ohmynote.com/")
            Log.e("$TAG","\n\nperfumeDetails_apicalling \nid $id\n\n taken $token\n\n ")

            val call: Call<PerfumeDetails> = apiService.getPerfumeDatail(token!!,id)

            call.enqueue(object : Callback<PerfumeDetails>{
                override fun onResponse(call: Call<PerfumeDetails>, response: Response<PerfumeDetails>
                ) {
                    if (response.isSuccessful){

                        var perfumeDetails : PerfumeDetails = response.body()!!

                        Log.e("$TAG","\n\nperfumeDetails_apicalling \nid $id\n\n taken $token\n\n perfumeDetails : $perfumeDetails\n response.body() ${response.body()}\n\n response.code ${response.code()} ")

                        notesList.clear()
                        layerList.clear()
                        notesList.addAll(perfumeDetails.notes)
                        layerList.addAll(perfumeDetails.layer)
                        if (perfumeDetails.image == ""){

                        }else{
                            val image = perfumeDetails.image?.replace("http","https")
                            Picasso
                                .with(this@PerfumeDetailsActivity)
                                .load(image)
                                .into(binding.itemImagePd)
                        }

                        binding.tvMainTitlePd.text = perfumeDetails.name
                        binding.tvSubTitlePd.text = "${perfumeDetails.brand}, ${perfumeDetails.gender}"
                        binding.tvOlfavValuePd.text = perfumeDetails.olfactiveFamily
                        binding.tvDesignerValuePd.text = perfumeDetails.brand
                        binding.tvYearValuePd.text = perfumeDetails.yearOfRelease

                        perfumeIngrediantAdapter = PerfumeIngrediantAdapter(this@PerfumeDetailsActivity,notesList)
                        binding.rvIngredientsPd.adapter = perfumeIngrediantAdapter
                        binding.rvIngredientsPd.setHasFixedSize(true)

                        var name:String = ""
                        var brand:String = ""
                        var image:String = ""

                        if (layerList.isNotEmpty()){
                           // for (i in 0..layerList.size-1){

                                if (layerList.get(0).image == ""){

                                }else{
                                    val image = layerList.get(0).image?.replace("http","https")


                                    Picasso.with(this@PerfumeDetailsActivity).load(image).into(binding.pdImagePd)
                                }

                                this@PerfumeDetailsActivity.id1 = layerList.get(0).pk!!
                            this@PerfumeDetailsActivity.id2 = layerList.get(1).pk!!
                                binding.tvNamePd.text = layerList.get(0).name
                                binding.tvSubNamePd.text = layerList.get(0).brand
                                if (layerList.get(1).image == ""){

                                }else{
                                    val image = layerList.get(1).image?.replace("http","https")
                                    Picasso.with(this@PerfumeDetailsActivity).load(image).into(binding.pdImage1Pd)
                                }

                                binding.tvName1Pd.text = layerList.get(1).name
                                binding.tvSubName1Pd.text = layerList.get(1).brand
                          //  }
                        }

                        viewVisible()

                    }
                }

                override fun onFailure(call: Call<PerfumeDetails>, t: Throwable) {

                    Log.e("$TAG","t : ${t.printStackTrace()}\n\n ${t.message}\n\n ${t.cause}")
                    t.printStackTrace()
                }

            })

        }else{
            SharePref.toast(this@PerfumeDetailsActivity, AppConstant.noInternet)
        }
    }

    fun expDialog(scrollY:Int){
        if (expDialog == null) {
            expDialog = Dialog(this@PerfumeDetailsActivity)
        }
        expDialog!!.setContentView(R.layout.view_experiencedialod)
        val btnNo = expDialog!!.findViewById<Button>(R.id.btnNo)
        val btnYes = expDialog!!.findViewById<Button>(R.id.btnYes)

            btnNo.setOnClickListener {
                expFlag = false
                expDialog!!.dismiss()
            }

            btnYes.setOnClickListener {
                expFlag = false
                expDialog!!.dismiss()
            }

        expDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (expDialog != null && !expDialog!!.isShowing){
            if (scrollY>2000) {
                expDialog!!.show()
            }
        }
        expDialog!!.setCanceledOnTouchOutside(false)

    }

    fun tryItUi() {

        binding.cdTryitView.background = getDrawable(R.drawable.bg_select_chip)
        binding.tryItName.setTextColor(Color.parseColor("#ffffff"))
        binding.tryItIcon.setColorFilter(Color.parseColor("#ffffff"))

        binding.cdBuyItView.background = getDrawable(R.drawable.bg_filter)
        binding.buyItName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.buyItIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdNotIntView.background = getDrawable(R.drawable.bg_filter)
        binding.notIntName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.notItIcon.setColorFilter(Color.parseColor("#9C9C9C"))

    }

    fun buyItUi() {
        binding.cdTryitView.background = getDrawable(R.drawable.bg_filter)
        binding.tryItName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.tryItIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdBuyItView.background = getDrawable(R.drawable.bg_select_chip)
        binding.buyItName.setTextColor(Color.parseColor("#ffffff"))
        binding.buyItIcon.setColorFilter(Color.parseColor("#ffffff"))

        binding.cdNotIntView.background = getDrawable(R.drawable.bg_filter)
        binding.notIntName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.notItIcon.setColorFilter(Color.parseColor("#9C9C9C"))
    }

    fun notItUi() {
        binding.cdTryitView.background = getDrawable(R.drawable.bg_filter)
        binding.tryItName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.tryItIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdBuyItView.background = getDrawable(R.drawable.bg_filter)
        binding.buyItName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.buyItIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdNotIntView.background = getDrawable(R.drawable.bg_select_chip)
        binding.notIntName.setTextColor(Color.parseColor("#ffffff"))
        binding.notItIcon.setColorFilter(Color.parseColor("#ffffff"))


    }


    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}