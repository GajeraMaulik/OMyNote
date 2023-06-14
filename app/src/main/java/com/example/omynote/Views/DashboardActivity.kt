package com.example.omynote.Views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Fragments.DashboardFragment
import com.example.omynote.Models.Recipe.Recipes
import com.example.omynote.Models.Recipe.WishPerfumes
import com.example.omynote.Models.Register.RegisterData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityDashboardBinding
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding

    lateinit var dashboardFragment: DashboardFragment
    var recipeList: ArrayList<Recipes> = ArrayList()
    var wishList: ArrayList<WishPerfumes> = ArrayList()

    var userName:String? =null
    lateinit var drawerLayout: DrawerLayout
    lateinit var deleteDialog: Dialog
    lateinit var feedBackDialog:Dialog

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgLogin)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        if (animationFlag) {
            setupAnim()
        }



        runOnUiThread {
                dashboardFragment = DashboardFragment()
        }

        setCurrentFragment(dashboardFragment)

        Handler().postDelayed({
            viewVisible()
            animationFlag = false
            binding.progressDb.clearAnimation()
        },3000)


        drawerLayout = binding.drawerLayout

        var toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolBarDashboard,
            com.example.omynote.R.string.open_nav_draw,
            R.string.close_nav_draw
        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()


         userName = SharePref.getStringValue(this,AppConstant.userName)
       Log.d("username","username $userName")
        binding.tvUserName.text = userName




    }
    private fun setupAnim() {
        binding.progressDb.setAnimation(R.raw.progress)
        binding.progressDb.repeatCount = LottieDrawable.INFINITE
        binding.progressDb.playAnimation()
        viewGone()

    }

    @SuppressLint("ResourceAsColor")
    fun viewGone(){
        binding.toolBarDashboard.visibility = View.GONE
      //  binding.drawerLayout.visibility = View.GONE
        binding.flMain.visibility = View.GONE
        binding.drawerLayout.setBackgroundColor(Color.parseColor("#F0F2F3"))
    }
    fun viewVisible(){
        binding.toolBarDashboard.visibility = View.VISIBLE
       // binding.drawerLayout.visibility = View.VISIBLE
        binding.flMain.visibility = View.VISIBLE
        binding.progressDb.visibility = View.GONE
        binding.drawerLayout.setBackgroundColor(Color.parseColor("#F0F2F3"))

      //  binding.drawerLayout.setBackgroundResource(R.color.bglogin)

    }
    @SuppressLint("SuspiciousIndentation")
    private fun setCurrentFragment(fragment: Fragment) {

        if (!supportFragmentManager.isDestroyed) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_main, fragment)
                commitAllowingStateLoss()

                //commit()
            }
        }

    }

    fun nav_Dadhboard(view: View) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        R.id.llDashboard.apply {
            SharePref.toast(this@DashboardActivity, "Dashboard")
            llDashboard.setBackgroundResource(com.example.omynote.R.drawable.bg_menu_select)
            llFav.setBackgroundResource(android.R.color.transparent)
            llProfile.setBackgroundResource(android.R.color.transparent)
            llPreviousScents.setBackgroundResource(android.R.color.transparent)
            llShare.setBackgroundResource(android.R.color.transparent)
            llFeedBack.setBackgroundResource(android.R.color.transparent)
        }
        val dashboardFont = resources.getFont(com.example.omynote.R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(com.example.omynote.R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(dashboardFont)
            tvDashboard.textSize = 15F
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(regular)
        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(regular)
        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(regular)
        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(regular)
        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(regular)
        }
    }

    fun nav_Fav(view: View) {
        R.id.llFav.apply {
            SharePref.toast(this@DashboardActivity, "Fav")
            llDashboard.setBackgroundResource(android.R.color.transparent)
            llFav.setBackgroundResource(com.example.omynote.R.drawable.bg_menu_select)
            llProfile.setBackgroundResource(android.R.color.transparent)
            llPreviousScents.setBackgroundResource(android.R.color.transparent)
            llShare.setBackgroundResource(android.R.color.transparent)
            llFeedBack.setBackgroundResource(android.R.color.transparent)
        }
        val favFont = resources.getFont(com.example.omynote.R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(com.example.omynote.R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(regular)
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(favFont)
            tvFav.textSize = 15F

        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(regular)
        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(regular)
        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(regular)
        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(regular)
        }
    }

    fun nav_Profile(view: View) {
        com.example.omynote.R.id.llProfile.apply {
            SharePref.toast(this@DashboardActivity, "Profile")
            llDashboard.setBackgroundResource(android.R.color.transparent)
            llFav.setBackgroundResource(android.R.color.transparent)
            llProfile.setBackgroundResource(R.drawable.bg_menu_select)
            llPreviousScents.setBackgroundResource(android.R.color.transparent)
            llShare.setBackgroundResource(android.R.color.transparent)
            llFeedBack.setBackgroundResource(android.R.color.transparent)
        }
        val profileFont = resources.getFont(com.example.omynote.R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(com.example.omynote.R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(regular)
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(regular)

        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(profileFont)
            tvProfile.textSize = 15F

        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(regular)
        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(regular)
        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(regular)
        }
    }

    fun nav_PreviousScents(view: View) {
        R.id.llPreviousScents.apply {
            SharePref.toast(this@DashboardActivity, "PreviousScents")
            llDashboard.setBackgroundResource(android.R.color.transparent)
            llFav.setBackgroundResource(android.R.color.transparent)
            llProfile.setBackgroundResource(android.R.color.transparent)
            llPreviousScents.setBackgroundResource(R.drawable.bg_menu_select)
            llShare.setBackgroundResource(android.R.color.transparent)
            llFeedBack.setBackgroundResource(android.R.color.transparent)
        }
        val preScentFont = resources.getFont(R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(regular)
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(regular)

        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(regular)

        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(preScentFont)
            tvNavPreviousScents.textSize = 15F

        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(regular)
        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(regular)
        }

    }

    fun nav_Share(view: View) {
        R.id.llShare.apply {
            SharePref.toast(this@DashboardActivity, "Share")
            llDashboard.setBackgroundResource(android.R.color.transparent)
            llFav.setBackgroundResource(android.R.color.transparent)
            llProfile.setBackgroundResource(android.R.color.transparent)
            llPreviousScents.setBackgroundResource(android.R.color.transparent)
            llShare.setBackgroundResource(R.drawable.bg_menu_select)
            llFeedBack.setBackgroundResource(android.R.color.transparent)
        }
        val shareFont = resources.getFont(com.example.omynote.R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(com.example.omynote.R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(regular)
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(regular)

        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(regular)

        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(regular)
        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(shareFont)
            tvShare.textSize = 15F

        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(regular)
        }
    }

    fun nav_FeedBack(view: View) {
        R.id.llFeedBack.apply {
            feedBackDialog()
            SharePref.toast(this@DashboardActivity, "FeedBack")
            llDashboard.setBackgroundResource(android.R.color.transparent)
            llFav.setBackgroundResource(android.R.color.transparent)
            llProfile.setBackgroundResource(android.R.color.transparent)
            llPreviousScents.setBackgroundResource(android.R.color.transparent)
            llShare.setBackgroundResource(android.R.color.transparent)
            llFeedBack.setBackgroundResource(R.drawable.bg_menu_select)
        }
        val feedFont = resources.getFont(com.example.omynote.R.font.pulpdisplay_extrabold)
        val regular = resources.getFont(com.example.omynote.R.font.pulpdisplay_light)

        R.id.tvDashboard.apply {
            tvDashboard.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvDashboard.setTypeface(regular)
        }
        R.id.tvFav.apply {
            tvFav.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFav.setTypeface(regular)

        }
        R.id.tvProfile.apply {
            tvProfile.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvProfile.setTypeface(regular)

        }
        R.id.tvNavPreviousScents.apply {
            tvNavPreviousScents.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvNavPreviousScents.setTypeface(regular)
        }
        R.id.tvShare.apply {
            tvShare.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvShare.setTypeface(regular)
        }
        R.id.tvFeedback.apply {
            tvFeedback.setTextColor(getResources().getColor(com.example.omynote.R.color.txttitle));
            tvFeedback.setTypeface(feedFont)
            tvFeedback.textSize = 15F

        }
    }

    fun deleteAccount(view: View) {
        R.id.llDelete.apply {
            deleteDialog()
            //SharePref.toast(this@DashboardActivity, "Delete")
        }
    }

    fun btnLogout(view: View) {
        R.id.llLogout.apply {
            SharePref.save(this@DashboardActivity, AppConstant.isLogin, false)
            SharePref.logout(this@DashboardActivity, "$userName")
            SharePref.toast(this@DashboardActivity, "Logout")
            SharePref.save(this@DashboardActivity,AppConstant.userName,"")
            val i = Intent(this@DashboardActivity, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }
    }

    fun deleteAccount_ApiCall(){
        if (CommonUtils.checkNetwork(this)) {

            val token = SharePref.getStringValue(this@DashboardActivity,AppConstant.userToken)
            val clientId = SharePref.getIntValue(this@DashboardActivity,AppConstant.clientId)
            val apiService: APIService = RetrofitClient().getApiResponse("https://api.ohmynote.com/")

            val call: Call<String> =
                apiService.accountDelete(token!!,clientId)

            call.enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d(" okhttp.OkHttpClient","usertoken : $token\n client Id : $clientId\n response code : ${response.code()}\n response body : ${response.body()}")

                    if (response.isSuccessful){

                        val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }

            })
        }else{
            SharePref.toast(this@DashboardActivity,AppConstant.noInternet)
        }
    }

    fun feedBackDialog()
    {
        feedBackDialog = Dialog(this@DashboardActivity)
        feedBackDialog.setContentView(R.layout.view_feedbackdialog)
        val btnSend = feedBackDialog.findViewById<Button>(R.id.btnSend)
        val ratingBar = feedBackDialog.findViewById<RatingBar>(R.id.ratingBar)
        val btnClose = feedBackDialog.findViewById<ImageView>(R.id.icClose_fb)
        val ratigTitle = feedBackDialog.findViewById<TextView>(R.id.rating_title)
        val etFeed = feedBackDialog.findViewById<EditText>(R.id.etFeed)
        etFeed.setBackgroundResource(R.drawable.edittext_selector)


        ratingBar.setOnRatingBarChangeListener { ratingBar, f1, b ->
            Log.d("ratingBar","f1f1 :$f1\n\n bbb : $b")
            when(f1){
                1F ->{
                    ratigTitle.text = "Terrible"
                }
                2F ->{
                    ratigTitle.text = "Bad"
                }
                3F ->{
                    ratigTitle.text = "Okay"
                }
                4F ->{
                    ratigTitle.text = "Good"
                }
                5F->{
                    ratigTitle.text = "Great"
                }
            }

        }

        btnSend.setOnClickListener {
        }
        btnClose.setOnClickListener {
            feedBackDialog.dismiss()
        }

        feedBackDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        feedBackDialog.show()
        feedBackDialog.setCanceledOnTouchOutside(true)
    }
    fun deleteDialog() {
        deleteDialog = Dialog(this@DashboardActivity)

        deleteDialog.setContentView(R.layout.view_deletedialog)
        val btnYes = deleteDialog.findViewById<Button>(R.id.btnYes)
        val btnCancel = deleteDialog.findViewById<Button>(R.id.btnCancel)
        val btnClose = deleteDialog.findViewById<ImageView>(R.id.icClose)




        btnYes.setOnClickListener {
            deleteAccount_ApiCall()
        }
        btnCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
        btnClose.setOnClickListener {
            deleteDialog.dismiss()
        }

        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.show()
        deleteDialog.setCanceledOnTouchOutside(true)
    }
    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()

        }
    }

    override fun onStart() {
        super.onStart()
        dashboardFragment.getRecipe_Apicall()

        //

    }
}