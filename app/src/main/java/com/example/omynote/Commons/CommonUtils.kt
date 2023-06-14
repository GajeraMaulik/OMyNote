package com.example.omynote.Commons


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.omynote.R
import com.google.gson.Gson
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class CommonUtils {

    companion object {
        private var loginDialog: Dialog? = null
        val gson = Gson()
        @SuppressLint("SuspiciousIndentation")
        fun showLoginDialog(context: Context, isShowing: Boolean, title: String,visibility: Int) {

            if (loginDialog == null) {
               loginDialog = Dialog(context)

               loginDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
               loginDialog!!.setContentView(R.layout.view_logindialog)
               loginDialog!!.setCancelable(false)
               loginDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val cpTitle =loginDialog!!.findViewById<View>(R.id.sd_title) as TextView
                val sdSubTitle =loginDialog!!.findViewById<View>(R.id.sd_Subtitle) as TextView
                val okbtn =loginDialog!!.findViewById<View>(R.id.sd_Okbtn) as Button
                if (!TextUtils.isEmpty(title)) {
                    sdSubTitle.text = title
                }
                cpTitle.visibility = visibility
                okbtn.setOnClickListener {
                   loginDialog!!.dismiss()
                }
            }
            if (context is Activity) {
                if (!context.isFinishing) {
                    if (loginDialog != null && !loginDialog!!.isShowing)
                       loginDialog!!.show()
                }
            }

        }
        fun checkNetwork(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                /* //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true*/
                else -> false
            }
        }

        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                Log.e("src", src!!)
                val url = URL(src)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                val myBitmap = BitmapFactory.decodeStream(input)
                Log.e("Bitmap", "returned")
                myBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Exception", e.message!!)
                null
            }
        }

        val animationImages = arrayOf(
            R.drawable.sphere_00000,
            R.drawable.sphere_00001,
            R.drawable.sphere_00002,
            R.drawable.sphere_00003,
            R.drawable.sphere_00004,
            R.drawable.sphere_00005,
            R.drawable.sphere_00006,
            R.drawable.sphere_00007,
            R.drawable.sphere_00008,
            R.drawable.sphere_00009,
            R.drawable.sphere_00010,
            R.drawable.sphere_00011,
            R.drawable.sphere_00012,
            R.drawable.sphere_00013,
            R.drawable.sphere_00014,
            R.drawable.sphere_00015,
            R.drawable.sphere_00016,
            R.drawable.sphere_00017,
            R.drawable.sphere_00018,
            R.drawable.sphere_00019,
            R.drawable.sphere_00020,
            R.drawable.sphere_00021,
            R.drawable.sphere_00022,
            R.drawable.sphere_00023,
            R.drawable.sphere_00024,
            R.drawable.sphere_00025,
            R.drawable.sphere_00026,
            R.drawable.sphere_00027,
            R.drawable.sphere_00028,
            R.drawable.sphere_00029,
            R.drawable.sphere_00030,
            R.drawable.sphere_00031,
            R.drawable.sphere_00032,
            R.drawable.sphere_00033,
            R.drawable.sphere_00034,
            R.drawable.sphere_00035,
            R.drawable.sphere_00036,
            R.drawable.sphere_00037,
            R.drawable.sphere_00038,
            R.drawable.sphere_00039,
            R.drawable.sphere_00040,
            R.drawable.sphere_00041,
            R.drawable.sphere_00042,
            R.drawable.sphere_00043,
            R.drawable.sphere_00044,
            R.drawable.sphere_00045,
            R.drawable.sphere_00046,
            R.drawable.sphere_00047,
            R.drawable.sphere_00048,
            R.drawable.sphere_00049,
            R.drawable.sphere_00050,
            R.drawable.sphere_00051,
            R.drawable.sphere_00052,
            R.drawable.sphere_00053,
            R.drawable.sphere_00054,
            R.drawable.sphere_00055,
            R.drawable.sphere_00056,
            R.drawable.sphere_00057,
            R.drawable.sphere_00058,
            R.drawable.sphere_00059,
            R.drawable.sphere_00060,
            R.drawable.sphere_00061,
            R.drawable.sphere_00062,
            R.drawable.sphere_00063,
            R.drawable.sphere_00064,
            R.drawable.sphere_00065,
            R.drawable.sphere_00066,
            R.drawable.sphere_00067,
            R.drawable.sphere_00068,
            R.drawable.sphere_00069,
            R.drawable.sphere_00070,
            R.drawable.sphere_00071,
            R.drawable.sphere_00072,
            R.drawable.sphere_00073,
            R.drawable.sphere_00074,
            R.drawable.sphere_00075,
            R.drawable.sphere_00076,
            R.drawable.sphere_00077,
            R.drawable.sphere_00078,
            R.drawable.sphere_00079,
            R.drawable.sphere_00080,
            R.drawable.sphere_00081,
            R.drawable.sphere_00082,
            R.drawable.sphere_00083,
            R.drawable.sphere_00084,
            R.drawable.sphere_00085,
            R.drawable.sphere_00086,
            R.drawable.sphere_00087,
            R.drawable.sphere_00088,
            R.drawable.sphere_00089,
            R.drawable.sphere_00090,
            R.drawable.sphere_00091,
            R.drawable.sphere_00092,
            R.drawable.sphere_00093,
            R.drawable.sphere_00094,
            R.drawable.sphere_00095,
            R.drawable.sphere_00096,
            R.drawable.sphere_00097,
            R.drawable.sphere_00098,
            R.drawable.sphere_00099,
            R.drawable.sphere_00100,
            R.drawable.sphere_00101,
            R.drawable.sphere_00102,
            R.drawable.sphere_00103,
            R.drawable.sphere_00104,
            R.drawable.sphere_00105,
            R.drawable.sphere_00106,
            R.drawable.sphere_00107,
            R.drawable.sphere_00108,
            R.drawable.sphere_00109,
            R.drawable.sphere_00110,
            R.drawable.sphere_00111,
            R.drawable.sphere_00112,
            R.drawable.sphere_00113,
            R.drawable.sphere_00114,
            R.drawable.sphere_00115,
            R.drawable.sphere_00116,
            R.drawable.sphere_00117,
            R.drawable.sphere_00118,
            R.drawable.sphere_00119,
            R.drawable.sphere_00120,
        )

       val animationDs = arrayOf(
            R.drawable.sphere_0000,
            R.drawable.sphere_0001,
            R.drawable.sphere_0002,
            R.drawable.sphere_0003,
            R.drawable.sphere_0004,
            R.drawable.sphere_0005,
            R.drawable.sphere_0006,
            R.drawable.sphere_0007,
            R.drawable.sphere_0008,
            R.drawable.sphere_0009,
            R.drawable.sphere_0010,
            R.drawable.sphere_0011,
            R.drawable.sphere_0012,
            R.drawable.sphere_0013,
            R.drawable.sphere_0014,
            R.drawable.sphere_0015,
            R.drawable.sphere_0016,
            R.drawable.sphere_0017,
            R.drawable.sphere_0018,
            R.drawable.sphere_0019,
            R.drawable.sphere_0020,
            R.drawable.sphere_0021,
            R.drawable.sphere_0022,
            R.drawable.sphere_0023,
            R.drawable.sphere_0024,
            R.drawable.sphere_0025,
            R.drawable.sphere_0026,
            R.drawable.sphere_0027,
            R.drawable.sphere_0028,
            R.drawable.sphere_0029,
            R.drawable.sphere_0030,
            R.drawable.sphere_0031,
            R.drawable.sphere_0032,
            R.drawable.sphere_0033,
            R.drawable.sphere_0034,
            R.drawable.sphere_0035,
            R.drawable.sphere_0036,
            R.drawable.sphere_0037,
            R.drawable.sphere_0038,
            R.drawable.sphere_0039,
            R.drawable.sphere_0040,
            R.drawable.sphere_0041,
            R.drawable.sphere_0042,
            R.drawable.sphere_0043,
            R.drawable.sphere_0044,
            R.drawable.sphere_0045,
            R.drawable.sphere_0046,
            R.drawable.sphere_0047,
            R.drawable.sphere_0048,
            R.drawable.sphere_0049,
            R.drawable.sphere_0050,
            R.drawable.sphere_0051,
            R.drawable.sphere_0052,
            R.drawable.sphere_0053,
            R.drawable.sphere_0054,
            R.drawable.sphere_0055,
            R.drawable.sphere_0056,
            R.drawable.sphere_0057,
            R.drawable.sphere_0058,
            R.drawable.sphere_0059,
            R.drawable.sphere_0060,
            R.drawable.sphere_0061,
            R.drawable.sphere_0062,
            R.drawable.sphere_0063,
            R.drawable.sphere_0064,
            R.drawable.sphere_0065,
            R.drawable.sphere_0066,
            R.drawable.sphere_0067,
            R.drawable.sphere_0068,
            R.drawable.sphere_0069,
            R.drawable.sphere_0070,
            R.drawable.sphere_0071,
            R.drawable.sphere_0072,
            R.drawable.sphere_0073,
            R.drawable.sphere_0074,
            R.drawable.sphere_0075,
            R.drawable.sphere_0076,
            R.drawable.sphere_0077,
            R.drawable.sphere_0078,
            R.drawable.sphere_0079,
            R.drawable.sphere_0080,
            R.drawable.sphere_0081,
            R.drawable.sphere_0082,
            R.drawable.sphere_0083,
            R.drawable.sphere_0084,
            R.drawable.sphere_0085,
            R.drawable.sphere_0086,
            R.drawable.sphere_0087,
            R.drawable.sphere_0088,
            R.drawable.sphere_0089,
            R.drawable.sphere_0090,
            R.drawable.sphere_0091,
            R.drawable.sphere_0092,
            R.drawable.sphere_0093,
            R.drawable.sphere_0094,
            R.drawable.sphere_0095,
            R.drawable.sphere_0096,
            R.drawable.sphere_0097,
            R.drawable.sphere_0098,
            R.drawable.sphere_0099,
            R.drawable.sphere_0100,
            R.drawable.sphere_0101,
            R.drawable.sphere_0102,
            R.drawable.sphere_0103,
            R.drawable.sphere_0104,
            R.drawable.sphere_0105,
            R.drawable.sphere_0106,
            R.drawable.sphere_0107,
            R.drawable.sphere_0108,
            R.drawable.sphere_0109,
            R.drawable.sphere_0110,
            R.drawable.sphere_0111,
            R.drawable.sphere_0112,
            R.drawable.sphere_0113,
            R.drawable.sphere_0114,
            R.drawable.sphere_0115,
            R.drawable.sphere_0116,
            R.drawable.sphere_0117,
            R.drawable.sphere_0118,
            R.drawable.sphere_0119,
            R.drawable.sphere_0120,
        )

        val personalityImage = arrayOf(
            R.drawable.personality_1,
            R.drawable.personality_2,
            R.drawable.personality_3,
            R.drawable.personality_4,
            R.drawable.personality_5,
            R.drawable.personality_6,
            R.drawable.personality_7,
            R.drawable.personality_8,
            R.drawable.personality_9,
            R.drawable.personality_10,
            R.drawable.personality_11,
            R.drawable.personality_12,
            R.drawable.personality_13,
            R.drawable.personality_14,
            R.drawable.personality_15,
            R.drawable.personality_16,
            R.drawable.personality_17,
            R.drawable.personality_18,
            R.drawable.personality_19,
            R.drawable.personality_20,
            R.drawable.personality_21,
            R.drawable.personality_22,
            R.drawable.personality_23,
            R.drawable.personality_24,
            R.drawable.personality_25,
            R.drawable.personality_26,
            R.drawable.personality_27,
            R.drawable.personality_28,
            R.drawable.personality_29,
            R.drawable.personality_30,
            R.drawable.personality_31,
            R.drawable.personality_32,
            R.drawable.personality_33,
            R.drawable.personality_34,
            R.drawable.personality_35,
            R.drawable.personality_36,
            R.drawable.personality_37,
            R.drawable.personality_38,
            R.drawable.personality_39,
            R.drawable.personality_40,
            R.drawable.personality_41,
            R.drawable.personality_42,
            R.drawable.personality_43,
            R.drawable.personality_44,
            R.drawable.personality_45,
            R.drawable.personality_46,
            R.drawable.personality_47,
            R.drawable.personality_48,
            R.drawable.personality_49,
            R.drawable.personality_50,
            R.drawable.personality_51,
            R.drawable.personality_52,
            R.drawable.personality_53,
            R.drawable.personality_54,
            R.drawable.personality_55,
            R.drawable.personality_56,
            R.drawable.personality_57,
            R.drawable.personality_58,
            R.drawable.personality_59,
            R.drawable.personality_60,
            R.drawable.personality_61,
            R.drawable.personality_62,
            R.drawable.personality_63,
            R.drawable.personality_64,
            R.drawable.personality_65,
            R.drawable.personality_66,
            R.drawable.personality_67,
            R.drawable.personality_68,
            R.drawable.personality_69,
            R.drawable.personality_70,
            R.drawable.personality_71,
            R.drawable.personality_72,
            R.drawable.personality_73,
            R.drawable.personality_74,
            R.drawable.personality_75,
            R.drawable.personality_76,
        )

    }


}