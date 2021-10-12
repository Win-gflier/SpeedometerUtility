package com.wingfly.speedtestutility


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.github.anastr.speedviewlib.Speedometer
import com.github.anastr.speedviewlib.components.Style
import com.github.anastr.speedviewlib.components.indicators.NeedleIndicator
import com.github.anastr.speedviewlib.components.indicators.SpindleIndicator
import com.github.anastr.speedviewlib.util.getRoundAngle
import android.graphics.RectF




/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
open class AwesomeSpeedometer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : Speedometer(context, attrs, defStyleAttr) {

    private val markPath = Path()
    private val markLargePath = Path()
    private val speedometerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val speedometerLargePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //    private val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val pointerBackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val customMarkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val trianglesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val speedometerRect = RectF()
    private val speedometerCenterRect = RectF()
    private val speedometerCenterInterRect = RectF()
    private val trianglesPath = Path()

    private var speedometerColor = 0xFFEEEEEE.toInt()
    private var pointerColor = 0xFFFFFFFF.toInt()

    private var withPointer = true

    /**
     * change the color of the center circle.
     */
    var centerCircleColor: Int
        get() = circlePaint.color
        set(centerCircleColor) {
            circlePaint.color = centerCircleColor
            if (isAttachedToWindow)
                invalidate()
        }

    /**
     * change the width of the center circle.
     */
    var centerCircleRadius = dpTOpx(35f)
        set(centerCircleRadius) {
            field = centerCircleRadius
            if (isAttachedToWindow)
                invalidate()
        }

    /**
     * enable to draw circle pointer on speedometer arc.
     *
     * this will not make any change for the Indicator.
     *
     * true: draw the pointer,
     * false: don't draw the pointer.
     */
    var isWithPointer: Boolean
        get() = withPointer
        set(withPointer) {
            this.withPointer = withPointer
            if (isAttachedToWindow)
                invalidate()
        }

    var trianglesColor: Int
        get() = trianglesPaint.color
        set(trianglesColor) {
            trianglesPaint.color = trianglesColor
            invalidateGauge()
        }

    init {
        init()
        initAttributeSet(context, attrs)
    }

    override fun defaultGaugeValues() {
        super.speedometerWidth = dpTOpx(10f)
        super.textColor = 0xFFFFFFFF.toInt()
        super.speedTextColor = 0xFFFFFFFF.toInt()
        super.unitTextColor = 0xFFFFFFFF.toInt()
        super.speedTextSize = dpTOpx(24f)
        super.unitTextSize = dpTOpx(11f)
        super.speedTextTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        super.speedTextPosition = Position.CENTER
        super.unitUnderSpeedText = true
    }

    override fun defaultSpeedometerValues() {
        super.marksNumber = 8
        super.marksPadding = speedometerWidth + dpTOpx(12f)
        super.markStyle = Style.ROUND
        super.markHeight = dpTOpx(5f)
        super.markWidth = dpTOpx(2f)
        indicator = SpindleIndicator(context)
        indicator.apply {
            width = dpTOpx(10f)
            color = 0xFFFFFFFF.toInt()

        }
        super.backgroundCircleColor = 0xff48cce9.toInt()
    }

    private fun init() {
        unit="Mbps"
        textPaint.textAlign = Paint.Align.CENTER
        speedometerPaint.style = Paint.Style.STROKE
        speedometerPaint.strokeCap = Paint.Cap.ROUND
        speedometerLargePaint.style = Paint.Style.STROKE
        speedometerLargePaint.strokeCap = Paint.Cap.ROUND
        circlePaint.color = 0xFFFFFFFF.toInt()
        customMarkPaint.style = Paint.Style.STROKE
        trianglesPaint.color = 0xff3949ab.toInt()

    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            initAttributeValue()
            return
        }
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PointerSpeedometer, 0, 0)

        speedometerColor =
            a.getColor(R.styleable.PointerSpeedometer_sv_speedometerColor, speedometerColor)
        pointerColor = a.getColor(R.styleable.PointerSpeedometer_sv_pointerColor, pointerColor)
        circlePaint.color =
            a.getColor(R.styleable.PointerSpeedometer_sv_centerCircleColor, circlePaint.color)
        centerCircleRadius =
            a.getDimension(R.styleable.SpeedView_sv_centerCircleRadius, centerCircleRadius)
        withPointer = a.getBoolean(R.styleable.PointerSpeedometer_sv_withPointer, withPointer)
        trianglesPaint.color = a.getColor(com.github.anastr.speedviewlib.R.styleable.AwesomeSpeedometer_sv_trianglesColor, trianglesPaint.color)
        a.recycle()
        initAttributeValue()
    }

    private fun initAttributeValue() {
//        pointerPaint.color = pointerColor
    }


    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)

        val risk = speedometerWidth * .5f + dpTOpx(24f) + padding.toFloat()
        speedometerRect.set(risk, risk, size - risk, size - risk)

        val riskCenter = speedometerWidth * .5f + dpTOpx(70f) + padding.toFloat()
        speedometerCenterRect.set(riskCenter, riskCenter, size - riskCenter, size - riskCenter)

        val riskCenterInter = speedometerWidth * .5f + dpTOpx(74f) + padding.toFloat()
        speedometerCenterInterRect.set(riskCenterInter, riskCenterInter, size - riskCenterInter, size - riskCenterInter)

        updateRadial()
        updateBackgroundBitmap()
    }

    private fun initDraw() {
        speedometerPaint.strokeWidth = speedometerWidth
        speedometerLargePaint.strokeWidth = speedometerWidth + 35
        speedometerPaint.shader = updateSweep()
        speedometerLargePaint.shader = updateLargeSweep()
        customMarkPaint.color = markColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initDraw()

        val roundAngle = getRoundAngle(speedometerWidth, speedometerRect.width())
        canvas.drawArc(
            speedometerRect,
            getStartDegree() + roundAngle,
            (getEndDegree() - getStartDegree()) - roundAngle * 2f,
            false,
            speedometerLargePaint
        )
        canvas.drawArc(
            speedometerRect,
            getStartDegree() + roundAngle,
            (getEndDegree() - getStartDegree()) - roundAngle * 2f,
            false,
            speedometerPaint
        )


        canvas.drawArc(
            speedometerCenterRect,
            getStartDegree() + roundAngle,
            (getEndDegree() - getStartDegree()) - roundAngle * 2f,
            false,
            markPaint
        )

        canvas.drawArc(
            speedometerCenterInterRect,
            getStartDegree() + roundAngle,
            (getEndDegree() - getStartDegree()) - roundAngle * 2f,
            false,
            markPaint
        )



        if (withPointer) {
            canvas.save()
            canvas.rotate(90 + degree, size * .5f, size * .5f)
//            canvas.drawCircle(
//                size * .5f,
//                speedometerWidth * .5f + dpTOpx(8f) + padding.toFloat(),
//                speedometerWidth * .5f + dpTOpx(8f),
//                pointerBackPaint
//            )

//            canvas.drawCircle(
//                size * .5f,
//                speedometerWidth * .5f + dpTOpx(8f) + padding.toFloat(),
//                speedometerWidth * .5f + dpTOpx(8f),
//                pointerBackPaint
//            )
//            canvas.drawCircle(
//                size * .5f,
//                speedometerWidth * .5f + dpTOpx(8f) + padding.toFloat(),
//                speedometerWidth * .5f + dpTOpx(1f),
//                pointerPaint
//            )
            canvas.restore()
        }


        drawIndicator(canvas)
        indicator.withEffects(true)
        val c = centerCircleColor
        circlePaint.color =
            Color.argb((Color.alpha(c) * .5f).toInt(), Color.red(c), Color.green(c), Color.blue(c))
        canvas.drawCircle(size * .5f, size * .5f, centerCircleRadius + dpTOpx(6f), circlePaint)
        circlePaint.color = c
        canvas.drawCircle(size * .5f, size * .5f, centerCircleRadius, circlePaint)

        drawNotes(canvas)
        drawSpeedUnitText(canvas)
    }

    override fun updateBackgroundBitmap() {
        val c = createBackgroundBitmapCanvas()
        initDraw()

        val markH = viewSizePa / 40f
        markPath.reset()
        markPath.moveTo(size * .5f,marksPadding+ padding.toFloat())
        markPath.lineTo(size * .5f, markH + marksPadding+padding)
        customMarkPaint.strokeWidth = markH / 5f

        markLargePath.reset()
        markLargePath.moveTo(size * .5f,marksPadding+ padding.toFloat())
        markLargePath.lineTo(size * .5f, markH+5 + marksPadding+padding)



        val triangleHeight = viewSizePa / 20f
        initTickPadding = triangleHeight

        trianglesPath.reset()
        trianglesPath.moveTo(size * .5f, padding.toFloat())
        val triangleWidth = viewSize / 20f
        trianglesPath.lineTo(size * .5f - triangleWidth / 2f,marksPadding+ padding.toFloat())
        trianglesPath.lineTo(size * .5f + triangleWidth / 2f,marksPadding+ padding.toFloat())




        drawCustomMarks(c)

        if (tickNumber > 0)
            drawTicks(c)
        else
            drawDefMinMaxSpeedPosition(c)
    }


    protected fun drawCustomMarks(c: Canvas) {
        c.save()
        c.rotate(90f + getStartDegree(), size * .5f, size * .5f)
        val everyDegree = (getEndDegree() - getStartDegree()) / (marksNumber + 1f)
        for (i in 1..marksNumber) {
            c.rotate(everyDegree, size * .5f, size * .5f)
            if (i%3==0){
                markPaint.strokeWidth = 2.0f
                c.drawPath(markLargePath, markPaint)
            }else{
                markPaint.strokeWidth = 1.0f
                c.drawPath(markPath, markPaint)
            }
        }
        c.restore()
    }


    private fun updateSweep(): SweepGradient {
        val startColor = Color.argb(
            150,
            Color.red(speedometerColor),
            Color.green(speedometerColor),
            Color.blue(speedometerColor)
        )
        val color2 = Color.argb(
            220,
            Color.red(speedometerColor),
            Color.green(speedometerColor),
            Color.blue(speedometerColor)
        )
        val color3 = Color.argb(
            70,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val endColor = Color.argb(
            15,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val position = getOffsetSpeed() * (getEndDegree() - getStartDegree()) / 360f
        val sweepGradient = SweepGradient(
            size * .5f,
            size * .5f,
            intArrayOf(startColor, color2, speedometerColor, color3, endColor, startColor),
            floatArrayOf(0f, position * .5f, position, position, .99f, 1f)
        )
        val matrix = Matrix()
        matrix.postRotate(getStartDegree().toFloat(), size * .5f, size * .5f)
        sweepGradient.setLocalMatrix(matrix)
        return sweepGradient
    }

    private fun updateLargeSweep(): SweepGradient {
        val startColor = Color.argb(
            150,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val color2 = Color.argb(
            220,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val color3 = Color.argb(
            70,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val endColor = Color.argb(
            15,
            Color.red(centerCircleColor),
            Color.green(centerCircleColor),
            Color.blue(centerCircleColor)
        )
        val position = getOffsetSpeed() * (getEndDegree() - getStartDegree()) / 360f
        val sweepGradient = SweepGradient(
            size * .5f,
            size * .5f,
            intArrayOf(startColor, color2, centerCircleColor, color3, endColor, startColor),
            floatArrayOf(0f, position * .5f, position, position, .99f, 1f)
        )
        val matrix = Matrix()
        matrix.postRotate(getStartDegree().toFloat(), size * .5f, size * .5f)
        sweepGradient.setLocalMatrix(matrix)
        return sweepGradient
    }

    private fun updateRadial() {
        val centerColor = Color.argb(
            160,
            Color.red(pointerColor),
            Color.green(pointerColor),
            Color.blue(pointerColor)
        )
        val edgeColor = Color.argb(
            10,
            Color.red(pointerColor),
            Color.green(pointerColor),
            Color.blue(pointerColor)
        )
        val pointerGradient = RadialGradient(
            size * .5f,
            speedometerWidth * .5f + dpTOpx(8f) + padding.toFloat(),
            speedometerWidth * .5f + dpTOpx(8f),
            intArrayOf(centerColor, edgeColor),
            floatArrayOf(.4f, 1f),
            Shader.TileMode.CLAMP
        )
//        pointerBackPaint.shader = pointerGradient
    }

    fun getSpeedometerColor(): Int {
        return speedometerColor
    }

    fun setSpeedometerColor(speedometerColor: Int) {
        this.speedometerColor = speedometerColor
        if (isAttachedToWindow)
            invalidate()
    }

    fun getPointerColor(): Int {
        return pointerColor
    }

    fun setPointerColor(pointerColor: Int) {
        this.pointerColor = pointerColor
//        pointerPaint.color = pointerColor
        updateRadial()
        if (isAttachedToWindow)
            invalidate()
    }

    fun setIndictorColor() {
        indicator.color=speedometerColor
    }
}
