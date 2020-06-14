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


class NumberPicker : RelativeLayout {
    val TAG = "NumberPicker"
    lateinit var increase: ImageButton
    lateinit var decrease: ImageButton


    lateinit var numberTxt: TextView
    var minValue = 0

    var changeListener: ChangeListener? = null
    var currentValue = 0

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
        increase = findViewById<ImageButton>(R.id.increase)
        decrease = findViewById<ImageButton>(R.id.decrease)
        numberTxt = findViewById<EditText>(R.id.numberText)
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
                minValue = a?.getInt(R.styleable.NumberPicker_minValue, 0)!!
                currentValue = a.getInt(R.styleable.NumberPicker_value, 0)
                if (currentValue < minValue)
                    currentValue = minValue
                numberTxt.setText("$currentValue")
            } catch (e: Exception) {
                Log.e(TAG, "error", e)
            } finally {
                a?.recycle()
            }
        }
    }

    private fun increaseClick() {
        currentValue += 1
        numberTxt.setText("$currentValue")
        changeListener?.onIncrease(currentValue)
    }

    private fun decreaseClick() {
        if (currentValue <= minValue)
            return
        currentValue--
        numberTxt.setText("$currentValue")
        changeListener?.onDecrease(currentValue)
    }


    fun setValue(value: Int) {
        this.currentValue = value
        numberTxt.setText("$value")
    }

    fun getValue(): Int {
        return currentValue
    }

    interface ChangeListener {
        fun onIncrease(newValue: Int)
        fun onDecrease(newValue: Int)
    }
}