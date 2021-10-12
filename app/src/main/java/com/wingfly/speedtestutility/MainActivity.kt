package com.wingfly.speedtestutility

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import java.lang.String


class MainActivity : AppCompatActivity() {
    var image: ImageView? = null
    var progressBar: ProgressBar? = null
    var status = 0
    var awesomeSpeedometer: AwesomeSpeedometer? = null
    var imageShadow: ImageView? = null
    var txtConnect: TextView? = null
    var shadowAnimatorSet: AnimatorSet? = null
    var scaleDownAnimatorSet: AnimatorSet? = null
    private val handler = Handler()
    var scaleUpAnimatorSet: AnimatorSet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        awesomeSpeedometer = findViewById<AwesomeSpeedometer>(R.id.speed)
        image = findViewById<ImageView>(R.id.main_background)
        txtConnect = findViewById<TextView>(R.id.txtConnect)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        imageShadow = findViewById<ImageView>(R.id.main_background2)

        progressBar?.visibility =View.VISIBLE



        val shadowAnim = ObjectAnimator.ofPropertyValuesHolder(
            imageShadow,
            PropertyValuesHolder.ofFloat("scaleY", 1.25f),
            PropertyValuesHolder.ofFloat("scaleX", 1.25f),
            PropertyValuesHolder.ofFloat("alpha", 0f),
        )

         shadowAnimatorSet = AnimatorSet()
         scaleDownAnimatorSet = AnimatorSet()
         scaleUpAnimatorSet = AnimatorSet()

        val scaleDown =
            ObjectAnimator.ofPropertyValuesHolder(
                image,
                PropertyValuesHolder.ofFloat("scaleY", .98f),
                PropertyValuesHolder.ofFloat("scaleX", .98f)
            )

        val scaleUp =
            ObjectAnimator.ofPropertyValuesHolder(
                image,
                PropertyValuesHolder.ofFloat("scaleY", 1f),
                PropertyValuesHolder.ofFloat("scaleX", 1f)
            )


        scaleDownAnimatorSet?.apply {
            play(scaleDown)
            duration = 100
            start()
            doOnEnd {
                scaleUpAnimatorSet.apply {
                    play(scaleUp)
                    duration = 200
                    start()
                    doOnEnd {
                        imageShadow?.visibility = View.VISIBLE
                        shadowAnimatorSet.apply {
                            play(shadowAnim)
                            duration = 1500
                            start()
                            doOnEnd {
                                scaleDownAnimatorSet!!.startDelay = 1300
                                scaleDownAnimatorSet!!.start()
                            }
                        }
                    }
                }
            }
        }


    }

    fun connectingView(view: android.view.View) {

        shadowAnimatorSet?.end()
        scaleUpAnimatorSet?.end()
        scaleDownAnimatorSet?.end()
        image?.visibility = View.GONE
        imageShadow?.visibility = View.GONE

        progressBar?.visibility=View.VISIBLE
        txtConnect?.text= "Подключение"


        Thread {
            while (status < 100) {
                status += 1
                handler.post(Runnable {
                    progressBar!!.progress = status
                })
                try {
                    Thread.sleep(16)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        // Code to show the loader here
        Handler(Looper.getMainLooper()).postDelayed({
            txtConnect?.visibility = View.GONE
            progressBar?.visibility=View.GONE
            awesomeSpeedometer?.visibility = View.VISIBLE
            awesomeSpeedometer?.speedTo(50F)
        }, 3000)
    }
}