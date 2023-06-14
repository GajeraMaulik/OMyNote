package com.example.omynote.Views

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieDrawable
import com.example.omynote.Adapter.*
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Fragments.wishlistAdapter
import com.example.omynote.Interface.onItemSelectedListerner
import com.example.omynote.Models.FilterData
import com.example.omynote.Models.Suggestion.MatchedPerfumes
import com.example.omynote.Models.Suggestion.SuggestionModel
import com.example.omynote.R
import com.example.omynote.databinding.ActivityDiscoverBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*


class DiscoverActivity : AppCompatActivity() {

    private val TAG: String = DiscoverActivity::class.java.getSimpleName()
    lateinit var binding: ActivityDiscoverBinding


    private var currentLocation: Location? = null
    private var REQUEST_LOCATION_CODE = 1000
    var originalFilterList: ArrayList<FilterData> = ArrayList()

    private val REQUEST_LOCATION = 1
    var locationManager: LocationManager? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var filterAdapter: FilterAdapter
    lateinit var discoverAdapter: DiscoverAdapter

    var filterName: String = ""
    var suggestionList: kotlin.collections.ArrayList<SuggestionModel> = ArrayList()
    var matchedPerfumesList: kotlin.collections.ArrayList<MatchedPerfumes> = ArrayList()
    val filterList: ArrayList<MatchedPerfumes> = ArrayList()
    var mainFilterList: ArrayList<MatchedPerfumes> = kotlin.collections.ArrayList()
    var male: Boolean = false
    var feMale: Boolean = false
    var uniSex: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor =
            ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)


        init()

        getLocation()

        suggestionList_Apicall()

        filterRecyclerViewSetup()

        binding.cdMaleView.setOnClickListener {
            maleUi()

        }

        binding.cdFemaleView.setOnClickListener {
            feMaleUi()
        }

        binding.cdUnisexView.setOnClickListener {
            uniSexUi()
        }
        binding.icCloseDiscover.setOnClickListener {
            finish()
        }

        binding.btnExitDiscover.setOnClickListener {
            finish()
        }
        binding.btnPreviousDiscover.setOnClickListener {
            finish()
        }

        binding.btnSaveDiscover.setOnClickListener {

            val i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
            finish()
        }


    }

    private fun setupAnim() {
        binding.progressDiscover.setAnimation(R.raw.progress)
        binding.progressDiscover.repeatCount = LottieDrawable.INFINITE
        binding.progressDiscover.playAnimation()

    }

    @SuppressLint("ResourceAsColor")
    fun viewGone() {
        binding.toolBarDiscover.visibility = View.GONE
        //  binding.drawerLayout.visibility = View.GONE
        binding.constraintLayout.visibility = View.GONE
        binding.viewDiscover.setBackgroundResource(R.color.bgLogin)
        window.statusBarColor =
            ContextCompat.getColor(this, com.example.omynote.R.color.bgLogin)
    }

    fun viewVisible() {
        binding.toolBarDiscover.visibility = View.VISIBLE
        // binding.drawerLayout.visibility = View.VISIBLE
        binding.constraintLayout.visibility = View.VISIBLE
        binding.progressDiscover.visibility = View.GONE
        binding.progressDiscover.clearAnimation()
        binding.viewDiscover.setBackgroundResource(R.color.colorPrimary)
        window.statusBarColor =
            ContextCompat.getColor(this, com.example.omynote.R.color.colorPrimary)
        //  binding.drawerLayout.setBackgroundResource(R.color.bglogin)

    }

    fun filterRecyclerViewSetup() {
        originalFilterList.clear()

        originalFilterList.add(FilterData(0, R.drawable.ic_unselect_all, "All"))
        originalFilterList.add(FilterData(1, R.drawable.ic_selector_fragrances, "Fragrances"))
        originalFilterList.add(FilterData(2, R.drawable.ic_selector_body, "Body"))
        originalFilterList.add(FilterData(3, R.drawable.ic_selector_oils, "Oils"))
        originalFilterList.add(FilterData(4, R.drawable.ic_selector_hair, "Hair"))
        originalFilterList.add(FilterData(5, R.drawable.ic_selector_home, "Home"))
        originalFilterList.add(FilterData(6, R.drawable.ic_selector_bath, "Bath"))
        originalFilterList.add(FilterData(7, R.drawable.ic_selector_more, "More"))

        filterAdapter =
            FilterAdapter(
                this@DiscoverActivity,
                originalFilterList,
                object : onItemSelectedListerner {
                    override fun onItemSelected(filterName: String, filterId: Int) {

                        this@DiscoverActivity.filterName = filterName

                        when (filterName) {
                            "All" -> {

                                if (male) {
                                    Log.e(
                                        "$TAG",
                                        "male : $male"
                                    )
                                    mainFilterList.clear()


                                    val list = filterList.filter {
                                        it.gender == "male"
                                    }

                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {
                                    Log.e(
                                        "$TAG",
                                        "feMale : $feMale"
                                    )

                                    val list = filterList.filter {
                                        it.gender == "female"
                                    }

                                    mainFilterList.clear()
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {
                                    Log.e(
                                        "$TAG",
                                        "uniSex : $uniSex"
                                    )

                                    val list = filterList.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.clear()
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }
                            }
                            "Fragrances" -> {

                                if (male) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.perfume == true

                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }

                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {
                                    val list1 = filterList.filter {
                                        it.perfume == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }

                                    mainFilterList.clear()
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {

                                    val list1 = filterList.filter {
                                        it.perfume == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.clear()
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }


                            }
                            "Body" -> {


                                if (male) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.body == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }

                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.body == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.body == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }

                            }
                            "Oils" -> {


                                if (male) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.oil == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.oil == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }
                                    mainFilterList.addAll(list)
                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.oil == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.addAll(list)


                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }

                            }
                            "Hair" -> {


                                if (male) {
                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.hair == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.hair == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }
                                    mainFilterList.addAll(list)


                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {
                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.hair == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }

                            }
                            "Home" -> {


                                if (male) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.home == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }

                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.home == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.home == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }
                            }
                            "Bath" -> {

                                if (male) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.clean == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "male"
                                    }

                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else if (feMale) {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.clean == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "female"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                } else {

                                    mainFilterList.clear()

                                    val list1 = filterList.filter {
                                        it.clean == true
                                    }
                                    val list = list1.filter {
                                        it.gender == "unisex"
                                    }
                                    mainFilterList.addAll(list)

                                    if (mainFilterList.isEmpty()){
                                        binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                        binding.btnMoreDiscover.visibility = View.VISIBLE
                                        binding.rvAllFilterlist.visibility = View.GONE

                                    }else{
                                        binding.tvNotFoundDiscover.visibility = View.GONE
                                        binding.btnMoreDiscover.visibility = View.GONE
                                        binding.rvAllFilterlist.visibility = View.VISIBLE
                                        discoverAdapter = DiscoverAdapter(
                                            this@DiscoverActivity,
                                            suggestionList,
                                            mainFilterList
                                        )
                                        binding.rvAllFilterlist.adapter = discoverAdapter
                                    }
                                }
                            }
                            "More" -> {
                                val list = matchedPerfumesList
                                // list.clear()
                                discoverAdapter.updateList(list)
                                binding.rvAllFilterlist.adapter = discoverAdapter

                            }
                            else -> {

                                discoverAdapter = DiscoverAdapter(
                                    this@DiscoverActivity,
                                    suggestionList,
                                    matchedPerfumesList
                                )
                                binding.rvAllFilterlist.adapter = discoverAdapter

                            }

                        }

                    }

                    override fun onItemunSelected(arraylist: String, intArrayList: Int) {
                    }

                })
        binding.rvFilterlist.adapter = filterAdapter

    }

    fun suggestionList_Apicall() {
        if (CommonUtils.checkNetwork(this@DiscoverActivity)) {
            val url = "https://mocki.io/"
            val apiService: APIService = RetrofitClient().getApiResponse(url)
            val token = SharePref.getStringValue(this@DiscoverActivity, AppConstant.userToken)

            val latitude = DecimalFormat("##.##").format(latitude)
            val longitude = DecimalFormat("##.##").format(longitude)

            val call: Call<SuggestionModel> = apiService.suggestionList()

            call.enqueue(object : Callback<SuggestionModel> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<SuggestionModel>,
                    response: Response<SuggestionModel>
                ) {
                    if (response.code() == 200) {

                        viewVisible()
                        val gson = Gson()

                        val suggestionModel = gson.toJson(response.body())

                        SharePref.save(
                            this@DiscoverActivity,
                            AppConstant.suggestionModel,
                            suggestionModel
                        )
                        val suggestionDetails =
                            SharePref.getStringValue(
                                this@DiscoverActivity,
                                AppConstant.suggestionModel
                            )
                        var suggestionDetailsData: SuggestionModel =
                            CommonUtils.gson.fromJson(
                                suggestionDetails,
                                SuggestionModel::class.java
                            )

                        //suggestionList.clear()
                        suggestionList.add(suggestionDetailsData)

                        val client = suggestionDetailsData.client
                        matchedPerfumesList = suggestionDetailsData.matchedPerfumes

                        filterList.addAll(matchedPerfumesList)


                        if (male) {
                            Log.e(
                                "$TAG",
                                "male : $male"
                            )
                            mainFilterList.clear()


                            val list = filterList.filter {
                                it.gender == "male"
                            }

                            mainFilterList.addAll(list)

                            if (mainFilterList.isEmpty()){
                                binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                binding.btnMoreDiscover.visibility = View.VISIBLE
                                binding.rvAllFilterlist.visibility = View.GONE

                            }else{
                                binding.tvNotFoundDiscover.visibility = View.GONE
                                binding.btnMoreDiscover.visibility = View.GONE
                                binding.rvAllFilterlist.visibility = View.VISIBLE
                                discoverAdapter = DiscoverAdapter(
                                    this@DiscoverActivity,
                                    suggestionList,
                                    mainFilterList
                                )
                                binding.rvAllFilterlist.adapter = discoverAdapter
                            }

                        } else if (feMale) {
                            Log.e(
                                "$TAG",
                                "feMale : $feMale"
                            )

                            val list = filterList.filter {
                                it.gender == "female"
                            }

                            mainFilterList.clear()
                            mainFilterList.addAll(list)

                            if (mainFilterList.isEmpty()){
                                binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                binding.btnMoreDiscover.visibility = View.VISIBLE
                                binding.rvAllFilterlist.visibility = View.GONE

                            }else{
                                binding.tvNotFoundDiscover.visibility = View.GONE
                                binding.btnMoreDiscover.visibility = View.GONE
                                binding.rvAllFilterlist.visibility = View.VISIBLE
                                discoverAdapter = DiscoverAdapter(
                                    this@DiscoverActivity,
                                    suggestionList,
                                    mainFilterList
                                )
                                binding.rvAllFilterlist.adapter = discoverAdapter
                            }

                        } else {
                            Log.e(
                                "$TAG",
                                "uniSex : $uniSex"
                            )
                            val list = filterList.filter {
                                it.gender == "unisex"
                            }
                            mainFilterList.clear()
                            mainFilterList.addAll(list)

                            if (mainFilterList.isEmpty()){
                                binding.tvNotFoundDiscover.visibility = View.VISIBLE
                                binding.btnMoreDiscover.visibility = View.VISIBLE
                                binding.rvAllFilterlist.visibility = View.GONE

                            }else{
                                binding.tvNotFoundDiscover.visibility = View.GONE
                                binding.btnMoreDiscover.visibility = View.GONE
                                binding.rvAllFilterlist.visibility = View.VISIBLE
                                discoverAdapter = DiscoverAdapter(
                                    this@DiscoverActivity,
                                    suggestionList,
                                    mainFilterList
                                )
                                binding.rvAllFilterlist.adapter = discoverAdapter
                            }

                        }
                    }
                    Log.d(
                        "suggestionList_Apicall",
                        "\n filterList ${filterList.size}\n\n filter : ${filterList.toString()} recponse :: ${
                            response.body().toString()
                        }\n\n code : ${response.code()}"
                    )
                }

                override fun onFailure(call: Call<SuggestionModel>, t: Throwable) {
                    Log.e(
                        "suggestionList_Apicall",
                        "onFailure : ${t.printStackTrace()}\n\n ${t.message}\n\n ${t.cause}"
                    )
                    t.printStackTrace()
                }


            })

        } else {
            SharePref.toast(this@DiscoverActivity, AppConstant.noInternet)
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this@DiscoverActivity, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@DiscoverActivity, ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            );
        } else {
            try {

                var locationGPS: Location? =
                    locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    var lat: Double = locationGPS.getLatitude();
                    var longi: Double = locationGPS.getLongitude();
                    latitude = lat
                    longitude = longi


                    SharePref.toast(this, "latitude $latitude\n longgitude $longitude")

                    Log.e("location", "\nlatitude $latitude\n longgitude $longitude")
                    /* showLocation.setText("Your Location: " + "
                         " + "Latitude: " + latitude + "
                         " + "Longitude: " + longitude);*/
                } else {
                    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAddress(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return addresses!!.get(0).getAddressLine(0).toString()
    }

    fun maleUi() {

        male = true
        feMale = false
        uniSex = false

        binding.cdMaleView.background = getDrawable(R.drawable.bg_select_chip)
        binding.maleName.setTextColor(Color.parseColor("#ffffff"))
        binding.maleIcon.setColorFilter(Color.parseColor("#ffffff"))

        binding.cdFemaleView.background = getDrawable(R.drawable.bg_filter)
        binding.feMaleName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.femaleIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdUnisexView.background = getDrawable(R.drawable.bg_filter)
        binding.unisexName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.unisexIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        when (filterName) {
            "All" -> {

                mainFilterList.clear()

                val list1 = matchedPerfumesList.filter {
                    it.gender == "male"
                }

                mainFilterList.addAll(list1)
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Fragrances" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.perfume == true
                }
                val list1 = list.filter {
                    it.gender == "male"
                }

                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Body" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.body == true
                }
                val list1 = list.filter {
                    it.gender == "male"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }

            "Oils" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.oil == true
                }

                val list1 = list.filter {
                    it.gender == "male"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
              /*  Log.e(
                    "$TAG", "filter  $filterName\n" +
                            "\n" +
                            " filterList.size : ${filterList.size}\n" +
                            "\n" +
                            "filterList : $filterList"
                )*/
            }
            "Hair" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.hair == true
                }
                val list1 = list.filter {
                    it.gender == "male"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Home" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.home == true
                }

                val list1 = list.filter {
                    it.gender == "male"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Bath" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.clean == true
                }

                val list1 = list.filter {
                    it.gender == "male"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "More" -> {
                val list = matchedPerfumesList
                // list.clear()
                discoverAdapter.updateList(list)
                binding.rvAllFilterlist.adapter = discoverAdapter

            }
            else -> {

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }

        }
    }

    fun feMaleUi() {

        feMale = true
        male = false
        uniSex = false

        binding.cdMaleView.background = getDrawable(R.drawable.bg_filter)
        binding.maleName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.maleIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdFemaleView.background = getDrawable(R.drawable.bg_select_chip)
        binding.feMaleName.setTextColor(Color.parseColor("#ffffff"))
        binding.femaleIcon.setColorFilter(Color.parseColor("#ffffff"))

        binding.cdUnisexView.background = getDrawable(R.drawable.bg_filter)
        binding.unisexName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.unisexIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        when (filterName) {
            "All" -> {
                mainFilterList.clear()
               /* Log.e(
                    "$TAG",
                    "filterName : $filterName\n\n matchedPerfumesList.size : ${matchedPerfumesList.size}\n\n matchedPerfumesList : $matchedPerfumesList"
                )*/
                val list1 = matchedPerfumesList.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Fragrances" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.perfume == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Body" -> {
                mainFilterList.clear()

                val list = filterList.filter {
                    it.body == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Oils" -> {
                mainFilterList.clear()

                val list = filterList.filter {
                    it.oil == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Hair" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.hair == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Home" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.home == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)
               /* Log.e(
                    "$TAG",
                    "filterName : $filterName\n\n mainFilterList.size :::  ${mainFilterList.size}\n\n mainFilterList :::  $mainFilterList"
                )*/
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Bath" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.clean == true
                }

                val list1 = list.filter {
                    it.gender == "female"
                }
                mainFilterList.addAll(list1)
                /* Log.e(
                     "$TAG",
                     "filterName : $filterName\n\n mainFilterList.size :::  ${mainFilterList.size}\n\n mainFilterList :::  $mainFilterList"
                 )*/
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "More" -> {
                val list = matchedPerfumesList
                // list.clear()
                discoverAdapter.updateList(list)
                binding.rvAllFilterlist.adapter = discoverAdapter

            }
            else -> {
              /*  Log.e(
                    "$TAG", "filter else $filterName\n" +
                            "\n" +
                            " matchedPerfumesList.size : ${matchedPerfumesList.size}\n" +
                            "\n" +
                            "matchedPerfumesList : $matchedPerfumesList"
                )*/
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }

        }

    }

    fun uniSexUi() {
        feMale = false
        male = false
        uniSex = true
        binding.cdMaleView.background = getDrawable(R.drawable.bg_filter)
        binding.maleName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.maleIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdFemaleView.background = getDrawable(R.drawable.bg_filter)
        binding.feMaleName.setTextColor(Color.parseColor("#2D2D2D"))
        binding.femaleIcon.setColorFilter(Color.parseColor("#9C9C9C"))

        binding.cdUnisexView.background = getDrawable(R.drawable.bg_select_chip)
        binding.unisexName.setTextColor(Color.parseColor("#ffffff"))
        binding.unisexIcon.setColorFilter(Color.parseColor("#ffffff"))

        when (filterName) {
            "All" -> {

                mainFilterList.clear()

                val list1 = matchedPerfumesList.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Fragrances" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.perfume == true
                }

                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Body" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.body == true
                }
                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Oils" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.oil == true
                }

                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Hair" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.hair == true
                }

                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }

            }
            "Home" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.home == true
                }

                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
            "Bath" -> {

                mainFilterList.clear()

                val list = filterList.filter {
                    it.clean == true
                }

                val list1 = list.filter {
                    it.gender == "unisex"
                }
                mainFilterList.addAll(list1)

                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }


            }
            "More" -> {
                val list = matchedPerfumesList
                // list.clear()
                discoverAdapter.updateList(list)
                binding.rvAllFilterlist.adapter = discoverAdapter

            }
            else -> {
                if (mainFilterList.isEmpty()){
                    binding.tvNotFoundDiscover.visibility = View.VISIBLE
                    binding.btnMoreDiscover.visibility = View.VISIBLE
                    binding.rvAllFilterlist.visibility = View.GONE

                }else{
                    binding.tvNotFoundDiscover.visibility = View.GONE
                    binding.btnMoreDiscover.visibility = View.GONE
                    binding.rvAllFilterlist.visibility = View.VISIBLE
                    discoverAdapter = DiscoverAdapter(
                        this@DiscoverActivity,
                        suggestionList,
                        mainFilterList
                    )
                    binding.rvAllFilterlist.adapter = discoverAdapter
                }
            }
        }
    }

    fun init() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding.maleIcon.setColorFilter(Color.parseColor("#ffffff"))
        male = true
        filterName = "All"
        filterAdapter = FilterAdapter()
        wishlistAdapter = WishlistAdapter()
        discoverAdapter = DiscoverAdapter()
        mainFilterList.clear()

        setupAnim()
        viewGone()

    }


}