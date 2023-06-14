package com.example.omynote.Views

import android.Manifest
import android.R.attr
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.omynote.Api.APIService
import com.example.omynote.Api.RetrofitClient
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.Login.LoginWithEmail
import com.example.omynote.R
import com.example.omynote.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding

    lateinit var email:String
    lateinit var password: String
    val Req_Code = 1000
    val Permission_code = 1008
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var getResult: ActivityResultLauncher<Intent>? = null
    private lateinit var permissionsRequest: ActivityResultLauncher<Array<String>>

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
                } else if (!hasPermission(this@LoginActivity, permissionsStr[i])) {
                    permissionsCount++
                }
            }
            if (pendingPermissionsList.size > 0) {
                //Some permissions are denied and can be asked again.
                askForPermissions(pendingPermissionsList)
            }

        /* else if (permissionsCount > 0) {
                //Show alert dialog
                showPermissionDialog()
            } else {
                //loginApiCall()
                requestOverlayPermission()
                //All permissions granted. Do your stuff 🤞
                // binding.txtStatus.setText("All permissions are granted!")
            }*/
        })

    // defining our own password pattern
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{5,}" +  // at least 8 characters
                "$"
    )

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
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.bgLogin)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        permissionsList = java.util.ArrayList<String>()
        permissionsList.addAll(permissionsStr.toList())

      //  permissionsRequest = getPermissionsRequest()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.etEmailLogin.setBackgroundResource(R.drawable.edittext_selector)
        binding.etPasswordLogin.setBackgroundResource(R.drawable.edittext_selector)

        var isVisiblePassword = false
        binding.ivEyeLogin.setOnClickListener {
            if (!isVisiblePassword) {
                binding.etPasswordLogin.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.ivEyeLogin.setImageResource(R.drawable.ic_visibility_on_eye)
                isVisiblePassword = true
            } else {
                binding.etPasswordLogin.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.ivEyeLogin.setImageResource(R.drawable.ic_visibility_off_eye)
                isVisiblePassword = false
            }
            binding.etPasswordLogin.setSelection(binding.etPasswordLogin.text.length)
        }
        ActivityCompat.requestPermissions(
            this,permissionsStr,
            PackageManager.PERMISSION_GRANTED
        )

   /*     getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (::email.isInitialized) {
                    login_ApiCall(email, password)
                }

            }
        }*/

        binding.btnLoginLogin.setOnClickListener {

                isValid()
         /*   if (!validateEmail() || !validatePassword()) {
               // Log.d("login_ApiCall","email :: ${email}\n\n pass : $password")

                login_ApiCall(email,password)
                return@setOnClickListener
            }*/
        }

        binding.btnLoginwithgoogleLogin.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Req_Code)
        }
        binding.tvRegisterLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }



    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Req_Code){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                startActivity(Intent(this, DashboardActivity::class.java))
              //  finish()

            }
        } catch (e: ApiException) {
            e.printStackTrace()
           // Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun isValid(): Boolean {

        var invalid = true



             email = binding.etEmailLogin.text.toString().trim()
             password = binding.etPasswordLogin.text.toString()
            if (email.isEmpty() && password.isEmpty()) {
                invalid = false

                binding.etEmailLogin.setBackgroundResource(R.drawable.error_selector)

                binding.etPasswordLogin.setBackgroundResource(R.drawable.error_selector)
               // binding.etEmailLogin.requestFocus()
               // binding.etPasswordLogin.requestFocus()

                binding.etEmailLogin.error = emailerror()


                //binding.etEmailLogin.requestFocus()

                binding.tvErrorEmailLogin.visibility = View.VISIBLE
                binding.tvErrorpassLogin.visibility = View.VISIBLE

              //


            }else {
               // CommonUtils.showLoginDialog(this,false,"")


                 if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    invalid = false
                    binding.tvErrorEmailLogin.visibility = View.VISIBLE

                }else{
                  //   CommonUtils.showLoginDialog(this,false,"")

                     invalid = true
                     binding.etEmailLogin.error = null

                     binding.tvErrorEmailLogin.visibility = View.GONE
                }


                if (password.isEmpty()){
                    invalid = false
                    binding.tvErrorpassLogin.visibility = View.VISIBLE
                }else {
                    if (password.length <= 7){
                        invalid = false
                        binding.tvErrorpassLogin.text = "Password must be at least 8 characters."
                        binding.tvErrorpassLogin.visibility = View.VISIBLE
                    }else{
                        invalid = true
                     //   CommonUtils.showLoginDialog(this,false,"")

                        binding.etPasswordLogin.error = null

                        binding.tvErrorpassLogin.visibility = View.GONE
                    }
                }



          /*      binding.etEmailLogin.setBackgroundResource(R.drawable.edittext_selector)
                binding.etPasswordLogin.setBackgroundResource(R.drawable.edittext_selector)

                binding.tvErrorEmailLogin.visibility = View.GONE
                binding.tvErrorpassLogin.visibility = View.GONE

             */
                askForPermissions(permissionsList)
                if (invalid){
                    login_ApiCall(email,password)
                }
                Log.d("login_ApiCall","email :  ${email}\n\n password : $password")
               // Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
            }

        return invalid

        }

    fun login_ApiCall(email: String,password:String){
        if (CommonUtils.checkNetwork(this)){
            val apiService: APIService = RetrofitClient().getApiResponse("https://api.ohmynote.com/")

            val call: Call<LoginWithEmail> =
                apiService.loginWithEmail(email,password)

            call.enqueue(object : Callback<LoginWithEmail>{
                override fun onResponse(
                    call: Call<LoginWithEmail>,
                    response: Response<LoginWithEmail>
                ) {
                    Log.d("login_ApiCall","code ${response.code()}\n\n body : ${response.body().toString()}\n usertoken \n apiKey :")

                    if (response.code() == 200){

                        val loginWithEmail: LoginWithEmail? = response.body()
                        val userToken = "Token ${loginWithEmail?.key}"
                        val apiKey = loginWithEmail?.api_key
                        userToken.let {
                            SharePref.save(
                                this@LoginActivity,
                                AppConstant.userToken,
                                it!!
                            )
                        }
                        apiKey.let {
                            if (apiKey != null){
                            SharePref.save(this@LoginActivity,AppConstant.apiKey,it!!)
                        }
                        }
                        SharePref.save(this@LoginActivity, AppConstant.isLogin, true)
                        val i = Intent(this@LoginActivity,DashboardActivity::class.java)
                        startActivity(i)
                       // finish()

                    }else if (response.code() == 400){
                        val loginWithEmail = LoginWithEmail()

                        try {
                            val message = loginWithEmail.fallbackMessage
                            Log.d("login_ApiCall","message :: ${message}\n")

                            CommonUtils.showLoginDialog(this@LoginActivity,true,message!!,View.VISIBLE)

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }else{

                    }


                }

                override fun onFailure(call: Call<LoginWithEmail>, t: Throwable) {
                    CommonUtils.showLoginDialog(this@LoginActivity,true,t.message!!,View.VISIBLE)
                    Log.d(" okhttp.OkHttpClient","${t.printStackTrace()}\n\n ${t.message}")
                    t.printStackTrace()
                }

            })

        }else{
            SharePref.toast(this@LoginActivity, AppConstant.noInternet)
        }
    }

    private fun emailerror(): CharSequence? {
        binding.etEmailLogin.setBackgroundResource(R.drawable.error_selector)

        return null
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

}
