package com.example.omynote.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieDrawable
import com.example.omynote.Adapter.PreviousScentAdapter
import com.example.omynote.Adapter.WishlistAdapter
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.Recipe.RecipeModel
import com.example.omynote.Models.Recipe.Recipes
import com.example.omynote.Models.Recipe.WishPerfumes
import com.example.omynote.R
import com.example.omynote.Views.DashboardActivity
import com.example.omynote.Views.ShareStoryActivity
import com.example.omynote.Views.animationFlag
import com.example.omynote.databinding.FragmentDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var wishlistAdapter: WishlistAdapter
var wishList: ArrayList<WishPerfumes> = ArrayList()

class DashboardFragment : Fragment() {


    private lateinit var binding: FragmentDashboardBinding
    lateinit var previousScentAdapter : PreviousScentAdapter
    var recipeList: ArrayList<Recipes> = ArrayList()
    var userName:String?= null

    lateinit var dashboardActivity: DashboardActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        previousScentAdapter  = PreviousScentAdapter()
        wishlistAdapter = WishlistAdapter()
        dashboardActivity = DashboardActivity()


        binding.rvPreviousScents.isNestedScrollingEnabled = true;

    /*
         binding.progressDb.addAnimatorUpdateListener { animation ->
             animation.start()

         }*/
        //setupAnim()

       // viewGone()

        binding.btnNext.setOnClickListener {
            val intent = Intent(requireContext(), ShareStoryActivity::class.java)
            startActivity(intent)

        }

        return binding.root
    }
    private fun setupAnim() {
        binding.progressFd.setAnimation(R.raw.progress)
        binding.progressFd.repeatCount = LottieDrawable.INFINITE
        binding.progressFd.playAnimation()
    }

    fun viewGone(){
        binding.bottomView.visibility = View.GONE
        binding.nestedScroll.visibility = View.GONE
    }
    fun viewVisible(){
        binding.bottomView.visibility = View.VISIBLE
        binding.nestedScroll.visibility = View.VISIBLE
        binding.progressFd.visibility = View.GONE

    }

    fun getRecipe_Apicall(){
        if (CommonUtils.checkNetwork(requireContext())){
            val apiService: APIService = RetrofitClient().getApiResponse("https://api.ohmynote.com/")
            val token = SharePref.getStringValue(requireContext(), AppConstant.userToken)
            val call: Call<RecipeModel> =
                apiService.getRecipe(token!!)

            call.enqueue(object : Callback<RecipeModel> {
                override fun onResponse(call: Call<RecipeModel>, response: Response<RecipeModel>) {
                    if (response.isSuccessful){


                      //  viewVisible()


                        val recipeModel: RecipeModel? = response.body()

                        val client  = recipeModel?.client
                        val recipes :ArrayList<Recipes> = recipeModel?.recipes!!

                        val wishPerfumes :ArrayList<WishPerfumes> = recipeModel?.client!!.wishPerfumes

                        Log.d("okhttp.OkHttpClient","client : ${client!!.id}\n usertoken : $token\n")

                        userName =  "Hello ${client!!.firstName}!"
                        SharePref.save(requireContext(),AppConstant.userName, userName!!)
                        SharePref.save(requireContext(),AppConstant.clientId,client.id!!)
                    //    dashboardActivity.binding.tvUserName.text = userName

                        //  binding.tvUserName.text = userName


                        recipeList.clear()
                        wishList.clear()
                        recipeList.addAll(recipes)
                        wishList.addAll(wishPerfumes)

                        if (recipeList.isEmpty()) {

                            val intent = Intent(requireContext(), ShareStoryActivity::class.java)
                            startActivity(intent)

                            //  Log.d("scentList", "\nempty : $recipeList")
                        } else {
                            //  Log.d("scentList", "\nnot empty : ${recipeList.size}")
                            //  Log.d("scentList", "\nnot empty : ${recipeList.toString()}")
                            var selectdata: String = ""


                            if (recipeList.size > 3) {
                                // topThreeScentList.clear()
                                recipeList = recipeList.take(3) as ArrayList<Recipes>
                                previousScentAdapter = PreviousScentAdapter(requireContext(),recipeList)
                                binding.rvPreviousScents.adapter = previousScentAdapter

                            } else {
                                // Log.e("recipeList", "before recipeList:  ${recipeList.toString()} ")
                                if (recipeList.size > 3) {

                                    recipeList = recipeList.take(3) as ArrayList<Recipes>

                                } else {
                                    previousScentAdapter = PreviousScentAdapter(requireContext(),recipeList)
                                    binding.rvPreviousScents.adapter = previousScentAdapter

                                    //  Log.d("recipeList", "\nrecipeList :::: ${recipeList.size}")
                                }
                                previousScentAdapter = PreviousScentAdapter(requireContext(),recipeList)
                                binding.rvPreviousScents.adapter = previousScentAdapter


                                //  Log.e("recipeList", "\n\nAfter : ${recipeList.toString()} ")
                            }
                        }

                        if(wishList.isEmpty()){
                            hideView()
                        }else{
                            showView()
                            wishlistAdapter = WishlistAdapter(requireContext(),wishList)
                            binding.rvWishlist.adapter = wishlistAdapter

                        }


                     //   Handler().postDelayed({
                          //  dashboardActivity.viewVisible()

                            animationFlag = false
                           // dashboardActivity.binding.progressDb.clearAnimation()
                      //  },3000)




                        Log.d("getRecipe_Apicall","\n\nusername :: ${userName}\n\nwishList :: ${wishList.size}\n\n ${wishList.toString()}\n\ngetRecipe_Apicall ::${recipeModel.toString()}\n\n recponse :: ${response.body().toString()}\n\n code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RecipeModel>, t: Throwable) {
                }


            })

        }else{
            SharePref.toast(requireContext(), AppConstant.noInternet)
        }
    }

    fun hideView(){

        binding.tvWishlist.visibility = View.GONE
        binding.tvSeeAllWishlist.visibility = View.GONE
        binding.btnWishlist.visibility = View.GONE
        binding.rvWishlist.visibility = View.GONE
    }

    fun showView(){
        binding.tvWishlist.visibility = View.VISIBLE
        binding.tvSeeAllWishlist.visibility = View.VISIBLE
        binding.btnWishlist.visibility = View.VISIBLE
        binding.rvWishlist.visibility = View.VISIBLE

    }


    override fun onStart() {
        super.onStart()
        getRecipe_Apicall()
    }
    companion object {

    }
}