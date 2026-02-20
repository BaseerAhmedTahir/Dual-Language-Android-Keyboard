package com.example.urduenglishkeyboard.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.urduenglishkeyboard.keyboard.KeyData
import android.graphics.drawable.GradientDrawable

class CustomKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var keyClickListener: ((KeyData) -> Unit)? = null
    private var isDarkTheme = false

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.parseColor("#EEEEEE")) // Default Light gray
    }

    fun setTheme(isDark: Boolean) {
        isDarkTheme = isDark
        setBackgroundColor(if (isDark) Color.parseColor("#1E1E1E") else Color.parseColor("#EEEEEE"))
    }

    fun setOnKeyClickListener(listener: (KeyData) -> Unit) {
        this.keyClickListener = listener
    }

    fun renderLayout(rows: List<List<KeyData>>, isShifted: Boolean) {
        removeAllViews()
        
        for (row in rows) {
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
            }
            
            for (key in row) {
                val keyView = TextView(context).apply {
                    val lParams = LayoutParams(0, LayoutParams.MATCH_PARENT, key.weight)
                    lParams.setMargins(6, 12, 6, 12)
                    layoutParams = lParams
                    
                    text = if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
                    gravity = Gravity.CENTER
                    val textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
                    setTextColor(textColor)
                    textSize = 24f
                    
                    val bgDrawable = GradientDrawable().apply {
                        val regularBg = if (isDarkTheme) Color.parseColor("#333333") else Color.WHITE
                        val functionalBg = if (isDarkTheme) Color.parseColor("#555555") else Color.parseColor("#D6D6D6")
                        setColor(if (key.isFunctional) functionalBg else regularBg)
                        cornerRadius = 16f // Rounded corners for keys
                    }
                    background = bgDrawable
                    
                    isClickable = true
                    isFocusable = true
                    
                    setOnClickListener {
                        keyClickListener?.invoke(key)
                    }
                }
                rowLayout.addView(keyView)
            }
            addView(rowLayout)
        }
    }
}
