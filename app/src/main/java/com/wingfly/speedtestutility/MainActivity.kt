package com.wingfly.speedtestutility

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.github.anastr.speedviewlib.PointerSpeedometer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val findViewById = findViewById<AwesomeSpeedometer>(R.id.speed)
        findViewById.speedTo(50F)
        /*val image = findViewById<ImageView>(R.id.main_background)
        val imageShadow = findViewById<ImageView>(R.id.main_background2)


        val shadowAnim = ObjectAnimator.ofPropertyValuesHolder(
            imageShadow,
            PropertyValuesHolder.ofFloat("scaleY", 1.25f),
            PropertyValuesHolder.ofFloat("scaleX", 1.25f),
            PropertyValuesHolder.ofFloat("alpha", 0f),
            )

        val shadowAnimatorSet = AnimatorSet()
        val scaleDownAnimatorSet = AnimatorSet()
        val scaleUpAnimatorSet = AnimatorSet()

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


        scaleDownAnimatorSet.apply {
            play(scaleDown)
            duration = 100
            start()
            doOnEnd {
                scaleUpAnimatorSet.apply {
                    play(scaleUp)
                    duration = 200
                    start()
                    doOnEnd {
                        imageShadow.visibility = View.VISIBLE
                        shadowAnimatorSet.apply {
                            play(shadowAnim)
                            duration = 1500
                            start()
                            doOnEnd {
                                scaleDownAnimatorSet.startDelay = 1300
                                scaleDownAnimatorSet.start()
                            }
                        }
                    }
                }
            }
        }*/
    }
}