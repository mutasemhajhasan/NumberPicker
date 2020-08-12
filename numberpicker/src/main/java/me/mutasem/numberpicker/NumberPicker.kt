package me.mutasem.numberpicker

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.math.BigDecimal


class NumberPicker : RelativeLayout {
    val TAG = "NumberPicker"
    lateinit var increase: ImageButton
    lateinit var decrease: ImageButton


    lateinit var numberTxt: TextView
    var minValue: BigDecimal = BigDecimal.ZERO
    var currentStep = BigDecimal.ONE
    var changeListener: ChangeListener? = null
    var currentValue = BigDecimal.ZERO

    constructor(context: Context?) : super(context) {
        vInit(context, null, 0, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        vInit(context, attrs, 0, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        vInit(context, attrs, defStyleAttr, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        vInit(context, attrs, defStyleAttr, defStyleRes)
    }

    fun vInit(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {

        View.inflate(context, R.layout.number_picker, this)
        increase = findViewById(R.id.increase)
        decrease = findViewById(R.id.decrease)
        numberTxt = findViewById(R.id.numberText)
        increase.setOnClickListener { increaseClick() }
        decrease.setOnClickListener { decreaseClick() }
        if (attrs != null) {
            val a = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.NumberPicker,
                defStyleAttr,
                defStyleRes
            )
            try {
                if (a != null) {
                    val mv = a?.getString(R.styleable.NumberPicker_minValue)
                    if (mv != null && mv.isNotEmpty())
                        minValue = BigDecimal(mv)
                    val cs = a?.getString(R.styleable.NumberPicker_step)
                    if (cs != null && cs.isNotEmpty())
                        currentStep = BigDecimal(cs)
                    val cv = a?.getString(R.styleable.NumberPicker_value)
                    if (cv != null && cv.isNotEmpty())
                        currentValue = BigDecimal(cv)
                }
                if (minValue > currentValue)
                    currentValue = minValue
                numberTxt.setText("${currentValue.toString()}")
            } catch (e: Exception) {
                Log.e(TAG, "error", e)
            } finally {
                a?.recycle()
            }
        }
    }

    private fun increaseClick() {
        currentValue = currentValue.add(currentStep)
        numberTxt.setText("$currentValue")
        changeListener?.onIncrease(currentValue)
    }

    fun setStep(step: BigDecimal) {
        currentStep = step
    }

    fun getStep(): BigDecimal {
        return currentStep
    }

    private fun decreaseClick() {
        if (currentValue <= minValue)
            return
        currentValue = currentValue.minus(currentStep)
        numberTxt.setText("$currentValue")
        changeListener?.onDecrease(currentValue)
    }


    fun setValue(value: BigDecimal) {
        this.currentValue = value
        numberTxt.setText("$value")
    }

    fun getValue(): BigDecimal {
        return currentValue
    }

    interface ChangeListener {
        fun onIncrease(newValue: BigDecimal)
        fun onDecrease(newValue: BigDecimal)
    }

    fun setEditable(b: Boolean) {
        numberTxt.isEnabled = b
    }
}