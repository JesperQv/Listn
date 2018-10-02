package com.jesperqvarfordt.listn.common.view.waveview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import io.reactivex.disposables.CompositeDisposable


open class WaveView : View {

    private var numberOfWaves: Int = 0
    private var phase: Float = 0f
    private var amplitude: Float = 0f
    private var frequency: Float = 0f
    private var phaseShift: Float = 0f
    private var density: Float = 0f
    private var primaryWaveLineWidth: Float = 0f
    private var secondaryWaveLineWidth: Float = 0f
    private var bgColor: Int = 0
    private var waveColor: Int = 0
    private var xAxisPositionMultiplier: Float = 0f

    private var paint: Paint? = null
    private var path: Path? = null

    private var disposables = CompositeDisposable()

    constructor(context: Context) : super(context) {
        setUp(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        try {
            setUp(a)
        } finally {
            a.recycle()
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        try {
            setUp(a)
        } finally {
            a.recycle()
        }
    }

    private fun setUp(typedArray: TypedArray?) {
        if (typedArray != null) {
            numberOfWaves = typedArray.getInt(R.styleable.WaveView_waveNumberOfWaves,
                    defaultNumberOfWaves)
            frequency = typedArray.getFloat(R.styleable.WaveView_waveFrequency, defaultFrequency)
            amplitude = typedArray.getFloat(R.styleable.WaveView_waveAmplitude, defaultAmplitude)
            phaseShift = typedArray.getFloat(R.styleable.WaveView_wavePhaseShift, defaultPhaseShift)
            density = typedArray.getFloat(R.styleable.WaveView_waveDensity, defaultDensity)
            primaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_wavePrimaryLineWidth,
                    defaultPrimaryLineWidth)
            secondaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_waveSecondaryLineWidth,
                    defaultSecondaryLineWidth)
            bgColor = typedArray.getColor(R.styleable.WaveView_waveBackgroundColor,
                    defaultBackgroundColor)
            waveColor = typedArray.getColor(R.styleable.WaveView_waveColor, defaultWaveColor)
            xAxisPositionMultiplier = typedArray.getFloat(R.styleable.WaveView_waveXAxisPositionMultiplier,
                    defaultXAxisPositionMultiplier)
            boundXAxisPositionMultiplier()
        } else {
            this.numberOfWaves = defaultNumberOfWaves
            this.frequency = defaultFrequency
            this.amplitude = defaultAmplitude
            this.phaseShift = defaultPhaseShift
            this.density = defaultDensity
            this.primaryWaveLineWidth = defaultPrimaryLineWidth
            this.secondaryWaveLineWidth = defaultSecondaryLineWidth
            this.bgColor = defaultBackgroundColor
            this.waveColor = defaultWaveColor
            this.xAxisPositionMultiplier = defaultXAxisPositionMultiplier
        }

        paint = Paint()
        paint?.color = waveColor
        paint?.style = Paint.Style.FILL_AND_STROKE
        paint?.isAntiAlias = true

        path = Path()

    }

    private fun boundXAxisPositionMultiplier() {
        if (xAxisPositionMultiplier < 0) {
            xAxisPositionMultiplier = 0f
        } else if (xAxisPositionMultiplier > 1) {
            xAxisPositionMultiplier = 1f
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var va: ValueAnimator? = null
        disposables.add(App.instance.appComponent.subscribeToPlayerInfoUseCase()
                .execute()
                .subscribe { data ->
                    if (data.isPlaying) {
                        va?.cancel()
                        va = ValueAnimator.ofFloat(amplitude, defaultAmplitude)
                        va?.duration = 1000
                        va?.addUpdateListener { valueAnimator ->
                            amplitude = valueAnimator.animatedValue as Float
                        }
                        va?.start()
                    } else {
                        va?.cancel()
                        va = ValueAnimator.ofFloat(amplitude, 5.0f)
                        va?.duration = 1000
                        va?.addUpdateListener { valueAnimator ->
                            amplitude = valueAnimator.animatedValue as Float
                        }
                        va?.start()
                    }
                })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables.clear()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(bgColor)

        // Prepare common values
        val xAxisPosition = height * xAxisPositionMultiplier
        val width = width
        val mid = width / 2

        for (i in 0 until numberOfWaves) {
            // Prepare variables for this wave
            paint?.strokeWidth = if (i == 0) primaryWaveLineWidth else secondaryWaveLineWidth
            val progress = 1.0f - i.toFloat() / this.numberOfWaves
            val normedAmplitude = (1.5f * progress - 0.5f) * this.amplitude

            // Prepare path for this wave
            path?.reset()
            var x = 0f
            while (x < width + density) {
                // We use a parable to scale the sinus wave, that has its peak in the middle of
                // the view.
                val scaling = (-Math.pow((1 / mid * (x - mid)).toDouble(), 2.0) + 1).toFloat()

                val y = ((scaling.toDouble() * amplitude.toDouble() * normedAmplitude.toDouble()
                        * Math.sin(2.0 * Math.PI * (x / width).toDouble() * frequency.toDouble() + phase * (i + 1))) + xAxisPosition).toFloat()

                if (x == 0f) {
                    path?.moveTo(x, y)
                } else {
                    path?.lineTo(x, y)
                }
                x += density
            }
            path?.lineTo(x, height.toFloat())
            path?.lineTo(0f, height.toFloat())
            path?.close()

            // Set opacity for this wave fill
            paint?.alpha = if (i == 0) 255 else 255 / (i + 1)

            // Draw wave
            if (path != null && paint != null){
                canvas.drawPath(path!!, paint!!)
            }
        }

        this.phase += phaseShift
        invalidate()
    }

    companion object {
        private const val defaultNumberOfWaves = 5
        private const val defaultFrequency = 2.0f
        private const val defaultAmplitude = 10.25f
        private const val defaultPhaseShift = -0.05f
        private const val defaultDensity = 5.0f
        private const val defaultPrimaryLineWidth = 3.0f
        private const val defaultSecondaryLineWidth = 1.0f
        private const val defaultBackgroundColor = Color.BLACK
        private const val defaultWaveColor = Color.WHITE
        private const val defaultXAxisPositionMultiplier = 0.5f
    }
}