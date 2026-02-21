package com.example.urduenglishkeyboard.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.PopupWindow
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.urduenglishkeyboard.keyboard.KeyData
import android.graphics.drawable.GradientDrawable
import com.example.urduenglishkeyboard.R

class CustomKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var keyClickListener: ((KeyData) -> Unit)? = null
    private var isDarkTheme = false
    private var keyLongClickListener: ((KeyData, TextView) -> Unit)? = null
    private var urduTypeface: android.graphics.Typeface? = null
    
    // Auto-repeat helpers
    private var isRepeating = false
    private val repeatHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private var repeatingKey: KeyData? = null
    
    private val repeatRunnable = object : Runnable {
        override fun run() {
            repeatingKey?.let {
                keyClickListener?.invoke(it)
                repeatHandler.postDelayed(this, 50) // 50ms interval for fast deletion
            }
        }
    }

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#EEEEEE")) // Default Light gray
        Thread {
            try {
                val tf = android.graphics.Typeface.createFromAsset(context.assets, "fonts/jameel_noori_nastaliq.ttf")
                post {
                    urduTypeface = tf
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun setTheme(isDark: Boolean) {
        isDarkTheme = isDark
        setBackgroundColor(if (isDark) Color.parseColor("#1E1E1E") else Color.parseColor("#EEEEEE"))
    }

    fun setOnKeyClickListener(listener: (KeyData) -> Unit) {
        this.keyClickListener = listener
    }
    
    fun setOnKeyLongClickListener(listener: (KeyData, TextView) -> Unit) {
        this.keyLongClickListener = listener
    }

    fun renderLayout(rows: List<List<KeyData>>, isShifted: Boolean, isUrdu: Boolean = false) {
        removeAllViews()
        
        for (row in rows) {
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
            }
            
            for (key in row) {
                val keyView = TextView(context).apply {
                    val lParams = LayoutParams(0, LayoutParams.MATCH_PARENT, key.weight)
                    lParams.setMargins(8, 12, 8, 12)
                    layoutParams = lParams
                    
                    text = if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
                    gravity = Gravity.CENTER
                    val textColor = if (isDarkTheme) Color.WHITE else Color.parseColor("#1F1F1F")
                    setTextColor(textColor)
                    
                    if (isUrdu && !key.isFunctional) {
                        typeface = urduTypeface
                        textSize = 28f // Nastaliq requires larger text
                    } else {
                        typeface = android.graphics.Typeface.DEFAULT
                        textSize = 24f
                    }
                    
                    val bgDrawable = GradientDrawable().apply {
                        val regularBg = if (isDarkTheme) Color.parseColor("#3C4043") else Color.WHITE
                        val functionalBg = if (isDarkTheme) Color.parseColor("#2E3134") else Color.parseColor("#D2D6DA")
                        setColor(if (key.isFunctional) functionalBg else regularBg)
                        cornerRadius = 24f // Softer, more modern rounded corners
                    }
                    val rippleColor = android.content.res.ColorStateList.valueOf(Color.parseColor(if (isDarkTheme) "#55AAAAAA" else "#33000000"))
                    background = android.graphics.drawable.RippleDrawable(rippleColor, bgDrawable, null)
                    elevation = 6f // Adds a subtle drop shadow to buttons
                    
                    isClickable = true
                    isFocusable = true
                    
                    setOnTouchListener { v, event ->
                        when (event.action) {
                            android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                                if (isRepeating) {
                                    isRepeating = false
                                    repeatingKey = null
                                    repeatHandler.removeCallbacks(repeatRunnable)
                                }
                            }
                        }
                        false // Allow the TextView to handle click and ripples normally
                    }
                    
                    setOnClickListener {
                        if (!isRepeating) { // Do not trigger an extra click if auto-repeat ran
                            keyClickListener?.invoke(key)
                        }
                    }
                    
                    setOnLongClickListener {
                        if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_DELETE || key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SPACE) {
                            // Start auto-repeating for backspace and space
                            isRepeating = true
                            repeatingKey = key
                            repeatHandler.post(repeatRunnable)
                            true
                        } else if (key.longPressOptions.isNotEmpty()) {
                            keyLongClickListener?.invoke(key, this)
                            true // Consume the long click event
                        } else {
                            false // Allow normal click
                        }
                    }
                }
                rowLayout.addView(keyView)
            }
            addView(rowLayout)
        }
    }
}
