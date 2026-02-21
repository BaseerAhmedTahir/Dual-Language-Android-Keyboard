package com.example.urduenglishkeyboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.PopupWindow
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.Gravity
import com.example.urduenglishkeyboard.data.AppDatabase
import com.example.urduenglishkeyboard.data.WordDao
import com.example.urduenglishkeyboard.data.WordEntity
import com.example.urduenglishkeyboard.keyboard.KeyData
import com.example.urduenglishkeyboard.keyboard.KeyboardLayouts
import com.example.urduenglishkeyboard.ui.CustomKeyboardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.view.HapticFeedbackConstants
import android.media.AudioManager
import android.content.res.Configuration
import android.graphics.Color
import kotlinx.coroutines.withContext

class UrduEnglishKeyboardService : InputMethodService() {
    
    private lateinit var keyboardView: CustomKeyboardView
    private lateinit var suggestion1: TextView
    private lateinit var suggestion2: TextView
    private lateinit var suggestion3: TextView
    
    private var isEnglish = true
    private var isShifted = false
    private var isEmoji = false
    
    private var popupWindow: PopupWindow? = null
    
    private val serviceJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private lateinit var wordDao: WordDao
    
    private var currentComposingText = StringBuilder()

    override fun onCreate() {
        super.onCreate()
        wordDao = AppDatabase.getDatabase(this).wordDao()
        seedDatabaseMockData()
    }

    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.keyboard_view, null)
        keyboardView = view.findViewById(R.id.custom_keyboard_view)
        suggestion1 = view.findViewById(R.id.suggestion1)
        suggestion2 = view.findViewById(R.id.suggestion2)
        suggestion3 = view.findViewById(R.id.suggestion3)
        
        applyTheme(view)
        
        keyboardView.setOnKeyClickListener { keyData ->
            handleKeyClick(keyData)
        }
        
        keyboardView.setOnKeyLongClickListener { keyData, keyViewTextView ->
            handleKeyLongClick(keyData, keyViewTextView)
        }
        
        setupSuggestionListeners()
        updateKeyboardLayout()
        return view
    }

    private fun applyTheme(view: View) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        
        val bgColor = if (isDark) Color.parseColor("#121212") else Color.parseColor("#E0E0E0")
        val suggestionBgColor = if (isDark) Color.parseColor("#1A1A1A") else Color.parseColor("#D0D0D0")
        val textColor = if (isDark) Color.WHITE else Color.BLACK

        view.setBackgroundColor(bgColor)
        view.findViewById<View>(R.id.suggestion_bar_layout)?.setBackgroundColor(suggestionBgColor)
        
        suggestion1.setTextColor(textColor)
        suggestion2.setTextColor(textColor)
        suggestion3.setTextColor(textColor)
        
        keyboardView.setTheme(isDark)
    }

    private fun setupSuggestionListeners() {
        val listener = View.OnClickListener { v ->
            val text = (v as TextView).text.toString()
            if (text.isNotEmpty()) {
                commitSuggestion(text)
            }
        }
        suggestion1.setOnClickListener(listener)
        suggestion2.setOnClickListener(listener)
        suggestion3.setOnClickListener(listener)
    }

    private fun seedDatabaseMockData() {
        coroutineScope.launch(Dispatchers.IO) {
            val testSuggestions = wordDao.getSuggestions("t", "en")
            if (testSuggestions.isEmpty()) {
                wordDao.insertWords(listOf(
                    WordEntity(word = "the", frequency = 100, language = "en"),
                    WordEntity(word = "this", frequency = 90, language = "en"),
                    WordEntity(word = "that", frequency = 85, language = "en"),
                    WordEntity(word = "there", frequency = 80, language = "en"),
                    WordEntity(word = "سلام", frequency = 100, language = "ur"),
                    WordEntity(word = "کیا", frequency = 90, language = "ur"),
                    WordEntity(word = "ہے", frequency = 85, language = "ur")
                ))
            }
        }
    }

    override fun onFinishInput() {
        super.onFinishInput()
        currentComposingText.clear()
        updateSuggestions(emptyList())
    }

    private fun handleKeyLongClick(keyData: KeyData, keyView: View) {
        if (keyData.longPressOptions.isEmpty()) return
        
        keyboardView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        
        val popupView = layoutInflater.inflate(R.layout.popup_key_options, null) as LinearLayout
        
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        val textColor = if (isDark) Color.WHITE else Color.BLACK

        for (option in keyData.longPressOptions) {
            val btn = TextView(this).apply {
                text = option
                textSize = 24f
                setPadding(32, 24, 32, 24)
                setTextColor(textColor)
                val typedArray = obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
                background = typedArray.getDrawable(0)
                typedArray.recycle()
                
                setOnClickListener {
                    currentInputConnection?.commitText(option, 1)
                    popupWindow?.dismiss()
                }
            }
            popupView.addView(btn)
        }

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 8f
            // Show above the key and slightly offset to center
            showAsDropDown(keyView, (keyView.width - popupView.measuredWidth) / 2, -keyView.height * 2)
        }
    }

    private fun handleKeyClick(keyData: KeyData) {
        val inputConnection = currentInputConnection ?: return
        
        keyboardView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        
        when (keyData.code) {
            KeyboardLayouts.CODE_DELETE -> {
                if (currentComposingText.isNotEmpty()) {
                    currentComposingText.deleteCharAt(currentComposingText.length - 1)
                    if (currentComposingText.isNotEmpty()) {
                        inputConnection.setComposingText(currentComposingText.toString(), 1)
                        querySuggestions()
                    } else {
                        inputConnection.commitText("", 1)
                        updateSuggestions(emptyList())
                    }
                } else {
                    inputConnection.deleteSurroundingText(1, 0)
                }
            }
            KeyboardLayouts.CODE_SHIFT -> {
                isShifted = !isShifted
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_LANGUAGE_SWITCH -> {
                isEnglish = !isEnglish
                isShifted = false
                isEmoji = false
                currentComposingText.clear()
                inputConnection.finishComposingText()
                updateSuggestions(emptyList())
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_EMOJI -> {
                isEmoji = !isEmoji
                updateKeyboardLayout()
            }
            KeyboardLayouts.CODE_SPACE -> {
                if (currentComposingText.isNotEmpty()) {
                    val topSuggestion = suggestion2.text.toString()
                    if (topSuggestion.isNotEmpty()) {
                        inputConnection.commitText(topSuggestion + " ", 1)
                    } else {
                        inputConnection.commitText(currentComposingText.toString() + " ", 1)
                    }
                    currentComposingText.clear()
                    updateSuggestions(emptyList())
                } else {
                    inputConnection.commitText(" ", 1)
                }
            }
            KeyboardLayouts.CODE_ENTER -> {
                if (currentComposingText.isNotEmpty()) {
                    inputConnection.commitText(currentComposingText.toString(), 1)
                    currentComposingText.clear()
                    updateSuggestions(emptyList())
                }
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
                inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
            }
            KeyboardLayouts.CODE_NUMBERS -> {
                // TODO: Switch to number/symbols layout
            }
            else -> {
                var textToCommit = if (isShifted && keyData.shiftLabel.isNotEmpty()) keyData.shiftLabel else keyData.label
                
                if (isEnglish && currentComposingText.isEmpty()) {
                    val textBeforeCursor = inputConnection.getTextBeforeCursor(2, 0)
                    if (textBeforeCursor.isNullOrEmpty() || textBeforeCursor.toString().endsWith(". ")) {
                        textToCommit = textToCommit.uppercase()
                    }
                }

                currentComposingText.append(textToCommit)
                inputConnection.setComposingText(currentComposingText.toString(), 1)
                querySuggestions()
                
                if (isShifted) {
                    isShifted = false
                    updateKeyboardLayout()
                }
            }
        }
    }

    private fun querySuggestions() {
        val prefix = currentComposingText.toString()
        if (prefix.isEmpty()) {
            updateSuggestions(emptyList())
            return
        }
        
        val lang = if (isEnglish) "en" else "ur"
        coroutineScope.launch {
            val suggestions = withContext(Dispatchers.IO) {
                wordDao.getSuggestions(prefix.lowercase(), lang)
            }
            updateSuggestions(suggestions.map { it.word })
        }
    }

    private fun updateSuggestions(words: List<String>) {
        suggestion1.text = words.getOrNull(1) ?: ""
        suggestion2.text = words.getOrNull(0) ?: "" // Top suggestion in the middle 
        suggestion3.text = words.getOrNull(2) ?: ""
    }

    private fun commitSuggestion(word: String) {
        val inputConnection = currentInputConnection ?: return
        inputConnection.commitText("$word ", 1)
        currentComposingText.clear()
        updateSuggestions(emptyList())
    }

    private fun updateKeyboardLayout() {
        val layout = if (isEmoji) {
            KeyboardLayouts.emojiLayout
        } else if (isEnglish) {
            KeyboardLayouts.englishQwerty
        } else {
            KeyboardLayouts.urduPhonetic
        }
        keyboardView.renderLayout(layout, isShifted, !isEnglish && !isEmoji)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
