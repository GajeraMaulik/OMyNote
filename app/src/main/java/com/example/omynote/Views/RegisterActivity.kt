package com.example.omynote.Views

import android.content.Intent
import android.opengl.ETC1.isValid
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.Login.LoginWithEmail
import com.example.omynote.Models.Register.RegisterData
import com.example.omynote.R
import com.example.omynote.databinding.ActivityLoginBinding
import com.example.omynote.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgLogin)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        var isVisiblePassword = false
        binding.ivEyeRegister.setOnClickListener {
            if (!isVisiblePassword) {
                binding.etPasswordRegister.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.ivEyeRegister.setImageResource(R.drawable.ic_visibility_on_eye)
                isVisiblePassword = true
            } else {
                binding.etPasswordRegister.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.ivEyeRegister.setImageResource(R.drawable.ic_visibility_off_eye)
                isVisiblePassword = false
            }
            binding.etPasswordRegister.setSelection(binding.etPasswordRegister.text.length)
        }

        binding.checkBoxRegister.setButtonDrawable(R.drawable.custom_checkbox)

        binding.btnSignupLogin.setOnClickListener {
            isValid()
        }


    }
    fun isValid(): Boolean {

        var invalid = true


        val firstName = binding.etFirstnameRegister.text.toString()
        val lastName = binding.etLastnameRegister.text.toString()
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()
        val check = binding.checkBoxRegister
        if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && password.isEmpty() && !check.isChecked) {
            invalid = false
            binding.etFirstnameRegister.setBackgroundResource(R.drawable.bg_error_ettext)
            binding.etLastnameRegister.setBackgroundResource(R.drawable.bg_error_ettext)
            binding.etEmailRegister.setBackgroundResource(R.drawable.bg_error_ettext)
            binding.etPasswordRegister.setBackgroundResource(R.drawable.bg_error_ettext)

            binding.tvErrorlNameLogin.visibility = View.VISIBLE
            binding.tvErrorfNameRegister.visibility = View.VISIBLE
            binding.tvErroreEmailLogin.visibility = View.VISIBLE
            binding.tvErrorpassRegister.visibility = View.VISIBLE
            binding.tvErrorCheckRegister.visibility = View.VISIBLE

        }

        else {
            Log.d("register","else")
            if (firstName.isEmpty()){
                invalid = false
                binding.tvErrorfNameRegister.visibility = View.VISIBLE
            }else{
                invalid = true
                binding.tvErrorfNameRegister.visibility = View.GONE
            }

                if (lastName.isEmpty()){
                 invalid = false
                binding.tvErrorlNameLogin.visibility = View.VISIBLE
                 }else{
                    invalid = true
                    binding.tvErrorlNameLogin.visibility = View.GONE
                }

                if (email.isEmpty()){
                invalid = false
                binding.tvErroreEmailLogin.visibility = View.VISIBLE
                } else{
                    invalid = true
                    binding.tvErroreEmailLogin.visibility = View.GONE
                }

            if (password.isEmpty()){
                invalid = false
                binding.tvErrorpassRegister.visibility = View.VISIBLE
            } else {
                invalid = true
                binding.tvErrorpassRegister.visibility = View.GONE
            }
            if (password.length<= 7){
                invalid = false
                binding.tvErrorpassRegister.visibility = View.VISIBLE
            }else{
                invalid = true
                binding.tvErrorpassRegister.visibility = View.GONE
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                invalid = false
                binding.tvErroreEmailLogin.visibility = View.VISIBLE

            }else{
                invalid = true
                binding.tvErroreEmailLogin.visibility = View.GONE

            }

                if (!check.isChecked){
                invalid = false
                binding.tvErrorCheckRegister.visibility = View.VISIBLE

                 }else{
                    invalid = true

                    binding.tvErrorCheckRegister.visibility = View.GONE
                 }
            Log.d("register_ApiCall","firstname : $firstName\n\n lastname : $lastName\n\n email :  ${email}\n\n password : $password")
            if (invalid){
                register_ApiCall(firstName,lastName,email,password)
            }
        }

        return invalid

    }

    fun register_ApiCall(firstname:String,lastname:String,email: String,password:String){
        if (CommonUtils.checkNetwork(this)){
            val apiService: APIService = RetrofitClient().getApiResponse("https://api.ohmynote.com/")

            val call: Call<RegisterData> =
                apiService.register(firstname,lastname,email,password,"en")

            call.enqueue(object : Callback<RegisterData>{
                override fun onResponse(
                    call: Call<RegisterData>,
                    response: Response<RegisterData>
                ) {
                    if (response.isSuccessful){

                        val registerData: RegisterData? = response.body()
                        val userToken ="Token ${registerData?.key}"
                        val apiKey = registerData?.apiKey

                        Log.d("register_ApiCall","code ${response.code()}\n\n body : ${response.body().toString()}\n usertoken : $userToken\n apiKey : $apiKey")

                        apiKey.let {
                            if (apiKey != null){
                                SharePref.save(
                                    this@RegisterActivity,
                                    AppConstant.apiKey,
                                    it!!
                                )
                            }
                        }
                        userToken.let {
                            SharePref.save(
                                this@RegisterActivity,
                                AppConstant.userToken,
                                it!!
                            )
                        }

                        val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                }

                override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                    Log.d("register_ApiCall","${t.printStackTrace()}\n\n ${t.message}")
                    t.printStackTrace()
                }

            })

        }else{
            SharePref.toast(this@RegisterActivity, AppConstant.noInternet)
        }
    }

}