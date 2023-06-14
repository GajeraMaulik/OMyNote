package com.example.omynote.Commons
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class SharePref {

    companion object {
        private const val MySharedPref = "MySharedPref"

        fun save(context: Context, key: String, value: String) {
            val sharedPref = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
            editor.commit()
        }

        fun save(context: Context, key: String, value: Int) {
            val sharedPref = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt(key, value)
            editor.apply()
            editor.commit()
        }

        fun save(context: Context, key: String, value: Boolean) {
            val sharedPref = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun save(context: Context, key: String, value: Long) {
            val sharedPref = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putLong(key, value)
            editor.apply()
        }


        fun save(context: Context, key: String?, value: Float) {
            val sharedPreferences = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        fun saveValue(context: Context, key: String?, value: Int) {
            val sharedPreferences = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(key, value!!)
            editor.apply()
        }
        fun saveArrayList(context: Context,  key: String?,value: ArrayList<String>) {
            val  sharedPreferences = context.getSharedPreferences(MySharedPref,Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json: String = gson.toJson(value)
            editor.putString(key,json)
            editor.apply()
        }
        fun getArrayList(context: Context,key: String): ArrayList<String> {
            val  sharedPreferences = context.getSharedPreferences(MySharedPref,Context.MODE_PRIVATE)
            val gson = Gson()
            val json: String = sharedPreferences.getStringSet(key, null).toString()
            val type: Type = object : TypeToken<ArrayList<String>>() {}.getType()
            return gson.fromJson(json, type)
        }

        fun getIntValue(context: Context, key: String?): Int {
            return context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE).getInt(key, -1)
        }
        fun getBooleanValue(context: Context, key: String?): Boolean {
            return context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
                .getBoolean(key, false)
        }
        fun getBoolean(context: Context,key:String?,value:Boolean):Boolean{
            return context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
                .getBoolean(key, value)
        }
        fun getStringValue(context: Context, key: String): String? {
            return context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
                .getString(key, "")
        }
        fun getLongValue(context: Context, key: String?): Long {
            return context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE).getLong(key, 0)
        }

        private fun saveMap(context: Context, key: String, inputMap: Map<String, Int>) {
            val pSharedPref: SharedPreferences =
                context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val jsonObject = JSONObject(inputMap)
            val jsonString: String = jsonObject.toString()
            val editor = pSharedPref.edit()
            editor.remove(key).apply()
            editor.putString(key, jsonString)
            editor.apply()
        }

        fun removeSharePref(context: Context) {
            val sharedPreferences = context.getSharedPreferences(MySharedPref, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun logout(context: Context,username: String?) {
            val preferences :SharedPreferences  = context.getSharedPreferences (MySharedPref, Context.MODE_PRIVATE);
            val editor  = preferences.edit()
            editor.remove(username)
          //editor.clear()
            editor.apply()

        }
        fun logout1(context: Context,id: String?) {
            val preferences :SharedPreferences  = context.getSharedPreferences (MySharedPref, Context.MODE_PRIVATE);
            val editor  = preferences.edit()
            editor.remove(id)
            //  editor.clear()
            editor.apply()

        }

        fun toast(context: Context, msg: String) {
            Toast.makeText(
                context,
                msg, Toast.LENGTH_SHORT
            ).show()
        }

    }
}







