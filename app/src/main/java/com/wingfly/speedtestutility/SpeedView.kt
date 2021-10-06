package com.wingfly.speedtestutility

import com.github.anastr.speedviewlib.Speedometer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.github.anastr.speedviewlib.components.Style
import com.github.anastr.speedviewlib.components.indicators.NormalIndicator
import com.github.anastr.speedviewlib.util.getRoundAngle

/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
open class SpeedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : Speedometer(context, attrs, defStyleAttr) {
    override fun defaultGaugeValues() {

    }

    override fun defaultSpeedometerValues() {
        TODO("Not yet implemented")
    }

    override fun updateBackgroundBitmap() {
        TODO("Not yet implemented")
    }


}
