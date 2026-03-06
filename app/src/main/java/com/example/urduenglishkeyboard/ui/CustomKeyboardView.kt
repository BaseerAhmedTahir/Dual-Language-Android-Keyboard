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
    
    private var enterKeyLabel = "↵"
    private var cursorMoveListener: ((Int) -> Unit)? = null
    private var swipeDeleteListener: (() -> Unit)? = null

    fun setEnterKeyLabel(label: String) {
        enterKeyLabel = label
    }

    fun setOnCursorMoveListener(listener: (Int) -> Unit) {
        this.cursorMoveListener = listener
    }

    fun setOnSwipeDeleteListener(listener: () -> Unit) {
        this.swipeDeleteListener = listener
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
                    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                    isVerticalScrollBarEnabled = false
                }
                val ll = LinearLayout(context).apply { 
                    orientation = VERTICAL
                }
                sv.addView(ll)
                addView(sv)
                ll
            } else {
                this
            }

            for (i in rows.indices) {
                val rowParams = rows[i]
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
                
                if (rows.size > 5 && i >= rows.size - 1) {
                    this.addView(rowLayout) // Sticky tabs and controls
                } else {
                    container.addView(rowLayout) // Scrollable emojis
                }
            }
        } else {
            // Fast Path: Layout hasn't changed structure, just update labels
            var globalRowIndex = 0
            if (rows.size > 5) {
                val sv = getChildAt(0) as android.widget.ScrollView
                val scrollingContainer = sv.getChildAt(0) as LinearLayout
                for (i in 0 until scrollingContainer.childCount) {
                    updateRowLabels(scrollingContainer.getChildAt(i) as LinearLayout, rows[globalRowIndex++], isShifted)
                }
                for (i in 1 until childCount) {
                    updateRowLabels(getChildAt(i) as LinearLayout, rows[globalRowIndex++], isShifted)
                }
            } else {
                for (i in 0 until childCount) {
                    updateRowLabels(getChildAt(i) as LinearLayout, rows[globalRowIndex++], isShifted)
                }
            }
        }
    }

    private fun updateRowLabels(rowLayout: LinearLayout, rowData: List<KeyData>, isShifted: Boolean) {
        for (j in 0 until rowLayout.childCount) {
            val keyContainer = rowLayout.getChildAt(j) as android.widget.FrameLayout
            val key = rowData[j]
            val childView = keyContainer.getChildAt(0)
            if (childView is TextView) {
                val actualLabel = if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_ENTER) enterKeyLabel else if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
                childView.text = actualLabel
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
                    setColor(if (isDarkTheme) Color.parseColor("#383C41") else Color.parseColor("#FFFFFF"))
                    cornerRadius = 32f
                    setStroke(2, if (isDarkTheme) Color.parseColor("#4A4D51") else Color.parseColor("#E0E0E0"))
                }
                background = bgDrawable
                elevation = 24f
                setPadding(24, 24, 24, 32)
            }

            popupTextView = TextView(context).apply {
                textSize = 44f
                setTextColor(if (isDarkTheme) Color.WHITE else Color.BLACK)
                gravity = Gravity.CENTER
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            }
            popupLayout.addView(popupTextView)

            keyPopupWindow = PopupWindow(popupLayout, 180, 220, false).apply {
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
        val actualLabel = if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_ENTER) enterKeyLabel else if (isShifted && key.shiftLabel.isNotEmpty()) key.shiftLabel else key.label
        val container = android.widget.FrameLayout(context).apply {
            val lParams = LayoutParams(0, LayoutParams.MATCH_PARENT, key.weight)
            lParams.setMargins(6, 8, 6, 14) // Adjusted layout margin to give keys more breathing room
            layoutParams = lParams
            
            // Premium color palette
            val regularBgColor = if (isDarkTheme) Color.parseColor("#34373C") else Color.parseColor("#FFFFFF")
            val functionalBgColor = if (isDarkTheme) Color.parseColor("#26282B") else Color.parseColor("#D4D8DD")
            val pressedBgColor = if (isDarkTheme) Color.parseColor("#4A4D54") else Color.parseColor("#E4E8ED")
            
            val bgDrawable = GradientDrawable().apply {
                setColor(if (key.isFunctional) functionalBgColor else regularBgColor)
                cornerRadius = 24f // Sleeker pill/rounded-rect shape
            }
            
            // Add a subtle bottom layer to simulate 3D depth, exactly like modern keyboards do
            val layerDrawable = android.graphics.drawable.LayerDrawable(arrayOf(
                GradientDrawable().apply {
                    setColor(if (isDarkTheme) Color.parseColor("#151618") else Color.parseColor("#B0B5BC"))
                    cornerRadius = 24f
                },
                bgDrawable
            ))
            layerDrawable.setLayerInset(1, 0, 0, 0, 6) // Inset the top layer by 6px from the bottom

            elevation = if (isDarkTheme) 2f else 3f // Very subtle drop shadow
            
            // Keep ripple for fallback, but we manually color change for instant response
            val rippleColor = android.content.res.ColorStateList.valueOf(Color.parseColor(if (isDarkTheme) "#55AAAAAA" else "#33000000"))
            background = android.graphics.drawable.RippleDrawable(rippleColor, layerDrawable, null)
            
            isClickable = true
            isFocusable = true
            
            var startX = 0f
            var isSwiping = false
                    
            setOnTouchListener { v, event ->
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX
                        isSwiping = false
                        bgDrawable.setColor(pressedBgColor)
                        v.invalidate()
                        showKeyPopup(v, actualLabel, key.isFunctional)
                    }
                    android.view.MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - startX
                        if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SPACE) {
                            if (Math.abs(deltaX) > 40f) { // Threshold for cursor move
                                isSwiping = true
                                if (deltaX > 0) {
                                    cursorMoveListener?.invoke(1)
                                } else {
                                    cursorMoveListener?.invoke(-1)
                                }
                                startX = event.rawX // Reset start to allow continuous sliding
                            }
                        } else if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_DELETE) {
                            if (deltaX < -50f && !isSwiping) { // Threshold for swipe delete left
                                isSwiping = true
                                swipeDeleteListener?.invoke()
                            }
                        }
                    }
                    android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                        bgDrawable.setColor(if (key.isFunctional) functionalBgColor else regularBgColor)
                        v.invalidate()
                        dismissKeyPopup()
                        if (isRepeating) {
                            isRepeating = false
                            repeatingKey = null
                            repeatHandler.removeCallbacks(repeatRunnable)
                        }
                        if (isSwiping && event.action == android.view.MotionEvent.ACTION_UP) {
                            return@setOnTouchListener true // Suppress standard click if we swiped
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
        
        val isIconKey = key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SHIFT ||
                        key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_DELETE ||
                        key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_LANGUAGE_SWITCH ||
                        key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_EMOJI ||
                        key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_VOICE
        
        if (isIconKey) {
            val iconView = android.widget.ImageView(context).apply {
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                ).apply {
                    setMargins(0, 0, 0, 6)
                }
                
                // Special tint logic
                val iconTint = if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_LANGUAGE_SWITCH) {
                    if (isDarkTheme) Color.parseColor("#A8C7FA") else Color.parseColor("#0B57D0") // Blue Globe
                } else {
                    textColor // Standard color for Shift and Delete
                }
                setColorFilter(iconTint, android.graphics.PorterDuff.Mode.SRC_IN)
                
                val drawableRes = when (key.code) {
                    com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_SHIFT -> R.drawable.ic_keyboard_shift
                    com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_DELETE -> R.drawable.ic_keyboard_delete
                    com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_LANGUAGE_SWITCH -> R.drawable.ic_keyboard_language
                    com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_EMOJI -> R.drawable.ic_keyboard_emoji
                    com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_VOICE -> R.drawable.ic_keyboard_voice
                    else -> 0
                }
                
                if (drawableRes != 0) {
                    setImageResource(drawableRes)
                }
            }
            container.addView(iconView)
        } else {
            val mainTextView = TextView(context).apply {
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                ).apply {
                    setMargins(0, 0, 0, 6) // Adjust center visually to account for 3D depth inset
                }
                text = actualLabel
                if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_ENTER) {
                    val enterColor = if (isDarkTheme) Color.parseColor("#8AB4F8") else Color.parseColor("#1A73E8")
                    setTextColor(enterColor)
                } else {
                    setTextColor(textColor)
                }
                gravity = Gravity.CENTER
                
                if (key.code == com.example.urduenglishkeyboard.keyboard.KeyboardLayouts.CODE_NUMBERS || 
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
        }
        
        // Secondary label hint for long-press
        if (key.longPressOptions.isNotEmpty() && !key.isFunctional) {
            val hintTextView = TextView(context).apply {
                val params = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP or Gravity.END
                )
                params.setMargins(0, 8, 16, 0)
                layoutParams = params
                text = key.longPressOptions[0]
                setTextColor(Color.parseColor(if (isDarkTheme) "#9AA0A6" else "#5F6368"))
                textSize = 10.5f // Slightly smaller hint text
                if (isUrdu) {
                    typeface = urduTypeface
                }
            }
            container.addView(hintTextView)
        }
        
        return container
    }
}
