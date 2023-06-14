package com.example.omynote.Commons

import android.os.Handler
import android.os.Looper
import android.widget.AdapterView.OnItemSelectedListener
import java.time.Duration

class Timer(listener:OnTimerTickListener) {

    interface OnTimerTickListener{
        fun onTimerTick(duration: String)
    }
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var duration = 0L
    private  var delay = 100L

    init {
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable,delay)
            listener.onTimerTick(format())
        }
    }

     fun start(){
        handler.postDelayed(runnable,delay)
    }

     fun pause(){
        handler.removeCallbacks(runnable)
    }
     fun stop(){
        handler.removeCallbacks(runnable)
       // duration = 0L
    }

    fun format():String{
        val millis:Long = duration % 1000
        val seconds:Long = (duration /1000)
        val min:Long = (duration / (1000 * 60)) % 60
        val hh:Long = (duration / (1000 * 60 * 60))

        val formatted = if(hh>0)
            "%02d%02d%02d%02d".format(hh,min,seconds,millis/10)
        else
            "%01d".format(seconds)

        return formatted

    }

}