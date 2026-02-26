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
    private var keyLongClickListener: ((KeyData, android.view.View) -> Unit)? = null
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
    
    fun setOnKeyLongClickListener(listener: (KeyData, android.view.View) -> Unit) {
        this.keyLongClickListener = listener
    }
    
    fun getUrduTypeface(): android.graphics.Typeface? = urduTypeface

    private var currentLayoutHashCode: Int = 0

    fun renderLayout(rows: List<List<KeyData>>, isShifted: Boolean, isUrdu: Boolean = false) {
        val layoutHash = rows.hashCode()
        val requiresFullRedraw = currentLayoutHashCode != layoutHash || childCount == 0
        
        if (requiresFullRedraw) {
            removeAllViews()
            currentLayoutHashCode = layoutHash
            
            val container = if (rows.size > 5) {
                val sv = android.widget.ScrollView(context).apply {
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    isVerticalScrollBarEnabled = false
                }
                val ll = LinearLayout(context).apply { 
                    orientation = VERTICAL
                    layoutParams = android.widget.FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT, 
                        android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                sv.addView(ll)
                addView(sv)
                ll
            } else {
                this
            }

            for (rowParams in rows) {
                val rowLayout = LinearLayout(context).apply {
                    orientation = HORIZONTAL
                    layoutParams = if (rows.size > 5) {
                        LayoutParams(LayoutParams.MATCH_PARENT, 150) // Fixed height in pixels for scrolling rows
                    } else {
                        LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                    }
                }
                for (key in rowParams) {
                    val keyView = createKeyView(key, isShifted, isUrdu)
                    rowLayout.addView(keyView)
                }
                container.addView(rowLayout)
            }
        } else {
            // Fast Path: Layout hasn't changed structure, just update labels
            val container = if (rows.size > 5) {
                (getChildAt(0) as android.widget.ScrollView).getChildAt(0) as LinearLayout
            } else {
                this
            }
            
            for (i in 0 until container.childCount) {
                val rowLayout = container.getChildAt(i) as LinearLayout
                val rowData = rows[i]
                for (j in 0 until rowLayout.childCount) {
                    val keyContainer = rowLayout.getChildAt(j) as android.widget.FrameLayout
                    val key = rowData[j]
                    val mainLabel = keyContainer.getChildAt(0) as TextView
                    mainLabel.text = if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
                }
            }
        }
    }
    
    private var keyPopupWindow: PopupWindow? = null
    private var popupTextView: TextView? = null

    private fun showKeyPopup(keyView: android.view.View, label: String, isFunctional: Boolean = false) {
        if (label.isBlank() || isFunctional) return
        
        if (keyPopupWindow == null) {
            val popupLayout = android.widget.FrameLayout(context).apply {
                val bgDrawable = GradientDrawable().apply {
                    setColor(if (isDarkTheme) Color.parseColor("#4A4D51") else Color.parseColor("#FFFFFF"))
                    cornerRadius = 28f
                }
                background = bgDrawable
                elevation = 20f
                setPadding(16, 16, 16, 24)
            }

            popupTextView = TextView(context).apply {
                textSize = 42f
                setTextColor(if (isDarkTheme) Color.WHITE else Color.BLACK)
                gravity = Gravity.CENTER
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            }
            popupLayout.addView(popupTextView)

            keyPopupWindow = PopupWindow(popupLayout, 160, 200, false).apply {
                isClippingEnabled = false
            }
        }
        
        popupTextView?.text = label
        popupTextView?.typeface = if (label.any { it in '\u0600'..'\u06FF' }) urduTypeface else android.graphics.Typeface.DEFAULT
        
        val location = IntArray(2)
        keyView.getLocationInWindow(location)
        val viewX = location[0]
        val viewY = location[1]

        keyPopupWindow?.showAtLocation(
            keyView,
            Gravity.NO_GRAVITY,
            viewX + keyView.width / 2 - 80,
            viewY - 200 + 40
        )
    }

    private fun dismissKeyPopup() {
        keyPopupWindow?.dismiss()
    }

    private fun createKeyView(key: KeyData, isShifted: Boolean, isUrdu: Boolean): android.view.View {
        val container = android.widget.FrameLayout(context).apply {
            val lParams = LayoutParams(0, LayoutParams.MATCH_PARENT, key.weight)
            lParams.setMargins(6, 12, 6, 12)
            layoutParams = lParams
            
            val bgDrawable = GradientDrawable().apply {
                val regularBg = if (isDarkTheme) Color.parseColor("#292C31") else Color.WHITE
                val functionalBg = if (isDarkTheme) Color.parseColor("#3B3E43") else Color.parseColor("#DEE1E5")
                setColor(if (key.isFunctional) functionalBg else regularBg)
                cornerRadius = 20f // Pill-shaped modern feel
            }
            elevation = 3f // Very subtle drop shadow
            
            val rippleColor = android.content.res.ColorStateList.valueOf(Color.parseColor(if (isDarkTheme) "#55AAAAAA" else "#33000000"))
            background = android.graphics.drawable.RippleDrawable(rippleColor, bgDrawable, null)
            
            isClickable = true
            isFocusable = true
                    
            setOnTouchListener { v, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        val currentLabel = if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
                        showKeyPopup(this, currentLabel, key.isFunctional)
                    }
                    android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                        dismissKeyPopup()
                        if (isRepeating) {
                            isRepeating = false
                            repeatingKey = null
                            repeatHandler.removeCallbacks(repeatRunnable)
                        }
                    }
                }
                false
            }
                    
            setOnClickListener {
                if (!isRepeating) {
                    keyClickListener?.invoke(key)
                }
            }
                    
            setOnLongClickListener {
                if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_DELETE || key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SPACE) {
                    isRepeating = true
                    repeatingKey = key
                    repeatHandler.post(repeatRunnable)
                    true
                } else if (key.longPressOptions.isNotEmpty()) {
                    dismissKeyPopup()
                    keyLongClickListener?.invoke(key, this)
                    true
                } else {
                    false
                }
            }
        }
        
        val textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
        
        val mainTextView = TextView(context).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            text = if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
            setTextColor(textColor)
            gravity = Gravity.CENTER
            
            if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_NUMBERS || 
                key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_LANGUAGE_SWITCH || 
                key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_EMOJI) {
                typeface = android.graphics.Typeface.DEFAULT
                textSize = 15f
            } else if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SPACE) {
                typeface = if (isUrdu) urduTypeface else android.graphics.Typeface.DEFAULT
                textSize = 18f
            } else if (isUrdu && !key.isFunctional) {
                typeface = urduTypeface
                textSize = 30f
            } else {
                typeface = android.graphics.Typeface.DEFAULT
                textSize = 26f
            }
        }
        
        container.addView(mainTextView)
        
        // Secondary label hint for long-press
        if (key.longPressOptions.isNotEmpty() && !key.isFunctional) {
            val hintTextView = TextView(context).apply {
                val params = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP or Gravity.END
                )
                params.setMargins(0, 10, 14, 0)
                layoutParams = params
                text = key.longPressOptions[0]
                setTextColor(Color.parseColor(if (isDarkTheme) "#A0A0A0" else "#707070"))
                textSize = 11f
                if (isUrdu) {
                    typeface = urduTypeface
                }
            }
            container.addView(hintTextView)
        }
        
        return container
    }
}
